fun main() {
    fun getNeighbours(i: Int, j:Int, rowSize: IntRange, colSize: IntRange):List<Pair<Int, Int>>{
        return listOf(i-1 to j , i to j-1, i to j+1, i+1 to j).filter { (i,j) ->
            i in rowSize && j in colSize
        }
    }

    fun part1(input: List<String>): Int {
        val matrix = input.getMatrix()
        val leastLocation = mutableListOf<Int>()
        for(i in matrix.indices){
            for(j in matrix[i].indices){
               if(matrix[i][j] < getNeighbours(i, j, matrix.indices, matrix[i].indices).minOf { (i,j) -> matrix[i][j] }) leastLocation.add(matrix[i][j])
            }
        }
        return leastLocation.sumOf { it + 1 }
    }

    fun part2(input: List<String>): Int {
        val matrix = input.getMatrix()
        val visitedMatrix = mutableListOf<Pair<Int, Int>>()
        fun getBasinSize(i: Int, j: Int): Int {
            return if(visitedMatrix.contains(i to j) || matrix[i][j] == 9) 0
            else {
                visitedMatrix.add(i to j)
                1 + getNeighbours(i,j, matrix.indices, matrix[0].indices).sumOf { (i,j) -> getBasinSize(i,j) }
            }
        }
        val basin = mutableListOf<Int>()
            for (i in matrix.indices) {
                for (j in matrix[i].indices) {
                    if(visitedMatrix.contains(i to j ) || matrix[i][j] == 9) continue
                    basin.add(getBasinSize(i,j))
                }
        }
        return basin.sortedDescending().subList(0,3).fold(1){acc, i ->  acc * i}
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

private fun List<String>.getMatrix(): List<List<Int>> {
    return this.map { input -> input.toList().map { it.digitToInt() } }
}
