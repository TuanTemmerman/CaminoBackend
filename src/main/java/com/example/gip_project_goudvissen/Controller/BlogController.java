package com.example.gip_project_goudvissen.Controller;

import com.example.gip_project_goudvissen.DTO.BlogpostDTO;
import com.example.gip_project_goudvissen.Entity.Blogpost;
import com.example.gip_project_goudvissen.Service.BlogService;
import com.example.gip_project_goudvissen.Service.JwtTokenService;
import com.example.gip_project_goudvissen.Service.TokenExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private JwtTokenService tokenService;

    @PostMapping("/addBlogpost")
    public ResponseEntity<Blogpost> addBlogpost(@RequestBody BlogpostDTO blogpostDTO, HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            Blogpost createdBlogpost = blogService.addBlogpost(blogpostDTO, userId);
            if (createdBlogpost != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(createdBlogpost);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/updateBlogpost/{id}")
    public ResponseEntity<Blogpost> updateBlogpost(@RequestBody BlogpostDTO blogpostDTO, @PathVariable Long id, HttpServletRequest request) {
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            Blogpost updatedBlogpost = blogService.updateBlogpost(userId, blogpostDTO, id);
            if (updatedBlogpost != null) {
                return ResponseEntity.ok(updatedBlogpost);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Blogpost> getBlogpost(@PathVariable Long id, HttpServletRequest request){
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            Blogpost blogpost = blogService.findBlogpostById(id, userId);
            if (blogpost != null) {
                blogpost.setUsername(blogpost.getBlog().getUser().getUsername());
                return ResponseEntity.ok(blogpost);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            Blogpost blogpost = blogService.findPublicBlogpostById(id);
            if (blogpost != null) {
                blogpost.setUsername(blogpost.getBlog().getUser().getUsername());
                return ResponseEntity.ok(blogpost);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Blogpost>> getAllBlogposts() {
        List<Blogpost> blogposts = blogService.getAllBlogposts();
        if (blogposts != null) {
            for (Blogpost blogpost : blogposts) {
                if (blogpost.getBlog() != null && blogpost.getBlog().getUser() != null) {
                    blogpost.setUsername(blogpost.getBlog().getUser().getUsername());
                }
            }
            return ResponseEntity.ok(blogposts);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/allBlogpostsFromUser")
    public ResponseEntity<List<Blogpost>> getAllBlogpostsFromUser(HttpServletRequest request){
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            List<Blogpost> blogposts = blogService.getAllBlogpostsFromUser(userId);
            if (blogposts != null) {
                return ResponseEntity.ok(blogposts);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBlogpost(@PathVariable Long id, HttpServletRequest request){
        String token = TokenExtractor.extractToken(request);
        if (token != null){
            Long userId = tokenService.extractUserId(token);
            blogService.deleteBlogpost(userId, id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
