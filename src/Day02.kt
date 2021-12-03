fun main() {
    fun part1(input: List<String>): Int {
        var horizontal = 0
        var depth  = 0

        input.forEach { data ->
           val value =  data.split(" ")
            when(value[0]){
                "forward" -> { horizontal += value[1].toInt() }
                "down" -> { depth += value[1].toInt() }
                "up" -> { depth -= value[1].toInt() }
            }
        }
        return (horizontal * depth)
    }

    fun part2(input: List<String>): Int {
        var horizontal = 0
        var depth  = 0
        var aim = 0

        input.forEach { data ->
            val value =  data.split(" ")
            when(value[0]){
                "forward" -> {
                    horizontal += value[1].toInt()
                    depth +=  aim * value[1].toInt()
                }
                "down" -> { aim += value[1].toInt() }
                "up" -> { aim -= value[1].toInt() }
            }
        }
        return (horizontal * depth)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}