package application.sichtung;

import application.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rest/sichtungen")
public class SichtungenRestController {
    @Autowired
    DatabaseService dbservice;


    @GetMapping("/{sid}")
    public Sichtung getSichtungById(@PathVariable("sid") long sid){
        return dbservice.findSichtungByID(sid);
    }

    @GetMapping
    public List<String> getAllSichtungen(){
        List<String> response = new ArrayList<>();
        for (Sichtung sichtung : dbservice.findAllSichtungen()){
            response.add("/rest/sichtungen/"+sichtung.getId());
        }
        return response;
    }

    @GetMapping("/{sid}/kommentare")
    public List<Comment> getSichtungskommentareById(@PathVariable("sid") long sid){
        return dbservice.findCommentsBySichtungOrderByDateDesc(dbservice.findSichtungByID(sid));
    }
    @GetMapping("/{sid}/kommentare/{kid}")
    public Comment getCommentBySichtungIdAndKommentarId(@PathVariable("sid") long sid, @PathVariable("kid") long kid){
        return dbservice.findCommentByID(kid);
    }
    @PostMapping("/{sid}/kommentare")
    @PreAuthorize("hasRole('MEMBER')")
    public Comment addCommentToSichtungById(@PathVariable("sid")long sid, @Valid @RequestBody Comment comment, BindingResult bindingResult, Principal principal) throws NotLoggedInException{
        if(bindingResult.hasFieldErrors("message")){
            return null;
        }
        comment.setCreationDate(LocalDate.now());
        comment.setSichtung(dbservice.findSichtungByID(sid));
        comment.setCreator(dbservice.findUserByLoginname(principal.getName()));
        return dbservice.addComment(comment);
    }
    @DeleteMapping("/{sid}/kommentare/{kid}")
    @PreAuthorize("hasRole('MEMBER')")
    public void deleteCommentByCommentId(@PathVariable("sid") long sid, @PathVariable("kid") long kid, Principal principal) throws NotYourCommentException{
        if(!dbservice.findCommentByID(kid).getCreator().getLoginname().equals(principal.getName())) throw new NotYourCommentException(principal.getName());
        dbservice.deleteCommentById(kid);
    }
    @PutMapping("/{sid}/kommentare/{kid}")
    @PreAuthorize("hasRole('MEMBER')")
    public Comment updateCommentById(@PathVariable("sid") long sid, @PathVariable("kid") long kid,
                                     @Valid @RequestBody Comment comment,
                                     BindingResult bindingResult, Principal principal){
        if(!principal.getName().equals(dbservice.findCommentByID(kid).getCreator().getLoginname())) throw new NotYourCommentException(principal.getName());
        if(bindingResult.hasFieldErrors("message")) return null;
        Comment updateComment = dbservice.findCommentByID(kid);
        updateComment.setMessage(comment.getMessage());

        return dbservice.addComment(updateComment);
    }
}

@ResponseStatus(HttpStatus.FORBIDDEN)
class NotLoggedInException extends RuntimeException{
    public NotLoggedInException(String message){
        super(message);
    }
}
@ResponseStatus(HttpStatus.BAD_REQUEST)
class NotYourCommentException extends RuntimeException{
    public NotYourCommentException(String message){super(message);}
}
