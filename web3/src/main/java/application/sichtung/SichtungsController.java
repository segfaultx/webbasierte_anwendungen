package application.sichtung;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("sichtungen")
public class SichtungsController {
    Logger logger = LoggerFactory.getLogger(SichtungsController.class);

    @GetMapping("/sichtung")
    public String showSichtungen(@ModelAttribute("sichtungsform") Sichtung sichtungsform, Model m) {
        logger.info("Sichtungsform: " + sichtungsform);
        m.addAttribute("sichtungsform", sichtungsform);
        return "sichtungen";
    }

    @PostMapping("/sichtung")
    @ResponseBody
    public String addSichtung(@ModelAttribute("sichtungsform") Sichtung sichtung, BindingResult result, Model m) {
        if (result.hasErrors()) {
            return "sichtungen";
        }
        return "Hallo";
    }
}