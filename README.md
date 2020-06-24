# awele

## V2 changes

### Major changes
- adding JavaFX user interface
- isolation of the game actions making them callable at different moments by the UI
- deleting ConsoleUI interface

### Minor changes
- update comments and adding missing comments
- reversing player order making easier javafx implementation
- adding PlayerType enum for the user interface call of getHouses() and improving the player's order management
- deleting HumanPlayer class and Player interface modification (->VirtualPlayer interface)

## V3 changes

### Major changes
- implementing scores logging in database and scores table view

## V3.1 changes

### Major changes
- implementing hard player minimax algorithm (optimized by alpha-beta pruning)