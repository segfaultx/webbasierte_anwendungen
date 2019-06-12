package application.sichtung;

import application.services.DatabaseService;
import application.services.PictureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
        m.addAttribute("nr", nr);
        return "editSighting";

    }
    @GetMapping("/sichtung/image/{nr}")
    public ResponseEntity<Resource> downloadImage(@PathVariable("nr") int nr) throws IOException {
        String mimetype = pictureService.getMimeTypeSighting(nr);
        ByteArrayResource resource = pictureService.loadSightingPicture(nr);
        return ResponseEntity.ok()
                .header(null)
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType(mimetype))
                .body(resource);
    }

    @PostMapping("/sichtung/edit/{nr}")
    public String saveSightingDetails(@PathVariable("nr") int nr , Model m, @ModelAttribute("detailsSighting") Sichtung sichtung, @RequestAttribute("picture") MultipartFile picture) throws IOException {
        if (picture != null && picture.getSize() > 0)
            pictureService.saveSightingPicture(nr, picture.getInputStream());
        Sichtung savesichtung = dbservice.findAllSichtungen().get(nr);
        savesichtung.setRating(sichtung.getRating());
        savesichtung.setPlace(sichtung.getPlace());
        savesichtung.setFinder(sichtung.getFinder());
        savesichtung.setDescription(sichtung.getDescription());
        savesichtung.setDay_time(sichtung.getDay_time());
        savesichtung.setDate(sichtung.getDate());
        dbservice.saveSichtung(savesichtung);
        m.addAttribute("sichtungsform", new Sichtung());
        m.addAttribute("sichtungen", dbservice.findAllSichtungen());
        return "redirect:/sichtung";

    }
}