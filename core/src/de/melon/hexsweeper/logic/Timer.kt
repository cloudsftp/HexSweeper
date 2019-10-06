package de.melon.hexsweeper.logic

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class Timer(val game: Game) : Thread() {

    private val running = AtomicBoolean(false)
    val time = AtomicInteger(0)

    override fun run() {
        while (true) {
            while (running.get()) {
                game.render()
                sleep(1000)
                time.getAndAdd(1)
            }
        }
    }

    fun startCounting() {
        time.set(0)
        running.set(true)
    }

    fun stopCounting() {
        running.set(false)
    }

}