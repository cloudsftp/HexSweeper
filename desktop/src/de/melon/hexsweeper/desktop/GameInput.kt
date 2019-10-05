package de.melon.hexsweeper.desktop

import com.badlogic.gdx.InputProcessor
import de.melon.hexsweeper.GUI.GameRenderer

class GameInput(val gameRenderer: GameRenderer) : InputProcessor {

    internal val numberOfTicksLongClick = 3
    internal var ticks = 0

    override fun touchDown(p0: Int, p1: Int, p2: Int, p3: Int): Boolean {
        gameRenderer.processTouchDown(p0.toFloat(), p1.toFloat())
        ticks = 0
        return true

    }

    override fun touchDragged(p0: Int, p1: Int, p2: Int): Boolean {
        if (ticks++ < numberOfTicksLongClick) return true
        gameRenderer.processDrag(p0.toFloat(), p1.toFloat())
        return true

    }

    override fun touchUp(p0: Int, p1: Int, p2: Int, p3: Int): Boolean {
        gameRenderer.processTouchUp(p0.toFloat(), p1.toFloat(), p3)
        return true

    }

    override fun scrolled(p0: Int): Boolean {
        gameRenderer.processScroll(p0)
        return true

    }


    // useless

    override fun mouseMoved(p0: Int, p1: Int): Boolean {
        return false
    }

    override fun keyTyped(p0: Char): Boolean {
        return false
    }

    override fun keyDown(p0: Int): Boolean {
        return false
    }

    override fun keyUp(p0: Int): Boolean {
        return false
    }

}