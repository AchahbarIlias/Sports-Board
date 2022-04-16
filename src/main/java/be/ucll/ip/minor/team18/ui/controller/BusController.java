package be.ucll.ip.minor.team18.ui.controller;

import be.ucll.ip.minor.team18.model.entity.Match;
import be.ucll.ip.minor.team18.model.service.BasketballService;
import be.ucll.ip.minor.team18.model.service.MatchService;
import be.ucll.ip.minor.team18.util.Validator;
import be.ucll.ip.minor.team18.model.entity.Bus;
import be.ucll.ip.minor.team18.util.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@Valid
@SessionAttributes({"lastShownBus", "lastUpdatedBus"}) // Spring Sessions - Ilias Achahbar & Lobke De Roover
@RequestMapping("/bus")
public class BusController {

    @Autowired
    private BasketballService service;
    @Autowired
    private MatchService matchService;

    @GetMapping("/all")
    public String showBusOverview(Model model) {
        model.addAttribute("overviewTitle", "bus.overview");
        model.addAttribute("buses", service.getAllBuses());
        return "overview-bus";
    }
    @GetMapping("/all/namesort")
    public String showBusOverviewSortName(Model model) {
        model.addAttribute("overviewTitle", "bus.sorted.name");
        model.addAttribute("buses", service.getAllBusesSortedByName());
        return "overview-bus";
    }
    @GetMapping("/all/emailsort")
    public String showBusOverviewSortEmail(Model model){
        model.addAttribute("overviewTitle", "bus.sorted.email");
        model.addAttribute("buses", service.getAllBusesSortedByEmail());
        return "overview-bus";
    }
    @GetMapping("/all/seatsort")
    public String showBusOverviewSortSeat(Model model){
        model.addAttribute("overviewTitle", "bus.sorted.seats");
        model.addAttribute("buses", service.getAllBusesSortedBySeats());
        return "overview-bus";
    }
    @GetMapping("/all/confirmadd")
    public String showBusOverviewConfirmationAdd(Model model) {
        model.addAttribute("confirmationMessage", "confirmation.add.bus");
        model.addAttribute("overviewTitle", "bus.overview");
        model.addAttribute("buses", service.getAllBuses());
        return "overview-bus";
    }
    @GetMapping("/all/confirmupdate")
    public String showBusOverviewConfirmationUpdate(Model model){
        model.addAttribute("confirmationMessage", "confirmation.update.bus");
        model.addAttribute("overviewTitle", "bus.overview");
        model.addAttribute("buses", service.getAllBuses());
        return "overview-bus";
    }
    @GetMapping("/all/confirmdelete")
    public String showBusOverviewConfirmationDelete(Model model){
        model.addAttribute("confirmationMessage", "confirmation.delete.bus");
        model.addAttribute("overviewTitle", "bus.overview");
        model.addAttribute("buses", service.getAllBuses());
        return "overview-bus";
    }

    @GetMapping("/add")
    public String addForm(Bus bus){
        return "add-bus";
    }

    @PostMapping("/add")
    public String addBus(@Valid Bus bus, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-bus";
        }
        try {
            service.addBus(bus);
        } catch(ServiceException e) {
            model.addAttribute("busError", e.getMessage());
            return "add-bus";
        }
        return "redirect:/bus/all";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        try{
            Bus bus = service.findBusById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid id: " + id));
            model.addAttribute("bus", bus);

            model.addAttribute("lastUpdatedBus", lastUpdatedBus(bus)); // Spring Sessions - Ilias Achahbar & Lobke De Roover
        }
        catch (IllegalArgumentException exc) {
            model.addAttribute("busError", exc.getMessage());
        }
        return "update-bus";
    }

    @PostMapping("/update/{id}")
    public String updateBus(@PathVariable("id") long id, @Valid Bus bus, BindingResult result, Model model) {
        if (result.hasErrors()) {
            bus.setId(id);
            return "update-bus";
        }
        try {
            service.updateBus(bus);
        }catch (ServiceException exc) {
            model.addAttribute("busError", exc.getMessage());
            return "update-bus";
        }

        model.addAttribute("lastUpdatedBus", lastUpdatedBus(bus));  // Spring Sessions - Ilias Achahbar & Lobke De Roover

        return "redirect:/bus/all/confirmupdate";
    }

    @GetMapping("/delete/{id}")
    public String deleteButton(@PathVariable("id") long id, Model model){
        try{
            Bus bus = service.findBusById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid id: "+ id));

            service.deleteBus(id);
        }catch(IllegalArgumentException e){
            model.addAttribute("busError", e.getMessage());
            return "overview-bus";
        }
        return "redirect:/bus/all/confirmdelete";
    }

    @GetMapping("/matches/{id}")
    public String showBusInformation(@PathVariable("id") long id, Model model) {
        try {
            Bus bus = service.findBusById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid id: " + id));
            model.addAttribute("bus", bus);
            model.addAttribute("busMatches", bus.getMatches());

            model.addAttribute("lastShownBus", lastShownBus(bus)); // Spring Sessions - Ilias Achahbar & Lobke De Roover
        } catch(IllegalArgumentException e){
            model.addAttribute("busError", e.getMessage());
            return "overview-bus";
        }
        return "info-bus";
    }

    @GetMapping("/delete_match/{id}/{matchId}")
    public String deletePlayer(@PathVariable("id") long id, @PathVariable("matchId") long matchId , Model model) {
        try {
            Bus bus = service.findBusById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid id: " + id));
            bus.deleteMatch(matchService.getMatchWithId(matchId));
            try {
                service.updateBus(bus);
            } catch(ServiceException e){
                model.addAttribute("busError", e.getMessage());
                return "info-bus";
            }
        } catch(IllegalArgumentException e){
            model.addAttribute("busError", e.getMessage());
            return "info-bus";
        }
        return "redirect:/bus/matches/"+id;
    }

    @GetMapping("/add_match/{id}")
    public String showAddMatch(@PathVariable("id") long id, Model model) {
        try{
            Bus bus = service.findBusById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid id: " + id));
            model.addAttribute("bus", bus);
            model.addAttribute("availableMatches", matchService.getMatchesWithoutBus());
        }
        catch(ServiceException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "add-bus-match";
    }

    @GetMapping("/add_match/{id}/{matchId}")
    public String addPlayer(@PathVariable("id") long id, @PathVariable("matchId") long matchId, Model model) {
        Bus bus = service.findBusById(id).orElseThrow(() -> new IllegalArgumentException("Invalid id: " + id));
        try {
            Match match = matchService.getMatchWithId(matchId);
            bus.addMatch(match);
            service.updateBus(bus);
            model.addAttribute("confirmationMessage", "confirmation.add.match.bus");
        }
        catch(Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("availableMatches", matchService.getMatchesWithoutBus());
        model.addAttribute("bus", bus);
        return "add-bus-match";
    }


    @GetMapping("/search")
    public String searchBuses(){
        return "search-bus";
    }

    @PostMapping("/showBusesWithDepartureLocation")
    public String showBusOverviewWithDepartureLocation(@RequestParam("departureLocation") String departureLocation,  Model model ) {
        try {
            List<Bus> buses = service.getBusesWithDepartureLocation(departureLocation);
            if(buses.isEmpty()){ model.addAttribute("emptyAlert", "searchbus.empty"); }
            else{ model.addAttribute("busesWithDepartureLocation", buses); }

        } catch (ServiceException | IllegalArgumentException e) {
            model.addAttribute("departureError", e.getMessage());
        }
        return "search-bus";
    }

    @PostMapping("/showBusesWithNumberOfSeatsBetween")
    public String showBusOverviewWithNumbersBetweenMinMax(@RequestParam("minSeats") String minSeats,@RequestParam("maxSeats") String maxSeats, Model model) {
       List<String> errorList = Validator.validateBusSeatsInput(minSeats,maxSeats);

       if(errorList.isEmpty()){
           List<Bus> buses = service.getBusesWithNumberOfSeatsBetween(minSeats, maxSeats);
           if(buses.isEmpty()){ model.addAttribute("emptyAlert", "searchbus.empty"); }
           else{ model.addAttribute("busesWithNumberOfSeatsBetween", buses); }

       } else {
           model.addAttribute("seatsError", errorList );
       }
       return "search-bus";
    }

    @PostMapping("/showBusesWithSeatsAbove")
    public String showBusesWithSeatsAbove(@RequestParam("seatsAbove") String seatsAbove, Model model){
        try {
            List<Bus> buses = service.getBusesWithNumberOfSeatsAbove(seatsAbove);
            if(buses.isEmpty()){ model.addAttribute("emptyAlert", "searchbus.empty"); }
            else{ model.addAttribute("busesWithSeatsAbove", buses); }

        } catch (IllegalArgumentException e){
            model.addAttribute("seatsAboveError", e.getMessage());
        }
        return "search-bus";
    }

/*
    Spring Sessions - Ilias Achahbar & Lobke De Roover
*/
    @ModelAttribute("lastUpdatedBus")
    private Bus lastUpdatedBus(Bus bus) {
        return bus;
    }
    @ModelAttribute("lastShownBus")
    private Bus lastShownBus(Bus bus) {
        return bus;
    }
/* Einde Implementatie Spring Sessions - Ilias Achahbar & Lobke De Roover */
}
