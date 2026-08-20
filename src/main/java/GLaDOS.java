// Controls the banner and Entry/Exit message of ChatBot

public class GLaDOS {
    public static void main(String[] args) {
        String banner = "   ________          ____  ____  _____\n"
                + "  / ____/ /   ____ _/ __ \\/ __ \\/ ___/\n"
                + " / / __/ /   / __ `/ / / / / / /\\__ \\ \n"
                + "/ /_/ / /___/ /_/ / /_/ / /_/ /___/ / \n"
                + "\\____/_____/\\__,_/_____/\\____//____/  \n";
        String line = "____________________________________________________________";

        System.out.println(line);
        System.out.println(banner);
        System.out.println("Hello, I'm GLaDOS nice to... Oh, it's you.");
        System.out.println("State your query. I have other tests to run.");

        System.out.println(line);
        System.out.println("Test concluded. Try not to disappoint me next time.");
        System.out.println(line);
    }
}
