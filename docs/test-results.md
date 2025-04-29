# Results of Testing

The test results show the actual outcome of the testing, following the [Test Plan](test-plan.md)

---

## The game sets up

when the code is run the game sets everything up so the player can play and no coin can start in the first slot

### Test Data Used

the output after the setup shows a table with set amount of coins from coinstash (excluding gold coin) and clearly showing where coins are

### Test Result

![setup.png](screenshots/setup.png)

the player selected 9 coins to put on the board from their stash, the board was set up with 9 coins on the board (doesn't include gold coin)

---

## Player Moves

the player has proper control over what they want to do and that they cant do stuff they're not meant to

### Test Data Used

output display that SHOWS players option and where they can move to plus what coins they can pick to move and the coins must be moveable same goes for the ai

### Test Result

![playermoves.png](screenshots/playermoves.png)

player options display valid moves, the player and ai can both select coins and move them to valid spots

---

## Player takes turn with ai

players swap between turns between ai and player

### Test Data Used

a gif of the player doing their turn then the ai doing its turn

### Test Result

![Takingturns.gif](screenshots/Takingturns.gif)

the player swaps turns with ai multiple times

---

