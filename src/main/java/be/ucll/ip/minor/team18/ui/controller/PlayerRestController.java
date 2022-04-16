package be.ucll.ip.minor.team18.ui.controller;


import be.ucll.ip.minor.team18.model.entity.Player;
import be.ucll.ip.minor.team18.model.service.PlayerService;
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
@RequestMapping("/api/player")
public class PlayerRestController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/all")
    public Iterable<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @PostMapping("/add")
    public Player addPlayer(@Valid @RequestBody Player player) {
        try {
            playerService.addPlayer(player);
        }
        catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fullName", e);
        }
        return player;
    }

    @DeleteMapping("/delete/{id}")
    public void deletePlayer(@PathVariable("id") long id) {
        try {
            playerService.deletePlayer(id);
        }
        catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fullName", e);
        }
    }

    @PostMapping("/update/{id}")
    public Iterable<Player> updatePlayer(@PathVariable("id") long id, @Valid @RequestBody Player player) {
        try {
            player.setId(id);
            playerService.updatePlayer(player);
        }
        catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fullName", e);
        }
        return playerService.getAllPlayers();
    }
    @PostMapping("/player_with_name")
    public Player getPlayerWithName(@RequestParam(value = "Name",required = false) String name){
        Player player;
        try {
            player = playerService.getPlayerWithName(name);
        }
        catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fullName", e);
        }
        return player;
    }
    @PostMapping("/players_with_age")
    public Iterable<Player> getPlayersWithAge(@RequestParam(value = "age",required = false) Integer age){
        Iterable<Player> players;
        try {

            players = playerService.getAllPlayersWithAge(age);
        }
        catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "age", e);
        }
        return players;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, ResponseStatusException.class})
    public Map<String, String> handleValidationEceptions(Exception exc) {
        Map<String, String> errors = new HashMap<>();
        if (exc instanceof MethodArgumentNotValidException) {
            ((MethodArgumentNotValidException)exc).getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
        } else {
            errors.put(((ResponseStatusException)exc).getReason(), exc.getCause().getMessage());
        }
        return errors;
    }

}
