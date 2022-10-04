package ServerModule.util;

import common.util.Request;
import common.util.Response;
import common.util.ResponseCode;
import common.util.User;

import java.util.HashMap;

public class RequestManager {
    interface ExecuteCommand {
        boolean runCommand(String argument, Object objectArgument, User user);
    }

    private CommandManager commandManager;
    private HashMap<String, ExecuteCommand> hashMap = new HashMap<>();

    public RequestManager(CommandManager commandManager) {
        this.commandManager = commandManager;
        hashMap.put("help", commandManager::help);
        hashMap.put("info", commandManager::info);
        hashMap.put("show", commandManager::show);
        hashMap.put("insert", commandManager::insert);
        hashMap.put("update", commandManager::update);
        hashMap.put("remove_key", commandManager::removeKey);
        hashMap.put("clear", commandManager::clear);
        hashMap.put("execute_script", commandManager::executeScript);
        hashMap.put("exit", commandManager::exit);
        hashMap.put("remove_greater", commandManager::removeGreater);
        hashMap.put("remove_lower ", commandManager::removeLower);
        hashMap.put("remove_greater_key", commandManager::removeGreaterKey);
        hashMap.put("filter_by_transport", commandManager::filterByTransport);
        hashMap.put("filter_less_than_new", commandManager::filterLessThanNew);
        hashMap.put("print_field_ascending_height", commandManager::printFieldAscendingHeight);
        hashMap.put("sign_up", commandManager::sign_up);
        hashMap.put("sign_in", commandManager::sign_in);
        hashMap.put("log_out", commandManager::log_out);
    }

    public Response manage(Request request) {
        User hashUser;
        if (request.getUser() == null) {
            hashUser = null;
        } else {
            hashUser = new User(request.getUser().getLogin(), DataHasher.hash(request.getUser().getPassword()));
        }
        ResponseCode responseCode = executeCommand(request.getCommandName(), request.getArgument(), request.getObjectArgument(), hashUser);
        return new Response(responseCode, ResponseOutputer.getAndClear());
    }

    private synchronized ResponseCode executeCommand(String command, String argument, Object objectArgument, User user) {
        if(hashMap.containsKey(command)) {
            if(hashMap.get(command).runCommand(argument, objectArgument, user)) {
                return ResponseCode.OK;
            } else {
                return ResponseCode.ERROR;
            }
        } else {
            ResponseOutputer.append("Команда '" + command + "' не найдена. Наберите 'help' для справки.\n");
            return ResponseCode.ERROR;
        }
    }
}
