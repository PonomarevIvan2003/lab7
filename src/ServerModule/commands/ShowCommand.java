package ServerModule.commands;

import ServerModule.util.ResponseOutputer;
import ServerModule.util.CollectionManager;
import common.exceptions.NonAuthorizedUserException;
import common.exceptions.WrongAmountOfArgumentsException;
import common.util.User;

/**
 * 'show' command. Shows information about all elements of the collection.
 */
public class ShowCommand extends Command {
    private CollectionManager collectionManager;

    public ShowCommand(CollectionManager collectionManager) {
        super("show", "вывести все элементы коллекции");
        this.collectionManager = collectionManager;
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
            ResponseOutputer.append(collectionManager.toString());
            return true;
        } catch (WrongAmountOfArgumentsException exception) {
            ResponseOutputer.append("Использование: '" + getName() + "'\n");
        } catch (NonAuthorizedUserException e) {
            ResponseOutputer.append("Необходимо авторизоваться!\n");
        }
        return false;
    }
}
