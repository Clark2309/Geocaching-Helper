package de.gchelper.filehelper

import com.opencsv.CSVParser
import com.opencsv.CSVReader
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import de.gchelper.cachedata.*

class CsvFile {
    List<String[]> content

    CsvFile(filename, boolean skipFirstLine) {
        def fh = new File(filename)
        int skipLines = skipFirstLine ? 1 : 0
        char separator = ","
        def Reader csvFileReader = new FileReader(fh)

        CSVParser parser = new CSVParserBuilder()
                .withIgnoreQuotations(true)
                .withSeparator(separator)
                .build()

        CSVReader csvReader = new CSVReaderBuilder(csvFileReader)
                .withSkipLines(skipLines)
                .withCSVParser(parser)
                .build()

        content = csvReader.readAll()

        csvReader.close()
        csvFileReader.close()
    }

    GcCache[] getCaches() {
        def caches = []
        List<String[]> colNames = content[0]
        List<String[]> cacheData = content[1..<content.size]

        cacheData.each {
            def gcCode = it[colNames.indexOf("gcCode")].toUpperCase().trim()
            println("Code: " + gcCode)
            def gcCorrCoords = it[colNames.indexOf("gcCorrCoords")]
            caches << new GcCache(gcCode: gcCode,
                    gcKey: gcCode.substring(2),
                    gcDescription: it[colNames.indexOf("gcDescription")],
                    gcCoordsDecDeg: ["0", "0"],
                    gcCorrectedCoordinates: new GcCacheWpt(gcCode: gcCode, gcCoordsDegDecMin: gcCorrCoords))
        }
        return caches
    }
}