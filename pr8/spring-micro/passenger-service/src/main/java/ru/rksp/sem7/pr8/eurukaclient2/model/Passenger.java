package ru.rksp.sem7.pr8.eurukaclient2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "passenger")
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;


    @Column(name = "fullName")
    private String fullName;

    @Column(name = "passportNumber")
    private String passportNumber;

    @Column(name = "passportSeries")
    private String passportSeries;
}
