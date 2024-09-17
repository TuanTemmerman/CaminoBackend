package com.example.gip_project_goudvissen.Repository;

import com.example.gip_project_goudvissen.Entity.Blogpost;
import com.example.gip_project_goudvissen.Entity.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotesRepository extends JpaRepository<Notes, Long> {
    List<Notes> findByBlog_Id(Long blogId);
}
