package application.sichtung;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@SessionAttributes("sichtungen")
public class SichtungsController {
    private Logger logger = LoggerFactory.getLogger(SichtungsController.class);

    @ModelAttribute("sichtungen")
    public void init_model(Model m) {
        m.addAttribute("sichtungen", new Sichtungen());
    }


    @GetMapping("/sichtung")
    public String showSichtungen(@ModelAttribute("sichtungsform") Sichtung sichtungsform, Model m) {
        logger.info("Sichtungsform: " + sichtungsform);
        m.addAttribute("sichtungsform", sichtungsform);
        return "sichtungen";
    }

    @PostMapping("/sichtung")
    public String addSichtung(@Valid @ModelAttribute("sichtung") Sichtung neueSichtung, BindingResult neueSichtungResult,
                              @ModelAttribute("sichtungen") Sichtungen sichtungen, Model m) {
        if (neueSichtungResult.hasErrors()) {
            m.addAttribute("sichtungsform", neueSichtung);
            return "sichtungen";
        }
        sichtungen.add(neueSichtung);
        m.addAttribute("sichtungsform", new Sichtung(null, null, null, null, null));

        return "sichtungen";

    }

    @GetMapping("/sichtung/{nr}")
    public String editSichtung(@PathVariable("nr") int nr, Model m, @ModelAttribute("sichtungen") Sichtungen sichtungen) {
        m.addAttribute("sichtungsform", sichtungen.getList().get(nr));
        sichtungen.getList().remove(nr);
        return "sichtungen";
    }
}