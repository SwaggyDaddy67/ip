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

        // Holds the tasks entered
        Task[] tasks = new Task[100];
        int taskCount = 0;

        Scanner in = new Scanner(System.in);
        String input = in.nextLine();

        while (!input.equals("bye")) {
            System.out.println(line);

            if (input.equals("list")) {
                System.out.println(indent + "Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(indent + (i + 1) + ".[" + tasks[i].getStatusIcon()
                            + "] " + tasks[i].getDescription());
                }
            } else if (input.startsWith("mark ")) {
                // "mark 2" -> cut off "mark " to get the number, -1 to get the slot
                int index = Integer.parseInt(input.substring(5)) - 1;
                tasks[index].markAsDone();
                System.out.println(indent + "Nice! I've marked this task as done:");
                System.out.println(indent + "  [X] " + tasks[index].getDescription());
            } else if (input.startsWith("unmark ")) {
                // same as mark, but "unmark " is 7 chars
                int index = Integer.parseInt(input.substring(7)) - 1;
                tasks[index].markAsNotDone();
                System.out.println(indent + "OK, I've marked this task as not done yet:");
                System.out.println(indent + "  [ ] " + tasks[index].getDescription());
            } else {
                tasks[taskCount] = new Task(input);
                taskCount++;
                System.out.println(indent + "added: " + input);
            }

            System.out.println(line);
            input = in.nextLine();
        }

        System.out.println(line);
        System.out.println(indent + "Test concluded. Try not to disappoint me next time.");
        System.out.println(line);
    }
}