package ServerModule.util;

import common.data.Coordinates;
import common.data.Flat;
import common.data.House;
import common.data.Transport;
import common.exceptions.DatabaseManagerException;
import common.util.FlatLite;
import common.util.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public class DatabaseCollectionManager {
    private final String CREATE_FLAT_TABLE = "CREATE TABLE IF NOT EXISTS " + DatabaseManager.FLAT_TABLE +
            " (" + DatabaseManager.FLAT_TABLE_ID_COLUMN + " SERIAL PRIMARY KEY, " +
            DatabaseManager.FLAT_TABLE_KEY_COLUMN + " INT, " +
            DatabaseManager.FLAT_TABLE_NAME_COLUMN + " VARCHAR(200), " +
            DatabaseManager.FLAT_TABLE_COORDINATES_ID_COLUMN + " INT REFERENCES " + DatabaseManager.COORDINATES_TABLE + "(" + DatabaseManager.COORDINATES_TABLE_ID_COLUMN + "), " +
            DatabaseManager.FLAT_TABLE_CREATION_DATE_COLUMN + " VARCHAR(200), " +
            DatabaseManager.FLAT_TABLE_AREA_COLUMN + " BIGINT, " +
            DatabaseManager.FLAT_TABLE_NUMBER_OF_ROOMS_COLUMN + " INT, " +
            DatabaseManager.FLAT_TABLE_HEIGHT_COLUMN + " INT, " +
            DatabaseManager.FLAT_TABLE_NEWNESS_COLUMN + " BOOL, " +
            DatabaseManager.FLAT_TABLE_TRANSPORT_COLUMN + " VARCHAR(200), " +
            DatabaseManager.FLAT_TABLE_HOUSE_ID_COLUMN + " INT REFERENCES " + DatabaseManager.HOUSE_TABLE + "(" + DatabaseManager.HOUSE_TABLE_ID_COLUMN + "), " +
            DatabaseManager.FLAT_TABLE_USER_ID_COLUMN + " INT REFERENCES " + DatabaseManager.USER_TABLE + "(" + DatabaseManager.USER_TABLE_ID_COLUMN + "))" ;
    private final String SELECT_ALL_FLATS = "SELECT * FROM " + DatabaseManager.FLAT_TABLE;
    private final String SELECT_FLAT_BY_ID = SELECT_ALL_FLATS + " WHERE " +
            DatabaseManager.FLAT_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_FLAT_ID_BY_KEY = SELECT_ALL_FLATS + " WHERE " +
            DatabaseManager.FLAT_TABLE_KEY_COLUMN + " = ?";
    private final String SELECT_FLAT_BY_ID_AND_USER_ID = SELECT_FLAT_BY_ID + " AND " +
            DatabaseManager.FLAT_TABLE_USER_ID_COLUMN + " = ?";
    private final String INSERT_FLAT = "INSERT INTO " +
            DatabaseManager.FLAT_TABLE + " (" +
            DatabaseManager.FLAT_TABLE_KEY_COLUMN + ", " +
            DatabaseManager.FLAT_TABLE_NAME_COLUMN + ", " +
            DatabaseManager.FLAT_TABLE_COORDINATES_ID_COLUMN + ", " +
            DatabaseManager.FLAT_TABLE_CREATION_DATE_COLUMN + ", " +
            DatabaseManager.FLAT_TABLE_AREA_COLUMN + ", " +
            DatabaseManager.FLAT_TABLE_NUMBER_OF_ROOMS_COLUMN + ", " +
            DatabaseManager.FLAT_TABLE_HEIGHT_COLUMN + ", " +
            DatabaseManager.FLAT_TABLE_NEWNESS_COLUMN + ", " +
            DatabaseManager.FLAT_TABLE_TRANSPORT_COLUMN + ", " +
            DatabaseManager.FLAT_TABLE_HOUSE_ID_COLUMN + ", " +
            DatabaseManager.FLAT_TABLE_USER_ID_COLUMN + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String DELETE_FLAT_BY_KEY = "DELETE FROM " + DatabaseManager.FLAT_TABLE +
            " WHERE " + DatabaseManager.FLAT_TABLE_KEY_COLUMN + " = ?";
    private final String UPDATE_FLAT_NAME_BY_ID = "UPDATE " + DatabaseManager.FLAT_TABLE + " SET " +
            DatabaseManager.FLAT_TABLE_NAME_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.FLAT_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_FLAT_AREA_BY_ID = "UPDATE " + DatabaseManager.FLAT_TABLE + " SET " +
            DatabaseManager.FLAT_TABLE_AREA_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.FLAT_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_FLAT_NUMBER_OF_ROOMS_BY_ID = "UPDATE " + DatabaseManager.FLAT_TABLE + " SET " +
            DatabaseManager.FLAT_TABLE_NUMBER_OF_ROOMS_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.FLAT_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_FLAT_HEIGHT_BY_ID = "UPDATE " + DatabaseManager.FLAT_TABLE + " SET " +
            DatabaseManager.FLAT_TABLE_HEIGHT_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.FLAT_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_FLAT_NEWNESS_BY_ID = "UPDATE " + DatabaseManager.FLAT_TABLE + " SET " +
            DatabaseManager.FLAT_TABLE_NEWNESS_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.FLAT_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_FLAT_TRANSPORT_BY_ID = "UPDATE " + DatabaseManager.FLAT_TABLE + " SET " +
            DatabaseManager.FLAT_TABLE_TRANSPORT_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.FLAT_TABLE_ID_COLUMN + " = ?";

    private final String CREATE_COORDINATES_TABLE = "CREATE TABLE IF NOT EXISTS " + DatabaseManager.COORDINATES_TABLE +
             "(" + DatabaseManager.COORDINATES_TABLE_ID_COLUMN + " SERIAL PRIMARY KEY, " +
            DatabaseManager.COORDINATES_TABLE_X_COLUMN + " INT, " +
            DatabaseManager.COORDINATES_TABLE_Y_COLUMN + " double precision)";
    private final String SELECT_ALL_COORDINATES = "SELECT * FROM " + DatabaseManager.COORDINATES_TABLE;
    private final String SELECT_COORDINATES_BY_ID = SELECT_ALL_COORDINATES + " WHERE " + DatabaseManager.COORDINATES_TABLE_ID_COLUMN + " = ?";
    private final String DELETE_COORDINATES_BY_ID = "DELETE FROM " + DatabaseManager.COORDINATES_TABLE +
            " WHERE " + DatabaseManager.COORDINATES_TABLE_ID_COLUMN + " = ?";
    private final String INSERT_COORDINATES = "INSERT INTO " +
            DatabaseManager.COORDINATES_TABLE + " (" +
            DatabaseManager.COORDINATES_TABLE_X_COLUMN + ", " +
            DatabaseManager.COORDINATES_TABLE_Y_COLUMN + ") VALUES (?, ?)";
    private final String UPDATE_COORDINATES_BY_ID = "UPDATE " + DatabaseManager.COORDINATES_TABLE + " SET " +
            DatabaseManager.COORDINATES_TABLE_X_COLUMN + " = ?, " +
            DatabaseManager.COORDINATES_TABLE_Y_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.COORDINATES_TABLE_ID_COLUMN + " = ?";

    private final String CREATE_HOUSE_TABLE = "CREATE TABLE IF NOT EXISTS " + DatabaseManager.HOUSE_TABLE +
            "(" + DatabaseManager.HOUSE_TABLE_ID_COLUMN + " SERIAL PRIMARY KEY, " +
            DatabaseManager.HOUSE_TABLE_NAME_COLUMN + " VARCHAR(200), " +
            DatabaseManager.HOUSE_TABLE_YEAR_COLUMN + " INT, " +
            DatabaseManager.HOUSE_TABLE_NUMBER_OF_LIFTS_COLUMN + " INT)";
    private final String SELECT_ALL_HOUSES = "SELECT * FROM " + DatabaseManager.HOUSE_TABLE;
    private final String SELECT_HOUSE_BY_ID = SELECT_ALL_HOUSES + " WHERE " + DatabaseManager.HOUSE_TABLE_ID_COLUMN + " = ?";
    private final String DELETE_HOUSE_BY_ID = "DELETE FROM " + DatabaseManager.HOUSE_TABLE +
            " WHERE " + DatabaseManager.HOUSE_TABLE_ID_COLUMN + " = ?";
    private final String INSERT_HOUSE = "INSERT INTO " +
            DatabaseManager.HOUSE_TABLE + " (" +
            DatabaseManager.HOUSE_TABLE_NAME_COLUMN + ", " +
            DatabaseManager.HOUSE_TABLE_YEAR_COLUMN + ", " +
            DatabaseManager.HOUSE_TABLE_NUMBER_OF_LIFTS_COLUMN + ") VALUES (?, ?, ?)";
    private final String UPDATE_HOUSE_BY_ID = "UPDATE " + DatabaseManager.HOUSE_TABLE + " SET " +
            DatabaseManager.HOUSE_TABLE_NAME_COLUMN + " = ?, " +
            DatabaseManager.HOUSE_TABLE_YEAR_COLUMN + " = ?, " +
            DatabaseManager.HOUSE_TABLE_NUMBER_OF_LIFTS_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.HOUSE_TABLE_ID_COLUMN + " = ?";

    private DatabaseManager databaseManager;
    private DatabaseUserManager databaseUserManager;

    public DatabaseCollectionManager(DatabaseManager databaseManager, DatabaseUserManager databaseUserManager) {
        this.databaseManager = databaseManager;
        this.databaseUserManager = databaseUserManager;
        createTables();
    }

    private void createTables() {
        PreparedStatement createFlat = null;
        PreparedStatement createCoordinates = null;
        PreparedStatement createHouse = null;
        try {
            createHouse = databaseManager.doPreparedStatement(CREATE_HOUSE_TABLE, false);
            createHouse.executeUpdate();

            createCoordinates = databaseManager.doPreparedStatement(CREATE_COORDINATES_TABLE, false);
            createCoordinates.executeUpdate();

            createFlat = databaseManager.doPreparedStatement(CREATE_FLAT_TABLE, false);
            createFlat.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            databaseManager.closePreparedStatement(createHouse);
            databaseManager.closePreparedStatement(createCoordinates);
            databaseManager.closePreparedStatement(createFlat);
        }
    }

    private Flat returnFlat(ResultSet resultSet, int id) throws SQLException{
        String name = resultSet.getString(DatabaseManager.FLAT_TABLE_NAME_COLUMN);
        Coordinates coordinates = getCoordinatesByID(resultSet.getInt(DatabaseManager.FLAT_TABLE_COORDINATES_ID_COLUMN));
        LocalDateTime creationDate = LocalDateTime.parse(resultSet.getString(DatabaseManager.FLAT_TABLE_CREATION_DATE_COLUMN));
        Long area = resultSet.getLong(DatabaseManager.FLAT_TABLE_AREA_COLUMN);
        Integer numberOfRooms = resultSet.getInt(DatabaseManager.FLAT_TABLE_NUMBER_OF_ROOMS_COLUMN);
        Integer height = resultSet.getInt(DatabaseManager.FLAT_TABLE_HEIGHT_COLUMN);
        Boolean newness  = resultSet.getBoolean(DatabaseManager.FLAT_TABLE_NEWNESS_COLUMN);
        Transport transport  = Transport.valueOf(resultSet.getString(DatabaseManager.FLAT_TABLE_TRANSPORT_COLUMN));
        House house = getHouseByID(resultSet.getInt(DatabaseManager.FLAT_TABLE_HOUSE_ID_COLUMN));
        User owner = databaseUserManager.getUserById(resultSet.getInt(DatabaseManager.FLAT_TABLE_USER_ID_COLUMN));
        return new Flat(id, name, coordinates, creationDate, area, numberOfRooms, height, newness, transport, house, owner);
    }

    public TreeMap<Integer, Flat> getCollection() {
        TreeMap<Integer, Flat> flats = new TreeMap<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_ALL_FLATS, false);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(DatabaseManager.FLAT_TABLE_ID_COLUMN);
                int key = resultSet.getInt(DatabaseManager.FLAT_TABLE_KEY_COLUMN);
                flats.put(key, returnFlat(resultSet, id));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
        return flats;
    }

    private Coordinates getCoordinatesByID(int id) throws SQLException {
        Coordinates coordinates;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_COORDINATES_BY_ID, false);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                coordinates = new Coordinates(
                        resultSet.getInt(DatabaseManager.COORDINATES_TABLE_X_COLUMN),
                        resultSet.getFloat(DatabaseManager.COORDINATES_TABLE_Y_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_COORDINATES_BY_ID!");
            throw new SQLException(e);
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
        return coordinates;
    }

    private House getHouseByID(int id) throws SQLException {
        House house;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_HOUSE_BY_ID, false);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                house = new House(
                        resultSet.getString(DatabaseManager.HOUSE_TABLE_NAME_COLUMN),
                        resultSet.getInt(DatabaseManager.HOUSE_TABLE_YEAR_COLUMN),
                        resultSet.getInt(DatabaseManager.HOUSE_TABLE_NUMBER_OF_LIFTS_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_HOUSE_BY_ID!");
            throw new SQLException(e);
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
        return house;
    }

    private int getHouseIdByFlatID (int FlatID) throws SQLException {
        int houseID;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_FLAT_BY_ID, false);
            preparedStatement.setInt(1, FlatID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                houseID = resultSet.getInt(DatabaseManager.FLAT_TABLE_HOUSE_ID_COLUMN);
            } else throw new SQLException();
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_FLAT_BY_ID!");
            throw new SQLException(e);
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
        return houseID;
    }

    private int getCoordinatesIdByFlatID(int FlatID) throws SQLException {
        int coordinatesID;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_FLAT_BY_ID, false);
            preparedStatement.setInt(1, FlatID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                coordinatesID = resultSet.getInt(DatabaseManager.FLAT_TABLE_COORDINATES_ID_COLUMN);
            } else throw new SQLException();
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_FLAT_BY_ID!");
            throw new SQLException(e);
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
        return coordinatesID;
    }

    public Flat insertFlat(Integer key, FlatLite flatLite, User user) throws DatabaseManagerException {
        Flat flatToInsert;
        PreparedStatement insertFlat = null;
        PreparedStatement insertCoordinates = null;
        PreparedStatement insertHouse = null;
        try {
            databaseManager.setCommit();
            databaseManager.setSavepoint();

            LocalDateTime localDateTime = LocalDateTime.now();

            insertHouse = databaseManager.doPreparedStatement(INSERT_HOUSE, true);
            insertHouse.setString(1, flatLite.getHouse().getName());
            insertHouse.setInt(2, flatLite.getHouse().getYear());
            insertHouse.setInt(3, flatLite.getHouse().getNumberOfLifts());
            if (insertHouse.executeUpdate() == 0) throw new SQLException();
            ResultSet resultSetHouse = insertHouse.getGeneratedKeys();
            int houseID;
            if (resultSetHouse.next()) houseID = resultSetHouse.getInt(1);
            else throw new SQLException();

            insertCoordinates = databaseManager.doPreparedStatement(INSERT_COORDINATES, true);
            insertCoordinates.setInt(1, flatLite.getCoordinates().getX());
            insertCoordinates.setFloat(2, flatLite.getCoordinates().getY());
            if (insertCoordinates.executeUpdate() == 0) throw new SQLException();
            ResultSet resultSetCoordinates = insertCoordinates.getGeneratedKeys();
            int coordinatesID;
            if (resultSetCoordinates.next()) coordinatesID = resultSetCoordinates.getInt(1);
            else throw new SQLException();

            insertFlat = databaseManager.doPreparedStatement(INSERT_FLAT, true);
            insertFlat.setInt(1, key);
            insertFlat.setString(2, flatLite.getName());
            insertFlat.setInt(3, coordinatesID);
            insertFlat.setString(4, localDateTime.toString());
            insertFlat.setLong(5, flatLite.getArea());
            insertFlat.setInt(6, flatLite.getNumberOfRooms());
            insertFlat.setInt(7, flatLite.getHeight());
            insertFlat.setBoolean(8, flatLite.getNewness());
            insertFlat.setString(9, flatLite.getTransport().toString());
            insertFlat.setInt(10, houseID);
            insertFlat.setInt(11, databaseUserManager.getUserIdByUsername(user));
            if (insertFlat.executeUpdate() == 0) throw new SQLException();
            ResultSet resultSetFlat = insertFlat.getGeneratedKeys();
            int FlatID;
            if (resultSetFlat.next()) FlatID = resultSetFlat.getInt(1);
            else throw new SQLException();
            flatToInsert = new Flat(
                    FlatID,
                    flatLite.getName(),
                    flatLite.getCoordinates(),
                    localDateTime,
                    flatLite.getArea(),
                    flatLite.getNumberOfRooms(),
                    flatLite.getHeight(),
                    flatLite.getNewness(),
                    flatLite.getTransport(),
                    flatLite.getHouse(),
                    user
            );
            databaseManager.commit();
            return flatToInsert;
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при добавлении нового объекта в БД!");
            exception.printStackTrace();
            databaseManager.rollback();
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(insertHouse);
            databaseManager.closePreparedStatement(insertCoordinates);
            databaseManager.closePreparedStatement(insertFlat);
            databaseManager.setAutoCommit();
        }
    }

    public void updateFlatByID(int FlatID, FlatLite flatLite) throws DatabaseManagerException {
        PreparedStatement updateFlatName = null;
        PreparedStatement updateFlatCoordinates = null;
        PreparedStatement updateFlatArea = null;
        PreparedStatement updateFlatNumberOfRooms = null;
        PreparedStatement updateFlatHeight = null;
        PreparedStatement updateFlatNewness = null;
        PreparedStatement updateFlatTransport = null;
        PreparedStatement updateFlatHouse = null;
        try {
            databaseManager.setCommit();
            databaseManager.setSavepoint();

            updateFlatName = databaseManager.doPreparedStatement(UPDATE_FLAT_NAME_BY_ID, false);
            updateFlatCoordinates = databaseManager.doPreparedStatement(UPDATE_COORDINATES_BY_ID, false);
            updateFlatArea = databaseManager.doPreparedStatement(UPDATE_FLAT_AREA_BY_ID, false);
            updateFlatNumberOfRooms = databaseManager.doPreparedStatement(UPDATE_FLAT_NUMBER_OF_ROOMS_BY_ID, false);
            updateFlatHeight = databaseManager.doPreparedStatement(UPDATE_FLAT_HEIGHT_BY_ID, false);
            updateFlatNewness = databaseManager.doPreparedStatement(UPDATE_FLAT_NEWNESS_BY_ID, false);
            updateFlatTransport = databaseManager.doPreparedStatement(UPDATE_FLAT_TRANSPORT_BY_ID, false);
            updateFlatHouse = databaseManager.doPreparedStatement(UPDATE_HOUSE_BY_ID, false);

            if (flatLite.getName() != null) {
                updateFlatName.setString(1, flatLite.getName());
                updateFlatName.setInt(2, FlatID);
                if (updateFlatName.executeUpdate() == 0) throw new SQLException();
            }
            if (flatLite.getCoordinates() != null) {
                updateFlatCoordinates.setInt(1, flatLite.getCoordinates().getX());
                updateFlatCoordinates.setFloat(2, flatLite.getCoordinates().getY());
                updateFlatCoordinates.setInt(3, getCoordinatesIdByFlatID(FlatID));
                if (updateFlatCoordinates.executeUpdate() == 0) throw new SQLException();
            }
            if (flatLite.getArea() != -1) {
                updateFlatArea.setLong(1, flatLite.getArea());
                updateFlatArea.setInt(2, FlatID);
                if (updateFlatArea.executeUpdate() == 0) throw new SQLException();
            }
            if (flatLite.getNumberOfRooms() != -1) {
                updateFlatNumberOfRooms.setInt(1, flatLite.getNumberOfRooms());
                updateFlatNumberOfRooms.setInt(2, FlatID);
                if (updateFlatNumberOfRooms.executeUpdate() == 0) throw new SQLException();
            }
            if (flatLite.getHeight() != -1) {
                updateFlatHeight.setInt(1, flatLite.getHeight());
                updateFlatHeight.setInt(2, FlatID);
                if (updateFlatHeight.executeUpdate() == 0) throw new SQLException();
            }
            if (flatLite.getNewness() != null) {
                updateFlatNewness.setBoolean(1, flatLite.getNewness());
                updateFlatNewness.setInt(2, FlatID);
                if (updateFlatNewness.executeUpdate() == 0) throw new SQLException();
            }
            if (flatLite.getTransport() != null) {
                updateFlatTransport.setString(1, flatLite.getTransport().toString());
                updateFlatTransport.setInt(2, FlatID);
                if (updateFlatTransport.executeUpdate() == 0) throw new SQLException();
            }
            if (flatLite.getHouse() != null) {
                updateFlatHouse.setString(1, flatLite.getHouse().getName());
                updateFlatHouse.setInt(2, flatLite.getHouse().getYear());
                updateFlatHouse.setInt(3, flatLite.getHouse().getNumberOfLifts());
                updateFlatHouse.setInt(4, getHouseIdByFlatID(FlatID));
                if (updateFlatHouse.executeUpdate() == 0) throw new SQLException();
            }
            databaseManager.commit();
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при выполнении группы запросов на обновление объекта!");
            databaseManager.rollback();
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(updateFlatName);
            databaseManager.closePreparedStatement(updateFlatCoordinates);
            databaseManager.closePreparedStatement(updateFlatArea);
            databaseManager.closePreparedStatement(updateFlatNumberOfRooms);
            databaseManager.closePreparedStatement(updateFlatHeight);
            databaseManager.closePreparedStatement(updateFlatNewness);
            databaseManager.closePreparedStatement(updateFlatTransport);
            databaseManager.closePreparedStatement(updateFlatHouse);
            databaseManager.setAutoCommit();
        }
    }

    public boolean checkFlatByIdAndUserId(int FlatID, User user) throws DatabaseManagerException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_FLAT_BY_ID_AND_USER_ID, false);
            preparedStatement.setInt(1, FlatID);
            preparedStatement.setInt(2, databaseUserManager.getUserIdByUsername(user));
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_FLAT_BY_ID_AND_USER_ID!");
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
    }

    private int getFlatIdByKey(int flatKey) throws SQLException {
        int flatID;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_FLAT_ID_BY_KEY, false);
            preparedStatement.setInt(1, flatKey);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                flatID = resultSet.getInt(DatabaseManager.FLAT_TABLE_ID_COLUMN);
            } else throw new SQLException();
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при выполнении запроса SELECT_FLAT_ID_BY_KEY!");
            throw new SQLException(e);
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
        return flatID;
    }

    public void deleteFlatByKey(int flatKey) throws DatabaseManagerException {
        PreparedStatement deleteFlat = null;
        PreparedStatement deleteCoordinates = null;
        PreparedStatement deleteHouse = null;
        try {
            int flatId = getFlatIdByKey(flatKey);
            int coordinatesID = getCoordinatesIdByFlatID(flatId);
            int houseID = getHouseIdByFlatID(flatId);
            deleteFlat = databaseManager.doPreparedStatement(DELETE_FLAT_BY_KEY, false);
            deleteFlat.setInt(1, flatKey);
            if (deleteFlat.executeUpdate() == 0) throw new SQLException();
            deleteCoordinates = databaseManager.doPreparedStatement(DELETE_COORDINATES_BY_ID, false);
            deleteCoordinates.setInt(1, coordinatesID);
            if (deleteCoordinates.executeUpdate() == 0) throw new SQLException();
            deleteHouse = databaseManager.doPreparedStatement(DELETE_HOUSE_BY_ID, false);
            deleteHouse.setInt(1, houseID);
            if (deleteHouse.executeUpdate() == 0) throw new SQLException();
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при выполнении запроса DELETE_FLAT_BY_ID!");
            exception.printStackTrace();
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(deleteFlat);
            databaseManager.closePreparedStatement(deleteCoordinates);
            databaseManager.closePreparedStatement(deleteHouse);
        }
    }

    public void clearCollection() throws DatabaseManagerException{
        TreeMap<Integer, Flat> flats = getCollection();
        for (Map.Entry<Integer, Flat> flat : flats.entrySet()) {
            deleteFlatByKey(flat.getKey());
        }
    }
}
