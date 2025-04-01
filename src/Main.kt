import sun.invoke.empty.Empty
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
 *
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
        setUpGame()
        displayGame(0)
        println("How many of your coins do you want to place on the next board (Max 9)")
        print("Amount: ")
        coinPot = readln().toInt()
        var player: Int = Random.nextInt(1, 2)
        while (true) {
            doTurn(player) // change player param eventually
            if (player == 2) player = 1
            if (player == 1) player = 2
        }
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

fun getAction(player: Int): List<Any> {
    var actionChose: Boolean = false
    while (!actionChose) { println()
        println("What Coin Do You Wish To Move? (1-6 in the [])")

        val coinSpots = mutableListOf<Int>()
        val pickableCoins = mutableListOf<Int>()
        var count = 1
        var displayCount = count
        var string = ""
        for (i in 0..<BOARD_LENGTH) { // -------------------------------POSSIBLE SPOTS TO MOVE
            when (game[i]) {
                EMPTY -> {
                    string = "| $EMPTY ".grey()
                }
                else -> {
                    string = "| ${game[i]} "
                    if (game[i] == GOLD) string = string.yellow()
                    if (coinSpots.isEmpty() || game[i-1] == EMPTY) {
                        string += "[$displayCount]".green()
                        pickableCoins.add(displayCount-1,i)
                        displayCount++
                    }
                    coinSpots.add(count-1,i)
                    count++
                }
            }
            print(string)
        }
        println("|")

        print("Coin?: ")
        var coinToMove = readln().toInt()  // ------------------------------CHOICE SELECTION THINGY
        var lowest: Int = 0
        coinToMove = pickableCoins[coinToMove-1]
        for ( i in coinToMove-1 downTo 0 ) {
            if (game[i] == EMPTY) continue
            lowest = i
            break
        }

        if (coinToMove == 0) { /// --------------------- coin takeout
            if (game[0] == GOLD) {
                print("\nWould you like to Take The ${"Gold".yellow()} Coin Out ${"AND WIN!!?".red()} (Yes or no)")
            } else {
                print("\nWould you like to Take The Coin Out? (Yes or no)")
            }
            val takeOut = readln().first().uppercase()
            if (takeOut != "Y") continue
            game[0] = EMPTY
            println("Coin Was Removed")
            return listOf(true, player)
        } // -----------------------------------------------------------------

        println("Pick a Spot to Move to")
        count = 1
        coinSpots.clear()

        for (i in 0..BOARD_LENGTH-1) { /// ------------------------- MOVE TO SPOTS
            string = "| ".black()
            string += when (game[i]) {
                GOLD -> game[i].toString().yellow() + " "
                EMPTY -> game[i].toString().grey() + " "
                else -> game[i].toString() + " "
            }

            if (i in lowest..<coinToMove && game[i] == EMPTY) {
                string += "[${count}]".green()
                coinSpots.add(count-1,i)
                count++
            }
            print(string)

        }
        println(" |")

        print("Move To?: ")
        val moveTo = readln().toInt()
        actionChose = true
        setSlot(coinSpots[moveTo-1], game[coinToMove] )
        game[coinToMove] = EMPTY
    }
    return listOf(false, player)
}

fun doTurn(player: Int) {
    getAction(player)
    displayGame(0)
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