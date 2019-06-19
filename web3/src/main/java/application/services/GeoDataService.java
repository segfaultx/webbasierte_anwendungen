package application.services;


import application.sichtung.GeoData;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.GpsDirectory;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.ZoneId;

@Service
public class GeoDataService {

    public GeoData retrieveGeoData(String filepath)throws IOException, ImageProcessingException {
        GeoData data = new GeoData();
        Metadata metadata = ImageMetadataReader.readMetadata(new FileInputStream(filepath));
        data.setOrigDate(metadata.getFirstDirectoryOfType(ExifIFD0Directory.class)
                .getDate(ExifIFD0Directory.TAG_DATETIME_ORIGINAL)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
        var geodir = metadata.getFirstDirectoryOfType(GpsDirectory.class).getGeoLocation();
        data.setLatitude(geodir.getLatitude());
        data.setLongtitude(geodir.getLongitude());
        return data;
    }
}
