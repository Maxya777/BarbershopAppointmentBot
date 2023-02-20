package com.example.barberskitchenbot.service;

import com.example.barberskitchenbot.model.Appointment;
import com.example.barberskitchenbot.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public void createAppointment(long clientId, LocalDate dateAppointment, LocalTime timeAppointment, String typeService, String master) {

        Appointment appointment = new Appointment();
        appointment.setClientId(clientId);
        appointment.setDateAppointment(dateAppointment);
        appointment.setTimeAppointment(timeAppointment);
        appointment.setTypeService(typeService);
        appointment.setMaster(master);

        appointmentRepository.save(appointment);
    }

    public SendMessage viewAppointmentsById(long clientId) {
        SendMessage sendMessage = new SendMessage();
        List<Appointment> appointments = appointmentRepository.findByClientId(clientId);
        sendMessage.setChatId(clientId);
        if (!appointments.isEmpty()) {
            sendMessage.setText("На данный момент у вас имеются записи: " + "\n\n" + appointments.toString().replace("[", "").replace("]", "").replace(",", ""));
        } else {
            sendMessage.setText("Записей нету");
        }
        return sendMessage;
    }

    public SendMessage removeAppointment(long clientId) {
        SendMessage sendMessage = new SendMessage();
        List<Appointment> appointments = appointmentRepository.findByClientId(clientId);
        sendMessage.setChatId(clientId);

        if (!appointments.isEmpty()) {
            appointments.forEach(appointment -> {
                appointmentRepository.removeAppointmentByDateAppointmentAndTimeAppointment(appointment.getDateAppointment(), appointment.getTimeAppointment());
                sendMessage.setText("Отменённые записи: " + "\n\n" + appointment);
            });
        } else {
            sendMessage.setText("Записей пока что нету");
        }
        return sendMessage;
    }
}
