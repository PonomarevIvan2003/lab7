package ServerModule;

import ServerModule.commands.*;
import ServerModule.util.*;

import java.io.IOException;

public class MainServer {
    public static final int PORT = 8778;

    public static void main(String[] args) throws IOException {
        DatabaseManager databaseManager = new DatabaseManager();
        DatabaseUserManager databaseUserManager = new DatabaseUserManager(databaseManager);
        DatabaseCollectionManager databaseCollectionManager = new DatabaseCollectionManager(databaseManager, databaseUserManager);
        CollectionManager collectionManager = new CollectionManager(databaseCollectionManager);
        CommandManager commandManager = new CommandManager(new HelpCommand(),
                new InfoCommand(collectionManager),
                new ShowCommand(collectionManager),
                new InsertCommand(collectionManager, databaseCollectionManager),
                new UpdateCommand(collectionManager, databaseCollectionManager),
                new RemoveKeyCommand(collectionManager, databaseCollectionManager),
                new ClearCommand(collectionManager, databaseCollectionManager),
                new ExecuteScriptCommand(),
                new ExitCommand(),
                new RemoveGreaterCommand(collectionManager, databaseCollectionManager),
                new RemoveLowerCommand(collectionManager, databaseCollectionManager),
                new RemoveGreaterKeyCommand(collectionManager, databaseCollectionManager),
                new FilterByTransportCommand(collectionManager),
                new FilterLessThanNewnessCommand(collectionManager),
                new PrintFieldAscendingHeightCommand(collectionManager),
                new SignUpCommand(databaseUserManager),
                new SignInCommand(databaseUserManager),
                new LogOutCommand(databaseUserManager));
        RequestManager requestManager = new RequestManager(commandManager);
        Server server = new Server(PORT, requestManager);
        server.run();
        databaseManager.closeConnection();
    }
}
