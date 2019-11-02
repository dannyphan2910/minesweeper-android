package hu.ait.minesweeper.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import hu.ait.minesweeper.MainActivity
import hu.ait.minesweeper.R
import hu.ait.minesweeper.model.FieldType
import hu.ait.minesweeper.model.GameMode
import hu.ait.minesweeper.model.MinesweeperBoard

class MinesweeperView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var DIMENSION = MinesweeperBoard.DIMENSION
    var MINES_COUNT = MinesweeperBoard.MINES_NUM

    var stopGame = false
    var mode = GameMode.NORMAL

    var paintBackground: Paint? = null
    var paintLine: Paint? = null
    var paintRed: Paint? = null

    var bitmapField = BitmapFactory.decodeResource(resources, R.drawable.field)
    var bitmapFlag = BitmapFactory.decodeResource(resources, R.drawable.flag)
    var bitmapMine = BitmapFactory.decodeResource(resources, R.drawable.mine)


    init {
        paintBackground = Paint()
        paintBackground?.color = Color.BLACK
        paintBackground?.style = Paint.Style.FILL

        paintLine = Paint()
        paintLine?.color = Color.WHITE
        paintLine?.style = Paint.Style.STROKE
        paintLine?.strokeWidth = 5f

        paintRed = Paint()
        paintRed?.color = Color.parseColor( "#8b0000")
        paintRed?.style = Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        bitmapField = Bitmap.createScaledBitmap(bitmapField, width / DIMENSION, height / DIMENSION, false)
        bitmapFlag = Bitmap.createScaledBitmap(bitmapFlag, width / (2 * DIMENSION), height / (2 * DIMENSION), false)
        bitmapMine = Bitmap.createScaledBitmap(bitmapMine, 2 * width / (3 * DIMENSION), 2 * height / (3 * DIMENSION), false)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBackground!!)

        drawGrid(canvas)
        fieldImage(canvas)
        drawField(canvas)

        (context as MainActivity).updateMineCount(MINES_COUNT)
    }

    private fun fieldImage(canvas: Canvas?) {
        for (i in 0..(DIMENSION - 1)) {
            for (j in 0..(DIMENSION - 1)) {
                canvas?.drawBitmap(bitmapField, i * width.toFloat() / DIMENSION, j * height.toFloat() / DIMENSION, null)
            }
        }
    }

    private fun drawGrid(canvas: Canvas?) {
        for (i in 1..(DIMENSION - 1)) {
            // vertical lines
            canvas?.drawLine(i * width.toFloat() / DIMENSION, 0f, i * width.toFloat() / DIMENSION, height.toFloat(), paintLine!!)
            // horizontal lines
            canvas?.drawLine(0f, i * height.toFloat() / DIMENSION, width.toFloat(), i * height.toFloat() / DIMENSION, paintLine!!)
        }
    }

    private fun drawField(canvas: Canvas?) {
        for (i in 0..(DIMENSION - 1)) {
            for (j in 0..(DIMENSION - 1)) {
                var field = MinesweeperBoard.getField(i, j)

                if (field!!.isFlagged) {
                    drawFlag(canvas, i , j)
                } else if (field.type == FieldType.MINE && field.wasTouched)  {
                    drawMine(canvas, i, j)
                } else if (field.type == FieldType.EMPTY && field.wasTouched) {
                    drawNum(canvas, i, j)
                }
            }
        }
    }

    private fun drawMine(canvas: Canvas?, i: Int, j: Int) {
        val xCoordPic = (i * width.toFloat() / DIMENSION) + 1/5f * width.toFloat() / DIMENSION
        val yCoordPic = (j * height.toFloat() / DIMENSION) + 1/5f * height.toFloat() / DIMENSION

        canvas?.drawRect(i * width.toFloat() / DIMENSION, j * height.toFloat() / DIMENSION,
            (i + 1) * width.toFloat() / DIMENSION, (j + 1) * height.toFloat() / DIMENSION, paintRed)
        canvas?.drawBitmap(bitmapMine, xCoordPic, yCoordPic, null)
    }

    private fun drawFlag(canvas: Canvas?, i: Int, j: Int) {
        val xCoordPic = (i * width.toFloat() / DIMENSION) + 1/4f * width.toFloat() / DIMENSION
        val yCoordPic = (j * height.toFloat() / DIMENSION) + 1/4f * height.toFloat() / DIMENSION

        canvas?.drawBitmap(bitmapFlag, xCoordPic, yCoordPic, null)
    }

    private fun drawNum(canvas: Canvas?, i: Int, j: Int) {
        drawBorder(canvas, i, j)
        drawTouchedField(canvas, i, j)
        drawNumColor(canvas, i, j)
    }

    private fun drawNumColor(canvas: Canvas?, i: Int, j: Int) {
        var num = MinesweeperBoard.getField(i, j)?.minesAround

        var paintNumber = Paint()
        paintNumber.style = Paint.Style.FILL_AND_STROKE
        paintNumber.textSize = 0.5f * height.toFloat() / (DIMENSION)
        var colorArr = arrayOf(Color.parseColor("#0407E1"), Color.parseColor("#087C09"),
            Color.parseColor("#F70008"), Color.parseColor("#030176"),
            Color.parseColor("#840000"), Color.parseColor("#008285"),
            Color.parseColor("#010101"), Color.parseColor("#7E7E7E"))

        paintNumber.color = colorArr[num!!]

        canvas?.drawText(MinesweeperBoard.getField(i, j)?.minesAround.toString(),
            (i * width.toFloat() / DIMENSION) + 1/3f * width.toFloat() / DIMENSION,
            ((j + 1) * height.toFloat() / DIMENSION) - 1/3f * height.toFloat() / DIMENSION, paintNumber!!)
    }

    private fun drawBorder(canvas: Canvas?, i: Int, j: Int) {
        var paintDkGray = Paint()
        paintDkGray.color = Color.DKGRAY
        paintDkGray.style = Paint.Style.STROKE

        canvas?.drawRect(i * width.toFloat() / DIMENSION, j * height.toFloat() / DIMENSION,
            (i + 1) * width.toFloat() / DIMENSION, (j + 1) * height.toFloat() / DIMENSION, paintDkGray)
    }

    private fun drawTouchedField(canvas: Canvas?, i: Int, j: Int) {
        var paintGray = Paint()
        paintGray.color = Color.GRAY
        paintGray.style = Paint.Style.FILL

        canvas?.drawRect(i * width.toFloat() / DIMENSION, j * height.toFloat() / DIMENSION,
            (i + 1) * width.toFloat() / DIMENSION, (j + 1) * height.toFloat() / DIMENSION, paintGray)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!stopGame) {
            val tX = event.x.toInt() / (width / DIMENSION)
            val tY = event.y.toInt() / (height / DIMENSION)

            if (tX < DIMENSION && tY < DIMENSION && MinesweeperBoard.getField(tX, tY)?.wasTouched == false) {
                var field =  MinesweeperBoard.getField(tX, tY)
                field?.wasTouched = true

                if (mode == GameMode.FLAG) {
                    MinesweeperBoard.getField(tX, tY)?.isFlagged = true
                    if (MinesweeperBoard.getField(tX, tY)?.type == FieldType.MINE) MINES_COUNT--
                }

                validateWinLose()
            }
        }

        invalidate()

        return super.onTouchEvent(event)
    }

    private fun validateWinLose() {
        when {
            MinesweeperBoard.hasLost() -> {
                (context as MainActivity).displayLose()
                stopGame = true
            }
            MinesweeperBoard.hasWon() -> {
                (context as MainActivity).displayWin()
                stopGame = true
            }
        }
    }

    fun resetGame() {
        MinesweeperBoard.reset()
        stopGame = false
        invalidate()
    }
}