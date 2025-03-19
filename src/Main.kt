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
var coinStash: Int = 40
var coinPot: Int = 5
val Game = mutableListOf<Any>()
//initiate vars and vals -- plan
fun main() {
    SetUp()
    DisplayGame()
    while (coinStash > 0) { // loop through turns till gold coin is removed -- plan
        while (true) {
            SetUpGame() // parameter, change to a number decided by player later ------ !!!
            DisplayGame()
            break // dont keep
        }
        break // dont keep
    }


    // do something for winner -- plan

}

fun SetUp() {
    for (i in 0..BOARD_LENGTH-1) {
        Game.add(EMPTY)
    }
}

fun SetSlot(index: Int, setTo: Any): Boolean {
    if (index < 0 || index >= Game.size) return false
    if (Game[index] != EMPTY) return false
    Game[index] = setTo
    return true
}

fun SetUpGame(): Boolean {
    if (coinPot > BOARD_LENGTH) return false
    while (coinPot > 0) {
        for (i in 1..BOARD_LENGTH) {
            if (coinPot <= 0) break
            if (Random.nextInt(1,10) < 5) continue
            val result = SetSlot(i, SILVER)
            if (!result) continue
            coinPot--
        }
    }
    var goldSlot = Random.nextInt(1,BOARD_LENGTH-1)
    while (Game[goldSlot] != EMPTY) goldSlot = Random.nextInt(1,BOARD_LENGTH-1)
    SetSlot(goldSlot, GOLD)


    return true
}

fun DisplayGame() {
    println(Game)
}
/**
 * functions plans
 * - one for changing main list
 * - one that updates and visualises the game
 * - more coming
 */