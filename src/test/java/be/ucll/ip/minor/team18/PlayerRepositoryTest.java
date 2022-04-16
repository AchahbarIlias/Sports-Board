package be.ucll.ip.minor.team18;

import be.ucll.ip.minor.team18.model.entity.Player;
import be.ucll.ip.minor.team18.model.repository.PlayerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PlayerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    public void givenPlayerRegistered_whenFindByFullName_thenReturnPlayer() {
        // given
        Player lucas = PlayerBuilder.aPlayerLucas().build();
        Player gerben = PlayerBuilder.aPlayerGerben().build();
        entityManager.persistAndFlush(lucas);
        entityManager.persistAndFlush(gerben);

        // when
        Player found = playerRepository.findPlayerByFullName(lucas.getFullName());

        // then
        assertThat(found.getFullName()).isEqualTo(lucas.getFullName());
    }

    @Test
    public void givenPlayerRegistered_whenFindByAge_thenReturnPlayer() {
        // given
        Player lucas = PlayerBuilder.aPlayerLucas().build();
        Player gerben = PlayerBuilder.aPlayerGerben().build();
        entityManager.persistAndFlush(lucas);
        entityManager.persistAndFlush(gerben);

        // when
        List<Player> found = playerRepository.findPlayersByAge(lucas.getAge());

        // then
        assertThat(found.get(0).getAge()).isEqualTo(lucas.getAge());
    }


    @Test
    public void givenPlayerRegistered_whenFindByFullNameNotSelf_thenReturnPlayer() {
        // given
        Player lucas = PlayerBuilder.aPlayerLucas().build();
        Player gerben = PlayerBuilder.aPlayerGerben().build();
        entityManager.persistAndFlush(lucas);
        entityManager.persistAndFlush(gerben);
        long id = entityManager.getId(lucas,Long.class);

        // when
        Player found = playerRepository.findPlayerByFullNameNotSelf(lucas.getFullName(),id);

        // then
        assertNull(found);

    }

    @Test
    public void givenPlayerRegistered_whenDelete_thenNoMorePLayer(){
        // given
        Player lucas = PlayerBuilder.aPlayerLucas().build();
        Player gerben = PlayerBuilder.aPlayerGerben().build();
        entityManager.persistAndFlush(lucas);
        entityManager.persistAndFlush(gerben);
        Long id = entityManager.getId(lucas,Long.class);

        // when
        playerRepository.deleteById(id);
        List<Player> found = playerRepository.findAll();

        // then
        assertEquals(found, Arrays.asList(gerben));
    }

}
