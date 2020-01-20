package de.gchelper

import de.gchelper.filehelper.*
import de.gchelper.cachedata.*

class App {
    static USER_HOME = System.getProperty('user.home')
    static USER_DOWNLOADS = USER_HOME + File.separator + "Downloads"

    static void main(String[] args) {
        def caches = []
        def myGpxFilePath = USER_DOWNLOADS + File.separator + "caches.gpx"
        def myCsvFile = USER_DOWNLOADS + File.separator + "caches.csv"
        myGpxFilePath = null

        if (myCsvFile) {
            def csv = new CsvFile(myCsvFile, false)
            def outFile = new File(USER_DOWNLOADS + File.separator + "myNewCacheFileFromCsv.gpx")
            outFile.delete()
            outFile << csv.getCaches()
            csv.getCaches().eachWithIndex { it, i ->
                println "i: " + i
                println it.getOverview()
                caches << it
            }
            new GpxFile().getGpxFile((GcCache[])caches, USER_DOWNLOADS + File.separator + "myNewCacheFileFromCsv.gpx")
        }
        if (myGpxFilePath) {
            def gpx = new GpxFile(myGpxFilePath)
            gpx.getCaches().eachWithIndex { it, i ->
                println "i: " + i
                println it.getOverview()
//            println it.gcDescription
//                println "Changing coordinates from " + it.gcCoords + " (" + it.getCoordsDecDegrees() + ")"
//                println it.getCoordsLatDeg() + ", " + it.getCoordsLonDeg()
//                println "Bearing " + getBearExped(it.gcDescription) + ", Distance " + getDistExped(it.gcDescription)
//                it.coordsProjection(getDistExped(it.gcDescription), getBearExped(it.gcDescription))
//            println "Offset N " + getOffsetLatFbptAbc(it.gcDescription) + ", E " + getOffsetLonFbptAbc(it.gcDescription)
//            it.coordsAddOffset(getOffsetLatFbptAbc(it.gcDescription), getOffsetLonFbptAbc(it.gcDescription))
//                println "New coordinates " + it.gcCoords + " (" + it.getCoordsDecDegrees() + ")"
//                println it.getCoordsLatDeg() + ", " + it.getCoordsLonDeg()
                caches << it
            }
//            gpx.getGpxFile((GcCache[])caches, USER_DOWNLOADS + File.separator + "myNewCacheFile.gpx")
        }
    }

    // Expedition
    static getBearExped(str) {
        def iFrom = str.indexOf(">", str.indexOf("Grad") - 10)
        def iTo = str.indexOf("Grad", iFrom)
        return str.substring(iFrom + 1, iTo).replaceAll('[.]', "").replaceAll(" ", "").toInteger()
    }

    // Expedition
    static getDistExped(str) {
        def iFrom = str.indexOf("Grad")
        def iTo = str.indexOf("Meter", iFrom)
        return str.substring(iFrom + 5, iTo).replaceAll("[.]", "").replaceAll(" ", "").toInteger()
    }

    // ABC
    static getOffsetLatAbc(str) {
        def iFrom = str.indexOf(">N")
        def iTo = str.indexOf("<", iFrom + 2)
        return str.substring(iFrom + 2, iTo).replaceAll('[.]', "").replaceAll(/\p{Z}/, "").replaceAll("&nbsp;", "").toFloat().div(1000)
    }

    // ABC
    static getOffsetLonAbc(str) {
        def iFrom = str.indexOf("O", str.indexOf(">N"))
        def iTo = str.indexOf("<", iFrom)
        return str.substring(iFrom + 1, iTo).replaceAll("[.]", "").replaceAll(/\p{Z}/, "").replaceAll("&nbsp;", "").toFloat().div(1000)
    }

    // Staufertour
    static getOffsetLatStaufer(str) {
        def iFrom = str.indexOf("N", str.indexOf("#"))
        def iTo = str.indexOf("E", iFrom)
        return str.substring(iFrom + 1, iTo).replaceAll('[.]', "").replaceAll(" ", "").toFloat().div(1000)
    }

    // Staufertour
    static getOffsetLonStaufer(str) {
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
