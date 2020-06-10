# awele

## V2 changes

### Major changes
- adding JavaFX user interface
- GameController abstract class creation (extended by the ConsoleGameController & the GameBoardPane JavaFX controller)
- isolation of the game actions making them to be callable at different moments by the UI
- deleting ConsoleUI interface

### Minor changes
- update old comments and adding missing comments
- reversing player order making easier javafx implementation
- adding PlayerType enum for the user interface call of getHouses() and improving the player's order management
- deleting HumanPlayer class and Player interface modification (->VirtualPlayer interface)
- 
