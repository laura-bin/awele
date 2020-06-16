package awele.console.controller;

import awele.ui.GameMessage;

import java.util.List;

/**
 * Menus of the application and list of possible choices for each
 */
public enum Menu {
    MAIN(GameMessage.MAIN_MENU,
            MenuChoice.SCORES, MenuChoice.PLAY, MenuChoice.QUIT),
    DIFFICULTY(GameMessage.NEW_GAME_SETTINGS,
            MenuChoice.NORMAL_MODE, MenuChoice.HARD_MODE, MenuChoice.BACK),
    START_GAME(GameMessage.PLAY_FIRST,
            MenuChoice.YES, MenuChoice.NO),
    NOT_YET_IMPLEMENTED(GameMessage.EMPTY,
            MenuChoice.BACK, MenuChoice.QUIT),
    GO_BACK(GameMessage.EMPTY,
            MenuChoice.BACK, MenuChoice.QUIT);

    /**
     * Title to display above the menu choices
     */
    private final GameMessage title;

    /**
     * List of choices that can be selected in the current menu
     */
    private final List<MenuChoice> choices;

    /**
     * Private constructor
     *
     * @param choices
     */
    Menu(GameMessage title, MenuChoice... choices) {
        this.title = title;
        this.choices = List.of(choices);
    }

    /**
     * @return the menu title formatted for teh console display
     */
    public String getTitle() {
        int titleLength = 50;
        StringBuilder sb = new StringBuilder();

        if (title == GameMessage.EMPTY) {
            sb.append("=".repeat(titleLength));
        } else {
            String titleMessage = String.format(" %s ", title.getText().toUpperCase());
            sb.append("=".repeat((titleLength - titleMessage.length()) / 2));
            sb.append(titleMessage);
            while (sb.length() < titleLength) {
                sb.append("=");
            }
        }
        return sb.toString();
    }

    /**
     * @return a MenuChoice list of possible choices for the current menu
     */
    public List<MenuChoice> getChoices() {
        return this.choices;
    }

}
