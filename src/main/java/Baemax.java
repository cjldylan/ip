/**
 * The Baemax chatbot entry point.
 */
import java.util.Scanner;
public class Baemax {
    public static void main(String[] args) {
        String banner = "╔════════════════╗\n"
                + "║     Baemax     ║\n"
                + "╚════════════════╝\n";
        System.out.println(banner);

        System.out.println("Hello, I am Baemax!");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();

            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            }
            System.out.println(command);
        }
        scanner.close();
        System.out.println("What can I do for you?");
        System.out.println("Bye. Hope to see you again soon.");
    }
}
