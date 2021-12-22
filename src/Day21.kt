import kotlin.math.max

fun main() {

    fun part1(input: List<String>): Long {
        val players = input.map { Player(space = it.split(':')[1].trim().toInt()) }
        var dice = 0
        fun roll():Int{
            dice = (dice % 100)+1
            return dice
        }
        while (!players.any { it.score >= 1000 }){
            players.map {
                it.rolled +=3
                it.space = (roll() + roll()+roll()+ it.space -1) % 10 +1
                it.score += it.space
            }
        }
        val loser = players.first { it.score < 1000 }
        return ((loser.score - loser.space) *  ((loser.rolled * 2) -3)).toLong()
    }

    fun part2(input: List<String>): Long {
        val players = input.map {  it.split(':')[1].trim().toInt() }
        val outcome = mutableMapOf<Universe, PossibleOutcome>()
        fun  calculateUniverses(universe: Universe): PossibleOutcome {
            outcome[universe]?.let { return it }
            val po = PossibleOutcome(0, 0)
            for(dice1 in 1..3){
                for(dice2 in 1..3) {
                    for(dice3 in 1..3){
                        val newSpace = (universe.player1 + dice1 +dice2 + dice3 -1) % 10 +1
                        val newScore = newSpace + universe.player1Score
                        if(newScore >=  21) po.player1++
                        else {
                            val newPO = calculateUniverses(Universe(universe.player2, newSpace, universe.player2Score, newScore ))
                            po.player1 += newPO.player2
                            po.player2 += newPO.player1
                        }
                    }
                }
            }
            outcome[universe] = po
            return po
        }
        return calculateUniverses(Universe(players[0], players[1])).let { max(it.player1, it.player2) }
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    check(part1(testInput)  == 739785L)
    check(part2(testInput) == 444356092776315L)

    val input = readInput("Day21_test")
    println(part1(input))
    println(part2(input))
}

data class Player(var space: Int, var score: Int  = 0, var rolled:Int = 0)
data class Universe(val player1:Int, val player2: Int, val player1Score:Int = 0,  val player2Score:Int = 0)
data class PossibleOutcome(var player1: Long, var player2: Long)