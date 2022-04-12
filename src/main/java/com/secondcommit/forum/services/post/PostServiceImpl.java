package com.secondcommit.forum.services.post;

import com.secondcommit.forum.entities.*;
import com.secondcommit.forum.entities.Module;
import com.secondcommit.forum.repositories.ModuleRepository;
import com.secondcommit.forum.services.answer.AnswerServiceImpl;
import com.secondcommit.forum.services.cloudinary.CloudinaryServiceImpl;
import com.secondcommit.forum.dto.PostDto;
import com.secondcommit.forum.repositories.PostRepository;
import com.secondcommit.forum.repositories.UserRepository;
import com.secondcommit.forum.security.payload.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * Implementation of the Post service interface
 */
@Service
public class PostServiceImpl implements PostService{

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudinaryServiceImpl cloudinary;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private AnswerServiceImpl answerService;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, CloudinaryServiceImpl cloudinary,
                           ModuleRepository moduleRepository, AnswerServiceImpl answerService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.cloudinary = cloudinary;
        this.moduleRepository = moduleRepository;
        this.answerService = answerService;
    }

    /**
     * Method to add post. Checks if the user has access to the subject the contains this post
     * @param postDto (title and content)
     * @param author (gets the username from the jwt token)
     * @return ResponseEntity (ok: post, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> addPost(Long id, PostDto postDto, String author) {

        //Tests if the post title already exists by title
        Optional<Post> postOpt = postRepository.findByTitle(postDto.getTitle());

        if (postOpt.isPresent())
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("This post title " + postDto.getTitle() + " is already being used"));

        //Gets user from author
        Optional<User> userOpt = userRepository.findByUsername(author);

        //Gets module
        Optional<Module> moduleOpt = moduleRepository.findById(id);

        //Gets subject
        Subject subject = moduleOpt.get().getSubject();

        //Checks if the user has access to the subject
        if(!userOpt.get().getHasAccess().contains(subject)){
            return ResponseEntity.badRequest().body(new MessageResponse("The user doesn't have access to the subject"));
        }

        //Upload images to Cloudinary
        if (postDto.getFiles() != null){
            for (MultipartFile image : postDto.getFiles()){
                //Saves image in Cloudinary
                try {
                    File photo = cloudinary.uploadImage(image);
                    postOpt.get().getFiles().add(photo);
                } catch (Exception e){
                    System.err.println("Error: " + e.getMessage());
                    return ResponseEntity.badRequest()
                            .body(new MessageResponse("Upload failed"));
                }
            }
        }

        //Creates the new post
        Post post = new Post(userOpt.get(), postDto.getTitle(), postDto.getContent(), moduleOpt.get());
        moduleOpt.get().getPosts().add(post);
        moduleOpt.get().refreshTotalQuestions();
        postRepository.save(post);
        moduleRepository.save(moduleOpt.get());

        return ResponseEntity.ok(post.getDtoFromPost());
    }

    /**
     * Method to get the post. Checks if the user has access to the subject the contains this post
     * @param id
     * @return ResponseEntity (ok: post, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> getPost(Long id, String username) {

        //Gets post
        Optional<Post> postOpt = postRepository.findById(id);

        //Gets module
        Module module = postOpt.get().getModule();

        //Gets subject
        Subject subject = module.getSubject();

        //Checks if the user has access to the subject
        Optional<User> userOpt = userRepository.findByUsername(username);

        if(!userOpt.get().getHasAccess().contains(subject)){
            return ResponseEntity.badRequest().body(new MessageResponse("The user doesn't have access to the post"));
        }

        return ResponseEntity.ok(postOpt.get().getDtoFromPost());
    }

    /**
     * Method to update the user. Only authors and admins are allowed
     * @param id
     * @param postDto (title and content)
     * @param username (takes the username from the jwt token)
     * @return ResponseEntity (ok: post, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> updatePost(Long id, PostDto postDto, String username) {

        //Gets post
        Optional<Post> postOpt = postRepository.findById(id);

        //Gets user
        Optional<User> userOpt = userRepository.findByUsername(username);

        //Tests if the user is allowed to edit this post (only authors and admins can do it)
        //If the user isn't the one trying to update, checks to see if the user is ADMIN
        if (postOpt.get().getAuthor() != userOpt.get()){

            boolean isAdmin = false;

            for (Role role  : userOpt.get().getRoles()){
                if (role.getName().equalsIgnoreCase("ADMIN")) isAdmin = true;
            }

            if (!isAdmin)
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("The user " + username + " is not allowed to update the post " ));
        }

        //Uploads images to Cloudinary
        if (postDto.getFiles() != null){

            //Removes old images
            for (File file : postOpt.get().getFiles()){

                try {
                    Boolean destroyed = cloudinary.deleteFile(file.getCloudinaryId());
                    if (destroyed) postOpt.get().getFiles().remove(file);

                } catch (IOException e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }

            //Adds the new images
            for (MultipartFile image : postDto.getFiles()){
                //Saves image in Cloudinary
                try {
                    File photo = cloudinary.uploadImage(image);
                    postOpt.get().getFiles().add(photo);
                } catch (Exception e){
                    System.err.println("Error: " + e.getMessage());
                    return ResponseEntity.badRequest()
                            .body(new MessageResponse("Upload failed"));
                }
            }
        }

        //Updates the post
        if (postDto.getTitle() != null)
            postOpt.get().setTitle(postDto.getTitle());

        if (postDto.getContent() != null)
            postOpt.get().setContent(postDto.getContent());

        postRepository.save(postOpt.get());

        return ResponseEntity.ok(postOpt.get().getDtoFromPost());
    }

    /**
     * Method to delete the post. Only authors and admins are allowed
     * @param id
     * @param username (gets the username from the jwt token)
     * @return ResponseEntity (ok: post, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> deletePost(Long id, String username) {

        //Gets post
        Optional<Post> postOpt = postRepository.findById(id);

        //Gets user
        Optional<User> userOpt = userRepository.findByUsername(username);

        //Tests if the user is allowed to edit this post (only authors and admins can do it)
        if (postOpt.get().getAuthor() != userOpt.get()){

            boolean isAdmin = false;

            for (Role role  : userOpt.get().getRoles()){
                if (role.getName().equalsIgnoreCase("ADMIN")) isAdmin = true;
            }

            if (!isAdmin)
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("The user " + username + " is not allowed to update the post " ));
        }

        //Removes old images
        if (postOpt.get().getFiles() != null){

            for (File file : postOpt.get().getFiles()){

                try {
                    Boolean destroyed = cloudinary.deleteFile(file.getCloudinaryId());
                    if (destroyed) postOpt.get().getFiles().remove(file);

                } catch (IOException e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
        }

        //Removes Post from Module
        Module module = postOpt.get().getModule();
        module.getPosts().remove(postOpt.get());
        module.refreshTotalQuestions();
        moduleRepository.save(module);

        postRepository.delete(postOpt.get());

        return ResponseEntity.ok().body(new MessageResponse("Post " + id + " deleted with success"));
    }

    /**
     * Method to add a like to the post. If the user has already liked the post, just removes the like
     * @param id
     * @param username (gets from the jwt token)
     * @return ResponseEntity(ok: totalLikes, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> like(Long id, String username) {

        //Gets post
        Optional<Post> postOpt = postRepository.findById(id);

        //Gets user
        Optional<User> userOpt = userRepository.findByUsername(username);

        //Tests it the user hasn't already liked the post
        boolean alreadyLiked = false;

        for (User user : postOpt.get().getUsersWhoLike()){
            if (userOpt.get() == user) alreadyLiked = true;
        }

        if (alreadyLiked){
            //Removes like
            postOpt.get().getUsersWhoLike().remove(userOpt.get());
        } else {

            //Adds like
            postOpt.get().getUsersWhoLike().add(userOpt.get());

            //Removes from dislike
            for (User user : postOpt.get().getUsersWhoDislike()){
                if (userOpt.get() == user) postOpt.get().getUsersWhoDislike().remove(userOpt.get());
            }

        }

        postOpt.get().refreshLikes();

        postRepository.save(postOpt.get());

        return ResponseEntity.ok(new MessageResponse("Likes: " + postOpt.get().getTotalLikes()));
    }

    /**
     * Method to add a dislike to the post. If the user has already disliked the post, just removes the dislike
     * @param id
     * @param username (gets from the jwt token)
     * @return ResponseEntity(ok: totalLikes, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> dislike(Long id, String username) {

        //Gets post
        Optional<Post> postOpt = postRepository.findById(id);

        //Gets user
        Optional<User> userOpt = userRepository.findByUsername(username);

        //Tests it the user hasn't already disliked the post
        boolean alreadyDisliked = false;

        for (User user : postOpt.get().getUsersWhoDislike()){
            if (userOpt.get() == user) alreadyDisliked = true;
        }

        if (alreadyDisliked){
            //Removes dislike
            postOpt.get().getUsersWhoDislike().remove(userOpt.get());
        } else {

            //Adds dislike
            postOpt.get().getUsersWhoDislike().add(userOpt.get());

            //Removes from like
            for (User user : postOpt.get().getUsersWhoLike()){
                if (userOpt.get() == user) postOpt.get().getUsersWhoLike().remove(userOpt.get());
            }
        }

        postOpt.get().refreshLikes();

        postRepository.save(postOpt.get());

        return ResponseEntity.ok(new MessageResponse("Dislikes: " + postOpt.get().getTotalDislikes()));
    }

    /**
     * Method to fix or unfix a post
     * @param id
     * @return ResponseEntity (ok: isFixed, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> fix(Long id) {

        //Gets the post
        Optional<Post> postOpt = postRepository.findById(id);

        if (postOpt.isEmpty())
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        //Fix or unfix the post
        if (postOpt.get().getFixed()){
            postOpt.get().setFixed(false);
        } else {
            postOpt.get().setFixed(true);
        }

        postRepository.save(postOpt.get());

        return ResponseEntity.ok(new MessageResponse("Fixed: " + postOpt.get().getFixed()));
    }

    /**
     * Method to make user follow a post. Checks if the user has access to the subject this post belongs to
     * @param id
     * @param username (gets from the jwt token)
     * @return ResponseEntity(messageResponse)
     */
    @Override
    public ResponseEntity<?> follow(Long id, String username) {

        //Gets user
        Optional<User> userOpt = userRepository.findByUsername(username);

        //Gets post
        Optional<Post> postOpt = postRepository.findById(id);

        if (postOpt.isEmpty())
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid id"));

        //Checks if the user has access to the subject
        //Get module than gets subject
        Module module = postOpt.get().getModule();
        Subject subject = module.getSubject();

        if (!userOpt.get().getHasAccess().contains(subject))
            return ResponseEntity.badRequest().body(new MessageResponse("The user doesn't have access to the subject"));

        userOpt.get().getFollowsPost().add(postOpt.get());
        userRepository.save(userOpt.get());
        postOpt.get().getUsersFollowing().add(userOpt.get());
        postRepository.save(postOpt.get());

        return ResponseEntity.ok(new MessageResponse("The user " + username + " now follows the post " + id));
    }

    /**
     * Method to make user unfollow a post
     * @param id
     * @param username (gets from the jwt token)
     * @return ResponseEntity(messageResponse)
     */
    @Override
    public ResponseEntity<?> unfollow(Long id, String username) {

        //Gets user
        Optional<User> userOpt = userRepository.findByUsername(username);

        //Gets post
        Optional<Post> postOpt = postRepository.findById(id);

        if (postOpt.isEmpty())
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid id"));

        userOpt.get().getFollowsPost().remove(postOpt.get());
        userRepository.save(userOpt.get());
        postOpt.get().getUsersFollowing().remove(userOpt.get());
        postRepository.save(postOpt.get());

        return ResponseEntity.ok(new MessageResponse("The user " + username + " has unfollowed the post " + id));

    }
}
