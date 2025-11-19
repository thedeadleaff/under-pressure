import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * <hr>
 * 
 * <table width="100%" summary="GoFitts title">
 * <tr style="background-color:#cccccc">
 * <td align="center">
 * <h1>GoFitts</h1>
 * </table>
 * 
 * <h2>Summary</h2>
 * 
 * <ul>
 * <li>Home screen for launching Fitts' law experiment software and utilities
 * </ul>
 * 
 * <h2>Background</h2>
 * <p>
 * 
 * GoFitts combines several Fitts' law apps in a single GUI. To run GoFitts,
 * your system must have the Java Runtime Environment (JRE). There is a good
 * chance your system already has the JRE, so just follow the Quick Start
 * instructions below. If you need the JRE, it is available from Oracle as a
 * free download.
 * <a href="http://www.oracle.com/technetwork/java/javase/downloads/index.html"
 * target="_blank"  style="color:#FF0000;">Click here</a> to visit Oracle's download site.
 * 
 * 
 * <h2>Quick Start</h2>
 * 
 * <ol>
 * <li><a href="http://www.yorku.ca/mack/FittsLawSoftware/GoFitts.jar" style="color:#FF0000;">Click
 * here</a> to download <tt>GoFitts.jar</tt>.  Depending on your browser, you might need to right-click and choose "Save link as...".
 * 
 * <li>Put the JAR file in a directory somewhere in your workspace.
 * 
 * <li>Double-click on the JAR file. GoFitts launches and the home screen
 * appears:
 * 
 * <blockquote><img src="GoFitts-1.jpg" alt="GoFitts-1"> </blockquote>
 * 
 * <li>Click the radio button for the desired utility.
 * 
 * <li>Click "Go".
 * </ol>
 * 
 * <h2>Security Note</h2>
 * <p>
 * 
 * Since a <tt>.jar</tt> file is executable, your browser might popup a security warning or block the download. Don't worry, 
 * GoFitts is safe.  You can allow the download. GoFitts' only purpose is experimental testing of pointing devices and interaction techniques. 
 * 
 * <h2>Installation</h2>
 * 
 * The first time GoFitts executes, a configuration file (<tt>FittsTask.cfg</tt>) is created in the same directory as GoFitts.  This file contains setup parameters
 * to configure the operating mode for GoFitts.  A directory (<tt>images</tt>) is also created containing image files used with GoFitts.
 * 
 * <h2>Documentation</h2>
 * 
 * Full details on the operation of each GoFitts utility are found in the APIs.
 * The APIs are accessed from the "View API in Browser" button that
 * appears at the bottom-left in the GUI for each utility:
 * 
 * <blockquote> <img src="GoFitts-2.jpg" alt="GoFitts-2"> </blockquote>
 * <p>
 * 
 * You'll need an Internet connection to access the API.
 * <p>
 * 
 * I'm happy to share the source files. Just get in touch and I'll send them to
 * you. If you have any questions, comments, or suggestions, please contact me
 * (mack "at" yorku.ca).
 * <p>
 * 
 * Good luck.
 * <p>
 * 
 * @author Scott MacKenzie 2017-2025
 */

public class GoFitts
{
	static final String APP_NAME = "GoFitts";
	
	// base URL
	static final String API_LOCATION = "http://www.yorku.ca/mack/FittsLawSoftware/doc/"; 

	final static int FITTS_TASK = 50;
	final static int MERGE_SD1_FILES = 200;
	final static int MERGE_SD2_FILES = 300;
	final static int FITTS_TRACE = 400;
	final static int FITTS_TILT_TRACE = 500;

	public static void main(String[] args)
	{
		 //use look and feel for "this" system
//		 try
//		 {
//		 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		 } catch (Exception e)
//		 {
//		 }

		/*
		 * This was a time consuming bug to resolve. For some reason, the color
		 * buttons in the setup dialog stopped showing the color in the button
		 * background. I'm not sure exactly when or why this happened, but it
		 * was probably the result of a JRE upgrade, or something. It seems the
		 * problem was caused by a change in the "WindowsLookAndFeel", which was
		 * the default L&F returned above when calling the
		 * getSystemLookAndFeelClassName method. Below, I am explicitly using a
		 * different L&F, rather than using the system default. Everything is
		 * working fine now; i.e., the colors appear in the background in the
		 * JButtons for the color options. NOTE: I haven't tested this on other
		 * machines, such as MacOS machines. (S.M., Oct 19, 2020)
		 */

		try
		{
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			// UIManager.setLookAndFeel("javax.swing.plaf.metal.NimbusLookAndFeel");
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
		} catch (Exception e)
		{
		}

		GoFittsFrame frame = new GoFittsFrame(FITTS_TASK, new File("."));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(APP_NAME);
		frame.pack();

		// position application window in center of display
		Dimension d1 = frame.getSize();
		Dimension d2 = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (d2.width - d1.width) / 2;
		int y = (d2.height - d1.height) / 2;
		frame.setLocation(new Point(x, y));
		frame.setVisible(true);
	}

	// This method is called from one of the utilities when the "Open API in Browser" button is clicked
	public static boolean openBrowser(String sArg)
	{
		String s = API_LOCATION + sArg;
		if (Desktop.isDesktopSupported())
		{
			Desktop desktop = Desktop.getDesktop();
			try
			{
				URI uri = new URI(s);
				desktop.browse(uri);
			} catch (IOException ex)
			{
				return false;
			} catch (URISyntaxException ex)
			{
				return false;
			}
		}
		return true;
	}

	// do all the setup things via a popup dialog and return a Configuration object (or null if there were any errors)
	public static Configuration doSetup(Frame f, String appName, SetupItemInfo[] setupItemInfo, String[] otherFiles)
	{
		// the default setting for each parameter is the 1st entry in the values array given to SetupItemInfo
		String[] defaultParameters = new String[setupItemInfo.length];
		for (int i = 0; i < setupItemInfo.length; ++i)
			defaultParameters[i] = setupItemInfo[i].itemValues[0];

		// read the configuration parameters from the .cfg file
		Configuration c = Configuration.readConfigurationData(appName, defaultParameters, otherFiles);

		if (c == null)
		{
			showError("Error reading configuration data from " + appName + ".cfg!");
			return null;
		}

		// use setup to allow changes to the default or existing configuration
		Setup s = new Setup(f, c, appName, setupItemInfo);
		return c;
		// if (s.showSetup(f))
		// {
        //     return c;
        // } else
        //     return null;
	}

	// return a base file name of the general form
	// APP-PARTICIPANT-CONDITION-SESSION-BLOCK
	// NOTE: the block code is computed herein as the 1st available block code
	
	// RMS - interactionMethodCode added as a parameter
	public static String getBaseFileName(String appName, String participantCode, String conditionCode,
			String sessionCode, String groupCode, String taskType, String selectionMethodCode, String interactionMethodCode, String cursorCode)
	{
		int blockCodeNumber = 0;
		String baseFileName;
		String testFileName; // baseFileName appended with ".sd1"
		do // find next available block code (using sd1 file as exemplar)
		{
			++blockCodeNumber;
			baseFileName = String.format("%s-%s-%s-%s-%s-%s-%s-%s-%s-B%02d", appName, participantCode, conditionCode,
					sessionCode, groupCode, taskType, selectionMethodCode, interactionMethodCode, cursorCode, blockCodeNumber); // RMS - interactionMethodCode in the file names
			testFileName = String.format("%s.sd1", baseFileName);
		} while (new File(testFileName).exists());
		return baseFileName;
	}

	// return a BufferedWriter reference for writing output data (or null if there were any problems)
	public static BufferedWriter openBufferedWriter(String fileName)
	{
		BufferedWriter bw;
		try
		{
			bw = new BufferedWriter(new FileWriter(fileName));
		} catch (IOException e)
		{
			showError("Error: opening data file: " + fileName);
			return null;
		}
		return bw;
	}

	// close a BufferedWriter
	public static void closeBufferedWriter(BufferedWriter bw)
	{
		try
		{
			bw.close();
		} catch (IOException e)
		{
			System.err.println("Error closing data file!");
			return;
		}
	}

	// write a string of data to a BufferedWriter
	public static void writeData(BufferedWriter bw, String data)
	{
		try
		{
			bw.write(data, 0, data.length());
			bw.flush();

		} catch (IOException e)
		{
			showError("Error writing data file(s)!");
		}
	}

	// show an error message in a popup dialog
	static void showError(String msg)
	{
		JLabel message = new JLabel(msg);
		message.setFont(new Font("sansserif", Font.PLAIN, 14));
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}

class GoFittsFrame extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;

	JRadioButton fittsTask, mergeSD1, mergeSD2, fittsTrace, fittsTiltTrace;
	JButton go, exit;
	ButtonGroup buttonGroup;
	File currentDirectory;

	GoFittsFrame(int defaultApp, File currentDirectoryArg)
	{
		currentDirectory = currentDirectoryArg;

		// ===================================
		// create and configure GUI components
		// ===================================
		go(new FittsTask(currentDirectory), "FittsTask");
		go.requestFocusInWindow();

		// JLabel welcome = new JLabel("Welcome to GoFitts");
		// welcome.setFont(new Font("sanserif", Font.BOLD, 20));
		// welcome.setHorizontalAlignment(SwingConstants.CENTER);

		// JLabel chooseApp = new JLabel("What would you like to do?");
		// chooseApp.setFont(new Font("sanserif", Font.BOLD, 16));
		// chooseApp.setHorizontalAlignment(SwingConstants.CENTER);

		// go = new JButton("Go");
		// exit = new JButton("Exit");

		// fittsTask = new JRadioButton("FittsTask (1D or 2D)");
		// mergeSD1 = new JRadioButton("Merge sd1 Files");
		// mergeSD2 = new JRadioButton("Merge sd2 Files");
		// fittsTrace = new JRadioButton("FittsTrace");
		// fittsTiltTrace = new JRadioButton("FittsTiltTrace");
		
		// fittsTask.setFont(new Font("sanserif", Font.BOLD, 14));
		// mergeSD1.setFont(new Font("sanserif", Font.BOLD, 14));
		// mergeSD2.setFont(new Font("sanserif", Font.BOLD, 14));
		// fittsTrace.setFont(new Font("sanserif", Font.BOLD, 14));
		// fittsTiltTrace.setFont(new Font("sanserif", Font.BOLD, 14));		

		// fittsTask.setActionCommand("fittstask");
		// mergeSD1.setActionCommand("mergesd1files");
		// mergeSD2.setActionCommand("mergesd2files");
		// fittsTrace.setActionCommand("fittstrace");
		// fittsTiltTrace.setActionCommand("fittstilttrace");

		// buttonGroup = new ButtonGroup();
		// buttonGroup.add(fittsTask);
		// buttonGroup.add(mergeSD1);
		// buttonGroup.add(mergeSD2);
		// buttonGroup.add(fittsTrace);
		// buttonGroup.add(fittsTiltTrace);

		// switch (defaultApp)
		// {
		// 	case GoFitts.FITTS_TASK:
		// 		fittsTask.setSelected(true);
		// 		break;

		// 	case GoFitts.MERGE_SD1_FILES:
		// 		mergeSD1.setSelected(true);
		// 		break;

		// 	case GoFitts.MERGE_SD2_FILES:
		// 		mergeSD2.setSelected(true);
		// 		break;

		// 	case GoFitts.FITTS_TRACE:
		// 		fittsTrace.setSelected(true);
		// 		break;

		// 	case GoFitts.FITTS_TILT_TRACE:
		// 		fittsTiltTrace.setSelected(true);
		// 		break;
		// }

		// // ================
		// // attach listeners
		// // ================

		// // go button gets the focus when the frame is activated
		// this.addWindowFocusListener(new WindowAdapter()
		// {
		// 	public void windowGainedFocus(WindowEvent e)
		// 	{
		// 		go.requestFocusInWindow();
		// 	}
		// });

		// fittsTask.addActionListener(this);
		// mergeSD1.addActionListener(this);
		// mergeSD2.addActionListener(this);
		// fittsTrace.addActionListener(this);
		// fittsTiltTrace.addActionListener(this);
		// go.addActionListener(this);
		// exit.addActionListener(this);

		// // =============
		// // layout panels
		// // =============

		// JPanel appNamePanel = new JPanel();
		// appNamePanel.add(welcome);
		// appNamePanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

		// JPanel instructionsPanel = new JPanel();
		// instructionsPanel.add(chooseApp);
		// instructionsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

		// JPanel appPanel = new JPanel(new GridLayout(0, 1)); // vertical
		// appPanel.add(fittsTask);
		// appPanel.add(mergeSD1);
		// appPanel.add(mergeSD2);
		// appPanel.add(fittsTrace);
		// appPanel.add(fittsTiltTrace);

		// JPanel appHolderPanel = new JPanel();
		// appHolderPanel.add(appPanel);

		// JPanel choiceInnerPanel = new JPanel();
		// choiceInnerPanel.setLayout(new BoxLayout(choiceInnerPanel, BoxLayout.Y_AXIS));
		// choiceInnerPanel.add(instructionsPanel);
		// choiceInnerPanel.add(appHolderPanel);
		// choiceInnerPanel.setBorder(new TitledBorder(new EtchedBorder(), ""));

		// JPanel choiceOuterPanel = new JPanel();
		// choiceOuterPanel.add(choiceInnerPanel);

		// JPanel buttonPanel = new JPanel();
		// buttonPanel.add(go);
		// buttonPanel.add(exit);

		// JPanel all = new JPanel();
		// all.setLayout(new BoxLayout(all, BoxLayout.Y_AXIS));
		// all.add(appNamePanel);
		// all.add(choiceOuterPanel);
		// all.add(buttonPanel);
		// all.setBorder(new EtchedBorder());

		// JPanel topLevel = new JPanel(); // prevents vertical stretching when
		// 								// resized
		// topLevel.add(all);

		// this.setContentPane(topLevel);
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		Object source = ae.getSource();

		if (source == go)
		{
			String command = this.buttonGroup.getSelection().getActionCommand();
			this.dispose();
			switch (command)
			{
				case "fittstask":
					go(new FittsTask(currentDirectory), "FittsTask");
					break;

				case "mergesd1files":
					go(new MergeSD1Files(currentDirectory), "MergeSD1Files");
					break;

				case "mergesd2files":
					go(new MergeSD2Files(currentDirectory), "MergeSD2Files");
					break;

				case "fittstrace":
					go(new FittsTrace(currentDirectory), "FittsTrace");
					break;

				case "fittstilttrace":
					go(new FittsTiltTrace(currentDirectory), "FittsTiltTrace");
					break;
			}
		}

		else if (source == fittsTask)
			go.requestFocusInWindow();
		else if (source == mergeSD1)
			go.requestFocusInWindow();
		else if (source == mergeSD2)
			go.requestFocusInWindow();
		else if (source == fittsTrace)
			go.requestFocusInWindow();
		else if (source == fittsTiltTrace)
			go.requestFocusInWindow();
		else if (source == exit)
			System.exit(0); // bye! bye!

	}

	void go(JFrame frame, String title)
	{
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(title);
		frame.pack();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
	}
}
