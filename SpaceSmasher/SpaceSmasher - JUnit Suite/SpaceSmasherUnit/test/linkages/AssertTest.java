package linkages;

import javax.swing.JOptionPane;

/**
 * The AssertTest checks for things you cannot handle programmatically, i.e. sounds
 * and is useful for other instances as well. A simple dialog box is launced
 * asking if the player the constructor question and displaying the name of the 
 * called method
 * 
 * Derived from Oracle -> http://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
 * 
 * @author Chris Sullivan
 */

public class AssertTest  {
	
	/** prompt and name of method **/
	private String question, name;
	private String[] selection = { "yes", "no" };
	
	/** constructor can be called like new AssertTest("question", "name").create()
		in order to quickly get a response, create must be called to get an 
		answer **/
	public AssertTest(String question, String name)
	{
		this.question = question;
		this.name = name;
	}
	
	/** returns the BLOCKING option pane selection
		Defaults to JSwing Look and Feel **/
	public int create()
	{
		
		// question is the dialog text
		// name is the title
		return JOptionPane.showOptionDialog(null,
			    question,
			    "method - " + name,
			    JOptionPane.YES_NO_CANCEL_OPTION,
			    JOptionPane.QUESTION_MESSAGE,
			    null,
			    selection, null);
	}
	
}
