package ServerModule.commands;


import ServerModule.util.CollectionManager;
import ServerModule.util.DatabaseCollectionManager;
import ServerModule.util.ResponseOutputer;
import common.data.Flat;
import common.exceptions.*;
import common.util.User;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class RemoveKeyCommand extends Command{
    private CollectionManager collectionManager;
    DatabaseCollectionManager databaseCollectionManager;

    public RemoveKeyCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("remove_key null", "удалить элемент из коллекции по его ключу");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (user == null) throw new NonAuthorizedUserException();
            if (argument.isEmpty() || objectArgument != null) throw new WrongAmountOfArgumentsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            int key = Integer.parseInt(argument);
            Flat o = collectionManager.getFromCollection(key);
            if (o == null) throw new FlatNotFoundException();
            if (!o.getOwner().equals(user)) throw new PermissionDeniedException();
            if (!databaseCollectionManager.checkFlatByIdAndUserId(o.getId(), user)) throw new IllegalDatabaseEditException();
            databaseCollectionManager.deleteFlatByKey(key);
            collectionManager.removeFromCollection(key);
            ResponseOutputer.append("Квартира успешно удалена!\n");
            return true;
        } catch (WrongAmountOfArgumentsException exception) {
            ResponseOutputer.append("Использование: '" + getName() + "'\n");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.append("Коллекция пуста!\n");
        } catch (FlatNotFoundException exception) {
            ResponseOutputer.append("Квартира не найден!\n");
        } catch (DatabaseManagerException e) {
            ResponseOutputer.append("Произошла ошибка при обращении к базе данных!\n");
        } catch (IllegalDatabaseEditException exception) {
            ResponseOutputer.append("Произошло нелегальное изменение объекта в базе данных!\n");
            ResponseOutputer.append("Перезапустите клиент для избежания ошибок!\n");
        } catch (NonAuthorizedUserException e) {
            ResponseOutputer.append("Необходимо авторизоваться!\n");
        } catch (PermissionDeniedException exception) {
            ResponseOutputer.append("Принадлежащие другим пользователям объекты доступны только для чтения!\n");
        }
        return false;
    }
}
