package application.services;


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

    @Value("${fileupload.directory}")
    private String UPLOADDIR;

    public void saveUserAvatar(String username, InputStream inputStream) throws IOException {
        if (inputStream != null) {
            Path filepath = Paths.get(UPLOADDIR, "avatar-" + username +
                    ".png");
            Files.copy(inputStream, filepath);
        }
    }

    public void removeUserAvatar(String username) throws IOException {
        Path path = Paths.get(UPLOADDIR, "avatar-" + username + ".png");
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    public String getMimeType(String username) throws IOException {
        Path path = Paths.get(UPLOADDIR, "avatar-" + username + ".png");
        if (Files.exists(path)) return Files.probeContentType(path);
        return Files.probeContentType(Paths.get(UPLOADDIR, "avatar-default.png"));
    }

    public ByteArrayResource loadUserAvatar(String username) throws IOException {
        Path path = Paths.get(UPLOADDIR, "avatar-" + username + ".png");
        if (Files.exists(path)) return new ByteArrayResource(Files.readAllBytes(path));
        return new ByteArrayResource(Files.readAllBytes(Paths.get(UPLOADDIR, "avatar-default.png")));
    }
}
