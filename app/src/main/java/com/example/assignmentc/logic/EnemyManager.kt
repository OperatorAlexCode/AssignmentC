package com.example.assignmentc.logic

import android.content.Context

class EnemyManager(private var context: Context,private val maze: Maze, var player: Player?) {
    var enemies: MutableList<Enemy> = mutableListOf()

    fun spawnEnemies() {
        val nonWallOrPlayerTiles = maze.Tiles.flatten().filter { !it.IsWall && player?.currentTile != it }
        val spawnTiles = nonWallOrPlayerTiles.shuffled().take(3)

        for (tile in spawnTiles) {
            enemies.add(Enemy(context,tile))
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
        for (enemy in enemies) {
            enemy.move()
        }
    }

    fun moveEnemy() {
        //TODO - Do we need to move a single enemy or only all at same time?
        //TODO - It's the simplest option to move all at a time | Alex
    }
}