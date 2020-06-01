package awele.controller;

import java.util.List;

/**
 * Menus of the application and list of possible choices for each
 */
public enum Menu {
    MAIN("= = = = = = = = = =  MAIN MENU  = = = = = = = = = =",
            MenuChoice.SCORES, MenuChoice.PLAY, MenuChoice.QUIT),
    DIFFICULTY("= = = = = = = = = =  NEW  GAME  = = = = = = = = = =",
            MenuChoice.NORMAL_MODE, MenuChoice.HARD_MODE, MenuChoice.BACK),
    START_GAME("Do you want to start the game ?",
            MenuChoice.YES, MenuChoice.NO),
    NOT_YET_IMPLEMENTED("This functionality is not yet implemented.",
            MenuChoice.BACK, MenuChoice.QUIT),
    GO_BACK("= = = = = = = = = = = = = = = = = = = = = = = = = =",
            MenuChoice.BACK, MenuChoice.QUIT);

    /**
     * Title to display above the menu choices
     */
    private final String title;

    /**
     * List of choices that can be selected in the current menu
     */
    private final List<MenuChoice> choices;

    /**
     * Private constructor
     *
     * @param choices
     */
    Menu(String title, MenuChoice... choices) {
        this.title = title;
        this.choices = List.of(choices);
    }

    public String getTitle() {
        return this.title;
    }

    /**
     * @return a MenuChoice list of possible choices for the current menu
     */
    public List<MenuChoice> getChoices() {
        return this.choices;
    }

}
