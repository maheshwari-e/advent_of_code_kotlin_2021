import kotlin.math.abs
import kotlin.math.roundToInt

fun main() {

    fun getFuelCost(crab: Int, pos: Int, summation:Boolean): Int {
      return when(summation){
          false -> abs(crab - pos)
          true -> abs(crab -pos).summation()
      }
    }

    fun getLeastFuel(input: List<String>, summation:Boolean = false): Int {
        val data = input.first().split(',').map { it.toInt() }
        val maxPos = data.average().roundToInt()
        var leastFuel = -1
        for(pos in 0..maxPos){
            val fuel =  mutableListOf<Int>()
            data.forEach { fuel.add(getFuelCost(it, pos, summation)) }
            leastFuel = if(leastFuel != -1 && leastFuel < fuel.sum() ) leastFuel else fuel.sum()
        }
        return leastFuel
    }

    fun part1(input: List<String>): Int {
        return getLeastFuel(input)
    }


    fun part2(input: List<String>): Int {
     return getLeastFuel(input, summation = true)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

fun Int.summation() = this * (this + 1) / 2
