package TestBot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.sql.SQLException;

public class Main extends TelegramLongPollingBot {
    public static void main(String[] args) throws SQLException, TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot( new Main());
        try{
            telegramBotsApi.registerBot(new Main());
        }
        catch (TelegramApiException e){
            e.printStackTrace();
        }
    }


    @Override
    public String getBotUsername() {
        return "weatherInTown_bot";
    }

    @Override
    public String getBotToken() {
        return "5014572088:AAEQ9HDZpzeUnZq2SVNj26BnY-uiZ0QM6ic";
    }

    @Override
    public void onUpdateReceived(Update update) {

    }
}
