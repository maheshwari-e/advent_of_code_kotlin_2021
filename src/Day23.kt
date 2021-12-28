import kotlin.math.abs

fun main() {
    val amphipodRoom = mapOf( 2 to 'A', 4 to 'B', 6 to 'C', 8 to 'D')

    fun getPossiblePosition(burrow: Burrow) = buildList {
        fun pathExists(hallway: Int, room: Int): Boolean {
            val min = minOf(hallway, room)
            val max = maxOf(hallway, room)
            return burrow.hallway.all { (index, value) -> index !in (min + 1) until max || value == '.' }
        }
        for((hIndex, hValue) in burrow.hallway){
            for((rIndex, rValue) in burrow.rooms) {

                if (hValue == '.' && rValue.isEmpty()) continue

                if (hValue != '.') {
                    if (hValue == amphipodRoom[rIndex] && rValue.all { it == hValue } && pathExists(hIndex, rIndex)) {
                        add(hIndex to rIndex)
                    }
                    continue
                }

                if (pathExists(rIndex, hIndex) && rValue.any { it != amphipodRoom[rIndex] }) { add(rIndex to hIndex) }
            }
        }
    }

    fun moveRoomToHallway(burrow: Burrow, roomPos: Int, hallwayPos: Int): Pair<Burrow, Int> {
        val occupiedAmphipods = burrow.rooms.getValue(roomPos)
        val amphipod = occupiedAmphipods.first()
        val steps = burrow.roomSize - occupiedAmphipods.size + abs(roomPos - hallwayPos) +  1
        val cost = steps * Energy.valueOf(amphipod.toString()).energy

        return burrow.copy(rooms =  burrow.rooms.plus(roomPos to occupiedAmphipods.drop(1)),
            hallway = burrow.hallway.plus(hallwayPos to amphipod )) to cost
    }

    fun moveHallwayToRoom(burrow: Burrow, hallwayPos: Int, roomPos: Int): Pair<Burrow, Int> {
        val occupiedAmphipods = burrow.rooms.getValue(roomPos)
        val amphipod = burrow.hallway.getValue(hallwayPos)
        val steps = burrow.roomSize - occupiedAmphipods.size + abs(roomPos - hallwayPos)
        val cost = steps * Energy.valueOf(amphipod.toString()).energy

        return burrow.copy(rooms =  burrow.rooms.plus(roomPos to listOf(amphipod)+ occupiedAmphipods),
            hallway = burrow.hallway.plus(hallwayPos to  '.' )) to cost

    }

    fun evaluate(burrow: Burrow, position: Pair<Int, Int>): Pair<Burrow, Int> {
        return if(position.first in amphipodRoom.keys) moveRoomToHallway(burrow, position.first, position.second)
        else moveHallwayToRoom(burrow, position.first, position.second)
    }

    fun evaluateBurrow(
        burrow: Burrow,
        position: Pair<Int, Int>,
        cost: Int,
        bestCost: Int,
        visited: MutableMap<Burrow, Int>
    ): Int {
        var energyCost =  bestCost
        if (cost >= energyCost) return energyCost
        val (newBurrow, newCost) = evaluate(burrow, position)
        val totalCost = cost + newCost
        if (newBurrow in visited) { if (visited.getValue(newBurrow) <= totalCost) return energyCost }
        visited[newBurrow] = totalCost
        if (amphipodRoom.all { amphipod ->
                val amphipods = newBurrow.rooms.getValue(amphipod.key)
                amphipods.size == newBurrow.roomSize && amphipods.all { it == amphipod.value }
            }) {
            if (totalCost < bestCost) energyCost = totalCost
            return totalCost
        }
        val newPosition = getPossiblePosition(newBurrow)
        if(newPosition.isEmpty()) return energyCost
        return newPosition.minOf { evaluateBurrow(newBurrow, it, totalCost, energyCost, visited) }
    }

    fun organizeAmphipods(input: List<String>, roomSize: Int, bestCost: Int): Int {
        val rooms = input.map { line -> line.filter { char -> char in 'A'..'D' }  }.filter { it.isNotEmpty() }
        val amphipods = (0..3).map { index -> rooms.map { it[index] }  }
        val visited = mutableMapOf<Burrow, Int>()
        val burrow = Burrow(roomSize = roomSize, rooms = (1..4).associate { it * 2 to amphipods[it - 1] })
        return getPossiblePosition(burrow).minOf { evaluateBurrow(burrow, it, 0, bestCost, visited)}
    }

    fun part1(input: List<String>): Int {
        return organizeAmphipods(input, 2, Int.MAX_VALUE)
    }

    fun part2(input: List<String>): Int {
        val newInput = input.take(3).toMutableList().apply {
            add("  #D#C#B#A#")
            add("  #D#B#A#C#")
        } + input.takeLast(2).toList()
        return organizeAmphipods(newInput, 4, Int.MAX_VALUE)
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day23_test")
    check(part1(testInput)  == 12521)
    check(part2(testInput) == 44169)

    val input = readInput("Day23")
    println(part1(input))
    println(part2(input))
}

data class Burrow(
    val roomSize: Int,
    val hallway: Map<Int, Char> = mapOf( 0 to '.', 1 to '.', 3 to '.', 5 to '.', 7 to '.', 9 to '.', 10 to '.' ),
    val rooms: Map<Int, List<Char>>
)

enum class Energy(val energy: Int) {
    A(1),
    B(10),
    C(100),
    D(1000)
}