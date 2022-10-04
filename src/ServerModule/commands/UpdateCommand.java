package ServerModule.commands;


import ServerModule.util.DatabaseCollectionManager;
import ServerModule.util.ResponseOutputer;
import common.data.*;
import common.exceptions.*;
import ServerModule.util.CollectionManager;
import common.util.FlatLite;
import common.util.User;

import java.time.LocalDateTime;

/**
 * 'update' command. Updates the information about selected studyGroup.
 */
public class UpdateCommand extends Command {
    private final CollectionManager collectionManager;
    private final DatabaseCollectionManager databaseCollectionManager;

    public UpdateCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("update <ID> {element}", "обновить значение элемента коллекции по ID");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Executes the command.
     * @return Command execute status.
     */
    @Override
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (user == null) throw new NonAuthorizedUserException();

            if (argument.isEmpty() || objectArgument == null) throw new WrongAmountOfArgumentsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();

            int id = Integer.parseInt(argument);
            int key = collectionManager.getKeyById(id);
            Flat oldFlat = collectionManager.getFromCollection(key);
            if (oldFlat == null) throw new FlatNotFoundException();
            if (!oldFlat.getOwner().equals(user)) throw new PermissionDeniedException();
            if (!databaseCollectionManager.checkFlatByIdAndUserId(oldFlat.getId(), user)) throw new IllegalDatabaseEditException();
            FlatLite flatLite = (FlatLite) objectArgument;

            databaseCollectionManager.updateFlatByID(id, flatLite);

            String name = flatLite.getName() == null ? oldFlat.getName() : flatLite.getName();
            Coordinates coordinates = flatLite.getCoordinates() == null ? oldFlat.getCoordinates() : flatLite.getCoordinates();
            LocalDateTime creationDate = oldFlat.getCreationDate();
            Long area = flatLite.getArea() == -1 ? oldFlat.getArea() : flatLite.getArea();
            Integer numberOfRooms = flatLite.getNumberOfRooms() == -1 ? oldFlat.getNumberOfRooms() : flatLite.getNumberOfRooms();
            Integer height = flatLite.getHeight() == -1 ? oldFlat.getHeight() : flatLite.getHeight();
            Boolean newness = flatLite.getNewness() == null ? oldFlat.getNewness() : flatLite.getNewness();
            Transport transport = flatLite.getTransport() == null ? oldFlat.getTransport() : flatLite.getTransport();
            House house = flatLite.getHouse() == null ? oldFlat.getHouse() : flatLite.getHouse();

            collectionManager.removeFromCollection(key);

            collectionManager.addToCollection(key, new Flat(
                    id,
                    name,
                    coordinates,
                    creationDate,
                    area,
                    numberOfRooms,
                    height,
                    newness,
                    transport,
                    house,
                    user
            ));
            ResponseOutputer.append("Квартира успешно изменена!\n");
            return true;
        } catch (WrongAmountOfArgumentsException exception) {
            ResponseOutputer.append("Использование: '" + getName() + "'\n");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.append("Коллекция пуста!\n");
        } catch (NumberFormatException exception) {
            ResponseOutputer.append("ID должен быть представлен числом!\n");
        } catch (FlatNotFoundException exception) {
            ResponseOutputer.append("Квартиры с таким ID в коллекции нет!\n");
        } catch (PermissionDeniedException e) {
            ResponseOutputer.append("Принадлежащие другим пользователям объекты доступны только для чтения!\n");
        } catch (DatabaseManagerException e) {
            ResponseOutputer.append("Произошла ошибка при обращении к базе данных!\n");
        } catch (IllegalDatabaseEditException e) {
            ResponseOutputer.append("Произошло нелегальное изменение объекта в базе данных!\n");
            ResponseOutputer.append("Перезапустите клиент для избежания ошибок!\n");
        } catch (NonAuthorizedUserException e) {
            ResponseOutputer.append("Необходимо авторизоваться!\n");
        }
        return false;
    }
}
