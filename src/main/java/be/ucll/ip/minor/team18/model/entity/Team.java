package be.ucll.ip.minor.team18.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity(name= "Team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "team.name.missing")
    private String name;

    @NotNull(message = "team.minAge.missing")
    @Min(value=1, message="team.minAge.min.invalid")
    @Max(value=120, message="team.minAge.max.invalid")
    private Integer minAge;

    @NotNull(message = "team.maxAge.missing")
    @Min(value=1, message="team.maxAge.min.invalid")
    @Max(value=120, message="team.maxAge.max.invalid")
    private Integer maxAge;

    @NotNull(message = "team.numberOfPlayers.missing")
    @Min(value=1, message="team.numberOfPlayers.min.invalid")
    @Max(value=2000, message="team.numberOfPlayers.max.invalid")
    private Integer numberOfPlayers;

    @JsonBackReference
    @OneToMany(mappedBy = "team")
    private List<Player> players;


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getMinAge() {
        return minAge;
    }
    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }
    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public Integer getNumberOfPlayers() {
        return numberOfPlayers;
    }
    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public Integer countPlayers() {
        return players.size();
    }

    public void addPlayer(@Valid Player player) {
        if(playerValidation(player)) {
            if (countPlayers() < getNumberOfPlayers()) {
                players.add(player);
                player.setTeam(this);
            } else {
                throw new IllegalArgumentException("team.player.amount.full");
            }
        }
        else throw new IllegalArgumentException("team.player.age.invalid");
    }

    public List<Player> getPlayers() {
        return players;
    }
    public void setPlayers(List<Player> players) {
        if(players != null) {
            this.players = players;
        }
    }

    public void deletePlayer(Player player) {
        players.remove(player);
        player.setTeam(null);
    }

    public boolean playerValidation(Player player) {
        return player.getAge() <= getMaxAge() && player.getAge() >= getMinAge();
    }
}
