package de.melon.hexsweeper.logic

import kotlin.random.Random.Default.nextDouble

class Field(val cells: Array<Array<Cell>>) : Iterable<Array<Cell>> {

    constructor(n: Int, m:Int, prob: Double = 0.2) : this(buildArrayWithDim(n, m, prob))

    init {
        for (i in 0 until cells.size)
            for (j in 0 until cells[i].size)
                determineNumOfAdjacentBombs(i, j)

    }

    fun determineNumOfAdjacentBombs(i: Int, j: Int) {
        var numOfBombs = 0

        for ((I: Int, J: Int) in getAdjacentIndices(i, j))
            if (cells[I][J].bomb) numOfBombs++

        cells[i][j].numOfBombs = numOfBombs

    }

    val maxWidth: Int
    get() = cells.iterator().asSequence().maxBy { c -> c.size }!!.size

    val maxHeight: Int
    get()  = cells.size


    fun open(i: Int, j: Int): Boolean {
        if (cells[i][j].open()) {
            if (cells[i][j].numOfBombs == 0)
                for ((I, J) in getAdjacentIndices(i, j))
                    open(I, J)

            return true

        } else {
            return false

        }

    }

    fun toggleFlag(i: Int, j: Int) {
        cells[i][j].toggleFlag()

    }

    fun getAdjacentIndices(i: Int, j: Int): List<Pair<Int, Int>> {
        val list = mutableListOf<Pair<Int, Int>>()

        fun addIndicesSafe(i: Int, j: Int) {
            if (i > -1 && i < cells.size && j > -1 && j < cells[i].size)
                list.add(Pair(i, j))

        }

        val offset = i % 2

        // Two rows above
        addIndicesSafe(i - 2, j)

        // One row above
        addIndicesSafe(i - 1, j - offset)
        addIndicesSafe(i - 1, j - offset + 1)

        // One row beneath
        addIndicesSafe(i + 1, j - offset)
        addIndicesSafe(i + 1, j - offset + 1)

        // Two rows beneath
        addIndicesSafe(i + 2, j)

        return list

    }

    override fun iterator(): Iterator<Array<Cell>> = cells.iterator()

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