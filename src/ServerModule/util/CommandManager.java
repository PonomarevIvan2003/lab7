package ServerModule.util;

import ServerModule.commands.Command;
import common.util.User;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private final List<Command> commands = new ArrayList<>();
    private final Command helpCommand;
    private final Command infoCommand;
    private final Command showCommand;
    private final Command insertCommand;
    private final Command updateCommand;
    private final Command removeKeyCommand;
    private final Command clearCommand;
    private final Command executeScriptCommand;
    private final Command exitCommand;
    private final Command removeGreaterCommand;
    private final Command removeLowerCommand;
    private final Command removeGreaterKeyCommand;
    private final Command filterByTransportCommand;
    private final Command filterLessThanNewCommand;
    private final Command printFieldAscendingHeightCommand;
    private final Command signUpCommand;
    private final Command signInCommand;
    private final Command logOutCommand;

    public CommandManager(Command helpCommand, Command infoCommand, Command showCommand, Command insertCommand, Command updateCommand, Command removeKeyCommand, Command clearCommand, Command executeScriptCommand, Command exitCommand, Command removeGreaterCommand, Command removeLowerCommand, Command removeGreaterKeyCommand, Command filterByTransportCommand, Command filterLessThanNewCommand, Command printFieldAscendingHeightCommand, Command signUpCommand, Command signInCommand, Command logOutCommand) {
        this.helpCommand = helpCommand;
        this.infoCommand = infoCommand;
        this.showCommand = showCommand;
        this.insertCommand = insertCommand;
        this.updateCommand = updateCommand;
        this.removeKeyCommand = removeKeyCommand;
        this.clearCommand = clearCommand;
        this.executeScriptCommand = executeScriptCommand;
        this.exitCommand = exitCommand;
        this.removeGreaterCommand = removeGreaterCommand;
        this.removeLowerCommand = removeLowerCommand;
        this.removeGreaterKeyCommand = removeGreaterKeyCommand;
        this.filterByTransportCommand = filterByTransportCommand;
        this.filterLessThanNewCommand = filterLessThanNewCommand;
        this.printFieldAscendingHeightCommand = printFieldAscendingHeightCommand;
        this.signUpCommand = signUpCommand;
        this.signInCommand = signInCommand;
        this.logOutCommand = logOutCommand;

        commands.add(infoCommand);
        commands.add(showCommand);
        commands.add(insertCommand);
        commands.add(updateCommand);
        commands.add(removeKeyCommand);
        commands.add(clearCommand);
        commands.add(exitCommand);
        commands.add(executeScriptCommand);
        commands.add(removeGreaterCommand);
        commands.add(removeLowerCommand);
        commands.add(removeGreaterKeyCommand);
        commands.add(filterByTransportCommand);
        commands.add(filterLessThanNewCommand);
        commands.add(printFieldAscendingHeightCommand);
        commands.add(logOutCommand);
        commands.add(new Command("log_in", "авторизоваться") {
            @Override
            public boolean execute(String argument, Object objectArgument, User user) {
                return false;
            }
        });
    }

    /**
     * @return List of manager's com.serverModule.commands.
     */
    public List<Command> getCommands() {
        return commands;
    }

    /**
     * Prints info about the all com.serverModule.commands.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean help(String argument, Object objectArgument, User user) {
        if (helpCommand.execute(argument, objectArgument, user)) {
            for (Command command : commands) {
                ResponseOutputer.appendTable(command.getName(), command.getDescription());
            }
            return true;
        } else return false;
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean info(String argument, Object objectArgument, User user) {
        return infoCommand.execute(argument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean show(String argument, Object objectArgument, User user) {
        return showCommand.execute(argument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean insert(String argument, Object objectArgument, User user) {
        return insertCommand.execute(argument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean update(String argument, Object objectArgument, User user) {
        return updateCommand.execute(argument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean removeKey(String argument, Object objectArgument, User user) {
        return removeKeyCommand.execute(argument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean clear(String argument, Object objectArgument, User user) {
        return clearCommand.execute(argument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean exit(String argument, Object objectArgument, User user) {
        return exitCommand.execute(argument,objectArgument, user);
    }

    public boolean sign_up(String argument, Object objectArgument, User user) {
        return signUpCommand.execute(argument, objectArgument, user);
    }

    public boolean sign_in(String argument, Object objectArgument, User user) {
        return signInCommand.execute(argument, objectArgument, user);
    }

    public boolean log_out(String argument, Object objectArgument, User user) {
        return logOutCommand.execute(argument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean executeScript(String argument, Object objectArgument, User user) {
        return executeScriptCommand.execute(argument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean removeGreater(String argument, Object objectArgument, User user) {
        return removeGreaterCommand.execute(argument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean removeLower(String argument, Object objectArgument, User user) {
        return removeLowerCommand.execute(argument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean removeGreaterKey(String argument, Object objectArgument, User user){
        return removeGreaterKeyCommand.execute(argument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean filterByTransport(String argument, Object objectArgument, User user){
        return filterByTransportCommand.execute(argument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean filterLessThanNew(String argument, Object objectArgument, User user){
        return filterLessThanNewCommand.execute(argument, objectArgument, user);
    }

    /**
     * Executes needed command.
     * @param argument Its argument.
     * @return Command exit status.
     */
    public boolean printFieldAscendingHeight(String argument, Object objectArgument, User user){
        return printFieldAscendingHeightCommand.execute(argument, objectArgument, user);
    }

}
