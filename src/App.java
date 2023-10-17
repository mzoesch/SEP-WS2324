import java.util.Scanner;
import java.io.IOException;


public class App {

    static final char EscapeCharacter = '\\';

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

    static void ClearScreen(Scanner scanner) {
        System.out.print("*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n*\n**** - ****\n");

        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        return;
    }

    static int WaitForCommand(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine();
            try {
                if (input.charAt(0) != App.EscapeCharacter) {
                    System.out.printf(
                            "This is not a command. Please enter a command starting with %c.\n", App.EscapeCharacter);
                    System.out.printf(
                            "For more information on commands, enter %chelp?.\n", App.EscapeCharacter);

                    continue;
                }

                System.out.printf("Command recognized: %s.\n", input);
                return 0;
            }

            catch (IndexOutOfBoundsException e) {
                System.out.printf("Command %s not recognized.\n", input);
            }

            continue;
        }
    }
}
