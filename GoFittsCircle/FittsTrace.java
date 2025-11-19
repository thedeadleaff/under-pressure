import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * <hr>
 * 
 * <table width="100%" summary="FittsTrace title">
 * <tr style="background-color:#cccccc">
 * <td align="center">
 * <h1>FittsTrace</h1>
 * </table>
 * 
 * <h2>Summary</h2>
 * 
 * <ul>
 * <li>Utility software to plot the trace data in the sd3 files created by FittsTask.
 * </ul>
 * 
 * <h2>Related References</h2>
 * 
 * The following are examples of published figures showing trace data that were gathered using a version of this
 * software:
 * 
 * <ul>
 * 
 * <li><a href="https://www.yorku.ca/mack/icchp2022.html#Figure_5">Figure 5</a> in
 * "TBS3: Two-Bar Single-Switch Scanning for Target Selection", by Raynal and MacKenzie (<i>ICCHP 2022</i>)
 * 
 * <li><a href="https://www.yorku.ca/mack/interact2019.html#Figure_7">Figure 7</a> in
 * "FittsFarm: Comparing Children's Drag-and-Drop Performance Using Finger and Stylus Input on Tablets", 
 * by Cassidy, Read, and MacKenzie (<i>INTERACT 2019</i>)
 * 
 * <li><a href="https://www.yorku.ca/mack/icchp2016b.html#Figure_6">Figure 6</a> in
 * "Comparison of Two Methods to Control the Mouse Using a Keypad", by Felzer, MacKenzie, and Magee (<i>ICCHP 2016</i>)
 * 
 * <li><a href="https://www.yorku.ca/mack/hcii2015b.html#Figure_9">Figure 9</a> and
 * <a href="http://www.yorku.ca/mack/hcii2015b.html#Figure_10">Figure 10</a>
 * in "Camera Mouse + ClickerAID: Dwell vs. Single-muscle Click
 * Actuation in Mouse-replacement Interfaces", by Magee, Felzer, and MacKenzie (<i>HCII 2015</i>)
 * 
 * <li><a href="https://www.yorku.ca/mack/gi2014.html#Figure_13">Figure 13</a> in
 * "Position vs. Velocity Control for Tilt-based Interaction", by Teather and MacKenzie (<i>GI 2014</i>
 * 
 * <li><a href="https://www.yorku.ca/mack/nordichi2012.html#Figure_9">Figure 9</a> and
 * <a href="http://www.yorku.ca/mack/nordichi2012.html#Figure_11">Figure 11</a> in "FittsTilt: The Application of Fitts'
 * Law to Tilt-based Interaction", by MacKenzie and Teather (<i>NordiCHI 2012</i>)
 * 
 * <li><a href="https://www.yorku.ca/mack/cogain.html#Figure_15">Figure 15</a> in
 * "Evaluating Eye Tracking Systems for Computer Input", by MacKenzie (in Majaranta, P., Aoki, H., Donegan, M.,
 * Hansen, D. W., Hansen, J. P., Hyrskykari, A., &amp; R&auml;ih&auml;, K.-J. (Eds.). (2012). <i>Gaze interaction and applications
 * of eye tracking: Advances in assistive technologies</i>, pp. 205-225. Hershey, PA: IGI Global.)
 * 
 * <li><a href="https://www.yorku.ca/mack/FuturePlay1.html#Figure_10">Figure 10</a> and
 * <a href="https://www.yorku.ca/mack/FuturePlay1.html#Figure_11">Figure 11</a> in
 * "The Trackball Controller: Improving the Analog Stick", by Natapov and MacKenzie ( <i>FuturePlay 2010</i>)
 * </ul>
 * 
 * <h2>Getting Started</h2>
 * 
 * FittsTrace is launched from GoFitts by selecting FittsTrace and clicking "Go" (below, left). FittsTrace begins with a
 * launch panel (below, centre). Clicking "Open..." opens a file chooser showing all the sd3 files in the current
 * directory (below, right). Select an sd3 file and click "Open". (Click images to enlarge.)
 * 
 * <center><p> <a href="FittsTrace-0.jpg"><img src="FittsTrace-0.jpg" width="180" alt="FittsTrace-0"></a>
 * <a href="FittsTrace-1.jpg"><img src="FittsTrace-1.jpg" width="450" alt= "FittsTrace-1"></a>
 * <a href="FittsTrace-2.jpg"><img src="FittsTrace-2.jpg" width="270" alt= "FittsTrace-2"></a> </center>
 * 
 * <h2>Input Data Format</h2>
 * 
 * The sd3 data processed by this application must be in a specific format, as produced by FittsTask. The first two
 * lines are for information only and are discarded. In the example sd3 file (see below), the first line is a title and
 * the second line contains comma-delimited labels identifying the rest of the data:
 * 
 * <pre>
 * 
 *     application name
 *     participant code
 *     condition code
 *     session code
 *     group code
 *     task type - 1D or 2D
 *     block code
 *     sequence - sequence number
 *     A - target amplitude (diameter of the layout circle in pixels)
 *     W - target width (diameter of target circle in pixels)
 *     trial - trial number within the sequence
 *     from_x - x coordinate of beginning of trial
 *     from_y - y coordinate of beginning of trial
 *     to_x - centre x coordinate of target
 *     to_y - centre y coordinate of target
 *     identifier ("t=", "x=", or "y=")
 * </pre>
 * 
 * The trace data begin after the first two lines just noted. Each trace is encoded in three consecutive lines: (i) the
 * timestamps, (ii) the <i>x</i> samples, and (iii) the <i>y</i> samples. <a href="FittsTask-sd3-example.txt">Click
 * here</a> for an example of an sd3 data file.
 * 
 * <h2>Example Traces</h2>
 * 
 * The following screen snap shows the trace data for one sequence of trials in the sd3 example file:
 * 
 * <center> <p><a href="FittsTrace-4.jpg"><img src="FittsTrace-4.jpg" width="300" alt="FittsTrace-4"></a> <a href=
 * "FittsTrace-11.jpg"> <img src="FittsTrace-11.jpg" width="300" alt="FittsTrace-11"> </a></center>
 * <p>
 * 
 * Use the "Previous" and "Next" buttons to advance the view to the previous or next sequence (above, right).
 * <p>
 * 
 * As well as identifying the test conditions, the UI displays the performance data associated with the traces,
 * including the mean movement time, error rate, throughput, and the mean of the accuracy measures. (The latter are
 * described in <a href="http://www.yorku.ca/mack/CHI01.htm">Accuracy Measures for Evaluating Computer Pointing
 * Devices</a> by MacKenzie, Kauppinen, and Silfverberg (2001); see as well the <a href="AccuracyMeasures.html">API</a>
 * for the <code>AccuracyMeasures</code> class.)
 * <p>
 * 
 * The "Trials" mode checkbox along the bottom of the UI allows the trace data to be inspected for individual trials
 * (below, left). To view the actual trace points, select the show "Points" checkbox (below, right).
 * 
 * <center><p> <a href="FittsTrace-5.jpg"><img src="FittsTrace-5.jpg" width="300" alt="FittsTrace-5"></a>
 * <a href="FittsTrace-10.jpg"><img src="FittsTrace-10.jpg" width="300" alt="FittsTrace-10"></a> </center>
 * <p>
 * 
 * In trial mode, the performance data correspond to individual trials, rather than a sequence of trials.
 * 
 * <h2>Panning and Zooming</h2>
 * 
 * FittsTrace supports panning and zooming to get different perspectives on the trace data. Zooming is performed by
 * pressing the CTRL key and moving the mouse wheel or by pressing CTRL along with "]" or "+" (zoom in) or "[" or "-"
 * (zoom out). Panning is performed by moving the mouse wheel, dragging the trace panel, or pressing the arrow keys
 * (with or without CTRL). For example, consider the following display of trace data (left) and the same data zoomed in
 * (right):
 * 
 * <center><p> <a href="FittsTrace-6.jpg"><img src="FittsTrace-6.jpg" height="180" alt="FittsTrace-6"></a>
 * <a href="FittsTrace-9.jpg"><img src="FittsTrace-9.jpg" height="180" alt="FittsTrace-9"></a> </center>
 * <p>
 * 
 * The current zoom factor is shown in the "Scale" textbox along the bottom of the UI. A closer look at the trace for
 * one of the trials is possible by zooming in further, perhaps with "Show Points" enabled:
 * 
 * <center><p> <a href="FittsTrace-7.jpg"><img src="FittsTrace-7.jpg" width="250" alt="FittsTrace-7"></a>
 * <a href="FittsTrace-8.jpg"><img src="FittsTrace-8.jpg" width="250" alt="FittsTrace-8"></a> </center>
 * <p>
 * 
 * Clicking the "Reset" button or entering CTRL-0 ("zero") restores the view to the home position with Scale = 1.0.
 * <p>
 * 
 * @author Scott MacKenzie, 2009-2025
 */

public class FittsTrace extends JFrame implements ActionListener, ItemListener, PanZoomPanel.PanZoomPanelListener
{
	// the following avoids a "warning" with Java 1.5.0 complier (?)
	static final long serialVersionUID = 42L;
	final String LAUNCH_MESSAGE = "Open sd3 File";

	final int MODE_1D = Throughput.ONE_DIMENSIONAL;
	final int MODE_2D = Throughput.TWO_DIMENSIONAL;

	boolean debug = false;

	private TracePanel tracePanel;
	private GoFittsLaunchPanel launchPanel;
	private JPanel mainPanel;
	private JTextField sd3File;
	private JTextField scaleTextField;
	private JButton reset;
	private JButton api;
	private JRadioButton trialModeButton, sequenceModeButton;
	private JButton open;
	private JButton next;
	private JButton previous;
	private JButton backToGoFitts;
	private JCheckBox showPointsCheckBox;
	private JCheckBox showTracesCheckBox;
	private JFileChooser fc;
	Clip tickSound, errorSound;

	PanZoomPanel panZoomPanel;
	JPanel topPanel, buttonPanel;
	File f, currentDirectory;
	String fileName;
	int sequenceCounter = 0;
	int trialCounter = 0;

	// filename tokens (in order)
	String appName, participantCode, conditionCode, sessionCode, groupCode, taskType, selectionMethod, interactionModeCode, blockCode;

	Trial[] trialData;
	Throughput tp;
	ArrayList<Trial[]> sequenceArray;

	private boolean showTraces = true;
	private boolean showTracePoints = false;
	private boolean trialMode = false;
	private int mode;

	public FittsTrace(File currentDirectoryArg)
	{
		currentDirectory = currentDirectoryArg;
		
		// ----------------------------------
		// construct and configure components
		// ----------------------------------

		fc = new JFileChooser(currentDirectory);
		fc.setFileFilter(new FileNameExtensionFilter("sd3 file", "sd3"));

		tracePanel = new TracePanel();
		tracePanel.setBorder(BorderFactory.createLineBorder(Color.gray));
		tracePanel.setBackground(Color.WHITE);

		launchPanel = new GoFittsLaunchPanel();
		launchPanel.launchMessage = this.LAUNCH_MESSAGE;

		errorSound = initSound("blip1.wav");
		tickSound = initSound("tick.wav");

		// define UI buttons/checkboxes in button panel (in left-to-right order)
		api = new JButton("View API in Browswer");
		api.setAlignmentY(Component.CENTER_ALIGNMENT);
		reset = new JButton("Reset");
		showTracesCheckBox = new JCheckBox("Traces");
		showPointsCheckBox = new JCheckBox("Points");
		trialModeButton = new JRadioButton("Trial");
		sequenceModeButton = new JRadioButton("Sequence");
		open = new JButton("Open...");
		previous = new JButton("Previous");
		next = new JButton("Next");
		backToGoFitts = new JButton("Back to GoFitts");
		
		// add tooltip text (added July 10, 2023)
		api.setToolTipText(GoFitts.API_LOCATION + "FittsTrace.html");

		previous.setEnabled(false);
		next.setEnabled(false);

		showTracesCheckBox.setSelected(true);
		showPointsCheckBox.setSelected(false);

		ButtonGroup bg = new ButtonGroup();
		bg.add(trialModeButton);
		bg.add(sequenceModeButton);
		sequenceModeButton.setSelected(true);
		trialMode = false;

		sd3File = new JTextField(30);
		sd3File.setEditable(false);
		sd3File.setBackground(Color.white);

		scaleTextField = new JTextField(4);
		scaleTextField.setEditable(false);
		scaleTextField.setBackground(Color.white);
		scaleTextField.setToolTipText("Zoom using CTRL+mouse_wheel");

		panZoomPanel = new PanZoomPanel(tracePanel);

		// disable focus for UI widgets (for keyboard panning/zooming, only the panZoomPanel gets focus)
		api.setFocusable(false);
		scaleTextField.setFocusable(false);
		reset.setFocusable(false);
		showTracesCheckBox.setFocusable(false);
		showPointsCheckBox.setFocusable(false);
		trialModeButton.setFocusable(false);
		sequenceModeButton.setFocusable(false);
		sd3File.setFocusable(false);
		open.setFocusable(false);
		previous.setFocusable(false);
		next.setFocusable(false);
		backToGoFitts.setFocusable(false);

		// -------------
		// add listeners
		// -------------

		api.addActionListener(this);
		next.addActionListener(this);
		previous.addActionListener(this);
		backToGoFitts.addActionListener(this);
		open.addActionListener(this);
		showTracesCheckBox.addItemListener(this);
		showPointsCheckBox.addItemListener(this);
		trialModeButton.addActionListener(this);
		sequenceModeButton.addActionListener(this);
		reset.addActionListener(this);
		panZoomPanel.addPanZoomPanelListener(this);

		// give the panZoomPanel focus when the frame is activated (so keyboard panning and zooming are enabled)
		this.addWindowFocusListener(new WindowAdapter()
		{
			public void windowGainedFocus(WindowEvent e)
			{
				panZoomPanel.requestFocusInWindow();
			}
		});

		// adjust target positions if the window is resized
		this.addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent e)
			{
				int newWidth = (int) Math
						.round(mainPanel.getWidth() - mainPanel.getInsets().left - mainPanel.getInsets().right);
				int newHeight = (int) Math
						.round(mainPanel.getHeight() - mainPanel.getInsets().top - mainPanel.getInsets().bottom);
				panZoomPanel.setPreferredSize(new Dimension(newWidth, newHeight));
				initTracePanel();
			}
		});

		// ------------------
		// arrange components
		// ------------------

		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(5, 5, 5, 5), new EtchedBorder()));
		mainPanel.add(launchPanel, "Center");

		JPanel buttonPanelLeft = new JPanel();
		buttonPanelLeft.setLayout(new BoxLayout(buttonPanelLeft, BoxLayout.Y_AXIS));
		buttonPanelLeft.setBorder(new EmptyBorder(5, 5, 5, 1));
		buttonPanelLeft.add(Box.createVerticalGlue()); // spring
		buttonPanelLeft.add(api); // centered vertically (thanks to the springs)
		buttonPanelLeft.add(Box.createVerticalGlue()); // spring

		JPanel scalePanel = new JPanel();
		scalePanel.setBorder(new TitledBorder(new EtchedBorder(), "Scale"));
		scalePanel.add(scaleTextField);
		scalePanel.add(reset);

		JPanel showPanel = new JPanel(new BorderLayout());
		showPanel.setBorder(new TitledBorder(new EtchedBorder(), "Show"));
		showPanel.add(showTracesCheckBox, "North");
		showPanel.add(showPointsCheckBox, "South");

		JPanel modePanel = new JPanel(new BorderLayout());
		modePanel.setBorder(new TitledBorder(new EtchedBorder(), "Mode"));
		modePanel.add(sequenceModeButton, "North");
		modePanel.add(trialModeButton, "South");

		JPanel filePanel = new JPanel();
		filePanel.setBorder(new TitledBorder(new EtchedBorder(), "sd3 File"));
		filePanel.add(sd3File);
		filePanel.add(open);

		Dimension d1 = scalePanel.getPreferredSize();
		Dimension d2 = showPanel.getPreferredSize();
		Dimension d3 = filePanel.getPreferredSize();
		scalePanel.setPreferredSize(new Dimension(d1.width, d2.height));
		filePanel.setPreferredSize(new Dimension(d3.width, d2.height));

		JPanel buttonPanelCentre = new JPanel();
		buttonPanelCentre.add(scalePanel);
		buttonPanelCentre.add(showPanel);
		buttonPanelCentre.add(modePanel);
		buttonPanelCentre.add(filePanel);
		buttonPanelCentre.add(previous);
		buttonPanelCentre.add(next);
		buttonPanelCentre.add(backToGoFitts);
		buttonPanelCentre.setBorder(BorderFactory.createEmptyBorder(5, 1, 5, 1));

		buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.add(buttonPanelLeft, BorderLayout.WEST);
		buttonPanel.add(buttonPanelCentre, BorderLayout.CENTER);

		panZoomPanel.add(tracePanel);

		topPanel = new JPanel(new BorderLayout());
		topPanel.add(mainPanel, "Center");
		topPanel.add(buttonPanel, "South");
		topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		this.setContentPane(topPanel);
	}

	// ===============================
	// Item Listener method (1 method)
	// ===============================

	public void itemStateChanged(ItemEvent ie)
	{
		Object source = ie.getSource();

		if (source == showTracesCheckBox)
			showTraces = !showTraces;

		else if (source == showPointsCheckBox)
			showTracePoints = !showTracePoints;

		this.repaint();
	}

	// ==========================
	// Action Listener (1 method)
	// ==========================

	public void actionPerformed(ActionEvent ae)
	{
		Object source = ae.getSource();

		if (source == trialModeButton)
		{
			trialMode = true;
			trialCounter = 0; // incremented to 1 before 1st draw
			tracePanel.repaint();

			// first trial displayed! play hit or miss audio sound
			if (sequenceArray.get(sequenceCounter)[trialCounter].getError() == 0)
				playSound(tickSound);
			else
				playSound(errorSound);

		} else if (source == sequenceModeButton)
		{
			trialMode = false;
			tracePanel.repaint();
		}

		else if (source == next)
			tracePanel.next();

		else if (source == previous)
			tracePanel.previous();

		else if (source == backToGoFitts)
		{
			this.dispose();
			GoFittsFrame frame = new GoFittsFrame(GoFitts.FITTS_TRACE, currentDirectory);

			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setTitle(GoFitts.APP_NAME);
			frame.pack();

			// position application window in center of display
			Dimension d1 = frame.getSize();
			Dimension d2 = Toolkit.getDefaultToolkit().getScreenSize();
			int x = (d2.width - d1.width) / 2;
			int y = (d2.height - d1.height) / 2;
			frame.setLocation(new Point(x, y));

			frame.setVisible(true);
		}

		else if (source == open)
		{
			if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			{
				f = fc.getSelectedFile();
				fileName = f.getName();
				
				currentDirectory = f.isDirectory() ? f : f.getParentFile();

				sequenceArray = new ArrayList<Trial[]>();
				loadData(); // data will be in sequenceArray (if successful)

				if (sequenceArray.size() == 0) // an error message was generated in loadData
					return;

				sequenceCounter = 0;
				trialCounter = 0;

				previous.setEnabled(true);
				next.setEnabled(true);

				sd3File.setText(f.getName());
				mainPanel.removeAll();
				mainPanel.add(panZoomPanel, "Center");
				mainPanel.revalidate();

				panZoomPanel.setPreferredSize(launchPanel.getSize());
				initTracePanel();
			}
		}

		else if (source == reset)
		{
			initTracePanel();
		}

		else if (source == api)
		{
			boolean okReturned = GoFitts.openBrowser("index.html?FittsTrace.html");
			if (!okReturned)
			{
				GoFitts.showError("Error launching browser");
				return;
			}
		}
	}

	void initTracePanel()
	{
		int newWidth = (int) (panZoomPanel.getPreferredSize().getWidth());
		int newHeight = (int) (panZoomPanel.getPreferredSize().getHeight());
		panZoomPanel.setInitialSizeAndLocation(newWidth, newHeight, 0, 0); // ... of the trace panel
	}

	// load the sd3 data into an array of Trial objects
	@SuppressWarnings("resource") // avoids resource leak warning (which I'm not sure how to avoid or fix)
	private void loadData()
	{
		String[] fileNameTokens = f.getName().split("[-.]");
		int sequenceIndex = -1;
		boolean newSequence = true;
		boolean legacyFormat = false;
		boolean newFormatGoFittsDragging = false;
		
//		System.out.println("file: " + f.getName());
//		System.out.println("appName: " + fileNameTokens[0]);
//		System.out.println("InteractionMode: " + fileNameTokens[7]);
//		System.out.println("Number of filename tokens: " + fileNameTokens.length);

		// default values (attempt to reassign from file name tokens or contents)
		participantCode = "P00";
		conditionCode = "C00";
		interactionModeCode = "PS"; // point-select 
		blockCode = "B00";
		taskType = "2D";

		// include legacy logic to allow reading sd3 data from FittsTaskOne, FittsTaskTwo, and FittsDragAndDrop
		String appName = fileNameTokens[0];
		if (appName.equals("FittsTask"))
		{
			if (fileNameTokens.length >= 10) // GoFitts with dragging mode and show/hide cursor
			{
				newFormatGoFittsDragging = true;
				participantCode = fileNameTokens[1];
				conditionCode = fileNameTokens[2];
				interactionModeCode = fileNameTokens[7]; // new
				blockCode = fileNameTokens[8];
				taskType = fileNameTokens[5];
			}
			else if (fileNameTokens.length >= 8)
			{
				participantCode = fileNameTokens[1];
				conditionCode = fileNameTokens[2];
				blockCode = fileNameTokens[7];
				taskType = fileNameTokens[5];
			}

		} else if (appName.equals("FittsDragAndDrop")) // legacy format
		{
			if (fileNameTokens.length >= 7)
			{
				legacyFormat = true;
				participantCode = fileNameTokens[1];
				conditionCode = fileNameTokens[5];
				blockCode = fileNameTokens[3];
				taskType = fileNameTokens[6];
			}

		} else // FittsTaskOne or FittsTaskTwo
		{
			if (fileNameTokens.length >= 4)
			{
				legacyFormat = true;
				participantCode = fileNameTokens[1];
				conditionCode = fileNameTokens[2];
				blockCode = fileNameTokens[3];
				taskType = appName.equals("FittsTaskOne") ? "1D" : "2D";
			}
		}

		// set the mode according to whether were are loading data for a 1D task or a 2D task
		if (taskType.equals("1D"))
			mode = MODE_1D;
		else
			mode = MODE_2D;

		Scanner inFile = null;
		try
		{
			inFile = new Scanner(f);
		} catch (FileNotFoundException e)
		{
			GoFitts.showError("File not found error!\nFile: " + f.toString());
			return;
		}

		ArrayList<Trial> trialArray = new ArrayList<Trial>();

		try
		{
			inFile.nextLine(); // 1st header line (discard)
			inFile.nextLine(); // 2nd header line (discard)
		} catch (NoSuchElementException e)
		{
			GoFitts.showError("Oops! No data in sd3 file.");
			return;
		}

		while (inFile.hasNextLine()) // assume at least three data lines, and subsequent groups of three
		{
			// data lines are in groups of three
			String tLine, xLine, yLine;
			try
			{
				tLine = inFile.nextLine().trim();
				xLine = inFile.nextLine().trim();
				yLine = inFile.nextLine().trim();
			} catch (NoSuchElementException e)
			{
				GoFitts.showError("Data read problem. Check format of data file.");
				return;
			}

			// delete commas at end of lines (if any)
			if (tLine.length() > 0 && tLine.charAt(tLine.length() - 1) == ',')
				tLine = tLine.substring(0, tLine.length() - 1);
			if (xLine.length() > 0 && xLine.charAt(xLine.length() - 1) == ',')
				xLine = xLine.substring(0, xLine.length() - 1);
			if (yLine.length() > 0 && yLine.charAt(yLine.length() - 1) == ',')
				yLine = yLine.substring(0, yLine.length() - 1);

			// get the trace data as time, x, and y arrays
			String[] timeTokens = tLine.split(",");
			String[] xTokens = xLine.split(",");
			String[] yTokens = yLine.split(",");

			// ensure the same number of elements in each array
			if (!(timeTokens.length == xTokens.length && xTokens.length == yTokens.length) || timeTokens.length < 16)
			{
				// line below added July 31, 2024 to help debug sd3 data from Francesca's experiment
				//System.out.printf("ERROR: seq:" + timeTokens[8] + ", " + timeTokens.length + ", " + xTokens.length + ", " + yTokens.length);
				GoFitts.showError("Bad time, x, or y data. Check format of data file.");
				return;
			}

			// set index of first data element (different for legacy format sd3 file)
			int idx;
			if (legacyFormat)
				idx = 4;
			else if (newFormatGoFittsDragging)
				idx = 9;
			else
				idx = 8;
			
//			System.out.println("idx=" + idx);

			// combining lead-in elements, there should be at least (idx + 10) total elements
			if (timeTokens.length < (idx + 10))
			{
				GoFitts.showError(String.format("Wrong number of tokens (%d). Check format of data file.",
						(timeTokens.length + 1)));
				return;
			}

			// condition arguments appear first (extract from timeTokens)
			int sequence, a, w, trialNumber;
			double xFrom, yFrom, xTo, yTo;
			try
			{
				sequence = Integer.parseInt(timeTokens[idx++]); // sequence
				a = Integer.parseInt(timeTokens[idx++]); // a
				w = Integer.parseInt(timeTokens[idx++]); // w
				trialNumber = Integer.parseInt(timeTokens[idx++]); // trial
				
				
				xFrom = Double.parseDouble(timeTokens[idx++]); // from_x
				yFrom = Double.parseDouble(timeTokens[idx++]); // from_y
				xTo = Double.parseDouble(timeTokens[idx++]); // to_x
				yTo = Double.parseDouble(timeTokens[idx++]); // to_y
				
				// attempt to use data from Mathieu Raynal's app -- doesn't work! (5/4/2022)
				//xFrom = Double.parseDouble(timeTokens[idx++ + 4]); // from_x
				//yFrom = Double.parseDouble(timeTokens[idx++ + 4]); // from_y
				//xTo = Double.parseDouble(timeTokens[idx++ + 4]); // to_x
				//yTo = Double.parseDouble(timeTokens[idx++ + 4]); // to_y
				
				
				idx++; // lead-in (e.g., "t=") at next idx not needed

			} catch (NumberFormatException e)
			{
				GoFitts.showError("Error parsing integers.  Check format of data file.");
				return;
			}

			if (legacyFormat)
				--trialNumber; // because trials start at "1" in FittsDragAndDrop, FittsTaskTwo, and FittsTaskOne
			
			// for debugging (4/4/2022)
			System.out.printf("sequence=%d, a=%d, w=%d, trialNumber=%d, xFrom=%.1f, yFrom=%.1f, xTo=%.1f, yTo=%.1f", sequence, a, w, trialNumber, xFrom, yFrom, xTo, yTo);

			/*
			 * We want the data organized by sequence, but the data are a continuous series of trial data. So, some
			 * extra logic is added to check the "sequence" code in each line read. Each time the sequence code changes,
			 * we've got a new sequence. The trials beforehand are added to sequenceArray. Since there is no change in
			 * the sequence code for the last sequence, we need to repeat some of this logic after the loop ends, so the
			 * trials for the last sequence are also added to sequenceArray.
			 */

			if (newSequence)
			{
				newSequence = false;
				sequenceIndex = sequence;
			}

			if (sequence != sequenceIndex && trialArray.size() > 0)
			{
				// convert trial data to array and add to sequenceArray
				trialData = new Trial[trialArray.size()];
				trialArray.toArray(trialData);
				sequenceArray.add(trialData);

				// prepare for the next series of trials
				trialArray = new ArrayList<Trial>();

				newSequence = true;
				sequenceIndex = sequence;
			}

			// process data tokens from idx to the end			
			
			
			// adjust idx as per Mathieu Raynal's modifications (4/4/2022)
			//idx = idx + 4;
			// this works (i.e., the FittsTrace doesn't crash) but the target and trace positions don't make sense		
			
			
			int[] timeStamp = new int[timeTokens.length - idx];
			Point2D.Double[] traceData = new Point2D.Double[timeTokens.length - idx];
			for (int i = 0; i < timeStamp.length; ++i)
			{
				try
				{
					// debugging (4/4/2022)
					System.out.printf("i=%d, x=%s, y=%s\n", i, xTokens[i + idx], yTokens[i + idx]);
					timeStamp[i] = Integer.parseInt(timeTokens[i + idx]);
					int x = Integer.parseInt(xTokens[i + idx]);
					int y = Integer.parseInt(yTokens[i + idx]);
					traceData[i] = new Point2D.Double(x, y);
				} catch (NumberFormatException e)
				{
					GoFitts.showError("Error parsing t, x, or y trace data.  Check format of data file.");
					return;
				}
			}
			trialArray.add(new Trial(mode, sequence, a, w, trialNumber, xFrom, yFrom, xTo, yTo, timeStamp, traceData));
		}
		inFile.close();

		// outside the loop, we need to store the last series of trials in the sequenceArray (see lengthy comment above)
		trialData = new Trial[trialArray.size()];
		trialArray.toArray(trialData);
		sequenceArray.add(trialData);

		return;

	} // end loadData

	// -----------------------------------------
	// A class to display the targets and traces
	// -----------------------------------------

	class TracePanel extends JPanel
	{
		private static final long serialVersionUID = 1L;
		final double HEIGHT_RATIO = 0.9f; // ratio of vertical space for 1D rectangular targets
		final double MARKER_DIAMETER = 5.0f; // scaled
		final double TRACE_POINTS_DIAMETER = 3.0f; // not scaled

		// parameters for scaling and shifting the draw objects
		double offsetX, offsetY;
		double panelScale;
		double targetScale = 1.0;

		TracePanel()
		{
		}

		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			this.positionTargets();
			this.paintInfo(g);
			this.drawTargets(g);
			this.drawTraces(g);
		}

		private void paintInfo(Graphics g)
		{
			// first, make sure there is something to paint
			if (sequenceArray == null || sequenceArray.get(sequenceCounter).length == 0)
				return;

			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(new Color(128, 0, 0));
			g2.setFont(new Font("SansSerif", Font.PLAIN, (int) Math.round(panelScale * 14)));

			// get the summary info, as per mode (trial or sequence)
			String[] s = trialMode ? getTrialModeSummary(sequenceCounter, trialCounter)
					: getSequenceModeSummary(sequenceCounter, mode);

			int lineHeight = (int) Math.round(g2.getFontMetrics().getHeight() * panelScale);
			int yOffset = (int) Math.round(panelScale * 10);
			int line = 1;
			for (int i = 0; i < s.length; ++i)
				g2.drawString(s[i], yOffset, line++ * lineHeight);

			// some extra info if target scaling is being used (so targets and traces fit in available space)
			if (targetScale != 1.0)
			{
				String message = String.format("Target scale = %4.2f", targetScale);
				g2.setColor(Color.GRAY);
				g2.drawString(message, yOffset, this.getHeight() - lineHeight + (int) (6 * panelScale));
			}

		} // end PaintInfo

		public void positionTargets()
		{
			// first, make sure there is something to paint
			if (sequenceArray == null || sequenceArray.get(sequenceCounter).length == 0)
				return;

			// get the trials for the current sequence
			Trial[] t = sequenceArray.get(sequenceCounter);

			// we need to determine the x and y centers of the layout in the original sd3 data
			double centerLayoutX;
			double centerLayoutY;

			/*
			 * The method below is a bit tricker than necessary for the 1D task, but it is necessary for the 2D task.
			 * Here's the idea. We iterate through all the "to" targets in a sequence. They span the layout circle (2D)
			 * or both sides of the layout (1D). With a little help from Math.min/max, we get the bounds needed to
			 * compute the x and y center-points of the original layout.
			 */
			double xMin = Integer.MAX_VALUE;
			double xMax = Integer.MIN_VALUE;
			double yMin = Integer.MAX_VALUE;
			double yMax = Integer.MIN_VALUE;

			for (int i = 0; i < t.length; ++i)
			{
				xMin = Math.min(t[i].getXTo(), xMin);
				xMax = Math.max(t[i].getXTo(), xMax);
				yMin = Math.min(t[i].getYTo(), yMin);
				yMax = Math.max(t[i].getYTo(), yMax);
			}

			/*
			 * NOTE: targetScale is an additional scaling factor that ensures the targets fit within 75% of the trace
			 * panel's available vertical or horizontal space, whichever is less. This provides a good visualization in
			 * situations where the movement amplitude was large and the original data were collected on a system with a
			 * high-resolution display.
			 */

			double targetXSpan = panelScale * (xMax - xMin);
			double targetYSpan = panelScale * (yMax - yMin);
			double availableWidth = 0.75f * this.getWidth();
			double availableHeight = 0.75f * this.getHeight();
			targetScale = Math.min(1.0f, Math.min(availableWidth / targetXSpan, availableHeight / targetYSpan));

			centerLayoutX = panelScale * targetScale * (xMin + xMax) / 2.0;
			centerLayoutY = panelScale * targetScale * (yMin + yMax) / 2.0;

			// we also need the current x and y center-points of the trace panel
			double centerPanelX = this.getWidth() / 2.0;
			double centerPanelY = this.getHeight() / 2.0;

			// compute offsets for drawing to accommodate window resizing and output scaling
			offsetX = centerPanelX - centerLayoutX;
			offsetY = centerPanelY - centerLayoutY;
		}

		public void drawTargets(Graphics g)
		{
			// first, make sure we've got something to paint
			if (sequenceArray.get(sequenceCounter).length == 0)
				return;

			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(1));

			// w and h are the same for the entire sequence (calculate once)
			double w = panelScale * targetScale * sequenceArray.get(sequenceCounter)[0].w;
			double h = panelScale * targetScale * (HEIGHT_RATIO * this.getHeight());

			// get the trials for the current sequence
			Trial[] t = sequenceArray.get(sequenceCounter);

			// iterate through the trials and draw all the targets
			for (int i = 0; i < t.length; ++i)
			{
				if (mode == MODE_1D) // 1D: rectangular targets
				{
					double x = offsetX + panelScale * targetScale * t[i].getXTo() - (w / 2.0);
					double y = offsetY + panelScale * targetScale * t[i].getYTo() - (h / 2.0);
					Rectangle2D.Double rect = new Rectangle2D.Double(x, y, w, h);
					g2.draw(rect);

				} else // 2D: circular targets
				{
					double x = offsetX + panelScale * targetScale * t[i].getXTo() - (w / 2.0);
					double y = offsetY + panelScale * targetScale * t[i].getYTo() - (w / 2.0);
					Ellipse2D.Double e = new Ellipse2D.Double(x, y, w, w);
					g2.draw(e);
				}
			}

			if (trialMode) // draw target to select last (to occlude overlapping targets, if any)
			{
				if (mode == MODE_1D) // 1D: rectangular targets
				{
					double x = offsetX + panelScale * targetScale * t[trialCounter].getXTo() - (w / 2.0);
					double y = offsetY + panelScale * targetScale * t[trialCounter].getYTo() - (h / 2.0);

					Rectangle2D.Double rect = new Rectangle2D.Double(x, y, w, h);
					g2.setColor(Color.LIGHT_GRAY);
					g2.fill(rect); // fill the target to select
					g2.setColor(Color.BLACK);
					g2.draw(rect);

				} else // 2D: circular targets
				{
					double x = offsetX + panelScale * targetScale * t[trialCounter].getXTo() - (w / 2.0);
					double y = offsetY + panelScale * targetScale * t[trialCounter].getYTo() - (w / 2.0);

					Ellipse2D.Double e = new Ellipse2D.Double(x, y, w, w);
					g2.setColor(Color.LIGHT_GRAY);
					g2.fill(e); // fill the target to select
					g2.setColor(Color.BLACK);
					g2.draw(e);
				}
			}
		} // end drawTargets

		public void drawTraces(Graphics g)
		{
			// first, make sure we've got something to draw
			if (sequenceArray.get(sequenceCounter).length == 0)
				return;

			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// get the trials for the current sequence
			Trial[] t = sequenceArray.get(sequenceCounter);

			// iterate through all trials in this sequence
			for (int i = 0; i < t.length; ++i)
			{
				Point2D.Double[] p = t[i].getTracePoints();
				g2.setColor(new Color(0, 0, 128)); // deep blue

				// iterate through all sample points in this trial
				int j;
				for (j = 0; j < p.length - 1; ++j)
				{
					// draw traces (conditionally)
					if (showTraces && (!trialMode || (trialMode && trialCounter == t[i].getTrialNumber())))
					{
						double x1 = offsetX + panelScale * targetScale * p[j].x;
						double y1 = offsetY + panelScale * targetScale * p[j].y;
						double x2 = offsetX + panelScale * targetScale * p[j + 1].x;
						double y2 = offsetY + panelScale * targetScale * p[j + 1].y;
						Line2D.Double inkSegment = new Line2D.Double(x1, y1, x2, y2);
						g2.draw(inkSegment); // draw it!
					}

					// draw trace points (conditionally)
					if (showTracePoints && (!trialMode || (trialMode && trialCounter == t[i].getTrialNumber())))
					{
						double x2 = offsetX + panelScale * targetScale * p[j + 1].x;
						double y2 = offsetY + panelScale * targetScale * p[j + 1].y;
						Ellipse2D.Double pt = new Ellipse2D.Double(x2 - 1, y2 - 1, TRACE_POINTS_DIAMETER,
								TRACE_POINTS_DIAMETER);
						g2.draw(pt);
						g2.fill(pt);
					}
				}

				// draw marker at selection point (conditionally)
				if (!trialMode || (trialMode && trialCounter == t[i].getTrialNumber()))
				{
					double x = offsetX + panelScale * targetScale * p[j].x
							- panelScale * targetScale * MARKER_DIAMETER / 2.0;
					double y = offsetY + panelScale * targetScale * p[j].y
							- panelScale * targetScale * MARKER_DIAMETER / 2.0;
					double width = panelScale * targetScale * MARKER_DIAMETER;
					Ellipse2D.Double e = new Ellipse2D.Double(x, y, width, width);

					g2.setColor(new Color(0xff, 0, 0, 0x80)); // semi-transparent red
					g2.draw(e);
					g2.fill(e);
				}

				if (debug) // green lines showing panel outline and center
				{
					double xTopLeft = 1;
					double yTopLeft = 1;
					double xBottomRight = this.getWidth() - 3;
					double yBottomRight = this.getHeight() - 3;
					Line2D.Double line = new Line2D.Double(xTopLeft, yTopLeft, xBottomRight, yBottomRight);
					g2.setColor(Color.GREEN);
					g2.draw(line);

					double xBottomLeft = 1;
					double yBottomLeft = this.getHeight() - 3;
					double xTopRight = this.getWidth() - 3;
					double yTopRight = 1;
					Line2D.Double line2 = new Line2D.Double(xBottomLeft, yBottomLeft, xTopRight, yTopRight);
					g2.draw(line2);

					Rectangle2D.Double box = new Rectangle2D.Double(xTopLeft, yTopLeft, xBottomRight, yBottomRight);
					g2.draw(box);
				}
			}
		} // end drawTraces

		// return the number of trials in the current sequence
		int trialsPerSequence()
		{
			return sequenceArray.get(0).length; // use first sequence as exemplar
		}

		// display trace data for the next trial or sequence (with wrap around)
		public void next()
		{
			if (trialMode)
			{
				++trialCounter;
				if (trialCounter == trialsPerSequence())
					trialCounter = 0;

				// play hit or miss audio sound
				if (sequenceArray.get(sequenceCounter)[trialCounter].getError() == 0)
					playSound(tickSound);
				else
					playSound(errorSound);

			} else
			{
				++sequenceCounter;
				trialCounter = 0;
				if (sequenceCounter == sequenceArray.size())
					sequenceCounter = 0;
			}
			this.repaint();
		}

		// display trace data for previous block of trials (with wrap around)
		public void previous()
		{
			if (trialMode)
			{
				--trialCounter;
				if (trialCounter < 0)
					trialCounter = trialsPerSequence() - 1;

				// play hit or miss audio sound
				if (sequenceArray.get(sequenceCounter)[trialCounter].getError() == 0)
					playSound(tickSound);
				else
					playSound(errorSound);

			} else
			{
				--sequenceCounter;
				trialCounter = 0;
				if (sequenceCounter < 0)
					sequenceCounter = sequenceArray.size() - 1;
			}
			this.repaint();
		}

		// return the trial summary stats to show in the upper-left of the trace panel
		private String[] getTrialModeSummary(int sequenceIdx, int trialIdx)
		{
			// get the trials for the current sequence
			Trial[] t = sequenceArray.get(sequenceIdx);

			StringBuilder sb = new StringBuilder();
			sb.append(String.format("File = %s:", fileName));
			sb.append(String.format("Participant = %s:", participantCode));
			sb.append(String.format("Condition = %s:", conditionCode));
			sb.append(String.format("Block = %s:", blockCode));
			sb.append(String.format("Sequence %d of %d (A=%d, W=%d):", sequenceIdx + 1, sequenceArray.size(),
					t[trialIdx].a, t[trialIdx].w));
			sb.append(String.format("Trial %d of %d:", trialIdx + 1, trialsPerSequence()));
			sb.append("-----:");

			// compute accuracy measures (first we need data in the correct format for AccuracyMeasures)
			Point2D.Double from = new Point2D.Double(t[trialIdx].getXFrom(), t[trialIdx].getYFrom());
			Point2D.Double to = new Point2D.Double(t[trialIdx].getXTo(), t[trialIdx].getYTo());
			double width = t[trialIdx].w;
			AccuracyMeasures ac = new AccuracyMeasures(mode, from, to, width, t[trialIdx].tracePoint);

			sb.append(String.format("Number of points = %d:", t[trialIdx].tracePoint.length));
			sb.append(String.format("MT = %d ms:", t[trialIdx].getMT()));
			sb.append(String.format("Error = %d:", t[trialIdx].getError()));
			sb.append(String.format("TRE = %d:", ac.getTRE()));
			sb.append(String.format("TAC = %d:", ac.getTAC()));
			sb.append(String.format("MDC = %d:", ac.getMDC()));
			sb.append(String.format("ODC = %d:", ac.getODC()));
			sb.append(String.format("MV = %1.2f:", ac.getMV()));
			sb.append(String.format("ME = %1.2f:", ac.getME()));
			sb.append(String.format("MO = %1.2f", ac.getMO()));

			return sb.toString().split(":");
		}

		// return the sequence summary stats for the upper-left of the trace panel
		private String[] getSequenceModeSummary(int sequenceIdx, int taskMode)
		{
			// get the trials for the specified sequence
			Trial[] t = sequenceArray.get(sequenceIdx);

			StringBuilder sb = new StringBuilder();
			sb.append(String.format("File = %s:", fileName));
			sb.append(String.format("Participant = %s:", participantCode));
			sb.append(String.format("Condition = %s:", conditionCode));
			sb.append(String.format("Block = %s:", blockCode));
			sb.append(String.format("Sequence %d of %d (A=%d, W=%d):", sequenceIdx + 1, sequenceArray.size(), t[0].a,
					t[0].w));
			sb.append("Trial (all trials):");
			sb.append("-----:");

			// compute the means for the measures over all trials in the sequence
			double meanMT = 0.0;
			double meanER = 0.0;
			double meanTRE = 0.0;
			double meanTAC = 0.0;
			double meanMDC = 0.0;
			double meanODC = 0.0;
			double meanMV = 0.0;
			double meanME = 0.0;
			double meanMO = 0.0;

			// iterate through the trials in this sequence
			for (int i = 0; i < t.length; ++i)
			{
				// 1st: get the data in the right format
				Point2D.Double from = new Point2D.Double(t[i].getXFrom(), t[i].getYFrom());
				Point2D.Double to = new Point2D.Double(t[i].getXTo(), t[i].getYTo());
				double width = t[i].w;
				AccuracyMeasures ac = new AccuracyMeasures(mode, from, to, width, t[i].tracePoint);

				meanMT += t[i].getMT();
				meanER += t[i].getError();
				meanTRE += ac.getTRE();
				meanTAC += ac.getTAC();
				meanMDC += ac.getMDC();
				meanODC += ac.getODC();
				meanMV += ac.getMV();
				meanME += ac.getME();
				meanMO += ac.getMO();
			}

			// now, we'll work on getting throughput
			Point2D.Double[] from = new Point2D.Double[t.length];
			Point2D.Double[] to = new Point2D.Double[t.length];
			Point2D.Double[] select = new Point2D.Double[t.length];
			int[] mt = new int[t.length];
			for (int i = 0; i < t.length; ++i)
			{
				from[i] = new Point2D.Double(t[i].xFrom, t[i].yFrom);
				to[i] = new Point2D.Double(t[i].xTo, t[i].yTo);
				double selectX = t[i].tracePoint[t[i].tracePoint.length - 1].x;
				double selectY = t[i].tracePoint[t[i].tracePoint.length - 1].y;
				select[i] = new Point2D.Double(selectX, selectY);
				mt[i] = t[i].getMT();
			}
			Throughput tp = new Throughput();
			tp.setData("", t[0].a, t[0].w, taskMode, Throughput.SERIAL, from, to, select, mt);

			meanMT /= t.length;
			meanER /= t.length;
			meanTRE /= t.length;
			meanTAC /= t.length;
			meanMDC /= t.length;
			meanODC /= t.length;
			meanMV /= t.length;
			meanME /= t.length;
			meanMO /= t.length;

			sb.append("Performance measures (sequence mean):");
			sb.append(String.format("MT = %.1f ms:", meanMT));
			sb.append(String.format("Error rate = %.1f%%:", meanER * 100.0));
			sb.append(String.format("Throughput = %.2f bits/s:", tp.getThroughput()));
			sb.append("-----:");

			sb.append("Accuracy measures (mean per trial):");
			sb.append(String.format("TRE = %.2f:", meanTRE));
			sb.append(String.format("TAC = %.2f:", meanTAC));
			sb.append(String.format("MDC = %.2f:", meanMDC));
			sb.append(String.format("ODC = %.2f:", meanODC));
			sb.append(String.format("MV = %1.2f:", meanMV));
			sb.append(String.format("ME = %1.2f:", meanME));
			sb.append(String.format("MO = %1.2f", meanMO));

			return sb.toString().split(":");
		}
	} // end TracePanel

	// =======================================================================
	// Trial - holds all the information for a trial, including the trace data
	// =======================================================================

	class Trial
	{
		int mode;
		int sequenceIndex;
		int a;
		int w;
		int trialIndex;
		double xFrom;
		double yFrom;
		double xTo;
		double yTo;
		int[] timeStamp;
		Point2D.Double[] tracePoint;

		Trial(int modeArg, int sequenceIndexArg, int aArg, int wArg, int trialIndexArg, double xFromArg,
				double yFromArg, double xToArg, double yToArg, int[] tArg, Point2D.Double[] pArg)
		{
			mode = modeArg;
			sequenceIndex = sequenceIndexArg;
			a = aArg;
			w = wArg;
			trialIndex = trialIndexArg;
			xFrom = xFromArg;
			yFrom = yFromArg;
			xTo = xToArg;
			yTo = yToArg;
			timeStamp = tArg;
			tracePoint = pArg;
		}

		public String toString()
		{
			String s = "sequence=" + sequenceIndex + ", " + "a=" + a + ", " + "w=" + w + ", " + "trialIndex="
					+ trialIndex + ", " + "xFrom=" + xFrom + ", " + "yFrom=" + yFrom + ", " + "xTo=" + xTo + ", "
					+ "yTo=" + yTo + ", " + "samples=";

			for (int i = 0; i < tracePoint.length; ++i)
				s += "{" + timeStamp[i] + "," + tracePoint[i].getX() + "," + tracePoint[i].getY() + "},";
			return s;
		}

		public int getSequence()
		{
			return sequenceIndex;
		}

		public int getTrialNumber()
		{
			return trialIndex;
		}

		public int getA()
		{
			return a;
		}

		public int getW()
		{
			return w;
		}

		public int getMT()
		{
			return timeStamp[timeStamp.length - 1] - timeStamp[0];
		}

		public int getError()
		{
			if (mode == MODE_2D)
			{
				double distanceFromCenter = Math.sqrt(Math.pow(tracePoint[tracePoint.length - 1].x - xTo, 2)
						+ Math.pow(tracePoint[tracePoint.length - 1].y - yTo, 2));
				double radius = w / 2.0f + 1.0f; // "+1" seems to be necessary
				if (distanceFromCenter > radius)
					return 1; // error
				else
					return 0; // no error
			} else // 1D mode
			{
				if (Math.abs(tracePoint[tracePoint.length - 1].x - xTo) > w / 2.0f)
					return 1; // error
				else
					return 0; // no error

			}
		}

		public double getXFrom()
		{
			return xFrom;
		}

		public double getYFrom()
		{
			return yFrom;
		}

		public double getXTo()
		{
			return xTo;
		}

		public double getYTo()
		{
			return yTo;
		}

		public int[] getTimeStamps()
		{
			return timeStamp;
		}

		public Point2D.Double[] getTracePoints()
		{
			return tracePoint;
		}
	}

	@Override
	public void onPanZoomPanelAction(PanZoomPanelEvent pzpe)
	{
		scaleTextField.setText(String.format("%5.2f", pzpe.getScale()));
		tracePanel.panelScale = pzpe.getScale();
	}

	// Initialize stream for sound clip
	public Clip initSound(String soundFile)
	{
		AudioInputStream audioInputStream;
		Clip c = null;
		try
		{
			// Added for executable Jar:
			audioInputStream = AudioSystem
					.getAudioInputStream(new BufferedInputStream(getClass().getResourceAsStream(soundFile)));
			c = AudioSystem.getClip();
			c.open(audioInputStream);
		} catch (Exception e)
		{
			GoFitts.showError("ERROR: Unable to load " + soundFile);
		}
		return c;
	}

	// play sound
	public void playSound(Clip c)
	{
		if (c != null)
		{
			// NOTE: for *some* files, sounds better if just a portion of the clip is played (based on trial and error)
			c.setFramePosition(c.getFrameLength() - 500);
			c.start(); // stops at end of clip
		}
	}
}
