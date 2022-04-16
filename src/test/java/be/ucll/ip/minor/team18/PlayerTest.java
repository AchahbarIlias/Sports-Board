package be.ucll.ip.minor.team18;

import be.ucll.ip.minor.team18.model.entity.Player;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.*;

public class PlayerTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;


    @BeforeClass
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterClass
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void givenValidPlayer_shouldHaveNoViolations(){
        Player lucas = PlayerBuilder.aPlayerLucas().build();

        Set<ConstraintViolation<Player>> violations = validator.validate(lucas);

        assertTrue(violations.isEmpty());

    }
    @Test
    public void givenPlayerWithEmptyFirstName_shouldDetectInvalidFirstName() {
        //given
        Player slecht = new Player();
        slecht.setFirstName("");
        slecht.setLastName("leuven");
        slecht.setAge(15);
        slecht.setPlayerNumber(7);

        //when
        Set<ConstraintViolation<Player>> violations = validator.validate(slecht);

        //then
        assertEquals(violations.size(), 1);

        ConstraintViolation<Player> violation = violations.iterator().next();
        assertEquals("player.firstname.missing", violation.getMessage());
        assertEquals("firstName", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }
    @Test
    public void givenPlayerWithEmptyLastName_shouldDetectInvalidLastName() {
        //given
        Player slecht = new Player();
        slecht.setFirstName("leuven");
        slecht.setLastName("");
        slecht.setAge(15);
        slecht.setPlayerNumber(7);

        //when
        Set<ConstraintViolation<Player>> violations = validator.validate(slecht);

        //then
        assertEquals(violations.size(), 1);

        ConstraintViolation<Player> violation = violations.iterator().next();
        assertEquals("player.lastname.missing", violation.getMessage());
        assertEquals("lastName", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }


    @Test
    public void givenPlayerWithEmptyAge_shouldDetectInvalidAge() {
        //given
        Player slecht = new Player();
        slecht.setLastName("aaa");
        slecht.setFirstName("bbbb");
        slecht.setPlayerNumber(15);

        //when
        Set<ConstraintViolation<Player>> violations = validator.validate(slecht);

        //then
        assertEquals(violations.size(), 1);

        ConstraintViolation<Player> violation = violations.iterator().next();
        assertEquals("player.age.missing", violation.getMessage());
        assertEquals("age", violation.getPropertyPath().toString());
        assertNull(violation.getInvalidValue());
    }
    @Test
    public void givenPlayerWithEmptyPlayerNumber_shouldDetectInvalidPlayerNumber() {
        //given
        Player slecht = new Player();
        slecht.setLastName("aaa");
        slecht.setFirstName("bbbb");
        slecht.setAge(15);

        //when
        Set<ConstraintViolation<Player>> violations = validator.validate(slecht);

        //then
        assertEquals(violations.size(), 1);

        ConstraintViolation<Player> violation = violations.iterator().next();
        assertEquals("player.number.missing", violation.getMessage());
        assertEquals("playerNumber", violation.getPropertyPath().toString());
        assertNull(violation.getInvalidValue());
    }

    @Test
    public void givenPlayerWithNegatiefAge_shouldDetectInvalidAge() {
        //given
        Player slecht = new Player();
        slecht.setFirstName("aaaaa");
        slecht.setLastName("leuven");
        slecht.setAge(-20);
        slecht.setPlayerNumber(7);


        //when
        Set<ConstraintViolation<Player>> violations = validator.validate(slecht);

        //then
        assertEquals(violations.size(), 1);

        ConstraintViolation<Player> violation = violations.iterator().next();
        assertEquals("player.age.negative", violation.getMessage());
        assertEquals("age", violation.getPropertyPath().toString());
        assertEquals(-20, violation.getInvalidValue());
    }


}
