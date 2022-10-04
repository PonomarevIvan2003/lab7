package ClientModule.util;

import com.sun.org.apache.xpath.internal.operations.Bool;
import common.data.Coordinates;
import common.data.House;
import common.data.Transport;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.MustBeNotEmptyException;
import common.exceptions.NotInBoundsException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Asks a user a flat's value.
 */
public class Iteractor {
    private final int MAX_NUMBER_OF_ROOMS_LENGTH = 15;
    private final int MIN_NUMBER_OF_ROOMS_LENGTH = 0;
    private final int MIN_AREA = 0;
    private final int MIN_HEIGHT = 0;
    private final int MIN_YEAR = 0;
    private final int MIN_NUMBER_OF_LIFTS = 0;
    private final int MIN_Y = -279;

    private Pattern patternNumber = Pattern.compile("-?\\d+(\\.\\d+)?");

    private Scanner userScanner;
    private boolean fileMode;

    public Iteractor(Scanner userScanner) {
        this.userScanner = userScanner;
        fileMode = false;
    }

    /**
     * Sets a scanner to scan user input.
     *
     * @param userScanner Scanner to set.
     */
    public void setUserScanner(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    /**
     * @return Scanner, which uses for user input.
     */
    public Scanner getUserScanner() {
        return userScanner;
    }

    /**
     * Sets studyGroup asker mode to 'File Mode'.
     */
    public void setFileMode() {
        fileMode = true;
    }

    /**
     * Sets studyGroup asker mode to 'User Mode'.
     */
    public void setUserMode() {
        fileMode = false;
    }


    /**
     * Asks a user the Flat's name.
     *
     * @param inputTitle title of input.
     * @param minLength  min length of string
     * @param maxLength  max length of string
     * @return name
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public String askName(String inputTitle, int minLength, int maxLength) throws IncorrectInputInScriptException {
        String name;
        while (true) {
            try {
                Console.println(inputTitle);
                Console.print(">");
                name = userScanner.nextLine().trim();
                if (fileMode) Console.println(name);
                if (name.equals("")) throw new MustBeNotEmptyException();
                if (name.length() <= minLength) throw new NotInBoundsException();
                if (name.length() >= maxLength) throw new NotInBoundsException();
                break;
            } catch (NoSuchElementException exception) {
                Console.printerror("Имя не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (MustBeNotEmptyException exception) {
                Console.printerror("Имя не может быть пустым!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            } catch (NotInBoundsException e) {
                Console.printerror(String.format("Длина строки не входит в диапазон (%d; %d)", minLength, maxLength));
            }
        }
        return name;
    }

    /**
     * Asks a user the Flat's X coordinate.
     *
     * @return flat's X coordinate.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public int askX() throws IncorrectInputInScriptException {
        String strX = "";
        int x;
        while (true) {
            try {
                Console.println("Введите координату X:");
                Console.print(">");
                strX = userScanner.nextLine().trim();
                if (fileMode) Console.println(strX);
                x = Integer.parseInt(strX);
                break;
            } catch (NoSuchElementException exception) {
                Console.printerror("Координата X не распознана!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                if (patternNumber.matcher(strX).matches())
                    Console.printerror("Координата X должна быть в диапазоне (" + (Integer.MIN_VALUE)
                            + ";" + (Integer.MAX_VALUE) + ")!");
                else
                    Console.printerror("Координата X должна быть представлена числом!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return x;
    }

    /**
     * Asks a user the flat's Y coordinate.
     *
     * @return Flat's Y coordinate.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public float askY() throws IncorrectInputInScriptException {
        String strY = "";
        float y;
        while (true) {
            try {
                Console.println("Введите координату Y:");
                Console.print(">");
                strY = userScanner.nextLine().trim();
                if (fileMode) Console.println(strY);
                y = Float.parseFloat(strY);
                if (y < MIN_Y) throw new NotInBoundsException();
                break;
            } catch (NoSuchElementException exception) {
                Console.printerror("Координата Y не распознана!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                if (patternNumber.matcher(strY).matches())
                    Console.printerror("Координата Y должна быть в диапазоне (" + Float.MIN_VALUE
                            + ";" + Float.MAX_VALUE + ")!");
                else
                    Console.printerror("Координата Y должна быть представлена числом!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            } catch (NotInBoundsException e) {
                Console.printerror("Число должно быть больше " + MIN_Y);
            }
        }
        return y;
    }

    /**
     * Asks a user the flat's coordinates.
     *
     * @return Flat's coordinates.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public Coordinates askCoordinates() throws IncorrectInputInScriptException {
        int x = askX();
        float y = askY();
        return new Coordinates(x, y);
    }

    /**
     * Asks a user the flat's area
     *
     * @return Flat's area
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public Long askArea() throws IncorrectInputInScriptException {
        String strArea = "";
        Long area;
        while (true) {
            try {
                Console.println("Введите площадь квартиры:");
                Console.print(">");
                strArea = userScanner.nextLine().trim();
                if (fileMode) Console.println(strArea);
                area = Long.parseLong(strArea);
                if (area <= MIN_AREA) throw new NotInBoundsException();
                break;
            } catch (NoSuchElementException exception) {
                Console.printerror("Число не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                if (patternNumber.matcher(strArea).matches())
                    Console.printerror("Число должно быть в диапазоне (" + MIN_AREA + ";" + Long.MAX_VALUE + ")!");
                else
                    Console.printerror("Площадь должна быть представлена числом!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            } catch (NotInBoundsException e) {
                Console.printerror("Число должно быть больше " + MIN_AREA);
            }
        }
        return area;
    }

    /**
     * Asks a user the flat's number of rooms
     *
     * @return Flat's number of rooms
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public Integer askNumberOfRooms() throws IncorrectInputInScriptException {
        String strNumberOfRooms = "";
        Integer numberOfRooms;
        while (true) {
            try {
                Console.println("Введите количество комнат:");
                Console.print(">");
                strNumberOfRooms = userScanner.nextLine().trim();
                if (fileMode) Console.println(strNumberOfRooms);
                numberOfRooms = Integer.parseInt(strNumberOfRooms);
                if (numberOfRooms <= MIN_NUMBER_OF_ROOMS_LENGTH) throw new NotInBoundsException();
                break;
            } catch (NoSuchElementException exception) {
                Console.printerror("Число не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                if (patternNumber.matcher(strNumberOfRooms).matches())
                    Console.printerror("Число должно быть в диапазоне (" + MIN_NUMBER_OF_ROOMS_LENGTH + ";" + Integer.MAX_VALUE + ")!");
                else
                    Console.printerror("Количество комнат должно быть представлено числом!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            } catch (NotInBoundsException e) {
                Console.printerror("Число должно быть больше " + MIN_NUMBER_OF_ROOMS_LENGTH);
            }
        }
        return numberOfRooms;
    }

    /**
     * Asks a user the flat's height
     *
     * @return Flat's height
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public Integer askHeight() throws IncorrectInputInScriptException {
        String strHeight = "";
        Integer height;
        while (true) {
            try {
                Console.println("Введите высоту:");
                Console.print(">");
                strHeight = userScanner.nextLine().trim();
                if (fileMode) Console.println(strHeight);
                height = Integer.parseInt(strHeight);
                if (height <= MIN_HEIGHT) throw new NotInBoundsException();
                break;
            } catch (NoSuchElementException exception) {
                Console.printerror("Число не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                if (patternNumber.matcher(strHeight).matches())
                    Console.printerror("Число должно быть в диапазоне (" + MIN_HEIGHT + ";" + Integer.MAX_VALUE + ")!");
                else
                    Console.printerror("Высота должна быть представлена числом!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            } catch (NotInBoundsException e) {
                Console.printerror("Число должно быть больше " + MIN_HEIGHT);
            }
        }
        return height;
    }

    /**
     * Asks a user the flat's newness
     *
     * @return Flat's newness
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public Boolean askNewness() throws IncorrectInputInScriptException {
        String strNewness;
        Boolean newness;
        while (true) {
            try {
                Console.println("Квартира новая?(1 - да, 0 - нет):");
                Console.print(">");
                strNewness = userScanner.nextLine().trim();
                if (fileMode) Console.println(strNewness);

                newness = strNewness.equals("1");
                break;
            } catch (NoSuchElementException exception) {
                Console.printerror("Число не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return newness;
    }

    /**
     * Asks a user the flat's transport
     *
     * @return Flat's transport
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public Transport askTransport() throws IncorrectInputInScriptException {
        String strTransport;
        Transport transport;
        while (true) {
            try {
                Console.println("Список возможных видов транспорта - " + Transport.nameList());
                Console.println("Введите вид транспорта:");
                Console.print(">");
                strTransport = userScanner.nextLine().trim();
                if (fileMode) Console.println(strTransport);
                transport = Transport.valueOf(strTransport.toUpperCase());
                break;
            } catch (NoSuchElementException exception) {
                Console.printerror("вид транспорта не распознан!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalArgumentException exception) {
                Console.printerror("вида транспорта нет в списке!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return transport;
    }


    /**
     * Asks a user the Flat's house
     *
     * @return House
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public House askHouse() throws IncorrectInputInScriptException {
        String name = askHouseName();
        Integer year = askYear();
        Integer numberOfLifts = askNumberOfLifts();
        return new House(name, year, numberOfLifts);
    }

    /**
     * Asks a user the house's name
     *
     * @return House's name
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public String askHouseName() throws IncorrectInputInScriptException {
        return askName("Введите имя дома:", 0, Integer.MAX_VALUE);
    }

    /**
     * Asks a user the flat's name
     *
     * @return Flat's name
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public String askFlatName() throws IncorrectInputInScriptException {
        return askName("Введите имя квартиры:", 0, Integer.MAX_VALUE);
    }

    /**
     * Asks a user the house's year
     *
     * @return House's year
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public Integer askYear() throws IncorrectInputInScriptException {
        String strYear = "";
        Integer year;
        while (true) {
            try {
                Console.println("Введите год постройки дома:");
                Console.print(">");
                strYear = userScanner.nextLine().trim();
                if (fileMode) Console.println(strYear);
                year = Integer.parseInt(strYear);
                if (year <= MIN_YEAR) throw new NotInBoundsException();
                break;
            } catch (NoSuchElementException exception) {
                Console.printerror("год постройки дома не распознан!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                if (patternNumber.matcher(strYear).matches())
                    Console.printerror("год постройки дома должно быть в диапазоне (" + (Integer.MIN_VALUE)
                            + ";" + (Integer.MAX_VALUE) + ")!");
                else
                    Console.printerror("год постройки дома должно быть представлена числом!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            } catch (NotInBoundsException e) {
                Console.printerror("Число должно быть больше " + MIN_YEAR);
            }
        }
        return year;
    }

    /**
     * Asks a user the house's number of lifts
     *
     * @return House's number of lifts
     * @throws IncorrectInputInScriptException if script is running and something goes wrong.
     */
    public Integer askNumberOfLifts() throws IncorrectInputInScriptException {
        String strNumberOfLifts = "";
        Integer numberOfLifts;
        while (true) {
            try {
                Console.println("Введите количество лифтов:");
                Console.print(">");
                strNumberOfLifts = userScanner.nextLine().trim();
                if (fileMode) Console.println(strNumberOfLifts);
                numberOfLifts = Integer.parseInt(strNumberOfLifts);
                if (numberOfLifts <= MIN_NUMBER_OF_LIFTS) throw new NotInBoundsException();
                break;
            } catch (NoSuchElementException exception) {
                Console.printerror("количество лифтов не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                if (patternNumber.matcher(strNumberOfLifts).matches())
                    Console.printerror("количество лифтов должно быть в диапазоне (" + (Integer.MIN_VALUE)
                            + ";" + (Integer.MAX_VALUE) + ")!");
                else
                    Console.printerror("количество лифтов должно быть представлена числом!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            } catch (NotInBoundsException e) {
                Console.printerror("Число должно быть больше " + MIN_NUMBER_OF_LIFTS);
            }
        }
        return numberOfLifts;
    }

    /**
     * Asks a user a question.
     *
     * @param question A question.
     * @return Answer (true/false).
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public boolean askQuestion(String question) throws IncorrectInputInScriptException {
        String finalQuestion = question + " (+/-):";
        String answer;
        while (true) {
            try {
                Console.println(finalQuestion);
                Console.print(">");
                answer = userScanner.nextLine().trim();
                if (fileMode) Console.println(answer);
                if (!answer.equals("+") && !answer.equals("-")) throw new NotInBoundsException();
                break;
            } catch (NoSuchElementException exception) {
                Console.printerror("Ответ не распознан!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NotInBoundsException exception) {
                Console.printerror("Ответ должен быть представлен знаками '+' или '-'!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Console.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return answer.equals("+");
    }
}
