package de.melon.hexsweeper.logic

import java.util.concurrent.atomic.AtomicBoolean

class Timer(val game: Game) : Thread() {

    var running = AtomicBoolean(true)

    override fun run() {
        while (running.get()) {
            sleep(1000)
            game.time.getAndAdd(1)
        }
    }

    fun stopCounting() {
        running.getAndSet(false)
    }

}