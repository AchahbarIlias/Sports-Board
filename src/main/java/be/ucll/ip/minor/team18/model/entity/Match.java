package be.ucll.ip.minor.team18.model.entity;


import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

@Entity(name = "Match")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "match.description.missing")
    @Size(max = 50,message = "match.description.long")
    private String description;

    @NotBlank(message = "match.location.missing")
    private String location;

    @NotNull(message = "match.amount.missing")
    @Positive(message = "match.amount.positive")
    private Integer amountOfVisitors;

    @ManyToOne
    @Valid
    private Bus bus;


    public long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public int getAmountOfVisitors() {
        return amountOfVisitors;
    }
    public void setAmountOfVisitors(int amountOfVisitors) {
        this.amountOfVisitors = amountOfVisitors;
    }

    public Bus getBus() {
        return bus;
    }
    public void setBus(Bus bus) {
            this.bus = bus;
    }

}
