package application.sichtung;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
@SessionAttributes(names = {"sichtungen", "langObject", "currentLang"})
public class SichtungsController {
    private Logger logger = LoggerFactory.getLogger(SichtungsController.class);

    public class langObject {
        private String[] langs = {"de", "en"};
        private String currLang = "de";

        public String getCurrLang() {
            return currLang;
        }

        public String[] getLangs() {
            return langs;
        }

        public void setCurrLang(String currLang) {
            this.currLang = currLang;
        }

        public void setLangs(String[] langs) {
            this.langs = langs;
        }
    }

    @ModelAttribute("sichtungen")
    public void init_model(Model m) {
        m.addAttribute("sichtungen", new Sichtungen());
    }

    @ModelAttribute("langObject")
    public void init_langs(Model m) {
        m.addAttribute("langObject", new langObject());
    }

    @GetMapping("/sichtung")
    public String showSichtungen(@ModelAttribute("sichtungsform") Sichtung sichtungsform, Model m) {
        logger.info("Sichtungsform: " + sichtungsform);
        m.addAttribute("sichtungsform", sichtungsform);
        m.addAttribute("currentLang", "de");
        return "sichtungen";
    }

    @PostMapping("/sichtung")
    public String addSichtung(@Valid @ModelAttribute("sichtungsform") Sichtung sichtungsform, BindingResult neueSichtungResult,
                              @ModelAttribute("sichtungen") Sichtungen sichtungen, Model m) {
        if (neueSichtungResult.hasErrors()) {
            m.addAttribute("sichtungsform", sichtungsform);
            return "sichtungen";
        }
        sichtungen.add(sichtungsform);
        m.addAttribute("sichtungsform", new Sichtung());

        return "sichtungen";

    }

    @GetMapping("/sichtung/{nr}")
    public String editSichtung(@PathVariable("nr") int nr, Model m, @ModelAttribute("sichtungen") Sichtungen sichtungen) {
        m.addAttribute("sichtungsform", sichtungen.getList().get(nr));
        sichtungen.getList().remove(nr);
        return "sichtungen";
    }

    @PostMapping(value = "/sichtung", params = "sprache")
    public String swapLang(@ModelAttribute("sichtungsform") Sichtung sichtungsform, Model m, @ModelAttribute("langObject") langObject langobject) {
        m.addAttribute("sichtungsform", sichtungsform);
        m.addAttribute("currLang", langobject.getCurrLang());
        return "sichtungen";
    }
}