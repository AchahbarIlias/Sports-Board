package be.ucll.ip.minor.team18;

import be.ucll.ip.minor.team18.model.entity.Player;
import be.ucll.ip.minor.team18.model.service.PlayerService;
import be.ucll.ip.minor.team18.ui.controller.PlayerRestController;
import be.ucll.ip.minor.team18.util.ServiceException;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(PlayerRestController.class)
public class PlayerRestControllerTest {

        @MockBean
    PlayerService playerService;

        @Autowired
        private MockMvc playerRestController;

        private Player lucas, gerben;

        @Before
        public void setUp() {
            lucas = PlayerBuilder.aPlayerLucas().build();
            gerben = PlayerBuilder.aPlayerGerben().build();
        }

        @Test
        public void givenPlayers_whenGetRequestToAllPlayers_thenJSONwithAllPlayersReturned() throws Exception {
            // given
            List<Player> players = Arrays.asList(lucas, gerben);

            // mocking
            given(playerService.getAllPlayers()).willReturn(players);

            // when
            playerRestController.perform(get("/api/player/all")
                    .contentType(MediaType.APPLICATION_JSON))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].firstName", Is.is("Lucas")));
        }

        @Test
        public void givenNoPlayers_whenPostRequestToAddAValidPlayer_thenJSONisReturned() throws Exception {
            // mocking
            when(playerService.addPlayer(any())).thenReturn((lucas));

            // given
            String player = "{\"firstName\":\"Lucas\",\"lastName\":\"Cuppers\",\"playerNumber\":12,\"age\":25}";

            // when
            playerRestController.perform(post("/api/player/add")
                    .content(player)
                    .contentType(MediaType.APPLICATION_JSON))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(content()
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.fullName", Is.is(lucas.getFullName())));
        }

        @Test
        public void givenNoPlayers_whenPostRequestToAddAnInvalidPlayer_thenErrorInJSONformatIsReturned() throws Exception {
            // given
            String player = "{\"firstName\":\"\",\"lastName\":\"Cuppers\",\"playerNumber\":12,\"age\":25}";

            // when
            playerRestController.perform(post("/api/player/add")
                    .content(player)
                    .contentType(MediaType.APPLICATION_JSON))
                    // then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.firstName", Is.is("player.firstname.missing")));
        }
        @Test
        public void givenNoPlayers_whenPostRequestToUpdateAValidplayer_thenJSONisReturned() throws Exception {
            // mocking
            when(playerService.updatePlayer(any())).thenReturn(lucas);
            when(playerService.getAllPlayers()).thenReturn(Arrays.asList(lucas));


            // given
            String playerupdated = "{\"firstName\":\"Lucas\",\"lastName\":\"Cuppers\",\"playerNumber\":100,\"age\":25}";

            // when

            lucas.setPlayerNumber(100);
            playerRestController.perform(post("/api/player/update/1")
                    .content(playerupdated)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content()
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].playerNumber", Is.is(100)));
        }
        @Test
        public void givenNoPlayers_whenPostRequestToUpdateAnInvalidplayer_thenErrorInJSONformatIsReturned() throws Exception {
            //mocking
            when(playerService.updatePlayer(any())).thenThrow(new ServiceException("player.name.in.use"));

            // given
            String player = "{\"firstName\":\"Lucas\",\"lastName\":\"Cuppers\",\"playerNumber\":12,\"age\":25}";

            // when
            playerRestController.perform(post("/api/player/update/1")
                    .content(player)
                    .contentType(MediaType.APPLICATION_JSON))
                    // then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.fullName", Is.is("player.name.in.use")));

        }
        @Test
        public void givenNoPlayers_whenPostRequestToAddAnInvalidplayerWithDuplicatefullName_thenErrorInJSONformatIsReturned() throws Exception {
            //mocking
            when(playerService.addPlayer(any())).thenThrow(new ServiceException("player.name.in.use"));



            // given
            String player = "{\"firstName\":\"Lucas\",\"lastName\":\"Cuppers\",\"playerNumber\":12,\"age\":25}";


            // when
            playerRestController.perform(post("/api/player/add")
                    .content(player)
                    .contentType(MediaType.APPLICATION_JSON))

                    // then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.fullName", Is.is("player.name.in.use")));

        }
        @Test
        public void givenNoPlayers_whenPostRequestToGetPlayersWithAge_thenJSONisReturned() throws Exception {
            //mocking
            when(playerService.getAllPlayersWithAge(any())).thenReturn(Arrays.asList(lucas));

            // when
            playerRestController.perform(post("/api/player/players_with_age")
                    .param("age","25"))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].age", Is.is(25)));

        }
        @Test
        public void givenNoPlayers_whenPostRequestToGetPlayersWithAge_thenErrorInJSONformatIsReturned() throws Exception {
            //mocking
            when(playerService.getAllPlayersWithAge(any())).thenThrow(new IllegalArgumentException("PlayersWithAge.Age.missing"));


            // when
            playerRestController.perform(post("/api/player/players_with_age")
            )
                    // then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.age", Is.is("PlayersWithAge.Age.missing")));

        }

        @Test
        public void givenNoPlayers_whenPostRequestToGetPlayerfullName_thenJSONisReturned() throws Exception {
            //mocking
            when(playerService.getPlayerWithName(any())).thenReturn(lucas);

            // when
            playerRestController.perform(post("/api/player/player_with_name")
                    .param("fullName","lucas cuppers"))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.fullName", Is.is("Lucas Cuppers")));

        }
        @Test
        public void givenNoPlayers_whenPostRequestToGetPlayerfullName_thenErrorInJSONformatIsReturned() throws Exception {
            //mocking
            when(playerService.getPlayerWithName(any())).thenThrow(new IllegalArgumentException("playerWithName.name.missing"));

            // when
            playerRestController.perform(post("/api/player/player_with_name")
            )
                    // then
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.fullName", Is.is("playerWithName.name.missing")));

        }

    }





