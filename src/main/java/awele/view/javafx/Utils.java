package awele.view.javafx;

import awele.view.GameMessage;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;

public class Utils {
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

    public static void setText(Label labeled, GameMessage message) {
        labeled.setText(message.getText());
    }
}
