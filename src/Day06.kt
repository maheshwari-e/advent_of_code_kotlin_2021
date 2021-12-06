fun main() {
    fun getNumberFish(input: List<String>, days: Int): Long {
        val data = input.first().split(',') .map { it.toInt() }
        val timer = mutableMapOf<Int, Long>()
        data.forEach {
            timer[it] = timer.getOrDefault(it, 0) + 1
        }
        repeat(days){
            val completed = timer.getOrDefault(0, 0)
            for(i in 1..8) { timer[i-1] = timer.getOrDefault(i, 0) }
            timer[8] = completed
            timer[6] = timer.getOrDefault(6,0) + completed
        }
        return timer.toList().sumOf { it.second }
    }

    fun part1(input: List<String>): Long {
        return getNumberFish(input, 80)
    }


    fun part2(input: List<String>): Long {
        return getNumberFish(input, 256)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934L)
    check(part2(testInput) == 26984457539)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}

