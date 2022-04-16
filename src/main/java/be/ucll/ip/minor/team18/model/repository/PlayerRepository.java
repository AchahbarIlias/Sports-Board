package be.ucll.ip.minor.team18.model.repository;

import be.ucll.ip.minor.team18.model.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository  extends JpaRepository<Player, Long> {

    @Query("SELECT p FROM Player p WHERE concat(lower(p.firstName), ' ', lower(p.lastName))=lower(?1)")
    Player findPlayerByFullName(String fullName);

    @Query("SELECT p FROM Player p WHERE concat(lower(p.firstName), ' ', lower(p.lastName))=lower(?1) AND p.id <> ?2")
    Player findPlayerByFullNameNotSelf(String fullName, long id);

    List<Player> findPlayersByAge(Integer age);

    @Query("SELECT p FROM Player p WHERE p.id = ?1")
    Player findPlayerById(long id);

    List<Player> findPlayersByTeamIsNull();

    int deletePlayerById(long id);
}
