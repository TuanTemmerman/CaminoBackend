package com.example.gip_project_goudvissen.Service;

import com.example.gip_project_goudvissen.DTO.NotesDTO;
import com.example.gip_project_goudvissen.Entity.Blog;
import com.example.gip_project_goudvissen.Entity.Notes;
import com.example.gip_project_goudvissen.Entity.User;
import com.example.gip_project_goudvissen.Repository.BlogRepository;
import com.example.gip_project_goudvissen.Repository.NotesRepository;
import com.example.gip_project_goudvissen.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class NotesService {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    public Notes addNotes(NotesDTO notesDTO, Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Notes notes = new Notes();
        notes.setText(notesDTO.getText());
        notes.setBlog(user.getBlog());
        return notesRepository.save(notes);
    }

    public Notes findNotesById(Long notesId, Long userId){
        Notes notes = notesRepository.findById(notesId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notes not found"));
        if (Objects.equals(notes.getBlog().getUser().getId(), userId)){
            return notes;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized to view this note");
        }
    }

    public List<Notes> getAllNotesFromUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Long blogId = user.getBlog().getId();
        return notesRepository.findByBlog_Id(blogId);
    }

    public Notes updateNotes(Long userId, NotesDTO notesDTO, Long notesId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Notes foundNotes = notesRepository.findById(notesId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notes not found"));
        Blog blog = user.getBlog();
        if (Objects.equals(foundNotes.getBlog().getId(), blog.getId())) {
            foundNotes.setText(notesDTO.getText());
            return notesRepository.save(foundNotes);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized to update this note");
        }
    }

    public void deleteNotes(Long userId, Long notesId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Notes foundNotes = notesRepository.findById(notesId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notes not found"));
        Blog blog = user.getBlog();
        if (Objects.equals(foundNotes.getBlog().getId(), blog.getId())){
            notesRepository.deleteById(notesId);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized to delete this note");
        }
    }
}
