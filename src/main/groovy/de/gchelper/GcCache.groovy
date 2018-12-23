package de.gchelper

class GcCache {
    String gcCode
    String gcTitle
    String gcDescription
    String gcCoords
    Map gcCoordsMap = [:]

    private void setGcCoords(c) {
        c = c.replaceAll(" ", "").replaceAll(",", ".").replaceAll("O", "E").replaceAll("\'", "")
        def cLat = c.substring(0,c.findIndexOf{name -> name =~ /[EW]/})
        def cLon = c.substring(c.findIndexOf{name -> name =~ /[EW]/})
        def latDir = cLat.substring(0,1)
        def latDeg = cLat.substring(1, cLat.indexOf("°"))
        def latMin = cLat.substring(cLat.indexOf("°") + 1, cLat.indexOf("."))
        def latDecMin = cLat.substring(cLat.indexOf(".") + 1)
        def lonDir = cLon.substring(0,1)
        def lonDeg = cLon.substring(1, cLon.indexOf("°"))
        def lonMin = cLon.substring(cLon.indexOf("°") + 1, cLon.indexOf("."))
        def lonDecMin = cLon.substring(cLon.indexOf(".") + 1)

        gcCoordsMap["latDir"] = latDir
        gcCoordsMap["latDeg"] = latDeg.toInteger()
        gcCoordsMap["latMin"] = latMin.toInteger()
        gcCoordsMap["latDecMin"] = latDecMin.toInteger()
        gcCoordsMap["lonDir"] = lonDir
        gcCoordsMap["lonDeg"] = lonDeg.toInteger()
        gcCoordsMap["lonMin"] = lonMin.toInteger()
        gcCoordsMap["lonDecMin"] = lonDecMin.toInteger()

        coordsUpdate()
    }

    String getOverview() {
        return "Cache " + gcCode + " (" + gcTitle + ") at " + gcCoords
    }

    void coordsUpdate() {
        // recalculate other formats
        // Must be calles after updating the degrees, minutes and minute decimals
        gcCoordsMap["latMinDec"] = gcCoordsMap["latMin"].toFloat() + gcCoordsMap["latDecMin"].toFloat() / 1000
        gcCoordsMap["latDecDeg"] = gcCoordsMap["latMinDec"] / 60
        gcCoordsMap["latSec"] = (gcCoordsMap["latDecMin"].toFloat() / 1000 * 60).round(3)
        gcCoordsMap["lonMinDec"] = gcCoordsMap["lonMin"].toFloat() + gcCoordsMap["lonDecMin"].toFloat() / 1000
        gcCoordsMap["lonDecDeg"] = gcCoordsMap["lonMinDec"] / 60
        gcCoordsMap["lonSec"] = (gcCoordsMap["lonDecMin"].toFloat() / 1000 * 60).round(3)

        def lat = gcCoordsMap["latDir"] + toTwoDigits(gcCoordsMap["latDeg"]) + "°" + toTwoDigits(gcCoordsMap["latMin"]) + "." + toThreeDigitsPost(gcCoordsMap["latDecMin"]) + "\'"
        def lon = gcCoordsMap["lonDir"] + toThreeDigits(gcCoordsMap["lonDeg"]) + "°" + toTwoDigits(gcCoordsMap["lonMin"]) + "." + toThreeDigitsPost(gcCoordsMap["lonDecMin"]) + "\'"
        gcCoords = lat + lon
    }

    // provides Latitude of actual coords
    String getCoordsLat() {
        def sep = gcCoords.findIndexOf{name -> name =~ /[EW]/}
        return gcCoords.substring(0,sep)
    }

    // provides Longitude of actual coords
    String getCoordsLon() {
        def sep = gcCoords.findIndexOf{name -> name =~ /[EW]/}
        return gcCoords.substring(sep)
    }

    String getCoordsDecDegrees() {
        def lat = gcCoordsMap["latDir"] + toTwoDigits(gcCoordsMap["latDeg"] + gcCoordsMap["latDecDeg"]) +  "°"
        def lon = gcCoordsMap["lonDir"] + toThreeDigits(gcCoordsMap["lonDeg"]+ gcCoordsMap["lonDecDeg"]) +  "°"
        return lat + lon
    }

    String getCoordsMinSec() {
        def lat = gcCoordsMap["latDir"] + toTwoDigits(gcCoordsMap["latDeg"]) +  "°" + toTwoDigits(gcCoordsMap["latMin"]) +  "\'" + toTwoDigits(gcCoordsMap["latSec"]) +  "\""
        def lon = gcCoordsMap["lonDir"] + toThreeDigits(gcCoordsMap["lonDeg"]) +  "°" + toTwoDigits(gcCoordsMap["lonMin"]) +  "\'" + toTwoDigits(gcCoordsMap["lonSec"]) +  "\"" 
        return lat + lon
    }

    void coordsAddOffset(latOffset, lonOffset) {
        def lat = gcCoordsMap["latMinDec"] + latOffset
        def lon = gcCoordsMap["lonMinDec"] + lonOffset

        gcCoordsMap["latMin"] = Math.floor(lat).toInteger()
        gcCoordsMap["lonMin"] = Math.floor(lon).toInteger()

        gcCoordsMap["latDecMin"] = Math.round((lat - gcCoordsMap["latMin"]) * 1000)
        gcCoordsMap["lonDecMin"] = Math.round((lon - gcCoordsMap["lonMin"]) * 1000)

        coordsUpdate()
    }

    void coordsAddOffsetInt(latOffset, lonOffset) {
        def lat = latOffset / 1000
        def lon = lonOffset / 1000
        coordsAddOffset(lat, lon)
    }

    void coordsProjection(dist, bear) {
        def lat = gcCoordsMap["latDeg"] + gcCoordsMap["latDecDeg"]
        def lon = gcCoordsMap["lonDeg"]+ gcCoordsMap["lonDecDeg"]

        def latRad = Math.PI / 180 * lat
        def lonRad = Math.PI / 180 * lon

        bear = Math.PI / 180 * bear
        dist = ( Math.PI * dist ) / ( 180 * 60 * 1852)
        
        def latCalc = Math.asin(Math.sin(latRad) * Math.cos(dist) + Math.cos(latRad) * Math.sin(dist) * Math.cos(bear))        
        def polarRad = -1 * Math.atan2(Math.sin(bear) * Math.sin(dist) * Math.cos(latRad), Math.cos(dist) - Math.sin(latRad) * Math.sin(latCalc))
        def lonCalc = (lonRad - polarRad + Math.PI) - Math.floor((lonRad - polarRad + Math.PI) / (2 * Math.PI)) - Math.PI

        gcCoordsMap["latDeg"] = Math.floor(180 / Math.PI * latCalc).toInteger()
        float latMin = ((180 / Math.PI * latCalc) - gcCoordsMap["latDeg"]) * 60
        gcCoordsMap["latMin"] = Math.floor(latMin).toInteger()
        latMin *= 1000
        gcCoordsMap["latDecMin"] = Math.floor(latMin.mod(1000)).toInteger()

        gcCoordsMap["lonDeg"] = Math.floor(180 / Math.PI * lonCalc).toInteger()
        float lonMin = ((180 / Math.PI * lonCalc) - gcCoordsMap["lonDeg"]) * 60
        gcCoordsMap["lonMin"] = Math.floor(lonMin).toInteger()
        lonMin *= 1000
        gcCoordsMap["lonDecMin"] = Math.floor(lonMin.mod(1000)).toInteger()

        coordsUpdate()
    }

    private String toTwoDigits(c) {
        return c < 10 ? "0" + c : c + ""
    }

    private String toThreeDigitsPost(c) {
        return c < 100 ? c + "0" + (c < 10 ? "0" : "") : c + ""
    }

    private String toThreeDigits(c) {
        return c < 100 ? "0" + (c < 10 ? "0" : "") + c : c + ""
    }
}