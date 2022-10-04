package ClientModule;

import ClientModule.util.AuthManager;
import ClientModule.util.Console;
import common.exceptions.IncorrectInputInScriptException;

import java.io.IOException;
import java.util.Scanner;

public class MainClient {
    private static String host = "localhost";
    private static int port = 8778;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthManager authManager = new AuthManager(scanner);
        Console console = new Console(scanner, authManager);
        Client client = new Client(host, port, console, authManager);
        client.run();
        scanner.close();
    }
}
