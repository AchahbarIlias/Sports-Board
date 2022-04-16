package be.ucll.ip.minor.team18.model.service;

import be.ucll.ip.minor.team18.model.entity.Bus;
import be.ucll.ip.minor.team18.model.entity.Team;
import be.ucll.ip.minor.team18.model.repository.BusRepository;
import be.ucll.ip.minor.team18.model.repository.TeamRepository;
import be.ucll.ip.minor.team18.util.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class BasketballService {

    @Autowired
    private TeamRepository teamRepository;

    public Team addTeam(Team team) throws ServiceException {
        if (teamRepository.findTeamByName(team.getName())!=null)throw new ServiceException("team.name.in.use");
        else return teamRepository.save(team);
    }

    public Team updateTeam(Team team) throws ServiceException {
        if(team == null) throw new ServiceException(("team.missing"));
        Team t = teamRepository.findTeamByName(team.getName());
        if(t != null && t.getId() != team.getId()){
            throw new ServiceException("team.name.in.use");
        }
        return teamRepository.save(team);
    }

    @Transactional
    public void deleteTeam(long id) {
        Team team = teamRepository.findTeamById(id);
        team.getPlayers().forEach( child -> child.setTeam(null));
       teamRepository.deleteTeamById(id);
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public List<Team> getAllTeamsSortedByName() {
        return teamRepository.findAll(Sort.by("name"));
    }

    public List<Team> getAllTeamsSortedByPlayers() {
        return teamRepository.findAll(Sort.by("numberOfPlayers"));
    }

    public List<Team> getTeamsWithMaxAge(String maxAge) {
        if(maxAge.isEmpty()) throw new IllegalArgumentException("teamsWithMaximumAge.maxAge.missing");
        else return teamRepository.findTeamsByMaxAge(Integer.parseInt(maxAge));
    }

    public List<Team> getTeamsWithNameThatContainsString(String string) {
        if(string.isEmpty()) throw new IllegalArgumentException("teamsWithNameThatContainsString.word.missing");
        else return teamRepository.findTeamsByNameContaining(string.toLowerCase());
    }

    public List<Team> getTeamsWithAgeAbove(String age) {
        if(age.isEmpty()) throw new IllegalArgumentException("teamsWithAgeAbove.age.missing");
        else return teamRepository.findTeamsByMinAgeAfter(Integer.parseInt(age));
    }

    public Optional<Team> findTeamById(long teamId) {
        return teamRepository.findById(teamId);
    }



    @Autowired
    private BusRepository busRepository;


    public Bus addBus(Bus bus) throws ServiceException {
        if(busRepository.findBusByEmail(bus.getEmail()) != null) throw new ServiceException("bus.email.in.use");
        else return busRepository.save(bus);
    }

    public Bus updateBus(Bus bus) throws ServiceException {
        if(busRepository.findBusByEmailNotSelf(bus.getEmail(), bus.getId()) != null) throw new ServiceException("bus.email.in.use");
        else return busRepository.save(bus);
    }

    @Transactional
    public void deleteBus(long id) {
        Bus bus = busRepository.findBusById(id);
        bus.getMatches().forEach( child -> child.setBus(null));
        busRepository.deleteBusById(id);
    }

    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }

    public List<Bus> getAllBusesSortedByName() {
        return busRepository.findAll(Sort.by("name"));
    }

    public List<Bus> getAllBusesSortedBySeats() {
        return busRepository.findAll(Sort.by("seats"));
    }

    public List<Bus> getAllBusesSortedByEmail() {
        return busRepository.findAll(Sort.by("email"));
    }

    public List<Bus> getBusesWithDepartureLocation(String departureLocation) throws ServiceException{
        if(departureLocation.isEmpty()) throw new IllegalArgumentException("busesWithDepartureLocation.departureLocation.missing");
        else return busRepository.findBusesByDepartureLocation(departureLocation); }

    public List<Bus> getBusesWithNumberOfSeatsBetween(String lowerLimit, String upperLimit){
        return busRepository.findBusesBySeatsBetween(Integer.parseInt(lowerLimit) ,Integer.parseInt(upperLimit));
    }

    public List<Bus> getBusesWithNumberOfSeatsAbove(String seatsAbove) {
        if(seatsAbove.isEmpty()) throw new IllegalArgumentException("busesWithSeatsAbove.seats.missing");
        else return busRepository.findBusesBySeatsIsGreaterThanEqual(Integer.parseInt(seatsAbove));
    }

    public Optional<Bus> findBusById(long id) {
        return busRepository.findById(id);
    }

}
