import java.lang.IllegalArgumentException
import kotlin.math.ceil
import kotlin.math.floor

fun main() {

    fun parseSnailFishNumber(line: String): SnailFishNumber {
        var i = 0
        fun getParse():SnailFishNumber{
            return  when(line[i++]){
                '[' -> {
                    val left = getParse().also { i++ }
                    val right = getParse().also { i++ }
                    SnailFishNumber.Pair(left, right)
                }
                in '0'..'9' -> SnailFishNumber.RegularNumber(line[i-1].digitToInt())
                else -> throw IllegalArgumentException("Invalid Check")
            }
        }
        return getParse()
    }


    fun part1(input: List<String>): Int {
        val snailFishNumbers = input.map { parseSnailFishNumber(it) }
        val value = snailFishNumbers.reduce { acc, snailFishNumber -> acc.sum(snailFishNumber)}
        return value.magnitude()
    }

    fun part2(input: List<String>): Int {
        val snailFishNumbers = input.map { parseSnailFishNumber(it) }
        return snailFishNumbers.combo().maxOf { it.first.sum(it.second).magnitude() }
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput)  == 4140)
    check(part2(testInput) == 3993)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}

private fun  List<SnailFishNumber>.combo(): List<Pair<SnailFishNumber, SnailFishNumber>> {
    return this.map {
            a ->  this.mapNotNull { if(a != it) Pair(a.copy() , it.copy()) else null }
    }.flatten()
}

private fun  List<SnailFishNumber>.reduce(): Boolean {
    val element = firstOrNull { it is SnailFishNumber.Pair  && it.isExploded()} as? SnailFishNumber.Pair
    if(element != null) {
        val xValue = take(indexOf(element)).lastOrNull { it is SnailFishNumber.RegularNumber } as? SnailFishNumber.RegularNumber
        val yValue = drop(indexOf(element) + 3).firstOrNull { it is SnailFishNumber.RegularNumber } as? SnailFishNumber.RegularNumber
        element.exploded(xValue, yValue)
        return true
    }
    val splitElement = firstOrNull { it is SnailFishNumber.RegularNumber && it.isSplitable() } as? SnailFishNumber.RegularNumber
    if(splitElement != null) {
        splitElement.split()
        return true
    }
    return false
}

sealed class SnailFishNumber {
    abstract fun copy(): SnailFishNumber
    var parent: Pair?= null
    fun sum(snailFishNumber: SnailFishNumber): Pair {
        val result = Pair(this, snailFishNumber)
        while (result.toList().reduce()){ continue }
        return result
    }

    fun toList(list: MutableList<SnailFishNumber> = mutableListOf()): List<SnailFishNumber> {
        list.add(this)
        if(this is Pair){
            this.x.toList(list)
            this.y.toList(list)
        }
        return  list
    }

    abstract fun magnitude():Int

    data class RegularNumber(var value: Int): SnailFishNumber() {
        fun isSplitable() = value >= 10
        fun split() {
            val number = value / 2.toDouble()
            val newPairValue = Pair(x = RegularNumber(floor(number).toInt()), y = RegularNumber(ceil(number).toInt()))
            parent?.update(this, newPairValue)
        }

        override fun toString(): String = value.toString()

        override fun magnitude(): Int = value

        override fun copy() = RegularNumber(this.value)
    }

    class Pair(var x: SnailFishNumber, var y:SnailFishNumber): SnailFishNumber() {
        init {
            x.parent = this
            y.parent = this
        }
        fun isExploded(): Boolean {
            return x is RegularNumber && y is RegularNumber && parent?.parent?.parent?.parent!=null
        }

        fun exploded(xValue: RegularNumber?, yValue: RegularNumber?) {
            val explodedX = x as? RegularNumber ?: return
            val explodedY = y as? RegularNumber ?: return
            xValue?.let {  it.value  += explodedX.value }
            yValue?.let {  it.value +=  explodedY.value}
            parent?.update(this, RegularNumber(0))
        }

        fun update(pair: SnailFishNumber, regularNumber: SnailFishNumber) {
            when{
                x == pair -> { x = regularNumber}
                y == pair -> {y = regularNumber}
            }
            regularNumber.parent = this
        }

        override fun toString(): String = "[$x, $y]"
        override fun magnitude(): Int = 3* x.magnitude() + 2 * y.magnitude()

        override fun copy() = Pair(x.copy(), y.copy())
    }
}
