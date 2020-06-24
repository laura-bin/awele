package awele;

import awele.console.controller.ConsoleGameController;
import awele.javafx.AweleApplication;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);

        System.out.println(list.get(1));

        if (args.length == 0) {
            Application.launch(AweleApplication.class, args);
        } else {
            ConsoleGameController game = new ConsoleGameController();
            game.start();
        }
    }
}
