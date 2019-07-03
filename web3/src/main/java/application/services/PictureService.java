package application.services;


import application.sichtung.GeoData;
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

/**
 * serviceclass handling all picture
 */
@Service
public class PictureService {

    @Value("${avatar_fileupload.directory}")
    private String AVATAR_UPLOADDIR;
    @Value("${sighting_fileupload.directory}")
    private String SIGHTING_UPLOADDIR;

    @Autowired
    GeoDataService geoDataService;
    private Logger logger = LoggerFactory.getLogger(SichtungsController.class);

    /**
     * method to save a user avatar
     * @param username
     * @param inputStream
     * @throws IOException
     */
    public void saveUserAvatar(String username, InputStream inputStream) throws IOException {
        if (inputStream != null) {
            Path filepath = Paths.get(AVATAR_UPLOADDIR, "avatar-" + username +
                    ".png");
            Files.copy(inputStream, filepath);
        }
    }

    /**
     *
     * @param username
     * @throws IOException
     */
    public void removeUserAvatar(String username) throws IOException {
        Path path = Paths.get(AVATAR_UPLOADDIR, "avatar-" + username + ".png");
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    /**
     * method to carve mimetype from picture of user
     * @param username
     * @return
     * @throws IOException
     */
    public String getMimeType(String username) throws IOException {
        Path path = Paths.get(AVATAR_UPLOADDIR, "avatar-" + username + ".png");
        if (Files.exists(path)) return Files.probeContentType(path);
        return Files.probeContentType(Paths.get(AVATAR_UPLOADDIR, "avatar-default.png"));
    }

    /**
     *
     * @param username
     * @return
     * @throws IOException
     */
    public ByteArrayResource loadUserAvatar(String username) throws IOException {
        Path path = Paths.get(AVATAR_UPLOADDIR, "avatar-" + username + ".png");
        if (Files.exists(path)) return new ByteArrayResource(Files.readAllBytes(path));
        return new ByteArrayResource(Files.readAllBytes(Paths.get(AVATAR_UPLOADDIR, "avatar-default.png")));
    }

    /**
     * method to save sighting picture by id
     * @param id
     * @param inputStream
     * @throws IOException
     */
    public void saveSightingPicture(long id, InputStream inputStream) throws IOException {
        if (inputStream != null) {
            Path filepath = Paths.get(SIGHTING_UPLOADDIR, "sighting-" + id + ".png");
            if(Files.exists(filepath)) Files.delete(filepath);
            Files.copy(inputStream, filepath);

        }
    }

    /**
     * uses geodataservice to carve geodata from picture
     * @param inputStream
     * @return
     */
    public GeoData getPictureGeoData(InputStream inputStream) {
        GeoData data = null;
        try {
            data = geoDataService.retrieveGeoData(inputStream);
        } catch (Exception ex) {
            logger.info(ex.getMessage());
        }
        return data;
    }

    /**
     *
     * @param id
     * @return
     * @throws IOException
     */
    public ByteArrayResource loadSightingPicture(long id) throws IOException {
        Path path = Paths.get(SIGHTING_UPLOADDIR, "sighting-" + id + ".png");
        if (Files.exists(path)) return new ByteArrayResource(Files.readAllBytes(path));
        return new ByteArrayResource(Files.readAllBytes(Paths.get(SIGHTING_UPLOADDIR, "sighting_default.png")));
    }

    /**
     *
     * @param id
     * @return
     * @throws IOException
     */
    public String getMimeTypeSighting(int id) throws IOException {
        Path path = Paths.get(SIGHTING_UPLOADDIR, "sighting-" + id + ".png");
        if (Files.exists(path)) return Files.probeContentType(path);
        return Files.probeContentType(Paths.get(SIGHTING_UPLOADDIR, "sigting_default.png"));
    }
}
