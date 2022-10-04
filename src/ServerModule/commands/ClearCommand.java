package ServerModule.commands;

import ServerModule.util.CollectionManager;
import ServerModule.util.DatabaseCollectionManager;
import ServerModule.util.ResponseOutputer;
import common.data.Flat;
import common.exceptions.*;
import common.util.User;

import java.util.Map;

/**
 * Класс, описывающий команду clear
 */
public class ClearCommand extends Command{
    private final CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public ClearCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("clear", "очистить коллекцию");
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
            if (!argument.isEmpty() || objectArgument != null) throw new WrongAmountOfArgumentsException();
            for (Map.Entry<Integer, Flat> entry : collectionManager.getCollection().entrySet()) {
                if (!entry.getValue().getOwner().equals(user)) throw new PermissionDeniedException();
                if (!databaseCollectionManager.checkFlatByIdAndUserId(entry.getValue().getId(), user)) throw new IllegalDatabaseEditException();
            }
            databaseCollectionManager.clearCollection();
            collectionManager.clearCollection();
            ResponseOutputer.append("Коллекция очищена!\n");
            return true;
        } catch (WrongAmountOfArgumentsException exception) {
            ResponseOutputer.append("Использование: '" + getName() + "'\n");
        } catch (PermissionDeniedException exception) {
            ResponseOutputer.append("Принадлежащие другим пользователям объекты доступны только для чтения!\n");
        } catch (DatabaseManagerException exception) {
            ResponseOutputer.append("Произошла ошибка при обращении к базе данных!\n");
        } catch (IllegalDatabaseEditException exception) {
            ResponseOutputer.append("Произошло нелегальное изменение объекта в базе данных!\n");
            ResponseOutputer.append("Перезапустите клиент для избежания ошибок!\n");
        } catch (NonAuthorizedUserException e) {
            ResponseOutputer.append("Необходимо авторизоваться!\n");
        }
        return false;
    }
}
