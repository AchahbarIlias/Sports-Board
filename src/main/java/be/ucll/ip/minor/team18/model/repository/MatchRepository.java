package be.ucll.ip.minor.team18.model.repository;

import be.ucll.ip.minor.team18.model.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match,Long> {

    @Query("SELECT m FROM Match m WHERE lower(m.location)=lower(?1)")
    List<Match> findMatchesByLocation(String location);

    Match findMatchByDescription(String description);

    @Query("SELECT m FROM Match m WHERE lower(m.description) LIKE %?1%")
    List<Match> findMatchesByDescriptionContaining(String description);

    @Query("SELECT m FROM Match m WHERE m.description=?1 AND m.id <> ?2")
    Match findMatchByDescriptionNotSelf(String description, long id);

    List<Match> findMatchesByBusIsNull();

    int deleteById(long id);

    Match findMatchById(long id);
}
