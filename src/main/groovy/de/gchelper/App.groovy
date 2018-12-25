package de.gchelper

import de.gchelper.filehelper.*

class App {
    static USER_HOME = System.getProperty('user.home')
    static USER_DOWNLOADS = USER_HOME + File.separator + "Downloads"

    static void main(String[] args) {
        def myGpxFilePath = USER_DOWNLOADS + File.separator + "caches.gpx"
        def gpx = new GpxFile(myGpxFilePath)
        def caches = []

        gpx.getCaches().eachWithIndex { it, i ->
            println "i: " + i
            println it.getOverview()
            // println it.gcDescription
            println "Changing coordinates from " + it.gcCoords + " (" + it.getCoordsDecDegrees() + ")"
            println it.getCoordsLatDeg() + ", " + it.getCoordsLonDeg()
            println "Offset N " + getOffsetLatFbptAbc(it.gcDescription) + ", E " + getOffsetLonFbptAbc(it.gcDescription)
            it.coordsAddOffset(getOffsetLatFbptAbc(it.gcDescription), getOffsetLonFbptAbc(it.gcDescription))
            println "New coordinates " + it.gcCoords + " (" + it.getCoordsDecDegrees() + ")"
            println it.getCoordsLatDeg() + ", " + it.getCoordsLonDeg()
            caches << it
        }

        gpx.getGpxFile((GcCache[])caches, USER_DOWNLOADS + File.separator + "myNewCacheFile.gpx")
    }

    // ABC
    static getOffsetLatFbptAbc(str) {
        def iFrom = str.indexOf(">N")
        def iTo = str.indexOf("<", iFrom + 2)
        return str.substring(iFrom + 2, iTo).replaceAll('[.]', "").replaceAll(/\p{Z}/, "").replaceAll("&nbsp;", "").toFloat().div(1000)
    }

    // ABC
    static getOffsetLonFbptAbc(str) {
        def iFrom = str.indexOf("O", str.indexOf(">N"))
        def iTo = str.indexOf("<", iFrom)
        return str.substring(iFrom + 1, iTo).replaceAll("[.]", "").replaceAll(/\p{Z}/, "").replaceAll("&nbsp;", "").toFloat().div(1000)
    }

    // Staufertour
    static getOffsetLatFbptStaufer(str) {
        def iFrom = str.indexOf("N", str.indexOf("#"))
        def iTo = str.indexOf("E", iFrom)
        return str.substring(iFrom + 1, iTo).replaceAll('[.]', "").replaceAll(" ", "").toFloat().div(1000)
    }

    // Staufertour
    static getOffsetLonFbptStaufer(str) {
        def iFrom = str.indexOf("E", str.indexOf("#"))
        def iTo = str.indexOf("<", iFrom)
        return str.substring(iFrom + 1, iTo).replaceAll("[.]", "").replaceAll(" ", "").toFloat().div(1000)
    }

    // FBPT K
    static getOffsetLatFbptK(str) {
        def iFrom = str.indexOf("<b>", str.indexOf("(Startcoords Nord)"))
        def iTo = str.indexOf("<", iFrom + 3)
        return str.substring(iFrom + 3, iTo).replaceAll('[.]', "").replaceAll(" ", "").toFloat().div(1000)
    }

    // FBPT K
    static getOffsetLonFbptK(str) {
        def iFrom = str.indexOf("<b>", str.indexOf("(Startcoords Ost/East)"))
        def iTo = str.indexOf("<", iFrom + 3)
        return str.substring(iFrom + 3, iTo).replaceAll("[.]", "").replaceAll(" ", "").toFloat().div(1000)
    }

    // FBPT M
    static getOffsetLatFbptM(str) {
        def iFrom = str.indexOf("#", str.indexOf("Start-Koordinaten"))
        def iTo = str.indexOf("<", iFrom + 9)
        return str.substring(iFrom + 9, iTo).replaceAll('[.]', "").replaceAll(" ", "").toFloat().div(1000)
    }

    // FBPT M
    static getOffsetLonFbptM(str) {
        def iFrom = str.indexOf("E", str.indexOf("Start-Koordinaten"))
        def iTo = str.indexOf("<", iFrom + 41)
        return str.substring(iFrom + 41, iTo).replaceAll("[.]", "").replaceAll(" ", "").toFloat().div(1000)
    }
}
