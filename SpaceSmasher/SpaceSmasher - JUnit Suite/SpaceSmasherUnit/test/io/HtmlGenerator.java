package io;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * 	HtmlGenerator extends RunListener which normally outputs data into an xml file
 *  this was useful but less readable than an html file which can be populated
 *  dynamically. This was made to be scaleable as well which can handle multiple test case
 *  classes in case more are added later. 
 *  
 *  WARNING - mucking about with the html markup within this file may/will be reflected in
 *  the outputted html file. If you desire a change to this class make sure you clearly 
 *  know what is being written to the html file before adding changes.
 * 
 * @author Chris Sullivan 2014
 */

public class HtmlGenerator extends RunListener {
	
	/**
	 * debugging data for html population - disregard
	 * 
	 * suite
	 * 		class
	 * 			function
	 * 				status
	 * 			function
	 * 		class
	 * 			functions
	 * 				status
	 * 		...
	 * 
	 */
	
	/** A holder for tested Classes, names and table entries **/
	private TreeMap<String, String> map;
	
	/** output and filename variables **/
	private PrintWriter out = null;
	private String SUITE_NAME = "";

	public HtmlGenerator(File fout, String suite)
	{
		try {
			out = new PrintWriter(fout);
		} catch (FileNotFoundException e) {
			System.out.println("HtmlGenerator Output Error : " + e.toString());
			System.exit(-1);
		}
		
		map = new TreeMap<String, String>();
		SUITE_NAME = suite;
	}
	
	/** if the test has just started create the header for the html file as well as the beginning table
		for the first class **/
	@Override
	public void testRunStarted(Description description) throws Exception {
		testCount = description.testCount();
		
		// html markup and inline css styling
		out.write("<!DOCTYPE HTML>\n<html>\n<body>\n");
		out.write("<style>h1, h2{margin-bottom: 0px} table{table-layout: fixed;" + 
				" border-collapse:collapse; } table, td {border:1px solid black; " + 
				"vertical-align:text-top;}</style>");
		
		// title for the suite name
		DateFormat df = DateFormat.getDateInstance(DateFormat.FULL);
		out.write("<h1>" + SUITE_NAME + " - " + df.format(new Date()) + "</h1>\n");
		super.testRunStarted(description);
	}

	private int testCount = 0;
	
	/** if the test is finished end the last table added and print a success result 
		message at the bottom of the html page. **/
	@Override
	public void testRunFinished(Result result) throws Exception {
		out.write("</table><p><b>" + (result.getRunCount() - amnt_failed) + 
				" / " + testCount + " successful<b></p>\n");
		out.write("</body>\n</html>");
		out.close();
		super.testRunFinished(result);
	}
	
	private boolean first = true;
	
	/** this is different than the method above as this handles the independent classes
		if a key does not exist in the map create a header with classname and then start a new table **/
	@Override
	public void testStarted(Description description) throws Exception {
		String className = description.getClassName();
		if(!map.containsKey(className))
		{
			map.put(className, "");
			if(first)
			{
				first = false;		// if leading table
				out.write("<h2 style='color: #3399CC'>" + className + "</h2>\n");
				out.write("<table width='100%'>\n");
			}
			else
			{
				out.write("</table>\n");		// if following tables, closes the first table
				out.write("<h2 style='color: #3399CC'>" + className + "</h2>\n");
				out.write("<table width='100%'>\n");
			}
		}
		super.testStarted(description);
	}

	/** if the test is finished on the independent class methods add the method name
		the current date it was tested and if it was successful -> no issues to the table **/
	@Override
	public void testFinished(Description description) throws Exception {
		if(!failed){
			out.write("<tr>\n<td style='font-weight: bold'>" + description.getMethodName() + "</td>\n");
			out.write("<td>" + new Date().toString() + "</td>\n");
			out.write("<td>no issues</td>\n");
		}
		failed = false;
		super.testFinished(description);
	}

	private boolean failed = false;
	private int amnt_failed = 0;
	
	/** if the test is finished on the independent class methods add the method name
		the current date it was tested and if it failed -> the stack trace from the failure **/
	@Override
	public void testFailure(Failure failure) throws Exception {
		failed = true;
		out.write("<tr>\n<td  style='font-weight: bold'>" 
					+ failure.getDescription().getMethodName() + "</td>\n");
		out.write("<td>" + new Date().toString() + "</td>\n");
		out.write("<td>" + failure.getMessage() + "\n" + failure.getTrace() + "</td>\n");
		amnt_failed++;
		super.testFailure(failure);
	}
	
}
