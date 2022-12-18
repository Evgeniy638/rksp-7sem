package ru.rksp.sem7.pr8.eurekaclient1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rksp.sem7.pr8.eurekaclient1.model.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {
}
