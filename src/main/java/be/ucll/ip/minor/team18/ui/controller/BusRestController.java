package be.ucll.ip.minor.team18.ui.controller;

import be.ucll.ip.minor.team18.model.entity.Bus;
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
@RequestMapping("/api/bus")
public class BusRestController {

    @Autowired
    private BasketballService basketballService;

    @GetMapping("/all")
    public Iterable<Bus> getAllBuses() {
        return basketballService.getAllBuses();
    }

    @GetMapping("/all/namesort")
    public Iterable<Bus> getAllBusesSortedByName() {
        return basketballService.getAllBusesSortedByName();
    }

    @GetMapping("/all/emailsort")
    public Iterable<Bus> getAllBusesSortedByEmail() {
        return basketballService.getAllBusesSortedByEmail();
    }

    @GetMapping("/all/seatsort")
    public Iterable<Bus> getAllBusesSortedBySeats() {
        return basketballService.getAllBusesSortedBySeats();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBus(@PathVariable("id") long id) {
        basketballService.deleteBus(id);
    }

    @PostMapping("/add")
    public Bus addBus(@Valid @RequestBody Bus bus) {

      try{
          basketballService.addBus(bus);

      }catch (ServiceException e){
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "busError", e);
      }
      return bus;
    }

    @PostMapping("/update/{id}")
    public Bus updateBus(@PathVariable("id") long id, @Valid @RequestBody Bus bus) {
        try{
            bus.setId(id);
            basketballService.updateBus(bus);

        }catch (ServiceException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "busError", e);
        }
        return bus;
       
    }

    @PostMapping("/showBusesWithDepartureLocation")
    public Iterable<Bus> showBusOverviewWithDepartureLocation(@RequestParam("departureLocation") String departureLocation) {
        Iterable<Bus> buses;
        try {
            buses = basketballService.getBusesWithDepartureLocation(departureLocation);
        }
        catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "DepartureLocation", e);
        }
        return buses;
    }

    @PostMapping("/showBusesWithSeatsAbove")
    public Iterable<Bus> showBusesWithSeatsAbove(@RequestParam("seatsAbove") String seatsAbove){
        Iterable<Bus> buses;
        try {
            buses = basketballService.getBusesWithNumberOfSeatsAbove(seatsAbove);
        }
        catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seats", e);
        }
        return buses;
    }

    @PostMapping("/showBusesWithNumberOfSeatsBetween")
    public Iterable<Bus> showBusOverviewWithNumbersBetweenMinMax(@RequestParam("minSeats") String minSeats,@RequestParam("maxSeats") String maxSeats) {
        Iterable<Bus> buses;
        try {
            buses = basketballService.getBusesWithNumberOfSeatsBetween(minSeats,maxSeats);
        }
        catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seats", e);
        }
        return buses;
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
