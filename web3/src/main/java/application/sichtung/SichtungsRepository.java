package application.sichtung;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * JpaRepository matching comment object to the respective entities in the database
 */
public interface SichtungsRepository extends JpaRepository<Sichtung,Long> {

    /**
     * 
     * @param date
     * @return
     */
    List<Sichtung> findByDate(LocalDateTime date);
}
