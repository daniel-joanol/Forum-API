package com.secondcommit.forum.services.post;

import com.secondcommit.forum.dto.PostDto;
import com.secondcommit.forum.entities.Post;
import com.secondcommit.forum.entities.Role;
import com.secondcommit.forum.entities.User;
import com.secondcommit.forum.repositories.PostRepository;
import com.secondcommit.forum.repositories.UserRepository;
import com.secondcommit.forum.security.payload.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    /**
     * Method to add post.
     * @param postDto (title and content)
     * @param author (gets the username from the jwt token)
     * @return ResponseEntity (ok: post, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> addPost(PostDto postDto, String author) {

        //Tests if the post title already exists by title
        Optional<Post> postOpt = postRepository.findByTitle(postDto.getTitle());

        if (postOpt.isPresent())
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("This post title " + postDto.getTitle() + " is already being used"));

        //Gets user from author
        Optional<User> userOpt = userRepository.findByUsername(author);

        //Creates the new post
        Post post = new Post(userOpt.get(), postDto.getTitle(), postDto.getContent());
        postRepository.save(post);

        return ResponseEntity.ok(post);
    }

    /**
     * Method to get the post
     * @param id
     * @return ResponseEntity (ok: post, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> getPost(Long id) {

        //Gets post
        Optional<Post> postOpt = postRepository.findById(id);

        return ResponseEntity.ok(postOpt.get());
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

        //Updates the post
        if (postDto.getTitle() != null)
            postOpt.get().setTitle(postDto.getTitle());

        if (postDto.getContent() != null)
            postOpt.get().setContent(postDto.getContent());

        postRepository.save(postOpt.get());

        return ResponseEntity.ok(postOpt.get());
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
            postOpt.get().removeUsersWhoLike(userOpt.get());
        } else {

            //Adds like
            postOpt.get().addUsersWhoLike(userOpt.get());

            //Removes from dislike
            for (User user : postOpt.get().getUsersWhoDislike()){
                if (userOpt.get() == user) postOpt.get().removeUsersWhoDislike(userOpt.get());
            }

        }

        postRepository.save(postOpt.get());

        return ResponseEntity.ok(postOpt.get().getLikes());
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
            postOpt.get().removeUsersWhoDislike(userOpt.get());
        } else {

            //Adds dislike
            postOpt.get().addUsersWhoDislike(userOpt.get());

            //Removes from like
            for (User user : postOpt.get().getUsersWhoLike()){
                if (userOpt.get() == user) postOpt.get().removeUsersWhoLike(userOpt.get());
            }
        }

        postRepository.save(postOpt.get());

        return ResponseEntity.ok(postOpt.get().getDislikes());
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
        if (postOpt.get().isFixed()){
            postOpt.get().setFixed(false);
        } else {
            postOpt.get().setFixed(true);
        }

        postRepository.save(postOpt.get());

        return ResponseEntity.ok(postOpt.get().isFixed());
    }
}
