package com.example.barberskitchenbot.repository;

import com.example.barberskitchenbot.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

List<Appointment> findByClientId(long clientId);

void removeAppointmentByDateAppointmentAndTimeAppointment(LocalDate date, LocalTime time);
}
