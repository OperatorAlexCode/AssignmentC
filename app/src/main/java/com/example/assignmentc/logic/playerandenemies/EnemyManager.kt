package com.example.assignmentc.logic.playerandenemies

import android.content.Context
import android.media.MediaPlayer
import com.example.assignmentc.R
import com.example.assignmentc.logic.GameManager
import com.example.assignmentc.logic.other.Maze
import com.example.assignmentc.logic.other.Tile
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class EnemyManager(private var context: Context, private val maze: Maze, var gameManager: GameManager) {
    var enemies: MutableList<Enemy> = mutableListOf()
    var hitSfx = MediaPlayer.create(context,R.raw.hit)

    fun spawnEnemies(amount: Int) {
        val nonWallOrPlayerTiles = maze.Tiles.flatten().filter { tile ->
            var playerTile = gameManager.player?.currentTile

            var closeToPlayer = false

            playerTile?.let {
                var minX = max(it.XPos - 3,0)
                var maxX = min(it.XPos + 3,maze.Size-1)

                var minY = max(it.YPos - 3,0)
                var maxY = min(it.YPos + 3,maze.Size-1)

                closeToPlayer = tile.XPos in minX..maxX && tile.YPos in minY..maxY
            }

            !tile.IsWall && playerTile != tile && !closeToPlayer
        }

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
                    hitSfx.start()
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
                return abs(a.XPos - b.XPos) + abs(a.YPos - b.YPos)
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