import java.util.*

fun main() {
    fun getAdjacency(i: Int, j:Int, rowSize: IntRange, colSize: IntRange):List<Pair<Int, Int>>{
        return listOf(i-1 to j , i to j-1, i to j+1, i+1 to j).filter { (i,j) ->
            i in rowSize && j in colSize
        }
    }

    fun getLowestRiskLevel(chiton: List<List<Int>>): Int {
        val visited =  MutableList(chiton.size){ MutableList(chiton[0].size){ false} }
        val queue = PriorityQueue<Pair<Pair<Int, Int>,Int>>(compareBy { it.second })
        queue.add((0 to 0) to 0)
        visited[0][0] = true
        while (queue.isNotEmpty()){
            val (index, cost) = queue.poll()
            if(index == (chiton.size -1 to chiton[0].size-1)) return cost
            getAdjacency(index.first, index.second, chiton.indices, chiton[index.first].indices).forEach { (i, j) ->
                if(!visited[i][j]) {
                    queue.add((i to j) to cost + chiton[i][j])
                    visited[i][j] = true
                }
            }
        }
        return -1
    }

    fun part1(input: List<String>): Int {
        val chiton = input.map { line -> line.toList().map { it.digitToInt() } }
        return getLowestRiskLevel(chiton)
    }

    fun part2(input: List<String>): Int {
        val chiton = ( 0 until input.size*5).map { i ->
            ( 0 until input.first().length*5).map { j ->
                (input[i % input.size][j % input.first().length].digitToInt() + (i/input.size + j/input.first().length))
                    .let { value -> if(value > 9 )  value - 9  else value }
            }
        }
        return getLowestRiskLevel(chiton)
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}