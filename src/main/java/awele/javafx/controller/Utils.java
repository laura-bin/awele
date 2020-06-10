package awele.javafx.controller;

import awele.ui.GameMessage;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;

public class Utils {

    /**
     * Sets a GameMessage text in a JavaFX view component
     *
     * @param labeled JavaFX component in which the text have to be set
     */
    public static void setText(Labeled labeled) {
        var gameMessageName = labeled.getText();
        GameMessage gameMessage;
        try {
            gameMessage = GameMessage.valueOf(gameMessageName);
        } catch (IllegalArgumentException e) {
            System.err.println(String.format("Missing GameMessage value for %s", gameMessageName));

            labeled.setText("**" + gameMessageName + "**");

            return;
        }

        labeled.setText(gameMessage.getText());
    }

    /**
     * Sets a GameMessage text in a JavaFX view component
     * @param labeled JavaFX component in which the text have to be set
     * @param message GameMessage to display
     */
    public static void setText(Label labeled, GameMessage message) {
        labeled.setText(message.getText());
    }
}
