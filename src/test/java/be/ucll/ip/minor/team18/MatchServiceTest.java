package be.ucll.ip.minor.team18;

import be.ucll.ip.minor.team18.model.entity.Match;
import be.ucll.ip.minor.team18.model.repository.MatchRepository;
import be.ucll.ip.minor.team18.model.service.MatchService;
import be.ucll.ip.minor.team18.util.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class MatchServiceTest {

    @TestConfiguration
    static class MatchServiceIntegrationTestContextConfiguration  {
        @Bean
       public MatchService basketballService(){
            return new MatchService();
        }
    }

    @Autowired
    private MatchService MatchService;

    @MockBean
    private MatchRepository MatchRepository;

    @Test
    public void givenMatchRegistered_whenFindByDescription_thenMatchFound() {
        // Given match is registered
        Match matchInLeuven = MatchBuilder.aMatchInLeuven().build();
        Mockito.when(MatchRepository.findMatchByDescription(matchInLeuven.getDescription()))
                .thenReturn(matchInLeuven);

        // When find match with description
        String description = "Leuven - Mechelen";
        Match found = MatchService.getMatchWithDescription(description);

        // Then match should be found
        assertEquals(description, found.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenMatchRegistered_whenFindByDescription_andEmptyDescription_thenThrowException() {
        // Given match is registered
        Match matchInLeuven = MatchBuilder.aMatchInLeuven().build();
        Mockito.when(MatchRepository.findMatchByDescription(matchInLeuven.getDescription()))
                .thenReturn(matchInLeuven);

        // When find match with description
        // And given description is empty
        String description = "";
        Match found = MatchService.getMatchWithDescription(description);

        // Then an exception is thrown
    }

    @Test
    public void givenMatchRegistered_whenFindByDescriptionContainingString_thenMatchFound() {
        // Given match is registered
        Match matchInLeuven = MatchBuilder.aMatchInLeuven().build();
        Match matchInAntwerpen = MatchBuilder.aMatchInAntwerpen().build();
        Mockito.when(MatchRepository.findMatchesByDescriptionContaining("antwerpen"))
                .thenReturn(Arrays.asList(matchInAntwerpen));

        // When find matches by description that contains string
        String description = "antwerpen";
        List<Match> found = MatchService.getAllMatchesWithDescriptionThatContainsString(description);

        // Then match should be found
        assertEquals(found.get(0).getDescription(), matchInAntwerpen.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenMatchRegistered_whenFindByDescriptionContainingString_andEmptyDescription_thenThrowException() {
        // Given match is registered
        Match matchInLeuven = MatchBuilder.aMatchInLeuven().build();
        Match matchInAntwerpen = MatchBuilder.aMatchInAntwerpen().build();
        Mockito.when(MatchRepository.findMatchesByDescriptionContaining("antwerpen"))
                .thenReturn(Arrays.asList(matchInAntwerpen));

        // When find matches with description that contains string
        // And given string is empty
        String description = "";
        List<Match> found = MatchService.getAllMatchesWithDescriptionThatContainsString(description);

        //Then an exception is thrown
    }

    @Test
    public void givenMatchRegistered_whenFindByLocation_thenMatchFound() {
        // Given match is registered
        Match matchInLeuven = MatchBuilder.aMatchInLeuven().build();
        Mockito.when(MatchRepository.findMatchesByLocation(matchInLeuven.getLocation().toLowerCase()))
                .thenReturn(Arrays.asList(matchInLeuven));

        // When find matches with location
        String location = "Leuven";
        List<Match> found = MatchService.getAllMatchesWithLocation(location);

        // Then matches should be found
        assertEquals(location, found.get(0).getLocation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenMatchRegistered_whenFindByLocation_andEmptyLocation_thenThrowException() {
        // Given match is registered
        Match leuven = MatchBuilder.aMatchInLeuven().build();
        Mockito.when(MatchRepository.findMatchesByLocation(leuven.getLocation()))
                .thenReturn(Arrays.asList(leuven));

        // When find matches with location
        // And given location is empty
        String description = "";
        List<Match> found = MatchService.getAllMatchesWithLocation(description);

        // Then an exception is thrown
    }

    @Test(expected = ServiceException.class)
    public void givenMatchRegistered_whenAddMatch_andDescriptionAlreadyInUse_thenExceptionIsThrown()  {
        // Given match is registered
        Match matchInLeuven = MatchBuilder.aMatchInLeuven().build();
        Mockito.when(MatchRepository.save(matchInLeuven)).thenReturn(matchInLeuven);
        MatchService.addMatch(matchInLeuven);
        Mockito.when(MatchRepository.save(matchInLeuven)).thenThrow(ServiceException.class);

        // When adding a match
        // And match description already used by another match
        MatchService.addMatch(matchInLeuven);

        // Then an exception is thrown
    }

    @Test
    public void givenMatchRegistered_whenUpdateMatch_thenUpdatedMatchFound()  {
        // Given match is registered
        Match matchInLeuven = MatchBuilder.aMatchInLeuven().build();
        matchInLeuven.setId(1L);
        Mockito.when(MatchRepository.save(matchInLeuven)).thenReturn(matchInLeuven);
        MatchService.addMatch(matchInLeuven);
        matchInLeuven.setAmountOfVisitors(40);
        Mockito.when(MatchRepository.save(matchInLeuven)).thenReturn(matchInLeuven);

        // When match is updated
        Match update =MatchService.updateMatch(matchInLeuven);

        // Then updated match is found
        assertEquals(update.getAmountOfVisitors(),40);
    }

    @Test(expected = ServiceException.class)
    public void givenMatchRegistered_whenUpdateMatch_andUpdatedDescriptionAlreadyInUse_thenExceptionIsThrown()  {
        // Given match is registered
        Match matchInLeuven = MatchBuilder.aMatchInLeuven().build();
        matchInLeuven.setId(1L);
        Match matchInAntwerpen = MatchBuilder.aMatchInAntwerpen().build();
        matchInAntwerpen.setId(2L);
        Mockito.when(MatchRepository.save(matchInLeuven)).thenReturn(matchInLeuven);
        MatchService.addMatch(matchInLeuven);

        Mockito.when(MatchRepository.save(matchInAntwerpen)).thenReturn(matchInAntwerpen);
        MatchService.addMatch(matchInAntwerpen);

        Mockito.when(MatchRepository.findMatchByDescription(matchInAntwerpen.getDescription()))
                .thenReturn(matchInAntwerpen);
        Match antwerpenUpdated = MatchService.getMatchWithDescription("Leuven - Antwerpen");

        Mockito.when(MatchRepository.save(antwerpenUpdated)).thenThrow(ServiceException.class);
        antwerpenUpdated.setDescription(matchInLeuven.getDescription());

        // When Match is updated
        // And updated description already in use
        Match update =MatchService.updateMatch(antwerpenUpdated);
    }

    @Test
    public void givenMatchesRegistered_whenGetAllMatches_thenMatchListFound()  {
        // Given multiple matches are registered
        Match matchInLeuven = MatchBuilder.aMatchInLeuven().build();
        matchInLeuven.setId(1L);
        Match matchInAntwerpen = MatchBuilder.aMatchInAntwerpen().build();
        matchInAntwerpen.setId(2L);
        Mockito.when(MatchRepository.save(matchInLeuven)).thenReturn(matchInLeuven);
        MatchService.addMatch(matchInLeuven);

        Mockito.when(MatchRepository.save(matchInAntwerpen)).thenReturn(matchInAntwerpen);
        MatchService.addMatch(matchInAntwerpen);
        Mockito.when(MatchRepository.findAll()).thenReturn(Arrays.asList(matchInLeuven,matchInAntwerpen));

        // When getting all matches
        List<Match> update =MatchService.getAllMatches();

        // Then a list with all registered matches is found
        assertEquals(update,Arrays.asList(matchInLeuven,matchInAntwerpen));
    }
    @Test
    public void givenMatchRegistered_whenDelete_thenListMatchIsFound()  {
        // Given match is registered
        Match leuven = MatchBuilder.aMatchInLeuven().build();
        leuven.setId(1L);
        Match antwerpen = MatchBuilder.aMatchInAntwerpen().build();
        antwerpen.setId(2L);
        Mockito.when(MatchRepository.save(leuven)).thenReturn(leuven);
        MatchService.addMatch(leuven);

        Mockito.when(MatchRepository.save(antwerpen)).thenReturn(antwerpen);
        MatchService.addMatch(antwerpen);


        Mockito.when(MatchRepository.findMatchById(2L)).thenReturn(antwerpen);
        // When match is deleted
        MatchService.deleteMatch(2L);

        //Then list with all matches doesn't contain deleted match
        Mockito.when(MatchRepository.findAll()).thenReturn(Arrays.asList(leuven));
        List<Match> all =MatchService.getAllMatches();

        assertEquals(all,Arrays.asList(leuven));
    }

}
