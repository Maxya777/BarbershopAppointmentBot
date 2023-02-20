package com.example.barberskitchenbot.keyboards;

import com.example.barberskitchenbot.model.ButtonName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class InlineKeyboard {

    public InlineKeyboardMarkup getInlineMessageButtons() {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        InlineKeyboardButton oneMasterButton = new InlineKeyboardButton("Анатолий Щербина");
        InlineKeyboardButton twoMasterButton = new InlineKeyboardButton("Дарья Королькова");
        InlineKeyboardButton threeMasterButton = new InlineKeyboardButton("Никита Саевич");

        oneMasterButton.setCallbackData(ButtonName.ANATOLII_MASTER.getButtonName());
        twoMasterButton.setCallbackData(ButtonName.DARYA_MASTER.getButtonName());
        threeMasterButton.setCallbackData(ButtonName.NIKITA_MASTER.getButtonName());

        List<InlineKeyboardButton> oneRowInline = new ArrayList<>();
        oneRowInline.add(oneMasterButton);

        List<InlineKeyboardButton> twoRowInline = new ArrayList<>();
        twoRowInline.add(twoMasterButton);

        List<InlineKeyboardButton> threeRowInline = new ArrayList<>();
        threeRowInline.add(threeMasterButton);

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        rowsInline.add(oneRowInline);
        rowsInline.add(twoRowInline);
        rowsInline.add(threeRowInline);

        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    public InlineKeyboardMarkup inlineButtonsLink() {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        InlineKeyboardButton websiteButton = new InlineKeyboardButton("Наш сайт");
        websiteButton.setUrl("https://barberskitchen.ru/");

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(websiteButton);

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        rowsInline.add(rowInline);

        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    public InlineKeyboardMarkup inlineButtonsMapLink() {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        InlineKeyboardButton websiteButtonYandex = new InlineKeyboardButton("Яндекс Карты");
        websiteButtonYandex.setUrl("https://yandex.ru/");

        InlineKeyboardButton websiteButton2Gis = new InlineKeyboardButton("2ГИС Карты");
        websiteButton2Gis.setUrl("https://2gis.ru/");

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(websiteButtonYandex);
        rowInline.add(websiteButton2Gis);

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        rowsInline.add(rowInline);

        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    public InlineKeyboardMarkup appointmentButtonForAdmin() {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        InlineKeyboardButton appointmentButton = new InlineKeyboardButton("Записать данного клиента");

        appointmentButton.setCallbackData(ButtonName.APPOINTMENT.getButtonName());

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(appointmentButton);

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        rowsInline.add(rowInline);

        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    public InlineKeyboardMarkup sendRecordDateRequest() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        LocalDate now = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            LocalDate date = now.plusDays(i);
            String text = date.format(DateTimeFormatter.ofPattern("EEE, MMM d"));
            String data = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
            InlineKeyboardButton button = new InlineKeyboardButton(text);
            button.setCallbackData("date:" + data);
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(button);
            rows.add(row);
        }
        markup.setKeyboard(rows);
        return markup;
    }

    public InlineKeyboardMarkup sendRecordTimeRequest(LocalDate selectedDate) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(21, 0);
        LocalDateTime startDateTime = LocalDateTime.of(selectedDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(selectedDate, endTime);
        while (startDateTime.isBefore(endDateTime)) {
            String text = startDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            String data = startDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            InlineKeyboardButton button = new InlineKeyboardButton(text);
            button.setCallbackData("time:" + data);
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(button);
            rows.add(row);
            startDateTime = startDateTime.plusHours(1);
        }
        markup.setKeyboard(rows);
        return markup;
    }

    public InlineKeyboardMarkup sendServiceRequest() {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton buttonCut = new InlineKeyboardButton("Стрижка");
        buttonCut.setCallbackData("Стрижка от мастера интрижка");
        row1.add(buttonCut);
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton buttonBeard = new InlineKeyboardButton("Стрижка бороды");
        buttonBeard.setCallbackData("Стрижка бороды");
        row2.add(buttonBeard);
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton buttonWax = new InlineKeyboardButton("Ваксинг");
        buttonWax.setCallbackData("Ваксинг");
        row3.add(buttonWax);

        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        markup.setKeyboard(rows);

        return markup;
    }

    public InlineKeyboardMarkup sendMasterRequest() {

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton buttonAnatolii = new InlineKeyboardButton("Анатолий");
        buttonAnatolii.setCallbackData("Анатолий Щербина");
        row.add(buttonAnatolii);
        InlineKeyboardButton buttonDarya = new InlineKeyboardButton("Дарья");
        buttonDarya.setCallbackData("Дарья Королькова");
        row.add(buttonDarya);
        InlineKeyboardButton buttonNikita = new InlineKeyboardButton("Никита");
        buttonNikita.setCallbackData("Никита Саевич");
        row.add(buttonNikita);

        rows.add(row);
        markup.setKeyboard(rows);

        return markup;
    }
}
