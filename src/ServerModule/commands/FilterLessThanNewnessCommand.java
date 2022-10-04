package ServerModule.commands;

import ServerModule.util.CollectionManager;
import ServerModule.util.ResponseOutputer;
import common.exceptions.NonAuthorizedUserException;
import common.exceptions.WrongAmountOfArgumentsException;
import common.util.User;

/**
 * 'exit' command. Closes the program.
 */
public class FilterLessThanNewnessCommand extends Command {
    private CollectionManager collectionManager;

    public FilterLessThanNewnessCommand(CollectionManager collectionManager) {
        super("filter_less_than_new new", "вывести элементы, значение поля new которых меньше заданного");
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
            if (!argument.isEmpty() || objectArgument == null) throw new WrongAmountOfArgumentsException();
            Boolean newness = (Boolean) objectArgument;
            ResponseOutputer.append(collectionManager.filterLessThanNew(newness));
            return true;
        } catch (WrongAmountOfArgumentsException exception) {
            System.out.println("Использование: '" + getName() + "'\n");
        } catch (NonAuthorizedUserException e) {
            ResponseOutputer.append("Необходимо авторизоваться!\n");
        }
        return false;
    }
}

