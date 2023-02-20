package com.example.barberskitchenbot.service;

import com.example.barberskitchenbot.configuration.BotConfig;
import com.example.barberskitchenbot.keyboards.InlineKeyboard;
import com.example.barberskitchenbot.keyboards.ReplyKeyboardMaker;
import com.example.barberskitchenbot.model.Client;
import com.example.barberskitchenbot.repository.ClientRepository;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class KeyboardServiceForAdmin {

    private final ClientRepository clientRepository;
    private  final BotConfig botConfig;
    private final InlineKeyboard inlineKeyboard;
    private final ReplyKeyboardMaker replyKeyboardMaker;


    public SendMessage sendMessageKeyboardMenuForAdmin(long chatId, String textSend) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), textSend);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboardForAdmin());
        return sendMessage;
    }
    public SendMessage appointmentInlineButtonForAdmin(String text) {
        SendMessage sendMessage = new SendMessage();
        List<Client> admins = clientRepository.findByChatIdIs(botConfig.getOwnerId());

        admins.forEach((admin) -> {
            sendMessage.setChatId(admin.getChatId());
            sendMessage.setText(text);
            sendMessage.setReplyMarkup(inlineKeyboard.appointmentButtonForAdmin());
        });

        return sendMessage;
    }
    public SendMessage sendInlineKeyboardDateAppointment(long chatId, String textSend) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), textSend);
        sendMessage.setReplyMarkup(inlineKeyboard.sendRecordDateRequest());
        return sendMessage;
    }
    public SendMessage sendInlineKeyboardTimeAppointment(long chatId, String textSend, LocalDate selectedDate) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), textSend);
        sendMessage.setReplyMarkup(inlineKeyboard.sendRecordTimeRequest(selectedDate));
        return sendMessage;
    }
    public SendMessage sendInlineKeyboardServiceAppointment(long chatId, String textSend) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), textSend);
        sendMessage.setReplyMarkup(inlineKeyboard.sendServiceRequest());
        return sendMessage;
    }
    public SendMessage sendInlineKeyboardMasterAppointment(long chatId, String textSend) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), textSend);
        sendMessage.setReplyMarkup(inlineKeyboard.sendMasterRequest());
        return sendMessage;
    }
    private EditMessageText executeEditMessageText(String text, long chatId, long messageId) {
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setText(text);
        message.setMessageId((int) messageId);
        return message;
    }
    public EditMessageText responseOnInlineButton(Update update) {
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        String textForButton = EmojiParser.parseToUnicode( "Выбрано! " + "✅");
        return executeEditMessageText(textForButton, chatId, messageId);
    }
}
