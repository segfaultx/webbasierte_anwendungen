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
import java.security.Principal;

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
        return "sichtung/sichtungen";
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

        return "sichtung/sichtungen";

    }

    @GetMapping("/sichtung/{nr}")
    public String editSichtung(@PathVariable("nr") int nr, Model m) {
        m.addAttribute("sichtungsform", dbservice.findAllSichtungen().get(nr));
        dbservice.deleteSichtung(dbservice.findAllSichtungen().get(nr));
        m.addAttribute("sichtungen", dbservice.findAllSichtungen());
        return "sichtung/sichtungen";
    }

    @PostMapping(value = "/sichtung", params = "sprache")
    public String swapLang(@ModelAttribute("sichtungsform") Sichtung sichtungsform, Model m, @ModelAttribute("langObject") langObject langobject) {
        m.addAttribute("sichtungsform", sichtungsform);
        m.addAttribute("currLang", langobject.getCurrLang());
        return "sichtung/sichtungen";
    }

    @GetMapping("/sichtung/edit/{nr}")
    public String editSightingDetails(@PathVariable("nr") long nr, Model m, Principal principal) {
        Sichtung editSichtung = dbservice.findSichtungByID(nr);
        m.addAttribute("detailsSighting", editSichtung);
        m.addAttribute("nr", nr);
        if (principal != null) m.addAttribute("principal",principal);
        return "sichtung/editSighting";

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
    public String saveSightingDetails(@PathVariable("nr")long nr,  Model m, @Valid @ModelAttribute("detailsSighting") Sichtung sichtung, BindingResult bindingResult, @RequestAttribute("picture") MultipartFile picture) throws IOException {
        if (bindingResult.hasErrors()) {
            m.addAttribute("detailsSighting", sichtung);
            return "sichtung/editSighting";
        }
        if (picture != null && picture.getSize() > 0)
            pictureService.saveSightingPicture(nr, picture.getInputStream());
        Sichtung savesichtung = dbservice.findSichtungByID(nr);
        savesichtung.setDate(sichtung.getDate());
        savesichtung.setDay_time(sichtung.getDay_time());
        savesichtung.setDescription(sichtung.getDescription());
        savesichtung.setFinder(sichtung.getFinder());
        savesichtung.setPlace(sichtung.getPlace());
        savesichtung.setRating(sichtung.getRating());
        dbservice.saveSichtung(savesichtung);
        m.addAttribute("sichtungsform", new Sichtung());
        m.addAttribute("sichtungen", dbservice.findAllSichtungen());
        return "redirect:/sichtung";

    }
}