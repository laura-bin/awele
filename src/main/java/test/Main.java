package test;

import controller.GameController;
import controller.UIConsoleController;
import controller.jfx.JavaFxController;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("missing UI to launch, pass 'javafx' or 'console' to the program to choose.");
            System.exit(1);
            return;
        }

        switch (args[0]) {
            case "javafx":
                Application.launch(JavaFxController.class, args);
                break;

            case "console":
                GameController game = new GameController(new UIConsoleController());
                game.start();
                break;

            default:
                throw new IllegalArgumentException("invalid ui type, valid ui types are 'console' and 'javafx'");
        }

    }
}
