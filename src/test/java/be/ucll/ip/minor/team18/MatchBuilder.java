package be.ucll.ip.minor.team18;

import be.ucll.ip.minor.team18.model.entity.Match;

public class MatchBuilder {
    private String description, location;
    private int amountOfVisitors;

    private MatchBuilder() {
    }
    public static MatchBuilder aMatch(){
        return new MatchBuilder();
    }

    public MatchBuilder withDescription(String description){
        this.description=description;
        return this;
    }
    public MatchBuilder withLocation(String location){
        this.location =location;
        return this;
    }
    public MatchBuilder amountOfVisitors(int amountOfVisitors){
        this.amountOfVisitors =amountOfVisitors;
        return this;
    }

    public static MatchBuilder aMatchInLeuven(){
        return aMatch().withDescription("Leuven - Mechelen").withLocation("Leuven").amountOfVisitors(20);
    }

    public static MatchBuilder aMatchInAntwerpen(){
        return aMatch().withDescription("Leuven - Antwerpen").withLocation("Antwerpen").amountOfVisitors(20);
    }

    public Match build(){
        Match match = new Match();
        match.setDescription(this.description);
        match.setLocation(this.location);
        match.setAmountOfVisitors(this.amountOfVisitors);
        return match;
    }
}
