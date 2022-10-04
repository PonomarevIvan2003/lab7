package ServerModule.util;

import common.exceptions.DatabaseManagerException;
import common.exceptions.MultiUserException;
import common.util.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUserManager {
    private final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + DatabaseManager.USER_TABLE +
            " (" + DatabaseManager.USER_TABLE_ID_COLUMN + " SERIAL PRIMARY KEY, " +
            DatabaseManager.USER_TABLE_USERNAME_COLUMN + " VARCHAR(50), " +
            DatabaseManager.USER_TABLE_PASSWORD_COLUMN + " VARCHAR(200), " +
            DatabaseManager.USER_TABLE_ONLINE_COLUMN + " BOOLEAN)";
    private final String SELECT_USER_BY_ID = "SELECT * FROM " + DatabaseManager.USER_TABLE +
            " WHERE " + DatabaseManager.USER_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_USER_BY_USERNAME = "SELECT * FROM " + DatabaseManager.USER_TABLE +
            " WHERE " + DatabaseManager.USER_TABLE_USERNAME_COLUMN + " = ?";
    private final String SELECT_USER_BY_USERNAME_AND_PASSWORD = SELECT_USER_BY_USERNAME + " AND " +
            DatabaseManager.USER_TABLE_PASSWORD_COLUMN + " = ?";
    private final String INSERT_USER = "INSERT INTO " +
            DatabaseManager.USER_TABLE + " (" +
            DatabaseManager.USER_TABLE_USERNAME_COLUMN + ", " +
            DatabaseManager.USER_TABLE_PASSWORD_COLUMN + ", " +
            DatabaseManager.USER_TABLE_ONLINE_COLUMN + ") VALUES (?, ?, ?)";
    private final String UPDATE_ONLINE_BY_USERNAME_AND_PASSWORD = "UPDATE " + DatabaseManager.USER_TABLE + " SET " +
            DatabaseManager.USER_TABLE_ONLINE_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.USER_TABLE_USERNAME_COLUMN + " = ?" + " AND " +
            DatabaseManager.USER_TABLE_PASSWORD_COLUMN + " = ?";
    private final String SWITCH_OFF_ALL_USERS = "UPDATE " + DatabaseManager.USER_TABLE + " SET " +
            DatabaseManager.USER_TABLE_ONLINE_COLUMN + " = ?";

    private DatabaseManager databaseManager;

    public DatabaseUserManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        createTable();
        switchOffAllUsers();
    }

    private void createTable() {
        PreparedStatement createUser = null;
        try {
            createUser = databaseManager.doPreparedStatement(CREATE_USER_TABLE, false);
            createUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            databaseManager.closePreparedStatement(createUser);
        }
    }

    public void switchOffAllUsers() {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SWITCH_OFF_ALL_USERS, false);
            preparedStatement.setBoolean(1, false);
            if (preparedStatement.executeUpdate() == 0) throw new SQLException();
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при выполнении запроса SWITCH_OFF_ALL_USERS!");
        }
    }

    public User getUserById(long userID) throws SQLException {
        User user;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_USER_BY_ID, false);
            preparedStatement.setLong(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User(
                        resultSet.getString(DatabaseManager.USER_TABLE_USERNAME_COLUMN),
                        resultSet.getString(DatabaseManager.USER_TABLE_PASSWORD_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_USER_BY_ID!");
            throw new SQLException();
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
        return user;
    }

    public boolean checkUserByUsernameAndPassword(User user) throws DatabaseManagerException, MultiUserException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD, false);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) return false;
            if (resultSet.getBoolean(DatabaseManager.USER_TABLE_ONLINE_COLUMN)) throw new MultiUserException();
            return true;
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_USER_BY_USERNAME_AND_PASSWORD!");
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
    }

    public void updateOnline(User user, boolean newValue) throws DatabaseManagerException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(UPDATE_ONLINE_BY_USERNAME_AND_PASSWORD, false);
            preparedStatement.setBoolean(1, newValue);
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getPassword());
            if (preparedStatement.executeUpdate() == 0) throw new SQLException();
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при выполнении запроса UPDATE_ONLINE_BY_USERNAME_AND_PASSWORD!");
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
    }

    public int getUserIdByUsername(User user) throws DatabaseManagerException {
        int userID;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_USER_BY_USERNAME, false);
            preparedStatement.setString(1, user.getLogin());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userID = resultSet.getInt(DatabaseManager.USER_TABLE_ID_COLUMN);
            } else userID = -1;
            return userID;
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_USER_BY_USERNAME!");
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
    }

    public boolean insertUser(User user) throws DatabaseManagerException {
        PreparedStatement preparedStatement = null;
        try {
            if (getUserIdByUsername(user) != -1) return false;

            preparedStatement = databaseManager.doPreparedStatement(INSERT_USER, false);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setBoolean(3, true);
            System.out.println(user.getLogin());
            System.out.println(user.getPassword());
            if (preparedStatement.executeUpdate() == 0) throw new SQLException();
            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Произошла ошибка при выполнении запроса INSERT_USER!");
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
    }
}
