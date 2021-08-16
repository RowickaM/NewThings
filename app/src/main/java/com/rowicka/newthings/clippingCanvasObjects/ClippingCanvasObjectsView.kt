package com.rowicka.newthings.clippingCanvasObjects

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.os.Build
import android.view.View
import com.rowicka.newthings.R

class ClippingCanvasObjectsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        isAntiAlias = true
        strokeWidth = resources.getDimension(R.dimen.strokeWidth)
        textSize = resources.getDimension(R.dimen.textSize)
    }

    private val path = Path()

    private val clipRectRight = resources.getDimension(R.dimen.clipRectRight)
    private val clipRectBottom = resources.getDimension(R.dimen.clipRectBottom)
    private val clipRectTop = resources.getDimension(R.dimen.clipRectTop)
    private val clipRectLeft = resources.getDimension(R.dimen.clipRectLeft)

    private val rectInset = resources.getDimension(R.dimen.rectInset)
    private val smallRectOffset = resources.getDimension(R.dimen.smallRectOffset)

    private val circleRadius = resources.getDimension(R.dimen.circleRadius)

    private val textOffset = resources.getDimension(R.dimen.textOffset)
    private val textSize = resources.getDimension(R.dimen.textSize)

    private val columnOne = rectInset
    private val columnTwo = columnOne + rectInset + clipRectRight

    private val rowOne = rectInset
    private val rowTwo = rowOne + rectInset + clipRectBottom
    private val rowThree = rowTwo + rectInset + clipRectBottom
    private val rowFour = rowThree + rectInset + clipRectBottom
    private val textRow = rowFour + (1.5f * clipRectBottom)

    private val rectF = RectF(
        rectInset,
        rectInset,
        clipRectRight - rectInset,
        clipRectBottom - rectInset
    )

    private val rejectRow = rowFour + rectInset + 2*clipRectBottom

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawBackAndUnclippedRectangle(canvas)
        drawDifferenceClippingExample(canvas)
        drawCircularClippingExample(canvas)
        drawIntersectionClippingExample(canvas)
        drawCombinedClippingExample(canvas)
        drawRoundedRectangleClippingExample(canvas)
        drawOutsideClippingExample(canvas)
        drawSkewedTextExample(canvas)
        drawTranslatedTextExample(canvas)
        drawQuickRejectExample(canvas)
    }

    private fun drawClippedRectangle(canvas: Canvas) {
        canvas.clipRect(
            clipRectLeft,
            clipRectTop,
            clipRectRight,
            clipRectBottom
        )

        canvas.drawColor(Color.WHITE)

        paint.color = Color.RED
        canvas.drawLine(
            clipRectLeft,
            clipRectTop,
            clipRectRight,
            clipRectBottom,
            paint
        )

        paint.color = Color.GREEN
        canvas.drawCircle(
            circleRadius,
            clipRectBottom - circleRadius,
            circleRadius,
            paint
        )

        paint.color = Color.BLUE
        paint.textSize = textSize
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText(
            context.getString(R.string.clipping),
            clipRectRight,
            textOffset,
            paint
        )
    }

    private fun drawBackAndUnclippedRectangle(canvas: Canvas?) {
        canvas?.apply {
            drawColor(Color.GRAY)
            save()
            translate(columnOne, rowOne)
            drawClippedRectangle(this)
            restore()
        }
    }

    private fun drawDifferenceClippingExample(canvas: Canvas?) {
        canvas?.apply {
            save()
            translate(columnTwo, rowOne)

//            canvas.clipRect(
//                rectInset * 2,
//                rectInset * 2,
//                clipRectRight - rectInset * 2,
//                clipRectBottom - rectInset * 2
//            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                clipOutRect(
//                    4 * rectInset,
//                    4 * rectInset,
//                    clipRectRight - 4 * rectInset,
//                    clipRectBottom - 4 * rectInset
//                )
                clipOutRect(
                    2 * rectInset,
                    2 * rectInset,
                    clipRectRight - 2 * rectInset,
                    clipRectBottom - 2 * rectInset
                )
            } else {
//                clipRect(
//                    4 * rectInset,
//                    4 * rectInset,
//                    clipRectRight - 4 * rectInset,
//                    clipRectBottom - 4 * rectInset,
//                    Region.Op.DIFFERENCE
//                )
                clipRect(
                    2 * rectInset,
                    2 * rectInset,
                    clipRectRight - 2 * rectInset,
                    clipRectBottom - 2 * rectInset,
                    Region.Op.DIFFERENCE
                )
            }

            drawClippedRectangle(this)
            restore()
        }
    }

    private fun drawCircularClippingExample(canvas: Canvas?) {
        canvas?.apply {
            save()
            translate(columnOne, rowTwo)

            path.rewind()
            path.addCircle(
                circleRadius,
                clipRectBottom - circleRadius,
                circleRadius,
                Path.Direction.CCW
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                clipOutPath(path)
            } else {
                clipPath(path, Region.Op.DIFFERENCE)
            }

            drawClippedRectangle(this)
            restore()
        }
    }

    private fun drawIntersectionClippingExample(canvas: Canvas?) {
        canvas?.apply {
            save()
            translate(columnTwo, rowTwo)

            clipRect(
                clipRectLeft,
                clipRectTop,
                clipRectRight - smallRectOffset,
                clipRectBottom - smallRectOffset
            )

            clipRect(
                clipRectLeft + smallRectOffset,
                clipRectTop + smallRectOffset,
                clipRectRight,
                clipRectBottom
            )

            drawClippedRectangle(this)
            restore()
        }
    }

    private fun drawCombinedClippingExample(canvas: Canvas?) {
        canvas?.apply {
            save()
            translate(columnOne, rowThree)

            path.rewind()
            path.addCircle(
                clipRectLeft + circleRadius + rectInset,
                clipRectTop + circleRadius + rectInset,
                circleRadius,
                Path.Direction.CCW
            )

            path.addRect(
                clipRectRight / 2 - circleRadius,
                clipRectTop + + circleRadius + rectInset,
                clipRectRight / 2 + circleRadius,
                clipRectBottom - rectInset,
                Path.Direction.CCW
            )

            clipPath(path)

            drawClippedRectangle(this)
            restore()
        }
    }

    private fun drawRoundedRectangleClippingExample(canvas: Canvas?) {
        canvas?.apply {
            save()
            translate(columnTwo, rowThree)

            path.rewind()
            path.addRoundRect(
                rectF,
                clipRectRight / 4,
                clipRectRight / 4,
                Path.Direction.CCW
            )
            clipPath(path)

            drawClippedRectangle(this)
            restore()
        }
    }

    private fun drawOutsideClippingExample(canvas: Canvas?) {
        canvas?.apply {
            save()
            translate(columnOne, rowFour)

            canvas.clipRect(
                rectInset * 2,
                rectInset * 2,
                clipRectRight - rectInset * 2,
                clipRectBottom - rectInset * 2
            )

            drawClippedRectangle(this)
            restore()
        }
    }

    private fun drawTranslatedTextExample(canvas: Canvas?) {
       canvas?.apply {
           paint.color = Color.GREEN
           paint.textAlign = Paint.Align.LEFT

           translate(columnTwo, textRow)

           drawText(
               context.getString(R.string.translated),
               clipRectLeft,
               clipRectTop,
               paint
           )
           restore()
       }
    }

    private fun drawSkewedTextExample(canvas: Canvas?) {
        canvas?.apply {
            save()
            paint.color = Color.YELLOW
            paint.textAlign = Paint.Align.RIGHT

            translate(columnTwo, textRow)

            canvas.skew(0.2f, 0.3f)
            canvas.drawText(
                context.getString(R.string.skewed),
                clipRectLeft,
                clipRectTop,
                paint
            )
            canvas.restore()
        }
    }

    private fun drawQuickRejectExample(canvas: Canvas?) {
        val inClipRectangle = RectF(clipRectRight / 2,
            clipRectBottom / 2,
            clipRectRight * 2,
            clipRectBottom * 2)

        val notInClipRectangle = RectF(RectF(clipRectRight+1,
            clipRectBottom+1,
            clipRectRight * 2,
            clipRectBottom * 2))

        canvas?.apply {
            save()
            translate(columnOne, rejectRow)
            clipRect(
                clipRectLeft, clipRectTop,
                clipRectRight, clipRectBottom
            )
            if (quickReject(inClipRectangle, Canvas.EdgeType.AA)) {
                canvas.drawColor(Color.WHITE)
            } else {
                drawColor(Color.BLACK)
                drawRect(
                    inClipRectangle, paint
                )
            }
            restore()
        }
    }
}