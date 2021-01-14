# DJ-DIFT (Desktop Java-based Digital Image Forensic Tool)
A GUI tool made for my undergraduate thesis in digital forensics, specifically digital image forensices, utilize ELA (Error Level Analysis) and image masking.
* Includes metadata and thumbnail reader for any supported image format by its running JRE.
* Designed in Java8, tested in Java8 and Java10.
* Based on JAVA ELA project by Robert Streetman.

# Credits
This tool won't complete without:
1.  Java ELA Project by Robert Streetman (https://github.com/rstreet85/ELA)
2.  Metadata-extractor by Drew Noakes (https://github.com/drewnoakes/metadata-extractor)

# Warning
There is no limit on the size of image as an input, you can theoritically use more than 100+ MB image but its processing time will increased significantly and it will takes a huge amount of RAM if such image is used.

# Requirements
1.  JRE8 or JRE10 (JRE10 may cause incorrect UI layout while able to use TIF image as input).
2.  Metadata-extractor source file as a dependency.
