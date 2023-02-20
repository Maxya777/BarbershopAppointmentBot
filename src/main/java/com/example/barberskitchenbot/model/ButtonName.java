package com.example.barberskitchenbot.model;

import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum ButtonName {

    SIGN_UP(EmojiParser.parseToUnicode("Записаться")),
    VIEW_RECORDINGS("Посмотреть мои записи"),
    CANCEL_RECORDING("Отменить запись"),
    LEAVE_REVIEW("Оставить отзыв"),
    INFO("О парикмахерской"),
    BACK_FOR_START_MENU ("Главное меню"),
    COMPLETE_IDENTIFICATION("Пройти идентификацию"),
    ANATOLII_MASTER("ANATOLII_MASTER"),
    DARYA_MASTER ("DARYA_MASTER"),
    NIKITA_MASTER ("NIKITA_MASTER"),
    APPOINTMENT("APPOINTMENT"),
    DATA("Указать дату и время записи");

    private final String buttonName;
}