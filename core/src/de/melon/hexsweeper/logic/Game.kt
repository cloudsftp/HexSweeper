package de.melon.hexsweeper.logic

import java.util.concurrent.atomic.AtomicInteger
import kotlin.properties.Delegates

class Game(val n: Int, val m: Int) {

    var timer = Timer(this)
    var time = AtomicInteger(0)

    internal var state: GameState by Delegates.observable(GameState.idle) {
        prop, old, new ->
        if (new != GameState.running) {
            timer.stopCounting()
        } else {
            time = AtomicInteger(0)
            timer = Timer(this)
            timer.start()
        }
    }

    internal var field = Field(n, m)

    fun start(i: Int, j: Int) {

        do buildField() while (field.cells[i][j].bomb)
        state = GameState.running

    }

    fun processOpen(i: Int, j: Int) {

        processClick(i, j)

        if (state == GameState.running) {
            if (!field.open(i, j)) {
                state = GameState.loose

            }

            checkForWin()

        }

    }

    fun processFlag(i: Int, j: Int) {

        processClick(i, j)

        if (state == GameState.running) {

            field.toggleFlag(i, j)

            checkForWin()

        }

    }

    fun processClick(i: Int, j: Int) {
        if (state == GameState.win || state == GameState.loose) {
            buildField()
            state = GameState.idle

        } else if (state == GameState.idle) {
            start(i, j)

        }

    }

    fun buildField() { field = Field(n, m) }

    fun checkForWin() {
        var win = true

        for (row in field)
            for (cell in row)
                win = win
                        && ((cell.bomb && cell.state == CellState.flagged)
                        || (!cell.bomb && cell.state == CellState.opened))

        if (win) {
            state = GameState.win
        }

    }

}