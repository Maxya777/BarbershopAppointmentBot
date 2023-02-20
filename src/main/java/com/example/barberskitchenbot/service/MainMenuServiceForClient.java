package com.example.barberskitchenbot.service;

import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
public class MainMenuServiceForClient {


    public SendMessage startMessage(long chatId, String name) {
        return sendMessage(chatId, EmojiParser.parseToUnicode("Привет, " + name + " :wave:" + ". Я - Ваш персональный автоматизированный бот помощник парикмахерской \"Кухня\" :scissors:"));
    }

    public SendMessage getMessageFromUserToAdmin(long chatIdAdmin, String messageText, String username, String firstName, long chatId) {
        return sendMessage(chatIdAdmin, "Сообщение от пользователя: \n" + "@" + username + " " + firstName + "\n (" + "ID: " + chatId + "):\n\n" + messageText);
    }

    public SendMessage getInformationAboutBarbershop(long chatId) {
        return sendMessage(chatId, "Тут длинное описание парикмахерской");
    }

    public SendMessage getAppointmentConfirm(long chatId) {
        return sendMessage(chatId, "Запись создана");
    }

    public SendMessage getDefaultResponse(long chatId) {
        return sendMessage(chatId, "Вы не можете отправлять сообщения в данном чате");
    }

    public EditMessageText responseUserOnInlineButton(Update update) {
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        String textForClient = "Отличный выбор! В ближайшее время с вами свяжется администратор для уточнения времени записи";
        return executeEditMessageText(textForClient, chatId, messageId);
    }

    private EditMessageText executeEditMessageText(String text, long chatId, long messageId) {
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setText(text);
        message.setMessageId((int) messageId);
        return message;
    }

    private SendMessage sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        return message;
    }
}
