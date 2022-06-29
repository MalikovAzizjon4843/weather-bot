import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface TelegramService {
    SendMessage start(Update update, String chatId);

    SendMessage deleteKeyboard(Update update, String chatId);

    SendMessage shareContact(Update update, String chatId);

    SendMessage shareLocation(Update update, String chatId);

    EditMessageText mainMenu(Update update, String chatId);

    EditMessageText uzbekinline(Update update, String chatId);

    SendMessage europainline(Update update, String chatId);

    SendLocation sendLocation(Update update, String chatId);
}
