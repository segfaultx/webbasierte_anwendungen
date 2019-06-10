package application.sichtung;

import application.services.DatabaseService;
import application.services.PictureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@SessionAttributes(names = {"sichtungen", "langObject", "currentLang"})
public class SichtungsController {
    private Logger logger = LoggerFactory.getLogger(SichtungsController.class);
    @Autowired
    DatabaseService dbservice;

    @Autowired
    PictureService pictureService;

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
        m.addAttribute("sichtungen", dbservice.findAllSichtungen());
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
    public String addSichtung(@Valid @ModelAttribute("sichtungsform") Sichtung sichtungsform, BindingResult neueSichtungResult, Model m) {
        if (neueSichtungResult.hasErrors()) {
            m.addAttribute("sichtungsform", sichtungsform);
            return "sichtungen";
        }
        dbservice.saveSichtung(sichtungsform);
        m.addAttribute("sichtungen", dbservice.findAllSichtungen());
        m.addAttribute("sichtungsform", new Sichtung());

        return "sichtungen";

    }

    @GetMapping("/sichtung/{nr}")
    public String editSichtung(@PathVariable("nr") int nr, Model m) {
        m.addAttribute("sichtungsform", dbservice.findAllSichtungen().get(nr));
        dbservice.deleteSichtung(dbservice.findAllSichtungen().get(nr));
        m.addAttribute("sichtungen", dbservice.findAllSichtungen());
        return "sichtungen";
    }

    @PostMapping(value = "/sichtung", params = "sprache")
    public String swapLang(@ModelAttribute("sichtungsform") Sichtung sichtungsform, Model m, @ModelAttribute("langObject") langObject langobject) {
        m.addAttribute("sichtungsform", sichtungsform);
        m.addAttribute("currLang", langobject.getCurrLang());
        return "sichtungen";
    }

    @GetMapping("/sichtung/edit/{nr}")
    public String editSightingDetails(@PathVariable("nr") int nr, Model m) {
        m.addAttribute("detailsSighting", dbservice.findAllSichtungen().get(nr));
        return "editSighting";

    }

    @PostMapping("/sichtung/edit/{nr}")
    public String saveSightingDetails(@PathVariable("nr") int nr, Model m, @ModelAttribute("detailsSighting") Sichtung sichtung, MultipartFile picture) throws IOException {
        if (picture != null && picture.getSize() > 0)
            pictureService.saveSightingPicture(sichtung.getId(), picture.getInputStream());
        Sichtung copySichtung = dbservice.findAllSichtungen().get(nr);
        copySichtung.setDate(sichtung.getDate());
        copySichtung.setDay_time(sichtung.getDay_time());
        copySichtung.setDescription(sichtung.getDescription());
        copySichtung.setFinder(sichtung.getFinder());
        copySichtung.setPlace(sichtung.getPlace());
        copySichtung.setRating(sichtung.getRating());
        m.addAttribute("sichtungsform", new Sichtung());
        m.addAttribute("sichtungen", dbservice.findAllSichtungen());
        return "redirect:/sichtung";

    }
}