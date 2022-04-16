package be.ucll.ip.minor.team18.ui.controller;

import be.ucll.ip.minor.team18.model.entity.Team;
import be.ucll.ip.minor.team18.model.service.BasketballService;
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
@RequestMapping("/api/team")
public class TeamRestController {

    @Autowired
    private BasketballService basketballService;

    @GetMapping("/all")
    public Iterable<Team> getAllTeames() {
        return basketballService.getAllTeams();
    }

    @GetMapping("/all/namesort")
    public Iterable<Team> getAllTeamesSortedByName() {
        return basketballService.getAllTeamsSortedByName();
    }

    @GetMapping("/all/playersort")
    public Iterable<Team> getAllTeamesSortedByEmail() {
        return basketballService.getAllTeamsSortedByPlayers();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteMatch(@PathVariable("id") long id) {
        basketballService.deleteTeam(id);
    }

    @PostMapping("/add")
    public Team addTeam(@Valid @RequestBody Team Team) {

        try{
            basketballService.addTeam(Team);

        }catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "TeamError", e);
        }
        return Team;
    }

    @PostMapping("/update/{id}")
    public Team updateTeam(@PathVariable("id") long id, @Valid @RequestBody Team Team) {
        try{
            Team.setId(id);
            basketballService.updateTeam(Team);

        }catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "TeamError", e);
        }
        return Team;
    }

    @PostMapping("/showTeamsWithNameThatContainsString")
    public Iterable<Team> showTeamsWithNameThatContainsString(@RequestParam("word") String word) {
        Iterable<Team> Teams;
        try {
            Teams = basketballService.getTeamsWithNameThatContainsString(word);
        }
        catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "word", e);
        }
        return Teams;
    }

    @PostMapping("/showTeamsWithAgeAbove")
    public Iterable<Team> showTeamsWithAgeAbove(@RequestParam("age") String age){
        Iterable<Team> Teames;
        try {
            Teames = basketballService.getTeamsWithAgeAbove(age);
        }
        catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "age", e);
        }
        return Teames;
    }

    @PostMapping("/showTeamsWithMaximumAge")
    public Iterable<Team> showTeamsWithMaximumAge(@RequestParam("maxAge") String maxage) {
        Iterable<Team> Teams;
        try {
            Teams = basketballService.getTeamsWithMaxAge(maxage);
        }
        catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MaxAge", e);
        }
        return Teams;

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
