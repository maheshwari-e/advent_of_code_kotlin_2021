fun main() {
    fun part1(input: List<String>): Int {
        var increased  = 0
        val measurement = input.map { it.toInt() }
        for(index in 1 until measurement.size){
            if(measurement[index - 1] < measurement[index]) increased++
        }
        return increased
    }

    fun part2(input: List<String>): Int {
        val measurement = input.map { it.toInt()}
        return measurement
            .windowed(3)
            .map{ it.sum() }
            .windowed(2)
            .count { (prev, cur) -> prev < cur }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
