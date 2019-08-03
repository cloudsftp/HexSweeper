package de.melon.hexsweeper.logic

import de.melon.hexsweeper.GUI.log

class Game {

    var n = 0
    var m = 0

    constructor(n: Int, m: Int) {

        this.n = n
        this.m = m

        start(n, m)

    }

    fun start(n: Int, m: Int) {

        field = Field(n, m)
        state = GameState.running

    }

    lateinit var field: Field
    var state = GameState.running

    fun processOpen(i: Int, j: Int) {

        if (state == GameState.running) {

            if (!field.open(i, j)) {
                state = GameState.end
                log("Game Over!")
            }

            checkForWin()

        } else {

            start(n, m)
            log("Started a new game ($n x $m)!")

        }

    }

    fun processFlag(i: Int, j: Int) {

        if (state == GameState.running) {

            field.toggleFlag(i, j)

            checkForWin()

        } else {

            start(n, m)

        }

    }

    fun checkForWin() {
        var win = true

        for (row in field)
            for (cell in row)
                win = win
                        && ((cell.bomb && cell.state == CellState.flagged)
                        || (!cell.bomb && cell.state == CellState.opened))

        if (win) {
            state = GameState.end
            log("You Win!")
        }

    }

}