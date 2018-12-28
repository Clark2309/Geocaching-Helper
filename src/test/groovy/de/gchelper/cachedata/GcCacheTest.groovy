package de.gchelper.cachedata

import junit.framework.TestCase
import groovy.util.GroovyTestCase

class GcCacheTest extends GroovyTestCase {
    
    void testInitGc() {
        def myGc = new GcCache(gcCode: "GC123", gcTitle: "mein Titel", gcDescription: "Desc", gcCoords: "N48°12.345 E011°12.345")

        assert "GC123 - (mein Titel) at N48°12.345\' E011°12.345\' (Size: null, D/T: null/null, Typ: null)" == myGc.getOverview()
    }

    void testGetCoordsLat() {
        def myGc = new GcCache(gcCoords: "N48°12.345 E011°12.345")

        assert "N48°12.345\'" == myGc.getCoordsLat()

        myGc = new GcCache(gcCoords: "N 48°12.345 E 011°12.345")
        assert "N48°12.345\'" == myGc.getCoordsLat()

        myGc = new GcCache(gcCoords: "N 48°2.345 E 011°12.345")
        assert "N48°02.345\'" == myGc.getCoordsLat()

        myGc = new GcCache(gcCoords: "N8°12.345 E011°12.345")
        assert "N08°12.345\'" == myGc.getCoordsLat()

        myGc = new GcCache(gcCoords: "N8°12,3 O011°12,345")
        assert "N08°12.300\'" == myGc.getCoordsLat()

        myGc = new GcCache(gcCoords: "N8°12.34 E011°12.345")
        assert "N08°12.340\'" == myGc.getCoordsLat()

        myGc = new GcCache(gcCoords: "N8°2.3 E011°12.345")
        assert "N08°02.300\'" == myGc.getCoordsLat()

        myGc = new GcCache(gcCoords: "N48°2.34 E011°12.345")
        assert "N48°02.340\'" == myGc.getCoordsLat()
    }

    void testGetCoordsLon() {
        def myGc = new GcCache(gcCoords: "N48°12.345 E011°12.345")

        assert "E011°12.345\'" == myGc.getCoordsLon()

        myGc = new GcCache(gcCoords: "N 48°12,345 E 11°12,345")
        assert "E011°12.345\'" == myGc.getCoordsLon()

        myGc = new GcCache(gcCoords: "N 48°2.345 E 1°12.345")
        assert "E001°12.345\'" == myGc.getCoordsLon()

        myGc = new GcCache(gcCoords: "N8°12.345 O11°2.345")
        assert "E011°02.345\'" == myGc.getCoordsLon()

        myGc = new GcCache(gcCoords: "N8°12.3 E011°2.34")
        assert "E011°02.340\'" == myGc.getCoordsLon()

        myGc = new GcCache(gcCoords: "N8°12.34 E1°2.34")
        assert "E001°02.340\'" == myGc.getCoordsLon()

        myGc = new GcCache(gcCoords: "N8°2.3 E1°1.3")
        assert "E001°01.300\'" == myGc.getCoordsLon()

        myGc = new GcCache(gcCoords: "N8°2.3 E1°2.5")
        assert "E001°02.500\'" == myGc.getCoordsLon()
    }

    void testCoordsCalcOffset() {
        def myGc = new GcCache(gcCoords: "N 48° 44.720' E 011° 43.640'")

        float latOffset =  0.259
        float lonOffset =  -0.07

        myGc.coordsAddOffset(latOffset, lonOffset)
        assert "N48°44.979\'" == myGc.getCoordsLat()
        assert "E011°43.570\'" == myGc.getCoordsLon()

        myGc = new GcCache(gcCoords: "N 48° 44.720' E 011° 43.640'")

        latOffset =  259
        lonOffset =  -70

        myGc.coordsAddOffsetInt(latOffset, lonOffset)
        assert "N48°44.979\'" == myGc.getCoordsLat()
        assert "E011°43.570\'" == myGc.getCoordsLon()
    }

    void testCoordsProjection() {
        def myGc = new GcCache(gcCoords: "N 48° 18.906' E 017° 36.853'")

        float dist =  17.1
        float bear =  347.5
        myGc.coordsProjection(dist, bear)
        assert "N48°18.915\'" == myGc.getCoordsLat()
        assert "E017°36.850\'" == myGc.getCoordsLon()
    }
}