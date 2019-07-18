package GUI

import Logic.Field
import java.lang.StringBuilder

fun printField(field: Field) {

    fun space(len: Int): String {
        var stringBuilder = StringBuilder()

        for (i in 0 until len) stringBuilder.append(" ")

        return stringBuilder.toString()

    }

    print(space(4))

    for (i in 0 until field.maxWidth)
        print(" $i${space(4)}")

    println("\n")

    var rowNumber = 0

    for (row in field) {
        print("$rowNumber${space(3)}")
        if (rowNumber % 2 == 0) print(space(2))

        for (cell in row)
            print("$cell${space(5)}")

        println("\n")

        rowNumber++

    }

}