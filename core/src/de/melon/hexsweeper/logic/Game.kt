package de.melon.hexsweeper.logic

class Game(val n: Int, val m: Int) {

    fun start(n: Int, m: Int) {

        field = Field(n, m)
        state = GameState.running

    }

    internal var field = Field(n, m)
    internal var state = GameState.idle

    fun processOpen(i: Int, j: Int) {

        if (state == GameState.running) {

            if (!field.open(i, j)) {
                state = GameState.loose

            }

            checkForWin()

        } else {

            start(n, m)

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
            state = GameState.win
        }

    }

}