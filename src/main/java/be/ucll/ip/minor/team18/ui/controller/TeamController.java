package be.ucll.ip.minor.team18.ui.controller;

import be.ucll.ip.minor.team18.model.entity.Player;
import be.ucll.ip.minor.team18.model.service.BasketballService;
import be.ucll.ip.minor.team18.model.service.PlayerService;
import be.ucll.ip.minor.team18.util.ServiceException;
import be.ucll.ip.minor.team18.model.entity.Team;
import be.ucll.ip.minor.team18.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@Valid
@SessionAttributes({"lastShownTeam", "lastUpdatedTeam"})  // Spring Sessions - Ilias Achahbar & Lobke De Roover
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private BasketballService service;
    @Autowired
    private PlayerService playerService;

    @GetMapping("/all")
    public String showTeamOverview(Model model) {
        model.addAttribute("overviewTitle", "team.overview");
        model.addAttribute("teams", service.getAllTeams());
        return "overview-team";
    }

    @GetMapping("/all/namesort")
    public String showTeamOveviewNameSort(Model model) {
        model.addAttribute("overviewTitle", "team.sorted.name");
        model.addAttribute("teams", service.getAllTeamsSortedByName());
        return "overview-team";
    }
    @GetMapping("/all/playersort")
    public String showTeamOverviewPlayerSort(Model model) {
        model.addAttribute("overviewTitle", "team.sorted.players");
        model.addAttribute("teams", service.getAllTeamsSortedByPlayers());
        return "overview-team";
    }
    @GetMapping("/all/confirmadd")
    public String showTeamOverviewConfirmationAdd(Model model) {
        model.addAttribute("confirmationMessage", "confirmation.add.team");
        model.addAttribute("overviewTitle", "team.overview");
        model.addAttribute("teams", service.getAllTeams());
        return "overview-team";
    }
    @GetMapping("/all/confirmupdate")
    public String showTeamOverviewConfirmationUpdate(Model model){
        model.addAttribute("confirmationMessage", "confirmation.update.team");
        model.addAttribute("overviewTitle", "team.overview");
        model.addAttribute("teams", service.getAllTeams());
        return "overview-team";
    }
    @GetMapping("/all/confirmdelete")
    public String showTeamOverviewConfirmationDelete(Model model){
        model.addAttribute("confirmationMessage", "confirmation.delete.team");
        model.addAttribute("overviewTitle", "team.overview");
        model.addAttribute("teams", service.getAllTeams());
        return "overview-team";
    }

    @GetMapping("/add")
    public String addForm(Team team){
        return "add-team";
    }

    @PostMapping("/add")
    public String addTeam(@Valid Team team, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-team";
        }
        List<String> errorList = Validator.validateMinMaxAgeInput(team.getMinAge(), team.getMaxAge());
        if(errorList.isEmpty()){
            try {
                service.addTeam(team);
            } catch(ServiceException e){
                model.addAttribute("teamError", e.getMessage());
                return "add-team";
            }
        }else {
            model.addAttribute("minmaxError", errorList);
            return "add-team";
        }
        return "redirect:/team/all/confirmadd";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        try{
            Team team = service.findTeamById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid id: " + id));
            model.addAttribute("team", team);

            model.addAttribute("lastUpdatedTeam", lastUpdatedTeam(team));  // Spring Sessions - Ilias Achahbar & Lobke De Roover
        } catch(IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "update-team";
    }

    @PostMapping("/update/{id}")
    public String updateTeam(@PathVariable("id") long id, @Valid Team team, BindingResult result, Model model) {
        if (result.hasErrors()) {
            team.setId(id);
            return "update-team";
        }

        List<String> errorList = Validator.validateMinMaxAgeInput(team.getMinAge(), team.getMaxAge());
        if(errorList.isEmpty()){
            try {
                service.updateTeam(team);
            } catch(ServiceException e){
                model.addAttribute("teamError", e.getMessage());
                return "update-team";
            }
        }else {
            model.addAttribute("minmaxError", errorList);
            return "update-team";
        }

        model.addAttribute("lastUpdatedTeam", lastUpdatedTeam(team));  // Spring Sessions - Ilias Achahbar & Lobke De Roover

        return "redirect:/team/all/confirmupdate";
    }

    @GetMapping("/delete/{id}")
    public String deleteButton(@PathVariable("id") long id, Model model){
        try{
            Team team = service.findTeamById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid id: "+ id));
            service.deleteTeam(id);
        } catch(IllegalArgumentException e){
            model.addAttribute("teamError",e.getMessage());
            return "overview-team";
        }
        return "redirect:/team/all/confirmdelete";
    }

    @GetMapping("/players/{id}")
    public String showTeamInformation(@PathVariable("id") long id, Model model) {
        try {
            Team team = service.findTeamById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid id: " + id));
            model.addAttribute("team", team);
            model.addAttribute("teamPlayers", team.getPlayers());

            model.addAttribute("lastShownTeam", lastShownTeam(team));  // Spring Sessions - Ilias Achahbar & Lobke De Roover
        } catch(IllegalArgumentException e){
            model.addAttribute("teamError", e.getMessage());
            return "overview-team";
        }
        return "info-team";
    }

    @GetMapping("/delete_player/{id}/{playerId}")
    public String deletePlayer(@PathVariable("id") long id, @PathVariable("playerId") long playerId , Model model) {
        try {
            Team team = service.findTeamById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid id: " + id));
            team.deletePlayer(playerService.getPlayerWithId(playerId));
            try {
                service.updateTeam(team);
            } catch(ServiceException e){
                model.addAttribute("teamError", e.getMessage());
                return "info-team";
            }
        } catch(IllegalArgumentException e){
            model.addAttribute("teamError", e.getMessage());
            return "info-team";
        }
        return "redirect:/team/players/"+id;
    }

    @GetMapping("/add_player/{id}")
    public String showAddPlayer(@PathVariable("id") long id, Model model) {
        try{
            Team team = service.findTeamById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid id: " + id));
            model.addAttribute("team", team);
            model.addAttribute("availablePlayers", playerService.getPlayersWithoutTeam());
        } catch(ServiceException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "add-team-player";
    }

    @GetMapping("/add_player/{id}/{playerId}")
    public String addPlayer(@PathVariable("id") long id, @PathVariable("playerId") long playerId, Model model) {
        Team team = service.findTeamById(id).orElseThrow(() -> new IllegalArgumentException("Invalid id: " + id));
        try {
            Player player = playerService.getPlayerWithId(playerId);
            String error = Validator.validateInAgeGroup(team.getMinAge(), team.getMaxAge(), player.getAge());
            if(error == null) {
                try {
                    team.addPlayer(player);
                    service.updateTeam(team);
                    model.addAttribute("confirmationMessage", "confirmation.add.player.team");
                } catch(ServiceException e){
                    model.addAttribute("error", e.getMessage());
                }
            } else {
                model.addAttribute("error", error);
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("availablePlayers", playerService.getPlayersWithoutTeam());
        model.addAttribute("team", team);
        return "add-team-player";
    }

    @GetMapping("/search")
    public String searchTeams(){
        return "search-team";
    }

    @PostMapping("/showTeamsWithMaximumAge")
     public String showTeamsWithMaximumAge(@RequestParam("maxAge") String maxAge, Model model ) {
        try {
            List<Team> teams = service.getTeamsWithMaxAge(maxAge);
            if(teams.isEmpty()){ model.addAttribute("emptyAlert", "searchteam.empty"); }
            else{ model.addAttribute("teamsWithMaximumAge", teams); }

        } catch(IllegalArgumentException e) {
            model.addAttribute("maxAgeError", e.getMessage());
        }
        return "search-team";
    }

    @PostMapping("/showTeamsWithNameThatContainsString")
    public String showTeamsWithNameThatContainsString(@RequestParam("word") String word, Model model) {
        try{
            List<Team> teams = service.getTeamsWithNameThatContainsString(word);
            if(teams.isEmpty()){ model.addAttribute("emptyAlert", "searchteam.empty"); }
            else{ model.addAttribute("teamsWithNameThatContainsString", teams); }

        } catch(IllegalArgumentException e){
            model.addAttribute("nameContainsError", e.getMessage());
        }
        return "search-team";
    }

    @PostMapping("/showTeamsWithAgeAbove")
    public String showTeamsWithAgeAbove(@RequestParam("age") String age, Model model){
        try {
            List<Team> teams = service.getTeamsWithAgeAbove(age);
            if(teams.isEmpty()){ model.addAttribute("emptyAlert", "searchteam.empty"); }
            else{ model.addAttribute("teamsWithAgeAbove", teams); }

        } catch(IllegalArgumentException e){
            model.addAttribute("ageAboveError", e.getMessage());
        }
        return "search-team";
    }

/*
    Spring Sessions - Ilias Achahbar & Lobke De Roover
*/
    @ModelAttribute("lastShownTeam")
    private Team lastShownTeam(Team team) {
        return team;
    }
    @ModelAttribute("lastUpdatedTeam")
    private Team lastUpdatedTeam(Team team) {
        return team;
    }
/* Einde Implementatie Spring Sessions - Ilias Achahbar & Lobke De Roover */
}
