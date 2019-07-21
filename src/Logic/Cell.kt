package Logic

import kotlin.system.exitProcess

class Cell(val bomb: Boolean, var state: CellState = CellState.closed) {

    constructor(): this(false, CellState.fake)

    var numOfBombs: Int = 0

    fun open() {
        if (state != CellState.flagged) {
            println("Opening Cell")

            if (bomb) {
                println("Game Over!")
                exitProcess(0)

            }

            state = CellState.opened

        } else {
            println("Cannot open flagged Cell")

        }

    }

    override fun toString(): String {
        return when (state) {
            CellState.fake -> " "
            CellState.opened -> if (numOfBombs == 0) "." else "$numOfBombs"
            CellState.closed -> "?"
            CellState.flagged -> "x"
        }
    }

}

enum class CellState {
    closed, opened, flagged, fake
}