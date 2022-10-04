package ServerModule.commands;

import ServerModule.util.CollectionManager;
import ServerModule.util.DatabaseCollectionManager;
import ServerModule.util.ResponseOutputer;
import common.data.Flat;
import common.exceptions.DatabaseManagerException;
import common.exceptions.NonAuthorizedUserException;
import common.exceptions.WrongAmountOfArgumentsException;
import common.util.FlatLite;
import common.util.User;
import org.w3c.dom.CDATASection;

import java.time.LocalDateTime;

/**
 * 'info' command. Prints information about the collection.
 */
public class InsertCommand extends Command {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public InsertCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("insert null {element}", "добавить новый элемент с заданным ключом");
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
            if (argument.isEmpty() || objectArgument == null) throw new WrongAmountOfArgumentsException();
            int key = Integer.parseInt(argument);
            FlatLite flatLite = (FlatLite) objectArgument;
            collectionManager.addToCollection(key, databaseCollectionManager.insertFlat(key, flatLite, user));
            ResponseOutputer.append("Успешно добавлено в коллекцию!\n");
            return true;
        } catch (WrongAmountOfArgumentsException exception) {
            ResponseOutputer.append("Использование: '" + getName() + "'\n");
        } catch (DatabaseManagerException e) {
            ResponseOutputer.append("Произошла ошибка при обращении к базе данных!\n");
        } catch (NonAuthorizedUserException e) {
            ResponseOutputer.append("Необходимо авторизоваться!\n");
        }
        return false;
    }
}