package com.example.assignmentc.logic

import android.content.Context

class EnemyManager(private var context: Context,private val maze: Maze, var gameManager: GameManager) {
    var enemies: MutableList<Enemy> = mutableListOf()

    fun spawnEnemies(amount: Int) {
        val nonWallOrPlayerTiles = maze.Tiles.flatten().filter { !it.IsWall && gameManager.player?.currentTile != it }
        val spawnTiles = nonWallOrPlayerTiles.shuffled().take(amount)

        for (tile in spawnTiles) {
            enemies.add(Enemy(context, tile))
        }
    }

    fun addEnemy(enemy: Enemy) {
        enemies.add(enemy)
    }

    fun removeEnemy(enemy: Enemy) {
        enemies.remove(enemy)
    }

    fun getLocationTile(enemy: Enemy): Tile {
        return enemy.currentTile
    }

    fun moveAllEnemies() {
        val playerTile = gameManager.player?.currentTile ?: return

        // Snapshot of occupied tiles before movement
        val occupiedTiles = enemies.map { it.currentTile }.toSet()

        for (enemy in enemies) {
            enemy.move(
                playerTile,
                isTileOccupied = { tile ->
                    tile != enemy.currentTile && occupiedTiles.contains(tile)
                },
                onHitPlayer = {
                    gameManager.damagePlayer(1)
                }
            )
        }
    }

    data class Node(val tile: Tile, val parent: Node?, val g: Int, val h: Int) {
        val f: Int get() = g + h
    }

    companion object {
        fun findPath(start: Tile, goal: Tile): List<Tile>? {
            val openList = mutableListOf<Node>()
            val closedSet = mutableSetOf<Tile>()

            fun heuristic(a: Tile, b: Tile): Int {
                return kotlin.math.abs(a.XPos - b.XPos) + kotlin.math.abs(a.YPos - b.YPos)
            }

            openList.add(Node(start, null, 0, heuristic(start, goal)))

            while (openList.isNotEmpty()) {
                val current = openList.minByOrNull { it.f }!!
                if (current.tile == goal) {
                    val path = mutableListOf<Tile>()
                    var node: Node? = current
                    while (node != null) {
                        path.add(0, node.tile)
                        node = node.parent
                    }
                    return path
                }

                openList.remove(current)
                closedSet.add(current.tile)

                val neighbors = listOfNotNull(
                    current.tile.NorthTile,
                    current.tile.SouthTile,
                    current.tile.EastTile,
                    current.tile.WestTile
                ).filter { !it.IsWall && !closedSet.contains(it) }

                for (neighbor in neighbors) {
                    val g = current.g + 1
                    val h = heuristic(neighbor, goal)
                    val existing = openList.find { it.tile == neighbor }

                    if (existing == null || g < existing.g) {
                        openList.remove(existing)
                        openList.add(Node(neighbor, current, g, h))
                    }
                }
            }

            return null // if no path is found
        }
    }
}