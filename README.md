# GISSystem
This stores and can show any GIS data through pr Quad trees, bufferPools, and hashTables.

The program imports data from different GIS data files.  It copies the valid data into another file, while also adding it to the prQuadTree, bufferPools, and hashTables.
The bufferPool stores at most 15 values that go from MRU to LRU.
What_is shows the data of the feature name being searched for using hashTables.
What_is_at shows the data at that current point using prQuadTrees.
What_is_in uses the inQuadrant method within the prQuadTrees.
I also have different classes which encompasses the data and tries to apply these different factors.
