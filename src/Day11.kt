fun main() {
    fun getNeighbours(row: Int, col:Int, rowSize: IntRange, colSize: IntRange):List<Pair<Int, Int>>{
        val index = mutableListOf<Pair<Int, Int>>()
        for(i in row-1..row+1){
            for(j in  col-1..col+1){
                if((i != row || j != col) && i in rowSize && j in colSize) index.add(i to j)
            }
        }
        return index
    }


    fun findFlashes(octpus: List<MutableList<Int>>): List<MutableList<Int>> {
        fun findNeighbourFlashes(row: Int, col:Int){
            if(octpus[row][col] <= 9) return
            octpus[row][col] = 0
            return getNeighbours(row, col , octpus.indices, octpus[row].indices).forEach {
                if(octpus[it.first][it.second] != 0) octpus[it.first][it.second] += 1
                if(octpus[it.first][it.second] > 9) findNeighbourFlashes(it.first, it.second)
            }
        }

        octpus.forEachIndexed{index, value ->
            for(col in value.indices) octpus[index][col] += 1
        }
        for(i in octpus.indices){
            for(j in octpus[i].indices){
                if(octpus[i][j] > 9) findNeighbourFlashes(i, j)
            }
        }
        return octpus
    }

    fun part1(input: List<String>): Int {
        var octpus = input.map { it.toCharArray().map { char -> char.digitToInt() }.toMutableList()}
        var countFlashes = 0
        repeat(100){
            octpus = findFlashes(octpus)
            countFlashes += octpus.flatten().count { it == 0 }
        }
        return countFlashes
    }

    fun part2(input: List<String>): Int {
        var octpus = input.map { it.toCharArray().map { char -> char.digitToInt() }.toMutableList()}
        var step = 0
        while(octpus.flatten().any { it != 0 }){
            step++
            octpus = findFlashes(octpus)
        }
        return step
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}