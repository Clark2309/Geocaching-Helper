# Geocaching-Helper
Little Scripts to help solving some caches. Planned is also a bigger class for coords calculations

## Helpers
### bww.groovy
For a given text file all char values are summarized. All non char calues are ignored.

## Main Program
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

### Functions to come
* convert gpx to csv
* make calculations on every csv value
* get cache information from web site (perhaps)
* build gpx with corrected coordiantes for import in L4C (hopefully)