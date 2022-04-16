package be.ucll.ip.minor.team18;

import be.ucll.ip.minor.team18.model.entity.Match;
import be.ucll.ip.minor.team18.model.service.MatchService;
import be.ucll.ip.minor.team18.ui.controller.MatchRestController;
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
@WebMvcTest(MatchRestController.class)
public class MatchRestControllerTest {

    @MockBean
    private MatchService matchService;

    @Autowired
    private MockMvc matchRestController;

    private Match leuven,antwerpen;

    @Before
    public void setup(){
        leuven =MatchBuilder.aMatchInLeuven().build();
        leuven.setId(1L);
        antwerpen =MatchBuilder.aMatchInAntwerpen().build();
        antwerpen.setId(2L);
    }
    @Test
    public void givenMatches_whenGetRequestToAllMatchs_thenJSONwithAllMatchsReturned() throws Exception {
        // given
        List<Match> matches = Arrays.asList(leuven,antwerpen);

        // mocking
        given(matchService.getAllMatches()).willReturn(matches);

        // when
        matchRestController.perform(get("/api/match/all")
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", Is.is("Leuven - Mechelen")));
    }

    @Test
    public void givenNoMatches_whenPostRequestToAddAValidMatch_thenJSONisReturned() throws Exception {
        // mocking
        when(matchService.addMatch(any())).thenReturn(leuven);
        when(matchService.getAllMatches()).thenReturn(Arrays.asList(leuven));

        // given
        String match = "{\"description\":\"Leuven - Mechelen\",\"location\":\"Leuven\",\"amountOfVisitors\":20}";

        // when
        matchRestController.perform(post("/api/match/add")
                .content(match)
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].description", Is.is(leuven.getDescription())));
    }

    @Test
    public void givenNoMatches_whenPostRequestToAddAnInvalidMatch_thenErrorInJSONformatIsReturned() throws Exception {
        // given
        String match = "{\"description\":\"\",\"location\":\"Leuven\",\"amountOfVisitors\":20}";

        // when
        matchRestController.perform(post("/api/match/add")
                .content(match)
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description", Is.is("match.description.missing")));
    }
    @Test
    public void givenNoMatches_whenPostRequestToUpdateAValidMatch_thenJSONisReturned() throws Exception {
        // mocking
        when(matchService.updateMatch(any())).thenReturn(leuven);
        when(matchService.getAllMatches()).thenReturn(Arrays.asList(leuven));


        // given
       String matchupdated = "{\"description\":\"Leuven - Mechelen\",\"location\":\"Leuven\",\"amountOfVisitors\":100}";

        // when

        leuven.setAmountOfVisitors(100);
        matchRestController.perform(post("/api/match/update/1")
                .content(matchupdated)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].amountOfVisitors", Is.is(100)));
    }
    @Test
    public void givenNoMatches_whenPostRequestToUpdateAnInvalidMatch_thenErrorInJSONformatIsReturned() throws Exception {
        //mocking
        when(matchService.updateMatch(any())).thenThrow(new ServiceException("match.description.in.use"));

        // given
        String match = "{\"description\":\"Leuven - Antwerpen\",\"location\":\"Leuven\",\"amountOfVisitors\":20}";

        // when
        matchRestController.perform(post("/api/match/update/1")
                .content(match)
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description", Is.is("match.description.in.use")));

    }
    @Test
    public void givenNoMatches_whenPostRequestToAddAnInvalidMatchWithDuplicateDescription_thenErrorInJSONformatIsReturned() throws Exception {
        //mocking
        when(matchService.addMatch(any())).thenThrow(new ServiceException("match.description.in.use"));



        // given
                String matchleuven = "{\"description\":\"Leuven - Mechelen\",\"location\":\"Leuven\",\"amountOfVisitors\":20}";


        // when
        matchRestController.perform(post("/api/match/add")
                .content(matchleuven)
                .contentType(MediaType.APPLICATION_JSON))

                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.description", Is.is("match.description.in.use")));

    }
    @Test
    public void givenNoMatches_whenPostRequestToGetMatchesWithLocation_thenJSONisReturned() throws Exception {
        //mocking
        when(matchService.getAllMatchesWithLocation(any())).thenReturn(Arrays.asList(leuven));

        // when
        matchRestController.perform(post("/api/match/matches_with_location")
                .param("Location","Leuven"))
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].location", Is.is("Leuven")));

    }
    @Test
    public void givenNoMatches_whenPostRequestToGetMatchesWithLocation_thenErrorInJSONformatIsReturned() throws Exception {
        //mocking
        when(matchService.getAllMatchesWithLocation(any())).thenThrow(new IllegalArgumentException("matchesWithLocation.location.missing"));


        // when
        matchRestController.perform(post("/api/match/matches_with_location")
        )
                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.location", Is.is("matchesWithLocation.location.missing")));

    }
    @Test
    public void givenNoMatches_whenPostRequestToGetMatchesDescriptionContaining_thenJSONisReturned() throws Exception {
        //mocking
        when(matchService.getAllMatchesWithDescriptionThatContainsString(any())).thenReturn(Arrays.asList(antwerpen));

        // when
        matchRestController.perform(post("/api/match/matches_with_description_containing")
                .param("Description","Antwerpen"))
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].description", Is.is("Leuven - Antwerpen")));

    }
    @Test
    public void givenNoMatches_whenPostRequestToGetMatchesDescriptionContaining_thenErrorInJSONformatIsReturned() throws Exception {
        //mocking
        when(matchService.getAllMatchesWithDescriptionThatContainsString(any())).thenThrow(new IllegalArgumentException("matchesWithDescriptionThatContainsString.word.missing"));

        // when
        matchRestController.perform(post("/api/match/matches_with_description_containing")
       )
                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description", Is.is("matchesWithDescriptionThatContainsString.word.missing")));

    }
    @Test
    public void givenNoMatches_whenPostRequestToGetMatchDescription_thenJSONisReturned() throws Exception {
        //mocking
        when(matchService.getMatchWithDescription(any())).thenReturn(antwerpen);

        // when
        matchRestController.perform(post("/api/match/match_with_description")
                .param("Description","Leuven - Antwerpen"))
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description", Is.is("Leuven - Antwerpen")));

    }
    @Test
    public void givenNoMatches_whenPostRequestToGetMatchDescription_thenErrorInJSONformatIsReturned() throws Exception {
        //mocking
        when(matchService.getMatchWithDescription(any())).thenThrow(new IllegalArgumentException("match.description.missing"));

        // when
        matchRestController.perform(post("/api/match/match_with_description")
        )
                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description", Is.is("match.description.missing")));

    }
}
