package app;

import java.io.BufferedReader;
import java.nio.file.Path;
import java.util.Scanner;
import java.io.IOException;


public class App {

    public static void main(String[] args) {

        System.out.print("\n******************** - * - ********************\n");
        System.out.print("Love Letter Premium Edition by Seiji Kanai.\n");
        System.out.print("Java implementation by Magnus Zoeschinger.\n");
        System.out.print("******************** - * - ********************\n\n");

        while (true) {
            try {
                GameInstance.runGame();
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.print("The Game Instance quit unexpectedly. Do you want to restart the game? (y/N): ");
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                if (input.equals("y") || input.equals("Y"))
                    continue;

                scanner.close();
                break;
            }
        }

        System.out.print("\nThank you for playing!\n");
        return;
    }

    /**
     * This will not work in an IDE out-console.
     * Therefore, we print starts.
     */
    public static void clearStdOut() {
        System.out.print(
"""
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
********************* - * - ********************
"""
        );

        // Works only in CMD.
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        // Works only in ANSI compatible terminals.
        System.out.print("\033[H\033[2J");
        System.out.flush();

        return;
    }

    public static void printArray_V2(String[] arr, String sep, String end) {
        for (int i = 0; i < arr.length; i++) {
            System.out.printf("%s", arr[i]);
            if (i != arr.length - 1)
                System.out.printf("%s", sep);

            continue;
        }
        System.out.printf("%s\n", end);

        return;
    }

    public static String waitForCommand_V2(Scanner scanner, Command[] validCommands) {
        while (true) {
            System.out.print("\nWhat do you want to do?: ");
            String input = scanner.nextLine();
            String command = "";

            try {
                if (input.charAt(0) != Command.EscapeCharacter) {
                    System.out.printf(
                            "This is not a command. Please enter a command starting with %c.\n", Command.EscapeCharacter);
                    System.out.printf(
                            "For more information on commands, enter %chelp.\n", Command.EscapeCharacter);

                    continue;
                }

                command = input.substring(1);
                for (Command validCommand : validCommands)
                    if (validCommand.isEqual(command))
                        return command;

                throw new IllegalArgumentException();
            }

            catch (IndexOutOfBoundsException e) {
                if (command.isEmpty())
                    continue;
                System.out.printf("Command \"%s\" not recognized.\n", command);
                continue;
            }

            catch (IllegalArgumentException e) {
                if (command.isEmpty())
                    continue;
                System.out.printf("Command \"%s\" is not valid.\n", command);
                continue;
            }
        }
    }

    public static int waitForInputInteger_V2(Scanner scanner, int min, int max, String prompt) {
        while (true) {
            if (prompt == null)
                System.out.printf("Enter your choice (%d-%d): ", min, max);
            else
                System.out.printf("%s (%d-%d): ", prompt, min, max);

            String input = scanner.nextLine();

            if (input.isEmpty())
                continue;

            try {
                Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.printf("Input \"%s\" not recognized. Retrying.\n\n", input);
                continue;
            }

            int choice = Integer.parseInt(input);
            if (choice < min || choice > max) {
                System.out.printf("Input \"%s\" is out of bounds. Retrying.\n\n", input);
                continue;
            }

            return choice;
        }
    }

    public static String waitForInputString_V2(Scanner scanner, int minLength, String prompt) {
        while (true) {
            if (prompt == null)
                System.out.printf("Enter your choice (min. %d characters): ", minLength);
            else
                System.out.printf("%s (min. %d characters): ", prompt, minLength);

            String input = scanner.nextLine();
            if (input.length() < minLength) {
                System.out.printf("Input \"%s\" is too short. Retrying.\n\n", input);
                continue;
            }

            return input;
        }
    }

    public static String waitForInputStringWithValidation_V2(Scanner scanner, String[] validInputs, String prompt) {
        while (true) {
            if (prompt == null)
                System.out.printf("Make a choice (%s): ", String.join(", ", validInputs));
            else
                System.out.printf("%s (%s): ", prompt, String.join(", ", validInputs));

            String input = scanner.nextLine();
            if (input.isEmpty())
                continue;

            for (String validInput : validInputs)
                if (input.equals(validInput))
                    return input;

            System.out.printf("Input \"%s\" not recognized. Retrying.\n\n", input);
            continue;
        }
    }

    public static String readFile(String path) throws IOException {
        // We need this to get to the root directory of the project.
        // IntelliJ and manual execution have different working directories.
        Path rootDir;
        Path currentAbsolutePath = Path.of("").toAbsolutePath();
        if (currentAbsolutePath.toString().endsWith("SEP-WS2324"))
            rootDir = currentAbsolutePath;
        else
            rootDir = currentAbsolutePath.getParent();

        Path pathToFile = Path.of(rootDir.toString(), path);
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(pathToFile.toString()))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();

                continue;
            }

            return sb.toString();
        }
    }
}
