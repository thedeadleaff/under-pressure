import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

/**
 * <table width="100%" summary="MergeSD1Files title">
 * <tr style="background-color:#cccccc">
 * <td align="center">
 * <h1>MergeSD1Files</h1>
 * </table>
 * 
 * <h2>Summary</h2>
 * 
 * <ul>
 * <li>Utility software to merge all the sd1 files in a directory into a single file for follow-up analyses.
 * </ul>
 * 
 * <h2>Getting Started</h2>
 * 
 * MergeSD1Files is launched from GoFitts by selecting "Merge sd1 files" and clicking "Go" (below, left). MergeSD1Files
 * begins with a launch panel (below, centre). Clicking "Open..." opens a file chooser showing all the sd1 files in the
 * current directory. Navigate to a desired directory, then select any sd1 file and click "Open" (below, right).
 * 
 * <center><p> <a href="MergeSD2Files-0.jpg"><img src="MergeSD1Files-0.jpg" width="200" alt="MergeSD1Files-0"></a>
 * <a href="MergeSD1Files-1.jpg"><img src="MergeSD1Files-1.jpg" width="400" alt="MergeSD1Files-1"></a>
 * <a href="MergeSD1Files-2.jpg"><img src="MergeSD1Files-2.jpg" width="300" alt="MergeSD1Files-2"></a> </center>
 * <p>
 * 
 * Upon selecting an sd1 file and clicking "Open", MergeSD1Files reads the data from all the sd1 files in the directory
 * and merges the data into a single data matrix. One convenient feature is that the header line from the first file appears
 * in the first row of the data matrix and identifies the data in each column. An example is shown below
 * for the sd1 data from an experiment that used an early version of GoFitts called FittsTaskTwo.
 * <a href="http://www.yorku.ca/mack/icchp2016b.html">Click here</a> to view the publication where these data are
 * analysed.
 * 
 * <center><p> <a href="MergeSD1Files-3.jpg"><img src="MergeSD1Files-3.jpg" width="800" alt="MergeSD1Files-3"></a></center>
 * <p>
 * 
 * The image above contains two regions. The top region shows the data matrix. Scrollbars appear for convenient viewing
 * and inspection of the data. The bottom region identifies the files read and the number of lines of data read for each
 * file. In the example, 72 files were read with 52 data lines per file.
 * <p>
 * 
 * The data can be saved by clicking "Save" and providing a name for the output data file. Adding ".csv" as the suffix
 * facilitates importing into Excel where the fun begins!
 * 
 * <h2>Include Sub-folders</h2>
 * 
 * If the "Include Sub-folders" checkbox is selected, the app will recursively traverse sub-folders to locate and merge
 * SD1 files. This option is useful if the data are organized in sub-folders, for example, with each sub-folder holding
 * the data files for a separate participant. However, you must select an ".sd1" file in the parent directory to begin
 * the merge process. If necessary, create such a file, perhaps called "dummy.sd1". The dummy file should contain one
 * line only -- the header line from any .sd1 file.
 * <p>
 * 
 * @author Scott MacKenzie, 2017-2023
 */

public class MergeSD1Files extends JFrame implements ActionListener, ItemListener
{
	// the following avoids a "warning" with Java 1.5.0 complier (?)
	static final long serialVersionUID = 42L;

	final Color SELECT_FILE_MESSAGE_COLOR = new Color(155, 155, 155);
	final Font SELECT_FILE_MESSAGE_FONT = new Font("SannsSerif", Font.ITALIC, 96);
	final String LAUNCH_MESSAGE = "Open sd1 File";

	final Color SELECT_FILE_SUB_MESSAGE_COLOR = new Color(155, 155, 155);
	final Font SELECT_FILE_SUB_MESSAGE_FONT = new Font("SannsSerif", Font.ITALIC, 48);
	final String LAUNCH_SUB_MESSAGE = "(and merge all sd1 files in directory)";

	JButton api, open, save, backToGoFitts;
	JFileChooser fcOpen, fcSave;
	JTable dataTable;
	JPanel mainPanel, spPanel;
	JScrollPane spData, spStatus;
	JLabel setupMessage, setupSubMessage;
	JTextArea status;
	StringBuilder statusMessage;

	String[][] data;
	String[] columnNames;

	ArrayList<File> sd1Files;
	static boolean includeSubFolders;
	JCheckBox includeSubDirectoriesCheckBox;

	File currentDirectory;

	public MergeSD1Files(File currentDirectoryArg)
	{
		currentDirectory = currentDirectoryArg;

		sd1Files = new ArrayList<File>();
		includeSubFolders = false; // default

		// ----------------------------------
		// construct and configure components
		// ----------------------------------

		includeSubDirectoriesCheckBox = new JCheckBox("Include Sub-folders");
		includeSubDirectoriesCheckBox.addItemListener(this);
		includeSubDirectoriesCheckBox.setSelected(includeSubFolders);

		api = new JButton("View API in Browser");
		open = new JButton("Open...");
		save = new JButton("Save");
		backToGoFitts = new JButton("Back to GoFitts");
		
		// add tooltip text (added July 10, 2023)
		api.setToolTipText(GoFitts.API_LOCATION + "MergeSD1Files.html");

		JLabel directoryLabel = new JLabel("sd1 File");
		JTextField directory = new JTextField(20);

		fcOpen = new JFileChooser(currentDirectory);
		fcOpen.setFileFilter(new FileNameExtensionFilter("sd1 file", "sd1"));

		fcSave = new JFileChooser(currentDirectory);

		setupMessage = new JLabel(LAUNCH_MESSAGE);
		setupMessage.setFont(SELECT_FILE_MESSAGE_FONT);
		setupMessage.setForeground(SELECT_FILE_MESSAGE_COLOR);
		setupMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

		setupSubMessage = new JLabel(LAUNCH_SUB_MESSAGE);
		setupSubMessage.setFont(SELECT_FILE_SUB_MESSAGE_FONT);
		setupSubMessage.setForeground(SELECT_FILE_SUB_MESSAGE_COLOR);
		setupSubMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

		spData = new JScrollPane();
		spData.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		spData.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		spData.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		spStatus = new JScrollPane();
		spStatus.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		spStatus.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		spStatus.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		status = new JTextArea();
		status.setBackground(new Color(222, 222, 222));

		// -------------
		// add listeners
		// -------------

		api.addActionListener(this);
		open.addActionListener(this);
		save.addActionListener(this);
		backToGoFitts.addActionListener(this);

		// make open button get the focus when the frame is activated
		this.addWindowFocusListener(new WindowAdapter()
		{
			public void windowGainedFocus(WindowEvent e)
			{
				open.requestFocusInWindow();
			}
		});

		// as the window is resized, ensure the scroll panes fill the container
		this.addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent e)
			{
				spData.setPreferredSize(new Dimension(mainPanel.getWidth(), (int) (mainPanel.getHeight() / 3)));
				spStatus.setPreferredSize(new Dimension(mainPanel.getWidth(), (int) (mainPanel.getHeight() / 3)));
			}
		});

		// ------------------
		// arrange components
		// ------------------

		GoFittsLaunchPanel launchPanel = new GoFittsLaunchPanel();
		launchPanel.launchMessage = this.LAUNCH_MESSAGE;
		launchPanel.launchSubMessage = this.LAUNCH_SUB_MESSAGE;

		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(5, 5, 5, 5), new EtchedBorder()));
		mainPanel.add(launchPanel, BorderLayout.CENTER);

		// make this panel now, it will replace the messagePanel in mainPanel when the user selects an sd2 file
		spPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 3;
		spPanel.add(spData, c); // at the top and with 75% of vertical space
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		spPanel.add(spStatus, c); // at the bottom and with 25% of vertical space

		JPanel buttonPanelCentre = new JPanel();
		buttonPanelCentre.add(includeSubDirectoriesCheckBox);
		buttonPanelCentre.add(new JLabel("     "));
		buttonPanelCentre.add(directoryLabel);
		buttonPanelCentre.add(directory);
		buttonPanelCentre.add(open);
		buttonPanelCentre.add(save);
		buttonPanelCentre.add(backToGoFitts);

		JPanel buttonPanelLeft = new JPanel();
		buttonPanelLeft.add(api);

		JPanel buttonPanelRight = new JPanel();
		JLabel dummy = new JLabel("");
		dummy.setPreferredSize(api.getPreferredSize());
		buttonPanelRight.add(dummy);

		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.add(buttonPanelLeft, BorderLayout.WEST);
		buttonPanel.add(buttonPanelCentre, BorderLayout.CENTER);
		buttonPanel.add(buttonPanelRight, BorderLayout.EAST);

		JPanel p = new JPanel(new BorderLayout());
		p.add(mainPanel, "Center");
		p.add(buttonPanel, "South");

		this.setContentPane(p);

	} // end constructor

	// -------------------------------
	// implement ActionListener method
	// -------------------------------

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();

		if (source == api)
		{
			boolean okReturned = GoFitts.openBrowser("index.html?MergeSD1Files.html");
			if (!okReturned)
			{
				showError("Error launching browser");
				return;
			}
		}

		else if (source == backToGoFitts)
		{
			this.dispose();
			GoFittsFrame frame = new GoFittsFrame(GoFitts.FITTS_TASK, currentDirectory);

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
			if (fcOpen.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			{
				statusMessage = new StringBuilder();

				File tmp = fcOpen.getSelectedFile();
				String appName = tmp.getName().substring(0, fcOpen.getSelectedFile().getName().indexOf("-"));

				currentDirectory = tmp.isDirectory() ? tmp : tmp.getParentFile();
				fcSave = new JFileChooser(currentDirectory);

				// String appName = "FittsTask";
				loadDataFromSd1Files(appName, fcOpen.getCurrentDirectory());
				status.setText(statusMessage.toString());

				dataTable = new JTable(data, columnNames);
				dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				mainPanel.removeAll();
				mainPanel.add(spPanel, BorderLayout.CENTER);
				mainPanel.revalidate();
				spData.setViewportView(dataTable);
				spStatus.setViewportView(status);
			}
		}

		else if (source == save)
		{
			if (columnNames == null || columnNames.length == 0 || data == null)
			{
				showError("Oops! No data to save!");
				return;
			}

			else if (fcSave.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
			{
				File saveFile = fcSave.getSelectedFile();

				currentDirectory = saveFile.isDirectory() ? saveFile : saveFile.getParentFile();

				// check if file exists before overwriting
				// (Note: Dialog only pops up if file exists)
				if (!saveFile.exists() || okToReplace(saveFile))
					saveToFile(saveFile);
			}
		}
	}

	private void saveToFile(File fArg)
	{
		PrintWriter pw = null;
		try
		{
			pw = new PrintWriter(new BufferedWriter(new FileWriter(fArg)));
		} catch (IOException e)
		{
			showError("Can't open file '" + fArg.getName() + "' for writing");
			return;
		}

		// first write the column names as a header line (comma delimited)
		StringBuilder rowData = new StringBuilder();
		for (int i = 0; i < columnNames.length; ++i)
			rowData.append(columnNames[i]).append(',');
		rowData.deleteCharAt(rowData.length() - 1); // remove trailing comma

		pw.println(rowData.toString());

		// write the row entries (comma delimited)
		for (int i = 0; i < data.length; ++i)
		{
			rowData = new StringBuilder();
			for (int j = 0; j < data[i].length; ++j)
				rowData.append(data[i][j]).append(',');
			rowData.deleteCharAt(rowData.length() - 1); // remove trailing comma
			pw.println(rowData.toString());
		}
		pw.close();
	}

	private boolean okToReplace(File f)
	{
		final Object[] options = { "Yes", "No", "Cancel" };
		String message = String.format("The file \"%s\" already exists. Replace?", f.getName());
		return JOptionPane.showOptionDialog(this, message, "Warning", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE, null, options, options[2]) == JOptionPane.YES_OPTION;
	}

	void loadDataFromSd1Files(String appName, File directory)
	{
		// get an array of all sd1 files for the specified app in the specified directory
		File[] f = getSD1Files(appName, directory.getAbsolutePath());

		statusMessage.append(String.format("Number_of_files: %d\n", f.length));

		if (f == null || f.length == 0)
		{
			showError("Oops! No sd1 files found.");
			return;
		}

		ArrayList<String> arrayList = new ArrayList<String>();
		String line = "";
		boolean firstFile = true;
		boolean firstLine;
		Scanner inFile = null;
		int rows = 0;
		int columns = 0;
		int linesRead;

		// read all the data into an ArrayList (header line is included from the 1st file only)
		for (int i = 0; i < f.length; ++i)
		{
			linesRead = 0;
			try
			{
				inFile = new Scanner(f[i]);
				firstLine = true;
				while (inFile.hasNextLine())
				{
					line = inFile.nextLine();
					++linesRead;
					if (!firstLine || (firstFile && firstLine))
					{
						arrayList.add(line);
						++rows;
					}
					firstLine = false;
				}
				firstFile = false;
			} catch (FileNotFoundException e)
			{
				showError("FileNotFoundException");
			}
			--linesRead; // don't count header line
			statusMessage.append(String.format("File: %s, Data_lines: = %d\n", f[i].getName(), linesRead));
		}
		inFile.close();
		--rows; // don't count header line

		// build array of column names
		
		// get header (remove trailing comma, if any; Nov 30, 2018)
		String header = arrayList.get(0).trim();
		if (header.charAt(header.length() - 1) == ',')
			header = header.substring(0, header.length() - 1);
		
		String[] tmp = header.split(",");
		columnNames = new String[tmp.length + 1]; // "+1" for row number at beginning
		for (int i = 0; i < columnNames.length; ++i)
		{
			if (i == 0)
				columnNames[i] = "Row"; // we're adding a left-hand column for the row number
			else
			{
				columnNames[i] = tmp[i - 1];

				// some silly re-mapping to make summary look good for FittsTouch sd2 data
				if (columnNames[i].equals("SequenceRepeatCount"))
					columnNames[i] = "SRC";
				if (columnNames[i].equals("ErrorRate(%)"))
					columnNames[i] = "ER(%)";
			}
		}

		// build matrix (rows and columns) of data
		columns = columnNames.length; 
		data = new String[rows][columns];
		for (int i = 0; i < rows; ++i) // iterator down rows
		{
			data[i] = new String[columns];
			
			// get row and remove trailing comma, if any (Oct 30, 2018)
			String s = arrayList.get(i + 1);			
			if (s.charAt(s.length() - 1) == ',')
				s = s.substring(0, s.length() - 1);	
			
			tmp = s.trim().split(",");			

			for (int j = 0; j < columns; ++j) // iterate across columns
				data[i][j] = j == 0 ? "" + (i + 1) : tmp[j - 1].trim();
		}
	}

	// return an array of sd2 Files for the specified app in the specified folder
	// NOTE: to be included, a file's name must begin with the provided appName string
	public File[] getSD1Files(String appName, String directoryName)
	{
		sd1Files = new ArrayList<File>();
		traverse(appName, new File(directoryName), includeSubFolders);
		File[] f = new File[sd1Files.size()];
		return sd1Files.toArray(f);
	}

	static void showError(String msg)
	{
		JOptionPane.showMessageDialog(null, msg, "I/O Error", JOptionPane.ERROR_MESSAGE);
	}

	class NumericColumnSummary
	{
		double mean, sd, min, max, sum;
		int nan; // Not a Number (as defined in Double)

		NumericColumnSummary()
		{
		}

		void setData(double[] columnData) // and compute summary stats
		{
			mean = mean(columnData);
			sd = standardDeviation(columnData);

			min = Double.MAX_VALUE;
			max = Double.MIN_VALUE;
			sum = 0.0;
			nan = 0;
			for (int i = 0; i < columnData.length; ++i)
			{
				if (Double.isNaN(columnData[i]))
				{
					++nan;
					continue;
				}
				if (columnData[i] < min)
					min = columnData[i];
				if (columnData[i] > max)
					max = columnData[i];
				sum += columnData[i];
			}
		}
	}

	// compute the standard deviation of values in a double array
	private double standardDeviation(double[] d)
	{
		double m = mean(d);
		double t = 0.0;
		int n = 0;
		for (int j = 0; j < d.length && !Double.isNaN(d[j]); j++)
		{
			t += (m - d[j]) * (m - d[j]);
			++n;
		}
		return Math.sqrt(t / (n - 1));
	}

	// compute the mean of values in a double array
	private double mean(double d[])
	{
		double mean = 0.0f;
		int n = 0;
		for (int j = 0; j < d.length && !Double.isNaN(d[j]); j++)
		{
			mean += d[j];
			++n;
		}
		return mean / n;
	}

	/*
	 * The traverse method recursively scans the file system, starting at the passed file (which must be a directory).
	 * When a directory is found that contains at least one sd2 file, the name of the directory is added to the
	 * directoryList and the name of the sd2 file is added to a file list.
	 *
	 * Adapted from... http://www.heimetli.ch/directory/RecursiveTraversal.html
	 */
	public void traverse(String appName, File file, boolean recurse)
	{
		// check if file is a directory
		if (file.isDirectory())
		{
			// get a list of all the entries in the directory
			String entryArray[] = file.list();
			String pathName = file.getPath();

			// ensure that the list is not null
			if (entryArray != null)
			{
				// scan the list to determine if there is at least one sd2 file. If so, add the file to the list
				for (String entry : entryArray)
					if (entry.startsWith(appName) && entry.endsWith(".sd1"))
						sd1Files.add(new File(pathName + File.separator + entry));

				// loop over all the entries, and recursively continue into sub-directories
				if (recurse)
				{
					for (String entry : entryArray)
					{
						// recursively enter sub-directories
						File f = new File(file, entry);
						if (f.isDirectory())
							traverse(appName, f, recurse);
					}
				}
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent arg0)
	{
		includeSubFolders = includeSubDirectoriesCheckBox.isSelected();
	}
}
