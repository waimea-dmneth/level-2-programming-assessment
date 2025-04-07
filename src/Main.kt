import sun.invoke.empty.Empty
import kotlin.concurrent.thread
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
const val WAIT = 900     // how long the sleeps lasts, every Thread.Sleep should base off of this
const val BOARD_LENGTH = 15 // Length of board
// -- coin values   v
const val EMPTY = " "
const val SILVER = "◍"
const val GOLD = "❂"
// --               ^
// display keys           v
val important = "!!!  ".red()
val question = "???  ".green()
val information = "+++  ".yellow()
// --                     ^
var coinStash: Int = 40
var coinPot: Int = 5
val game = mutableListOf<Any>()  // game table
//initiate vars and vals -- plan
fun main() {
    println("${question}Whats yer name")
    print("Name: ")
    val Name = "skib"//readln()

    while (coinStash > 0) { // loop through turns till gold coin is removed -- plan
        setUp()
        displayGame()
        println()
        println("${question}How many of your coins do you want to place on the next board " + "(Max 9)".red())
        print("Amount: ")
        coinPot = readln().toInt()
        val bet = coinPot
        coinStash -= coinPot
        setUpGame()
        displayGame()

        var player: Int = Random.nextInt(1, 2)
        while (true) {
            val end = doTurn(player) // change player param eventually
            if (end) break
            when (player) {
                1 -> player = 2
                2 -> player = 1
            }
        }
        when (player) {
            1 -> {
                println("${important}You Won!".green() + Name)
                coinStash += (bet * 1.5).toInt()
                println("${important}CoinStash $coinStash")
            }
            2 -> println("You Lost! ".red() + Name)
        }
        game.clear()
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
        if (player == 1) println("${question}What Coin Do You Wish To Move? (1-6 in the [])")
        if (player == 2) println("${information}Choosing Coin")

        val coinSpots = mutableListOf<Int>()
        val pickableCoins = mutableListOf<Int>()
        var count = 1
        var displayCount = count
        var string: String

        Thread.sleep((WAIT/6).toLong())
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

        var coinToMove = 1
        when (player) {
            1 -> {
                print("Coin?: ")
                coinToMove = readln().toInt()
                }
            2 -> {
                if (pickableCoins.size != 1) coinToMove = Random.nextInt(1,pickableCoins.size)
                if (game[0] == GOLD) coinToMove = 1
                Thread.sleep(WAIT.toLong())
                println("Picked Coin $coinToMove")
            }
        }

        // ------------------------------CHOICE SELECTION THINGY
        var lowest: Int = 0
        coinToMove = pickableCoins[coinToMove-1]
        for ( i in coinToMove-1 downTo 0 ) {
            if (game[i] == EMPTY) continue
            lowest = i
            break
        }

        if (coinToMove == 0) { /// --------------------- coin takeout ----- ---------------------------------------------------
            if (player == 1) {
                if (game[0] == GOLD) {
                    print("\n${question}Would you like to Take The ${"Gold".yellow()} Coin Out ${"AND WIN!!?".green()} (Yes or no)")
                } else {
                    print("\n${question}Would you like to Take The Coin Out? (Yes or no)")
                }
            }
            var takeOut = "Y"
            if (player == 1) takeOut = readln().first().uppercase()

            if (takeOut != "Y") continue
            println("\nCoin Was Removed")

            if (game[0] == GOLD){
                game[0] = EMPTY
                return listOf(true, player)
            }
            game[0] = EMPTY
            return listOf(false, player)
        } // ------------------------------------------------------------------------------------------------------------------

        when (player) {
            1 -> println("\n${question}Pick a Spot to Move to")
            2 -> println("\n${information}Choosing Spot to Move to")
        }

        count = 1
        coinSpots.clear()
        Thread.sleep((WAIT/6).toLong())
        for (i in 0..<BOARD_LENGTH) { /// ------------------------- MOVE TO SPOTS
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

        var moveTo = 1
        when (player) {
            1 -> {
                print("Move To?: ")
                moveTo = readln().toInt()
            }
            2 -> {
                if (coinSpots.size != 1) moveTo = Random.nextInt(1,coinSpots.size)
                Thread.sleep(WAIT.toLong())
                println("Picked Spot $moveTo")
            }
        }

        actionChose = true
        setSlot(coinSpots[moveTo-1], game[coinToMove] )
        game[coinToMove] = EMPTY
    }
    return listOf(false, player)
}

fun doTurn(player: Int): Boolean {
    Thread.sleep((WAIT + 100).toLong())
    println("\nPlayer $player")
    println("---------------------------------------")
    val end = getAction(player)
    displayGame()
    return end[0] as Boolean
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
    if (lowestSlot > 12) lowestSlot = BOARD_LENGTH - 9
    var goldSlot = Random.nextInt(lowestSlot+1,BOARD_LENGTH-1)
    while (game[goldSlot] != EMPTY) goldSlot = Random.nextInt(lowestSlot+1,BOARD_LENGTH-1)
    setSlot(goldSlot, GOLD)

    return true
}

fun displayGame() {
    val tsLength = 5
    val divider = "┃"

    val title = (divider + ("OLD " + "G" + GOLD.yellow() + "LD").padStart((BOARD_LENGTH*tsLength)/2)).padEnd(BOARD_LENGTH*tsLength) + divider

    val topBracket = ("╭" + ("─".repeat(tsLength)).repeat(BOARD_LENGTH) + "╮").col(255,255,255)
    val underTitleBracket = ("├" + ("─".repeat(tsLength-1) + "┬").repeat(BOARD_LENGTH-1) + "─".repeat(tsLength) + "┫")

    println(topBracket)
    println(title)
    println(underTitleBracket)
    for (i in 0..<BOARD_LENGTH) {
        print(divider + " ${game[i]} ")
    }
}
/**
 * functions plans
 * - one for changing main list
 * - one that updates and visualises the game
 * - more coming
 */