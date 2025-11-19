import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * Setup - generalized setup class to create setup dialogs for experimental applications.
 * <p>
 * 
 * @author Scott MacKenzie, 2014-2025
 *
 */

public class Setup extends JDialog implements SetupItem.SetupItemListener, ActionListener
{
	// the following avoids a "warning" with Java 1.5.0 complier (?)
	static final long serialVersionUID = 42L;

	Configuration c, cSave;
	String appName;
	JLabel banner;

	SetupItem[] items;
	SetupItemInfo[] itemInfo;

	JButton okButton;
	JButton saveButton;
	JButton resetButton;
	JButton exitButton;
	boolean ok;

	JFileChooser fc;
	JScrollPane scrollPane;

	final Font BIG = new Font("sansserif", Font.PLAIN, 12); // see also "BIG" in SetupItem class
	final Font BIGGER = new Font("sansserif", Font.PLAIN, 14);
	final Font BIGGEST = new Font("sansserif", Font.PLAIN, 18);

	Setup(Frame owner, Configuration cArg, String appNameArg, SetupItemInfo[] itemInfoArg)
	{
		super(owner, "Configure " + appNameArg, true);

		c = cArg;
		appName = appNameArg;
		itemInfo = itemInfoArg;
		cSave = new Configuration(appName, c.toArray());

		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		fc = new JFileChooser(new File("."));
		scrollPane = new JScrollPane();

		banner = new JLabel("Setup", SwingConstants.CENTER);
		banner.setFont(BIGGEST);
		banner.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

		/*
		 * Each entry in the setup dialog is defined by a SetupItem. Here, we instantiate a SetupItem array and populate
		 * it with SetupItem instances. Each instance is constructed using information in the SetupItemInfo array which
		 * was passed in to the Setup constructor.
		 */
		items = new SetupItem[itemInfo.length];
		for (int i = 0; i < itemInfo.length; ++i)
		{
			items[i] = new SetupItem(itemInfo[i].itemType, itemInfo[i].itemLabel, itemInfo[i].itemValues);
			items[i].addSetupItemListener(this);
		}

		okButton = new JButton("OK");
		okButton.setFont(BIGGER);
		okButton.addActionListener(this);

		saveButton = new JButton("Save");
		saveButton.setFont(BIGGER);
		saveButton.addActionListener(this);

		resetButton = new JButton("Reset");
		resetButton.setFont(BIGGER);
		resetButton.addActionListener(this);

		exitButton = new JButton("Exit");
		exitButton.setFont(BIGGER);
		exitButton.addActionListener(this);

		okButton.setPreferredSize(resetButton.getPreferredSize());
		saveButton.setPreferredSize(resetButton.getPreferredSize());
		exitButton.setPreferredSize(resetButton.getPreferredSize());

		setDefaults();

		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());

		// add items to parameter panel and determine/set the item widths for a consistent look
		int preferredLeftWidth = 0;
		int preferredRightWidth = 0;
		JPanel paramPanel = new JPanel();
		paramPanel.setLayout(new BoxLayout(paramPanel, BoxLayout.Y_AXIS));
		for (int i = 0; i < items.length; ++i)
		{
			paramPanel.add(items[i]);
			if (items[i].getPreferredLeftWidth() > preferredLeftWidth)
				preferredLeftWidth = items[i].getPreferredLeftWidth();

			if (items[i].getPreferredRightWidth() > preferredRightWidth)
				preferredRightWidth = items[i].getPreferredRightWidth();
		}

		for (int i = 0; i < items.length; ++i)
			items[i].setPreferredItemWidth(preferredLeftWidth, preferredRightWidth);

		paramPanel.setBorder(new TitledBorder(new EtchedBorder(), "Parameters"));

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		buttonPanel.add(saveButton);
		buttonPanel.add(resetButton);
		buttonPanel.add(exitButton);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

		top.add("North", banner);
		top.add("Center", paramPanel);
		top.add("South", buttonPanel);
		top.add("West", new JLabel("            "));
		top.add("East", new JLabel("            "));

		int ownerWidth = owner.getWidth();
		int ownerHeight = owner.getHeight();
		int setupPanelWidth = top.getPreferredSize().width;
		setBounds(ownerWidth / 2 - setupPanelWidth / 2, 20, setupPanelWidth + 30, ownerHeight - 40);

		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		scrollPane.setViewportView(top);

		this.getRootPane().setDefaultButton(okButton);
		okButton.requestFocus();		
	}

	public void actionPerformed(ActionEvent ae)
	{
		Object source = ae.getSource();

		if (source == resetButton)		
			setDefaults();			

		else if (source == saveButton)
			saveToFile();

		else if (source == okButton)
		{
			ok = true;
			this.setVisible(false);
		}

		else if (source == exitButton)
		{
			ok = false;
			this.setVisible(false);
			return;
		}
	}

	public void onSetupItemChanged(SetupEvent e)
	{
		int idx;
		String value = null;

		// determine which SetupItem is the source of this event
		for (idx = 0; idx < items.length; ++idx)
		{
			Object o1 = items[idx].getParameterObject();
			Object o2 = e.getSource();
			if (o1 == o2)
				break;
		} // idx is the index in the items array of the SetupItem that triggered this event

		// if no match was found, return now
		if (idx >= items.length)
			return;

		// use idx to
		else if (items[idx].type == SetupItem.CHECK_BOX)
			value = Boolean.toString(items[idx].setupCheckBox.isSelected());

		else if (items[idx].type == SetupItem.COMBO_BOX)
			value = (String) items[idx].setupComboBox.getSelectedItem();

		else if (items[idx].type == SetupItem.TEXT_FIELD)
			value = items[idx].setupTextField.getText();

		else if (items[idx].type == SetupItem.RADIO_BUTTON)
		{
			for (int i = 0; i < items[idx].setupRadioButtons.length; ++i)
			{
				if (items[idx].setupRadioButtons[i].isSelected())
				{
					value = items[idx].setupRadioButtons[i].getText();
					break;
				}
			}
		}

		else if (items[idx].type == SetupItem.FILE_CHOOSER)
		{
			if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			{
				value = fc.getSelectedFile().getName();
			}
		}

		else if (items[idx].type == SetupItem.COLOR_BUTTONS)
		{
			//System.out.println("Got here!");

			int buttonClicked = Integer.parseInt(((JButton) e.getSource()).getActionCommand());

			// get current colors from button backgrounds
			int numberOfButtons = items[idx].value[0].split(",").length;
			String[] colors = new String[numberOfButtons];
			for (int i = 0; i < numberOfButtons; ++i)
				colors[i] = String.format("0x%06X",
						0x00ffffff & items[idx].setupColorButtons[i].getBackground().getRGB());
//			for (int i = 0; i < numberOfButtons; ++i)
//				colors[i] = String.format("0x%06X",
//						0x00ffffff & items[idx].colorButtonBorders[i].getLineColor().getRGB());

			// popup a color chooser to see what new color is desired
			Color tmp = JColorChooser.showDialog(this, "Choose color", new Color(Integer.decode(colors[buttonClicked])));			
			if (tmp != null)
			{
				colors[buttonClicked] = String.format("0x%06X", 0x00ffffff & tmp.getRGB());
				
				((JButton) e.getSource()).setBackground(tmp);
				
//				items[idx].colorButtonBorders[buttonClicked] = new LineBorder(tmp, 12);
//				items[idx].setupColorButtons[buttonClicked].setBorder(new LineBorder(tmp, 12));
			}

			// recreate configuration string argument, inserting new color
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < colors.length; ++i)
			{
				sb.append(colors[i]);
				if (i < colors.length - 1) // append , symbol, but not at the end
					sb.append(", ");
			}
			value = sb.toString();
		}

		c.setConfigurationParameter(idx, value);		
	}

	private void setDefaults()
	{
		for (int i = 0; i < items.length; ++i)
			items[i].setInitialState(items[i].type, cSave.getConfigurationParameter(i));
	}

	private void saveToFile()
	{
		PrintWriter out;
		try
		{
			out = new PrintWriter(new BufferedWriter(new FileWriter(appName + ".cfg")));
			out.print(c.toString());
			out.flush();
			out.close();
		} catch (IOException ioe)
		{
			JOptionPane.showMessageDialog(null, "Unable to save config to " + appName + ".cfg" + System.lineSeparator()
					+ "Please ensure the file is writable.", "I/O Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public boolean showSetup(Frame f)
	{
		ok = false;
		this.setLocationRelativeTo(f);
		this.setVisible(true);	
		return ok;
	}
	
} // end Setup

// ---------------------
// S E T U P _ E V E N T
// ---------------------

class SetupEvent
{
	Object source;

	SetupEvent(Object sourceArg)
	{
		source = sourceArg;
	}

	Object getSource()
	{
		return source;
	}
}

// -----------------------------
// S E T U P _ I T E M _ I N F O
// -----------------------------

class SetupItemInfo
{
	int itemType;
	String itemLabel;
	String[] itemValues;

	SetupItemInfo(int itemTypeArg, String itemLabelArg, String[] itemValuesArg)
	{
		itemType = itemTypeArg;
		itemLabel = itemLabelArg;
		itemValues = itemValuesArg;
	}
}

// -------------------
// S E T U P _ I T E M
// -------------------

class SetupItem extends JPanel implements ActionListener, ItemListener, FocusListener
{
	// the following avoids a "warning" with Java 1.5.0 complier
	private static final long serialVersionUID = 1L;

	final static int COMBO_BOX = 100;
	final static int TEXT_FIELD = 200;
	final static int CHECK_BOX = 300;
	final static int RADIO_BUTTON = 400;
	final static int FILE_CHOOSER = 500;
	final static int COLOR_BUTTONS = 600;

	final Font BIG = new Font("sansserif", Font.PLAIN, 14); // see also "BIG" in Setup class
	final Font SMALL = new Font("sansserif", Font.PLAIN, 10); // see also "BIG" in Setup class

	JLabel comboBoxLabel;
	JComboBox<String> setupComboBox;

	JLabel textFieldLabel;
	JTextField setupTextField;

	JCheckBox setupCheckBox;
	JLabel checkBoxLabel;

	JRadioButton[] setupRadioButtons;
	JPanel setupRadioButtonPanel;

	JLabel fileChooserLabel;
	JButton fileChooserBrowseButton;

	JButton[] setupColorButtons;
	//LineBorder[] colorButtonBorders;
	JPanel setupColorButtonPanel;

	int type;
	String label;
	String[] value;
	int preferredLeftWidth, preferredRightWidth;

	SetupItem(int typeArg, String labelArg, String[] valueArg)
	{
		type = typeArg;
		label = labelArg;
		value = valueArg;

		if (type == COMBO_BOX)
		{
			comboBoxLabel = new JLabel(label);
			comboBoxLabel.setFont(BIG);
			comboBoxLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			preferredLeftWidth = comboBoxLabel.getPreferredSize().width;
			this.add(comboBoxLabel);

			setupComboBox = new JComboBox<String>(value);
			setupComboBox.setFont(BIG);
			setupComboBox.addActionListener(this);
			preferredRightWidth = setupComboBox.getPreferredSize().width;
			this.add(setupComboBox);
		}

		else if (type == TEXT_FIELD)
		{
			textFieldLabel = new JLabel(label);
			textFieldLabel.setFont(BIG);
			textFieldLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			preferredLeftWidth = textFieldLabel.getPreferredSize().width;
			this.add(textFieldLabel);

			setupTextField = new JTextField(value[0]);
			setupTextField.setFont(BIG);
			setupTextField.addActionListener(this);
			setupTextField.addFocusListener(this);
			preferredRightWidth = setupTextField.getPreferredSize().width;
			this.add(setupTextField);
		}

		else if (type == CHECK_BOX)
		{
			setupCheckBox = new JCheckBox();
			setupCheckBox.setFont(BIG);
			setupCheckBox.addItemListener(this);
			setupCheckBox.setHorizontalAlignment(SwingConstants.RIGHT);
			preferredLeftWidth = setupCheckBox.getPreferredSize().width;
			this.add(setupCheckBox);

			checkBoxLabel = new JLabel(label);
			checkBoxLabel.setFont(BIG);
			checkBoxLabel.setHorizontalAlignment(SwingConstants.LEFT);
			preferredRightWidth = checkBoxLabel.getPreferredSize().width;
			this.add(checkBoxLabel);
		}

		else if (type == RADIO_BUTTON)
		{
			setupRadioButtonPanel = new JPanel();
			setupRadioButtonPanel.setBorder(new TitledBorder(new EtchedBorder(), label));
			setupRadioButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

			ButtonGroup bg = new ButtonGroup();

			setupRadioButtons = new JRadioButton[value.length];
			for (int i = 0; i < setupRadioButtons.length; ++i)
			{
				setupRadioButtons[i] = new JRadioButton(value[i]);
				setupRadioButtons[i].setFont(BIG);
				setupRadioButtons[i].addActionListener(this);
				setupRadioButtonPanel.add(setupRadioButtons[i]);
				bg.add(setupRadioButtons[i]);
			}

			setupRadioButtons[0].setSelected(true);
			this.add(setupRadioButtonPanel);
		}

		else if (type == FILE_CHOOSER)
		{
			fileChooserLabel = new JLabel(label);
			fileChooserLabel.setFont(BIG);
			fileChooserLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			preferredLeftWidth = fileChooserLabel.getPreferredSize().width;
			this.add(fileChooserLabel);

			fileChooserBrowseButton = new JButton("Browse...");
			fileChooserBrowseButton.addActionListener(this);
			fileChooserBrowseButton.setFont(BIG);
			preferredRightWidth = fileChooserBrowseButton.getPreferredSize().width;
			this.add(fileChooserBrowseButton);
		}

		else if (type == COLOR_BUTTONS)
		{
			setupColorButtonPanel = new JPanel();
			setupColorButtonPanel.setBorder(new TitledBorder(new EtchedBorder(), "Colors"));
			setupColorButtonPanel.setLayout(new GridLayout(1, 0)); // horizontal

			//System.out.println("label=" + label);
			//System.out.println("value[0]=" + value[0]);
			String[] colorButtonLabelStrings = label.split(",");
			String[] colorButtonColorStrings = value[0].split(",");
			if (colorButtonLabelStrings.length != colorButtonColorStrings.length)
			{
				System.out.println("Oops! Number of button labels must equal number of button colors!");
				System.exit(0);
			}
			
//			for (int i = 0; i < colorButtonColorStrings.length; ++i)
//			{
//				String tmp = colorButtonColorStrings[i].trim();
//				System.out.printf("i=%d, c=%s, d=%d\n", i, tmp, Integer.decode(tmp));
//			}

			JPanel[] buttonAndLabel = new JPanel[colorButtonLabelStrings.length];
			JLabel[] colorButtonLabels = new JLabel[colorButtonLabelStrings.length];
			setupColorButtons = new JButton[colorButtonColorStrings.length];
			//colorButtonBorders = new LineBorder[colorButtonColorStrings.length];

			for (int i = 0; i < setupColorButtons.length; ++i)
			{
				setupColorButtons[i] = new JButton(" "); // need at least one character
				//setupColorButtons[i].setUI(new ColorButtonUI());
				
				//setupColorButtons[i].setOpaque(true);

				setupColorButtons[i].addActionListener(this);
				setupColorButtons[i].setBackground(new Color(Integer.decode(colorButtonColorStrings[i].trim())));
				//setupColorButtons[i].setBackground(Color.RED);
				setupColorButtons[i].setActionCommand("" + i);
				colorButtonLabels[i] = new JLabel(colorButtonLabelStrings[i].trim());
				colorButtonLabels[i].setFont(SMALL);
				
				Color tmp = new Color(Integer.decode(colorButtonColorStrings[i].trim()));
				//colorButtonBorders[i] = new LineBorder(tmp, 12);
				//setupColorButtons[i].setBorder(colorButtonBorders[i]);
				setupColorButtons[i].setBackground(tmp);
				
				JPanel jp1 = new JPanel();
				jp1.setLayout(new FlowLayout(FlowLayout.LEFT)); // label is above button, left aligned
				jp1.add(colorButtonLabels[i]);

				JPanel jp2 = new JPanel();
				jp2.setLayout(new GridLayout(1, 0)); // so button fills available space
				jp2.add(setupColorButtons[i]);

				buttonAndLabel[i] = new JPanel();
				buttonAndLabel[i].setLayout(new BoxLayout(buttonAndLabel[i], BoxLayout.Y_AXIS));
				buttonAndLabel[i].add(jp1);
				buttonAndLabel[i].add(jp2);
				buttonAndLabel[i].setBorder(new EmptyBorder(0, 5, 5, 5));

				setupColorButtonPanel.add(buttonAndLabel[i]);
				this.add(setupColorButtonPanel);
			}
		}
	}

	public int getPreferredLeftWidth()
	{
		return preferredLeftWidth;
	}

	public int getPreferredRightWidth()
	{
		return preferredRightWidth;
	}

	public void setPreferredItemWidth(int left, int right)
	{
		switch (this.type)
		{
			case SetupItem.CHECK_BOX:
				setupCheckBox.setPreferredSize(new Dimension(left, setupCheckBox.getPreferredSize().height));
				checkBoxLabel.setPreferredSize(new Dimension(right, checkBoxLabel.getPreferredSize().height));
				break;
			case SetupItem.COMBO_BOX:
				comboBoxLabel.setPreferredSize(new Dimension(left, comboBoxLabel.getPreferredSize().height));
				setupComboBox.setPreferredSize(new Dimension(right, setupComboBox.getPreferredSize().height));
				break;

			case SetupItem.TEXT_FIELD:
				textFieldLabel.setPreferredSize(new Dimension(left, textFieldLabel.getPreferredSize().height));
				setupTextField.setPreferredSize(new Dimension(right, setupTextField.getPreferredSize().height));
				break;

			case SetupItem.RADIO_BUTTON:
				setupRadioButtonPanel
						.setPreferredSize(new Dimension(left + right, setupRadioButtonPanel.getPreferredSize().height));
				break;

			case SetupItem.FILE_CHOOSER:
				fileChooserLabel.setPreferredSize(new Dimension(left, fileChooserLabel.getPreferredSize().height));
				fileChooserBrowseButton
						.setPreferredSize(new Dimension(right, fileChooserBrowseButton.getPreferredSize().height));

			case SetupItem.COLOR_BUTTONS:
				setupColorButtonPanel
						.setPreferredSize(new Dimension(left + right, setupColorButtonPanel.getPreferredSize().height));

		}
	}

	public static interface SetupItemListener
	{
		public abstract void onSetupItemChanged(SetupEvent se);
	}

	SetupItemListener setupItemListener;

	public void addSetupItemListener(SetupItemListener setupItemListenerArg)
	{
		setupItemListener = setupItemListenerArg;
	}

	@Override
	public void focusGained(FocusEvent arg0)
	{
	}

	@Override
	public void focusLost(FocusEvent arg0)
	{
		setupItemListener.onSetupItemChanged(new SetupEvent(this.getParameterObject()));
	}

	@Override
	public void itemStateChanged(ItemEvent arg0)
	{
		setupItemListener.onSetupItemChanged(new SetupEvent(this.getParameterObject()));
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		setupItemListener.onSetupItemChanged(new SetupEvent(this.getParameterObject()));
	}

	public void setInitialState(int typeArg, String newValueArg)
	{
		int type = typeArg;
		String newValue = newValueArg;

		if (type == SetupItem.CHECK_BOX)
		{
			setupCheckBox.setSelected(Boolean.valueOf(newValue));
		}

		else if (type == SetupItem.COMBO_BOX)
		{
			for (int i = 0; i < value.length; ++i)
			{
				if (value[i].equals(newValue))
				{
					setupComboBox.setSelectedIndex(i);
					break;
				}
			}
		}

		else if (type == SetupItem.TEXT_FIELD)
		{
			setupTextField.setText(newValue);
		}

		else if (type == SetupItem.RADIO_BUTTON)
		{
			for (int i = 0; i < setupRadioButtons.length; ++i)
			{
				if (setupRadioButtons[i].getText().equals(newValue))
				{
					setupRadioButtons[i].setSelected(true);
					break;
				}
			}
		}

		else if (type == SetupItem.COLOR_BUTTONS)
		{
			//System.out.println("Got here!");
			String[] s = newValue.split(",");
			for (int i = 0; i < this.setupColorButtons.length; ++i)
			{
				this.setupColorButtons[i].setBackground(new Color(Integer.decode(s[i].trim())));
				//this.setupColorButtons[i].setOpaque(true);
				//this.setupColorButtons[i].setBackground(Color.BLUE);
				
				//Color tmp = new Color(Integer.decode(s[i].trim()));
				//System.out.printf("i=%d, c=0x%08x\n", i, tmp.getRGB());
				//colorButtonBorders[i] = new LineBorder(tmp, 12);
				//setupColorButtons[i].setBorder(colorButtonBorders[i]);
				//setupColorButtons[i].setBackground(tmp);
			}
		}
	}

	public Object getParameterObject()
	{
		Object o = null;
		if (type == SetupItem.CHECK_BOX)
			o = setupCheckBox;
		else if (type == SetupItem.COMBO_BOX)
			o = setupComboBox;
		else if (type == SetupItem.TEXT_FIELD)
			o = setupTextField;
		else if (type == SetupItem.RADIO_BUTTON)
		{
			for (int i = 0; i < setupRadioButtons.length; ++i)
				if (setupRadioButtons[i].isSelected())
				{
					o = setupRadioButtons[i];
					break;
				}
		} else if (type == SetupItem.FILE_CHOOSER)
			o = fileChooserBrowseButton;
		
		else if (type == SetupItem.COLOR_BUTTONS)
		{
			for (int i = 0; i < this.setupColorButtons.length; ++i)
				if (setupColorButtons[i].hasFocus())
				{
					o = this.setupColorButtons[i];
					break;
				}
		}

		return o;
	}	
} // end SetupItem

// -------------------------
// C O N F I G U R A T I O N
// -------------------------

class Configuration
{
	String appName;
	String[] parameters;

	Configuration(String appNameArg, String[] parametersArg)
	{
		appName = appNameArg;
		parameters = new String[parametersArg.length];
		System.arraycopy(parametersArg, 0, parameters, 0, parametersArg.length);
	}

	// ----------
	// set methods
	// -----------

	void setConfigurationParameter(int idx, String par)
	{
		parameters[idx] = par;
	}

	// -----------
	// get methods
	// -----------

	String getConfigurationParameter(int idx)
	{
		String s = null;
		try
		{
			s = parameters[idx];
		} catch (ArrayIndexOutOfBoundsException e)
		{
			GoFitts.showError("Oops! Wrong number of parameters in cfg file (idx=" + idx + ")");
		}
		return s;
	}

	String[] toArray()
	{
		return parameters;
	}

	public static Configuration readConfigurationData(String appNameArg, String[] defaultParametersArg,	String[] otherFilesArg)
	{
		String appName = appNameArg;
		String[] defaultParameters = defaultParametersArg;
		String[] otherFiles = otherFilesArg; // other files to copy (may be null)

		Scanner inFile = null;
		String fileName = appName + ".cfg"; // name of the configuration file
		String line = "";

		// Added for executable Jar:
		File cfgFile = new File(fileName);

		if (!cfgFile.exists())
		{
			String msg = "The " + fileName + " file was not found. Would you" + System.lineSeparator()
					+ "like a default configuration file to be created for you?";
			int resp = JOptionPane.showConfirmDialog(null, msg, "Missing Configuration File", JOptionPane.YES_NO_OPTION);
			if (resp == JOptionPane.YES_OPTION)
			{
				PrintWriter out;
				try
				{
					out = new PrintWriter(new BufferedWriter(new FileWriter(appName + ".cfg")));
					out.print((new Configuration(appName, defaultParameters)).toString());
					out.flush();
					out.close();
				} catch (IOException ioe)
				{
					// March 19, 2021 - added dump of ioe
					showError("Unable to save config to " + appName + ".cfg" + System.lineSeparator()
							+ "Please ensure the file/folder is writable." + System.lineSeparator()
							+ "IOException: " + ioe.toString());
					return null;
				}
			} else
				return null;			
			
			// copy image files -- needed when executing from .jar file for the first time
			for (int i = 0; otherFiles != null && i < otherFiles.length; ++i)
			{
				if (!(new File(otherFiles[i]).exists()))
					copyFile(appName, otherFiles[i]);
			}			
		}

		// with the above preamble (maybe), we can now read the .cfg file
		try
		{
			inFile = new Scanner(new FileReader(cfgFile));
		} catch (FileNotFoundException e)
		{
			showError("Configuration file not found: " + fileName);
			return null;
		}

		// ----------------------------------
		// read lines from configuration file
		// ----------------------------------

		ArrayList<String> tmp = new ArrayList<String>();

		// skip over blank lines and lines beginning with "#", then read the next parameter

		while (inFile.hasNextLine())
		{
			line = inFile.nextLine();
			if (line.length() == 0 || line.charAt(0) == '#')
				continue;
			else
				tmp.add(line);

		} // finished reading configuration file
		inFile.close();

		String[] parameters = new String[tmp.size()];
		parameters = tmp.toArray(parameters);
		Configuration c = new Configuration(appName, parameters);
		return c;
	}

	private static void showError(String msg)
	{
		JOptionPane.showMessageDialog(null, msg, "I/O Error", JOptionPane.ERROR_MESSAGE);
	}

	// copy a resource file (in the JAR) to the working directory
	private static void copyFile(String appNameArg, String fileNameArg)
	{
		String appName = appNameArg;
		String fileName = fileNameArg;

		// use a 'generic' approach to copy a resource file
		Class<?> appClass = null;
		try
		{
			appClass = Class.forName(appName);
		} catch (ClassNotFoundException e)
		{
			showError("ClassNotFound error copying file: " + fileName);
			System.exit(1);
		}	

		// --------------------------------------------------------------------------------------------
		// new code to copy *any* file (including binary image files) - S.M. June 6, 2018
		// gleaned from https://examples.javacodegeeks.com/core-java/io/copy-binary-file-with-streams/
		// --------------------------------------------------------------------------------------------

		InputStream input = null;
		OutputStream output = null;
	
		try
		{		
			input = appClass.getResourceAsStream(fileName);		
			
			File imagesDir = new File("images");
			if (!imagesDir.exists()) // create the 'images' directory, popup error message if problem
			{
				System.out.println("Got here! (if)");
				if (!imagesDir.mkdir())	
				{
					showError("Can't create 'images' directory; check permissions");
					System.exit(1);
				}
			}			
		
//			output = new FileOutputStream(fileName);
			output = new FileOutputStream("images" + File.separator + fileName); // 12/3/2025

			byte[] buffer = new byte[1024];
			int numberOfBytes = 0;

			// read bytes from source file and write to destination file
			while ((numberOfBytes = input.read(buffer)) != -1)
				output.write(buffer, 0, numberOfBytes);
	
		} catch (FileNotFoundException e)
		{
			showError("FileNotFound error copying file: " + fileName);
			System.exit(1);
			
		}
		catch (IOException e)
		{
			showError("IO error copying file: " + fileName);
			System.exit(1);
			
		}
		finally
		{
			// close the streams
			try
			{
				if (input != null)
					input.close();			

				if (output != null)
					output.close();
			}

			catch (IOException e)
			{
				showError("IO error while closing stream!");
				System.exit(1);
			}
		}
	}

	/*
	 * Return the configuration parameters as a string. The string is written to the .cfg file. The parameters passed in
	 * to this method are only used the first time the method is called (i.e., when launching the application for the
	 * first time). Normally, this method is called when the user clicks the Save button in the setup dialog's UI. In
	 * this case, the current values of the setup parameters are returned in the configuration string.
	 */
	public String toString()
	{
		String title = "Configuration parameters for " + appName + System.lineSeparator();

		StringBuilder sb = new StringBuilder();
		sb.append("# ");
		for (int i = 0; i < title.length() - 1; ++i)
			sb.append("="); // ================ (as needed)
		sb.append(System.lineSeparator());
		sb.append("# " + title);
		sb.append("# ");
		for (int i = 0; i < title.length() - 1; ++i)
			sb.append("="); // ================ (as needed)
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());

		for (int i = 0; i < parameters.length; ++i)
		{
			sb.append("# -----");
			sb.append(System.lineSeparator());
			sb.append("# Parameter #" + (i + 1) + " (as per setup dialog)");
			sb.append(System.lineSeparator());
			sb.append(parameters[i]);
			sb.append(System.lineSeparator());
			sb.append(System.lineSeparator());
		}
		sb.append("# --- end ---");
		sb.append(System.lineSeparator());
		return sb.toString();
	}
} // end Configuration
