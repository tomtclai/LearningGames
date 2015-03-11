

import Engine.BaseCode;
import Engine.LibraryCode;
import GhostLight.JarResources;

public class test extends LibraryCode {
	
	public void initializeWorld() {
		super.initializeWorld();
	    BaseCode.resources.setClassInJar(new JarResources());
	}

}
