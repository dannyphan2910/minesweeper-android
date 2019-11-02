package hu.ait.minesweeper.model

import android.util.Log
import hu.ait.minesweeper.ui.MinesweeperView
import java.util.*

object MinesweeperBoard {

    var DIMENSION = 5
    var MINES_NUM = 3

    lateinit var board: Array<Array<Field>>

    fun initializeBoard() {
        board = Array<Array<Field>>(DIMENSION) {
            Array<Field>(DIMENSION) {
                Field(FieldType.EMPTY, 0, false, false)}}

        placeMines()
        calculateMineDistance()
    }

    private fun placeMines() {
        if (MINES_NUM > 0) {
            for (i in 1..MINES_NUM) {
                var minePlaced = false

                while (!minePlaced) {
                    var xCoord = Random().nextInt(DIMENSION)
                    var yCoord = Random().nextInt(DIMENSION)

                    if (board[xCoord][yCoord].type == FieldType.EMPTY) {
                        board[xCoord][yCoord].type = FieldType.MINE
                        minePlaced = true
                    }
                }
            }
        }
    }

    private fun calculateMineDistance() {
        for (x in 0..(DIMENSION - 1)) {
            for (y in 0..(DIMENSION - 1)) {
                var field = board[x][y]

                field.minesAround = checkMinesNeighbors(x, y)
            }
        }
    }

    private fun checkMinesNeighbors(xCoord: Int, yCoord: Int): Int {
        var minesAround = 0

        for (neighbor in getNeighbors(xCoord, yCoord)) {
            if (neighbor != null && neighbor.type == FieldType.MINE) minesAround++
        }

        return minesAround
    }

    private fun getNeighbors(xCoord: Int, yCoord: Int): Array<Field?> {
        var topLeft = if (xCoord > 0 && yCoord > 0) board[xCoord - 1][yCoord - 1] else null
        var left = if (xCoord > 0) board[xCoord - 1][yCoord] else null
        var bottomLeft = if (xCoord > 0 && yCoord < DIMENSION - 1) board[xCoord - 1][yCoord + 1] else null
        var bottom = if (yCoord < DIMENSION - 1) board[xCoord][yCoord + 1] else null
        var bottomRight = if (xCoord < DIMENSION - 1 && yCoord < DIMENSION - 1) board[xCoord + 1][yCoord + 1] else null
        var right = if (xCoord < DIMENSION - 1) board[xCoord + 1][yCoord] else null
        var topRight = if (xCoord < DIMENSION - 1 && yCoord > 0) board[xCoord + 1][yCoord - 1] else null
        var top = if (yCoord > 0) board[xCoord][yCoord - 1] else null

        return arrayOf(topLeft, left, bottomLeft, bottom, bottomRight, right, topRight, top)
    }

    fun getField(xCoord: Int, yCoord: Int): Field? {
        if (xCoord >= 0 && xCoord < DIMENSION &&
                    yCoord >= 0 && yCoord < DIMENSION) {
            return board[xCoord][yCoord]
        }

        return null
    }

    fun hasLost(): Boolean {
        for (i in 0..(DIMENSION - 1)) {
            for (j in 0..(DIMENSION - 1)) {
                var field = board[i][j]

                if (field.isFlagged && field.type == FieldType.EMPTY || field.wasTouched && field.type == FieldType.MINE && !field.isFlagged) return true
            }
        }

        return false
    }

    fun hasWon(): Boolean {
        var mineCount = MINES_NUM

        for (i in 0..(DIMENSION - 1)) {
            for (j in 0..(DIMENSION - 1)) {
                var field = board[i][j]

                if (field.type == FieldType.MINE && !field.isFlagged) return false
                else if (field.type == FieldType.MINE && field.isFlagged) mineCount--
            }
        }

        return mineCount == 0
    }

    fun reset() {
        board = Array<Array<Field>>(DIMENSION) {
            Array<Field>(DIMENSION) {
                Field(FieldType.EMPTY, 0, false, false)}}
        initializeBoard()
    }

}