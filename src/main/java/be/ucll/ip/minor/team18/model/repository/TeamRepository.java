package be.ucll.ip.minor.team18.model.repository;

import be.ucll.ip.minor.team18.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Team findTeamByName(String name);

    Team findTeamById(long id);

    int deleteTeamById(long id);

    List<Team> findTeamsByMaxAge(int maxAge);

    List<Team> findTeamsByMinAgeAfter(int minAge);

    @Query("SELECT t FROM Team t WHERE lower(t.name) LIKE %?1%")
    List<Team> findTeamsByNameContaining(String string);

}
