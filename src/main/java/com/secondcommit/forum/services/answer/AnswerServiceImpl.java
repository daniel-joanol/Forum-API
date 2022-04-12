package com.secondcommit.forum.services.answer;

import com.secondcommit.forum.dto.AnswerDto;
import com.secondcommit.forum.entities.*;
import com.secondcommit.forum.repositories.AnswerRepository;
import com.secondcommit.forum.repositories.PostRepository;
import com.secondcommit.forum.repositories.UserRepository;
import com.secondcommit.forum.security.payload.MessageResponse;
import com.secondcommit.forum.services.cloudinary.CloudinaryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * Implementation of the Answer service interface
 */
@Service
public class AnswerServiceImpl implements AnswerService{

    @Autowired
    private final AnswerRepository answerRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudinaryServiceImpl cloudinary;

    public AnswerServiceImpl(AnswerRepository answerRepository, PostRepository postRepository,
                             UserRepository userRepository, CloudinaryServiceImpl cloudinary) {
        this.answerRepository = answerRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.cloudinary = cloudinary;
    }

    /**
     * Method to add answer
     * @param answerDto (content)
     * @param author (gets the username from the jwt token)
     * @return ResponseEntity (ok: answer, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> addAnswer(Long post_id, AnswerDto answerDto, String author) {

        //Gets post
        Optional<Post> postOpt = postRepository.findById(post_id);

        //Gets user
        Optional<User> userOpt = userRepository.findByUsername(author);

        //Upload images to Cloudinary
        List<File> files = new ArrayList<>();

        if (answerDto.getFiles() != null && answerDto.getFiles().length > 0){
            for (MultipartFile image : answerDto.getFiles()){
                //Saves image in Cloudinary
                try {
                    File photo = cloudinary.uploadImage(image);
                    files.add(photo);
                } catch (Exception e){
                    System.err.println("Error: " + e.getMessage());
                    return ResponseEntity.badRequest()
                            .body(new MessageResponse("Upload failed"));
                }
            }
        }

        //Creates answer
        Answer answer = new Answer(answerDto.getContent(), userOpt.get(), files, postOpt.get());
        postOpt.get().getAnswers().add(answer);
        answerRepository.save(answer);
        postRepository.save(postOpt.get());

        return ResponseEntity.ok(answer.getDtoFromAnswer());
    }

    /**
     * Method to get answer
     * @param id
     * @return ResponseEntity (ok: answer, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> getAnswer(Long id) {

        Optional<Answer> answerOpt = answerRepository.findById(id);
        return ResponseEntity.ok(answerOpt.get().getDtoFromAnswer());

    }

    /**
     * Method to update the answer
     * @param id
     * @param answerDto (content)
     * @param username (gets from the jwt token)
     * @return ResponseEntity (ok: answer, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> updateAnswer(Long id, AnswerDto answerDto, String username) {

        //Gets post
        Optional<Answer> answerOpt = answerRepository.findById(id);

        //Gets user
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (answerOpt.get().getAuthor() != userOpt.get())
            return ResponseEntity.badRequest().body(new MessageResponse("The answer doesn't belong to this user"));

        //If it already has files, removes them
        if (answerOpt.get().getFiles() != null) {
            for (File file : answerOpt.get().getFiles()){

                try {
                    Boolean destroyed = cloudinary.deleteFile(file.getCloudinaryId());
                    if (destroyed) answerOpt.get().getFiles().remove(file);

                } catch (IOException e){
                    System.err.println("Error: " + e.getMessage());
                }
            }
        }

        //Upload images to Cloudinary
        if (answerDto.getFiles() != null){
            for (MultipartFile image : answerDto.getFiles()){
                //Saves image in Cloudinary
                try {
                    File photo = cloudinary.uploadImage(image);
                    answerOpt.get().getFiles().add(photo);
                } catch (Exception e){
                    System.err.println("Error: " + e.getMessage());
                    return ResponseEntity.badRequest()
                            .body(new MessageResponse("Upload failed"));
                }
            }
        }

        answerOpt.get().setContent(answerDto.getContent());
        answerRepository.save(answerOpt.get());

        return ResponseEntity.ok(answerOpt.get().getDtoFromAnswer());
    }

    /**
     * Method to delete the answer. Only authors and admins are allowed
     * @param id
     * @param username (gets from the jwt token)
     * @return ResponseEntity (ok: answer, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> deleteAnswer(Long id, String username) {

        //Gets answer
        Optional<Answer> answerOpt = answerRepository.findById(id);

        //Gets user
        Optional<User> userOpt = userRepository.findByUsername(username);

        //Gets the post that this answer belongs to
        Post post = answerOpt.get().getPost();

        //Tests if the user is allowed to edit this post (only authors of the post or answer, and admins can do it)
        if (answerOpt.get().getAuthor() != userOpt.get() && post.getAuthor() != userOpt.get()){

            boolean isAdmin = false;

            for (Role role  : userOpt.get().getRoles()){
                if (role.getName().equalsIgnoreCase("ADMIN")) isAdmin = true;
            }

            if (!isAdmin)
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("The user " + username + " is not allowed to update the answer"));
        }

        //Remove files
        if (answerOpt.get().getFiles() != null){
            for (File file : answerOpt.get().getFiles()){
                try {
                    Boolean destroyed = cloudinary.deleteFile(file.getCloudinaryId());
                    if (destroyed) answerOpt.get().getFiles().remove(file);

                } catch (IOException e){
                    System.err.println("Error: " + e.getMessage());
                }
            }
        }

        //Removes answer from post
        post.getAnswers().remove(answerOpt.get());
        postRepository.save(post);
        answerRepository.delete(answerOpt.get());

        return ResponseEntity.ok().body(new MessageResponse("Answer " + id + " deleted with success"));
    }

    /**
     * Method to add a like to the answer. If the user has already liked the answer, just removes the like
     * @param id
     * @param username (gets from the jwt token)
     * @return ResponseEntity (ok: totalLikes, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> like(Long id, String username) {

        //Gets answer
        Optional<Answer> answerOpt = answerRepository.findById(id);

        //Gets user
        Optional<User> userOpt = userRepository.findByUsername(username);

        //Tests it the user hasn't already liked the post
        boolean alreadyLiked = false;

        for (User user : answerOpt.get().getUsersWhoLike()){
            if (userOpt.get() == user) alreadyLiked = true;
        }

        if (alreadyLiked){
            //Removes like
            answerOpt.get().getUsersWhoLike().remove(userOpt.get());
        } else {

            //Adds like
            answerOpt.get().getUsersWhoLike().add(userOpt.get());

            //Removes from dislike
            for (User user : answerOpt.get().getUsersWhoDislike()){
                if (userOpt.get() == user) answerOpt.get().getUsersWhoDislike().remove(userOpt.get());
            }

        }

        answerOpt.get().refreshLikes();

        answerRepository.save(answerOpt.get());

        return ResponseEntity.ok(new MessageResponse("Likes: " + answerOpt.get().getTotalLikes()));
    }

    /**
     * Method to add a dislike to the answer. If the user has already disliked the answer, just removes the dislike
     * @param id
     * @param username (gets from the jwt token)
     * @return ResponseEntity(ok: totalLikes, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> dislike(Long id, String username) {

        //Gets answer
        Optional<Answer> answerOpt = answerRepository.findById(id);

        //Gets user
        Optional<User> userOpt = userRepository.findByUsername(username);

        //Tests it the user hasn't already disliked the post
        boolean alreadyDisliked = false;

        for (User user : answerOpt.get().getUsersWhoDislike()){
            if (userOpt.get() == user) alreadyDisliked = true;
        }

        if (alreadyDisliked){
            //Removes dislike
            answerOpt.get().getUsersWhoDislike().remove(userOpt.get());
        } else {

            //Adds dislike
            answerOpt.get().getUsersWhoDislike().add(userOpt.get());

            //Removes from like
            for (User user : answerOpt.get().getUsersWhoLike()){
                if (userOpt.get() == user) answerOpt.get().getUsersWhoLike().remove(userOpt.get());
            }
        }

        answerOpt.get().refreshLikes();

        answerRepository.save(answerOpt.get());

        return ResponseEntity.ok(new MessageResponse("Dislikes: " + answerOpt.get().getTotalDislikes()));
    }

    /**
     * Method to fix or unfix a post
     * @param id
     * @return ResponseEntity (ok: isFixed, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> fix(Long id) {

        //Gets the answer
        Optional<Answer> answerOpt = answerRepository.findById(id);

        if (answerOpt.isEmpty())
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        //Fix or unfix the answer
        if (answerOpt.get().getFixed()){
            answerOpt.get().setFixed(false);
        } else {
            answerOpt.get().setFixed(true);
        }

        answerRepository.save(answerOpt.get());

        return ResponseEntity.ok(new MessageResponse("Fixed: " + answerOpt.get().getFixed()));
    }
}
