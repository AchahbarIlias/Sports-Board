package be.ucll.ip.minor.team18;

import be.ucll.ip.minor.team18.model.entity.Player;
import be.ucll.ip.minor.team18.model.repository.PlayerRepository;
import be.ucll.ip.minor.team18.model.service.PlayerService;
import be.ucll.ip.minor.team18.util.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class PlayerServiceTest {

    @TestConfiguration
    static class PlayerServiceIntegrationTestContextConfiguration  {
        @Bean
       public PlayerService basketballService(){
            return new PlayerService();
        }
    }

    @Autowired
    private PlayerService playerService;

    @MockBean
    private PlayerRepository playerRepository;

    @Test
    public void givenPlayerRegistered_whenFindByFullName_thenPlayerShouldBeFound() {
        // given
        Player lucas = PlayerBuilder.aPlayerLucas().build();
        Mockito.when(playerRepository.findPlayerByFullName(lucas.getFullName()))
                .thenReturn(lucas);

        // when
        String fullName = "Lucas Cuppers";
        Player found = playerService.getPlayerWithName(fullName);

        // then
        assertEquals(found.getFullName(),fullName);
    }
    @Test(expected = IllegalArgumentException.class)
    public void givenPlayerRegistered_whenFindByFullNameWith_Empty_fullName_thenThrowException() {
        // given
        Player lucas = PlayerBuilder.aPlayerLucas().build();
        Mockito.when(playerRepository.findPlayerByFullName(lucas.getFullName()))
                .thenReturn(lucas);

        // when
        String fullName = "";
        Player found = playerService.getPlayerWithName(fullName);


    }

    @Test
    public void givenPlayerRegistered_whenFindByAge_thenPlayerShouldBeFound() {
        // given
        Player lucas = PlayerBuilder.aPlayerLucas().build();
        Mockito.when(playerRepository.findPlayersByAge(lucas.getAge()))
                .thenReturn(Arrays.asList(lucas));

        // when
        List<Player> found = playerService.getAllPlayersWithAge(25);

        // then
        assertEquals(Integer.valueOf(25), found.get(0).getAge());
    }
    @Test(expected = IllegalArgumentException.class)
    public void givenPlayerRegistered_whenFindByAgeWith_Empty_Age_thenThrowException() {
        // given
        Player lucas = PlayerBuilder.aPlayerLucas().build();
        Mockito.when(playerRepository.findPlayersByAge(lucas.getAge()))
                .thenReturn(Arrays.asList(lucas));

        // when
        Integer age = null;
        List<Player> found = playerService.getAllPlayersWithAge(age);


    }
    @Test(expected = ServiceException.class)
    public void givenplayerRegistered_whenPlayerAddedWithSameFullName_thenExceptionIsThrown()  {
        // given
        Player lucas = PlayerBuilder.aPlayerLucas().build();
        Mockito.when(playerRepository.save(lucas)).thenReturn(lucas);
        playerService.addPlayer(lucas);
        Mockito.when(playerRepository.save(lucas)).thenThrow(ServiceException.class);


        // when
        playerService.addPlayer(lucas);
    }
    @Test
    public void givenPlayerRegistered_whenPlayerUpdate_thenPlayer_is_found()  {
        // given
        Player lucas = PlayerBuilder.aPlayerLucas().build();
        lucas.setId(1L);
        Mockito.when(playerRepository.save(lucas)).thenReturn(lucas);
        playerService.addPlayer(lucas);
        lucas.setPlayerNumber(40);
        Mockito.when(playerRepository.save(lucas)).thenReturn(lucas);


        // when
        Player updatedPlayer =playerService.updatePlayer(lucas);

        //then
        assertEquals(updatedPlayer.getPlayerNumber(),Integer.valueOf(40));
        assertEquals(updatedPlayer.getId(), lucas.getId());
    }
    @Test(expected = ServiceException.class)
    public void givenPlayerRegistered_whenPlayerUpdate_with_same_fullName_thenException_is_thrown()  {
        // given
        Player lucas = PlayerBuilder.aPlayerLucas().build();
        lucas.setId(1L);
        Player gerben = PlayerBuilder.aPlayerGerben().build();
        gerben.setId(2L);
        Mockito.when(playerRepository.save(lucas)).thenReturn(lucas);
        playerService.addPlayer(lucas);

        Mockito.when(playerRepository.save(gerben)).thenReturn(gerben);
        playerService.addPlayer(gerben);

        Mockito.when(playerRepository.findPlayerByFullName(gerben.getFullName()))
                .thenReturn(gerben);
        Player gerbenupdated = playerService.getPlayerWithName("Gerben Piot");
        gerbenupdated.setLastName("Cuppers");
        gerbenupdated.setFirstName("Lucas");

        Mockito.when(playerRepository.save(gerbenupdated)).thenThrow(ServiceException.class);

        // when
        Player update =playerService.updatePlayer(gerbenupdated);


    }

    @Test
    public void givenplayerRegistered_whengetAllPlayer_thenlistPlayer_is_found()  {
        // given
        Player lucas = PlayerBuilder.aPlayerLucas().build();
        lucas.setId(1L);
        Player gerben = PlayerBuilder.aPlayerGerben().build();
        gerben.setId(2L);
        Mockito.when(playerRepository.save(lucas)).thenReturn(lucas);
        playerService.addPlayer(lucas);

        Mockito.when(playerRepository.save(gerben)).thenReturn(gerben);
        playerService.addPlayer(gerben);
    Mockito.when(playerRepository.findAll()).thenReturn(Arrays.asList(lucas,gerben));

        // when
        List<Player> update =playerService.getAllPlayers();

        //then
        assertEquals(update,Arrays.asList(lucas,gerben));
    }
    @Test
    public void givenPlayerRegistered_whenDelete_thenListPlayer_is_found()  {
        // given
        Player lucas = PlayerBuilder.aPlayerLucas().build();
        lucas.setId(1L);
        Player gerben = PlayerBuilder.aPlayerGerben().build();
        gerben.setId(2L);
        Mockito.when(playerRepository.save(lucas)).thenReturn(lucas);
        playerService.addPlayer(lucas);

        playerService.addPlayer(gerben);

        Mockito.when(playerRepository.findPlayerById(2L)).thenReturn(gerben);


    // when
       // Mockito.when(playerRepository.deletePlayerById(1L));
        playerService.deletePlayer(2L);


        Mockito.when(playerRepository.findAll()).thenReturn(Arrays.asList(lucas));
        List<Player> all =playerService.getAllPlayers();

        //then
        assertEquals(all,Arrays.asList(lucas));
    }

}
