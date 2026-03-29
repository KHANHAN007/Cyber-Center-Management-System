import presentation.LoginMenu;
import presentation.MainMenu;
import model.User;
import service.interfaces.IUserService;
import dao.interfaces.IUserDAO;
import utils.AppFactory;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        showBanner();
        Scanner sc = new Scanner(System.in);
        try {
            IUserService userService = AppFactory.getUserService();
            IUserDAO userDAO = AppFactory.getUserDAO();

            while (true) {
                LoginMenu loginMenu = new LoginMenu(sc, userService, userDAO);
                User loggedInUser = loginMenu.displayLoginMenu();
                if (loggedInUser != null) {
                    MainMenu mainMenu = new MainMenu(loggedInUser, sc);
                    mainMenu.displayMainMenu();
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }

    public static void showBanner() throws Exception {
        String banner = """
                      ___                                     ___           ___                    ___           ___           ___           ___           ___
                    /\\__\\                     _____         /\\__\\         /\\  \\                  /\\  \\         /\\  \\         /\\__\\         /\\  \\         /\\  \\
                   /:/  /          ___       /::\\  \\       /:/ _/_       /::\\  \\                /::\\  \\       /::\\  \\       /:/ _/_        \\:\\  \\       /::\\  \\
                  /:/  /          /|  |     /:/\\:\\  \\     /:/ /\\__\\     /:/\\:\\__\\              /:/\\:\\  \\     /:/\\:\\__\\     /:/ /\\__\\        \\:\\  \\     /:/\\:\\  \\
                 /:/  /  ___     |:|  |    /:/ /::\\__\\   /:/ /:/ _/_   /:/ /:/  /             /:/ /::\\  \\   /:/ /:/  /    /:/ /:/ _/_   _____\\:\\  \\   /:/ /::\\  \\
                /:/__/  /\\__\\    |:|  |   /:/_/:/\\:|__| /:/_/:/ /\\__\\ /:/_/:/__/___          /:/_/:/\\:\\__\\ /:/_/:/__/___ /:/_/:/ /\\__\\ /::::::::\\__\\ /:/_/:/\\:\\__\\
                \\:\\  \\ /:/  /  __|:|__|   \\:\\/:/ /:/  / \\:\\/:/ /:/  / \\:\\/:::::/  /          \\:\\/:/  \\/__/ \\:\\/:::::/  / \\:\\/:/ /:/  / \\:\\~~\\~~\\/__/ \\:\\/:/  \\/__/
                 \\:\\  /:/  /  /::::\\  \\    \\::/_/:/  /   \\::/_/:/  /   \\::/~~/~~~~            \\::/__/       \\::/~~/~~~~   \\::/_/:/  /   \\:\\  \\        \\::/__/
                  \\:\\/:/  /   ~~~~\\:\\  \\    \\:\\/:/  /     \\:\\/:/  /     \\:\\~~\\                 \\:\\  \\        \\:\\~~\\        \\:\\/:/  /     \\:\\  \\        \\:\\  \\
                   \\::/  /         \\:\\__\\    \\::/  /       \\::/  /       \\:\\__\\                 \\:\\__\\        \\:\\__\\        \\::/  /       \\:\\__\\        \\:\\__\\
                    \\/__/           \\/__/     \\/__/         \\/__/         \\/__/                  \\/__/         \\/__/         \\/__/         \\/__/         \\/__/
                """;
        String[] colors = {
                "\033[91m", "\033[93m", "\033[92m",
                "\033[96m", "\033[94m", "\033[95m"
        };
        clearScreen();
        int colorIndex = 0;
        for (char c : banner.toCharArray()) {
            if (c == '\n') {
                System.out.print("\n");
                continue;
            }
            System.out.print(colors[colorIndex % colors.length] + c);
            colorIndex++;
        }

        System.out.println("\033[0m");
        Thread.sleep(2000);
        clearScreen();
    }

    public static void clearScreen() {
        for (int i = 0; i < 30; i++) {
            System.out.println();
        }
    }

}
