package de.melon.hexsweeper.GUI

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import de.melon.hexsweeper.logic.Cell
import de.melon.hexsweeper.logic.CellState
import de.melon.hexsweeper.logic.Game
import de.melon.hexsweeper.logic.GameState

class GameRenderer(internal val scaling: Float) : ApplicationListener, InputProcessor {

    internal lateinit var fieldBatch: SpriteBatch
    internal lateinit var endScreenBatch: SpriteBatch

    override fun create() {
        fieldBatch = SpriteBatch()
        endScreenBatch = SpriteBatch()

        importTextures()
        setFieldAndCamera()

        Gdx.input.inputProcessor = this

    }

    internal lateinit var backgroundTexture: Texture
    internal lateinit var hexagonClosedTexture: Texture
    internal lateinit var hexagonFlaggedTexture: Texture
    internal lateinit var hexagonBombTexture: Texture
    internal lateinit var hexagonFakeTexture: Texture
    internal lateinit var hexagonOpenedTextures: Array<Texture>

    internal lateinit var gameOverTexture: Texture
    internal lateinit var youWonTexture: Texture

    private fun importTextures() {
        fun generateTexture(name: String): Texture {
            val hexagonFileHandle = Gdx.files.internal(name)
            val hexagonPixmap = Pixmap(hexagonFileHandle)
            val hexagonTexture = Texture(hexagonPixmap)

            hexagonPixmap.dispose()

            return hexagonTexture

        }

        fun generateHexagonTexture(name: String) = generateTexture("cells/$name")
        fun generateScreenTexture(name: String) = generateTexture("screens/$name")

        hexagonOpenedTextures = Array(7) { i -> generateHexagonTexture("opened_$i.png") }

        hexagonClosedTexture = generateHexagonTexture("closed.png")
        hexagonFlaggedTexture = generateHexagonTexture("flagged.png")
        hexagonBombTexture = generateHexagonTexture("bomb.png")
        hexagonFakeTexture = generateHexagonTexture("fake.png")

        gameOverTexture = generateScreenTexture("gameover.png")
        youWonTexture = generateScreenTexture("youwon.png")

    }

    internal lateinit var camera: OrthographicCamera

    internal var windowWidth = 0f
    internal var windowHeight = 0f

    internal val offsetX = 50
    internal val offsetXbonus = 77
    internal val offsetY = 45
    internal val cellSpacingX = 153f
    internal val cellSpacingY = 45f

    internal lateinit var game: Game

    var dragged = false

    private fun setFieldAndCamera() {
        // field
        windowWidth = Gdx.graphics.width.toFloat()
        windowHeight = Gdx.graphics.height.toFloat()

        val fieldWidth = (scaling * windowWidth).toInt()
        val fieldHeight = (scaling * windowHeight).toInt()

        val backgroundPixmap = Pixmap(fieldWidth, fieldHeight, Pixmap.Format.RGB565)
        backgroundPixmap.setColor(Color.DARK_GRAY)
        backgroundPixmap.fill()
        backgroundTexture = Texture(backgroundPixmap)

        backgroundPixmap.dispose()

        var n = 0
        var m = 0

        while (n * cellSpacingY + offsetY + 100 < fieldHeight) n++
        while (m * cellSpacingX + offsetX + 100 < fieldWidth) m++

        game = Game(n, m)

        // camera
        camera = OrthographicCamera(windowWidth, windowHeight)
        val center = Vector2(fieldWidth / 2f, fieldHeight / 2f)
        camera.position.set(center, 0f)
        camera.zoom = scaling
        camera.update()

    }

    internal val numOfRendersPerChange = 2
    internal var render = 0

    override fun render() {
        if (render < numOfRendersPerChange) {
            Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

            drawField()
            drawEndScreen()

        } else if (render < numOfRendersPerChange + 1) {
            resetDrag()

        }

        render++

    }

    internal lateinit var hexagonSprites: MutableList<MutableList<Sprite>>

    private fun drawField() {
        fun selectTexture(cell: Cell) = when(cell.state) {
            CellState.closed -> hexagonClosedTexture
            CellState.exploded -> hexagonBombTexture
            CellState.flagged -> hexagonFlaggedTexture
            CellState.fake -> hexagonFakeTexture
            CellState.opened -> hexagonOpenedTextures[cell.numOfBombs]

        }

        fieldBatch.projectionMatrix = camera.combined
        fieldBatch.begin()

        Sprite(backgroundTexture).draw(fieldBatch)

        var offsetXcomp: Int

        hexagonSprites = mutableListOf()

        for (i in 0 until game.field.cells.size) {
            hexagonSprites.add(mutableListOf())

            offsetXcomp = offsetX
            offsetXcomp += if (i % 2 == 0) offsetXbonus else 0

            for (j in 0 until game.field.cells[i].size) {
                val hexagonSprite = Sprite(selectTexture(game.field.cells[i][j]))
                hexagonSprite.setPosition(j * cellSpacingX + offsetXcomp, i * cellSpacingY + offsetY)
                hexagonSprite.draw(fieldBatch)

                hexagonSprites[i].add(hexagonSprite)

            }

        }

        fieldBatch.end()

    }

    private fun drawEndScreen() {
        if (game.state != GameState.running && game.state != GameState.idle) {
            endScreenBatch.begin()

            var texture = gameOverTexture
            if (game.state == GameState.win) texture = youWonTexture

            val sprite = Sprite(texture)

            val endScreenOffsetX = (windowWidth - texture.width) / 2f
            val endScreenOffsetY = (windowHeight - texture.height) / 2f

            sprite.setPosition(endScreenOffsetX, endScreenOffsetY)
            sprite.draw(endScreenBatch)

            endScreenBatch.end()

        }

    }

    override fun touchUp(p0: Int, p1: Int, p2: Int, p3: Int): Boolean {
        if(dragged){
            dragged = false
            return true
        }

        val screenClickVector = Vector3(p0.toFloat(), p1.toFloat(), 0f)
        camera.unproject(screenClickVector)

        fun clickInSprite(sprite: Sprite, x: Float, y: Float): Boolean {
            val center = Vector2(sprite.x + sprite.width / 2f, sprite.y + sprite.height / 2f)
            val relativeX = x - center.x
            val relativeY = y - center.y

            var hit = true

            hit = hit && relativeY < 2 * relativeX + sprite.height
            hit = hit && relativeY < sprite.height / 2f
            hit = hit && relativeY < sprite.height - 2 * relativeX

            hit = hit && relativeY > 2 * relativeX - sprite.height
            hit = hit && relativeY > - sprite.height / 2f
            hit = hit && relativeY > - sprite.height - 2 * relativeX

            return hit

        }

        for (row in hexagonSprites)
            for (sprite in row)
                if (clickInSprite(sprite, screenClickVector.x, screenClickVector.y)) {
                    val i = hexagonSprites.indexOf(row)
                    val j = row.indexOf(sprite)

                    println("$i, $j")

                    if (p3 == 0) {
                        game.processOpen(i, j)

                    } else {
                        game.processFlag(i, j)

                    }

                }

        startRender()

        return true
    }

    override fun touchDown(p0: Int, p1: Int, p2: Int, p3: Int): Boolean {
        return false
    }

    // zoom

    override fun scrolled(p0: Int): Boolean {

        var newZoom = (1 + 0.1f * p0) * camera.zoom
        if (newZoom > scaling) newZoom = scaling

        camera.zoom = newZoom
        camera.update()

        startRender()

        return true
    }

    fun startRender() { render = 0 }

    // movement

    var lastDragPosition = Vector3(Float.NaN, Float.NaN, 0f)

    fun resetDrag() {
        lastDragPosition = Vector3(Float.NaN, Float.NaN, 0f)

    }

    override fun touchDragged(p0: Int, p1: Int, p2: Int): Boolean {
        val p0f = p0.toFloat()
        val p1f = p1.toFloat()

        val currentDragPosition = camera.unproject(Vector3(p0f, p1f, 0f))

        if (lastDragPosition.x.isNaN()) {
            lastDragPosition = currentDragPosition

        } else {
            val drag = currentDragPosition.cpy()
            drag.sub(lastDragPosition)

            camera.position.sub(drag)
            camera.update()

            startRender()

            lastDragPosition = currentDragPosition.cpy()
        }

        dragged = true

        return true
    }


    override fun dispose() {
        fieldBatch.dispose()

    }



    // useless

    override fun pause() {

    }

    override fun resize(p0: Int, p1: Int) {

    }

    override fun resume() {

    }

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
