package ru.rksp.sem7.pr8.eurukaclient2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rksp.sem7.pr8.eurukaclient2.model.Passenger;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, String> {
}
