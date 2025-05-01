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
 * technically not two players but its against an 'ai' so you can play it by yourself
 * the ai is quite simple and technically isn't an ai because I didn't have time to properly script it
 * =====================================================================
 */
const val WAIT = 900     // how long the sleeps lasts, every Thread.Sleep should base off of this
const val BOARD_LENGTH = 15 // Length of board
// -- coin values   v v   v    v   v
const val EMPTY = " "
const val SILVER = "●"
const val GOLD = "⎊"
// display keys     v       v       v       v         v
const val TITLE = 0
const val BOX = 1
const val STRING = 2
const val SPACE = 3
const val SPOT = 4
const val BRACKET = 5
const val TSLENGTH = 5
const val DIVIDER = "│"
// ---------------------------------
val important = "!!!".red()
val question = "???".green()
val information = "+++".yellow()

// --   ---- ^ ---- ^ ----- ^ ----- ^ ------- ^ ----- ^ ---
val game = mutableListOf<Any>()  // game table
var coinStash: Int = BOARD_LENGTH * 3 - 5
var coinPot: Int = 5
var prevBracket: Int? = null
var name = ""
//initiate vars and vals -- plan

fun main() {
    var highScore = coinStash
    setUp()

    println("The goal is to get the other person to put the gold coin into the first slot so you take it out".green())
    println("whoever take the gold coin out wins (you will be against an ai)".green())
    println("you can only move coins left and cannot jump over other coins".green())
    println("the board is $BOARD_LENGTH tiles long and has a set number of coins in it that you choose from a coinstash".green())
    println("winning will add coins chosen * 1.8 to your coinstash, but losing will make you lose that x the amount in your coinstash/10 ".green())
    println("don't loose all your money and get as much as possible for a high score. bet more but at a greater risk of losing".green())
    println("\n")

    displayGame(TITLE,"", listOf(1,2))
    displayGame(STRING,"$question  Whats yer name?", listOf(1,2))
    print("$DIVIDER Name: ")
    name = readln().blue()

    while (coinStash >= 0) { // loop through turns till gold coin is removed -- plan
        displayGame(STRING,"$important  CoinStash: $coinStash", listOf(1,2))
        var maxCoins = BOARD_LENGTH-3
        if (maxCoins > coinStash) maxCoins = coinStash
        coinPot = maxCoins+1
        while (coinPot > maxCoins || coinPot < 1) {
            displayGame(STRING,"$question  How many of your coins do you want to place on the next board " + "(Max $maxCoins) ".red(), listOf(1,2))
            print("$DIVIDER Amount: ")
            coinPot = readln().toInt()
        }
        val bet = coinPot
        coinStash -= coinPot
        setUpGame()

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
                displayGame(STRING,("$important  You Won! and gained ${(bet * 1.8 - bet).toInt()} coins: ". green()) + name, listOf(1,2))
                coinStash += (bet * 1.8).toInt()
            }
            2 -> {
                displayGame(STRING,("$important  You lost ${((bet * 1.5)*coinStash/10 + bet).toInt()} coins: ".red()) + name, listOf(1,2))
                coinStash -= ((bet * 1.5)*coinStash/10).toInt()
                if (coinStash == 0) break
            }
        }
        if (coinStash > highScore) highScore = coinStash
        game.clear()
    }
    displayGame(STRING,"$important  You ran out of money and are now broke! ".red() + name, listOf(1,2))
    displayGame(STRING,"$important  you got a High Score of " + highScore.toString().yellow(), listOf(1,2))
    displayGame(-999999999, "", listOf(1,2))
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
    while (!actionChose) {
        if (player == 1) displayGame(STRING,"$question  What Coin Do You Wish To Move? (1-6 in the ${"[]".green()})              ", listOf(1,2))
        if (player == 2) displayGame(STRING,"$information  Choosing Coin", listOf(1,2))

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
                    if (coinSpots.isEmpty() || game[i-1] == EMPTY) {
                        pickableCoins.add(displayCount-1,i)
                        displayCount++
                    }
                    coinSpots.add(count-1,i)
                    count++
                }
            }
        }
        displayGame(BOX,"", listOf(1,2))
        displayGame(SPOT,"",pickableCoins)
        displayGame(SPACE,"", listOf(1,2))
        var coinToMove = 1
        when (player) {
            1 -> {
                print("$DIVIDER Coin?: ")
                coinToMove = readln().toInt()
                }
            2 -> {
                if (pickableCoins.size != 1) coinToMove = Random.nextInt(1,pickableCoins.size)
                if (game[0] == GOLD) coinToMove = 1
                Thread.sleep(WAIT.toLong())
                displayGame(STRING,"$information  Picked Coin $coinToMove", listOf(1,2))
            }
        }
        displayGame(SPACE,"", listOf(1,2))
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
                    displayGame(STRING,"$question  Would you like to Take The ${"Gold".yellow()} Coin Out ${"AND WIN!!?".green()} (Yes or no)", listOf(1,2))
                } else {
                    displayGame(STRING,"$question  Would you like to Take The Coin Out? (Yes or no)",listOf(1,2))
                }
            }
            print("$DIVIDER ")

            var takeOut = "Y"
            if (player == 1) takeOut = readln().first().uppercase()
            else println()

            if (takeOut != "Y") continue
            displayGame(STRING,"$important  Coin Was Removed", listOf(1,2))

            if (game[0] == GOLD){
                game[0] = EMPTY
                return listOf(true, player)
            }
            game[0] = EMPTY
            return listOf(false, player)
        } // ------------------------------------------------------------------------------------------------------------------

        when (player) {
            1 -> displayGame(STRING,"$question  Pick a Spot to Move to",listOf(1,2))
            2 -> displayGame(STRING,"$information  Choosing Spot to Move to", listOf(1,2))
        }

        count = 1
        coinSpots.clear()
        Thread.sleep((WAIT/6).toLong())
        for (i in 0..<BOARD_LENGTH) { /// ------------------------- MOVE TO SPOTS
            if (i in lowest..<coinToMove && game[i] == EMPTY) {
                coinSpots.add(count-1,i)
                count++
            }
        }
        displayGame(BOX,"", listOf(1,2))
        displayGame(SPOT,"",coinSpots)
        displayGame(SPACE,"", listOf(1,2))

        var moveTo = 1
        when (player) {
            1 -> {
                print("$DIVIDER Move To?: ")
                moveTo = readln().toInt()
            }
            2 -> {
                if (coinSpots.size != 1) moveTo = Random.nextInt(1,coinSpots.size)
                Thread.sleep(WAIT.toLong())
                displayGame(STRING,"Picked Spot $moveTo", listOf(1,2))
            }
        }
        displayGame(BRACKET,"", listOf(1,2))

        actionChose = true
        setSlot(coinSpots[moveTo-1], game[coinToMove] )
        game[coinToMove] = EMPTY
    }
    return listOf(false, player)
}

fun doTurn(player: Int): Boolean {
    displayGame(BOX,"", listOf(1,2))
    displayGame(BRACKET,"", listOf(1,2))

    if (player == 1) displayGame(STRING,"P1: $name", listOf(1,2))
    else displayGame(STRING,"P2: ${"AI".red()}", listOf(1,2))
    Thread.sleep((WAIT + 100).toLong())

    displayGame(SPACE, "", listOf(1,2))
    val end = getAction(player)
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



fun displayGame(type:Int, string:String, fillNums:List<Any>) {  // display setup
    var strang:String = EMPTY

    val title = (DIVIDER + "OLD G${GOLD.yellow()}LD".padStart((BOARD_LENGTH*TSLENGTH)/2+13).padEnd(BOARD_LENGTH*TSLENGTH+9) + DIVIDER)
    val bracket = "├" + "─".repeat(BOARD_LENGTH * TSLENGTH) + "┤"
    val roundBracket = "╭" + "─".repeat(BOARD_LENGTH * TSLENGTH) + "╮"
    val bottomBoxBracket = ("├" + ("─".repeat(TSLENGTH-1) + "┴").repeat(BOARD_LENGTH-1) + "─".repeat(TSLENGTH) + "┤")
    val boxBracket = ("├" + ("─".repeat(TSLENGTH-1) + "┬").repeat(BOARD_LENGTH-1) + "─".repeat(TSLENGTH) + "┤")
    val boxConnectorBracket = ("├" + ("─".repeat(TSLENGTH-1) + "┼").repeat(BOARD_LENGTH-1) + "─".repeat(TSLENGTH) + "┤")
    val endBracket = "╰" + "─".repeat(BOARD_LENGTH * TSLENGTH) + "╯"

    fun fillBox() {
        for (i in 0..<BOARD_LENGTH) {
            strang = when (game[i]) {
                GOLD -> game[i].toString().yellow() + " "
                else -> game[i].toString()
            }

            print(DIVIDER + " $strang ".padEnd(TSLENGTH-1))
        }
        println(" $DIVIDER")
    }

    fun boxSpots() {
        for (i in 0..<BOARD_LENGTH) {
            for (j in fillNums.indices) {
                if (fillNums[j] == i) {
                    strang = "[${j + 1}]"
                    break
                }
                strang = "   "
            }
            print(DIVIDER + strang.padEnd(TSLENGTH-1).green())
        }
        println(" $DIVIDER")
    }


// display code  --------------------------------------------------------------------------------------------------------------------------------------

    when (type) {
        TITLE -> {
            println(roundBracket)
            println(title)
            prevBracket = STRING
        }
        SPACE -> {
            println(DIVIDER.padEnd(BOARD_LENGTH*TSLENGTH+1) + DIVIDER)
            prevBracket = SPACE
        }
        BOX -> {
            when (prevBracket) {
                BOX -> println(boxConnectorBracket)
                else -> println(boxBracket)
            }
            fillBox()
            prevBracket = BOX
        }
        SPOT -> {
            when (prevBracket) {
                BOX -> println(boxConnectorBracket)
                else -> println(boxBracket)
            }
            boxSpots()
            println(bottomBoxBracket)
            prevBracket = SPACE
        }
        STRING -> {
            if (prevBracket != SPACE) when (prevBracket) {
                BOX -> println(bottomBoxBracket)
                else -> println(bracket)
            }

            println(DIVIDER + string.padEnd(BOARD_LENGTH*TSLENGTH+9) + DIVIDER)
            prevBracket = STRING
        }
        BRACKET -> {
            when (prevBracket) {
                BOX -> println(bottomBoxBracket)
                else -> println(bracket)
            }
            prevBracket = STRING
        }
        else -> println(endBracket)
    }

//    println(boxBracket)
//    fillBox()
//    println(boxConnectorBracket)
//    println(boxBracket)


}
/**
 * functions plans
 * - one for changing main list
 * - one that updates and visualises the game
 * - other ones
 */

