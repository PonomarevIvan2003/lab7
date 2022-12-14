package ServerModule.commands;

import ServerModule.util.ResponseOutputer;
import common.exceptions.NonAuthorizedUserException;
import common.exceptions.WrongAmountOfArgumentsException;
import common.util.User;

/**
 * 'exit' command. Closes the program.
 */
public class ExitCommand extends Command {

    public ExitCommand() {
        super("exit", "завершает работу клиентского приложения");
    }

    /**
     * Executes the command.
     * @return Command execute status.
     */
    @Override
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (!argument.isEmpty() || objectArgument != null) throw new WrongAmountOfArgumentsException();
            return true;
        } catch (WrongAmountOfArgumentsException exception) {
            System.out.println("Использование: '" + getName() + "'\n");
        }
        return false;
    }
}

