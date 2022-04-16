package be.ucll.ip.minor.team18.model.service;

import be.ucll.ip.minor.team18.model.entity.Player;
import be.ucll.ip.minor.team18.model.repository.PlayerRepository;
import be.ucll.ip.minor.team18.util.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public Player addPlayer(Player player) throws ServiceException {
        if (playerRepository.findPlayerByFullName(player.getFullName())!=null) throw new ServiceException("player.name.in.use");
        else return playerRepository.save(player);
    }

    public Player updatePlayer(Player player) throws ServiceException {
        if(playerRepository.findPlayerByFullNameNotSelf(player.getFullName(), player.getId()) !=null) throw new ServiceException("player.name.in.use");
        else return playerRepository.save(player);
    }

    @Transactional
    public void deletePlayer(long id) {
        if(playerRepository.findPlayerById(id).getTeam() != null) throw new IllegalArgumentException("delete.error.player.in.team");
        else playerRepository.deleteById(id);
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public List<Player> getAllPlayersWithAge(Integer age) {
        if(age == null) throw new IllegalArgumentException("playersWithAge.age.missing");
        else return playerRepository.findPlayersByAge(age);
    }

    public Player getPlayerWithName(String name) {
        if(name.isEmpty()) throw new IllegalArgumentException("playerWithName.name.missing");
        else return playerRepository.findPlayerByFullName(name);
    }

    public Player getPlayerWithId(long id) {
        if(playerRepository.findPlayerById(id) == null) throw new IllegalArgumentException("player.not.in.team");
        else return playerRepository.findPlayerById(id);
    }

    public List<Player> getPlayersWithoutTeam() {
        if(playerRepository.findPlayersByTeamIsNull().isEmpty()) throw new ServiceException("empty.players.without.team");
        else return playerRepository.findPlayersByTeamIsNull();
    }

    public Optional<Player> findPlayerById(long id) {
        return playerRepository.findById(id);
    }

}
