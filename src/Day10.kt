fun main() {
    val brackets = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
    fun getResultLine(line: String): Pair<Boolean, MutableList<Char>> {
        val stack = mutableListOf<Char>()
        for( it in line.toCharArray()){
            when{
                (stack.isEmpty() || brackets.keys.contains(it))  -> stack.add(it)
                it == brackets[stack.last()] -> stack.removeLast()
                else -> {
                    stack.add(it)
                    return Pair(true, stack)
                }
            }
        }
        return Pair(false, stack)
    }
    fun part1(input: List<String>): Int {
        val points = mapOf(')' to 3,']' to 57,'}' to 1197,'>' to 25137)
        return input.sumOf {
            val(illegal, stack ) = getResultLine(it)
            (if(illegal) points[stack.last()] else 0)!! 
        }
    }

    fun part2(input: List<String>): Long {
        val points = mapOf(')' to 1,']' to 2,'}' to 3,'>' to 4)
        val inCompleteLine = mutableListOf<Long>()
        input.forEach { line ->
            val(illegal, stack) = getResultLine(line)
            if(stack.isNotEmpty() && !illegal ) {
                inCompleteLine.add(stack.reversed().map { points[brackets[it]]!! }.fold(0){acc, i -> (acc * 5)+i})
            }
        }
        return inCompleteLine.sorted()[inCompleteLine.size/2]
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}