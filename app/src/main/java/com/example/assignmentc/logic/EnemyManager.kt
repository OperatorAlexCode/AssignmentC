package com.example.assignmentc.logic

class EnemyManager(private val maze: Maze) {
    var enemies: MutableList<Enemy> = mutableListOf()

    fun spawnEnemies() {
        val nonWallOrPlayerTiles = maze.Tiles.flatten().filter { !it.IsWall && !it.isPlayerLocation }
        val spawnTiles = nonWallOrPlayerTiles.shuffled().take(3)

        for (tile in spawnTiles) {
            enemies.add(Enemy(tile))
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
    }
}