package be.ucll.ip.minor.team18.model.repository;

import be.ucll.ip.minor.team18.model.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {

    Bus findBusByEmail(String email);

    List<Bus> findBusesBySeatsIsGreaterThanEqual(int seatsAbove);

    List<Bus> findBusesBySeatsBetween(int lowerLimit, int upperLimit);

    List<Bus> findBusesByDepartureLocation(String departureLocation);

    int deleteBusById(long id);

    @Query("SELECT b FROM Bus b WHERE b.email=?1 AND b.id <> ?2")
    Bus findBusByEmailNotSelf(String email,  long id);

    Bus findBusById(long id);
}
