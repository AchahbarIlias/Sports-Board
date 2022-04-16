package be.ucll.ip.minor.team18.model.service;

import be.ucll.ip.minor.team18.model.entity.Match;
import be.ucll.ip.minor.team18.model.repository.MatchRepository;
import be.ucll.ip.minor.team18.util.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    public Match addMatch(Match match) throws ServiceException {
        if (matchRepository.findMatchByDescription(match.getDescription())!=null) throw new ServiceException("match.description.in.use");
        else return matchRepository.save(match);
    }

    public Match updateMatch(Match match) throws ServiceException {
        if(matchRepository.findMatchByDescriptionNotSelf(match.getDescription(), match.getId())!=null) throw new ServiceException("match.description.in.use");
        else return matchRepository.save(match);
    }

    @Transactional
    public void deleteMatch(long id) {
        if(matchRepository.findMatchById(id).getBus() != null) throw new IllegalArgumentException("delete.error.bus.in.match");
        else matchRepository.deleteById(id);
    }

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public Match getMatchWithDescription(String description) {
        if(description.isEmpty()) throw new IllegalArgumentException("match.description.missing");
        else return matchRepository.findMatchByDescription(description);
    }

    public List<Match> getAllMatchesWithLocation(String location) {
        if(location.isEmpty()) throw new IllegalArgumentException("matchesWithLocation.location.missing");
        else return matchRepository.findMatchesByLocation(location.toLowerCase());
    }

    public List<Match> getAllMatchesWithDescriptionThatContainsString(String string) {
        if(string.isEmpty()) throw new IllegalArgumentException("matchesWithDescriptionThatContainsString.word.missing");
        else return matchRepository.findMatchesByDescriptionContaining(string.toLowerCase());
    }

    public Match getMatchWithId(long id) {
        if(matchRepository.findMatchById(id) == null) throw new IllegalArgumentException("match.not.in.bus");
        else return matchRepository.findMatchById(id);
    }

    public List<Match> getMatchesWithoutBus() {
        if(matchRepository.findMatchesByBusIsNull().isEmpty()) throw new ServiceException("empty.matches.without.bus");
        else return matchRepository.findMatchesByBusIsNull();
    }



}
