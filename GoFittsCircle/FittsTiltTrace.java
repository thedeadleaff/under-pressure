import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * <hr>
 * 
 * <table width="100%" summary="Anova2GUI title">
 * <tr style="background-color:#cccccc">
 * <td align="center">
 * <h1>FittsTiltTrace</h1>
 * </table>
 * 
 * <h2>Summary</h2>
 * 
 * <ul>
 * <li>Utility software to plot the trace data in the sd3 files created by FittsTilt.
 * </ul>
 * 
 * <h2>Related References</h2>
 * 
 * <ul>
 * <li><a href="https://www.yorku.ca/mack/nordichi2012.html">
 * "The Application of Fitts' Law to Tilt-based Interaction"</a>, by MacKenzie and Teather (<i>NordiCHI 2012</i>). This
 * paper presents a user study using FittsTilt, an Android variation of FittsTaskTwo using device tilt for input. (
 * <a href="http://www.yorku.ca/mack/nordichi2012.html#Figure_9">Figure 9</a>,
 * <a href="http://www.yorku.ca/mack/nordichi2012.html#Figure_11">Figure 11</a>)
 * 
 * 
 * <li><a href="https://www.yorku.ca/mack/gi2014.html">"Position vs. Velocity Control for Tilt-based Interaction"</a>, by
 * Teather and MacKenzie, a continuation of the research in the paper above investigating order-of-control issues for
 * tilt-based interaction. (<a href="http://www.yorku.ca/mack/gi2014.html#Figure_13">Figure 13</a>)
 * </ul>
 * 
 * <h2>Program Operation</h2>
 * 
 * FittsTiltTrace is the same as FittsTrace except the sd3 input data are in a different format. FittsTiltTrace
 * processes the sd3 data created by the Android application FittsTilt (see Related References above). An extra line is
 * included for each trial representing the tilt of the device. An example follows:
 * 
 * <ul>
 * <li><a href="FittsTilt-P16-S01-B01-TG50-FE-sd3-example.txt">sd3 example</a>
 * </ul>
 * <p>
 * 
 * The data are comma-delimited, full precision, and there are lots of sample points; so, the file above is hard to view
 * in a text editor. Below is an example of how this file might look after importing in to Excel. Some sample points
 * from the first trial are highlighted: (click to enlarge)
 * 
 * <center><p> <a href="FittsTiltTrace-2.jpg"><img src="FittsTiltTrace-2.jpg" width=500 alt="FittsTiltTrace-2"></a>
 * </center>
 * <p>
 * 
 * In the plots created by FittsTiltTrace, the width of the trace is proportional to the device tilt. Below is an
 * example: (click to enlarge)
 * 
 * <center><p> <a href="FittsTiltTrace-1.jpg"><img src="FittsTiltTrace-1.jpg" width=500 alt="FittsTiltTrace-1"></a>
 * </center>
 * <p>
 * 
 * See as well the figures in the above-noted Related References (links above).
 * <p>
 *
 * @author Scott MacKenzie, 2011-2025
 */

public class FittsTiltTrace extends JFrame implements ActionListener, ItemListener
{
	// the following avoids a "warning" with Java 1.5.0 complier (?)
	static final long serialVersionUID = 42L;

	final String LAUNCH_MESSAGE = "Open sd3 File";

	final String[] SCALE_OPTIONS = { "0.4", "0.6", "0.8", "1.0", "1.2", "1.4", "1.6" };
	final int DEFAULT_INDEX = 3; // "1.0" (adjust as necessary)

	/*
	 * The visualization will show a circle at the location of selection. The circle diameter is equal to the ball
	 * diameter in the task. This seems quite large. The purpose in showing a large circle is to help illustrate the
	 * challenge of dwell-time selection (where where the cursor/ball must remain fully within the target for the
	 * specified dwell-time delay).
	 */
	final double BD = 20 * 1.075; // Nexus 4 FittsTilt ball diameter

	int sequenceIdx = -1;
	int trialIdx = -1;

	private TracePanel tracePanel; // for drawing traces
	private GoFittsLaunchPanel launchPanel;
	private JPanel mainPanel;
	private JTextField sd3File;
	private JButton api;
	private JButton open;
	private JButton next;
	private JButton previous;
	private JButton backToGoFitts;
	private JCheckBox showPoints;
	private JCheckBox showTraces;
	private JCheckBox trialByTrial;
	private JComboBox<String> scaleCombo;
	private JLabel scaleLabel, sd3Label;
	private JFileChooser fc;
	private File f;

	double scale = 1.0;
	private boolean traces = true;
	private boolean points = false;
	private boolean trialMode = false;

	int ballDiameter = (int)(BD * scale + 0.5);

	String participantCode, selectionMode, tiltGain;
	FittsTiltTraceSequence[] ts; // ... each of which will hold an array of TiltTrace objects
	
	File currentDirectory;

	public FittsTiltTrace(File currentDirectoryArg)
	{
		currentDirectory = currentDirectoryArg;
		
		fc = new JFileChooser(new File("."));
		fc.setFileFilter(new FileNameExtensionFilter("sd3 file", "sd3"));

		tracePanel = new TracePanel();
		tracePanel.setBorder(BorderFactory.createLineBorder(Color.gray));
		launchPanel = new GoFittsLaunchPanel();
		launchPanel.launchMessage = this.LAUNCH_MESSAGE;

		previous = new JButton("Previous");
		next = new JButton("Next");
		backToGoFitts = new JButton("Back to GoFitts");

		showTraces = new JCheckBox("Show traces");
		showPoints = new JCheckBox("Show points");
		trialByTrial = new JCheckBox("Trial Mode");
		showTraces.setSelected(true);
		showPoints.setSelected(false);
		trialByTrial.setSelected(false);

		scaleLabel = new JLabel("Scale: ");

		scaleCombo = new JComboBox<String>(SCALE_OPTIONS);
		scaleCombo.setSelectedIndex(DEFAULT_INDEX);

		scale = Double.parseDouble((String)scaleCombo.getSelectedItem());

		sd3Label = new JLabel("sd3 File ");

		api = new JButton("View API in Browser");
		open = new JButton("Open...");
		
		// add tooltip text (added July 10, 2023)
		api.setToolTipText(GoFitts.API_LOCATION + "FittsTiltTrace.html");

		sd3File = new JTextField(20);
		sd3File.setEditable(false);
		sd3File.setFocusable(false);
		sd3File.setBackground(Color.white);

		// -------------
		// add listeners
		// -------------

		api.addActionListener(this);
		next.addActionListener(this);
		previous.addActionListener(this);
		backToGoFitts.addActionListener(this);
		open.addActionListener(this);
		showTraces.addItemListener(this);
		showPoints.addItemListener(this);
		trialByTrial.addItemListener(this);
		scaleCombo.addActionListener(this);

		// make go get the focus when the frame is activated
		this.addWindowFocusListener(new WindowAdapter()
		{
			public void windowGainedFocus(WindowEvent e)
			{
				open.requestFocusInWindow();
			}
		});

		// things to do if the window is resized
		this.addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent e)
			{
				tracePanel.positionTargets(); // resize and reposition targets based on new size of taskPanel
			}
		});

		// ------------------
		// arrange components
		// ------------------

		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(5, 5, 5, 5), new EtchedBorder()));		
		mainPanel.add(launchPanel, "Center");

		JPanel pp = new JPanel();
		pp.add(api);
		pp.setAlignmentY(CENTER_ALIGNMENT);

		JPanel buttonPanelLeft = new JPanel();
		buttonPanelLeft.add(pp);

		JPanel buttonPanelRight = new JPanel();
		JLabel dummy = new JLabel("");
		dummy.setPreferredSize(api.getPreferredSize());
		buttonPanelRight.add(dummy);

		JPanel buttonPanelCentre = new JPanel();
		buttonPanelCentre.add(scaleLabel);
		buttonPanelCentre.add(scaleCombo);
		buttonPanelCentre.add(showTraces);
		buttonPanelCentre.add(showPoints);
		buttonPanelCentre.add(trialByTrial);
		buttonPanelCentre.add(sd3Label);
		buttonPanelCentre.add(sd3File);
		buttonPanelCentre.add(open);
		buttonPanelCentre.add(previous);
		buttonPanelCentre.add(next);
		buttonPanelCentre.add(backToGoFitts);
		buttonPanelCentre.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.add(buttonPanelLeft, BorderLayout.WEST);
		buttonPanel.add(buttonPanelCentre, BorderLayout.CENTER);
		buttonPanel.add(buttonPanelRight, BorderLayout.EAST);

		JPanel p = new JPanel(new BorderLayout());
		p.add(mainPanel, BorderLayout.CENTER);
		p.add(buttonPanel, BorderLayout.SOUTH);
		p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		this.pack();
		this.setContentPane(p);
	}

	// -------------------------------
	// implement ItemListener method
	// -------------------------------
	public void itemStateChanged(ItemEvent ie)
	{
		Object source = ie.getSource();
		if (source == showTraces)
			traces = !traces;
		else if (source == showPoints)
			points = !points;
		else if (source == trialByTrial)
		{
			trialMode = !trialMode;
			trialIdx = 0;
		}
		this.repaint();
	}

	// -------------------------------
	// implement ActionListener method
	// -------------------------------

	public void actionPerformed(ActionEvent ae)
	{
		Object source = ae.getSource();

		if (source == next)
			next();
		else if (source == previous)
			previous();
		else if (source == backToGoFitts)
		{
			this.dispose();
			GoFittsFrame frame = new GoFittsFrame(GoFitts.FITTS_TILT_TRACE, currentDirectory);

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
				loadData();
				if (f == null) // f is set to null in loadData if there is a problem loading data
					return;
				
				currentDirectory = f.isDirectory() ? f : f.getParentFile();
				
				sd3File.setText(f.getName());
				mainPanel.removeAll();
				mainPanel.add(tracePanel, BorderLayout.CENTER);
				tracePanel.setSize(new Dimension(mainPanel.getWidth(), mainPanel.getHeight())); // a kluge or what!
				mainPanel.revalidate();
			}
		}

		else if (source == scaleCombo)
		{
			scale = Double.parseDouble((String)scaleCombo.getSelectedItem());
			ballDiameter = (int)(20.0 * scale + 0.5);
			tracePanel.repaint();
		}

		else if (source == api)
		{
			boolean okReturned = GoFitts.openBrowser("index.html?FittsTiltTrace.html");
			if (!okReturned)
			{
				popupErrorDialog("Error launching browser");
				return;
			}
		}
	}

	/*
	 * Load the sd3 data. This method must be tailored to the format of the data in the input file. The goal is to
	 * create and array of TiltSequence objects that will be subsequently accessed to show the targets and trace data
	 * from trials in the experiment.
	 */
	@SuppressWarnings("resource") // avoids resource leak warning (which I'm not sure how to avoid)
	private void loadData()
	{
		String[] fileNameTokens = f.getName().split("[-.]");
		if (fileNameTokens.length != 7) // 7 tokens required in filename
		{
			popupErrorDialog(String.format("Oops! \"%s\" is not an sd3 file from FittsTilt. Try again.", f.getName()));
			f = null;
			return;
		}
		// get condition arguments
		participantCode = fileNameTokens[1];
		selectionMode = fileNameTokens[5];
		tiltGain = fileNameTokens[4];

		Scanner inFile = null;
		try
		{
			inFile = new Scanner(f);
		} catch (FileNotFoundException e)
		{
			popupErrorDialog("File not found error!\nFile: " + f.toString());
			f = null;
			return;
		}

		Vector<FittsTiltTraceTrial> vTrial = new Vector<FittsTiltTraceTrial>();
		Vector<FittsTiltTraceSequence> vSequence = new Vector<FittsTiltTraceSequence>();
		int sequenceIdxSave = 0;
		double aSave = 0.0;
		double wSave = 0.0;

		inFile.nextLine(); // discard 1st header
		inFile.nextLine(); // discard 2nd header
		double a = 0;
		double w = 0;
		while (inFile.hasNextLine())
		{
			String timeLine = inFile.nextLine().trim();
			String xLine = inFile.nextLine().trim();
			String yLine = inFile.nextLine().trim();
			String tiltLine = inFile.nextLine().trim();

			// delete commas at end of lines (if any)
			if (timeLine.charAt(timeLine.length() - 1) == ',')
				timeLine = timeLine.substring(0, timeLine.length() - 1);
			if (xLine.charAt(xLine.length() - 1) == ',')
				xLine = xLine.substring(0, xLine.length() - 1);
			if (yLine.charAt(yLine.length() - 1) == ',')
				yLine = yLine.substring(0, yLine.length() - 1);
			if (tiltLine.charAt(tiltLine.length() - 1) == ',')
				tiltLine = tiltLine.substring(0, tiltLine.length() - 1);

			String[] timeTokens = timeLine.split(",");
			String[] xTokens = xLine.split(",");
			String[] yTokens = yLine.split(",");
			String[] tiltTokens = tiltLine.split(",");

			if (!(timeTokens.length == xTokens.length && xTokens.length == yTokens.length
					&& yTokens.length == tiltTokens.length) || timeTokens.length < 10)
			{
				popupErrorDialog("Bad time, x, y, or tilt data. Check format of data file.");
				return;
			}

			int sequenceIdx;
			double fromX, fromY, toX, toY;
			try
			{
				sequenceIdx = Integer.parseInt(xTokens[0]); // sequence
				a = Double.parseDouble(xTokens[1]); // a
				w = Double.parseDouble(xTokens[2]); // w
				// trial at idx 3 (implicit)
				fromX = Double.parseDouble(xTokens[4]); // from_x
				fromY = Double.parseDouble(xTokens[5]); // from_y
				toX = Double.parseDouble(xTokens[6]); // to_x
				toY = Double.parseDouble(xTokens[7]); // to_y
				// "x=" token at idx 8 (not needed)
			} catch (NumberFormatException e)
			{
				popupErrorDialog("Error parsing integers.  Check format of data file.");
				return;
			}

			FittsTiltTracePoint[] tp = new FittsTiltTracePoint[timeTokens.length - 9];

			for (int i = 0; i < timeTokens.length - 9; ++i)
			{
				int time = Integer.parseInt(timeTokens[i + 9]);
				int x = Integer.parseInt(xTokens[i + 9]);
				int y = Integer.parseInt(yTokens[i + 9]);
				double tilt = Double.parseDouble(tiltTokens[i + 9]);
				tp[i] = new FittsTiltTracePoint(x, y, time, tilt);
			}
			FittsTiltTraceTrial temp = new FittsTiltTraceTrial(fromX, fromY, toX, toY, w, ballDiameter, tp);
			if (sequenceIdx != sequenceIdxSave)
			{
				FittsTiltTraceTrial[] tt = new FittsTiltTraceTrial[vTrial.size()];
				vTrial.copyInto(tt);
				vSequence.add(new FittsTiltTraceSequence(sequenceIdxSave, aSave, wSave, tt));
				vTrial.clear();
			}
			vTrial.add(temp);
			sequenceIdxSave = sequenceIdx;
			aSave = a;
			wSave = w;
		}
		inFile.close();

		FittsTiltTraceTrial[] tt = new FittsTiltTraceTrial[vTrial.size()];
		vTrial.copyInto(tt);
		vSequence.add(new FittsTiltTraceSequence(sequenceIdxSave, aSave, wSave, tt));

		ts = new FittsTiltTraceSequence[vSequence.size()];
		vSequence.copyInto(ts);

		sequenceIdx = 0;
		trialIdx = 0;
		this.repaint();
		return;
	} // end loadData

	void popupErrorDialog(String message)
	{
		JTextArea tmp = new JTextArea(message);
		tmp.setBackground(new Color(212, 208, 200));
		JOptionPane.showMessageDialog(this, tmp, "Error", JOptionPane.ERROR_MESSAGE);
	}

	// display trace data for the next sequence/trial
	public void next()
	{
		if (trialMode)
		{
			++trialIdx;
			if (trialIdx >= trialsPerSequence())
				trialIdx = 0;
		} else
		{
			++sequenceIdx;
			if (sequenceIdx >= numberOfSequences())
				sequenceIdx = 0;
		}
		this.repaint();
	}

	// display trace data for previous sequence/trial
	public void previous()
	{
		if (trialMode)
		{
			--trialIdx;
			if (trialIdx < 0)
				trialIdx = trialsPerSequence() - 1;
		} else
		{
			--sequenceIdx;
			if (sequenceIdx < 0)
				sequenceIdx = numberOfSequences() - 1;
		}
		this.repaint();
	}

	// return the number of sequences of trials (in this file/block)
	int numberOfSequences()
	{
		if (ts == null)
			return 0;
		else
			return ts.length;
	}

	// return the number of trials in the current sequence
	int trialsPerSequence()
	{
		if (ts == null)
			return 0;
		else
			return ts[0].tiltTrial.length;
	}

	// -----------------------------------------
	// A class to display the targets and traces
	// -----------------------------------------

	class TracePanel extends JPanel
	{
		static final long serialVersionUID = 42L;

		final double BOTTOM_MARGIN = 50.0;
		double offsetX, offsetY;

		TracePanel()
		{
			this.setBackground(Color.WHITE);
		}

		public void paintComponent(Graphics g)
		{
			super.paintComponent(g); // paint background
			this.paintInfo(g);
			this.drawTargets(g);
			this.drawTraces(g);
		}

		public void positionTargets()
		{
			if (ts == null || ts.length == 0)
				return;

			double centerX = scale * (ts[sequenceIdx].tiltTrial[0].fromX + ts[sequenceIdx].tiltTrial[0].toX) / 2.0;
			double centerY = scale * (ts[sequenceIdx].tiltTrial[0].fromY + ts[sequenceIdx].tiltTrial[0].toY) / 2.0;
			double centerPanelX = this.getWidth() / 2.0;
			double centerPanelY = this.getHeight() / 2.0 - BOTTOM_MARGIN;
			offsetX = centerPanelX - centerX;
			offsetY = centerPanelY - centerY;
		}

		public void drawTargets(Graphics g)
		{
			if (ts == null || ts.length == 0)
				return;

			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(Color.black);

			for (int i = 0; i < ts[sequenceIdx].tiltTrial.length; ++i)
			{
				double w = scale * ts[sequenceIdx].w; // same throughout sequence

				if (i == 0) // "from" target in 1st trial only (to avoid duplication)
				{
					double x = offsetX + scale * ts[sequenceIdx].tiltTrial[i].fromX - (w / 2);
					double y = offsetY + scale * ts[sequenceIdx].tiltTrial[i].fromY - (w / 2);
					Ellipse2D.Double e = new Ellipse2D.Double(x, y, w, w);
					g2.draw(e);
				}

				double x = offsetX + scale * ts[sequenceIdx].tiltTrial[i].toX - (w / 2);
				double y = offsetY + scale * ts[sequenceIdx].tiltTrial[i].toY - (w / 2);
				Ellipse2D.Double e = new Ellipse2D.Double(x, y, w, w);
				g2.draw(e);

				// in trial mode, draw target to select last (to occlude overlapping targets, if any)
				if (trialMode)
				{
					x = offsetX + scale * ts[sequenceIdx].tiltTrial[trialIdx].toX - (w / 2);
					y = offsetY + scale * ts[sequenceIdx].tiltTrial[trialIdx].toY - (w / 2);
					e = new Ellipse2D.Double(x, y, w, w);
					g2.setColor(Color.lightGray);
					g2.fill(e); // fill the target to select in light gray
					g2.setColor(Color.black);
					g2.draw(e);
				}
			}
		}

		public void drawTraces(Graphics g)
		{
			if (ts == null || ts.length == 0)
				return;

			Graphics2D g2 = (Graphics2D)g;
			Ellipse2D.Double e;

			for (int i = 0; i < ts[sequenceIdx].tiltTrial.length; ++i)
			{
				FittsTiltTracePoint[] tp = ts[sequenceIdx].tiltTrial[i].p;

				// draw special marker at start of sequence
				if (i == 0 && (!trialMode || (trialMode && trialIdx == i)))
				{
					double xx = offsetX + scale * ts[sequenceIdx].tiltTrial[i].p[0].x - 5;
					double yy = offsetY + scale * ts[sequenceIdx].tiltTrial[i].p[0].y - 5;
					e = new Ellipse2D.Double(xx, yy, 11, 11);
					g2.setColor(Color.BLACK);
					g2.draw(e);
					g2.setColor(Color.YELLOW);
					g2.fill(e);
				}

				int j;
				for (j = 0; j < tp.length - 1; ++j)
				{
					double x1 = offsetX + scale * tp[j].x;
					double y1 = offsetY + scale * tp[j].y;
					double x2 = offsetX + scale * tp[j + 1].x;
					double y2 = offsetY + scale * tp[j + 1].y;

					Line2D.Double inkSegment = new Line2D.Double(x1, y1, x2, y2);

					g2.setColor(new Color(0, 0, 128)); // deep blue

					// make the thickness proportional to the tilt magnitude
					float lineWidth = (float)tp[j + 1].tilt / 2f;
					g2.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

					if (traces && (!trialMode || (trialMode && trialIdx == i)))
						g2.draw(inkSegment); // draw it!

					Ellipse2D.Double pt = new Ellipse2D.Double(x2 - 1, y2 - 1, 2, 2);
					if (points && (!trialMode || (trialMode && trialIdx == i)))
					{
						g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
						g2.setColor(Color.red);
						g2.draw(pt);
						g2.fill(pt);
					}
				}
			}

			// draw marker at selection point (do last to avoid occulsion)
			for (int i = 0; i < ts[sequenceIdx].tiltTrial.length; ++i)
			{
				int lastPointIdx = ts[sequenceIdx].tiltTrial[i].p.length - 1;
				double x = offsetX + scale * (ts[sequenceIdx].tiltTrial[i].p[lastPointIdx].x - ballDiameter / 2.0);
				double y = offsetY + scale * (ts[sequenceIdx].tiltTrial[i].p[lastPointIdx].y - ballDiameter / 2.0);
				e = new Ellipse2D.Double(x, y, ballDiameter, ballDiameter);
				g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g2.setColor(Color.RED);
				if (!trialMode || (trialMode && trialIdx == i))
				{
					g2.draw(e);
					g2.fill(e);
				}
			}
		}

		public void clear()
		{
			this.repaint();
		}

		private void paintInfo(Graphics g)
		{
			String header1 = String.format("Sequence: %d of %d (A=%.1f, W=%.1f)", (sequenceIdx + 1),
					numberOfSequences(), ts[sequenceIdx].a, ts[sequenceIdx].w);
			String header2 = trialMode ? String.format("Trial: %d of %d", (trialIdx + 1), trialsPerSequence())
					: "Trial: (all trials)";

			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(new Color(128, 0, 0));
			g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
			FontMetrics fm = g2.getFontMetrics();
			int height = fm.getHeight();

			int yOffset = 10;
			int line = 1;
			g2.drawString("Participant = " + participantCode, yOffset, line++ * height);
			g2.drawString("Selection mode = " + selectionMode, yOffset, line++ * height);
			g2.drawString("Tilt gain = " + tiltGain, yOffset, line++ * height);
			g2.drawString(header1, yOffset, line++ * height);
			g2.drawString(header2, yOffset, line++ * height);
			g2.drawString("-----", yOffset, line++ * height);

			if (trialMode)
			{
				g2.drawString(String.format("Number of points = %d",
						ts[sequenceIdx].tiltTrial[trialIdx].numberOfSamplePoints), yOffset, line++ * height);
				g2.drawString(String.format("PT = %d ms", ts[sequenceIdx].tiltTrial[trialIdx].positioningTime), yOffset,
						line++ * height);
				g2.drawString(String.format("ST = %d ms", ts[sequenceIdx].tiltTrial[trialIdx].selectionTime), yOffset,
						line++ * height);
				g2.drawString(String.format("MT = %d ms", ts[sequenceIdx].tiltTrial[trialIdx].movementTime), yOffset,
						line++ * height);
				g2.drawString(String.format("Max Tilt = %1.1f degrees", ts[sequenceIdx].tiltTrial[trialIdx].maxTilt),
						10, line++ * height);
				g2.drawString(String.format("TRE = %d", ts[sequenceIdx].tiltTrial[trialIdx].tre), yOffset,
						line++ * height);
				g2.drawString(String.format("TAC = %d", ts[sequenceIdx].tiltTrial[trialIdx].tac), yOffset,
						line++ * height);
				g2.drawString(String.format("MDC = %d", ts[sequenceIdx].tiltTrial[trialIdx].mdc), yOffset,
						line++ * height);
				g2.drawString(String.format("ODC = %d", ts[sequenceIdx].tiltTrial[trialIdx].odc), 10, line++ * height);
				g2.drawString(String.format("MV = %1.2f", ts[sequenceIdx].tiltTrial[trialIdx].mv), yOffset,
						line++ * height);
				g2.drawString(String.format("ME = %1.2f", ts[sequenceIdx].tiltTrial[trialIdx].me), yOffset,
						line++ * height);
				g2.drawString(String.format("MO = %1.2f", ts[sequenceIdx].tiltTrial[trialIdx].mo), yOffset,
						line++ * height);
			} else
			// sequence mode
			{
				g2.drawString("Performance measures (mean per sequence)", yOffset, line++ * height);
				g2.drawString(String.format("PT = %.1f ms", ts[sequenceIdx].meanPT), yOffset, line++ * height);
				g2.drawString(String.format("ST = %.1f ms", ts[sequenceIdx].meanST), yOffset, line++ * height);
				g2.drawString(String.format("MT = %.1f ms", ts[sequenceIdx].meanMT), yOffset, line++ * height);
				g2.drawString(String.format("Max tilt = %.1f degrees", ts[sequenceIdx].meanMaxTilt), yOffset,
						line++ * height);
				g2.drawString(String.format("TRE = %.1f", ts[sequenceIdx].meanTRE), yOffset, line++ * height);
				g2.drawString(String.format("TAC = %.1f", ts[sequenceIdx].meanTAC), yOffset, line++ * height);
				g2.drawString(String.format("MDC = %.1f", ts[sequenceIdx].meanMDC), yOffset, line++ * height);
				g2.drawString(String.format("ODC = %.1f", ts[sequenceIdx].meanODC), yOffset, line++ * height);
				g2.drawString(String.format("MV = %1.2f", ts[sequenceIdx].meanMV), yOffset, line++ * height);
				g2.drawString(String.format("ME = %1.2f", ts[sequenceIdx].meanME), yOffset, line++ * height);
				g2.drawString(String.format("MO = %1.2f", ts[sequenceIdx].meanMO), yOffset, line++ * height);
			}
		} // end PaintInfo

	} // end FittsTiltTracePanel

	class FittsTiltTraceSequence
	{
		int sequenceIdx;
		double a, w;
		FittsTiltTraceTrial[] tiltTrial;
		double meanTRE, meanTAC, meanMDC, meanODC, meanMV, meanME, meanMO;
		double meanPT, meanST, meanMT, meanMaxTilt;

		FittsTiltTraceSequence(int sequenceIdxArg, double aArg, double wArg, FittsTiltTraceTrial[] tiltTrialArg)
		{
			sequenceIdx = sequenceIdxArg;
			a = aArg;
			w = wArg;
			tiltTrial = tiltTrialArg;

			// compute means for accuracy measures
			meanTRE = meanTAC = meanMDC = meanODC = meanMV = meanME = meanMO = 0.0;

			// compute means for other measures
			meanPT = meanST = meanMT = meanMaxTilt = 0.0;

			for (int i = 0; i < tiltTrial.length; ++i)
			{
				meanTRE += tiltTrial[i].tre;
				meanTAC += tiltTrial[i].tac;
				meanMDC += tiltTrial[i].mdc;
				meanODC += tiltTrial[i].odc;
				meanMV += tiltTrial[i].mv;
				meanME += tiltTrial[i].me;
				meanMO += tiltTrial[i].mo;
				meanPT += tiltTrial[i].positioningTime;
				meanST += tiltTrial[i].selectionTime;
				meanMT += tiltTrial[i].movementTime;
				meanMaxTilt += tiltTrial[i].maxTilt;
			}
			meanTRE /= tiltTrial.length;
			meanTAC /= tiltTrial.length;
			meanMDC /= tiltTrial.length;
			meanODC /= tiltTrial.length;
			meanMV /= tiltTrial.length;
			meanME /= tiltTrial.length;
			meanMO /= tiltTrial.length;
			meanPT /= tiltTrial.length;
			meanST /= tiltTrial.length;
			meanMT /= tiltTrial.length;
			meanMaxTilt /= tiltTrial.length;

		}
	} // end FittsTiltTraceSequence

	class FittsTiltTracePoint
	{
		int x, y;
		int timeStamp;
		double tilt;

		FittsTiltTracePoint(int xArg, int yArg, int tArg, double tiltArg)
		{
			x = xArg;
			y = yArg;
			timeStamp = tArg;
			tilt = tiltArg;
		}
	} // end FittsTiltTracePoint

	class FittsTiltTraceTrial
	{
		final float TWO_TIMES_PI = 6.283185307f;

		double fromX, fromY, toX, toY;
		double w;
		double selectX, selectY;
		int positioningTime, selectionTime, movementTime;
		int ballDiameter; // diameter of the ball that is moved to a target (only used to compute positioning time)

		FittsTiltTracePoint[] p; // raw trace points

		AccuracyMeasures ac; // 7 accuracy measures (see below)
		int tre; // target re-entries
		int tac; // task axis crossings
		int mdc; // movement direction changes
		int odc; // orthogonal direction changes
		double mv; // movement variability
		double me; // movement error
		double mo; // movement offset

		int numberOfSamplePoints; // ... in the trace array

		double maxTilt; // maximum tilt angle (degrees) during a trial

		FittsTiltTraceTrial(double fromXArg, double fromYArg, double toXArg, double toYArg, double wArg,
				int ballDiameterArg, FittsTiltTracePoint[] pArg)
		{
			fromX = fromXArg;
			fromY = fromYArg;
			toX = toXArg;
			toY = toYArg;
			w = wArg;
			ballDiameter = ballDiameterArg;
			p = pArg;

			selectX = p[p.length - 1].x;
			selectY = p[p.length - 1].y;

			// do things that require iterating over the trace points
			Point2D.Double[] path = new Point2D.Double[p.length];
			int startTime = p[0].timeStamp;
			movementTime = p[p.length - 1].timeStamp - startTime;
			boolean ballEnteredTarget = false;
			for (int i = 0; i < path.length; ++i)
			{
				// create an array of Point2D.Double's (for AccuracyMeasures)
				path[i] = new Point2D.Double(p[i].x, p[i].y);

				// find the maximum tilt
				if (p[i].tilt > maxTilt)
					maxTilt = p[i].tilt;

				// calculated positioning time (time to first-entry)
				double distanceFromTarget = Math.hypot(p[i].x - toX, p[i].y - toY);
				if (!ballEnteredTarget && distanceFromTarget < (w / 2.0 - ballDiameter / 2.0))
				{
					ballEnteredTarget = true;
					positioningTime = p[i].timeStamp - startTime;
				}
			}
			selectionTime = movementTime - positioningTime;

			ac = new AccuracyMeasures(Throughput.TWO_DIMENSIONAL, new Point2D.Double(fromX, fromY), new Point2D.Double(toX, toY), w, path);
			tre = ac.getTRE();
			tac = ac.getTAC();
			mdc = ac.getMDC();
			odc = ac.getODC();
			mv = ac.getMV();
			me = ac.getME();
			mo = ac.getMO();
			numberOfSamplePoints = path.length;
		}
	} // end FittsTiltTraceTrial
}