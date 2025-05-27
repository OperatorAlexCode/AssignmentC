package com.example.assignmentc.logic

import kotlin.random.Random

class TrapManager(
    private val maze: Maze,
    private val spawnLifetime: Int = 8,   // turns before an unpicked trap expires
    private val holdLifetime: Int = 5,    // turns the player can hold a trap
    private val dropLifetime: Int = 5     // turns a dropped trap remains active
) {
    private var activeTrap: Trap? = null
    private var heldTurnsRemaining: Int = 0

    /** Spawn a new trap on a random free tile, if none on ground or in hand. */
    fun spawnRandomTrap() {
        if (activeTrap != null || heldTurnsRemaining > 0) return

        val freeTiles = maze.allTiles()
            .filter { !it.isWall && !it.hasPlayer && !it.hasEnemy }
        if (freeTiles.isEmpty()) return

        val tile = freeTiles.random()
        activeTrap = Trap(tile, spawnLifetime)
    }

    /** Advance all trap timers by one turn. */
    fun tickAll() {
        // ground‐trap expiration
        activeTrap?.let { trap ->
            trap.remainingTurns--
            if (trap.remainingTurns <= 0) {
                activeTrap = null
            }
        }
        // held‐trap expiration
        if (heldTurnsRemaining > 0) {
            heldTurnsRemaining--
            if (heldTurnsRemaining == 0) {
                // the trap vanishes from the player’s inventory
            }
        }
    }

    /** If the player steps on the trap’s tile, pick it up. */
    fun tryPickUp(at: Tile): Boolean {
        val trap = activeTrap ?: return false
        return if (trap.tile == at) {
            activeTrap = null
            heldTurnsRemaining = holdLifetime
            true
        } else {
            false
        }
    }

    /** Drop the held trap onto the given tile. */
    fun drop(at: Tile) {
        if (heldTurnsRemaining > 0) {
            activeTrap = Trap(at, dropLifetime)
            heldTurnsRemaining = 0
        }
    }

    /** Check if a trap is lying on that tile. */
    fun isOnGround(at: Tile): Boolean {
        return activeTrap?.tile == at
    }

    /** Check if the player currently holds a trap. */
    fun isHeld(): Boolean {
        return heldTurnsRemaining > 0
    }

    fun getGroundTrapTile(): Tile? {
        return activeTrap?.tile
    }

}
