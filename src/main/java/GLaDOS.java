import java.util.Scanner;

// Controls the banner, entry/exit messages, and command loop of ChatBot

public class GLaDOS {
    public static void main(String[] args) {
        String banner = "        ________          ____  ____  _____\n"
                + "       / ____/ /   ____ _/ __ \\/ __ \\/ ___/\n"
                + "      / / __/ /   / __ `/ / / / / / /\\__ \\ \n"
                + "     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / \n"
                + "     \\____/_____/\\__,_/_____/\\____//____/  \n";
        String indent = "     ";
        String line = "    ____________________________________________________________";

        System.out.println(line);
        System.out.println(banner);
        System.out.println(indent + "Hello, I'm GLaDOS nice to... Oh, it's you.");
        System.out.println(indent + "State your query. I have other tests to run.");
        System.out.println(line);

        // Echo each command back until the user types "bye".
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();

        while (!input.equals("bye")) {
            System.out.println(line);
            System.out.println(indent + input);
            System.out.println(line);
            input = in.nextLine();
        }

        System.out.println(line);
        System.out.println(indent + "Test concluded. Try not to disappoint me next time.");
        System.out.println(line);
    }
}
