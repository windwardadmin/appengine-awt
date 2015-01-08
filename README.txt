appengine-awt
=============

Original location: https://github.com/witwall/appengine-awt
Brought here by marcusj

This project replaces java.awt and javax.imagio since we can't use either of 
these classes in Google App Engine.  Specifically, the source code is here 
because modifications are needed in order to actually remove the File I/O 
dependencies.

=========
CHANGELOG
=========

 1.0.1 - changing a few File references to use gaevfs instead
         -- see FILEIO_INFO.txt for more information on the change.

 1.0.0 - initial import from github


============
DEPENDENCIES
============
	
IMPORTANT: You need to add the included jars to the local Maven repository in
		   order to build appengine-awt.  To do this, run the .bat script
		   located in the dep directory.

 -  Maven
	In order to build sfntly and appengine-awt, you need Maven (it was already
	set up well for both projects) -- Maven will basically generate all the
	jars without a second thought.

 -  sfntly (jar included in dep)
    Located at: https://code.google.com/p/sfntly/
	
 -  gaevfs (jar included in dep)
	Located at: https://code.google.com/p/gaevfs/
	
 -  commons-vfs (jar included in dep)
	Although the original project originates from Apache Commons, the jar included
	is a patched copy that came with gaevfs.

========
BUILDING
========

 -  This project uses Maven (I didn't want to set up ant and Maven worked
    perfectly well and was already set up).  You should check the Dependencies
	section before building to make sure dependencies are satisfied!!

		mvn clean - clean up generated files
		mvn compile - compile
		mvn test - run tests (haha j/k; there are none)
		mvn package - generate a jar <-- This is most likely what you want
		mvn install - install to the local Maven repository

 -  any output files generated are usually placed in target\ such as the jar and
    class files.

 -  The version number can be updated in pom.xml


=====
ABOUT
=====

 -  This project was downloaded from https://github.com/witwall/appengine-awt
    Original README below:

appengine-awt
=============
 fork from appengine-export:https://github.com/bedatadriven/appengine-export
 
appengine-awt is a pure java implementation of the java.awt and javax.imageio packages for use in the Google AppEngine environment.

The code is based mainly on the Apache Harmony and Apache Sanselan projects.

http://code.google.com/p/appengine-awt/
