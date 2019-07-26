package Logic

import kotlin.system.exitProcess

class Cell(val bomb: Boolean, var state: CellState = CellState.closed) {

    constructor(): this(false, CellState.fake)

    var numOfBombs: Int = 0

    fun open(): Boolean {
        if (state != CellState.closed) {
            println("Cannot open Cell")

        } else {
            println("Opening Cell")

            if (bomb) {
                println("Game Over!")
                exitProcess(0)

            }

            state = CellState.opened

            return true

        }

        return false

    }

    override fun toString(): String {
        return when (state) {
            CellState.fake -> " "
            CellState.opened -> if (numOfBombs == 0) "." else "$numOfBombs"
            CellState.closed -> "?"
            CellState.flagged -> "!"
        }
    }

}

enum class CellState {
    closed, opened, flagged, fake
}