package com.rain.mariokartworldonlinetracker.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.rain.mariokartworldonlinetracker.R

class CustomOutlineTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
    // Wenn Sie app:outlineColor etc. im Theme definieren wollen,
    // können Sie hier auch R.attr.customOutlineTextViewStyle als default Style Attr verwenden
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var outlineColor: Int = currentTextColor // Standard: Textfarbe
    private var outlineWidthPx: Float = 0f

    init {
        // Lade benutzerdefinierte Attribute aus XML
        // Zuerst definieren Sie diese Attribute in res/values/attrs.xml:
        // <declare-styleable name="CustomOutlineTextView">
        //     <attr name="outlineColor" format="color" />
        //     <attr name="outlineWidth" format="dimension" />
        // </declare-styleable>
        attrs?.let {
            context.withStyledAttributes(it, R.styleable.CustomOutlineTextView, 0, 0) {
                outlineColor = getColor(R.styleable.CustomOutlineTextView_customOutlineColor, currentTextColor)
                outlineWidthPx = getDimension(R.styleable.CustomOutlineTextView_customOutlineWidth, 0f)
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (outlineWidthPx > 0 && outlineColor != 0) {
            val originalColor = this.currentTextColor
            val originalStyle = this.paint.style
            val originalStrokeWidth = this.paint.strokeWidth
            val originalTextAlign = this.paint.textAlign // Speichern, falls nötig

            // Stelle sicher, dass textAlign für beide Zeichenvorgänge gleich ist (oft CENTER)
            // this.paint.textAlign = Paint.Align.CENTER // Falls nicht schon durch gravity gesetzt

            this.setTextColor(outlineColor)
            this.paint.style = Paint.Style.STROKE
            this.paint.strokeWidth = outlineWidthPx
            super.onDraw(canvas)

            this.setTextColor(originalColor)
            this.paint.style = originalStyle
            this.paint.strokeWidth = originalStrokeWidth
             this.paint.textAlign = originalTextAlign // Wiederherstellen, falls geändert
        }
        super.onDraw(canvas)
    }

    // Öffentliche Methoden, um Umriss-Eigenschaften programmgesteuert zu ändern
    fun setOutlineColor(color: Int) {
        outlineColor = color
        invalidate() // Fordere ein Neuzeichnen an
    }

    fun setOutlineWidth(widthDp: Float) {
        // Konvertiere dp in Pixel, falls du dp als Eingabe erwartest
        outlineWidthPx = widthDp * resources.displayMetrics.density
        invalidate()
    }

    fun setOutlineWidthPx(widthPx: Float) {
        outlineWidthPx = widthPx
        invalidate()
    }

    // Es ist nicht unbedingt nötig, Attribute wie textSize, typeface etc. hier zu überschreiben,
    // da wir das originale `paint`-Objekt des TextViews für beide Zeichenvorgänge verwenden
    // und nur dessen Stil temporär ändern. Die Basisklasse `AppCompatTextView` kümmert
    // sich darum, dass das `paint`-Objekt bei Änderungen von textSize etc. korrekt aktualisiert wird.
}