import kotlin.math.absoluteValue

fun main() {

    fun getScanners(input: List<String>): List<Set<Point3d>> {
        val scanners = mutableListOf<Set<Point3d>>()
        val beacons = mutableSetOf<Point3d>()
        input.map {
            when{
                it.startsWith("---") -> {}
                it.isNotEmpty() -> beacons.add(Point3d.format(it))
                else ->{
                    scanners.add(beacons.toSet())
                    beacons.clear()
                }
            }
        }
        return scanners.apply { add(beacons.toSet()) }
    }

    fun findIntersectBeacons(otherScanner: Set<Point3d>, scanner0: Set<Point3d>):Scanner? =
        (0 until 6).firstNotNullOfOrNull  { facing ->
            (0 until 4).firstNotNullOfOrNull  { direction ->
                val otherScannerOrientation = otherScanner.map { it.face(facing).rotation(direction) }.toSet()
                scanner0.firstNotNullOfOrNull { s0 ->
                    otherScannerOrientation.firstNotNullOfOrNull { s1 ->
                        val position = s0.minus(s1)
                        val relativePosition = otherScannerOrientation.map { it.plus(position) }.toSet()
                        if (relativePosition.intersect(scanner0).size >= 12) {
                            Scanner(position, relativePosition)
                        } else null
                    }
                }
            }
        }

    fun findCommonBeacons(scanners: List<Set<Point3d>>): Pair<MutableSet<Point3d>, MutableSet<Point3d>> {
        val scanner0 = scanners.first().toMutableSet()
        val positionScanners = mutableSetOf(Point3d(0,0,0))
        val unMappedScanner = ArrayDeque<Set<Point3d>>().apply{ addAll(scanners.drop(1))}
        while (unMappedScanner.isNotEmpty()){
            val currentScanner = unMappedScanner.removeFirst()
            when(val scanner = findIntersectBeacons(currentScanner, scanner0)){
                null -> unMappedScanner.add(currentScanner)
                else ->{
                    scanner0.addAll(scanner.beacons)
                    positionScanners.add(scanner.point)
                }
            }
        }
        return (positionScanners to scanner0)
    }

    fun part1(input: List<String>): Int {
        val scanners = getScanners(input)
        return findCommonBeacons(scanners).second.size
    }

    fun part2(input: List<String>): Int {
        val scanners = getScanners(input)
        return findCommonBeacons(scanners).first.combo().maxOf { it.first.manhattanDistance(it.second) }
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
    check(part1(testInput)  == 79)
    check(part2(testInput) == 3621)

    val input = readInput("Day19")
    println(part1(input))
    println(part2(input))
}

private fun  MutableSet<Point3d>.combo() = this.mapIndexed { index, a ->
    this.drop(index+1).map { b ->  a to b }
}.flatten()


data class Point3d(private val x:Int, private val y:Int, private val z:Int){

    fun manhattanDistance(point: Point3d) = (x - point.x).absoluteValue + (y - point.y).absoluteValue+(z -point.z).absoluteValue

    fun plus(point: Point3d) =  Point3d(x + point.x, y + point.y, z + point.z)

    fun minus(point: Point3d) = Point3d(x - point.x, y - point.y, z - point.z)

    fun face(facing: Int): Point3d = when(facing){
        0 -> this
        1 -> Point3d(x, -y, -z)
        2 -> Point3d(x, -z, y)
        3 -> Point3d(-y, -z, x)
        4 -> Point3d(y, -z, -x)
        5 -> Point3d(-x, -z, -y)
        else -> throw IllegalArgumentException("Invalid Face")
    }

    fun rotation(direction: Int): Point3d = when(direction){
        0 -> this
        1 -> Point3d(-y, x, z)
        2 -> Point3d(-x, -y, z)
        3 -> Point3d(y, -x, z)
        else -> throw IllegalArgumentException("Invalid Rotation")
    }

    override fun toString(): String {
        return "x -> $x, y -> $y, z-> $z"
    }
    companion object {
        fun format(point: String) = point.split(",").let {
            Point3d(it[0].trim().toInt(), it[1].trim().toInt(), it[2].trim().toInt())
        }
    }
}

data class Scanner(val point: Point3d, val beacons: Set<Point3d>)