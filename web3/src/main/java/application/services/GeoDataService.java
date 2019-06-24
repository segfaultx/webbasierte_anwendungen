package application.services;


import application.sichtung.GeoData;
import application.sichtung.SichtungsController;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.GpsDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.ZoneId;

@Service
public class GeoDataService {
    private Logger logger = LoggerFactory.getLogger(SichtungsController.class);

    public GeoData retrieveGeoData(InputStream file) throws Exception {
        GeoData data = new GeoData();
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        try {
            data.setOrigDate(metadata.getFirstDirectoryOfType(ExifIFD0Directory.class)
                    .getDate(ExifIFD0Directory.TAG_DATETIME_ORIGINAL)
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        } catch (NullPointerException ex) {
            logger.info(ex.getMessage());
        }
        var geodir = metadata.getFirstDirectoryOfType(GpsDirectory.class).getGeoLocation();
        data.setLatitude(geodir.getLatitude());
        data.setLongtitude(geodir.getLongitude());
        return data;
    }
}
