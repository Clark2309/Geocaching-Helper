/*
* This file calculates the bww from a given text file
* e.g. GC4HNFZ
*/

static main(args) {
    def myMap = "a".."z"
    myMap += "1".."9"
    def myText = new File(args[0]).getText()
    def bww = 0

    myText.each {
        def val = myMap.indexOf(it.toLowerCase())
        if (val != null) {
            val = val < 26 ? val + 1 : val - 25
            bww += val
        }
    }
    println "BWW: " + bww
}