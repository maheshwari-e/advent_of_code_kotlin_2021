import kotlin.math.abs

fun main() {
    fun countDotsAfterFirstInstruction(fold: Pair<String, Int>, dots: Set<Pair<Int, Int>>): Set<Pair<Int, Int>> {
        return dots.map { (x, y) ->
            if(fold.first == "y") (x to fold.second - abs(y - fold.second))
            else (fold.second - abs(x - fold.second) to y)
        }.toSet()
    }
    fun getDotsAndInstruction(input: List<String>):Pair<List<Pair<Int, Int>>,List<Pair<String, Int>>>{
        val dots = mutableListOf<Pair<Int, Int>>()
        val instruction = mutableListOf<Pair<String, Int>>()
        input.map { value ->
            when {
                value.isNotEmpty()  && value.startsWith("fold along") -> {
                    instruction.add(value.split("fold along")[1].split('=').let { it[0].trim() to it[1].toInt() })
                }
                value.isNotEmpty() -> dots.add (value.split(',').let { (x, y) -> x.toInt() to y.toInt() })
                else -> {}
            }
        }
        return (dots to instruction)
    }

    fun part1(input: List<String>): Int {
        val (dots, instruction) = getDotsAndInstruction(input)
        return if (instruction.isNotEmpty()) countDotsAfterFirstInstruction(instruction.first(), dots.toSet()).size else 0
    }

    fun part2(input: List<String>): Int {
        val (dots, instruction) = getDotsAndInstruction(input)
        var updatedDots = dots.toSet()
        instruction.forEach { updatedDots = countDotsAfterFirstInstruction(it, updatedDots) }
        updatedDots.display()
        return 0
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)
//    check(part2(testInput) == 3509)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}

private fun Set<Pair<Int, Int>>.display() {
    for(y in 0..this.maxOfOrNull { it.second }!!) {
      val result =  (0..this.maxOfOrNull { it.first }!!).joinToString(" "){ if(this.contains(it to y)) "#" else "." }
        println(result)
    }
}
