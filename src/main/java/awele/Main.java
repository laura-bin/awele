package awele;

import awele.console.controller.ConsoleGameController;
import awele.javafx.AweleApplication;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            Application.launch(AweleApplication.class, args);
        } else {
            ConsoleGameController game = new ConsoleGameController();
            game.start();
        }
    }
}
