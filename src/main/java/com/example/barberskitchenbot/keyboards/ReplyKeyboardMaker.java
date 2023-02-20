package com.example.barberskitchenbot.keyboards;

import com.example.barberskitchenbot.model.ButtonName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReplyKeyboardMaker {

    public ReplyKeyboardMarkup getMainMenuKeyboardForClient() {

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(ButtonName.SIGN_UP.getButtonName()));
        keyboardRows.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(ButtonName.VIEW_RECORDINGS.getButtonName()));
        row2.add(new KeyboardButton(ButtonName.CANCEL_RECORDING.getButtonName()));
        keyboardRows.add(row2);

        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton(ButtonName.LEAVE_REVIEW.getButtonName()));
        keyboardRows.add(row3);

        KeyboardRow row4 = new KeyboardRow();
        row4.add(new KeyboardButton(ButtonName.INFO.getButtonName()));
        keyboardRows.add(row4);

        final ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
        keyboardMarkup.setKeyboard(keyboardRows);

        return keyboardMarkup;
    }

    public ReplyKeyboardMarkup getMainMenu() {

        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton(ButtonName.BACK_FOR_START_MENU.getButtonName()));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row);

        final ReplyKeyboardMarkup replyKeyboardMarkupSub = new ReplyKeyboardMarkup();
        replyKeyboardMarkupSub.setKeyboard(keyboard);
        replyKeyboardMarkupSub.setSelective(true);
        replyKeyboardMarkupSub.setResizeKeyboard(true);
        replyKeyboardMarkupSub.setOneTimeKeyboard(false);

        return replyKeyboardMarkupSub;
    }

    public ReplyKeyboardMarkup requestContact() {

        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText(ButtonName.COMPLETE_IDENTIFICATION.getButtonName());
        keyboardButton.setRequestContact(true);

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(keyboardButton);

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(keyboardFirstRow);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup startKeyboard() {

        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("/start"));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row);

        final ReplyKeyboardMarkup replyKeyboardMarkupSub = new ReplyKeyboardMarkup();

        replyKeyboardMarkupSub.setKeyboard(keyboard);
        replyKeyboardMarkupSub.setSelective(true);
        replyKeyboardMarkupSub.setResizeKeyboard(true);
        replyKeyboardMarkupSub.setOneTimeKeyboard(false);

        return replyKeyboardMarkupSub;
    }

    public ReplyKeyboardMarkup getMainMenuKeyboardForAdmin() {

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Отправить сообщение пользователю"));
        keyboardRows.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Отправить сообщение всем пользователям"));
        keyboardRows.add(row2);

        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton("Забанить пользователя"));
        keyboardRows.add(row3);

        KeyboardRow row4 = new KeyboardRow();
        row4.add(new KeyboardButton("Снять бан с пользователя"));
        keyboardRows.add(row4);

        final ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
        keyboardMarkup.setKeyboard(keyboardRows);

        return keyboardMarkup;
    }
}
