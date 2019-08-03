package de.melon.hexsweeper.logic

class Cell(val bomb: Boolean, var state: CellState = CellState.closed) {

    constructor(): this(false, CellState.fake)

    var numOfBombs: Int = 0

    fun open(): Boolean {
        println("Opening Cell")

        if (bomb) {
            println("Game Over!")
            state = CellState.exploded
            return false

        }

        state = CellState.opened

        return true
    }

    fun toggleFlag() {
        if (state == CellState.closed)
            state = CellState.flagged

        else if (state == CellState.flagged)
            state = CellState.closed

    }

}
