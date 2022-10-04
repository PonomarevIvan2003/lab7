package ServerModule.commands;


import ServerModule.util.CollectionManager;
import ServerModule.util.DatabaseCollectionManager;
import ServerModule.util.ResponseOutputer;
import common.data.Flat;
import common.exceptions.*;
import common.util.User;

import java.util.Map;

public class RemoveGreaterKeyCommand extends Command{
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public RemoveGreaterKeyCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager){
        super("remove_greater_key null", "удалить из коллекции все элементы, ключ которых превышает заданный");
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
            if (argument.isEmpty() || objectArgument != null) throw new WrongAmountOfArgumentsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            int key = Integer.parseInt(argument);
            Map<Integer, Flat> flats = collectionManager.getGreaterKey(key);
            int k = 0;
            for (Map.Entry<Integer, Flat> entry : flats.entrySet()) {
                if (!entry.getValue().getOwner().equals(user)) continue;
                if (!databaseCollectionManager.checkFlatByIdAndUserId(entry.getValue().getId(), user)) throw new IllegalDatabaseEditException();
                databaseCollectionManager.deleteFlatByKey(entry.getKey());
                collectionManager.removeFromCollection(entry.getKey());
                k ++;
            }
            ResponseOutputer.append("Удалено квартир: " + k + "\n");
            return true;
        } catch (WrongAmountOfArgumentsException exception) {
            ResponseOutputer.append("Использование: '" + getName() + "'\n");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.append("Коллекция пуста!\n");
        } catch (DatabaseManagerException e) {
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
