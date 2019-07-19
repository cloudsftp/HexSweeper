package Logic

import kotlin.system.exitProcess

class Cell(private val bomb: Boolean, private var state: CellState = CellState.closed) {

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
            CellState.opened -> " "
            CellState.closed -> "?"
            CellState.flagged -> ">"
        }
    }

}

enum class CellState {
    closed, opened, flagged
}