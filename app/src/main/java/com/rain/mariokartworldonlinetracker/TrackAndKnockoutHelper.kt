package com.rain.mariokartworldonlinetracker

object TrackAndKnockoutHelper {
    private val trackMap: Map<TrackName?, List<TrackName>> = mutableMapOf(
        null to TrackName.values().toList(),
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
        TrackName.PS15 to listOf(TrackName.MMM24, TrackName.CCF20, TrackName.FAO14, TrackName.KTB13, TrackName.CC2, TrackName.CM25),
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


}