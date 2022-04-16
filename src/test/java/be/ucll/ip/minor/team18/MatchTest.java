package be.ucll.ip.minor.team18;

import be.ucll.ip.minor.team18.model.entity.Match;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MatchTest {
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
    public void givenValidMatch_shouldHaveNoViolations(){
        Match matchInLeuven = MatchBuilder.aMatchInLeuven().build();

        Set<ConstraintViolation<Match>> violations = validator.validate(matchInLeuven);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void givenMatchWithEmptyDescription_shouldDetectInvalidDescription() {
        Match fault = new Match();
        fault.setDescription("");
        fault.setLocation("Leuven");
        fault.setAmountOfVisitors(15);

        Set<ConstraintViolation<Match>> violations = validator.validate(fault);

        assertEquals(violations.size(), 1);

        ConstraintViolation<Match> violation = violations.iterator().next();
        assertEquals("match.description.missing", violation.getMessage());
        assertEquals("description", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }

    @Test
    public void givenMatchWithTooLongDescriptionLength60_shouldDetectInvalidDescription() {
        Match fault = new Match();
        fault.setDescription("x".repeat(60));
        fault.setLocation("Leuven");
        fault.setAmountOfVisitors(15);

        Set<ConstraintViolation<Match>> violations = validator.validate(fault);

        assertEquals(violations.size(), 1);

        ConstraintViolation<Match> violation = violations.iterator().next();
        assertEquals("match.description.long", violation.getMessage());
        assertEquals("description", violation.getPropertyPath().toString());
        assertEquals("x".repeat(60), violation.getInvalidValue());
    }

    @Test
    public void givenMatchWithTooLongDescriptionLength51_shouldDetectInvalidDescription() {
        Match fault = new Match();
        fault.setDescription("x".repeat(51));
        fault.setLocation("Leuven");
        fault.setAmountOfVisitors(15);

        Set<ConstraintViolation<Match>> violations = validator.validate(fault);

        assertEquals(violations.size(), 1);

        ConstraintViolation<Match> violation = violations.iterator().next();
        assertEquals("match.description.long", violation.getMessage());
        assertEquals("description", violation.getPropertyPath().toString());
        assertEquals("x".repeat(51), violation.getInvalidValue());
    }

    @Test
    public void givenMatchWithEmptyLocation_shouldDetectInvalidLocation() {
        Match fault = new Match();
        fault.setDescription("x");
        fault.setLocation("");
        fault.setAmountOfVisitors(15);

        Set<ConstraintViolation<Match>> violations = validator.validate(fault);

        assertEquals(violations.size(), 1);

        ConstraintViolation<Match> violation = violations.iterator().next();
        assertEquals("match.location.missing", violation.getMessage());
        assertEquals("location", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }

    @Test
    public void givenMatchWithNegativeAmountOfVisitors_shouldDetectInvalidAmountOfVisitors() {
        Match fault = new Match();
        fault.setDescription("x");
        fault.setLocation("Leuven");
        fault.setAmountOfVisitors(-20);

        Set<ConstraintViolation<Match>> violations = validator.validate(fault);

        assertEquals(violations.size(), 1);

        ConstraintViolation<Match> violation = violations.iterator().next();
        assertEquals("match.amount.positive", violation.getMessage());
        assertEquals("amountOfVisitors", violation.getPropertyPath().toString());
        assertEquals(-20, violation.getInvalidValue());
    }

    @Test
    public void givenMatchWith0AmountOfVisitors_shouldDetectInvalidAmountOfVisitors() {
        Match fault = new Match();
        fault.setDescription("x");
        fault.setLocation("Leuven");
        fault.setAmountOfVisitors(0);

        Set<ConstraintViolation<Match>> violations = validator.validate(fault);

        assertEquals(violations.size(), 1);

        ConstraintViolation<Match> violation = violations.iterator().next();
        assertEquals("match.amount.positive", violation.getMessage());
        assertEquals("amountOfVisitors", violation.getPropertyPath().toString());
        assertEquals(0, violation.getInvalidValue());
    }

    @Test
    public void givenMatchWithnNoAmountOfVisitors_shouldDetectInvalidAmountOfVisitors() {
        Match fault = new Match();
        fault.setDescription("x");
        fault.setLocation("Leuven");

        //when
        Set<ConstraintViolation<Match>> violations = validator.validate(fault);

        //then
        assertEquals(violations.size(), 1);

        ConstraintViolation<Match> violation = violations.iterator().next();
        assertEquals("match.amount.missing", violation.getMessage());
        assertEquals("amountOfVisitors", violation.getPropertyPath().toString());
        assertEquals(null, violation.getInvalidValue());
    }

}
