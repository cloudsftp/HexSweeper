package Logic

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