package ClientModule.util;

import common.data.Coordinates;
import common.data.House;
import common.data.Transport;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.ScriptRecursionException;
import common.exceptions.WrongAmountOfParametersException;
import common.util.FlatLite;
import common.util.Request;
import common.util.ResponseCode;
import common.util.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

public class Console {
    private Scanner userScanner;
    private Stack<File> scriptFileNames = new Stack<>();
    private Stack<Scanner> scannerStack = new Stack<>();
    private AuthManager authManager;

    public Console(Scanner userScanner, AuthManager authManager) {
        this.userScanner = userScanner;
        this.authManager = authManager;
    }

    private boolean fileMode() {
        return !scannerStack.isEmpty();
    }

    /**
     * Mode for catching ServerModule.commands from user input.
     */
    public Request interactiveMode(ResponseCode serverResponseCode, User user) throws IncorrectInputInScriptException {
        String userInput = "";
        String[] userCommand = {"", ""};
        ProcessCode processCode = null;
        try {
            do {
                try {
                    if (fileMode() && (serverResponseCode == ResponseCode.SERVER_EXIT || serverResponseCode == ResponseCode.ERROR)) {
                        throw new IncorrectInputInScriptException();
                    }
                    while (fileMode() && !userScanner.hasNextLine()) {
                        userScanner.close();
                        userScanner = scannerStack.pop();
                        System.out.println("Возвращаюсь из скрипта '" + scriptFileNames.pop().getName() + "'!");
                    }
                    if (fileMode()) {
                        userInput = userScanner.nextLine();
                        if (!userInput.isEmpty()) {
                            System.out.print("$ ");
                            System.out.println(userInput);
                        }
                    } else {
                        System.out.print("$ ");
                        if (userScanner.hasNext()) {
                            userInput = userScanner.nextLine();
                        } else {
                            System.out.println("Клиент завершен!");
                            System.exit(0);
                        }
                    }
                    userCommand = (userInput.trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                } catch (NoSuchElementException | IllegalStateException exception) {
                    System.out.println("Произошла ошибка при вводе команды!");
                    userCommand = new String[]{"", ""};
                }
                processCode = checkCommand(userCommand[0], userCommand[1]);
            } while (processCode == ProcessCode.ERROR && !fileMode() || userCommand[0].isEmpty());
            try {
                switch (processCode) {
                    case OBJECT:
                        FlatLite flatToInsert = generateFlatToInsert();
                        return new Request(userCommand[0], userCommand[1], flatToInsert, user);
                    case UPDATE_OBJECT:
                        FlatLite flatToUpdate = generateFlatToUpdate();
                        return new Request(userCommand[0], userCommand[1], flatToUpdate, user);
                    case SCRIPT:
                        File scriptFile = new File(userCommand[1]);
                        if (!scriptFile.exists()) throw new FileNotFoundException();
                        if (!scriptFileNames.isEmpty() && scriptFileNames.search(scriptFile) != -1) {
                            throw new ScriptRecursionException();
                        }
                        scannerStack.push(userScanner);
                        scriptFileNames.push(scriptFile);
                        userScanner = new Scanner(scriptFile);
                        System.out.println("Выполняю скрипт '" + scriptFile.getName() + "'!");
                        break;
                    case PARAMETER_TRANSPORT:
                        Transport transport = generateTransport();
                        return new Request(userCommand[0], userCommand[1], transport, user);
                    case PARAMETER_NEWNESS:
                        Boolean newness = generateNewness();
                        return new Request(userCommand[0], userCommand[1], newness, user);
                    case LOG_IN:
                        return authManager.handle();
                }
            } catch (FileNotFoundException exception) {
                System.out.println("Файл со скриптом не найден!");
            } catch (ScriptRecursionException exception) {
                System.out.println("Скрипты не могут вызываться рекурсивно!");
                scriptFileNames.pop();
                throw new IncorrectInputInScriptException();
            }
        } catch (IncorrectInputInScriptException exception) {
            System.out.println("Выполнение скрипта прервано!");
            while (!scannerStack.isEmpty()) {
                userScanner.close();
                userScanner = scannerStack.pop();
            }
        }
        return new Request(userCommand[0], userCommand[1], null, user);
    }

    /**
     * Launches the command.
     * @param command Command to launch.
     * @return Exit code.
     */
    private ProcessCode checkCommand(String command, String argument) {
        try {
            switch (command) {
                case "":
                    return ProcessCode.ERROR;
                case "help":
                case "info":
                case "show":
                case "clear":
                case "print_field_ascending_height":
                case "exit":
                case "log_out":
                    if (!argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.OK;
                case "log_in":
                    if (!argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.LOG_IN;
                case "insert":
                    if (argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.OBJECT;
                case "update":
                    if (argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.UPDATE_OBJECT;
                case "remove_key":
                case "remove_greater_key":
                    if (argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.OK;
                case "execute_script":
                    if (argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.SCRIPT;
                case "remove_greater":
                case "remove_lower":
                    if (!argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.OBJECT;
                case "filter_by_transport":
                    if (!argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.PARAMETER_TRANSPORT;
                case "filter_less_than_new":
                    if (!argument.isEmpty()) throw new WrongAmountOfParametersException();
                    return ProcessCode.PARAMETER_NEWNESS;
                case "save":
                    System.out.println("Эта команда недоступна клиентам!");
                    return ProcessCode.ERROR;
                default:
                    System.out.println("Команда '" + command + "' не найдена. Наберите 'help' для справки.");
                    return ProcessCode.ERROR;
            }
        } catch (WrongAmountOfParametersException e) {
            System.out.println("Проверьте правильность ввода аргументов!");
        }
        return ProcessCode.OK;
    }

    private FlatLite generateFlatToInsert() throws IncorrectInputInScriptException {
        Iteractor builder = new Iteractor(userScanner);
        if (fileMode()) {
            builder.setFileMode();
        } else {
            builder.setUserMode();
        }
        return new FlatLite(
                builder.askFlatName(),
                builder.askCoordinates(),
                LocalDateTime.now(),
                builder.askArea(),
                builder.askNumberOfRooms(),
                builder.askHeight(),
                builder.askNewness(),
                builder.askTransport(),
                builder.askHouse()
        );
    }

    private FlatLite generateFlatToUpdate() throws IncorrectInputInScriptException{
        Iteractor builder = new Iteractor(userScanner);
        if (fileMode()) {
            builder.setFileMode();
        } else {
            builder.setUserMode();
        }
        String name = builder.askQuestion("Хотите изменить имя квартиры?") ?
                builder.askFlatName() : null;
        Coordinates coordinates = builder.askQuestion("Хотите изменить координаты квартиры?") ?
                builder.askCoordinates() : null;
        Long area = builder.askQuestion("Хотите изменить площадь?") ?
                builder.askArea() : -1;
        Integer numberOfRooms  = builder.askQuestion("Хотите изменить количество комнат?") ?
                builder.askNumberOfRooms() : -1;
        Integer height  = builder.askQuestion("Хотите изменить высоту потолков?") ?
                builder.askHeight() : -1;
        Boolean newness = builder.askQuestion("Хотите изменить новизну?") ?
                builder.askNewness() : null;
        Transport transport  = builder.askQuestion("Хотите изменить тип транспорта?") ?
                builder.askTransport() : null;
        House house  = builder.askQuestion("Хотите изменить дом?") ?
                builder.askHouse() : null;
        return new FlatLite(
                name,
                coordinates,
                LocalDateTime.now(),
                area,
                numberOfRooms,
                height,
                newness,
                transport,
                house
        );
    }

    private Transport generateTransport() throws IncorrectInputInScriptException {
        Iteractor builder = new Iteractor(userScanner);
        if (fileMode()) {
            builder.setFileMode();
        } else {
            builder.setUserMode();
        }
        return builder.askTransport();
    }

    private Boolean generateNewness() throws IncorrectInputInScriptException {
        Iteractor builder = new Iteractor(userScanner);
        if (fileMode()) {
            builder.setFileMode();
        } else {
            builder.setUserMode();
        }
        return builder.askNewness();
    }


    /**
     * Prints toOut.toString() to Console
     * @param toOut Object to print
     */
    public static void print(Object toOut) {
        System.out.print(toOut);
    }

    /**
     * Prints toOut.toString() + \n to Console
     * @param toOut Object to print
     */
    public static void println(Object toOut) {
        System.out.println(toOut);
    }

    /**
     * Prints error: toOut.toString() to Console
     * @param toOut Error to print
     */
    public static void printerror(Object toOut) {
        System.out.println("error: " + toOut);
    }

}
