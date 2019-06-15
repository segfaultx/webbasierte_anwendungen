package application.sichtung;

import application.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        List<String> response = new ArrayList<String>();
        for (Sichtung sichtung : dbservice.findAllSichtungen()){
            response.add("/rest/sichtungen/"+sichtung.getId());
        }
        return response;
    }
}
