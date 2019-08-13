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
import de.melon.hexsweeper.logic.Cell
import de.melon.hexsweeper.logic.CellState
import de.melon.hexsweeper.logic.Game

var INSTANCE: GameRenderer? = null

class GameRenderer : ApplicationListener, InputProcessor {

    val n = 13
    val m = 6

    val game = Game(n , m)

    internal lateinit var hexagonSprites: MutableList<MutableList<Sprite>>

    internal lateinit var center: Vector2
    internal lateinit var camera: OrthographicCamera
    internal lateinit var batch: SpriteBatch
    internal lateinit var backgroundTexture: Texture
    internal lateinit var hexagonClosedTexture: Texture
    internal lateinit var hexagonFlaggedTexture: Texture
    internal lateinit var hexagonBombTexture: Texture
    internal lateinit var hexagonFakeTexture: Texture
    internal lateinit var hexagonOpenedTextures: Array<Texture>

    internal val numOfRendersPerChange = 2
    internal var render = 0
    lateinit var bitmapFont: BitmapFont

    internal var fieldWidth = 2000
    internal var fieldHeight = 1000

    internal var windowWidth = 0f
    internal var windowHeight = 0f

    override fun create() {
        batch = SpriteBatch()
        bitmapFont = BitmapFont()

        // import Textures

        fun generateHexagonTexture(name: String): Texture {
            val hexagonFileHandle = Gdx.files.internal("cells/$name")
            val hexagonPixmap = Pixmap(hexagonFileHandle)
            val hexagonTexture = Texture(hexagonPixmap)

            hexagonPixmap.dispose()

            return hexagonTexture

        }

        hexagonOpenedTextures = Array<Texture>(7) { i -> generateHexagonTexture("opened_$i.png") }

        hexagonClosedTexture = generateHexagonTexture("closed.png")
        hexagonFlaggedTexture = generateHexagonTexture("flagged.png")
        hexagonBombTexture = generateHexagonTexture("bomb.png")
        hexagonFakeTexture = generateHexagonTexture("fake.png")

        // game field

        windowWidth = Gdx.graphics.width.toFloat()
        windowHeight = Gdx.graphics.height.toFloat()

        fieldWidth = (2 * windowWidth).toInt()
        fieldHeight = (2 * windowHeight).toInt()

        val backgroundPixmap = Pixmap(fieldWidth, fieldHeight, Pixmap.Format.RGB565);
        backgroundPixmap.setColor(Color.DARK_GRAY)
        backgroundPixmap.fill()
        backgroundTexture = Texture(backgroundPixmap)

        backgroundPixmap.dispose()

        // camera

        camera = OrthographicCamera(windowWidth, windowHeight)
        center = Vector2(fieldWidth / 2f, fieldHeight / 2f)
        camera.position.set(center, 0f)
        camera.zoom = 2f
        camera.update()

        Gdx.input.inputProcessor = this

        INSTANCE = this
    }

    override fun render() {
        if (render < numOfRendersPerChange) {
            Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

            batch.projectionMatrix = camera.combined
            batch.begin()

            drawField()
            drawLog()

            batch.end()

            render++

        }

    }


    private fun drawField() {
        fun selectTexture(cell: Cell) = when(cell.state) {
            CellState.closed -> hexagonClosedTexture
            CellState.exploded -> hexagonBombTexture
            CellState.flagged -> hexagonFlaggedTexture
            CellState.fake -> hexagonFakeTexture
            CellState.opened -> hexagonOpenedTextures[cell.numOfBombs]

        }

        Sprite(backgroundTexture).draw(batch)


        var offsetX: Int
        val offsetY = 30

        hexagonSprites = mutableListOf()

        for (i in 0 until game.field.cells.size) {
            hexagonSprites.add(mutableListOf())

            offsetX = 50
            offsetX += if (i % 2 == 0) 77 else 0

            for (j in 0 until game.field.cells[i].size) {
                val hexagonSprite = Sprite(selectTexture(game.field.cells[i][j]))
                hexagonSprite.setPosition(j * 153f + offsetX, i * 45f + offsetY)
                hexagonSprite.draw(batch)

                hexagonSprites[i].add(hexagonSprite)

            }

        }

    }

    private val log = mutableListOf<String>()
    private fun drawLog(){
        bitmapFont.color = Color.WHITE

        if(log.size > 3){
            log.removeAt(0)
        }

        log.forEachIndexed { index, str ->
            bitmapFont.draw(batch, str, 10f, (index + 1) * 16f)
        }
    }

    fun log(s: String){
        log.add(s)
    }


    override fun dispose() {
        batch.dispose()

    }

    override fun touchDown(p0: Int, p1: Int, p2: Int, p3: Int): Boolean {
        render = numOfRendersPerChange

        println("Click at $p0, $p1, $p2, $p3")

        val screenClickVector = Vector3(p0.toFloat(), p1.toFloat(), 0f)
        camera.unproject(screenClickVector)

        println("World ${screenClickVector.x}, ${screenClickVector.y}")

        fun clickInSprite(sprite: Sprite, x: Float, y: Float)
                = x > sprite.x && x < sprite.x + sprite.width
                && y > sprite.y && y < sprite.y + sprite.height

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

    fun startRender() { render = 0 }


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

fun log(s: String){
    INSTANCE?.log(s)
}
