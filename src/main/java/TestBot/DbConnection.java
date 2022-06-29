package TestBot;

import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


    public  class DbConnection {
        public Connection connection = null;

        public DbConnection() throws SQLException {
            connection = null;
            String url = "jdbc:sqlite:BotDb.sqlite";

            try {
                connection = DriverManager.getConnection(url);
            } catch (Exception e) {
                System.err.print(e.getMessage());
                e.printStackTrace();
                return;
            }
            System.out.println("Opened database successfully");
        }

        public void insertSubscription(Message userMessage) throws SQLException {
            if (userMessage.hasLocation()) {
                long chatId = userMessage.getChatId();
                double lat = userMessage.getLocation().getLatitude();
                double lon = userMessage.getLocation().getLongitude();
                PreparedStatement stm;
                String sqlInsert = "INSERT INTO Subscription (chatId, lat, lon) VALUES (?,?,?);";
                stm = connection.prepareStatement(sqlInsert);
                stm.setLong(1, chatId);
                stm.setDouble(2, lat);
                stm.setDouble(3, lon);
                stm.executeUpdate();
                connection.close();
            } else if (userMessage.hasText()) {
                long chatId = userMessage.getChatId();
                String city = userMessage.getText();
                PreparedStatement stm;
                String sqlInsert = "INSERT INTO Subscription (chatId, city) VALUES (?,?);";
                stm = connection.prepareStatement(sqlInsert);
                stm.setLong(1, chatId);
                stm.setString(2, city);
                stm.executeUpdate();
                stm.close();
            }
        }

        public void removeSubscription(Message userMessage) throws SQLException {
            long chatId = userMessage.getChatId();
            PreparedStatement stm;
            String sqlRemove = "DELETE FROM Subscription WHERE chatId = ?;";
            stm = connection.prepareStatement(sqlRemove);
            stm.setLong(1, chatId);
            stm.execute();
            stm.close();
        }
    }

