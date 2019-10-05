package de.melon.hexsweeper.logic

import java.util.concurrent.atomic.AtomicBoolean

class Timer(val game: Game) : Thread() {

    var running = AtomicBoolean(true)

    override fun run() {
        while (true) {
            while (running.get()) {
                sleep(1000)
                game.time.getAndAdd(1)
                game.gameRenderer.startRender()
            }
        }
    }

    fun startCounting() {
        running.getAndSet(true)
    }

    fun stopCounting() {
        running.getAndSet(false)
    }

}