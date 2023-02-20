package com.example.barberskitchenbot.service;

import com.example.barberskitchenbot.configuration.BotConfig;
import com.example.barberskitchenbot.model.Client;
import com.example.barberskitchenbot.repository.ClientRepository;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MainMenuServiceForAdmin {

    private final ClientRepository clientRepository;
    private final BotConfig botConfig;



    public SendMessage getHintSendAll(long chatId) {
        return sendMessage(chatId, "Введите в поле ввода \"/sendAll\" и после пробела  сообщение, которое хотите адресовать для всех пользователей");
    }

    public SendMessage getHintSendOne(long chatId) {
        return sendMessage(chatId, "Введите в поле ввода \"/send\" и после пробела ID пользователя и далее сообщение, которое хотите ему адресовать");
    }

    public SendMessage sendMessageAllAdministrators(String textToSend) {
        SendMessage sendMessage = new SendMessage();
        List<Client> admins = clientRepository.findByChatIdIs(botConfig.getOwnerId());
        admins.forEach((admin -> {
            sendMessage.setChatId(admin.getChatId());
            sendMessage.setText(textToSend);
        }));
        return sendMessage;
    }

    public SendMessage responseAdminOnInlineButton(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        String firstName = update.getCallbackQuery().getFrom().getFirstName();
        String nickName = update.getCallbackQuery().getFrom().getUserName();
        long chatId = update.getCallbackQuery().getFrom().getId();

        String textForAdmin = firstName + " хочет записаться к " + callbackData + ". \n Никнейм: " + "@" + nickName + "\n ID: " + chatId;
        return sendMessageAllAdministrators(textForAdmin);
    }

    public SendMessage sendMessageAllUsers(Update update) {
        SendMessage sendMessage = new SendMessage();
        var textToSend = EmojiParser.parseToUnicode(update.getMessage().getText().substring(update.getMessage().getText().indexOf(" ")));
        var clients = clientRepository.findAll();
        clients.forEach((client -> {
            sendMessage.setChatId(client.getChatId());
            sendMessage.setText(textToSend);
        }));
        return sendMessage;
    }

    public SendMessage sendMessageUserById(Update update) {
        String[] args = update.getMessage().getText().split(" ");
        long userId;

        userId = Long.parseLong(args[1]);

        String textToSend = EmojiParser.parseToUnicode(":exclamation: *Сообщение от администратора: *\n" + update.getMessage().getText().substring(update.getMessage().getText().indexOf(args[1]) + args[1].length() + 1)) + "  ";
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(userId);
        sendMessage.setText(textToSend);
        sendMessage.setParseMode(ParseMode.MARKDOWNV2);

        return sendMessage;
    }

    private SendMessage sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        return message;
    }
}
