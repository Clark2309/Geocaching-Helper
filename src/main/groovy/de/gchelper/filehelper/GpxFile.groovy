package de.gchelper.filehelper

import de.gchelper.*

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
            caches << new GcCache(  gcCode: it.name.text(), 
                                    gcTitle: it.desc.text(), 
                                    gcDescription: it."groundspeak:long_description".text(), 
                                    coordsDecDeg: [it.@lat, it.@lon])
        }
        return caches
    }
}
