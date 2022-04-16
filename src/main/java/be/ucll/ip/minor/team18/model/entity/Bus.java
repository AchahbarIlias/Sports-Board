package be.ucll.ip.minor.team18.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Entity(name = "Bus")
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "bus.email.missing")
    @Email(message = "bus.email.invalid")
    private String email;

    @NotNull(message = "bus.seats.missing")
    @Min(value=1, message="bus.seats.min.invalid")
    @Max(value=100, message="bus.seats.max.invalid")
    private Integer seats;

    @NotBlank(message = "bus.departureLocation.missing")
    private String departureLocation;

    @NotBlank(message = "bus.name.missing")
    private String name;

    @JsonBackReference
    @OneToMany(mappedBy = "bus")
    private List<Match> matches;


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSeats() {
        return seats;
    }
    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getDepartureLocation() { return departureLocation; }
    public void setDepartureLocation(String departureLocation) { this.departureLocation = departureLocation; }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void addMatch(@Valid Match match) {
        if (validateSeats(match.getAmountOfVisitors())){
            matches.add(match);
            match.setBus(this);
        } else {
            throw new IllegalArgumentException("bus.match.seats.invalid");
        }
    }

    public List<Match> getMatches() {
        return matches;
    }
    public void setMatches(List<Match> matches) {
        if(matches != null) {
            this.matches = matches;
        }
    }

    public void deleteMatch(Match match) {
        match.setBus(null);
        matches.remove(match);
    }

    public boolean validateSeats(Integer visitors) {
        return visitors <= getSeats();
    }

}
