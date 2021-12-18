import kotlin.math.abs
import kotlin.math.sign

fun main() {
    fun getVelocities(targetArea: TargetArea): List<Velocity> {
        val velocities = mutableListOf<Velocity>()
        (0..targetArea.xRange.last).forEach { x->
            (targetArea.yRange.first..abs(targetArea.yRange.first)).forEach { y->
                if(Probe(Velocity(x, y), targetArea).launch()) velocities += Velocity(x, y)
            }
        }
        return velocities
    }

    fun part1(input: List<String>): Int {
        val targetArea= input.first().getTargetArea()
        val velocities: List<Velocity> = getVelocities(targetArea)
        return velocities.maxOf { (it.y)*(it.y+1)/2 }
    }

    fun part2(input: List<String>): Int {
        val targetArea= input.first().getTargetArea()
        val velocities: List<Velocity> = getVelocities(targetArea)
        return velocities.size
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput)  == 45)
    check(part2(testInput) == 112)

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}

private fun String.getTargetArea():TargetArea {
    val target = mutableListOf<Int>()
    this.substringAfter(':').trim().split(',').let { (x, y) ->
        x.split('=')[1].trim().split("..").map { target.add(it.toInt()) }
        y.split('=')[1].trim().split("..").map { target.add(it.toInt()) }
    }
    return TargetArea(xRange = target[0]..target[1], yRange = target[2]..target[3])
}

data class TargetArea(val xRange: IntRange, val yRange: IntRange)
data class Velocity(val x:Int, val y:Int)
class Probe(private var velocity: Velocity, private val targetArea: TargetArea){
    private var x: Int= 0
    private var y: Int= 0
    fun launch(): Boolean {
        do {
            x += velocity.x
            y += velocity.y
            velocity = velocity.copy(x = velocity.x - velocity.x.sign, y = velocity.y -  1)
            if(checkProbeHit()) return true
            val probeMissTarget= checkMissTarget()
        }while(!probeMissTarget)
        return false
    }

    private fun checkMissTarget(): Boolean = x > targetArea.xRange.last || y < targetArea.yRange.first
    private fun checkProbeHit(): Boolean =  (x in targetArea.xRange && y in targetArea.yRange)

}
