package application.sichtung;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

interface SichtungsRepository extends JpaRepository<Sichtung,Long> {

    List<Sichtung> findByDate(LocalDateTime date);

}
