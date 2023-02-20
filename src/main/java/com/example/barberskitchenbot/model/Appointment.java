package com.example.barberskitchenbot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "customer_posts")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "client_id")
    private Long clientId;
    @Column(name = "date_appointment")
    private LocalDate dateAppointment;

    @Column(name = "time_appointment")
    private LocalTime timeAppointment;

    @Column(name = "type_service")
    private String typeService;

    @Column(name = "master")
    private String master;

    @Override
    public String toString() {
        return  "Дата записи: " + dateAppointment + "\n" +
                "Время записи: " + timeAppointment + "\n" +
                "Услуга: " + typeService + "\n" +
                "Ваш мастер: " + master + "\n";
    }
}
