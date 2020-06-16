package awele.javafx.controller;

public class ScoreTablePane {

    private RootStack root;

    /**
     * Sets the root stack that manages the navigation in the application
     *
     * @param root root stack
     */
    public void setRoot(RootStack root) {
        this.root = root;
    }

    /**
     * Go back to main menu
     */
    public void goBack() {
        root.setMainMenuVisible();
    }

}
