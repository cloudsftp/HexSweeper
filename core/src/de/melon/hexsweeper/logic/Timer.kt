package de.melon.hexsweeper.logic

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class Timer(val game: Game) : Thread() {

    val running = AtomicBoolean(true)
    val time = AtomicInteger(0)

    override fun run() {
        while (true) {
            while (running.get()) {
                sleep(1000)
                time.getAndAdd(1)
                game.render()
            }
        }
    }

    fun startCounting() {
        time.getAndSet(0)
        running.getAndSet(true)
    }

    fun stopCounting() {
        running.getAndSet(false)
    }

}