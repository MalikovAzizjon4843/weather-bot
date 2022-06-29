//import lombok.SneakyThrows;
//import org.telegram.telegrambots.meta.TelegramBotsApi;
//import org.telegram.telegrambots.meta.api.objects.Message;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
//import org.telegram.telegrambots.meta.generics.BotOptions;
//import org.telegram.telegrambots.meta.generics.LongPollingBot;
//import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
//
//import java.util.List;
//
//import static jdk.javadoc.internal.tool.Main.execute;
//
//public class WeatherMainBot implements LongPollingBot {
//    public static void main(String[] args) throws TelegramApiException {
//        System.out.println("Success!  Telegram bot running...");
//        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
//        telegramBotsApi.registerBot( new WeatherMainBot());
//    }
//
//    public static final String username = "weatherInTown_bot";
//    public static final String token = "5014572088:AAEQ9HDZpzeUnZq2SVNj26BnY-uiZ0QM6ic";
//    public String state = null;
//    TelegramServiceImpl telegramService = new TelegramServiceImpl();
//
//    @Override
//    public String getBotUsername() {
//        return username;
//    }
//
//    @Override
//    public String getBotToken() {
//        return token;
//    }
//
//    @SneakyThrows
//    @Override
//    public void onUpdateReceived(Update update) {
//        if (update.hasMessage()) {
//            Message message = update.getMessage();
//            String text = update.getMessage().getText();
//            String chatId = String.valueOf(update.getMessage().getChatId());
//            if (update.getMessage().hasText()) {
//                if (text.equals("/start")) {
//                    execute(telegramService.start(update, chatId));
//                    state = BotState.START;
//                }
//                //oddiy text
//            } else if (message.hasLocation()) {
//                execute(telegramService.deleteKeyboard(update, chatId));
//                execute(telegramService.shareLocation(update, chatId));
//
//                //location
//            } else if (message.hasContact()) {
//                execute(telegramService.shareContact(update, chatId));
//                //contact
//            }
//        } else if (update.hasCallbackQuery()) {
//            String chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
//            //inline
//            switch (update.getCallbackQuery().getData()) {
//                case "#addresses":
//                    execute(telegramService.sendLocation(update, chatId));
//                    break;
//                case "#uzbek":
//                    execute(telegramService.uzbekinline(update, chatId));
//                    break;
//                case "#europa":
//                    execute(telegramService.europainline(update, chatId));
//                    break;
//                case "#backMenu":
//                    execute(telegramService.mainMenu(update, chatId));
//                    break;
//            }
//        }
//    }
//}
