import com.sun.media.sound.InvalidDataException

fun main() {
    fun getRandomNumbers(input: List<String>): List<Int> {
      return input.first().split(',').map { it.toInt() }
    }

    fun getBoards(input: List<String>): List<Board> {
        val boards = mutableListOf<Board>()
        val formatInput = input.subList(1, input.size).filter { it.isNotEmpty() }.chunked(5)
        formatInput.forEach { data ->
            boards.add(
                Board(
                    value = data.map { row ->
                        row.split(' ')
                            .filter { it.isNotEmpty() }
                            .map { Card(it.toInt()) }
                    }
                )
            )
        }
        return boards
    }

    fun part1(input: List<String>): Int {
        val randomNumbers = getRandomNumbers(input)
        val boards = getBoards(input)
        randomNumbers.forEach { number ->
            boards.forEach{ board ->
                board.value.forEach { row ->
                    row.forEach {  if (it.value == number) it.isMarked = true }
                }
               if(board.hasWon()) return board.getResult(number)
            }
        }
        throw InvalidDataException("None of the board won")
    }


    fun part2(input: List<String>): Int {
        val randomNumbers = getRandomNumbers(input)
        val boards = getBoards(input).toMutableList()
        randomNumbers.forEach { number ->
            val wonBoards = mutableListOf<Board>()
            boards.forEach{ board ->
                board.value.forEach { row ->
                    row.forEach {  if (it.value == number) it.isMarked = true }
                }
                if(board.hasWon()) {
                    if (boards.size > 1) wonBoards.add(board)
                    else { return board.getResult(number) }
                }
            }
            boards.removeAll(wonBoards)
        }
        throw InvalidDataException("None of the board won")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

data class Board(val value: List<List<Card>>){
    fun hasWon():Boolean {
       value.forEach { row ->
           if (row.count { it.isMarked } == 5 ) return true
       }
        for(i in value.indices){
            var count  = 0
            for(j in value.indices){ if (value[j][i].isMarked) count++ }
            if (count == 5)  return true
        }
        return false
    }
    fun getResult(winNumber: Int): Int {
       return value.sumOf { row ->
            row.filter { !it.isMarked }.sumOf { it.value }
        } * winNumber
    }
}
data class Card(val value: Int, var isMarked: Boolean = false)