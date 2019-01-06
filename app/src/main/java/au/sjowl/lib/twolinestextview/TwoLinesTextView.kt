package au.sjowl.lib.twolinestextview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.TextView

class TwoLinesTextView : TextView {

    var text: List<String> = emptyList()
    var underlines: List<String> = emptyList()

    private val paintText = Paint().apply {
        isAntiAlias = true
    }

    private val paintUnderline = Paint().apply {
        isAntiAlias = true
    }

    private var widthText = 0f
    private var widthUnderlines = 0f
    private var tX = paddingStart.toFloat()
    private var tY = paddingTop.toFloat()
    private var i = 0
    private var spaceWidth = 0f

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        with(context.obtainStyledAttributes(attrs, R.styleable.TwoLinesTextView, 0, 0)) {
            paintUnderline.apply {
                color = getColor(R.styleable.TwoLinesTextView_underlineColor, this@TwoLinesTextView.textColors.defaultColor)
                textSize = getDimension(R.styleable.TwoLinesTextView_underlineSize, this@TwoLinesTextView.textSize / 2)
            }
            text = this@TwoLinesTextView.getText().split(" ")
            underlines = getString(R.styleable.TwoLinesTextView_underlineText).orEmpty().split(" ")
            recycle()
        }
        paintText.apply {
            color = this@TwoLinesTextView.textColors.defaultColor
            textSize = this@TwoLinesTextView.textSize
        }
    }

    override fun onDraw(canvas: Canvas) {
// todo large text scrolling
        while (
            i < text.size &&
            tY < measuredHeight
        ) {
            val word = text[i]
            val translation = if (i < underlines.size) underlines[i] else ""
            widthText = paintText.measureText(word)
            widthUnderlines = paintUnderline.measureText(translation)
            val maxWidth = Math.max(widthText, widthUnderlines)
            if (tX + maxWidth > measuredWidth - paddingEnd) {
                tY += paintText.textSize + lineSpacingExtra + paintText.textSize * lineSpacingMultiplier
                tX = paddingStart.toFloat()
            }
            canvas.drawText(word, tX, tY + paintText.textSize, paintText)
            canvas.drawText(translation, tX, tY + paintText.textSize + paintUnderline.textSize, paintUnderline)

            i++
            tX += spaceWidth + maxWidth
        }
        setNulls()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        while (
            i < text.size &&
            tY < measuredHeight
        ) {
            val word = text[i]
            val translation = if (i < underlines.size) underlines[i] else ""
            widthText = paintText.measureText(word)
            widthUnderlines = paintUnderline.measureText(translation)
            val maxWidth = Math.max(widthText, widthUnderlines)
            if (tX + maxWidth > measuredWidth - paddingEnd) {
                incrementHeight()
                tX = paddingStart.toFloat()
            }

            i++
            tX += spaceWidth + maxWidth
        }
        if (tX > 0) incrementHeight()

        setMeasuredDimension(widthMeasureSpec, tY.toInt())
        setNulls()
        spaceWidth = paintText.measureText(" ")
    }

    private fun incrementHeight() {
        tY += paintText.textSize + lineSpacingExtra + paintText.textSize * lineSpacingMultiplier
    }

    private fun setNulls() {
        widthText = 0f
        widthUnderlines = 0f
        tX = paddingStart.toFloat()
        tY = paddingTop.toFloat()
        i = 0
    }
}
