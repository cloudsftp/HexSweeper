package Logic

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

    fun determineNumOfBombs(i: Int, j: Int) {

        fun isCellBombSafe(i: Int, j: Int): Boolean
                =  i > -1 && i < cells.size          // checks i
                && j > -1 && j < cells[i].size       // checks j
                && cells[i][j].bomb

        var numOfBombs = 0

        fun checkCellForBomb(i: Int, j: Int) { if (isCellBombSafe(i, j)) numOfBombs++ }

        // Offset for row above and row beneath
        val offset = i % 2

        // Two rows above
        checkCellForBomb(i - 2, j)

        // One row above
        checkCellForBomb(i - 1, j - offset)
        checkCellForBomb(i - 1, j - offset + 1)

        // Same row
        checkCellForBomb(i, j -1)
        checkCellForBomb(i, j + 1)

        // One row beneath
        checkCellForBomb(i + 1, j - offset)
        checkCellForBomb(i + 1, j - offset + 1)

        // Two rows beneath
        checkCellForBomb(i + 2, j)

        cells[i][j].numOfBombs = numOfBombs

    }

}