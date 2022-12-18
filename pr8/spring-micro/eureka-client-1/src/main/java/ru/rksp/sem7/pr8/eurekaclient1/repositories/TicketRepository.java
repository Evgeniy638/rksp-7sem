package ru.rksp.sem7.pr8.eurekaclient1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rksp.sem7.pr8.eurekaclient1.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
}
