import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Engine.Vector2;

@SuppressWarnings("serial")
public class SpriteControls extends JFrame implements ActionListener, ChangeListener
{
	private static int WINDOW_WIDTH = 650;
	private static int WINDOW_HEIGHT = 200;
	private static int TEXTFIELD_SIZE = 5;
	
	private SpriteDisplayer display;
	private JFileChooser imageFileOpener;
	private BufferedImage originalImage;
	private BufferedImage scaledSpriteSheet;
	
	private File batchFileSaveLoc = null;
	
	private int scaledSpriteWidth;
	private int scaledSpriteHeight;
	
	private JPanel mainPanel;
	private JPanel topPanel;
	private JPanel bottomPanel;
	private JPanel rightPanel;
	
	// Right Layer
	private JSlider resolutionSlider;
	private JLabel resolutionLabel;
	private JButton resolutionUpdate;
	
	// Top Layer
	private JButton loadImageButton;
	private JButton saveButton;
	private JButton loadJSButton;
	private JButton batchResizeButton;
	private JButton batchSaveLocButton;
	
	// Bottom Layer
	private JButton updateButton;

	// Middle Layer
	private JLabel spriteWidthLabel;
	private JLabel spriteHeightLabel;
	private JLabel spriteFramesLabel;
	private JLabel spriteTicksLabel;
	private JTextField spriteWidthText;
	private JTextField spriteHeightText;
	private JTextField spriteFramesText;
	private JTextField spriteTicksText;
	
	public SpriteControls(SpriteDisplayer displayInterface)
	{
		display = displayInterface;
		imageFileOpener = new JFileChooser();
		
		// Init Panels
		mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints mainGrid = new GridBagConstraints();
		mainGrid.gridx = 0;
		mainGrid.gridy = 0;
		
		topPanel = new JPanel();
		bottomPanel = new JPanel();
		rightPanel = new JPanel();
		
		// Setup the Top layer
		loadImageButton = new JButton("Load Sprite Sheet");
		saveButton = new JButton("Save");
		loadJSButton = new JButton("Load JS Text");
		batchResizeButton = new JButton("Batch Resize");
		batchSaveLocButton = new JButton("Choose Batch Save Location");
		
		loadImageButton.addActionListener(this);
		saveButton.addActionListener(this);
		loadJSButton.addActionListener(this);
		batchResizeButton.addActionListener(this);
		batchSaveLocButton.addActionListener(this);
		
		topPanel.add(loadImageButton);
		topPanel.add(loadJSButton);
		topPanel.add(saveButton);
		topPanel.add(batchResizeButton);
		topPanel.add(batchSaveLocButton);
		
		
		// Setup Bottom Layer
		updateButton = new JButton("Update Sprite Info");
		updateButton.addActionListener(this);
		bottomPanel.add(updateButton);
		
		// Setup Right Layer
		resolutionSlider = new JSlider(JSlider.VERTICAL, 1, 100, 100);
		resolutionSlider.setPreferredSize(new Dimension(20, 80));
		resolutionSlider.addChangeListener(this);

		rightPanel.add(resolutionSlider);
		
		resolutionLabel = new JLabel("100");
		
		resolutionUpdate = new JButton("Update Resolution");
		resolutionUpdate.addActionListener(this);
		rightPanel.add(resolutionLabel);
		rightPanel.add(resolutionUpdate);
		
		// Setup Middle Layer
		spriteWidthLabel = new JLabel("Sprite Width:");
		spriteWidthText = new JTextField(TEXTFIELD_SIZE);
		spriteWidthText.setText("10");
		mainPanel.add(spriteWidthLabel, mainGrid);
		mainGrid.gridx = 1;
		mainPanel.add(spriteWidthText, mainGrid);
		mainGrid.gridx = 0;
		mainGrid.gridy++;		
		
		spriteHeightLabel = new JLabel("Sprite Height:");
		spriteHeightText = new JTextField(TEXTFIELD_SIZE);
		spriteHeightText.setText("10");
		mainPanel.add(spriteHeightLabel, mainGrid);
		mainGrid.gridx = 1;
		mainPanel.add(spriteHeightText, mainGrid);
		mainGrid.gridx = 0;
		mainGrid.gridy++;
		
		spriteFramesLabel = new JLabel("Sprite Frames:");
		spriteFramesText = new JTextField(TEXTFIELD_SIZE);
		spriteFramesText.setText("10");
		mainPanel.add(spriteFramesLabel, mainGrid);
		mainGrid.gridx = 1;
		mainPanel.add(spriteFramesText, mainGrid);
		mainGrid.gridx = 0;
		mainGrid.gridy++;
		
		spriteTicksLabel = new JLabel("Sprite Ticks:");
		spriteTicksText = new JTextField(TEXTFIELD_SIZE);
		spriteTicksText.setText("10");
		mainPanel.add(spriteTicksLabel, mainGrid);
		mainGrid.gridx = 1;
		mainPanel.add(spriteTicksText, mainGrid);
		mainGrid.gridx = 0;
		mainGrid.gridy++;
		
		
		// test label
		//testLabel = new JLabel("No Test");
		//mainPanel.add(testLabel);
		
		
		// Setup the Frame
		this.getContentPane().add(mainPanel, BorderLayout.WEST);
		this.getContentPane().add(topPanel, BorderLayout.NORTH);
		this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		this.getContentPane().add(rightPanel, BorderLayout.EAST);
		this.setTitle("Controls");
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.setAlwaysOnTop(true);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) 
	{
		if(event.getSource() == loadImageButton)
		{	
			resolutionSlider.setValue(100);
			
			// Gets file
			imageFileOpener.setFileSelectionMode(JFileChooser.FILES_ONLY);
			imageFileOpener.showOpenDialog(this);

			// Attempts to read file display
			try
			{
				originalImage = ImageIO.read(imageFileOpener.getSelectedFile());
				
				display.loadSpriteSheet(originalImage,
						Integer.parseInt(spriteWidthText.getText()),
						Integer.parseInt(spriteHeightText.getText()),
						Integer.parseInt(spriteFramesText.getText()),
						Integer.parseInt(spriteTicksText.getText()));
			}
			catch (Exception e)
			{
				// Cry
			}
		}
		else if(event.getSource() == updateButton)
		{	
			// Attempts to read file display
			try
			{
				display.updateSprite(
						Integer.parseInt(spriteWidthText.getText()),
						Integer.parseInt(spriteHeightText.getText()),
						Integer.parseInt(spriteFramesText.getText()),
						Integer.parseInt(spriteTicksText.getText()));
			}
			catch (Exception e)
			{
				// Cry
			}
		}
		else if (event.getSource() == saveButton)
		{
			// Gets file
			imageFileOpener.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int retrival = imageFileOpener.showSaveDialog(this);

			if(retrival == JFileChooser.APPROVE_OPTION)
			{
				// Attempts to write to files.
				try
				{
					BufferedWriter out = new BufferedWriter(new FileWriter(imageFileOpener.getSelectedFile()+".txt"));
					out.write(imageFileOpener.getSelectedFile().getName());
					out.newLine();
					out.write( "Sprite Width: " + this.scaledSpriteWidth);
					out.newLine();
					out.write("Sprite Height: " + this.scaledSpriteHeight);
					out.newLine();
					out.write("Sprite Frames: " + spriteFramesText.getText());
					out.newLine();
					out.close();
					
					File outputfile = new File(imageFileOpener.getSelectedFile()+".png");
					ImageIO.write(scaledSpriteSheet, "png", outputfile);
					
					
				}
				catch (Exception e)
				{
					// Cry some more
				}
			}
		}
		else if(event.getSource() == resolutionUpdate)
		{
			int scaler = resolutionSlider.getValue();
			
			scaledSpriteSheet = scaleImage(originalImage,
					Integer.parseInt(spriteWidthText.getText()),
					Integer.parseInt(spriteHeightText.getText()),
					Integer.parseInt(spriteFramesText.getText()),
					scaler);
			
			display.loadSpriteSheet(scaledSpriteSheet,
					this.scaledSpriteWidth,
					this.scaledSpriteHeight,
					Integer.parseInt(spriteFramesText.getText()),
					Integer.parseInt(spriteTicksText.getText()));
		}
		else if(event.getSource() == loadJSButton)
		{
			// Gets file
			imageFileOpener.setFileSelectionMode(JFileChooser.FILES_ONLY);
			imageFileOpener.showOpenDialog(this);

			Vector2 size = null;
			int frames = 0;
			
			// Attempts to read file display
			try
			{
				size = getSizeFromFile(imageFileOpener.getSelectedFile());
				frames = getFramesFromFile(imageFileOpener.getSelectedFile());
			}
			catch (Exception e)
			{
				// Cry
			}
			
			this.spriteWidthText.setText(((int)size.getX()) + "");
			this.spriteHeightText.setText(((int)size.getY()) + "");
			this.spriteFramesText.setText(frames + "");
			this.spriteTicksText.setText("1");
			
			
			display.loadSpriteSheet(originalImage,
					(int)size.getX(),
					(int)size.getY(),
					frames,
					1);
		}
		else if(event.getSource() == batchSaveLocButton)
		{
			// Gets file
			imageFileOpener.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int retrival = imageFileOpener.showSaveDialog(this);

			if(retrival == JFileChooser.APPROVE_OPTION)
			{
				this.batchFileSaveLoc = imageFileOpener.getSelectedFile();
			}
		}
		else if(event.getSource() == batchResizeButton)
		{
			// Gets file
			imageFileOpener.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int retrival = imageFileOpener.showSaveDialog(this);

			if(retrival == JFileChooser.APPROVE_OPTION)
			{
				FilenameFilter pngOnly = new FilenameFilter() {
				    public boolean accept(File dir, String name) {
				        return name.endsWith(".png");
				    }
				};
				
				File spriteFolder = imageFileOpener.getSelectedFile();
				File[] listOfSprites = spriteFolder.listFiles(pngOnly);
				
				// For each file, get its sprite meta data and resize it as a new save
				for(int i = 0; i < listOfSprites.length; i++)
				{
					// Get the JS file
					String pathStr = listOfSprites[i].getPath();
					String nameAndPathOfFile = pathStr.substring(0, pathStr.lastIndexOf("."));
					String pathToJS = nameAndPathOfFile + ".js";
					File jsFile = new File(pathToJS);
					
					// Load JS file
					Vector2 size = getSizeFromFile(jsFile);
					int frames = getFramesFromFile(jsFile);
					
					// Load and resize
					
					try
					{
						originalImage = ImageIO.read(listOfSprites[i]);
					}
					catch (IOException e) {} // Do nothing, invalid data.
					
					// Load with whatever to get image info
					display.loadSpriteSheet(originalImage,
							(int)size.getX(),
							(int)size.getY(),
							frames,
							1);
					
					int scaler = resolutionSlider.getValue();
					
					// Get scaled version
					scaledSpriteSheet = scaleImage(originalImage,
							(int)size.getX(),
							(int)size.getY(),
							frames,
							scaler);
					
					// Give user something to look at.
					display.loadSpriteSheet(originalImage,
							(int)size.getX(),
							(int)size.getY(),
							frames,
							1);
					
					this.spriteWidthText.setText(((int)size.getX()) + "");
					this.spriteHeightText.setText(((int)size.getY()) + "");
					this.spriteFramesText.setText(frames + "");
					this.spriteTicksText.setText("1");
					
					
					if(batchFileSaveLoc == null)
					{
						batchFileSaveLoc = spriteFolder;
					}
					
					// Attempts to write to files.
					try
					{
						String nameOfFile = nameAndPathOfFile.substring(
								nameAndPathOfFile.lastIndexOf("\\"),
								nameAndPathOfFile.length());
						
						BufferedWriter out = new BufferedWriter(new FileWriter(batchFileSaveLoc + 
								nameOfFile + "_r" +".txt"));
						out.write(imageFileOpener.getSelectedFile().getName());
						out.newLine();
						out.write( "Sprite Width: " + this.scaledSpriteWidth);
						out.newLine();
						out.write("Sprite Height: " + this.scaledSpriteHeight);
						out.newLine();
						out.write("Sprite Frames: " + frames);
						out.newLine();
						out.close();
						
						File outputfile = new File(batchFileSaveLoc + nameOfFile + "_r" +".png");
						ImageIO.write(scaledSpriteSheet, "png", outputfile);
						
					}
					catch (Exception e)
					{
						// Cry some more
					}
					
					
				}

			}
		}
	}

	@Override
	public void stateChanged(ChangeEvent event) 
	{
		if(event.getSource() == resolutionSlider)
		{	
			int scaler = ((JSlider)event.getSource()).getValue();
			resolutionLabel.setText(scaler + "");
		}
	}

	private BufferedImage scaleImage(BufferedImage spriteSheet,
			int originalWidth, int originalHeight, int Frames, int scaler) 
	{
		float percentScale = scaler / 100f;
		
		int scaledWidth = (int)(originalWidth * percentScale);
		int scaledHeight = (int)(originalHeight * percentScale);
		
		
		// Calculate end of spriteSheet
		int numImagesPerRow = display.getColumns();
		int numOfRows = display.getRows();
		int endOfSheetY = numOfRows * scaledHeight;
		int endOfSheetX = numImagesPerRow * scaledWidth;
		
		BufferedImage scaledSheet = new BufferedImage(endOfSheetX, endOfSheetY, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = scaledSheet.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		
		for(int i = 0; i <= numOfRows; i++)
		{
			for(int j = 0; j <= numImagesPerRow; j++)
			{
			    g.drawImage(spriteSheet, j * scaledWidth, i * scaledHeight, scaledWidth, scaledHeight, 
			    		j * originalWidth, i * originalHeight, originalWidth, originalHeight, null);
			}
		}
		g.dispose();
		
		this.scaledSpriteWidth = scaledWidth;
		this.scaledSpriteHeight = scaledHeight;
		
		return scaledSheet;
	}
	
	/**
	 * This function only retrieves information of a very specific type of file.
	 * It will take the third and fourth number from a file and make that a vector.
	 * 
	 * @param file
	 * @return
	 */
	Vector2 getSizeFromFile(File file)
	{
		String str = null;
		
		try
		{
			byte[] encoded = Files.readAllBytes(file.toPath());
			str = new String(encoded, StandardCharsets.UTF_16LE);
		}
		catch(IOException e){}
		
	    Pattern pattern = Pattern.compile("[0-9]+");
	    Matcher matcher = pattern.matcher(str);
	    int numberX = 0;
	    int numberY = 0;
	    
	    matcher.find(); // Skip 2 zeros
	    matcher.find();
	    matcher.find();
	    
	    numberX = Integer.parseInt(matcher.group());
	    
	    matcher.find();
	    
	    numberY = Integer.parseInt(matcher.group());
	  
		return new Vector2(numberX, numberY);
	}
	int getFramesFromFile(File file)
	{
		String str = null;
		
		try
		{
			byte[] encoded = Files.readAllBytes(file.toPath());
			str = new String(encoded, StandardCharsets.UTF_16LE);
		}
		catch(IOException e){}
		
		// This regex gets all enclosing brackets that
		// don't have other brackets inside
	    Pattern pattern = Pattern.compile("\\[([^\\[\\]]*)\\]");
	    Matcher matcher = pattern.matcher(str);
	    
	    matcher.find(); // Skip first one (useless one)
	    
	    int count = 0;
	    while(matcher.find())
	    	count++;
	  
		return count;
	}
}
