package com.example.assignmentc.logic

import android.content.Context

class EnemyManager(var context: Context, private val maze: Maze, private val trapManager: TrapManager) {
    var enemies: MutableList<Enemy> = mutableListOf()

    fun spawnEnemies() {
        val nonWallOrPlayerTiles = maze.Tiles.flatten().filter { !it.IsWall && !it.isPlayerLocation }
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
        // Iterate over a snapshot so we can safely remove enemies as we go
        for (enemy in enemies.toList()) {
            // 1. Move the enemy
            enemy.move()

            // 2. If it steps on a trap, kill it and clear the ground trap
            if (trapManager.isOnGround(enemy.currentTile)) {
                enemies.remove(enemy)
                trapManager.clearGroundTrap()  // make sure you’ve added this to TrapManager
            }
        }
    }

    fun moveEnemy() {
        //TODO - Do we need to move a single enemy or only all at same time?
        //TODO - It's the simplest option to move all at a time | Alex
    }
}