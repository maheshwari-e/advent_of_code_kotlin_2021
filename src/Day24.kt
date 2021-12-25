fun main() {

    fun aluProgram(z: Long, instruction: List<String>, w: Long): Pair<Long, Int> {
        val dimension = mutableMapOf('w' to w,'x' to 0L, 'y' to 0L, 'z' to z )
        val div = instruction[4].substringAfterLast(" ").toInt()
        instruction.map {
            val value = it.split(" ").toMutableList()
            when(value[0]){
                "inp" -> {}
                else -> { val b= if(value[2].toIntOrNull() == null) dimension[value[2].first()]!! else value[2].toLong()
                    when(value[0]) {
                        "add" -> dimension.merge(value[1].first(), b) { v1, v2 -> v1 + v2 }
                        "mul" -> dimension.merge(value[1].first(), b) { v1, v2 -> v1 * v2 }
                        "div" -> dimension.merge(value[1].first(), b) { v1, v2 -> v1 / v2 }
                        "mod" -> dimension.merge(value[1].first(),b) { v1, v2 -> v1.mod(v2)}
                        "eql" -> dimension.merge(value[1].first(),b) { v1, v2 -> if(v1 == v2) 1L else 0L}
                        else -> throw IllegalArgumentException("Invalid Operation ${value[0]}")
                    }
                }
            }
        }
        return (dimension['z']!! to div)
    }

    fun solution(input: List<String>): Pair<Long, Long> {
        var evaluateZ = mutableMapOf(0L to (0L to 0L))
        input.chunked(18).map { instruction ->
            val  calculateMap = mutableMapOf<Long, Pair<Long, Long>>()
            evaluateZ.map {  (z, MONAD) ->
                (1..9).map {
                    val (calculateZ, div) = aluProgram(z, instruction, it.toLong())
                    if(div == 1 || (div == 26 && calculateZ < z)) {
                        calculateMap[calculateZ] =
                            minOf(calculateMap[calculateZ]?.first?: Long.MAX_VALUE, MONAD.first * 10  + it) to
                                    maxOf(calculateMap[calculateZ]?.second ?: Long.MIN_VALUE, MONAD.second * 10 + it)
                    }
                }
            }
            evaluateZ = calculateMap
        }
        return evaluateZ[0]!!
    }
    fun part1(input: List<String>): Long {
        return solution(input).second
    }
    fun part2(input: List<String>): Long {
        return solution(input).first
    }


// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day24")
    check(part1(testInput)  == 94399898949959L)
    check(part2(testInput) == 21176121611511L)

    val input = readInput("Day24")
    println(part1(input))
    println(part2(input))
}