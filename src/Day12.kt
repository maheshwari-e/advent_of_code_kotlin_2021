fun main() {
    fun findPath(graph: Graph, startNode: String, endNode: String, solution2: Boolean = false): Int {
        val queue = ArrayDeque<Pair<String,Map<String, Int>>>()
        queue.add(Pair(startNode, mapOf(startNode to 1)))
        var  count = 0
        while(queue.isNotEmpty()){
            val (currentNode,visited)  = queue.removeFirst()
            if(currentNode == endNode) count++
            else {
                graph.adjacencyMap[currentNode]?.forEach {
                    when {
                        it !in visited.keys  -> {
                            if(it == it.lowercase()) queue.add(it to (visited + mapOf(it to 1))) else queue.add(it to visited)
                        }
                        !visited.containsValue(2) && it != startNode && solution2 -> queue.add(it to visited.toMutableMap().apply { put(it, visited[it]!!+1)})
                    }
                }
            }
        }
        return count
    }

    fun part1(input: List<String>): Int {
        val graph = Graph()
        input.map { it.split('-').let { (src, dest) -> graph.addEdge(src, dest) } }
        return findPath(graph, "start", "end")
    }

    fun part2(input: List<String>): Int {
        val graph = Graph()
        input.map { it.split('-').let { (src, dest) -> graph.addEdge(src, dest) } }
        return findPath(graph, "start", "end", true)
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 226)
    check(part2(testInput) == 3509)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}

class Graph {
    val adjacencyMap = mutableMapOf<String, MutableList<String>>()
    fun addEdge(srcVertex: String, destVertex: String){
        adjacencyMap.computeIfAbsent(srcVertex) { mutableListOf() }.add(destVertex)
        adjacencyMap.computeIfAbsent(destVertex){ mutableListOf() }.add(srcVertex)
    }
    override fun toString(): String = StringBuffer().apply {
        for (key in adjacencyMap.keys) {
            append("$key -> ")
            append(adjacencyMap[key]?.joinToString(", ", "[", "]\n"))
        }
    }.toString()
}