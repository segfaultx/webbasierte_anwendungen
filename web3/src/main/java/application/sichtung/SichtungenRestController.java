package application.sichtung;

import application.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/sichtungen")
public class SichtungenRestController {
    @Autowired
    DatabaseService dbservice;


    @GetMapping("/{sid}")
    public Sichtung getSichtungById(@PathVariable("sid") long sid){
        return dbservice.findSichtungByID(sid);
    }
}
