package application.services;


import application.sichtung.SichtungsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PictureService {

    @Value("${avatar_fileupload.directory}")
    private String AVATAR_UPLOADDIR;
    @Value("${sighting_fileupload.directory}")
    private String SIGHTING_UPLOADDIR;

    @Autowired
    GeoDataService geoDataService;
    private Logger logger = LoggerFactory.getLogger(SichtungsController.class);

    public void saveUserAvatar(String username, InputStream inputStream) throws IOException {
        if (inputStream != null) {
            Path filepath = Paths.get(AVATAR_UPLOADDIR, "avatar-" + username +
                    ".png");
            Files.copy(inputStream, filepath);
        }
    }

    public void removeUserAvatar(String username) throws IOException {
        Path path = Paths.get(AVATAR_UPLOADDIR, "avatar-" + username + ".png");
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    public String getMimeType(String username) throws IOException {
        Path path = Paths.get(AVATAR_UPLOADDIR, "avatar-" + username + ".png");
        if (Files.exists(path)) return Files.probeContentType(path);
        return Files.probeContentType(Paths.get(AVATAR_UPLOADDIR, "avatar-default.png"));
    }

    public ByteArrayResource loadUserAvatar(String username) throws IOException {
        Path path = Paths.get(AVATAR_UPLOADDIR, "avatar-" + username + ".png");
        if (Files.exists(path)) return new ByteArrayResource(Files.readAllBytes(path));
        return new ByteArrayResource(Files.readAllBytes(Paths.get(AVATAR_UPLOADDIR, "avatar-default.png")));
    }

    public void saveSightingPicture(long id, InputStream inputStream) throws IOException {
        if (inputStream != null) {
            Path filepath = Paths.get(SIGHTING_UPLOADDIR, "sighting-" + id + ".png");
            Files.copy(inputStream, filepath);
            try{
                geoDataService.retrieveGeoData(SIGHTING_UPLOADDIR+"sighting-"+id+".png");
            }catch(Exception ex){
                logger.info(ex.getMessage());
            }
        }
    }

    public ByteArrayResource loadSightingPicture(long id) throws IOException {
        Path path = Paths.get(SIGHTING_UPLOADDIR, "sighting-" + id + ".png");
        if (Files.exists(path)) return new ByteArrayResource(Files.readAllBytes(path));
        return new ByteArrayResource(Files.readAllBytes(Paths.get(SIGHTING_UPLOADDIR, "sighting_default.png")));
    }

    public String getMimeTypeSighting(int id) throws IOException {
        Path path = Paths.get(SIGHTING_UPLOADDIR, "sighting-" + id + ".png");
        if (Files.exists(path)) return Files.probeContentType(path);
        return Files.probeContentType(Paths.get(SIGHTING_UPLOADDIR, "sigting_default.png"));
    }
}
