package corrupted;

public abstract class ErrorHandler {
	public static boolean exitOnNextUpdate = false;
	public static final String EXPECTED_VIRUS = "ERROR: Non-Virus found in the Virus Grid.";
	public static final String EXPECTED_FUSEVIRUS = "ERROR: Non-FuseVirus found in FuseVirus expectant codeblock.";
	public static final String EXPECTED_CHAINVIRUS = "ERROR: Non-Chainvirus found in ChainVirus expectant codeblock.";
	public static final String EXPECTED_TILE = "ERROR: Non-tile found in the Tile Grid.";
	public static final String OUT_OF_BOUNDS = "ERROR: Array index out of bounds. Grid array sizes may have been unintentionally altered.";
	public static final String MUST_HAVE_GAME = "ERROR: GridElements must be Initialized with a Game reference. This must not be null.";
	
	/**
	 * This method is used to print error messages to the java console and quit the application.
	 * @param errorMessage error message to be print to the java console.
	 */
	public static void printErrorAndQuit(String errorMessage)
	{
		if (errorMessage.contains("unintent"))
		{
			exitOnNextUpdate = true;
		}
		System.err.println(errorMessage);
		exitOnNextUpdate = true;
		
		//TODO: when debugging, comment out the line below. there are some test cases meant to trigger this quit mechanism
		//removing thisline allows the test to continue.
		//System.exit(-1);
	}
}
