# Plan for Testing the Program

The test plan lays out the actions and data I will use to test the functionality of my program.

Terminology:

- **VALID** data values are those that the program expects
- **BOUNDARY** data values are at the limits of the valid range
- **INVALID** data values are those that the program should reject

---

## Game Setup

proof that the game sets itself up properly

### Test Data To Use

the output after the setup finishes and code that proves where its doing what

### Expected Test Result

the game should fill the board with the amount of coins decided in random places, no coin should start in first slot

---

## Player Moves

the player has proper control over what they want to do and that they cant do stuff they're not meant to

### Test Data To Use

output and proof of corrections where problems happened and code that stop stuff like coins going past each other and how the player selects what they want to do

### Expected Test Result

smooth control for the player with no errors or unfair advantages

---

## Player takes turns

players swap between turns between ai and player

### Test Data To Use

small clip of player taking turn then ai taking turn till game end

### Expected Test Result

they swap turns after doing their turn

---

## Player gains coins back into their coin stash and looses them and highscore is correct

the player has proper control over what they want to do and that they cant do stuff they're not meant to

### Test Data To Use

the output that shows their coins stash changes when they win or loose

### Expected Test Result

the coin stash changes when you lose or win the round and the highscore updates properly

---

## Player can win and lose -- (edit) this was shown in the test above

the player wins rounds and loses as intended and game ends when they run out of money

### Test Data To Use

output once again when they take out the gold coin they win and when the ai does they lose

### Expected Test Result

it works as intended

---

