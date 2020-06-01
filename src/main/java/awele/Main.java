package awele;

import awele.controller.UIConsoleController;
import awele.controller.GameController;
import awele.view.javafx.AweleApplication;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {

        if (false) {
            GameController game = new GameController(new UIConsoleController());
            game.start();
        }

        Application.launch(AweleApplication.class, args);
    }
}
