package com.rain.mariokartworldonlinetracker

import android.content.Context
import android.media.MediaPlayer
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins

object TrackAndKnockoutHelper {
    private val trackMap: Map<TrackName?, List<TrackName>> = mutableMapOf(
        null to TrackName.entries.toList(),
        TrackName.MBC1 to listOf(TrackName.WS7, TrackName.TF26, TrackName.DKSS4, TrackName.WSS3, TrackName.DH5),
        TrackName.CC2 to listOf(TrackName.CM25, TrackName.PS15, TrackName.FAO14, TrackName.DKSS4, TrackName.DH5, TrackName.WS7),
        TrackName.WSS3 to listOf(TrackName.MBC1, TrackName.DKSS4, TrackName.DH5),
        TrackName.DKSS4 to listOf(TrackName.CC2, TrackName.KTB13, TrackName.WSS3, TrackName.MBC1),
        TrackName.DH5 to listOf(TrackName.SGB6, TrackName.MBC1, TrackName.CC2, TrackName.WSS3),
        TrackName.SGB6 to listOf(TrackName.AF8, TrackName.WS7, TrackName.CM25, TrackName.DH5),
        TrackName.WS7 to listOf(TrackName.BC27, TrackName.CC2, TrackName.MBC1, TrackName.SGB6, TrackName.AF8),
        TrackName.AF8 to listOf(TrackName.BC27, TrackName.TF26, TrackName.WS7, TrackName.SGB6),
        TrackName.DKP9 to listOf(TrackName.SVP10, TrackName.SHS11, TrackName.WSY12, TrackName.SSSW17, TrackName.CCF20),
        TrackName.SVP10 to listOf(TrackName.SHS11, TrackName.DKP9, TrackName.CCF20, TrackName.MC29, TrackName.BC22),
        TrackName.SHS11 to listOf(TrackName.WSY12, TrackName.DKP9, TrackName.DD21, TrackName.SVP10),
        TrackName.WSY12 to listOf(TrackName.SHS11, TrackName.PB16, TrackName.CCF20, TrackName.DKP9),
        TrackName.KTB13 to listOf(TrackName.PS15, TrackName.FAO14, TrackName.DDJ18, TrackName.DKSS4),
        TrackName.FAO14 to listOf(TrackName.CCF20, TrackName.SSSW17, TrackName.GBR19, TrackName.DDJ18, TrackName.KTB13, TrackName.PS15),
        TrackName.PS15 to listOf(TrackName.MMM24, TrackName.CCF20, TrackName.FAO14, TrackName.KTB13, TrackName.CC2, TrackName.CM25, TrackName.RR30),
        TrackName.PB16 to listOf(TrackName.WSY12, TrackName.GBR19, TrackName.SSSW17),
        TrackName.SSSW17 to listOf(TrackName.DKP9, TrackName.PB16, TrackName.GBR19, TrackName.DDJ18, TrackName.FAO14),
        TrackName.DDJ18 to listOf(TrackName.SSSW17, TrackName.GBR19, TrackName.KTB13, TrackName.FAO14),
        TrackName.GBR19 to listOf(TrackName.PB16, TrackName.DDJ18, TrackName.FAO14, TrackName.SSSW17),
        TrackName.CCF20 to listOf(TrackName.DD21, TrackName.SVP10, TrackName.DKP9, TrackName.WSY12, TrackName.FAO14, TrackName.PS15, TrackName.CM25, TrackName.MMM24),
        TrackName.DD21 to listOf(TrackName.SHS11, TrackName.CCF20, TrackName.MMM24, TrackName.TF26, TrackName.AH28),
        TrackName.BC22 to listOf(TrackName.SVP10, TrackName.AH28),
        TrackName.DBB23 to listOf(TrackName.AH28, TrackName.MC29, TrackName.TF26, TrackName.BC27),
        TrackName.MMM24 to listOf(TrackName.MC29, TrackName.DD21, TrackName.CCF20, TrackName.PS15, TrackName.CM25, TrackName.TF26),
        TrackName.CM25 to listOf(TrackName.TF26, TrackName.CCF20, TrackName.PS15, TrackName.CC2, TrackName.SGB6),
        TrackName.TF26 to listOf(TrackName.DBB23, TrackName.AH28, TrackName.MC29, TrackName.DD21, TrackName.MMM24, TrackName.CM25, TrackName.MBC1, TrackName.AF8, TrackName.BC27),
        TrackName.BC27 to listOf(TrackName.DBB23, TrackName.TF26, TrackName.WS7, TrackName.AF8),
        TrackName.AH28 to listOf(TrackName.BC22, TrackName.DD21, TrackName.MC29, TrackName.TF26, TrackName.DBB23),
        TrackName.MC29 to listOf(TrackName.AH28, TrackName.SVP10, TrackName.MMM24, TrackName.TF26, TrackName.DBB23)
    )

    private val trackResMap: Map<TrackName, Int> = mutableMapOf(
        TrackName.MBC1 to R.drawable.mbc1,
        TrackName.CC2 to R.drawable.cc2,
        TrackName.WSS3 to R.drawable.wss3,
        TrackName.DKSS4 to R.drawable.dkss4,
        TrackName.DH5 to R.drawable.dh5,
        TrackName.SGB6 to R.drawable.sgb6,
        TrackName.WS7 to R.drawable.ws7,
        TrackName.AF8 to R.drawable.af8,
        TrackName.DKP9 to R.drawable.dkp9,
        TrackName.SVP10 to R.drawable.svp10,
        TrackName.SHS11 to R.drawable.shs11,
        TrackName.WSY12 to R.drawable.wsy12,
        TrackName.KTB13 to R.drawable.ktb13,
        TrackName.FAO14 to R.drawable.fao14,
        TrackName.PS15 to R.drawable.ps15,
        TrackName.PB16 to R.drawable.pb16,
        TrackName.SSSW17 to R.drawable.sssw17,
        TrackName.DDJ18 to R.drawable.ddj18,
        TrackName.GBR19 to R.drawable.gbr19,
        TrackName.CCF20 to R.drawable.ccf20,
        TrackName.DD21 to R.drawable.dd21,
        TrackName.BC22 to R.drawable.bc22,
        TrackName.DBB23 to R.drawable.dbb23,
        TrackName.MMM24 to R.drawable.mmm24,
        TrackName.CM25 to R.drawable.cm25,
        TrackName.TF26 to R.drawable.tf26,
        TrackName.BC27 to R.drawable.bc27,
        TrackName.AH28 to R.drawable.ah28,
        TrackName.MC29 to R.drawable.mc29,
        TrackName.RR30 to R.drawable.rr30
    )

    private val knockoutCupResMap: Map<KnockoutCupName, Int> = mutableMapOf(
        KnockoutCupName.GR1 to R.drawable.button_gr1,
        KnockoutCupName.IR2 to R.drawable.button_ir2,
        KnockoutCupName.MR3 to R.drawable.button_mr3,
        KnockoutCupName.SR4 to R.drawable.button_sr4,
        KnockoutCupName.CR5 to R.drawable.button_cr5,
        KnockoutCupName.AR6 to R.drawable.button_ar6,
        KnockoutCupName.CR7 to R.drawable.button_cr7,
        KnockoutCupName.HR8 to R.drawable.button_hr8
    )

    private val positionResMap: Map<Short, Int> = mutableMapOf(
        1.toShort() to R.drawable.button_1,
        2.toShort() to R.drawable.button_2,
        3.toShort() to R.drawable.button_3,
        4.toShort() to R.drawable.button_4,
        5.toShort() to R.drawable.button_5,
        6.toShort() to R.drawable.button_6,
        7.toShort() to R.drawable.button_7,
        8.toShort() to R.drawable.button_8,
        9.toShort() to R.drawable.button_9,
        10.toShort() to R.drawable.button_10,
        11.toShort() to R.drawable.button_11,
        12.toShort() to R.drawable.button_12,
        13.toShort() to R.drawable.button_13,
        14.toShort() to R.drawable.button_14,
        15.toShort() to R.drawable.button_15,
        16.toShort() to R.drawable.button_16,
        17.toShort() to R.drawable.button_17,
        18.toShort() to R.drawable.button_18,
        19.toShort() to R.drawable.button_19,
        20.toShort() to R.drawable.button_20,
        21.toShort() to R.drawable.button_21,
        22.toShort() to R.drawable.button_22,
        23.toShort() to R.drawable.button_23,
        24.toShort() to R.drawable.button_24
    )

    fun getTrackResId(trackName: TrackName?): Int {
        return trackResMap[trackName] ?: 0
    }

    fun getPossibleTracks(selectedTrack: TrackName?): List<TrackName> {
        return trackMap[selectedTrack] ?: emptyList()
    }

    fun getKnockoutCups(): List<KnockoutCupName> {
        return KnockoutCupName.entries.toList()
    }

    /**
     * Erstellt und füllt ein LinearLayout mit Reihen von ImageViews.
     *
     * @param context Der Anwendungskontext.
     * @param dataList Die Liste der Datenobjekte (z.B. TrackInfo).
     * @param itemsPerRow Anzahl der Items, die pro Reihe angezeigt werden sollen.
     * @param containerLinearLayout Das LinearLayout, dem die Reihen hinzugefügt werden sollen.
     * @param itemMarginPx Der Margin für jedes einzelne ImageView in Pixeln.
     * @param createImageViewFunc Eine Funktion, die ein einzelnes ImageView erstellt.
     *                            Sie erhält ein Datenobjekt und den Margin.
     * @param onItemClicked Eine Lambda-Funktion, die aufgerufen wird, wenn ein Item geklickt wird.
     *                      Sie erhält das Datenobjekt des geklickten Items.
     */
    fun <T> populateImageRows(
        context: Context,
        dataList: List<T>,
        itemsPerRow: Int,
        containerLinearLayout: LinearLayout,
        itemMarginPx: Int,
        // Diese Funktion erstellt das spezifische ImageView
        createImageViewFunc: (Context, T, Int, (T) -> Unit) -> ImageView,
        // Diese Funktion wird als Klick-Handler für jedes ImageView verwendet
        onItemClicked: (T) -> Unit
    ) {
        var currentRowLinearLayout: LinearLayout? = null
        containerLinearLayout.removeAllViews() // Bestehende Views entfernen, falls neu befüllt wird

        dataList.forEachIndexed { index, dataItem ->
            if (index % itemsPerRow == 0) {
                currentRowLinearLayout = LinearLayout(context).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    weightSum = itemsPerRow.toFloat()
                }
                containerLinearLayout.addView(currentRowLinearLayout)
            }
            // Das spezifische ImageView mit dem spezifischen Klick-Handler erstellen lassen
            val imageView = createImageViewFunc(context, dataItem, itemMarginPx, onItemClicked)
            currentRowLinearLayout?.addView(imageView)
        }
    }

    fun createImageView(
        context: Context,
        contentDescr: String,
        imageResId: Int,
        marginPx: Int
    ): ImageView {
        // Dieser Code ist im Grunde Ihre createTrackImageViewWithCustomClickHandler,
        // aber so angepasst, dass er den Kontext explizit nimmt und den Klick-Handler weiterreicht.
        return ImageView(context).apply {
            id = View.generateViewId()
            setImageResource(imageResId)
            adjustViewBounds = true

            val imageLayoutParams = LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                weight = 1f
                setMargins(marginPx, marginPx, marginPx, marginPx)
            }
            this.layoutParams = imageLayoutParams
            scaleType = ImageView.ScaleType.FIT_CENTER

            isClickable = true
            isFocusable = true
            contentDescription = contentDescr
        }
    }

    // Spezifische ImageView-Erstellungsfunktion (kann auch außerhalb dieser Klasse sein)
    // Diese Funktion wird an populateImageRows übergeben
    fun createStandardTrackImageView(
        context: Context,
        trackName: TrackName,
        marginPx: Int,
        onTrackClicked: (TrackName) -> Unit // Wichtig: Der Klick-Handler wird durchgereicht
    ): ImageView {
        // Dieser Code ist im Grunde Ihre createTrackImageViewWithCustomClickHandler,
        // aber so angepasst, dass er den Kontext explizit nimmt und den Klick-Handler weiterreicht.
        return ImageView(context).apply {
            id = View.generateViewId()
            setImageResource(trackResMap[trackName] ?:0)
            adjustViewBounds = true

            val imageLayoutParams = LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                weight = 1f
                setMargins(marginPx, marginPx, marginPx, marginPx)
            }
            this.layoutParams = imageLayoutParams
            scaleType = ImageView.ScaleType.FIT_CENTER

            isClickable = true
            isFocusable = true
            contentDescription = trackName.name // Annahme: TrackInfo hat trackName

            setOnClickListener {
                onTrackClicked(trackName) // Der übergebene Klick-Handler wird hier aufgerufen
            }
        }
    }

    // Spezifische ImageView-Erstellungsfunktion (kann auch außerhalb dieser Klasse sein)
    // Diese Funktion wird an populateImageRows übergeben
    fun createStandardKnockoutCupImageView(
        context: Context,
        knockoutCupName: KnockoutCupName,
        marginPx: Int,
        onKnockoutCupClicked: (KnockoutCupName) -> Unit // Wichtig: Der Klick-Handler wird durchgereicht
    ): ImageView {
        // Dieser Code ist im Grunde Ihre createTrackImageViewWithCustomClickHandler,
        // aber so angepasst, dass er den Kontext explizit nimmt und den Klick-Handler weiterreicht.
        return ImageView(context).apply {
            id = View.generateViewId()
            setImageResource(knockoutCupResMap[knockoutCupName] ?: 0) // Annahme: TrackInfo hat iconResId
            adjustViewBounds = true

            val imageLayoutParams = LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                weight = 1f
                setMargins(marginPx, marginPx, marginPx, marginPx)
            }
            this.layoutParams = imageLayoutParams
            scaleType = ImageView.ScaleType.FIT_CENTER

            isClickable = true
            isFocusable = true
            contentDescription = knockoutCupName.name // Annahme: TrackInfo hat trackName

            setOnClickListener {
                onKnockoutCupClicked(knockoutCupName) // Der übergebene Klick-Handler wird hier aufgerufen
            }
        }
    }

    fun createStandardPositionImageView(
        context: Context,
        position: Short,
        marginPx: Int,
        onPositionClicked: (Short) -> Unit // Wichtig: Der Klick-Handler wird durchgereicht
    ): ImageView {
        // Dieser Code ist im Grunde Ihre createTrackImageViewWithCustomClickHandler,
        // aber so angepasst, dass er den Kontext explizit nimmt und den Klick-Handler weiterreicht.
        return ImageView(context).apply {
            id = View.generateViewId()
            setImageResource(positionResMap[position] ?:0)
            adjustViewBounds = true

            val imageLayoutParams = LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                weight = 1f
                setMargins(marginPx, marginPx, marginPx, marginPx)
            }
            this.layoutParams = imageLayoutParams
            scaleType = ImageView.ScaleType.FIT_CENTER

            isClickable = true
            isFocusable = true
            contentDescription = position.toString()

            setOnClickListener {
                onPositionClicked(position) // Der übergebene Klick-Handler wird hier aufgerufen
            }
        }
    }
}