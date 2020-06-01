package controller.jfx;

import controller.Menu;
import controller.MenuChoice;
import controller.UIController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import model.GameBoard;
import view.GameErrorMessage;
import view.GameMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class ViewController implements UIController {

    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(1);

    private enum State {
        MENU,
        GAME,
        SCORES,
    }

    @FXML
    private Pane rootContainer;

    @FXML
    private Label message;

    @FXML
    private Label error;

    @FXML
    private Pane mainContainer;

    private State currentState = null;

    @Override
    public void menu(Menu menu, Consumer<MenuChoice> callback) {
        var nodes = mainContainer.getChildren();
        nodes.clear();

        nodes.add(new Label(menu.getTitle()));
        for (MenuChoice choice : menu.getChoices()) {
            var button = new Button(choice.getText());
            button.setOnAction(e -> callback.accept(choice));
            nodes.add(button);
        }
    }

    @Override
    public void waitNumber(IntConsumer callback) {

    }

    @Override
    public void displayBoard(GameBoard board) {
        var nodes = mainContainer.getChildren();

        if (currentState != State.GAME) {
            nodes.clear();
            // loadBoard();
            currentState = State.GAME;
        }
    }

    @Override
    public void displayMessage(GameMessage message, Object... params) {
        this.error.setText("");
        this.message.setText(message.getText(params));
    }

    @Override
    public void displayMessage(GameErrorMessage message, Object... params) {
        this.message.setText("");
        this.error.setText(message.getText(params));
    }

    @Override
    public void displayVirtualPlayerPlayingAnimation(Runnable callback) {
        message.setText("AI is picking");
        Task<Object> task = new Task<>() {
            @Override
            protected Object call() {
                for (int i = 0; i < 3; i++) {
                    final int j = i;
                    Platform.runLater(() -> message.setText(message.getText() + "."));

                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        break;
                    }
                }

                Platform.runLater(callback);

                return null;
            }
        };

        THREAD_POOL.submit(task);
    }
}
