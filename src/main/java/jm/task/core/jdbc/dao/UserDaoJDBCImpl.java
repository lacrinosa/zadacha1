package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    // Создаем логгер
    private static final Logger logger = LoggerFactory.getLogger(UserDaoJDBCImpl.class);

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String query = "CREATE TABLE IF NOT EXISTS Users (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50), " +
                "lastName VARCHAR(50), " +
                "age TINYINT)";
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
            logger.info("Table 'Users' created or already exists.");
        } catch (SQLException e) {
            logger.error("Error creating table 'Users':", e);
        }
    }

    public void dropUsersTable() {
        String query = "DROP TABLE IF EXISTS Users";
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
            logger.info("Table 'Users' dropped.");
        } catch (SQLException e) {
            logger.error("Error dropping table 'Users':", e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String query = "INSERT INTO Users (name, lastName, age) VALUES (?, ?, ?)";
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            logger.info("User с именем — {} {} {} добавлен в базу данных.", name, lastName, age);
        } catch (SQLException e) {
            logger.error("Error saving user {} {}: ", name, lastName, e);
        }
    }

    public void removeUserById(long id) {
        String query = "DELETE FROM Users WHERE id = ?";
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            logger.info("User with ID {} was removed from the database.", id);
        } catch (SQLException e) {
            logger.error("Error removing user with ID {}: ", id, e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM Users";
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("lastName");
                byte age = resultSet.getByte("age");
                User user = new User(id, name, lastName, age);
                users.add(user);

                logger.info("User added: {}", user);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving users from database:", e);
        }
        return users;
    }

    public void cleanUsersTable() {
        String query = "DELETE FROM Users";
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
            logger.info("Users table cleaned.");
        } catch (SQLException e) {
            logger.error("Error cleaning users table:", e);
        }
    }
}
