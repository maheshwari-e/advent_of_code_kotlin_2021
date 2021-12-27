fun main() {
    fun getStopMovingSteps(seaCucumbers: MutableList<MutableList<Char>>): Int{
        var prevSeaCucumbers = seaCucumbers
        var move = 0
        var step = 0
        while(true){
            step++
            val currentSeaCucumbers = prevSeaCucumbers.copy()
            for(row in prevSeaCucumbers.indices){
                for(col in prevSeaCucumbers[row].indices){
                    if(prevSeaCucumbers[row][col] == '>' && prevSeaCucumbers[row][(col+1)%prevSeaCucumbers[row].size] == '.'){
                        move++
                        currentSeaCucumbers[row][(col+1)% currentSeaCucumbers[row].size] = prevSeaCucumbers[row][col]
                        currentSeaCucumbers[row][col] = '.'
                    }
                }
            }
            prevSeaCucumbers = currentSeaCucumbers.copy()
            for(row in currentSeaCucumbers.indices){
                for(col in currentSeaCucumbers[row].indices){
                    if(currentSeaCucumbers[row][col] == 'v' && currentSeaCucumbers[(row+1)%currentSeaCucumbers.size][col] == '.'){
                        move++
                        prevSeaCucumbers[(row+1)%prevSeaCucumbers.size][col] = currentSeaCucumbers[row][col]
                        prevSeaCucumbers[row][col] = '.'
                    }
                }
            }
            if(move == 0) return step else move = 0
        }
    }

    fun part1(input: List<String>): Int {
        val seaCucumbers = input.map { row ->  row.toMutableList() }.toMutableList()
        return getStopMovingSteps(seaCucumbers)
    }
    fun part2(input: List<String>): Int {
        return input.size
    }


// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day25_test")
    check(part1(testInput)  == 58)
    check(part2(testInput) == 9)

    val input = readInput("Day25")
    println(part1(input))
    println(part2(input))
}

private fun MutableList<MutableList<Char>>.copy() = this.map { it.toList().toMutableList() }.toMutableList()
