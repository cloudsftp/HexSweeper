package GUIgdx

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Main : ApplicationListener {

    internal lateinit var batch: SpriteBatch
    internal lateinit var hexagon: Texture

    override fun create() {
        batch = SpriteBatch()
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()

        hexagon = Texture("res/hexagonsmall.png")

        batch.draw(hexagon, 1f, 1f)

        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        hexagon.dispose()
    }

    override fun pause() {

    }

    override fun resize(p0: Int, p1: Int) {

    }

    override fun resume() {

    }

}
