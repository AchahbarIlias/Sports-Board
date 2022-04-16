package be.ucll.ip.minor.team18;

import be.ucll.ip.minor.team18.model.entity.Match;
import be.ucll.ip.minor.team18.model.repository.MatchRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MatchRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MatchRepository matchRepository;


    @Test
    public void givenMatchRegistered_whenFindByDescription_thenReturnMatch() {
        // Given
        Match matchLeuven = MatchBuilder.aMatchInLeuven().build();
        Match matchAntwerpen = MatchBuilder.aMatchInAntwerpen().build();
        entityManager.persistAndFlush(matchLeuven);
        entityManager.persistAndFlush(matchAntwerpen);

        // When
        Match found = matchRepository.findMatchByDescription(matchLeuven.getDescription());

        // Then
        assertThat(found.getDescription()).isEqualTo(matchLeuven.getDescription());
    }

    @Test
    public void givenMatchRegistered_whenFindByLocation_thenReturnMatch() {
        // Given
        Match matchInLeuven = MatchBuilder.aMatchInLeuven().build();
        Match matchInAntwerpen = MatchBuilder.aMatchInAntwerpen().build();
        entityManager.persistAndFlush(matchInLeuven);
        entityManager.persistAndFlush(matchInAntwerpen);

        // When
        List<Match> found = matchRepository.findMatchesByLocation(matchInLeuven.getLocation());

        // Then
        assertThat(found.get(0).getLocation()).isEqualTo(matchInLeuven.getLocation());
    }

    @Test
    public void givenMatchRegistered_whenFindByDescriptionContainingString_thenReturnMatches() {
        // Given
        Match matchInLeuven = MatchBuilder.aMatchInLeuven().build();
        Match matchInAntwerpen = MatchBuilder.aMatchInAntwerpen().build();
        entityManager.persistAndFlush(matchInLeuven);
        entityManager.persistAndFlush(matchInAntwerpen);

        // When
        List<Match> found = matchRepository.findMatchesByDescriptionContaining("leuven");

        // Then
        assertThat(found.get(0).getDescription()).isEqualTo(matchInLeuven.getDescription());
    }

    @Test
    public void givenMatchRegistered_whenFindByDescriptionNotSelf_thenReturnMatch() {
        // Given
        Match matchLeuven = MatchBuilder.aMatchInLeuven().build();
        Match matchAntwerpen = MatchBuilder.aMatchInAntwerpen().build();
        entityManager.persistAndFlush(matchLeuven);
        entityManager.persistAndFlush(matchAntwerpen);
        long id = entityManager.getId(matchLeuven,Long.class);

        // When
        Match found = matchRepository.findMatchByDescriptionNotSelf(matchLeuven.getDescription(),id);

        // Then
        assertNull(found);
    }

    @Test
    public void givenMatchRegistered_whenDeleteMatch_thenMatchDeleted(){
        // Given
        Match matchLeuven = MatchBuilder.aMatchInLeuven().build();
        Match matchAntwerpen = MatchBuilder.aMatchInAntwerpen().build();
        entityManager.persistAndFlush(matchLeuven);
        entityManager.persistAndFlush(matchAntwerpen);
        Long id = entityManager.getId(matchLeuven,Long.class);

        // When
        matchRepository.deleteById(id);
        List<Match> found = matchRepository.findAll();

        // Then
        assertEquals(found, Arrays.asList(matchAntwerpen));
    }

}
