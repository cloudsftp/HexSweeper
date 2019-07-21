package GUI

import Logic.*
import java.util.*

val scan = Scanner(System.`in`)

fun main() {
    val n = 5
    val m = 6

    val field = Field(n, m)

    while (true) periodic(field)

}

fun periodic(field: Field) {

    printField(field)

    print("Zeile: ")
    val i = scan.nextLine().trim().toInt()

    print("Spalte: ")
    val j = scan.nextLine().trim().toInt()

}