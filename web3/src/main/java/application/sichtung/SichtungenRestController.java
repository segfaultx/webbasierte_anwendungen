package application.sichtung;

import application.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
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
    public Comment addCommentToSichtungById(@PathVariable("sid")long sid, @Valid @RequestBody Comment comment, BindingResult bindingResult) throws NotLoggedInException{
        if(bindingResult.hasFieldErrors("message") || bindingResult.hasFieldErrors("creationDate")){
            return null;
        }
        comment.setSichtung(dbservice.findSichtungByID(sid));
        comment.setCreator(dbservice.findUserByLoginname(comment.getCreator().getLoginname()));
        return dbservice.addComment(comment);
    }
    @DeleteMapping("/{sid}/kommentare/{kid}")
    @PreAuthorize("hasRole('MEMBER')")
    public void deleteCommentByCommentId(@PathVariable("sid") long sid, @PathVariable("kid") long kid, Principal principal){
        System.out.println(principal.getName());
        dbservice.deleteCommentById(kid);
    }
}

@ResponseStatus(HttpStatus.FORBIDDEN)
class NotLoggedInException extends RuntimeException{
    public NotLoggedInException(String message){
        super(message);
    }
}
