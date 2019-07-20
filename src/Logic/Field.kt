package Logic

class Field(val cells: Array<Array<Cell>>) : Iterable<Array<Cell>> {


    val maxWidth: Int
    get() = cells.iterator().asSequence().maxBy { c -> c.size }!!.size

    val maxHeight: Int
    get()  = cells.size

    override fun iterator(): Iterator<Array<Cell>> = cells.iterator()

}