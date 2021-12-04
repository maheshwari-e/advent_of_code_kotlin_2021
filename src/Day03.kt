fun main() {
    fun part1(input: List<String>): Int {
        val strLength = input.first().length
        var gammarate = StringBuilder()
        var epsilonrate = StringBuilder()
        for (i in 0 until strLength) {
            val count = input.count { it.toCharArray()[i] == '0' }
            if (count > (input.size - count)) {
                gammarate.append('0')
                epsilonrate.append('1')
            } else {
                gammarate.append('1')
                epsilonrate.append('0')
            }
        }
        return gammarate.toString().toInt(2) * epsilonrate.toString().toInt(2)
    }

    fun determineRating(input: List<String>, type: String): String {
        var data = input.toMutableList()
        var index = 0
        while(data.size > 1) {
            val filterData = data.filter {  it.toCharArray()[index] == '0' }
            ++index
            val count = filterData.size
            when{
                count > (data.size - count) && type == "oxygen"  -> { data.removeAll(data.filter { !filterData.contains(it) }) }
                count <= (data.size - count) && type == "oxygen" -> { data.removeAll(filterData)}
                count <= (data.size - count) && type == "co2"  -> { data.removeAll(data.filter { !filterData.contains(it) }) }
                count > (data.size - count) && type == "co2" -> {  data.removeAll(filterData) }
            }
        }
        return data.first()
    }

    fun part2(input: List<String>): Int {
        val oxygenrate = determineRating(input, "oxygen").toInt(2)
        val co2rate = determineRating(input, "co2").toInt(2)
        return oxygenrate * co2rate
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}