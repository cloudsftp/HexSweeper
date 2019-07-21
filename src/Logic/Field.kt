package Logic

import kotlin.random.Random.Default.nextDouble

class Field(val cells: Array<Array<Cell>>) : Iterable<Array<Cell>> {

    init {
        for (i in 0 until cells.size)
            for (j in 0 until cells[i].size)
                determineNumOfBombs(i, j)

    }

    val maxWidth: Int
    get() = cells.iterator().asSequence().maxBy { c -> c.size }!!.size

    val maxHeight: Int
    get()  = cells.size

    override fun iterator(): Iterator<Array<Cell>> = cells.iterator()


    fun open(i: Int, j: Int) {

        cells[i][j].open()

    }


    fun determineNumOfBombs(i: Int, j: Int) {

        fun isCellBombSafe(i: Int, j: Int): Boolean
                =  i > -1 && i < cells.size          // checks i
                && j > -1 && j < cells[i].size       // checks j
                && cells[i][j].bomb

        var numOfBombs = 0

        fun checkCellForBomb(i: Int, j: Int) = if (isCellBombSafe(i, j)) numOfBombs++
                else numOfBombs

        // Offset for row above and row beneath
        val offset = i % 2

        // One row above
        checkCellForBomb(i - 1, j - offset)
        checkCellForBomb(i - 1, j - offset + 1)

        // Same row
        checkCellForBomb(i, j - 1)
        checkCellForBomb(i, j + 1)

        // One row beneath
        checkCellForBomb(i + 1, j - offset)
        checkCellForBomb(i + 1, j - offset + 1)

        cells[i][j].numOfBombs = numOfBombs

    }

    constructor(n: Int, m:Int, prob: Double) : this(buildArrayWithDim(n, m, prob))
    constructor(n: Int, m: Int) : this(n, m, 0.2)

    companion object {

        private fun buildArrayWithDim(n: Int, m: Int, prob: Double): Array<Array<Cell>> {

            val list = mutableListOf<Array<Cell>>()

            for (i in 0 until n) {
                val rowList = mutableListOf<Cell>()

                for (j in 0 until m + (i % 2) - 1)
                    rowList.add(Cell(nextDouble(1.0) < prob))

                list.add(rowList.toTypedArray())

            }

            return list.toTypedArray()

        }

    }

}