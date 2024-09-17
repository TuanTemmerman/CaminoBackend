package com.example.gip_project_goudvissen.Repository;

import com.example.gip_project_goudvissen.Entity.Blog;
import com.example.gip_project_goudvissen.Entity.Blogpost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
}
