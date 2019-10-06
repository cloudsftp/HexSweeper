package de.melon.hexsweeper.GUI

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.badlogic.gdx.utils.viewport.Viewport
import de.melon.hexsweeper.logic.Cell
import de.melon.hexsweeper.logic.CellState
import de.melon.hexsweeper.logic.Game
import de.melon.hexsweeper.logic.GameState
import kotlin.properties.Delegates

class GameRenderer(internal val scaling: Float) : ApplicationListener {

    internal lateinit var fieldBatch: SpriteBatch
    internal lateinit var fontBatch: SpriteBatch

    override fun create() {
        fieldBatch = SpriteBatch()
        fontBatch = SpriteBatch()

        importTextures()
        setFont()
        setFieldAndCamera()

    }

    internal lateinit var backgroundTexture: Texture
    internal lateinit var hexagonClosedTexture: Texture
    internal lateinit var hexagonFlaggedTexture: Texture
    internal lateinit var hexagonBombTexture: Texture
    internal lateinit var hexagonFakeTexture: Texture
    internal lateinit var hexagonOpenedTextures: Array<Texture>

    private fun importTextures() {
        fun generateTexture(name: String): Texture {
            val hexagonFileHandle = Gdx.files.internal(name)
            val hexagonPixmap = Pixmap(hexagonFileHandle)
            val hexagonTexture = Texture(hexagonPixmap)

            hexagonPixmap.dispose()

            return hexagonTexture

        }

        fun generateHexagonTexture(name: String) = generateTexture("cells/$name")

        hexagonOpenedTextures = Array(7) { i -> generateHexagonTexture("opened_$i.png") }

        hexagonClosedTexture = generateHexagonTexture("closed.png")
        hexagonFlaggedTexture = generateHexagonTexture("flagged.png")
        hexagonBombTexture = generateHexagonTexture("bomb.png")
        hexagonFakeTexture = generateHexagonTexture("fake.png")

    }

    internal lateinit var statusFont: BitmapFont
    internal lateinit var timerFont: BitmapFont

    fun setFont() {
        statusFont = BitmapFont(Gdx.files.internal("screens/calibri.fnt"))
        statusFont.color = Color.WHITE

        timerFont = BitmapFont(Gdx.files.internal("screens/calibri.fnt"))
        timerFont.color = Color.WHITE

    }

    internal lateinit var camera: OrthographicCamera
    internal lateinit var viewPort: Viewport

    internal val backgroundColorTop = Color.BLUE
    internal val backgroundColorField = Color.DARK_GRAY

    internal var windowWidth = 0f
    internal var windowHeight = 0f

    internal var fieldWidth = 0
    internal var fieldHeight = 0

    internal var offsetX = 0
    internal val offsetXbonus = 77
    internal var offsetY = 0
    internal val cellSpacingX = 153f
    internal val cellSpacingY = 45f

    internal lateinit var game: Game

    var dragged = false

    private fun setFieldAndCamera() {
        // field
        windowWidth = Gdx.graphics.width.toFloat()
        windowHeight = Gdx.graphics.height.toFloat()

        fieldWidth = (scaling * windowWidth).toInt()
        fieldHeight = (scaling * windowHeight).toInt() - 100

        val backgroundPixmap = Pixmap(fieldWidth, fieldHeight, Pixmap.Format.RGB565)
        backgroundPixmap.setColor(backgroundColorField)
        backgroundPixmap.fill()
        backgroundTexture = Texture(backgroundPixmap)

        backgroundPixmap.dispose()

        var n = 0
        var m = 0

        while (n * cellSpacingY + 100 < fieldHeight) n++
        if (n % 2 == 0) n--
        while (m * cellSpacingX + 100 < fieldWidth) m++
        if (m % 2 == 0) m--

        offsetY = ((fieldHeight - ((n - 1) * cellSpacingY + 100)) / 2).toInt()
        offsetX = ((fieldWidth - ((m - 1) * cellSpacingX + 100)) / 2).toInt()

        game = Game(n, m, this)

        // camera
        camera = OrthographicCamera(windowWidth, windowHeight)
        val center = Vector2(fieldWidth / 2f, (fieldHeight + (windowHeight * scaling - fieldHeight)) / 2f)
        camera.position.set(center, 0f)
        camera.zoom = scaling
        camera.update()

        viewPort = ScalingViewport(Scaling.fill, windowWidth, windowHeight, camera)

    }

    internal val numOfRendersPerChange = 2
    internal var render = 0

    override fun render() {
        if (render < numOfRendersPerChange) {
            Gdx.gl.glClearColor(backgroundColorTop.r, backgroundColorTop.g,
                                backgroundColorTop.b, backgroundColorTop.a)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

            drawField()
            drawFonts()

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

    private fun drawFonts() {
        fontBatch.begin()

        var message = ""
        when (game.state) {
            GameState.idle -> {
                message = "Click somewhere to start the Game"
                statusFont.color = Color.WHITE
            }
            GameState.running -> {
                message = "running"
                statusFont.color = Color.WHITE
            }
            GameState.loose -> {
                message = "Game Over!"
                statusFont.color = Color.RED
            }
            GameState.win -> {
                message = "You Won!"
                statusFont.color = Color.GREEN
            }

        }

        val yPosition = fieldHeight / scaling + 32f
        statusFont.draw(fontBatch, message, 50f, yPosition)
        timerFont.draw(fontBatch, String.format("%d", game.getTime()), 900f, yPosition)

        fontBatch.end()

    }

    var lastDragPosition = Vector3(Float.NaN, Float.NaN, 0f)

    fun processTouchDown(p0: Float, p1: Float) {
        lastDragPosition = camera.unproject(Vector3(p0, p1, 0f))

    }

    fun processDrag(p0: Float, p1: Float) {
        val currentDragPosition = camera.unproject(Vector3(p0, p1, 0f))

        val drag = currentDragPosition.cpy()
        drag.sub(lastDragPosition)
        camera.position.sub(drag)

        val edgeXleft = 0f
        val edgeXright = fieldWidth.toFloat()

        if (camera.position.x < edgeXleft)
            camera.position.x = edgeXleft
        else if (camera.position.x > edgeXright)
            camera.position.x = edgeXright

        val edgeYbottom = 0f
        val edgeYtop = fieldHeight.toFloat()

        if (camera.position.y < edgeYbottom)
            camera.position.y = edgeYbottom
        else if (camera.position.y > edgeYtop)
            camera.position.y = edgeYtop

        camera.update()

        dragged = true
    }

    fun processTouchUp(p0: Float, p1: Float, p3: Int) {
        if(dragged){
            dragged = false
            return
        }

        val screenClickVector = Vector3(p0, p1, 0f)
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

    }

    // zoom

    internal val maxZoom = scaling * 2

    fun processScroll(p0: Int) {
        var newZoom = (1 + 0.1f * p0) * camera.zoom
        if (newZoom > maxZoom) newZoom = maxZoom

        camera.zoom = newZoom
        camera.update()

    }

    fun startRender() { render = 0 }

    override fun resize(p0: Int, p1: Int) {
        viewPort.update(p0, p1)
        startRender()
    }


    override fun dispose() {
        fieldBatch.dispose()

    }



    // useles

    override fun pause() {

    }

    override fun resume() {

    }

}
