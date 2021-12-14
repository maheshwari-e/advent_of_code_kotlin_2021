fun main() {
    fun getResultForSolution(input:List<String>, iteratorCount: Int): Long{
        var polymer = mutableMapOf<List<Char>, Long>()
        input.first().toList().windowed(2).map { polymer[it]  = polymer.getOrDefault(it, 0L) + 1 }
        val rules = input.subList(2, input.size).associate { rule -> rule.split(" -> ").let {
                it[0] to it[1].first()
            }
        }
        repeat(iteratorCount){
            val newPolymer = mutableMapOf<List<Char>, Long>()
            polymer.map { (key, value) ->
                val key1 = listOf(key[0], rules.getValue(key.joinToString("")).toChar())
                newPolymer[key1] = newPolymer.getOrDefault(key1, 0) + value
                val key2  = listOf(rules.getValue(key.joinToString("")).toChar(), key[1])
                newPolymer[key2] = newPolymer.getOrDefault(key2, 0) + value
            }
            polymer  =  newPolymer
        }
        val polymerCount = polymer.keys.flatten().distinct().map { it to 0L }.associate { it }.toMutableMap()
        polymer.map { (key, value) ->
            polymerCount[key[0]] = polymerCount[key[0]]!!.plus(value)
            polymerCount[key[1]] = polymerCount[key[1]]!!.plus(value)
        }
        return polymerCount.values.let { values ->  (values.maxOf { it } - values.minOf { it })/2 + 1}
    }

    fun part1(input: List<String>): Int {
        return getResultForSolution(input, 10).toInt()
    }

    fun part2(input: List<String>): Long {
        return getResultForSolution(input, 40)
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 1588)
    check(part2(testInput) == 2188189693529L)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}