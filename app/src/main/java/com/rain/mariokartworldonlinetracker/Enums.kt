package com.rain.mariokartworldonlinetracker

// Für den Status von "drivingFromTrackName"
enum class DrivingFromOption {
    UNKNOWN,
    LAST, // Verwendet den letzten bekannten drivingToTrackName
    NONE, // Wird leer gelassen oder ein spezifischer "None"-Wert
    OTHER // Benutzer gibt einen neuen Namen ein oder wählt aus einer Liste
}

enum class RaceCategory {
    UNKNOWN,
    RACE,
    KNOCKOUT
}

enum class RaceMode {
    UNKNOWN,
    _100CC,
    _150CC,
    MIRROR
}

enum class KnockoutCupName {
    GR1,
    IR2,
    MR3,
    SR4,
    CR5,
    AR6,
    CR7,
    HR8
}

enum class TrackName {
    MBC1,
    CC2,
    WSS3,
    DKSS4,
    DH5,
    SGB6,
    WS7,
    AF8,
    DKP9,
    SVP10,
    SHS11,
    WSY12,
    KTB13,
    FAO14,
    PS15,
    PB16,
    SSSW17,
    DDJ18,
    GBR19,
    CCF20,
    DD21,
    BC22,
    DBB23,
    MMM24,
    CM25,
    TF26,
    BC27,
    AH28,
    MC29,
    RR30
}