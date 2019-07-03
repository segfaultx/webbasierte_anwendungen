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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;

/**
 * Controller managing sighting requests
 */
@Controller
@SessionAttributes(names = {"sichtungen", "langObject", "currentLang"})
public class SichtungsController {
    private Logger logger = LoggerFactory.getLogger(SichtungsController.class);
    @Autowired
    DatabaseService dbservice;

    @Autowired
    PictureService pictureService;

    /**
     * Holds information of currently selected language
     */
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

    /**
     * gets all sightings from the database and saves them locally
     * @param m
     */
    @ModelAttribute("sichtungen")
    public void init_model(Model m) {
        m.addAttribute("sichtungen", dbservice.findAllSichtungen());
    }

    /**
     * creates language object with initial language 
     * @param m
     */
    @ModelAttribute("langObject")
    public void init_langs(Model m) {
        m.addAttribute("langObject", new langObject());
    }

    /**
     * shows overview of all sightings
     * @param sichtungsform
     * @param m
     * @return
     */
    @GetMapping("/sichtung")
    public String showSichtungen(@ModelAttribute("sichtungsform") Sichtung sichtungsform, Model m) {
        logger.info("Sichtungsform: " + sichtungsform);
        m.addAttribute("sichtungsform", sichtungsform);
        m.addAttribute("currentLang", "de");
        return "sichtung/sichtungen";
    }

    /**
     * adds a sighting to the database
     * @param sichtungsform
     * @param neueSichtungResult
     * @param m
     * @return
     */
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

    /**
     * opens editing view of a sighting and deletes it from the database
     */
    @GetMapping("/sichtung/{nr}")
    public String editSichtung(@PathVariable("nr") int nr, Model m) {
        m.addAttribute("sichtungsform", dbservice.findAllSichtungen().get(nr));
        dbservice.deleteSichtung(dbservice.findAllSichtungen().get(nr));
        m.addAttribute("sichtungen", dbservice.findAllSichtungen());
        return "sichtung/sichtungen";
    }

    /**
     * swaps to chosen language
     * @param sichtungsform
     * @param m
     * @param langobject
     * @return
     */
    @PostMapping(value = "/sichtung", params = "sprache")
    public String swapLang(@ModelAttribute("sichtungsform") Sichtung sichtungsform, Model m, @ModelAttribute("langObject") langObject langobject) {
        m.addAttribute("sichtungsform", sichtungsform);
        m.addAttribute("currLang", langobject.getCurrLang());
        return "sichtung/sichtungen";
    }

    /**
     * opens editing view for given sighting
     * @param nr
     * @param m
     * @param principal
     * @return
     */
    @GetMapping("/sichtung/edit/{nr}")
    public String editSightingDetails(@PathVariable("nr") long nr, Model m, Principal principal) {
        Sichtung editSichtung = dbservice.findSichtungByID(nr);
        m.addAttribute("detailsSighting", editSichtung);
        m.addAttribute("sightingCommentList", dbservice.findCommentsBySichtungOrderByDateDesc(editSichtung));
        m.addAttribute("newComment", new Comment());
        if (principal != null) m.addAttribute("principal", principal);
        return "sichtung/editSighting";

    }

    /**
     * gets image of a sighting
     * @param nr
     * @return
     * @throws IOException
     */
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

    /**
     * saves changes of given sighting
     * @param nr
     * @param m
     * @param sichtung
     * @param bindingResult
     * @param picture
     * @return
     * @throws IOException
     */
    @PostMapping("/sichtung/edit/{nr}")
    public String saveSightingDetails(@PathVariable("nr") long nr, Model m, @Valid @ModelAttribute("detailsSighting") Sichtung sichtung, BindingResult bindingResult, @RequestAttribute("picture") MultipartFile picture) throws IOException {
        if (bindingResult.hasErrors()) {
            m.addAttribute("detailsSighting", sichtung);
            return "sichtung/editSighting";
        }
        GeoData geoData = null;
        if (picture != null && picture.getSize() > 0) {
            pictureService.saveSightingPicture(nr, picture.getInputStream());
            geoData = pictureService.getPictureGeoData(picture.getInputStream());
        }
        Sichtung savesichtung = dbservice.findSichtungByID(nr);
        savesichtung.setDate(sichtung.getDate());
        savesichtung.setDay_time(sichtung.getDay_time());
        savesichtung.setDescription(sichtung.getDescription());
        savesichtung.setFinder(sichtung.getFinder());
        savesichtung.setPlace(sichtung.getPlace());
        savesichtung.setRating(sichtung.getRating());
        if (geoData != null) {
            if(geoData.getOrigDate() != null) savesichtung.setDate(geoData.getOrigDate());
            savesichtung.setLatitude(geoData.getLatitude());
            savesichtung.setLongtitude(geoData.getLongtitude());
        }
        dbservice.saveSichtung(savesichtung);
        m.addAttribute("sichtungsform", new Sichtung());
        m.addAttribute("sichtungen", dbservice.findAllSichtungen());
        return "redirect:/sichtung";

    }

    /**
     * method to delete a comment
     * @param id
     * @param m
     * @param nr
     * @param principal
     * @return
     */
    @PostMapping("/sichtung/edit/{nr}/deletecomment/{id}")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    public String deleteComment(@PathVariable("id") long id, Model m, @PathVariable("nr") long nr, Principal principal) {
        Comment toDelete = dbservice.findCommentByID(id);
        if (toDelete.getCreator().getLoginname().equals(principal.getName())) dbservice.removeComment(toDelete);
        m.addAttribute("detailsSighting", dbservice.findSichtungByID(nr));
        return "redirect:/sichtung/edit/" + nr;
    }

    /**
     * posts a comment. requires user to be logged in and authorized
     * @param nr
     * @param m
     * @param principal
     * @param newComment
     * @param bindingResult
     * @return
     */
    @PostMapping("/sichtung/edit/{nr}/postcomment")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    public String postComment(@PathVariable("nr") long nr, Model m, Principal principal, @Valid @ModelAttribute("newComment") Comment newComment, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors("message")) {
            m.addAttribute("detailsSighting", dbservice.findSichtungByID(nr));
            m.addAttribute("newComment", newComment);
            m.addAttribute("sightingCommentList", dbservice.findCommentsBySichtungOrderByDateDesc(dbservice.findSichtungByID(nr)));
            return "redirect:/sichtung/edit/" + nr;
        }
        newComment.setCreationDate(LocalDate.now());
        newComment.setCreator(dbservice.findUserByLoginname(principal.getName()));
        newComment.setSichtung(dbservice.findSichtungByID(nr));
        dbservice.addComment(newComment);
        m.addAttribute("detailsSighting", dbservice.findSichtungByID(nr));
        m.addAttribute("sightingCommentList", dbservice.findCommentsBySichtungOrderByDateDesc(dbservice.findSichtungByID(nr)));
        return "redirect:/sichtung/edit/" + nr;
    }
}