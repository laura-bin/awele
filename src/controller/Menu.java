package controller;

import java.util.List;

/**
 * Menus of the application and list of possible choices for each
 */
public enum Menu {
    MAIN (List.of(MenuChoice.SCORES, MenuChoice.PLAY, MenuChoice.QUIT)),
    DIFFICULTY (List.of(MenuChoice.NORMAL_MODE, MenuChoice.HARD_MODE, MenuChoice.BACK)),
    GO_BACK (List.of(MenuChoice.BACK, MenuChoice.QUIT));

    /**
     * List of choices that can be selected in the current menu
     */
    private List<MenuChoice> choices;

    /**
     * Private constructor
     * @param choices
     */
    Menu(List<MenuChoice> choices) {
        this.choices = choices;
    }

    /**
     * @return a MenuChoice list of possible choices for the current menu
     */
    public List<MenuChoice> getChoices() {
        return this.choices;
    }

}
