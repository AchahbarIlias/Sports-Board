package be.ucll.ip.minor.team18.model.entity;


import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity(name= "Player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "player.firstname.missing")
    private String firstName;

    @NotBlank(message = "player.lastname.missing")
    private String lastName;

    @NotNull(message = "player.number.missing")
    @Positive(message = "player.number.zero")
    private Integer playerNumber;

    @NotNull(message = "player.age.missing")
    @Min(value=1, message="player.age.negative")
    private Integer age;

    @ManyToOne
    @Valid
    private Team team;


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getPlayerNumber() {
        return playerNumber;
    }
    public void setPlayerNumber(Integer playerNumber) {
        this.playerNumber = playerNumber;
    }

    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }

    public String getFullName() {
        return firstName + ' ' + lastName;
    }

    public Team getTeam() {
      return team;
  }
    public void setTeam(Team team) {
        this.team = team;
    }
}
