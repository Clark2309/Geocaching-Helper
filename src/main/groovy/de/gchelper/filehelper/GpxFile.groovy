package de.gchelper.filehelper

import de.gchelper.cachedata.*
import groovy.xml.MarkupBuilder

class GpxFile {
    Node xmlContent
    String txtContent

    GpxFile(filename) {
        def f = new File(filename)        

        txtContent = f.getText('UTF-8')
        xmlContent = parseToXml(txtContent)
    }

    Node parseToXml(str) {
        return new XmlParser().parseText(str)
    }

    GcCache[] getCaches() {
        def caches = []

        xmlContent.wpt.each {
            if (it.name.text().startsWith("GC")) {
                caches << new GcCache(  gcCode: it.name.text(), 
                                        gcKey: it.name.text().substring(2),
                                        gcTitle: it."groundspeak:cache"."groundspeak:name".text(), 
                                        gcDescription: it."groundspeak:cache"."groundspeak:long_description".text(), 
                                        gcCoordsDecDeg: [it.@lat, it.@lon],
                                        gcType: it."groundspeak:cache"."groundspeak:type".text(),
                                        gcContainer: it."groundspeak:cache"."groundspeak:container".text(),
                                        gcDiff: it."groundspeak:cache"."groundspeak:difficulty".text(),
                                        gcTerr: it."groundspeak:cache"."groundspeak:terrain".text())
            } else {
                // println "Skipping " + it.name.text() + " - " + it."groundspeak:cache"."groundspeak:name".text()
            }
        }
        println "Count caches: " + caches.size()
        return caches
    }

    void getGpxFile(GcCache[] caches, path) {
        def xmlWriter = new StringWriter()
        def xmlMarkup = new MarkupBuilder(xmlWriter)

        xmlMarkup.mkp.xmlDeclaration(version: "1.0", encoding: "utf-8")
        xmlMarkup
            .'gpx'( "xmlns:xsd": "http://www.w3.org/2001/XMLSchema", 
	                "xmlns:xsi": "http://www.w3.org/2001/XMLSchema-instance", 
	                version: "1.0", 
	                creator: "Christof Hurst", 
	                "xsi:schemaLocation": "http://www.topografix.com/GPX/1/0 http://www.topografix.com/GPX/1/0/gpx.xsd http://www.groundspeak.com/cache/1/0/1 http://www.groundspeak.com/cache/1/0/1/cache.xsd", 
	                xmlns: "http://www.topografix.com/GPX/1/0") { 
                'name'('GcHelper Export')
                'desc'('Geocache file generated by GcHelper')
                'author'('GcHelper (Christof Hurst)')
                'email'('info@cf-edvservice.de')
                'keywords'('cache, geocache, groundspeak')
                caches.each { cache ->
                    'wpt'( lat: cache.getCoordsLatDeg(), lon: cache.getCoordsLonDeg()) {
                        'name'(cache.gcCode)
                        'desc'(cache.gcTitle + ", " + cache.gcType)
                        'sym'('Geocache')
                        'type'('Geocache|' + cache.gcType)
                        'groundspeak:cache'('xmlns:groundspeak': "http://www.groundspeak.com/cache/1/0/1") {
                            'groundspeak:name'(cache.gcTitle)
                            'groundspeak:long_description'(cache.gcDescription)
                            'groundspeak:type'(cache.gcType)
                            'groundspeak:container'(cache.gcContainer)
                            'groundspeak:difficulty'(cache.gcDiff)
                            'groundspeak:terrain'(cache.gcTerr)
                        }
                    }
                }
            }
        def outFile = new File(path)
        outFile.delete();
        outFile << xmlWriter.toString()
    }
}
