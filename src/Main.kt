import kotlin.random.Random

/**
 * =====================================================================
 * Programming Project for NCEA Level 2, Standard 91896
 * ---------------------------------------------------------------------
 * Project Name:   Old Gold
 * Project Author: David Neth
 * GitHub Repo:    https://github.com/waimea-dmneth/level-2-programming-assessment
 * ---------------------------------------------------------------------
 * Notes:
 * i use capitals for tables and functions its not a mistake
 * =====================================================================
 */
const val BOARD_LENGTH = 15
// -- coin values   v
const val EMPTY = 0
const val SILVER = 1
const val GOLD = 2
// --               ^
// display keys           v
const val SHOWCOINS = 1
// --                     ^
var coinStash: Int = 40
var coinPot: Int = 5
val game = mutableListOf<Any>()
//initiate vars and vals -- plan
fun main() {
    setUp()
    displayGame(0)
    while (coinStash > 0) { // loop through turns till gold coin is removed -- plan
        while (true) {
            setUpGame()
            displayGame(0)
            doTurn(1) // change player param eventually
            break // dont keep
            //coinPot = 5

        }
        break // dont keep
    }


    // do something for winner -- plan
}

fun setUp() {
    for (i in 0..<BOARD_LENGTH) {
        game.add(EMPTY)
    }
}

fun setSlot(index: Int, setTo: Any): Boolean {
    if (index < 0 || index >= game.size) return false
    if (game[index] != EMPTY) return false
    game[index] = setTo
    return true
}

fun getAction(player: Int) {
    println()
    println("What Coin Do You Wish To Move? (1-6 in the [])")
    displayGame(SHOWCOINS)
    print("Coin?: ")
    var coinToMove = readln().toInt()
    for (i in 0..BOARD_LENGTH-1) {
        if (i != EMPTY) coinToMove--
        if (coinToMove == 0) continue
        coinToMove = i
        break
    }
    println("Pick a Spot to move to")
    for (i in 0..coinToMove) {}
        print()
    // get the action and show possible moves with chosen piece
    // return action with player num
}

fun doTurn(player: Int) {
    getAction(player)
    // get action
    // play action
}

fun setUpGame(): Boolean {
    var lowestSlot = 15
    if (coinPot > BOARD_LENGTH || coinPot == 0) return false
    while (coinPot > 0) {
        for (i in BOARD_LENGTH downTo 1) {               // checks for
            if (coinPot <= 0) break                           // if coinpot is empty
            if (Random.nextInt(1,10) < 9) continue // randomly set
            val result = setSlot(i, SILVER)                  // checks slot is empty V
            if (!result) continue                           //                       ^
            coinPot--
            if (i < lowestSlot) lowestSlot = i
            //println(lowestSlot)
        }
    }
    var goldSlot = Random.nextInt(lowestSlot+1,BOARD_LENGTH-1)
    while (game[goldSlot] != EMPTY) goldSlot = Random.nextInt(lowestSlot+1,BOARD_LENGTH-1)
    setSlot(goldSlot, GOLD)

    return true
}

fun displayGame(type: Int) {
    when (type) {
        SHOWCOINS -> {
            var count:Int = 1
            for (i in 0..<BOARD_LENGTH) {
                when (game[i]) {
                    EMPTY -> print("|"+" $EMPTY ".grey())
                    SILVER -> {
                        print("| $SILVER [$count] ")
                        count++
                    }
                    GOLD -> {
                        print("|"+" $GOLD [$count] ".yellow())
                        count++
                    }
                }

            }
            println("|")
        }
        else -> {
            println(game)
        }
    }

}
/**
 * functions plans
 * - one for changing main list
 * - one that updates and visualises the game
 * - more coming
 */