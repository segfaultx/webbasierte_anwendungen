package application.sichtung;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/sichtung")
@SessionAttributes("sichtungen")
public class SichtungsController{

    @GetMapping("/sichtung")
    public String showSichtungen(){
        return "sichtungen";
    } 
}