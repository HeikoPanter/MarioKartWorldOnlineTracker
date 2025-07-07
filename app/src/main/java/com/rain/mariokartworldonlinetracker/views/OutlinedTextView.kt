package com.rain.mariokartworldonlinetracker.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.rain.mariokartworldonlinetracker.R

class OutlinedTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle // Wichtig für Standard-TextView-Attribute
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var strokeColor: Int = currentTextColor // Standardmäßig Textfarbe
    private var strokeWidthValue: Float = 0f
        set(value) {
            field = value
            requestLayout() // Wichtig: Neuvermessung anfordern, wenn sich die Strichbreite ändert
            invalidate()
        }

    init {
        // Optional: Attribute aus XML laden, wenn Sie sie definieren möchten
        // z.B. app:outlineColor, app:outlineWidth
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.OutlinedTextView, 0, 0)
            strokeColor = typedArray.getColor(R.styleable.OutlinedTextView_outlineColor, currentTextColor)
            strokeWidthValue = typedArray.getDimension(R.styleable.OutlinedTextView_outlineWidth, 0f)
            typedArray.recycle()
        }
    }

    // Stellen Sie sicher, dass der Text für die Kontur unter dem Haupttext gezeichnet wird.
    // Wir müssen den Zustand des Paints des TextViews speichern und wiederherstellen.

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Lassen Sie AppCompatTextView die normale Messung durchführen
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (strokeWidthValue > 0) {
            // Erhöhe die gemessene Breite und Höhe um die Konturbreite auf beiden Seiten
            // Math.ceil, um sicherzustellen, dass wir genug Platz haben
            val extraSpace = (Math.ceil(strokeWidthValue.toDouble()) * 2).toInt()
            val newWidth = measuredWidth + extraSpace
            val newHeight = measuredHeight + extraSpace
            setMeasuredDimension(newWidth, newHeight)
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (strokeWidthValue > 0) {
            // WICHTIG: Da wir die View-Größe in onMeasure geändert haben,
            // müssen wir den Text jetzt möglicherweise verschieben, damit er
            // zentriert innerhalb des neuen, größeren Bereichs erscheint und
            // die Kontur Platz hat.
            // Die Kontur wird relativ zum Text gezeichnet.
            // canvas.save() und canvas.translate() könnten hier nützlich sein,
            // BEVOR super.onDraw() aufgerufen wird, um den Text leicht zu verschieben,
            // sodass die Kontur nicht am Rand der ursprünglichen Textbox klebt.

            // Einfacher Ansatz: Der Text wird an seiner normalen Position gezeichnet,
            // aber die View ist jetzt größer, sodass die Kontur Platz hat.
            // Der Text erscheint also nicht mehr perfekt zentriert, wenn man
            // android:gravity="center" erwartet hätte, ohne das zusätzliche "innere" Padding.

            // ----- Bessere Methode für onDraw mit onMeasure Anpassung -----
            val originalPaintStyle = paint.style
            val originalStrokeW = paint.strokeWidth // Store original strokeWidth
            val originalColor = currentTextColor

            // Verschiebe den Canvas, um Platz für die linke/obere Kontur zu schaffen
            // Dies zentriert den Text + Kontur innerhalb der vergrößerten View
            val offset = Math.ceil(strokeWidthValue.toDouble()).toFloat()
            canvas.save()
            canvas.translate(offset, offset) // Text nach rechts unten verschieben

            // Kontur zeichnen
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = strokeWidthValue
            this.setTextColor(strokeColor)
            super.onDraw(canvas)

            // Haupttext darüber zeichnen
            paint.style = Paint.Style.FILL
            paint.strokeWidth = originalStrokeW // Restore original strokeWidth for fill
            this.setTextColor(originalColor)
            super.onDraw(canvas)

            canvas.restore() // Canvas-Transformation zurücksetzen

            // Alten Paint-Stil wiederherstellen
            paint.style = originalPaintStyle

        } else {
            super.onDraw(canvas)
        }
    }

    // Öffentliche Methoden zum Einstellen der Kontur-Eigenschaften programmatisch
    fun setOutlineColor(color: Int) {
        strokeColor = color
        invalidate() // Neuzeichnen anfordern
    }

    fun setOutlineWidth(width: Float) {
        strokeWidthValue = width
        invalidate()
    }
}

