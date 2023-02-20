package com.example.barberskitchenbot.exceptions;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BotException {

    public SendMessage sendMessageIncorrectlyId(Update update) {
        SendMessage errorMessage = new SendMessage();
        errorMessage.setChatId(update.getMessage().getChatId());
        errorMessage.setText("Ошибка: Неправильно введен пароль");
        return errorMessage;
    }
}
