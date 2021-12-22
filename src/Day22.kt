fun main() {
    val inputRegex = """(on|off) x=(-?\d+)..(-?\d+),y=(-?\d+)..(-?\d+),z=(-?\d+)..(-?\d+)""".toRegex()

    fun getCuboid(input:List<String>): List<Cube> {
        return input.map {
            val (onStatus, minX,maxX, minY,maxY, minZ, maxZ) = inputRegex
                .matchEntire(it)?.destructured?: throw IllegalArgumentException("Incorrect input line $it")
            Cube(onStatus, IntRange(minX.toInt(), maxX.toInt()),IntRange(minY.toInt(), maxY.toInt()),IntRange(minZ.toInt(), maxZ.toInt()))
        }
    }

    fun totalOnCubes(steps: List<Cube>): Long {
        val cuboids = mutableMapOf<Cube, Int>()
        steps.map { step ->
            val overlap = mutableMapOf<Cube, Int>()
            for((cube, count) in cuboids){
                val x = IntRange(maxOf(step.x.first, cube.x.first) , minOf(step.x.last, cube.x.last))
                val y = IntRange(maxOf(step.y.first, cube.y.first) , minOf(step.y.last, cube.y.last))
                val z = IntRange(maxOf(step.z.first, cube.z.first) , minOf(step.z.last, cube.z.last))
                if(x.first <= x.last && y.first <= y.last && z.first <= z.last){
                    overlap.merge(Cube(step.status, x, y, z), -count) { a, b -> a+b}
                }
            }
            if(step.status == "on") overlap.merge(step, 1){a,b -> a+b }
            overlap.map { cuboids.merge(it.key, it.value) { a,b -> a+b} }
        }
        return  cuboids.entries.sumOf { (it.key.x.toList().size.toLong() * it.key.y.toList().size * it.key.z.toList().size) * it.value}
    }

    fun part1(input: List<String>): Long {
        val areaRange = IntRange(-50, 50)
        val steps = getCuboid(input).filter { cube ->
            listOf(cube.x.first, cube.x.last, cube.y.first, cube.y.last, cube.z.first, cube.z.last).all { it in areaRange }
        }
        return totalOnCubes(steps)
    }

    fun part2(input: List<String>): Long {
        val steps = getCuboid(input)
        return totalOnCubes(steps)
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")
    check(part1(testInput)  == 590784L)
    check(part2(testInput) == 2758514936282235L)

    val input = readInput("Day22")
    println(part1(input))
    println(part2(input))
}

data class Cube(val status:String,val x:IntRange, val y:IntRange,val z:IntRange)