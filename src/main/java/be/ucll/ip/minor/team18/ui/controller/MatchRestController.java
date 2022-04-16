package be.ucll.ip.minor.team18.ui.controller;

import be.ucll.ip.minor.team18.model.entity.Match;
import be.ucll.ip.minor.team18.model.service.MatchService;
import be.ucll.ip.minor.team18.util.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/match")
public class MatchRestController {

    @Autowired
    private MatchService matchService;

    @GetMapping("/all")
    public Iterable<Match> getAllMatches() {
        return matchService.getAllMatches();
    }


    @PostMapping("/add")
    public Iterable<Match> addMatch(@Valid @RequestBody Match match) {
        try {
            matchService.addMatch(match);
        }
        catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "description", e);
        }
        return matchService.getAllMatches();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteMatch(@PathVariable("id") long id) {
        try {
            matchService.deleteMatch(id);
        }
        catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "description", e);
        }
    }

    @PostMapping("/update/{id}")
    public Iterable<Match> updateMatch(@PathVariable("id") long id, @Valid @RequestBody Match match) {
        try {
            match.setId(id);
            matchService.updateMatch(match);
        }
        catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "description", e);
        }
        return matchService.getAllMatches();
    }

    @PostMapping("/match_with_description")
    public Match getMatchWithDescription(@Valid @RequestParam(value = "Description",required = false) String description) {
        Match match;
        try {
           match = matchService.getMatchWithDescription(description);
        }
        catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "description", e);
        }
        return match;
    }

    @PostMapping("/matches_with_location")
    public Iterable<Match> getMatchesWithLocation(@RequestParam(value = "Location",required = false) String location){
        Iterable<Match> matches;
        try {
           matches = matchService.getAllMatchesWithLocation(location);
        }
        catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "location", e);
        }
        return matches;
    }
    @PostMapping("/matches_with_description_containing")
    public Iterable<Match> getMatchesWithDescriptionContaining(@RequestParam(value = "Description",required = false) String description){
        Iterable<Match> matches;
        try {
            matches = matchService.getAllMatchesWithDescriptionThatContainsString(description);
        }
        catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "description", e);
        }
        return matches;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, ResponseStatusException.class})
    public Map<String, String> handleValidationEceptions(Exception e) {
        Map<String, String> errors = new HashMap<>();
        if (e instanceof MethodArgumentNotValidException) {
            ((MethodArgumentNotValidException)e).getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
        } else {
            errors.put(((ResponseStatusException)e).getReason(), e.getCause().getMessage());
        }
        return errors;
    }

}
