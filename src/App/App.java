package App;

import App.Cards.Card;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;


public class App {

    static private final char EscapeCharacter = '\\';

    public static void main(String[] args) {

        System.out.print("Love Letter Premium Edition by Seiji Kanai.\n");
        System.out.print("Java implementation by Magnus Zoeschinger.\n");
        System.out.print("**** - * - ****\n\n");

        GameInstance gameInstance = null;

        while (true) {
            try {
                gameInstance = new GameInstance();
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.print("The Game Instance quit unexpectedly. Do you want to try again? (Y/n): ");
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                if (input.equals("n")) {
                    System.out.print("\n\nThank you for playing!\n\n");
                    return;
                }

                continue;
            }
        }

        gameInstance = null;
        System.out.print("\nThank you for playing!\n");
        return;
    }

    public static void ClearScreen(Scanner scanner) {
        System.out.print(
                  "*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n"
                + "*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n"
                + "*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n"
                + "**** - ****\n"
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

    public static void PrintArrayListPC(ArrayList<PlayerController> list, String sep, String end) {
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%s", list.get(i).GetPlayerName());
            if (i != list.size() - 1)
                System.out.printf("%s", sep);

            continue;
        }
        System.out.printf("%s\n", end);

        return;
    }

    public static void PrintArrayListCard(ArrayList<Card> list, String sep) {
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("(%d) %s", i + 1, list.get(i).GetName());
            if (i != list.size() - 1)
                System.out.printf("%s", sep);

            continue;
        }

        return;
    }

    public static String WaitForCommand(Scanner scanner, String[] validCommands) {
        while (true) {
            System.out.print("\nWhat do you want to do?: ");
            String input = scanner.nextLine();
            String command = "";

            try {
                if (input.charAt(0) != App.EscapeCharacter) {
                    System.out.printf(
                            "This is not a command. Please enter a command starting with %c.\n", App.EscapeCharacter);
                    System.out.printf(
                            "For more information on commands, enter %chelp.\n", App.EscapeCharacter);

                    continue;
                }

                command = input.substring(1);
                for (String validCommand : validCommands)
                    if (command.equals(validCommand))
                        return command;

                throw new IndexOutOfBoundsException();
            }

            catch (IndexOutOfBoundsException e) {
                if (command.isEmpty())
                    continue;
                System.out.printf("Command \"%s\" not recognized.\n", command);
                continue;
            }
        }
    }

    public static String WaitForInput(Scanner scanner, String[] validInputs) {
        while (true) {
            System.out.printf("Valid inputs: %s\n", String.join(", ", validInputs));
            System.out.print("Enter your choice: ");
            String input = scanner.nextLine();

            if (input.isEmpty())
                continue;

            for (String validInput : validInputs)
                if (input.equals(validInput))
                    return input;

            System.out.printf("Input \"%s\" not recognized. Retrying.\n", input);
            continue;
        }
    }

    public static int WaitForInputInteger(Scanner scanner, int min, int max) {
        while (true) {
            System.out.printf("Enter your choice (%d-%d): ", min, max);
            String input = scanner.nextLine();

            if (input.isEmpty())
                continue;

            try {
                int choice = Integer.parseInt(input);
                if (choice < min || choice > max)
                    throw new NumberFormatException();

                return choice;
            }

            catch (NumberFormatException e) {
                System.out.printf("Input \"%s\" not recognized. Retrying.\n", input);
                continue;
            }
        }
    }
}
