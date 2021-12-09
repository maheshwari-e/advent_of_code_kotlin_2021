fun main() {

    fun getInputOutputData(input: List<String>): List<Pair<String, String>> {
        return input.map { it.split(" | ").let { (ip, op) -> ip to op }}
    }

    fun part1(input: List<String>): Int {
        val inputOutput = getInputOutputData(input)
        return  inputOutput.map { io ->
            io.second.split(" ").filter{ it.length in listOf(2,3,4,7) }
        }.flatten().size
    }


    fun part2(input: List<String>): Int {
        val inputOutput = getInputOutputData(input)
        val digits = mutableListOf<Int>()
        val mapDigits = hashMapOf<Int, String>()
        repeat(input.size) { index ->
            inputOutput[index].first.split(" ").sortedBy { it.length }.forEach{ string ->
                when(string.length){
                    2 ->  mapDigits[1] = string
                    3 ->  mapDigits[7] = string
                    4 -> mapDigits[4] = string
                    7 -> mapDigits[8] = string
                    5 ->
                        when{
                            mapDigits[7]!!.toCharArray().all { string.contains(it)} ->  mapDigits[3] = string
                            mapDigits[4]!!.count{string.contains(it)}  == 3 -> mapDigits[5] = string
                            else -> mapDigits[2] = string
                        }
                    else ->
                        when{
                            mapDigits[3]!!.toCharArray().all { string.contains(it)} ->  mapDigits[9] = string
                            mapDigits[7]!!.toCharArray().all { string.contains(it)} ->  mapDigits[0] = string
                            else -> mapDigits[6] = string
                        }
                }
            }
            digits.add(inputOutput[index].second.split(" ").map { string ->
                mapDigits.entries.find { it.value.toCharArray().sorted() == string.toCharArray().sorted() }?.key
            }.joinToString("").toInt())
        }
        return digits.sum()
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}