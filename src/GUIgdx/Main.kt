package GUIgdx

import Logic.Field
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Main : ApplicationListener {

    internal lateinit var batch: SpriteBatch
    internal lateinit var hexagonFileHandle: FileHandle

    override fun create() {
        batch = SpriteBatch()

        hexagonFileHandle = Gdx.files.internal("res/hexagonsmall.png")
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()

        batch.draw(fieldTexture(), 0f, 0f)

        batch.end()
    }

    private fun fieldTexture(): Texture {
        val pixmap = Pixmap(500, 500, Pixmap.Format.RGB565)
        pixmap.setColor(Color.WHITE)
        pixmap.fill()

        val field = Field(3, 3)

        var offsetX: Int
        val offsetY = 30

        val hexagonPixmap = Pixmap(hexagonFileHandle)

        for (i in 0 until field.cells.size) {
            offsetX = if (i % 2 == 0) 75 else 0

            for (j in 0 until field.cells[i].size)
                pixmap.drawPixmap(hexagonPixmap, j * 150 + offsetX, i * 45 + offsetY)

        }

        hexagonPixmap.dispose()

        val texture = Texture(pixmap)
        pixmap.dispose()

        return texture

    }

    override fun dispose() {
        batch.dispose()

    }

    override fun pause() {

    }

    override fun resize(p0: Int, p1: Int) {

    }

    override fun resume() {

    }

}
