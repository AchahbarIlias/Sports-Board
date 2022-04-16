package be.ucll.ip.minor.team18;

import be.ucll.ip.minor.team18.model.entity.Player;
import be.ucll.ip.minor.team18.model.entity.Team;

public class PlayerBuilder {
    private String firstName, lastName;
    private Integer age,playerNumber;
    Team team;

    private PlayerBuilder() {
    }

    public static PlayerBuilder aPlayer(){
        return new PlayerBuilder();
    }

    public PlayerBuilder withFirstName(String firstName){
        this.firstName=firstName;
        return this;
    }

    public PlayerBuilder withLastName(String lastName){
        this.lastName=lastName;
        return this;
    }

    public PlayerBuilder withAge(int age){
        this.age=age;
        return this;
    }

    public PlayerBuilder withPlayerNumber(int playerNumber){
        this.playerNumber=playerNumber;
        return this;
    }

    public PlayerBuilder withTeam(Team team) {
        this.team = team;
        return this;
    }

    public static PlayerBuilder aPlayerLucas(){
        return aPlayer().withFirstName("Lucas").withLastName("Cuppers").withAge(25).withPlayerNumber(11).withTeam(null);
    }

    public static PlayerBuilder aPlayerGerben(){
        return aPlayer().withFirstName("Gerben").withLastName("Piot").withAge(19).withPlayerNumber(7).withTeam(null);
    }

    public Player build(){
        Player player = new Player();
        player.setFirstName(this.firstName);
        player.setLastName(this.lastName);
        player.setAge(this.age);
        player.setPlayerNumber(this.playerNumber);
        player.setTeam(this.team);
        return player;
    }
}
