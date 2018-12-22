package de.gchelper

class GcCache {
    String gcCode
    String gcTitle
    String gcDescription
    String gcCoords
    Map gcCoordsMap = [:]

    private void setGcCoords(c) {
        c = c.replaceAll(" ", "").replaceAll(",", ".").replaceAll("O", "E")
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
        lonDecMin = lonDecMin.size() < 3 ? lonDecMin + "0" + (lonDecMin.size() < 2 ? "0" : "") : latDecMin

        gcCoordsMap["latDir"] = latDir
        gcCoordsMap["latDeg"] = latDeg.toInteger()
        gcCoordsMap["latMin"] = latMin.toInteger()
        gcCoordsMap["latDecMin"] = latDecMin.toInteger()
        gcCoordsMap["latMinDec"] = "$latMin.$latDecMin".toFloat()
        gcCoordsMap["lonDir"] = lonDir
        gcCoordsMap["lonDeg"] = lonDeg.toInteger()
        gcCoordsMap["lonMin"] = lonMin.toInteger()
        gcCoordsMap["lonDecMin"] = lonDecMin.toInteger()
        gcCoordsMap["lonMinDec"] = "$lonMin.$lonDecMin".toFloat()

        gcCoords = "$latDir$latDeg°$latMin.$latDecMin$lonDir$lonDeg°$lonMin.$lonDecMin"
    }

    String getOverview() {
        return "Cache " + gcCode + " (" + gcTitle + ") at " + gcCoords
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
        return ""
    }
}