import controller.UIConsoleController;
import controller.GameController;

public class Main {
    public static void main(String[] args) {

        GameController game = new GameController(new UIConsoleController());
        game.start();

    }
}
