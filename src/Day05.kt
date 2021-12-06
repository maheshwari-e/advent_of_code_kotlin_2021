fun main() {


    fun getLinesRange(xy1: Point, xy2: Point): Int {
        return when{
             xy1.x == xy2.x -> {
                if (xy2.y > xy1.y)  xy2.y -xy1.y else  xy1.y - xy2.y
            }
            xy1.y == xy2.y -> {
                if (xy2.x > xy1.x)   xy2.x -xy1.x else  xy1.x - xy2.x
            }
            else -> if (xy1.x > xy2.x) xy1.x - xy2.x else  xy2.x - xy1.x
        }
    }

    fun getLinesValue(data: String): Line {
        val line = data.split(" -> ").let { (start, end) -> 
            Line(
                start =  start.split(',').let { (x, y) -> Point(x.toInt(), y.toInt()) },
                end =  end.split(',').let { (x, y) -> Point(x.toInt(), y.toInt()) }
            )
        }
        return line
    }

    fun getAxisDirection(xy1: Point, xy2: Point): Pair<Int, Int> {
        return Pair(
            when{
                xy1.x == xy2.x -> 0
                xy1.x > xy2.x -> -1
                else -> 1
            },
            when{
                xy1.y == xy2.y -> 0
                xy1.y > xy2.y -> -1
                else -> 1
            }
        )
    }

    fun part1(input: List<String>): Int {
        val linesData = mutableListOf<String>()
        input.forEach { data ->
            val line = getLinesValue(data)
            val range = getLinesRange(line.start, line.end)
            val (xDirection, yDirection) =  getAxisDirection(line.start, line.end)
            if( line.start.x == line.end.x ||  line.start.y == line.end.y) {
                var x = line.start.x
                var y = line.start.y
                for (i in 0..range) {
                     linesData.add("$x,$y")
                     x += xDirection
                     y += yDirection
                }
            }
        }
        return linesData.groupingBy { it }.eachCount().filter { it.value > 1 }.count()
    }


    fun part2(input: List<String>): Int {
        val linesData = mutableListOf<String>()
        input.forEach { data ->
            val line = getLinesValue(data)
            val range = getLinesRange(line.start, line.end)
            val (xDirection, yDirection) = getAxisDirection(line.start, line.end)
            var x = line.start.x
            var y = line.start.y
            for (i in 0..range) {
                linesData.add("$x,$y")
                x += xDirection
                y += yDirection
            }
        }
        return linesData.groupingBy { it }.eachCount().filter { it.value > 1 }.count()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

data class Point(val x: Int, val y:Int)
data class Line(val start: Point, val end: Point)
