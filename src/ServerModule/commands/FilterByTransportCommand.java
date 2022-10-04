package ServerModule.commands;

import ServerModule.util.CollectionManager;
import ServerModule.util.ResponseOutputer;
import common.data.Transport;
import common.exceptions.NonAuthorizedUserException;
import common.exceptions.WrongAmountOfArgumentsException;
import common.util.User;

/**
 * 'exit' command. Closes the program.
 */
public class FilterByTransportCommand extends Command {
    private CollectionManager collectionManager;

    public FilterByTransportCommand(CollectionManager collectionManager) {
        super("filter_by_transport transport", "вывести элементы, значение поля transport которых равно заданному");
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
            Transport transport = (Transport) objectArgument;
            ResponseOutputer.append(collectionManager.filterByTransport(transport));
            return true;
        } catch (WrongAmountOfArgumentsException exception) {
            System.out.println("Использование: '" + getName() + "'\n");
        } catch (NonAuthorizedUserException e) {
            ResponseOutputer.append("Необходимо авторизоваться!\n");
        }
        return false;
    }
}

