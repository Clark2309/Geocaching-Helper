package de.gchelper.cachedata

class GcCache extends AbstractWaypoint {
    static TYPE = "Geocache"
    static TRADI = "Traditional Cache"
    static MYST = "Unknown Cache"
    static MULTI = "Multi-cache"
    static EVENT = "Event Cache"

    String gcTitle = ""
    String gcType = ""
    String gcContainer = ""
    String gcDiff = ""
    String gcTerr = ""
    GcCacheWpt gcCorrectedCoordinates

    String getOverview() {
        return gcCode + " - (" + gcTitle + ") at " + gcCoords + " (korrigiert: " + gcCorrectedCoordinates.gcCoords + ") (Size: " + gcContainer + ", D/T: " + gcDiff + "/" + gcTerr + ", Typ: " + gcType + ")"
    }
}