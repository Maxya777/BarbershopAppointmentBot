package com.example.barberskitchenbot.service;

import com.example.barberskitchenbot.model.Client;
import com.example.barberskitchenbot.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.Timestamp;


@Component
@RequiredArgsConstructor
public class RegistrationService {

    private final ClientRepository clientRepository;

    public void registerClient(Message message) {

        if (clientRepository.findByChatId(message.getChatId()).isEmpty()) {
            Long chatId = message.getChatId();
            Chat chat = message.getChat();

            Client client = new Client();

            client.setChatId(chatId);
            client.setFirstName(chat.getFirstName());
            client.setLastName(chat.getLastName());
            client.setUsername(chat.getUserName());
            client.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
            client.setBanStatus(false);
            client.setPhoneNumber(message.getContact().getPhoneNumber());

            clientRepository.save(client);
        }
    }

}
