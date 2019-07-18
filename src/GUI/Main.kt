package GUI

import Logic.*
import kotlin.random.Random.Default.nextBoolean

fun main() {
    val n = 5
    val m = 6



    val array = arrayOf(
        arrayOf(Cell(false), Cell(true)),
        arrayOf(Cell(false), Cell(false), Cell(true)),
        arrayOf(Cell(true), Cell(false)))

    val field = Field(array)

    field.cells[1][1].open()

    printField(field)

}