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
    internal lateinit var hexagonOpenedTextures: Array<Texture>
    internal lateinit var hexagonClosedTexture: Texture
    internal lateinit var hexagonFlaggedTexture: Texture
    internal lateinit var hexagonBombTexture: Texture
    internal lateinit var hexagonFakeTexture: Texture

    internal val numOfRendersPerChange = 2
    internal var render = 0
    lateinit var bitmapFont: BitmapFont

    override fun create() {
        batch = SpriteBatch()
        bitmapFont = BitmapFont()

        val hexagonOpenedTexturesList = mutableListOf<Texture>()
        for (i in 0..6) {
            val hexagonFileHandle = Gdx.files.internal("cells/opened_$i.png")
            val hexagonPixmap = Pixmap(hexagonFileHandle)
            val hexagonTexture = Texture(hexagonPixmap)
            hexagonOpenedTexturesList.add(hexagonTexture)

            hexagonPixmap.dispose()

        }

        hexagonOpenedTextures = hexagonOpenedTexturesList.toTypedArray()

        val hexagonClosedFileHandle = Gdx.files.internal("cells/closed.png")
        val hexagonClosedPixmap = Pixmap(hexagonClosedFileHandle)
        hexagonClosedTexture = Texture(hexagonClosedPixmap)

        hexagonClosedPixmap.dispose()

        val hexagonFlaggedFileHandle = Gdx.files.internal("cells/flagged.png")
        val hexagonFlaggedPixmap = Pixmap(hexagonFlaggedFileHandle)
        hexagonFlaggedTexture = Texture(hexagonFlaggedPixmap)

        hexagonFlaggedPixmap.dispose()

        val hexagonBombFileHandle = Gdx.files.internal("cells/bomb.png")
        val hexagonBombPixMap = Pixmap(hexagonBombFileHandle)
        hexagonBombTexture = Texture(hexagonBombPixMap)

        hexagonBombPixMap.dispose()

        val hexagonFakeFileHandle = Gdx.files.internal("cells/fake.png")
        val hexagonFakePixmap = Pixmap(hexagonFakeFileHandle)
        hexagonFakeTexture = Texture(hexagonFakePixmap)

        hexagonFakePixmap.dispose()

        val windowWidth = Gdx.graphics.width.toFloat()
        val windowHeight = Gdx.graphics.height.toFloat()
        camera = OrthographicCamera(windowWidth, windowHeight)
        center = Vector2(windowWidth / 2, windowHeight / 2)

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

            drawBackground()
            drawField()
            drawLog()

            batch.end()

            render++

        }

    }

    private fun drawBackground() {
        val pixmap = Pixmap(Gdx.graphics.width, Gdx.graphics.height, Pixmap.Format.RGB565)
        pixmap.setColor(Color.DARK_GRAY)
        pixmap.fill()
        batch.draw(Texture(pixmap), -center.x, -center.y)

    }


    private fun drawField() {
        fun selectTexture(cell: Cell) = when(cell.state) {
            CellState.closed -> hexagonClosedTexture
            CellState.exploded -> hexagonBombTexture
            CellState.flagged -> hexagonFlaggedTexture
            CellState.fake -> hexagonFakeTexture
            CellState.opened -> hexagonOpenedTextures[cell.numOfBombs]

        }


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
        val mouseLoc = Vector2(p0.toFloat(), p1.toFloat())
        val direction = mouseLoc.sub(center)

        println("Vector: $direction")

        fun clickInSprite(sprite: Sprite, x: Float, y: Float)
                = x > sprite.x && x < sprite.x + sprite.width
                && y > sprite.y && y < sprite.y + sprite.height

        for (row in hexagonSprites)
            for (sprite in row)
                if (clickInSprite(sprite, direction.x, direction.y)) {
                    val i = hexagonSprites.size - hexagonSprites.indexOf(row) - 1
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

