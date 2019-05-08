package application.sichtung;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("sichtungen")
public class SichtungsController{

    @GetMapping("/sichtung")
    public String showSichtungen(@ModelAttribute("sichtungsform") Sichtung sichtungsform){

        return "sichtungen";
    } 
    @PostMapping("/sichtung")
    @ResponseBody
    public String addSichtung(){
        return "Hallo";
    }
}