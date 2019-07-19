package Logic

import kotlin.system.exitProcess

class Cell(private val bomb: Boolean, private var marked: Boolean = false, private var opened :Boolean = false) {

    fun open() {
        println("Opening Cell")

        if (bomb) {
            println("Game Over!")
            exitProcess(0)

        }

        opened = true

    }

    override fun toString(): String {
        return when {
            opened -> "+"
            marked -> "-"
            else -> "?"
        }
    }

}