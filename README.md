# Geocaching-Helper
Little Scripts to help solving some caches. Planned is also a bigger class for coords calculations

## Helpers
### bww.groovy
For a given text file all char values are summarized. All non char calues are ignored.

## Main Program
To use the programm a small workflow must be written in App class.
To keep track of found series an individual grabber for offset will be implemented.

### The main object 'GcCache'
This object provides the basic parameters of a geocache.

#### this.gcCoords
Provides the coordinates of the cache object as a String without blanks

#### getCoordsLat()
Provides the latitude of actual objects coords

#### getCoordsLon()
Provides the longitude of actual objects coords

#### getCoordsDecDegrees()
Provides the coordinates as decimaldegrees (D.d°)

#### getCoordsMinSec()
Provides the coordinates as degresse, minutes and seconds (D°M'S.s")

#### coordsAddOffset(latOffset, lonOffset)
Calculates an offset of the original coordinates with given float values 

#### coordsAddOffsetInt(latOffset, lonOffset)
Calculates an offset of the original coordinates with given integer values

#### coordsProjection(dist, bear)
Calculates a projection from original coordinates with given distance and bearance

### GPX-File
With this class it's possible to read from gpx files, modify some values and export the the results into a new gpx file that can be imported in e.g. L4C.

### Functions to come
* get cache information from web site (perhaps)