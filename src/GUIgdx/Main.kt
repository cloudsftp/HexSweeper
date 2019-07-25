package GUIgdx

import Logic.Field
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Main : ApplicationListener, InputProcessor {

    internal lateinit var hexagonSprites: MutableList<MutableList<Sprite>>

    internal lateinit var batch: SpriteBatch
    internal lateinit var hexagonTexture: Texture

    override fun create() {
        batch = SpriteBatch()

        val hexagonFileHandle = Gdx.files.internal("res/hexagonsmall.png")
        val hexagonPixmap = Pixmap(hexagonFileHandle)
        hexagonTexture = Texture(hexagonPixmap)

        hexagonPixmap.dispose()

    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()

        drawBackground()
        drawField()

        batch.end()

    }

    private fun drawBackground() {
        val pixmap = Pixmap(Gdx.graphics.width, Gdx.graphics.height, Pixmap.Format.RGB565)
        pixmap.setColor(Color.DARK_GRAY)
        pixmap.fill()
        batch.draw(Texture(pixmap), 0f, 0f)

    }


    private fun drawField() {

        val n = 7
        val m = 4

        val field = Field(n, m)

        var offsetX: Int
        val offsetY = 30

        hexagonSprites = mutableListOf()

        for (i in 0 until field.cells.size) {
            hexagonSprites.add(mutableListOf())

            offsetX = if (i % 2 == 0) 77 else 0

            for (j in 0 until field.cells[i].size) {
                val hexagonSprite = Sprite(hexagonTexture)
                hexagonSprite.setPosition(j * 153f + offsetX, i * 45f + offsetY)
                hexagonSprite.draw(batch)

                hexagonSprites[i].add(hexagonSprite)

            }

        }

    }


    override fun dispose() {
        batch.dispose()

    }

    override fun touchDown(p0: Int, p1: Int, p2: Int, p3: Int): Boolean {

        print("Click")

        return false

    }


    // useless

    override fun pause() {

    }

    override fun resize(p0: Int, p1: Int) {

    }

    override fun resume() {

    }


    override fun touchUp(p0: Int, p1: Int, p2: Int, p3: Int): Boolean {
        return false
    }

    override fun touchDragged(p0: Int, p1: Int, p2: Int): Boolean {
        return false
    }

    override fun mouseMoved(p0: Int, p1: Int): Boolean {
        return false
    }

    override fun scrolled(p0: Int): Boolean {
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
