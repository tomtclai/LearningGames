package TowerDefense;

/*
 * This empty class is only here so that you will read this.
 * 
 * In your API, one of your classes inherits from LibraryCode.  The very first
 * line of code in this class's initializeWorld method should be as follows:
 * 
 * 			resources.setClassInJar(new JarResources());
 * 
 * This command is confusing, so let me try to explain it.
 * 
 * What this line is doing is trying to figure out where your files are located.
 * After this line, your program will consider its base directory to be the
 * directory in which it finds the JarResources.java file.
 * 
 * If you wanted, you could use any other .java file for this, but we use
 * this file to make it obvious that this file and the object instantiated
 * from it are not important due to their content.
 * 
 * When looking for art, sprites, and sound, the game engine will look in
 * the following places in the following order:
 * 
 * 		1.	base/resources/
 *      2.	../resources/
 *      3.	base/bin/resources/
 * 
 * So if you try to do something like:
 * 
 * 		BaseCode.resources.playLoopingSound("music.mp3");
 * 
 * The game will first look in base/resources, and if it finds a music.mp3 there, that's
 * what it will use.  Otherwise it will try the next location, and then the next.
 *      
 * Remember that "base" just means whatever folder your JarResources.java file
 * is in.
 * 
 * So yeah, make sure you have a JarResources.java in your project, and put your
 * resources folder in one of those 3 locations.
 */
public class JarResources {
	
}
