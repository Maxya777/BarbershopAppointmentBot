package com.example.barberskitchenbot.service;

import com.example.barberskitchenbot.configuration.BotConfig;
import com.example.barberskitchenbot.exceptions.BotException;
import com.example.barberskitchenbot.model.Appointment;
import com.example.barberskitchenbot.model.ButtonName;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


@Component
@RequiredArgsConstructor
public class BotService extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final MainMenuServiceForClient mainMenuServiceForClient;
    private final KeyboardServiceForClient keyboardServiceForClient;
    private final RegistrationService registrationService;
    private final KeyboardServiceForAdmin keyboardServiceForAdmin;
    private final MainMenuServiceForAdmin mainMenuServiceForAdmin;
    private final AppointmentService appointmentService;
    private final BotException botException;
    boolean startWith = false;
    private Appointment appointment = new Appointment();

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText() && !startWith) {
            String messageText = update.getMessage().getText();
            String firstName = update.getMessage().getChat().getFirstName();
            long chatId = update.getMessage().getChatId();

            if (messageText.contains("/sendAll") && botConfig.getOwnerId() == chatId) {
                try {
                    execute(mainMenuServiceForAdmin.sendMessageAllUsers(update));
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else if (messageText.contains("/send") && botConfig.getOwnerId() == chatId) {
                try {
                    execute(mainMenuServiceForAdmin.sendMessageUserById(update));
                } catch (TelegramApiException e) {
                    if (e.getMessage().contains("Bad Request: chat not found")) {
                        try {
                            execute(botException.sendMessageIncorrectlyId(update));
                        } catch (TelegramApiException ex) {
                            throw new RuntimeException(ex.getMessage());
                        }
                        return;
                    } else {
                        System.out.println("Error sending message: " + e.getMessage());
                    }
                }
            } else {
                switch (messageText) {
                    case "/start" -> {
                        try {
                            execute(mainMenuServiceForClient.startMessage(chatId, firstName));
                            execute(keyboardServiceForClient.sendIdentificationMenu(chatId, firstName + ", для использования полного функционала бота "
                                    + " Вам нужно пройти идентификацию. Для этого нажмите кнопку \"Пройти идентификацию\" внизу экрана"
                                    + ", а затем согласитесь поделиться номером телефона"));

                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                    case "Отправить сообщение пользователю" -> {
                        try {
                            execute(mainMenuServiceForAdmin.getHintSendOne(chatId));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                    case "Отправить сообщение всем пользователям" -> {
                        try {
                            execute(mainMenuServiceForAdmin.getHintSendAll(chatId));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                    case "Записаться" -> {
                        try {
                            execute(keyboardServiceForClient.sendReturnMainMenu(chatId, "Вы можете сами обозначить день и примерное время для записи, написав сообщение и отправив его в чат. \n Пожалуйста не нажимайте кнопку \"Главное меню\" пока администратор не запишет Вас на приём к специалисту.\n "
                                    + "Помните, что за спам в чате администратор может назначить Вам бан."));
                            execute(keyboardServiceForClient.inlineMenuBarbers(chatId, "Выберите специалиста"));
                            startWith = true;
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                    case "Посмотреть мои записи" -> {
                        try {
                            execute(appointmentService.viewAppointmentsById(chatId));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                    case "Отменить запись" -> {
                        try {
                            execute(appointmentService.removeAppointment(chatId));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                    case "Оставить отзыв" -> {
                        try {
                            execute(keyboardServiceForClient.inlineLinkMapWebsite(chatId, EmojiParser.parseToUnicode("Мы будем очень вам благодарны, если вы оставите отзыв о нашей парикмахерской на картах :smile:")));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                    case "О парикмахерской" -> {
                        try {
                            execute(mainMenuServiceForClient.getInformationAboutBarbershop(chatId));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                    default -> {
                        try {
                            execute(mainMenuServiceForClient.getDefaultResponse(chatId));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                }
            }
            if (update.getMessage().getText().equals("/start")) {
                startWith = true;

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (update.hasMessage() && !update.getMessage().hasContact() && startWith) {
                            try {
                                execute(keyboardServiceForClient.sendMessageStartKeyboardForClient(chatId, "Время авторизации истекло, пожалуйста нажмите кнопку \"/start\" для повторной попытки"));
                                startWith = false;
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e.getMessage());
                            }
                        }
                    }
                }, 10000);
            }
        } else if (update.hasMessage() && update.getMessage().hasContact() && startWith) {
            Contact contact = update.getMessage().getContact();
            contact.getPhoneNumber();
            registrationService.registerClient(update.getMessage());

            if (!Objects.equals(contact.getUserId(), botConfig.getOwnerId())) {
            try {
                execute(keyboardServiceForClient.sendMessageKeyboardMenuForClient(contact.getUserId(), contact.getFirstName() + ", поздравляю, Вы идентифицированы в нашей парикмахерской!"
                        + " Для удобства использования Вы можете воспользоваться клавиатурой внизу экрана "));
                startWith = false;
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            } else {
                try {
                    startWith = false;
                    execute(keyboardServiceForAdmin.sendMessageKeyboardMenuForAdmin(contact.getUserId(), "Доступно меню для администрации бота"));
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }

        } else if (update.hasMessage() && update.getMessage().hasText() && startWith) {
            String messageText = update.getMessage().getText();
            String username = update.getMessage().getChat().getUserName();
            String firstName = update.getMessage().getChat().getFirstName();
            long chatId = update.getMessage().getChatId();

            if (messageText.contains("/sendAll") && botConfig.getOwnerId() == chatId) {
                try {
                    execute(mainMenuServiceForAdmin.sendMessageAllUsers(update));
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else if (messageText.contains("/send") && botConfig.getOwnerId() == chatId) {
                try {
                    execute(mainMenuServiceForAdmin.sendMessageUserById(update));
                } catch (TelegramApiException e) {
                    if (e.getMessage().contains("Bad Request: chat not found")) {
                        try {
                            execute(botException.sendMessageIncorrectlyId(update));
                        } catch (TelegramApiException ex) {
                            throw new RuntimeException(ex.getMessage());
                        }
                        return;
                    } else {
                        System.out.println("Error sending message: " + e.getMessage());
                    }
                }
            } else {
                switch (messageText) {
                    case "Главное меню" -> {
                        try {
                            execute(keyboardServiceForClient.sendMessageKeyboardMenuForClient(chatId, "Вы вернулись в главное меню, отправка сообщений администратору недоступна"));
                            startWith = false;
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                    default -> {
                        try {
                            execute(mainMenuServiceForClient.getMessageFromUserToAdmin(botConfig.getOwnerId(), messageText, username, firstName, chatId));
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                }

            }
        }
        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().equals(ButtonName.ANATOLII_MASTER.getButtonName())
                    || update.getCallbackQuery().getData().equals(ButtonName.NIKITA_MASTER.getButtonName())
                    || update.getCallbackQuery().getData().equals(ButtonName.DARYA_MASTER.getButtonName())) {
                try {
                    execute(mainMenuServiceForClient.responseUserOnInlineButton(update));
                    execute(keyboardServiceForClient.inlineLinkWebsite(update.getCallbackQuery().getFrom().getId(), "Вы так же можете сделать запись на нашем сайте"));
                    execute(mainMenuServiceForAdmin.responseAdminOnInlineButton(update));
                    long chatId = update.getCallbackQuery().getFrom().getId();
                    appointment.setClientId(chatId);
                    execute(keyboardServiceForAdmin.appointmentInlineButtonForAdmin(EmojiParser.parseToUnicode("Сделать запись клиента \uD83D\uDD3D")));
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else if (update.getCallbackQuery().getData().equals(ButtonName.APPOINTMENT.getButtonName())) {
                try {
                    execute(keyboardServiceForAdmin.sendInlineKeyboardDateAppointment(update.getCallbackQuery().getFrom().getId(), "Выберите дату записи"));
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else if (update.getCallbackQuery().getData().startsWith("date:")) {
                String dateString = update.getCallbackQuery().getData().substring(5);
                LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
                appointment.setDateAppointment(date);
                try {
                    execute(keyboardServiceForAdmin.sendInlineKeyboardTimeAppointment(update.getCallbackQuery().getFrom().getId(), "Выберите время записи", date));
                    execute(keyboardServiceForAdmin.responseOnInlineButton(update));
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else if (update.getCallbackQuery().getData().startsWith("time:")) {
                String timeString = update.getCallbackQuery().getData().substring(5);
                LocalTime time = LocalTime.parse(timeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                appointment.setTimeAppointment(time);
                try {
                    execute(keyboardServiceForAdmin.sendInlineKeyboardMasterAppointment(update.getCallbackQuery().getFrom().getId(), "Выберите мастера"));
                    execute(keyboardServiceForAdmin.responseOnInlineButton(update));
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else if (update.getCallbackQuery().getData().equals("Анатолий Щербина")
                    || update.getCallbackQuery().getData().equals("Дарья Королькова")
                    || update.getCallbackQuery().getData().equals("Никита Саевич")) {
                String master = update.getCallbackQuery().getData();
                appointment.setMaster(master);
                try {
                    execute(keyboardServiceForAdmin.sendInlineKeyboardServiceAppointment(update.getCallbackQuery().getFrom().getId(), "Выберите услугу"));
                    execute(keyboardServiceForAdmin.responseOnInlineButton(update));
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else if (update.getCallbackQuery().getData().equals("Стрижка от мастера интрижка")
                    || update.getCallbackQuery().getData().equals("Стрижка бороды")
                    || update.getCallbackQuery().getData().equals("Ваксинг")) {
                String service = update.getCallbackQuery().getData();
                appointment.setTypeService(service);
                appointmentService.createAppointment(appointment.getClientId(),
                        appointment.getDateAppointment(),
                        appointment.getTimeAppointment(),
                        appointment.getTypeService(),
                        appointment.getMaster());
                try {
                    execute(mainMenuServiceForClient.getAppointmentConfirm(update.getCallbackQuery().getFrom().getId()));
                    execute(keyboardServiceForAdmin.responseOnInlineButton(update));
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
    }
}
