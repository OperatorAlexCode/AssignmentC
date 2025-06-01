package com.example.assignmentc.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MazePickerViewModel : ViewModel() {
    var currentMazeIndex by mutableIntStateOf(0)
        private set

    fun nextMaze(totalMazes: Int) {
        currentMazeIndex = (currentMazeIndex + 1) % totalMazes
    }

    fun prevMaze(totalMazes: Int) {
        currentMazeIndex = if (currentMazeIndex == 0) totalMazes - 1 else currentMazeIndex - 1
    }
}