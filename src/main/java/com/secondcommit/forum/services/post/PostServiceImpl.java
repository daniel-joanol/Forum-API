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

        //Test if the author exists
        Optional<User> userOpt = userRepository.findByUsername(author);

        if (userOpt.isEmpty())
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The user " + author + " doesn't exist"));

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

        //Validates the id
        Optional<Post> postOpt = postRepository.findById(id);

        if (postOpt.isEmpty())
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

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

        //Validates the id
        Optional<Post> postOpt = postRepository.findById(id);

        if (postOpt.isEmpty())
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        //Tests if this user exists
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty())
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The user " + username + " doesn't exist"));

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

        //Updates the post
        if (postDto.getTitle() != null)
            postOpt.get().setTitle(postDto.getTitle());

        if (postDto.getContent() != null)
            postOpt.get().setContent(postDto.getContent());

        postRepository.save(postOpt.get());

        return ResponseEntity.ok(postOpt.get());
    }

    /**
     * Methos to delete the post. Only authors and admins are allowed
     * @param id
     * @param username (takes the username from the jwt token)
     * @return ResponseEntity (ok: post, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> deletePost(Long id, String username) {
        //Validates the id
        Optional<Post> postOpt = postRepository.findById(id);

        if (postOpt.isEmpty())
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        //Tests if this user exists
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty())
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The user " + username + " doesn't exist"));

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
}
