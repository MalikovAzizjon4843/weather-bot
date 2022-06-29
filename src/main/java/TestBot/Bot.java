package TestBot;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Bot extends TelegramLongPollingBot {

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new Bot());
    }

    private static final String URL_TEMPLATE =
            "http://api.openweathermap.org/data/2.5/weather?%s&units=metric&appid=f6ac6fcc3347e99980399429cdca7343";
    public Bot(){
        ScheduledExecutorService service= Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(sendNotification,0,24,TimeUnit.HOURS);
    }

    private static CityWeather getWeatherByCityName(String city) throws Exception {

        String urlString = String.format(URL_TEMPLATE, city);
        URL url = new URL(urlString);
        URLConnection urlConnection = url.openConnection();


        if (urlConnection instanceof HttpURLConnection) {
            System.out.println(((HttpURLConnection) urlConnection).getResponseCode());
            InputStream stream = urlConnection.getInputStream();
            Gson gson = new Gson();

            CityWeather cityWeather = gson.fromJson(new InputStreamReader(stream), new TypeToken<CityWeather>() {
            }.getType());

            return cityWeather;
        } else {
            throw new RuntimeException("It is not http connection");
        }

    }

    public void sendForecast(Message userMessage) {
        if (userMessage.hasLocation()) {
            double lat = userMessage.getLocation().getLatitude();
            double lon = userMessage.getLocation().getLongitude();
            try {
                String locRequest = String.format("lat=%f&lon=%f", lat, lon);
                CityWeather output = getWeatherByCityName(locRequest);
                sendMsg(userMessage, output.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                String cityRequest = "q=" + userMessage.getText();
                CityWeather output = getWeatherByCityName(cityRequest);
                sendMsg(userMessage, output.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendForecast(User user) {
        long chatId = user.chatId;
        if (user.lon != null && user.lat != null) {
            Double lat = user.lat;
            Double lon = user.lon;
            try {
                String locRequest = String.format("lat=%f&lon=%f", lat, lon);
                CityWeather output = getWeatherByCityName(locRequest);
                SendMessage send = new SendMessage();
                send.setChatId(String.valueOf(chatId));
                send.setText(output.toString());
                execute(send);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                String cityRequest = "q="+user.city;
                CityWeather output = getWeatherByCityName(cityRequest);
                SendMessage send = new SendMessage();
                send.setChatId(String.valueOf(chatId));
                send.setText(output.toString());
                execute(send);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpdateReceived(Update update) {

        Thread thread = new Thread() {
            @Override
            public void run() {

                Message userMessage = update.getMessage();

                if (userMessage.isReply() && userMessage.getReplyToMessage().getText().equals("Please send your location or city you want to subscribe as a reply to this message.")) {
                    DbConnection connection = null;
                    try {
                        connection = new DbConnection();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        connection.insertSubscription(userMessage);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    sendMsg(userMessage, "You've been subscribed\n and will receive your forecast every day.");
                    return;
                }

                else if (userMessage.hasText()) {
                    String msgText = userMessage.getText();
                    if (msgText.equals("/start")) {
                        sendMsg(userMessage, "Welcome to WeatherBot.");
                    }

                    else if (msgText.equals("/location")) {
                        sendMsg(userMessage, "Please send your location or city you want to subscribe as a reply to this message.");
                        return;
                    } else if (msgText.equals("/unsubscribe")) {
                        DbConnection connection = null;
                        try {
                            connection = new DbConnection();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        try {
                            connection.removeSubscription(userMessage);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    else {

                        sendForecast(userMessage);
                        return;
                    }
                }

                else if (userMessage.hasLocation()) {
                    sendForecast(userMessage);
                    return;
                }

                else {
                    sendMsg(userMessage, "This is not a city or location");
                    return;
                }
            }
        };
        thread.start();
    }

    @Override
    public String getBotUsername() {
        return "weatherInTown_bot";
    }

    private void sendMsg(Message msg, String text) {
        SendMessage s = new SendMessage();
        s.setChatId(String.valueOf(msg.getChatId()));
        s.setText(text);
        try {
            execute(s);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return "5014572088:AAEQ9HDZpzeUnZq2SVNj26BnY-uiZ0QM6ic";
    }


    public void sendToAll() throws SQLException {
        DbConnection con = new DbConnection();
        Statement stmt = con.connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM Subscription");
        List<User> listOfRequest = new ArrayList<>();
        while (resultSet.next()) {
            User temp = new User();
            temp.chatId = resultSet.getLong("chatId");
            temp.city = resultSet.getString("city");
            temp.lat = resultSet.getDouble("lat");
            temp.lon = resultSet.getDouble("lon");
            listOfRequest.add(temp);
        }
        for (User user : listOfRequest) {
            sendForecast(user);
        }
    }

    Runnable sendNotification = new Runnable() {
        @Override
        public void run() {
            try {
                sendToAll();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };
}


