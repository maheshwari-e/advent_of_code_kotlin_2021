import java.security.InvalidParameterException

fun main() {

    fun part1(input: List<String>): Int {
        val binary = input.first().convertHexDecimalToBinary()
        val encodeBITS = EncodeBITS(binary.toMutableList())
        return encodeBITS.encodeBITSOperator().second.sum()
    }

    fun part2(input: List<String>): Long {
        val binary = input.first().convertHexDecimalToBinary()
        val encodeBITS = EncodeBITS(binary.toMutableList())
        return encodeBITS.encodeBITSOperator().first
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput)  == 14)
    check(part2(testInput) == 3L)

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}

private fun String.convertHexDecimalToBinary() = this.map {
    var digit = Integer.toBinaryString(Integer.parseInt(it.toString(), 16))
    if (digit.length < 4) { repeat((0 until 4 - digit.length).count()) { digit = "0$digit" } }
    digit
}.joinToString("")

fun <T> MutableList<T>.takeAndRemove(n: Int)  = this.take(n).also { this.subList(0,n).clear()}.joinToString("")

class EncodeBITS(private val binary: MutableList<Char>){
    private val version = mutableListOf<Int>()

    private fun literalValue(binary: MutableList<Char>): Long {
        val literalValue = StringBuilder()
        do{
            val lastGroup = binary.takeAndRemove(1) != "0"
            literalValue.append(binary.takeAndRemove(4))
        } while (lastGroup)
        return literalValue.toString().toLong(2)
    }

    private fun getSubPacketsOperator(binary: MutableList<Char>, subPacketsLiteralValue: MutableList<Long>) {
        val indicator = binary.takeAndRemove(1)
        when {
            indicator == "0" && binary.size > 15 -> {
                val length = binary.takeAndRemove(15).toInt(2)
                val subPackets = binary.takeAndRemove(length).toMutableList()
                while (subPackets.isNotEmpty()) { subPacketsLiteralValue.add(encodeBITSOperator(subPackets).first) }
            }
            indicator == "1"  -> {
                val length = binary.takeAndRemove(11).toInt(2)
                repeat(length){ subPacketsLiteralValue.add(encodeBITSOperator(binary).first) }
            }
        }
    }

    fun encodeBITSOperator(packets: MutableList<Char>  = binary): Pair<Long, MutableList<Int>> {
        version.add(packets.takeAndRemove(3).toInt(2))
        val subPacketsLiteralValue = mutableListOf<Long>()
        return( when(val typeId = packets.takeAndRemove(3).toInt(2)) {
            4 -> literalValue(packets)
            else -> {
                getSubPacketsOperator(packets, subPacketsLiteralValue)
                evaluateSubPackets(typeId, subPacketsLiteralValue)
            }
        } to version)
    }

    private fun evaluateSubPackets(typeId: Int, subPacketsLiteralValue: MutableList<Long>): Long {
        return when(typeId){
            0 -> subPacketsLiteralValue.sum()
            1 -> subPacketsLiteralValue.fold(1){ acc, value -> acc * value  }
            2 -> subPacketsLiteralValue.minOf { it }
            3 -> subPacketsLiteralValue.maxOf { it }
            5 -> if(subPacketsLiteralValue[0] > subPacketsLiteralValue[1])  1L else 0L
            6 -> if(subPacketsLiteralValue[0] < subPacketsLiteralValue[1]) 1L else 0L
            7 -> if( subPacketsLiteralValue[0] == subPacketsLiteralValue[1]) 1L else 0L
            else -> throw InvalidParameterException("Invalid TypeId for evaluate the literal value :$typeId")
        }
    }

}