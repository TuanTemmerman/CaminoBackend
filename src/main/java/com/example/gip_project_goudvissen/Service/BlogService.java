package com.example.gip_project_goudvissen.Service;

import com.example.gip_project_goudvissen.DTO.BlogpostDTO;
import com.example.gip_project_goudvissen.Entity.Blog;
import com.example.gip_project_goudvissen.Entity.Blogpost;
import com.example.gip_project_goudvissen.Entity.User;
import com.example.gip_project_goudvissen.Repository.BlogRepository;
import com.example.gip_project_goudvissen.Repository.BlogpostRepository;
import com.example.gip_project_goudvissen.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private BlogpostRepository blogpostRepository;

    @Autowired
    private UserRepository userRepository;

    public void createBlog(User user) {
        Blog blog = new Blog();
        blog.setUser(user);
        blogRepository.save(blog);
    }

    public Blogpost addBlogpost(BlogpostDTO blogpostDTO, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Blogpost blogpost = new Blogpost();
        blogpost.setTitle(blogpostDTO.getTitle());
        blogpost.setDate(blogpostDTO.getDate());
        blogpost.setText(blogpostDTO.getText());
        blogpost.setRestricted(blogpostDTO.isRestricted());
        Blog blog = user.getBlog();
        if (blog != null) {
            blogpost.setBlog(blog);
            return blogpostRepository.save(blogpost);
        } else {
            //Blog wordt automatisch mee aangemaakt met user, dus deze error zou NOOIT horen te verschijnen
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Blog not found for the user.");
        }
    }

    public Blogpost updateBlogpost(Long userId, BlogpostDTO blogpostDTO, Long id){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Blogpost foundBlogpost = blogpostRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blogpost not found"));
        Blog blog = user.getBlog();
        if (Objects.equals(foundBlogpost.getBlog().getId(), blog.getId())) {
            foundBlogpost.setTitle(blogpostDTO.getTitle());
            foundBlogpost.setDate(blogpostDTO.getDate());
            foundBlogpost.setText(blogpostDTO.getText());
            foundBlogpost.setRestricted(blogpostDTO.isRestricted());
            return blogpostRepository.save(foundBlogpost);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized to update this Blogpost");
        }
    }

    public Blogpost findBlogpostById(Long id, Long userId){
        Blogpost blogpost = blogpostRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blogpost not found"));
        if (blogpost != null){
            if (blogpost.getRestricted()){
                if (Objects.equals(blogpost.getBlog().getUser().getId(), userId)){
                    return blogpost;
                } else {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized to see this Blogpost");
                }
            }
        }
        return blogpost;
    }

    public Blogpost findPublicBlogpostById(Long id){
        Blogpost blogpost = blogpostRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blogpost not found"));
        if (!blogpost.getRestricted()){
            return blogpost;
        }
        return null;
    }

    public List<Blogpost> getAllBlogposts(){
        return blogpostRepository.findByIsRestrictedIsFalse();
    }

    public List<Blogpost> getAllBlogpostsFromUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Long blogId = user.getBlog().getId();
        return blogpostRepository.findByBlog_Id(blogId);
    }

    public void deleteBlogpost(Long userId, Long id){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Blogpost foundBlogpost = blogpostRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blogpost not found"));
        Blog blog = user.getBlog();
        if (Objects.equals(foundBlogpost.getBlog().getId(), blog.getId())) {
            blogpostRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized to delete this Blogpost");
        }
    }



}
