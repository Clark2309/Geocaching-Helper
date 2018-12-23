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
        latDeg = latDeg.size() < 2 ? "0" + latDeg : latDeg
        def latMin = cLat.substring(cLat.indexOf("°") + 1, cLat.indexOf("."))
        latMin = latMin.size() < 2 ? "0" + latMin : latMin
        def latDecMin = cLat.substring(cLat.indexOf(".") + 1)
        latDecMin = latDecMin.size() < 3 ? latDecMin + "0" + (latDecMin.size() < 2 ? "0" : "") : latDecMin
        def lonDir = cLon.substring(0,1)
        def lonDeg = cLon.substring(1, cLon.indexOf("°"))
        lonDeg = lonDeg.size() < 3 ? "0" + (lonDeg.size() < 2 ? "0" : "") + lonDeg : lonDeg
        def lonMin = cLon.substring(cLon.indexOf("°") + 1, cLon.indexOf("."))
        lonMin = lonMin.size() < 2 ? "0" + lonMin : lonMin
        def lonDecMin = cLon.substring(cLon.indexOf(".") + 1)
        lonDecMin = lonDecMin.size() < 3 ? lonDecMin + "0" + (lonDecMin.size() < 2 ? "0" : "") : lonDecMin

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
        gcCoordsMap["latMinDec"] = gcCoordsMap["latMin"].toFloat() + gcCoordsMap["latDecMin"].toFloat() / 1000
        gcCoordsMap["latDecDeg"] = gcCoordsMap["latMinDec"] / 60
        gcCoordsMap["latSec"] = (gcCoordsMap["latDecMin"].toFloat() / 1000 * 60).round(3)
        gcCoordsMap["lonMinDec"] = gcCoordsMap["lonMin"].toFloat() + gcCoordsMap["lonDecMin"].toFloat() / 1000
        gcCoordsMap["lonDecDeg"] = gcCoordsMap["lonMinDec"] / 60
        gcCoordsMap["lonSec"] = (gcCoordsMap["lonDecMin"].toFloat() / 1000 * 60).round(3)

        def lat = gcCoordsMap["latDir"] + (gcCoordsMap["latDeg"] + "°" + gcCoordsMap["latMin"]) + "." + gcCoordsMap["latDecMin"] + "\'"
        def lon = gcCoordsMap["lonDir"] + (gcCoordsMap["lonDeg"] + "°" + gcCoordsMap["lonMin"]) + "." + gcCoordsMap["lonDecMin"] + "\'"
        gcCoords = lat + lon
    }

    String getCoordsLat() {
        def sep = gcCoords.findIndexOf{name -> name =~ /[EOW]/}
        return gcCoords.substring(0,sep)
    }

    String getCoordsLon() {
        def sep = gcCoords.findIndexOf{name -> name =~ /[EOW]/}
        return gcCoords.substring(sep)
    }

    String getCoordsDecDegrees() {
        def lat = gcCoordsMap["latDir"] + (gcCoordsMap["latDeg"] + gcCoordsMap["latDecDeg"]) + "°"
        def lon = gcCoordsMap["lonDir"] + (gcCoordsMap["lonDeg"] + gcCoordsMap["lonDecDeg"]) + "°"
        return lat + lon
    }

    String getCoordsMinSec() {
        def lat = gcCoordsMap["latDir"] + gcCoordsMap["latDeg"] +  "°" + gcCoordsMap["latMin"] +  "\'" + gcCoordsMap["latSec"] +  "\""
        def lon = gcCoordsMap["lonDir"] + gcCoordsMap["lonDeg"] +  "°" + gcCoordsMap["lonMin"] +  "\'" + gcCoordsMap["lonSec"] +  "\"" 
        return lat + lon
    }
}