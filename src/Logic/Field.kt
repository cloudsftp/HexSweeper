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

class Field(val cells: Array<Array<Cell>>) : Iterable<Array<Cell>> {

    val maxWidth: Int
    get() {
        var max = 0

        for (row in this)
            if (max < row.size)
                max = row.size

        return max

    }

    override fun iterator(): Iterator<Array<Cell>> = cells.iterator()

}