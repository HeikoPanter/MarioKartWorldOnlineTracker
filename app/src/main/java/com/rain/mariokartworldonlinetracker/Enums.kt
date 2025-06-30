package com.rain.mariokartworldonlinetracker

// Für den Status von "drivingFromTrackName"
enum class DrivingFromOption {
    LAST, // Verwendet den letzten bekannten drivingToTrackName
    NONE, // Wird leer gelassen oder ein spezifischer "None"-Wert
    OTHER // Benutzer gibt einen neuen Namen ein oder wählt aus einer Liste
}

enum class RaceCategory {
    RACE,
    KNOCKOUT
}

enum class RaceMode {
    _100CC,
    _150CC,
    MIRROR
}