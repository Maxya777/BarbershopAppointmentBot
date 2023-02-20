package com.example.barberskitchenbot.service;

import com.example.barberskitchenbot.configuration.BotConfig;
import com.example.barberskitchenbot.keyboards.InlineKeyboard;
import com.example.barberskitchenbot.keyboards.ReplyKeyboardMaker;
import com.example.barberskitchenbot.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class KeyboardServiceForClient {

    private final ReplyKeyboardMaker replyKeyboardMaker;
    private final InlineKeyboard inlineKeyboard;


    private SendMessage sendMessage(long chatId, String textToSend, ReplyKeyboardMarkup markup) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        message.setReplyMarkup(markup);
        return message;
    }

    public SendMessage sendMessageKeyboardMenuForClient(long chatId, String textSend) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), textSend);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboardForClient());
        return sendMessage;
    }

    public SendMessage sendIdentificationMenu(long chatId, String textSend) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), textSend);
        sendMessage.setReplyMarkup(replyKeyboardMaker.requestContact());
        return sendMessage;
    }

    public SendMessage sendReturnMainMenu(long chatId, String textSend) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), textSend);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenu());
        return sendMessage;
    }

    public SendMessage inlineMenuBarbers(long chatId, String textSend) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), textSend);
        sendMessage.setReplyMarkup(inlineKeyboard.getInlineMessageButtons());
        return sendMessage;
    }

    public SendMessage inlineLinkWebsite(long chatId, String textSend) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), textSend);
        sendMessage.setReplyMarkup(inlineKeyboard.inlineButtonsLink());
        return sendMessage;
    }

    public SendMessage inlineLinkMapWebsite(long chatId, String textSend) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), textSend);
        sendMessage.setReplyMarkup(inlineKeyboard.inlineButtonsMapLink());
        return sendMessage;
    }
    public SendMessage sendMessageStartKeyboardForClient(long chatId, String textSend) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), textSend);
        sendMessage.setReplyMarkup(replyKeyboardMaker.startKeyboard());
        return sendMessage;
    }
}
