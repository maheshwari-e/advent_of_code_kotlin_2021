fun main() {
    fun processInput(input: List<String>): Pair<List<Char>, List<List<Char>>> {
        val imageEnhancement = input.first().map { it }
        val ipImage = input.drop(2).map {row -> row.map { it } }
        return (imageEnhancement to ipImage)
    }

    fun imageEnhancementAlgorithms(imageEnhancement: List<Char>, ipImage: List<List<Char>>, times: Int): List<List<Char>> {
        var inputImage = ipImage
        var darkValue = '.'
        repeat(times){
            val infiniteIPImage = List(inputImage.size + 10 ){ MutableList(inputImage.size + 10) {darkValue}}
            (inputImage.indices).map{ row ->
                (inputImage[0].indices).map{ col -> infiniteIPImage[row+5][col+5] = inputImage[row][col] }
            }
            inputImage =  (infiniteIPImage.indices).map { row ->
                (infiniteIPImage[row].indices).map { col ->
                    val binary = StringBuilder()
                    for(i in row-1.. row+1){
                        for(j in col-1..col+1){
                            val value =  if(i in infiniteIPImage.indices  && j in infiniteIPImage[row].indices) infiniteIPImage[i][j] else darkValue
                            binary.append(value.let { if(value == '.') 0 else 1 })
                        }
                    }
                    imageEnhancement[binary.toString().toInt(2)]
                }
            }
            if(imageEnhancement[0] == '#') darkValue = if(darkValue == '.') '#' else '.'
        }
        return inputImage
    }

    fun part1(input: List<String>): Int {
        val (imageEnhancement, ipImage) = processInput(input)
        return imageEnhancementAlgorithms(imageEnhancement, ipImage, 2).flatten().count { it == '#' }
    }

    fun part2(input: List<String>): Int {
        val (imageEnhancement, ipImage) = processInput(input)
        return imageEnhancementAlgorithms(imageEnhancement, ipImage, 50).flatten().count { it == '#' }
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    check(part1(testInput)  == 35)
    check(part2(testInput) == 3351)

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}