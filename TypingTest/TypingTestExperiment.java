import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * <h1>TypingTestExperiment</h1>
 * 
 * <h3>Summary</h3>
 * 
 * <ul>
 * <li>Experiment software to implement a simple typing test.
 * <p>
 * 
 * <li>Demonstrates the dependent measurements typically gathered in text entry experiments, such as entry speed (wpm),
 * KSPC, and the MSD error rate (%).
 * <p>
 * 
 * </ul>
 * <p>
 * 
 * <h3>Related References</h3>
 * 
 * <ul>
 * <li><a href="http://www.yorku.ca/mack/hci3.html">Text entry for mobile computing: Models and methods, theory and
 * practice</a>, by MacKenzie and Soukoreff (<i>Human-Computer Interaction</i>, 2002). This paper includes an extensive
 * analysis on typing (more broadly known as <i>text entry</i>), including input methods, models of interaction,
 * evaluation methods, and user performance measures.
 * <p>
 * 
 * <li><a href="http://www.yorku.ca/mack/hcimobile02.html">KSPC (keystrokes per character) as a characteristic of text
 * entry techniques</a>, by MacKenzie (<i>Proceedings of the Fourth International Symposium on Human Computer
 * Interaction with Mobile Devices</i>, 2002). This is the original paper that introduced <i>KSPC</i> (keystrokes per
 * character) as a metric for text entry research.
 * <p>
 * 
 * <li><a href="http://www.yorku.ca/mack/CHI01a.htm">Measuring errors in text entry tasks: An application of the
 * Levenshtein string distance statistic</a>, by Soukoreff and MacKenzie (<i>Extended Abstracts of the ACM Conference on
 * Human Factors in Computing Systems - CHI 2001</i>, 2001). This is the original paper that introduced the minimum
 * string distance (MSD) method for measuring error rates in text entry research.
 * <p>
 * </ul>
 * <p>
 * 
 * <h3>Running the Experiment Software</h3>
 * <p>
 * 
 * <a href="http://www.yorku.ca/mack/HCIbook/Running/">Click here</a> for instructions on launching/running the
 * application.
 * <p>
 * 
 * <h3>Setup Parameters</h3>
 * 
 * Upon launching, the program presents a setup dialog:
 * <p>
 * 
 * <center> <img src="TypingTestExperiment-1.jpg"> </center>
 * <p>
 * 
 * The default parameter settings are read from a configuration file called <a href="TypingTestExperiment.cfg">
 * <code>TypingTestExperiment.cfg</code></a>. This file is created automatically when the application is launched for
 * the first time. The default parameter settings may be changed through the setup dialog. The setup parameters are as
 * follows:
 * <p>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="6" valign="top">
 * <tr bgcolor="#cccccc" align="center">
 * <th>Parameter
 * <th>Description
 * 
 * <tr valign="top">
 * <td>Participant code
 * <td>Identifies the current participant. This is used in forming the names for the output data files. Also, the sd2
 * output data file includes a column with the participant code.
 * <p>
 * 
 * <tr valign="top">
 * <td>Condition code
 * <td>An arbitrary code used to associate a test condition with this invocation. This parameter might be useful if the
 * software is used in an experiment, since conditions might be tested that are not inherently part of the application
 * (e.g., Gender &rarr; male, female; Input method &rarr; mouse, stylus). The condition code is used in forming the name
 * for the output data file. Also, the sd2 output data file contains a column with the condition code.
 * <p>
 * 
 * <tr valign="top">
 * <td>Session code
 * <td>Identifies the session. This is useful if testing proceeds over multiple sessions to gauge the progression of
 * learning. The session code is used in forming the name for the output data file. Also, the sd2 output data file
 * contains a column with the condition code.
 * <p>
 * 
 * Note: The setup dialog does not include an entry for "Block code". The block code is generated automatically by the
 * software.
 * <p>
 * 
 * <tr valign="top">
 * <td>Number of phrases
 * <td>Specifies the number of phrases presented to the participant in the current block.
 * <p>
 * 
 * <tr valign="top">
 * <td>Phrases file
 * <td>Specifies a file containing phrases of text to be presented to participants for entry. Phrases are drawn from the
 * file at random. Typically, <a href="phrases2.txt">phrases2.txt</a> is used. This is the phrase set published by
 * MacKenzie and Soukoreff in 2003 (<a href="http://www.yorku.ca/mack/chi03b.html">click here</a>). Other file names may
 * be specified. For example, the file <a href="quickbrownfox.txt">quickbrownfox.txt</a> contains the phrase
 * <p>
 * 
 * <pre>
*     the quick brown fox jumps over the lazy dog
 * </pre>
 * 
 * This file might be useful for demonstration or specialized testing.
 * <p>
 * 
 * <tr valign="top">
 * <td>Show presented text during input
 * <td>A checkbox item that determines whether or not the presented text is visible during entry of a phrase. Either
 * way, the text phrase appears at the beginning of a trial. If this option is unchecked, the phrase will disappear when
 * the first keystroke is entered.
 * <p>
 * </table>
 * </blockquote>
 * <p>
 * 
 * <h3>Program Operation</h3>
 * <p>
 * 
 * When the main program window opens, a phrase of text appears in the presented text field. As the user types, text
 * appears in the transcribed text field. Timing begins with the first keystroke. At the end of each phrase, the user
 * presses the ENTER key. This terminates entry of the current phrase. Summary statistics are displayed and 
 * the next phrase to be entered appears.
 * <p>
 * 
 * Here's a screen snap of the program in action:
 * <p>
 * 
 * <center><img src = "TypingTestExperiment-2.jpg"></center>
 * <p>
 * 
 * <h3>Output Data Files</h3>
 * 
 * At the end of a block, two output data files are created, an "sd1" file and an "sd2" file. ("sd" is for
 * "summary data".) The sd1 file contains the presented and transcribed text followed by the timestamp and character associated with
 * each keystroke. The sd2 file contains summary data, one line per phrase.
 * <p>
 * 
 * Here are example output data files:
 * <p>
 * 
 * <ul>
 * <li><a href="TypingTestExperiment-sd1-example.txt">sd1 example</a>
 * <li><a href="TypingTestExperiment-sd2-example.txt">sd2 example</a>
 * </ul>
 * <p>
 * 
 * The data in the sd2 file are full-precision, comma-delimited. Importing into a spreadsheet application provides a
 * convenient method to examine the data on a phrase-by-phrase basis. Below is an example of how the data might look
 * after importing into Microsoft <i>Excel</i>: (click to enlarge)
 * <p>
 * 
 * <center> <a href="TypingTestExperiment-4.jpg"><img src="TypingTestExperiment-4.jpg" width=800></a> </center>
 * <p>
 * 
 * Actual output files use "TypingTestExperiment" as the base filename. This is followed by the participant code, the
 * condition code, the session code, and the block code, for example,
 * <code>TypingTestExperiment-P01-C01-S01-B01.sd2</code>.
 * <p>
 * 
 * <h3>Miscellaneous</h3>
 * 
 * When using this program in an experiment, it is a good idea to terminate all other applications and disable the
 * system's network connection. This will maintain the integrity of the data collected and ensure that the program runs
 * without hesitations.
 * <p>
 * 
 * @author Scott MacKenzie, 2001-2016
 * @author Steven Castellucci, 2014
 */
public class TypingTestExperiment
{
	final static String APP_NAME = "TypingTestExperiment";
	final static int PARAMETERS = 6; // must equal the number of parameters defined below

	// setup parameters (1st value is default)
	final static String[] PARTICIPANT_CODES = { "P00", "P01", "P02", "P03", "P04", "P05", "P06", "P07", "P08", "P09",
			"P10", "P11", "P12", "P13", "P14", "P15", "P16", "P17", "P18", "P19", "P20", "P21", "P22", "P23", "P24",
			"P25", "P26", "P27", "P28", "P29", "P30" };
	final static String[] CONDITION_CODE = { "C00" };
	final static String[] SESSION_CODES = { "S00", "S01", "S02", "S03", "S04", "S05", "S06", "S07", "S08", "S09", "S10",
			"S11", "S12", "S13", "S14", "S15", "S16", "S17", "S18", "S19", "S20", "S21", "S22", "S23", "S24", "S25",
			"S26", "S27", "S28", "S29", "S30" };
	final static String[] NUMBER_OF_PHRASES = { "5" };
	final static String[] PHRASES_FILE = { "phrases2.txt" };
	final static String[] SHOW_PRESENTED = { "true" };

	// identify other files needed (used when executing from the .jar file for the 1st time)
	final static String[] OTHER_FILES = { "phrases2.txt", "helloworld.txt", "quickbrownfox.txt", "phrases.txt" };

	public static void main(String[] args) throws IOException
	{
		// use Win32 look and feel
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e)
		{
		}

		/*
		 * Define the default parameters settings. These are only used if the .cfg file does not exit, such as when the
		 * application is launched for the 1st time.
		 */
		String[] defaultParameters = new String[PARAMETERS];
		defaultParameters[0] = PARTICIPANT_CODES[0];
		defaultParameters[1] = CONDITION_CODE[0];
		defaultParameters[2] = SESSION_CODES[0];
		defaultParameters[3] = NUMBER_OF_PHRASES[0];
		defaultParameters[4] = PHRASES_FILE[0];
		defaultParameters[5] = SHOW_PRESENTED[0];

		Configuration c = Configuration.readConfigurationData(APP_NAME, defaultParameters, OTHER_FILES);
		if (c == null)
		{
			System.out.println("Error reading configuration data from " + APP_NAME + ".cfg!");
			System.exit(0);
		}

		// ----------------------------------------------------------
		// user the app's parameter settings to create a setup dialog
		// ----------------------------------------------------------

		SetupItemInfo[] sii = new SetupItemInfo[PARAMETERS];
		sii[0] = new SetupItemInfo(SetupItem.COMBO_BOX, "Participant code ", PARTICIPANT_CODES);
		sii[1] = new SetupItemInfo(SetupItem.TEXT_FIELD, "Condition code ", CONDITION_CODE);
		sii[2] = new SetupItemInfo(SetupItem.COMBO_BOX, "Session code ", SESSION_CODES);
		sii[3] = new SetupItemInfo(SetupItem.TEXT_FIELD, "Number of phrases ", NUMBER_OF_PHRASES);
		sii[4] = new SetupItemInfo(SetupItem.TEXT_FIELD, "Phrases file ", PHRASES_FILE);
		sii[5] = new SetupItemInfo(SetupItem.CHECK_BOX, "Show presented text during input ", SHOW_PRESENTED);

		// use setup to allow changes to the default or existing configuration
		Setup s = new Setup(null, c, APP_NAME, sii);
		// s.showSetup(null);

		TypingTestExperimentGui screen = new TypingTestExperimentGui(c);
		screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		screen.setTitle("Typing Test Experiment");
		screen.pack();

		// put application in centre of screen
		int w = screen.getWidth();
		int h = screen.getHeight();
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension d = t.getScreenSize();
		screen.setLocation((d.width - w) / 2, (d.height - h) / 2);

		screen.setVisible(true);
	}
}

class TypingTestExperimentGui extends JFrame implements KeyListener
{
	// the following avoids a "warning" with Java 1.5.0 complier (?)
	static final long serialVersionUID = 42L;

	final String SD1_HEADER = "";
	final String SD2_HEADER = "App,Participant,Condition,Session,Block,"
			+ "Keystrokes,Characters,Time_(s),MSD,Speed_(wpm),Error_rate_(%),KSPC\n";

	private String participantCode, conditionCode, sessionCode, blockCode;
	private String appName;

	private BufferedWriter sd1File;
	private BufferedWriter sd2File;
	private Vector<Sample> samples;
	private String phrasesFile;
	private boolean showPresented;
	private boolean newPhrase = true;
	private Random r = new Random();
	private JTextField presentedTextField;
	private JTextField transcribedTextField;
	private JTextArea results;
	private String[] phrases;
	private String presentedPhrase; // presented phrase
	// private String tPhrase; // transcribed phrase
	private long t1, t2;
	private int count; // count keystrokes per phrase
	private int phraseCount;

	private Font BIG = new Font("monospaced", Font.BOLD, 24);
	private Color BACKGROUND = new Color(254, 254, 218);
	private Color FOREGROUND = new Color(11, 11, 109);

	Configuration c;

	// constructor
	TypingTestExperimentGui(Configuration cArg)
	{
		// initialize setup parameters
		c = cArg;
		participantCode = c.getConfigurationParameter(0);
		conditionCode = c.getConfigurationParameter(1);
		sessionCode = c.getConfigurationParameter(2);
		// Note: Block code generated automatically by the software (see below)
		phraseCount = Integer.parseInt(c.getConfigurationParameter(3));
		phrasesFile = c.getConfigurationParameter(4);
		showPresented = Boolean.valueOf(c.getConfigurationParameter(5));

		appName = TypingTestExperiment.APP_NAME;

		// ------------------
		// initialize phrases
		// ------------------

		Scanner inFile = null;
		try
		{
			inFile = new Scanner(new File(phrasesFile));
		} catch (FileNotFoundException e)
		{
			showError("\nPhrases file not found - " + phrasesFile);
			System.exit(1);
		}
		
		ArrayList<String> arrayList = new ArrayList<String>();
		while (inFile.hasNextLine())
			arrayList.add(inFile.nextLine());
		phrases = arrayList.toArray(new String[arrayList.size()]);

		// -----------------------
		// initialize output files
		// -----------------------

		blockCode = "";
		int blockCodeNumber = 0;
		String base = TypingTestExperiment.APP_NAME + "-" + participantCode + "-" + conditionCode + "-" + sessionCode;
		String s1 = "";
		String s2 = "";
		do // find next available block code
		{
			++blockCodeNumber;
			blockCode = blockCodeNumber < 10 ? "B0" + blockCodeNumber : "B" + blockCodeNumber;
			s1 = base + "-" + blockCode + ".sd1";
			s2 = base + "-" + blockCode + ".sd2";
		} while (new File(s1).exists());

		try
		{
			sd1File = new BufferedWriter(new FileWriter(s1));
			sd2File = new BufferedWriter(new FileWriter(s2));
		} catch (IOException e)
		{
			System.out.println("I/O error: can't open sd1/sd2 data files");
			System.exit(0);
		}

		try
		{
			sd1File.write(SD1_HEADER, 0, SD1_HEADER.length());
			sd1File.flush();
			sd2File.write(SD2_HEADER, 0, SD2_HEADER.length());
			sd2File.flush();
		} catch (IOException e)
		{
			System.err.println("ERROR WRITING TO DATA FILE!\n" + e);
			System.exit(1);
		}

		// ---------------------
		// initialize components
		// ---------------------

		presentedTextField = new JTextField(50);
		presentedTextField.setFocusable(false);
		presentedTextField.setFont(BIG);
		presentedTextField.setBackground(new Color(254, 254, 218));
		presentedTextField.setForeground(new Color(11, 11, 109));
		JPanel text1Panel = new JPanel();
		text1Panel.add(presentedTextField);
		text1Panel.setBorder(new TitledBorder(new EtchedBorder(), "Presented text"));

		transcribedTextField = new JTextField(50);
		transcribedTextField.setFont(BIG);
		transcribedTextField.setBackground(BACKGROUND);
		transcribedTextField.setForeground(FOREGROUND);
		JPanel text2Panel = new JPanel();
		text2Panel.add(transcribedTextField);
		text2Panel.setBorder(new TitledBorder(new EtchedBorder(), "Transcribed text"));

		transcribedTextField.setEditable(true);
		transcribedTextField.addKeyListener(this);

		results = new JTextArea(13, 50);
		results.setFocusable(false);
		results.setFont(new Font("monospaced", Font.BOLD, 18));
		results.setBackground(BACKGROUND);
		results.setForeground(FOREGROUND);
		JPanel resultsPanel = new JPanel(new BorderLayout());
		resultsPanel.add(results, "Center");
		Border b1 = new EtchedBorder();
		Border b2 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		resultsPanel.setBorder(new CompoundBorder(b1, b2));

		// present first phrase for input
		presentedPhrase = phrases[r.nextInt(phrases.length)];
		presentedTextField.setText(presentedPhrase);
		transcribedTextField.setText("");
		results.setText("");
		t1 = 0;
		count = 0;
		samples = new Vector<Sample>();

		// layout components
		JPanel p1 = new JPanel();
		p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
		p1.add(Box.createRigidArea(new Dimension(5, 10)));
		p1.add(text1Panel);
		p1.add(Box.createRigidArea(new Dimension(5, 10)));
		p1.add(text2Panel);
		p1.add(Box.createRigidArea(new Dimension(5, 10)));

		JPanel p = new JPanel(new BorderLayout());
		p.add(p1, "North");
		p.add(resultsPanel, "South");

		this.setContentPane(p);
	}

	void showError(String msg)
	{
		JOptionPane.showMessageDialog(null, msg, "I/O Error", JOptionPane.ERROR_MESSAGE);
	}

	// Key listener interface methods
	public void keyReleased(KeyEvent ke)
	{
	}

	public void keyTyped(KeyEvent ke)
	{
	}

	public void keyPressed(KeyEvent ke)
	{
		if (newPhrase)
		{
			newPhrase = false;
			if (!showPresented)
				presentedTextField.setText("");
		}

		++count;
		if (t1 == 0)
			t1 = System.currentTimeMillis();

		int keyCode = ke.getKeyCode();
		String keyText = KeyEvent.getKeyText(keyCode);

		t2 = System.currentTimeMillis() - t1;
		samples.addElement(new Sample(t2, keyText.toLowerCase()));

		if (keyCode == KeyEvent.VK_ENTER)
		{
			newPhrase = true;
			--count; // don't count pressing Enter as a keystroke

			String s1 = presentedPhrase.toLowerCase();
			String s2 = transcribedTextField.getText().toLowerCase();

			MSD s1s2 = new MSD(s1, s2);
			int msd = s1s2.getMSD();

			// output formatted results in GUI
			StringBuilder resultsString = new StringBuilder();
			resultsString.append(" DATA COLLECTED:\n");
			resultsString.append(String.format("   Presented:   %s\n", s1));
			resultsString.append(String.format("   Transcribed: %s\n", s2));
			resultsString.append(String.format("   Keystrokes:  %d\n", count));
			resultsString.append(String.format("   Characters:  %d (%.1f words)\n", s2.length(), s2.length() / 5.0));
			resultsString.append(String.format("   Time:        %.2f s (%.1f minutes)\n", t2 / 1000.0, t2 / 1000.0 / 60.0));
			resultsString.append(String.format("   MSD:         %d\n\n", msd));
			resultsString.append(" PARTICIPANT PERFORMANCE:\n");
			resultsString.append(String.format("   Entry Speed: %.2f wpm\n", wpm(s2, t2)));
			resultsString.append(String.format("   Error rate:  %.2f%%\n", s1s2.getErrorRateNew()));
			resultsString.append(String.format("   KSPC:        %.4f\n", (double)count / s2.length()));
			results.setText(resultsString.toString());			

			// build output data for sd2 file
			StringBuilder sd2Stuff = new StringBuilder();
			sd2Stuff.append(appName).append(',');
			sd2Stuff.append(participantCode).append(',');
			sd2Stuff.append(conditionCode).append(',');
			sd2Stuff.append(sessionCode).append(',');
			sd2Stuff.append(blockCode).append(',');
			sd2Stuff.append(count).append(','); // number of keystrokes
			sd2Stuff.append(s2.length()).append(','); // number of characters entered
			sd2Stuff.append(t2 / 1000.0).append(','); // time in milliseconds
			sd2Stuff.append(msd).append(','); // MSD
			sd2Stuff.append(wpm(s2, t2)).append(','); // words per minute
			sd2Stuff.append(s1s2.getErrorRateNew()).append(','); // error rate
			sd2Stuff.append((double)count / s2.length()).append('\n'); // KSPC

			// build output data for sd1 file
			StringBuilder sd1Stuff = new StringBuilder();
			sd1Stuff.append(presentedPhrase).append('\n');
			sd1Stuff.append(s2).append('\n');			
			for (int i = 0; i < samples.size(); ++i)
				sd1Stuff.append(samples.elementAt(i)).append('\n');
			sd1Stuff.append('#').append('\n');

			// dump data
			try
			{
				sd1File.write(sd1Stuff.toString(), 0, sd1Stuff.length());
				sd1File.flush();
				sd2File.write(sd2Stuff.toString(), 0, sd2Stuff.length());
				sd2File.flush();
			} catch (IOException e)
			{
				System.err.println("ERROR WRITING TO DATA FILE!\n" + e);
				System.exit(1);
			}

			// terminate if finished last phrase in block
			if (--phraseCount == 0)
			{
				JLabel thankyou = new JLabel("End of block. Thank you.");
				thankyou.setFont(new Font("sansserif", Font.PLAIN, 16));
				JOptionPane.showMessageDialog(this, thankyou);
				try
				{
					sd1File.close();
					sd2File.close();
				} catch (IOException e)
				{
					System.err.println("ERROR CLOSING DATA FILES!\n" + e);
					System.exit(1);
				}
				System.exit(0);
			}

			// prepare for next phrase
			samples = new Vector<Sample>();
			presentedPhrase = phrases[r.nextInt(phrases.length)];
			presentedTextField.setText(presentedPhrase);
			transcribedTextField.setText("");
			t1 = 0;
			count = 0;
		}
	}

	// compute typing speed in wpm given text entered and time in ms
	public static double wpm(String text, long msTime)
	{
		double speed = text.length();
		speed = speed / (msTime / 1000.0) * (60 / 5);
		return speed;
	}

	private class Sample
	{
		Sample(long l, String s)
		{
			time = l;
			key = s;
		}

		@Override
		public String toString()
		{
			return time + " " + key;
		}

		private long time;
		private String key;
	}
}