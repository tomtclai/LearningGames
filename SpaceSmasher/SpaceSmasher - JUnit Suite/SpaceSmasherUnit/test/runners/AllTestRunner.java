package runners;
import java.io.File;

import io.HtmlGenerator;

import org.junit.runner.JUnitCore;

/**
 * 	AllTestRunner acts a wrapper for AllTests which is in itself a test case of subsequent
 *  test cases. This class was made to provide a dedicated listener to the AllTest.class and
 *  output its results into an HTML file using the HtmlGenerator class.
 * 
 * @author Chris Sullivan 2014
 */

public class AllTestRunner {

	public static void main(String[] args)
	{
		String suite_name = "SpaceSmasher JUnit Suite";
		String dir = System.getProperty("user.dir");
		JUnitCore core = new JUnitCore();
		core.addListener(new HtmlGenerator(new File(dir + "/" + suite_name + " Results.html"), suite_name));
		core.run(AllTests.class);
	}
	
}
