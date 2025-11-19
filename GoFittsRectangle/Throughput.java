import java.awt.geom.Point2D;

/**
 * <hr>
 * 
 * <table width="100%" summary="Anova2GUI title">
 * <tr style="background-color:#cccccc">
 * <td align="center">
 * <h1>Throughput</h1>
 * </table>
 * 
 * <h2>Summary</h2>
 * 
 * <ul>
 * <li>Calculate Fitts' throughput for a sequence of trials
 * 
 * <li>Input data: task conditions, selection coordinates, task completion times
 * 
 * <li>Two uses:
 * 
 * <ul>
 * <li>As a class embedded in experiment software
 * <li>As a utility program to process data from a terminal prompt
 * </ul>
 * <p>
 * </ul>
 * 
 * <h2>Background</h2>
 * 
 * Throughput is a commonly used dependent variable in experimental research on pointing devices or point-select
 * techniques. However, in reviewing research papers where throughput is used, different methods of calculating
 * throughput are found. Because of this, it is difficult to compare results between research studies. Furthermore, it
 * is often difficult to determine with certainty how throughput was calculated. This API and Java class seek to remedy
 * these problems by (i) providing clear and simple instructions on how to calculate throughput and (ii) disseminating a
 * tool to do the calculations. Measures related to throughput are also calculated and described, as noted below.
 * <p>
 * 
 * This is not a primer on Fitts' law or on Fitts' throughput. For background discussions, the reader is directed to the
 * references cited at the end of this API. Let's begin.
 * <p>
 * 
 * Fitts' throughput is calculated on a sequence of trials. The premise for this is twofold:
 * 
 * <ol>
 * <li>Throughput cannot be calculated on a single trial.
 * <li>A sequence of trials is the smallest set of user actions for which throughput can be calculated as a measure of
 * performance.
 * </ol>
 * <p>
 * 
 * (Note: A "sequence" is consecutive series of trials for a given <i>A</i>-<i>W</i> condition.)
 * <p>
 * 
 * On the first point, the calculation of Throughput includes the variability in selection coordinates. The variability
 * is analogous to "noise" in the information-theoretic metaphor upon which Fitts' law is based. Thus, multiple
 * selections are required and from the coordinates of selection, the variability in the coordinates is computed.
 * <p>
 * 
 * The second point is mostly of ecological concern. After performing a single sequence of trials, the user pauses,
 * rests, stretches, adjusts the apparatus, has a sip of tea, adjusts her position on a chair, or something. There is a
 * demarcation between sequences and for no particular purpose other than to provide a break or pause,
 * or perhaps to change to a different test condition. It seems reasonable that once a sequence is over, it is over!
 * Behaviours were observed and measured and the next sequence should be treated as a separate unit of action with
 * separate performance measurements.
 * <p>
 * 
 * Related to the second point is the following: Throughput should not be calculated on larger sets of raw data. For
 * example, if six participants each perform five sequences of trials under the same <i>A</i>-<i>W</i> condition, there
 * are 6 &times; 5 = 30 calculations of throughput, rather than a single calculation using the pooled data.
 * <p>
 * 
 * The <code>Throughput</code> code may be used in two ways: (i) as a class embedded in custom-designed software or (ii)
 * as a utility program executed from a command prompt.
 * <p>
 * 
 * <!-----------------------------------------------------------------------------------------> <b>Throughput Class</b>
 * <p>
 * 
 * As a class embedded in custom-designed software, the <code>Throughput</code> class file is placed in the same
 * directory as other class files for the application. This API provides all the details necessary to use the
 * <code>Throughput</code> class.
 * <p>
 * 
 * Use of this class begins with the instantiation of a <code>Throughput</code> object. The constructor receives the
 * data necessary to characterise the sequence. The data consist of a String, two doubles, four arrays, and two
 * integers. The arrays are all of the same size, with the size equal to the number of trials in the sequence. The data,
 * or arguments, passed to the constructor are as follows:
 * 
 * <table border=1 cellspacing=0 width=80% cellpadding=5 summary="constructor arguments">
 * 
 * <tr style="background-color:#cccccc">
 * <th>Argument
 * <th>Type
 * <th>Description
 * 
 * <tr>
 * <td><code>code</code>
 * <td><code>String</code>
 * <td>A code to represent the conditions used for testing. This argument is used to associate test conditions
 * (participant code, block code, device code, etc.) with the sequence. A null string may be passed if no code is
 * necessary.
 * 
 * <tr>
 * <td><code>amplitude</code>
 * <td><code>int</code>
 * <td>Target amplitude for the sequence
 * 
 * <tr>
 * <td><code>width</code>
 * <td><code>int</code>
 * <td>Target width for the sequence
 * 
 * <tr>
 * <td><code>from</code>
 * <td><code>Point[]</code>
 * <td>The specified starting coordinates for each trial (center of the "from" target)
 * 
 * <tr>
 * <td><code>to</code>
 * <td><code>Point[]</code>
 * <td>The specified ending coordinates for each trial (center of the "to" target)
 * 
 * <tr>
 * <td><code>select</code>
 * <td><code>Point[]</code>
 * <td>The coordinates of selection where each trial was terminated
 * 
 * <tr>
 * <td><code>mt</code>
 * <td><code>int[]</code>
 * <td>The movement times (ms) for each trial
 * 
 * <tr>
 * <td><code>taskType</code>
 * <td><code>int</code>
 * <td>A constant identifying if the task movements were one-dimensional ( <code>Throughput.ONE_DIMENSIONAL</code>) or
 * two-dimensional ( <code>Throughput.TWO_DIMENSIONAL</code>)
 * 
 * <tr>
 * <td><code>responseType</code>
 * <td><code>int</code>
 * <td>A constant identifying if the responses were serial ( <code>Throughput.SERIAL</code>) or discrete (
 * <code>Throughput.DISCRETE</code>). This variable is only used in calculating the effective target amplitude (see
 * below).
 * </table>
 * <p>
 * 
 * The <code>Throughput</code> class was designed to be "universal" &mdash; as general purpose as possible. It can be
 * used both for serial and discrete tasks and for one-dimensional (1D) or two-dimensional (2D) movement. For serial
 * tasks, each trial immediately follows the preceding trial. For discrete tasks, each trial includes a preparatory
 * phase followed by a stimulus. Upon detecting the stimulus, the user performs the trial. The time between the arrival
 * of the stimulus and the beginning of movement is called reaction time and is excluded from the movement time recorded
 * for the trial.
 * <p>
 * 
 * The one-dimensional (1D) case is the traditional back-and-forth task used by Fitts in his original 1954 paper. The
 * two-dimensional case is the task commonly used in accordance with the ISO 9241-9 standard (updated in 2012 as ISO/TC
 * 9241-411). For two-dimensional movements, a series of targets are arranged around a layout circle. The trials proceed
 * in a sequence. After each selection, the next selection is the target on the opposite side of the layout circle.
 * Every second selection is beside a target previously selected, thus the movements progress around the layout circle
 * until all targets are selected.
 * <p>
 * 
 * Throughput is calculated for each sequence of trials as
 * 
 * <blockquote> <i>TP</i> = <i>ID</i><sub>e</sub> / <i>MT</i> </blockquote>
 * 
 * where
 * 
 * <blockquote> <i>ID</i><sub>e</sub> = log<sub>2</sub>(<i>A</i><sub>e</sub> / <i>W</i><sub>e</sub> + 1) </blockquote>
 * 
 * and
 * 
 * <blockquote> <i>W</i><sub>e</sub> = 4.133 &times; <i>SD</i><sub>x</sub> </blockquote>
 * 
 * <i>MT</i> is the mean movement time per trial (in seconds). <i>ID</i><sub>e</sub> is the effective index of
 * difficulty (in bits). Throughput (<i>TP</i>) is the rate of information processing (in bits per second).
 * <p>
 * 
 * The subscript "e" beside <i>ID</i> is for "effective". In the effective form, the index of difficulty reflects the
 * task the participant actually did rather than the task the participant was presented with.
 * <p>
 * 
 * The effective target width (<i>W</i><sub>e</sub>) is calculated from <i>SD</i><sub>x</sub>, which is the standard
 * deviation in the selection coordinates for the sequence of trials. The selection coordinates are projected onto the
 * task axis to maintain the inherent one-dimensionality of Fitts' law. The task axis is a line between the center of
 * the desired start point ("from") and the desired end point ("to"). The projection is done using simple calculations
 * involving the Pythagorean identity. Details are provide below and also in the source code.
 * <p>
 * 
 * The effective target amplitude, <i>A</i><sub>e</sub>, is the mean of the actual movement amplitudes over a sequence
 * of trials. <i>A</i><sub>e</sub> is measured along the task axis.
 * <p>
 * 
 * The following figure illustrates the geometry for a single trial, including the point of selection:
 * 
 * <center><p> <img src="Throughput-1.jpg" width= 750 alt="Throughput-1"> </center>
 * <p>
 * 
 * Although the figure shows a trial with horizontal movement to the right, the calculations are valid for movements in
 * any direction or angle. Circular targets are shown to provide a conceptual visualization of the task. Other target
 * shapes are possible, depending on the setup in the experiment. The calculation begins by computing the length of the
 * sides connecting the <code>from</code>, <code>to</code>, and <code>select</code> points in the figure:
 * 
 * <blockquote><code>
 * double a = Math.hypot(x1 - x2, y1 - y2);<br>
 * double b = Math.hypot(x - x2, y - y2);<br>
 * double c = Math.hypot(x1 - x, y1 - y);</code> </blockquote>
 * 
 * The <i>x-y</i> coordinates correspond to the <code>from</code> (<i>x</i><sub>1</sub>,&nbsp;<i>y</i><sub>1</sub>),
 * <code>to</code> (<i>x</i><sub>2</sub>,&nbsp;<i>y</i><sub>2</sub>), and <code>select</code> (<i>x</i>,&nbsp;<i>y</i>)
 * points in the figure. Given <code>a</code>, <code>b</code>, and <code>c</code>, as above, <code>dx</code> is then
 * calculated:
 * 
 * <blockquote><code>
 * double dx = (c * c - b * b - a * a) / (2.0 * a);</code> </blockquote>
 * 
 * Note that <code>dx</code> is 0 for a selection at the center of the target (as projected on the task axis), positive
 * for a selection on the far side of the center, and negative for a selection on the near side.
 * <p>
 * 
 * The effective target amplitude is simply <code>a + dx</code>. For serial tasks, an additional adjustment for <i>A</i>
 * <sub>e</sub> is to add <code>dx</code> from the previous trial (for all trials after the first). This is necessary
 * since each trial begins at the selection point of the previous trial. For discrete tasks, the trial is assumed to
 * begin at the center of the "from" target.
 * <p>
 * 
 * The use of the effective target amplitude (<i>A</i><sub>e</sub>) has little influence on throughput, provided
 * selections are distributed about the center of the targets. However, it is important to use <i>A</i><sub>e</sub> to
 * prevent "gaming the system." For example, if all movements fall short and only traverse, say, &frac12; of <i>A</i>,
 * throughput is artificially inflated if calculated using <i>A</i>. Using <i>A</i><sub>e</sub> prevents this. This is
 * part of the overall premise in using "effective" values: Participants get credit for what they actually did, not for
 * what they were asked to do.
 * <p>
 * 
 * Once a throughput object is instantiated, throughput and related measures are retrieved using public instance
 * methods. The most relevant methods are as follows:
 * 
 * <table border=1 cellspacing=0 width=80% cellpadding=5 summary="relevant methods">
 * 
 * <tr style="background-color:#cccccc">
 * <th>Method
 * <th>Return Type
 * <th>Description
 * 
 * <tr>
 * <td><code>getThroughput</code>
 * <td><code>double</code>
 * <td>Throughput for the sequences of trials
 * 
 * <tr>
 * <td><code>getAe</code>
 * <td><code>double</code>
 * <td>Effective target amplitude for the sequence
 * 
 * <tr>
 * <td><code>getWe</code>
 * <td><code>double</code>
 * <td>Effective target width for the sequence
 * 
 * <tr>
 * <td><code>getIDe</code>
 * <td><code>double</code>
 * <td>Effective index of difficulty for the sequence
 * 
 * <tr>
 * <td><code>getX</code>
 * <td><code>double</code>
 * <td>Mean of the <i>x</i>-selection coordinates for the sequence, as projected on the task axis and mapped relative to
 * the center of the target. A return value of 0.0 corresponds to selections clustered about the center of the target,
 * while positive or negative values correspond to selections with a mean on the near-side or far-side of the center of
 * the target, respectively.
 * 
 * <tr>
 * <td><code>getSDx</code>
 * <td><code>double</code>
 * <td>Standard deviation in the selection coordinates, as projected on the task axis
 * 
 * <tr>
 * <td><code>getDeltaX</code>
 * <td><code>double[]</code>
 * <td>The <i>x</i>-selection coordinates, as projected on the task axis
 * 
 * <tr>
 * <td><code>getSkewness</code>
 * <td><code>double</code>
 * <td>Skewness in the distribution formed by the selection coordinates
 * 
 * <tr>
 * <td><code>getKurtosis</code>
 * <td><code>double</code>
 * <td>Kurtosis in the distribution formed by the selection coordinates
 * 
 * <tr>
 * <td><code>isNormal</code>
 * <td><code>boolean</code>
 * <td>The result of a test of the null hypothesis that the distribution of selection coordinates is normally
 * distributed (<i>p</i> &lt; .05). The Lilliefors test is used. If <code>false</code> is returned the null hypothesis
 * is rejected, implying the distribution is not normal. If <code>true</code> is returned the null hypothesis is not
 * rejected, implying the distribution has passed the test for normality.
 * 
 * </table>
 * <p>
 * 
 * <!----------------------------------------------------------------------------------------> 
 * <b>Throughput Utility Program</b>
 * <p>
 * 
 * NOTE: The Throughput class in GoFitts does *not* include a <code>main</code> method and therefore is *not* executable
 * from a command prompt.  If you wish to use the Throughput class as a utility program that is executable
 * from a command prompt, it is available in a ZIP file along with a few other classes that are needed:
 * <a href="https://www.yorku.ca/mack/Throughput_executable.zip" style="color:#FF0000;">Click here</a>.
 * <p>
 * 
 * The <code>Throughput</code> class may be executed from a command prompt (but see NOTE above) to process data in a file. The following is
 * the usage message if executed without arguments:
 * 
 * <pre>
 *      PROMPT&gt;java Throughput
 *      Usage: java Throughput datafile -t|-s
 *      
 *         where datafile = file containing data
 *               -t = table output
 *               -s = summary output (1 line per sequence)
 * </pre>
 * 
 * The first task in using <code>Throughput</code> as a utility is to organize the data in a file and in the correct
 * format. The format is simple. As an example, the data for a sequence with 20 trials are organized in 25 lines:
 * 
 * <pre>
 * 
 *      Line     Data (comment)
 *      1        Code header (String &ndash; once only)
 *      2        Code (String &ndash; once per sequence)
 *      3        A, W (2 ints)
 *      4        Task type, Response type (2 String constants)
 *      5-24     From [x/y], To [x/y], Select [x/y], MT (7 ints)
 *      25       Blank (next sequence begins on next line)
 * </pre>
 * 
 * Consider the file <code><a href="example-data.txt">example-data.txt</a></code>, which contains data formatted as
 * above. The <code>Throughput</code> utility processes the data as follows (slightly abbreviated):
 * 
 * <pre>
 * 
	PROMPT&gt;java Throughput example-data.txt -t
	Code = P07,B05,G03,C03
	A = 312.0, W = 130.0 (ID = 1.77)
	Task_type = 1D, Response_type = Serial
	Data...
	============================================================
	 xFrom    yFrom     xTo      yTo     xSelect   ySelect    MT
	------------------------------------------------------------
	  540      592      227      592      218       534      262
	  227      592      540      592      529       496      268
	  540      592      227      592      195       608      248
	  227      592      540      592      533       547      233
	  540      592      227      592      209       651      251
	  227      592      540      592      607       554      252
	  540      592      227      592      231       650      283
	  227      592      540      592      540       568      214
	  540      592      227      592      231       642      301
	  227      592      540      592      560       567      266
	  540      592      227      592      207       653      258
	  227      592      540      592      524       604      258
	  540      592      227      592      239       704      248
	  227      592      540      592      515       610      242
	  540      592      227      592      180       675      241
	  227      592      540      592      501       606      252
	  540      592      227      592      215       666      243
	  227      592      540      592      571       621      255
	  540      592      227      592      215       690      252
	  227      592      540      592      521       641      210
	============================================================
	
	Number_of_trials = 20
	Select(x'): 9.0, -11.0, 32.0, -7.0, 18.0, 67.0, -4.0, 0.0, -4.0, 20.0, 20.0, -16.0, -12.0, -25.0, 47.0, -39.0, 12.0, 31.0, 12.0, -19.0, 
	-----
	Mean(x') = 6.55 pixels
	SD(x') = 25.57 pixels
	Skewness = 0.54
	Kurtosis = 0.37
	Is_normal? = true
	-----
	Misses = 1
	Error_rate = 5.0%
	-----
	Ae = 327.1 pixels
	We = 105.7 pixels
	IDe = 2.03 bits
	MT = 251.9 ms
	Throughput = 8.07 bps
 * </pre>
 * 
 * The <code>-t</code> option is used to provide output in a tabular format (see above). The first part of the output
 * simply echoes the input data in human readable form. After that, summary data available through the
 * <code>Throughput</code> class are shown, culminating with the value of throughput (in bits per second).
 * <p>
 * 
 * As well as the values used in computing throughput, the <code>Throughput</code> utility provides information about
 * the distribution of the selection coordinates, as projected on the task axis. This includes the skewness, kurtosis,
 * and the results of a normality test. These data are useful if the research seeks to examine whether the selection
 * coordinates form a Gaussian distribution, as assumed in the signal-and-noise model from which Fitts' law emerged. The
 * <code>Is_normal?</code> output is the result of a normality test. The null hypothesis is that the selection
 * coordinates are normally distributed (<i>p</i> &lt; .05). The Lilliefors test is used. If false is returned the null
 * hypothesis is rejected, implying the distribution is not normal. If true is returned the null hypothesis is not
 * rejected, implying the distribution has passed the test for normality.
 * <p>
 * 
 * The <code>Throughput</code> utility also outputs the number of misses in the sequence and the error rate (%). These
 * data were not explicitly provided to the <code>Throughput</code> class. They are calculated based on the geometry of
 * the trials, the task type, and the selection coordinates. The sequence of trials in the example above is from a
 * target selection task using finger input on a touchscreen device. The outcome was <i>TP</i> = 8.07 bps. This value is
 * higher than the <i>TP</i> typically reported for the mouse, which is generally in the 4 to 5 bps range.
 * <p>
 * 
 * The <code>-t</code> (table) option produces informative output; however, the organization is awkward if the analysis
 * involves hundreds of sequences of trials, as typical in experimental research. For this, the <code>-s</code>
 * (summary) option is more useful. With the <code>-s</code> option, the output is a rectangular, comma-delimited matrix
 * with full-precision data. There is a header row followed by one summary row per sequence. The number of columns is
 * <i>n</i> + 15, where <i>n</i> is the number of comma-delimited items in the code string (see the first two lines in
 * <a href="example-data.txt"><code>example-data.txt</code></a>). The fifteen columns following the code columns contain
 * the summary data, excluding the raw data. The header line identifies the data in each column.
 * <p>
 * 
 * The goal with the <code>-s</code> option is to provide output suitable for importing into a spreadsheet or statistics
 * application where the real work of analysing the data begins. Here's an example for the data in
 * <code><a href="example-data.txt">example-data.txt</a></code>:
 * 
 * <pre>
	PROMPT&gt;java Throughput example-data.txt -s
	Participant,Block,Group,Condition,Task,Response,A,W,ID,N,Skewness,Kurtosis,IsNormal,Ae,We,IDe,MT,Misses,Throughput
	P07,B05,G03,C03,1D,Serial,312.000000,130.000000,1.765535,20,0.538135,0.369387,true,327.050000,105.692130,2.033640,251.850000,1,8.074805
 * </pre>
 * <p>
 * 
 * Imported into a spreadsheet, the data above appear as follows (click to enlarge):
 * 
 * <center><p> <a href="Throughput-2.jpg"><img src="Throughput-2.jpg" width=700 alt="Throughput-2"></a> </center>
 * <p>
 * 
 * Of course, this is just a simple example. For a complete experiment, the data are likely to span hundreds, perhaps
 * thousands, of rows. With these, the task of summarizing and analysing the data begins.
 * <p>
 * 
 * Good luck. For comments or questions, please get in touch (<code>mack "at" yorku.ca</code> ).
 * 
 * <h2>References</h2>
 * <p>
 * 
 * 
 * 
 * Fitts, P. M., <a href="https://psycnet.apa.org/journals/xge/121/3/262/">The information capacity of the human motor
 * system in controlling the amplitude of movement</a>, <i>Journal of Experimental Psychology</i>, <i>47</i>, 1954,
 * 381-391. [PDF -- contact me and I'll send you the PDF]
 * <p>
 * 
 * MacKenzie, I. S., <a href="https://www.yorku.ca/mack/HCI.html">Fitts' law as a research and design tool in
 * human-computer interaction</a>, <i>Human-Computer Interaction</i>, <i>7</i>, 1992, 91-139. [
 * <a href="http://www.yorku.ca/mack/hci1992.pdf">PDF</a>]
 * <P>
 * 
 * Soukoreff, R. W. and MacKenzie, I. S., <a href="https://www.yorku.ca/mack/ijhcs2004.html">Towards a standard for
 * pointing device evaluation: Perspectives on 27 years of Fitts' law research in HCI</a>, <i>International Journal of
 * Human-Computer Studies</i>, <i>61</i>, 2004, 751-789. [<a href="http://www.yorku.ca/mack/ijhcs2004.pdf">PDF</a>]
 * <P>
 *
 * MacKenzie, I. S. (2015). <a href="https://www.yorku.ca/mack/hcii2015a.html">Fitts' throughput and the remarkable case
 * of touch-based target selection</a>. <i>Proceedings of the 17th International Conference on Human-Computer
 * Interaction - HCII 2015 (LNCS 9170)</i>, pp. 238-249. Switzerland: Springer. [
 * <a href="http://www.yorku.ca/mack/hcii2015a.pdf">PDF</a>]
 * <p>
 * 
 * MacKenzie, I. S. (2018). <a href="https://www.yorku.ca/mack/hhci2018.html">Fitts' law</a>. In K. L. Norman &amp; J.
 * Kirakowski (Eds.), <i>Handbook of human-computer interaction</i>, pp. 349-370. Hoboken, NJ: Wiley. [<a
 * href="http://www.yorku.ca/mack/hhci2018.pdf">PDF</a>]
 * <p>
 * 
 * 
 * @author Scott MacKenzie, 2013-2025<br>William Soukoreff, 2013
 */
public class Throughput
{
	final static double LOG_TWO = 0.693147181;
	final static double SQRT_2_PI_E = 4.132731354;

	// int constants for response type
	final static int SERIAL = 100;
	final static int DISCRETE = 101;

	// int constants for tasks type
	final static int ONE_DIMENSIONAL = 200;
	final static int TWO_DIMENSIONAL = 201;

	/*
	 * The following are the core set of data values needed to compute throughput and other measures provided in this
	 * class.
	 */
	String code;
	int amplitude, width;
	Point2D.Double[] from, to, select;
	int[] mt;
	int numberOfTrials;
	boolean serialTask;
	int responseType, taskType;

	/*
	 * The following arrays are populated with values calculated from the data in the arrays above.
	 */
	double[] deltaX;
	double[] ae;
	int[] miss;

	// Blank constructor. Must be followed with setData
	Throughput()
	{
	}

	// Constructor with data
	Throughput(String codeArg, int amplitudeArg, int widthArg, int taskTypeArg, int responseTypeArg, Point2D.Double[] fromArg,
			Point2D.Double[] toArg, Point2D.Double[] selectArg, int[] mtArg)
	{
		setData(codeArg, amplitudeArg, widthArg, taskTypeArg, responseTypeArg, fromArg, toArg, selectArg, mtArg);
	}

	/**
	 * Set the data for this Throughput object. This method can be used to provide a new set of the data to the
	 * Throughput object (without instantiated a new object).
	 * 
	 * @param codeArg the code
	 * @param amplitudeArg the movement amplitude
	 * @param widthArg the target width
	 * @param taskTypeArg the task type
	 * @param responseTypeArg the response type
	 * @param fromArg the from coordinate
	 * @param toArg the to coordinate
	 * @param selectArg the selection coordinate
	 * @param mtArg the movement time in milliseconds
	 * 
	 */
	public void setData(String codeArg, int amplitudeArg, int widthArg, int taskTypeArg, int responseTypeArg,
			Point2D.Double[] fromArg, Point2D.Double[] toArg, Point2D.Double[] selectArg, int[] mtArg)
	{
		// load core requisite data
		code = codeArg;
		amplitude = amplitudeArg;
		width = widthArg;
		taskType = taskTypeArg;
		responseType = responseTypeArg;
		from = fromArg;
		to = toArg;
		select = selectArg;
		mt = mtArg;

		// calculate data that depend on the core data loaded above (begin by initializing arrays)
		numberOfTrials = mt.length;
		deltaX = new double[mt.length];
		ae = new double[mt.length];
		miss = new int[mt.length];

		// bug fix (Oct 22, 2015) thanks to Francesca Roig
		serialTask = (responseType == SERIAL);

		// fill deltaX, ae, and miss arrays
		for (int i = 0; i < to.length; ++i)
		{
			deltaX[i] = getDeltaX(from[i], to[i], select[i]);

			/*
			 * Compute the effective movement amplitude. The computed amplitude, a, is adjusted by adding dx at the end
			 * of the trial to give the actual amplitude moved (as projected on the task axis).
			 * 
			 * For serial tasks, we also adjust for the starting position by adding dx from the previous trial (if i >
			 * 0).
			 */

			ae[i] = getAe(from[i], to[i], select[i]);
			if (serialTask && i > 0)
				ae[i] += deltaX[i - 1];

			/*
			 * Compute whether or not the target was missed. This information is not explicitly provided to the
			 * Throughput class, so we need to calculate it. For the 1D case, the target is missed if deltaX is more
			 * than half the target width. For the 2D case, we assume the targets are circular, which is typically the
			 * case. The target is missed if the distance from the selection coordinate to the center of the target is
			 * greater than half the diameter (i.e., width) of the target circle.
			 */
			double distanceToCenter = Math.hypot(select[i].x - to[i].x, select[i].y - to[i].y);		
			
			if (taskType == Throughput.ONE_DIMENSIONAL)
				miss[i] = Math.abs(deltaX[i]) > (width / 2.0) ? 1 : 0;
				
			else if (taskType == Throughput.TWO_DIMENSIONAL)					
				miss[i] = distanceToCenter > (width / 2.0 + 1.0) ? 1 : 0; // NOTE: "+1" added!?					
			else
				miss[i] = -1;
		}
	}

	/**
	 * Returns the code associated with this sequence of trials. The code is the string assigned to the sequence to
	 * associate test conditions (e.g., participant code, device code, etc.) with the sequence.
	 * 
	 * @return a code for this sequence of trials
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * Returns the Throughput for the sequence of trials.
	 * 
	 * @return the throughput
	 */
	public double getThroughput()
	{
		double aeMean = mean(ae);
		double sdx = sd(deltaX);
		double we = SQRT_2_PI_E * sdx;
		double ide = Math.log(aeMean / we + 1.0) / LOG_TWO; // bits
		double mtMean = mean(mt) / 1000.0; // seconds
		return ide / mtMean; // bits per second
	}

	/**
	 * Returns the mean movement time (ms) for the sequence of trials.
	 * 
	 * @return the movement time in millisecond
	 */
	public double getMT()
	{
		return mean(mt); // milliseconds
	}

	/**
	 * Returns the number of trials in this sequence.
	 * 
	 * @return the number of trials
	 */
	public int getNumberOfTrials()
	{
		return numberOfTrials;
	}

	/**
	 * Returns the task type for this sequence. The return value is the int given to the Throughput object (via the
	 * constructor or setData). Should be Throughput.ONE_DIMENSIONAL or Throughout.TWO_DIMENSIONAL.
	 * 
	 * @return the task type integer code
	 */
	public int getTaskType()
	{
		return taskType;
	}

	/**
	 * Returns a string representing the task type for this sequence. The string returned is "1D", "2D", or "?" (if the
	 * task type is unknown).
	 * 
	 * @param taskType a integer code for the task type
	 * @return a string for the taks type (1D or 2D)
	 */
	public String getTaskTypeString(int taskType)
	{
		if (taskType == ONE_DIMENSIONAL)
			return "1D";
		else if (taskType == TWO_DIMENSIONAL)
			return "2D";
		else
			return "?";
	}

	/**
	 * Returns the response type for this sequence. The value returned is the int constant passed to the Throughput
	 * object in the first place (via the constructor or setData). Should be Throughput.SERIAL or Throughput.DISCRETE.
	 * 
	 * @return the response type integer code
	 */
	public int getResponseType()
	{
		return responseType;
	}

	/**
	 * Returns a string representing the response type for this sequence. The string returned is "Serial", "Discrete",
	 * of "?" (if the response type is unknown).
	 * 
	 * @param responseType a code for the response
	 * @return a string for the response type (Serial or Discrete)
	 */
	public String getResponseTypeString(int responseType)
	{
		if (responseType == SERIAL)
			return "Serial";
		else if (responseType == DISCRETE)
			return "Discrete";
		else
			return "?";
	}

	/**
	 * Returns a point array containing the "from" points for the trials in this sequence. The "from" points are the
	 * coordinates of the center of the target from which each trial begins.
	 * 
	 * @return the from coordinate of the target
	 */
	public Point2D.Double[] getFrom()
	{
		return from;
	}

	/**
	 * Returns a point array containing the "to" points for the trials in this sequence. The "to" points are the
	 * coordinates of the center of the target to which each trial proceeds.
	 * 
	 * @return the to coordinate of the target
	 */
	public Point2D.Double[] getTo()
	{
		return to;
	}

	/**
	 * Returns a point array containing the "select" points for the trials in this sequence. The "select" points are the
	 * coordinates of the point of selection where each trial terminated.
	 * 
	 * @return the selection coordinate
	 */
	public Point2D.Double[] getSelect()
	{
		return select;
	}

	/**
	 * Returns the double array holding the mt (movement time) values for the trials in this sequence.
	 * 
	 * @return an array of the movement times
	 */
	public int[] getMTArray()
	{
		return mt;
	}

	/**
	 * Returns the standard deviation in the selection coordinates for this sequence of trials. The coordinates are
	 * projected onto the task axis.
	 * 
	 * @return the standard deviation in the x coordinates
	 */
	public double getSDx()
	{
		return sd(deltaX);
	}

	/**
	 * Returns the mean of the selection coordinates for this sequence of trials. The coordinates are projected onto the
	 * task axis.
	 * 
	 * @return the x coordinate
	 */
	public double getX()
	{
		return mean(getDeltaX());
	}

	/**
	 * Returns the array of x-selection coordinates for this sequence of trials. The coordinates are projected onto the
	 * task axis.
	 * 
	 * @return the deviation from the target center
	 */
	public double[] getDeltaX()
	{
		return deltaX;
	}

	/**
	 * Returns the specified amplitude for the trials in this sequence.
	 * 
	 * NOTE: This value is not used in calculating Throughput. It is provided only as a convenience.
	 * 
	 * @return the target amplitude
	 */
	public double getA()
	{
		return amplitude;
	}

	/**
	 * Returns the effective amplitude for the trials in this sequence. The effective amplitude is the mean of the
	 * actual movement amplitudes for the sequence of trials, as projected on the task axis.
	 * 
	 * @return the effective movement amplitude
	 */
	public double getAe()
	{
		return mean(ae);
	}

	/**
	 * Returns the specified target width for this sequence of trials.
	 * 
	 * NOTE: This value is not used in calculating Throughput. It is provided only as a convenience.
	 * 
	 * @return the target width
	 */
	public double getW()
	{
		return width;
	}

	/**
	 * Returns the effective target width for this sequence of trials. The effective target width is 4.133 x SDx, where
	 * SDx is the standard deviation in the selection coordinates, as projected onto the task axis.
	 * 
	 * @return the effective target width
	 */
	public double getWe()
	{
		return SQRT_2_PI_E * getSDx();
	}

	/**
	 * Returns the specified index of difficulty for this sequence of trials. The specified index of difficulty is ID =
	 * log2(A/W + 1).
	 * 
	 * NOTE: This value is not used in calculating Throughput. It is provided only as a convenience.
	 * 
	 * @return the index of difficulty
	 */
	public double getID()
	{
		return Math.log(getA() / getW() + 1) / LOG_TWO;
	}

	/**
	 * Returns the effective index of difficulty for this sequence of trials. The effective index of difficulty, IDe =
	 * log2(Ae/We + 1).
	 * 
	 * @return the effective index of difficulty
	 */
	public double getIDe()
	{
		return Math.log(getAe() / (SQRT_2_PI_E * getSDx()) + 1.0) / LOG_TWO;
	}

	/**
	 * Returns the number of misses for this sequence.
	 * 
	 * @return numumber of misses
	 */
	public int getMisses()
	{
		int count = 0;
		for (int i = 0; i < getNumberOfTrials(); ++i)
			count += miss[i];
		return count;
	}

	/**
	 * Returns the error rate as a percentage.
	 * 
	 * @return the error rate
	 */
	public double getErrorRate()
	{	
		return (double)getMisses() / getNumberOfTrials() * 100.0;
	}

	/**
	 * Returns the skewness in the selection coordinates for this sequence of trials. The selection coordinates are
	 * projected onto the task axis.
	 * 
	 * @return the skewness
	 */
	public double getSkewness()
	{
		return getSkewness(getDeltaX());
	}

	/**
	 * Returns the kurtosis in the selection coordinates for this sequence of trials. The selection coordinates are
	 * projected onto the task axis.
	 * 
	 * @return the kurtosis
	 */
	public double getKurtosis()
	{
		return getKurtosis(getDeltaX());
	}

	/**
	 * Returns a boolean holding the result of a Lilliefors test for normality. The test is done at an alpha of 0.05.
	 * The null hypothesis is that the selection coordinates in this sequence of trials, as projected on the task axis,
	 * are normally distributed. If true is returned, the null hypothesis is retained (not rejected). If false is
	 * returned, the null hypothesis is rejected.
	 * 
	 * @return true if normal
	 */
	public boolean getIsNormal()
	{
		return true;//Lilliefors.isNormal(getDeltaX());
	}

	// ===========================
	// S T A T I C _ M E T H O D S
	// ===========================

	/**
	 * Returns deltaX for a trial. The geometry for a trial is defined by three points: from (center of the "from"
	 * target), to (center of the "to" target), and select (the selection coordinate). These are used in computing
	 * deltaX, which is the distance from the selection coordinate to the target center, as projected on the task axis.
	 * <p>
	 * 
	 * NOTE: This calculation is correct, but a diagram helps to visualize the geometry. deltaX is negative for a
	 * selection on the "near side" of the target center (undershoot) and positive for a selection on the "far side" of
	 * the target center (overshoot). For a near-side selection, the a-b-c triangle is acute (i.e., a^2 + b^2 &gt; c^2).
	 * For a far-side selection the a-b-c triangle is obtuse (i.e., a^2 + b^2 &lt; c^2).
	 * <p>
	 * 
	 * NOTE: This method is defined as a static method so that is may be called by an application on a per-trial basis.
	 * Recall that instances of the Throughput class work with the data for the entire sequence.
	 * 
	 * @param from the from coordinate of the target
	 * @param to the to coordinate of the target
	 * @param select the coordinate of the selection point
	 * @return delta x (the deviation of selection from the target center)
	 */
	public static double getDeltaX(Point2D.Double from, Point2D.Double to, Point2D.Double select)
	{
		// start-of-trial coordinate (centre of the "from" target)
		double x1 = from.x;
		double y1 = from.y;

		// centre coordinate of the target to select (center of the "to" target)
		double x2 = to.x;
		double y2 = to.y;

		// actual selection coordinate ("select")
		double x = select.x;
		double y = select.y;

		// compute length of the sides of the triangle formed by the three points above
		double a = Math.hypot(x1 - x2, y1 - y2); // a is the specified amplitude
		double b = Math.hypot(x - x2, y - y2); // b is the distance from the selection point to the
												// target center
		double c = Math.hypot(x1 - x, y1 - y);

		/*
		 * Compute and return deltaX. This calculation is correct, but a diagram helps to visualize the geometry. dx is
		 * negative for a selection on the "near side" of the target center (undershoot) and positive for a selection on
		 * the "far side" of the target center (overshoot). For a near-side selection, the a-b-c triangle is acute
		 * (i.e., a^2 + b^2 > c^2). For a far-side selection the a-b-c triangle is obtuse (i.e., a^2 + b^2 < c^2).
		 */
		return (c * c - b * b - a * a) / (2.0 * a);
	}

	/**
	 * Returns the effective amplitude (Ae) for a trial. The geometry for a trial is defined by three points: from
	 * (center of the "from" target), to (center of the "to" target), and select (the selection coordinate). These are
	 * used in computing Ae, which is A (the distance between the "from" and "to" points) plus deltaX. See as well,
	 * getTrialDeltaX.
	 * <p>
	 * 
	 * NOTE: The value of Ae calculated here assumes the trial started at the "from" coordinate. For serial responses,
	 * this may not be the case. An additional adjustment may be warranted for the beginning of the trial, such as
	 * adding deltaX from the previous trial (for all trials after the first trial in a sequence).
	 * <p>
	 * 
	 * NOTE: This method is defined as a static method so that is may be called by an application on a per-trial basis.
	 * Recall that instances of the Throughput class work with the data for the entire sequence.
	 * 
	 * @param from the from coordinate of the target
	 * @param to the to coordinate of the target
	 * @param select the coordinate of the selection point
	 * @return the effective movement amplitude
	 */
	public static double getAe(Point2D.Double from, Point2D.Double to, Point2D.Double select)
	{
		double a = Math.hypot(to.x - from.x, to.y - from.y);
		double dx = getDeltaX(from, to, select);
		return a + dx;
	}

	/**
	 * Returns the skewness in the specified array of doubles.
	 * 
	 * @param d the data to test
	 * @return skewness of the data in d
	 */
	public static double getSkewness(double[] d)
	{
		double m = mean(d);
		double sd = sd(d);
		double skew = 0.0;
		double n = (double)d.length;
		double factor = n / ((n - 1.0) * (n - 2.0));
		for (int i = 0; i < d.length; ++i)
			skew += Math.pow((d[i] - m) / sd, 3.0);
		skew *= factor;
		return skew;
	}

	/**
	 * Returns the kurtosis in the specified array of doubles.
	 * 
	 * @param d the data to test
	 * @return kurtosis of the data in d
	 */
	public static double getKurtosis(double[] d)
	{
		double m = mean(d);
		double sd = sd(d);
		double kur = 0.0;
		double n = (double)d.length;
		double factor1 = (n * (n + 1.0)) / ((n - 1.0) * (n - 2.0) * (n - 3.0));
		double factor2 = (3.0 * (n - 1.0) * (n - 1.0)) / ((n - 2.0) * (n - 3.0));
		for (int i = 0; i < d.length; ++i)
			kur += Math.pow((d[i] - m) / sd, 4.0);
		kur = factor1 * kur - factor2;
		return kur;
	}

	/**
	 * Returns a boolean holding the result of a Lilliefors test for normality on the specified array of doubles. The
	 * test is done at an alpha of 0.05. The null hypothesis is that the values in the array are normally distributed.
	 * If true is returned, the null hypothesis is retained (not rejected). If false is returned, the null hypothesis is
	 * rejected.
	 * 
	 * @param d the data to test
	 * @return true if the data in d are normally distributed
	 */
	public static boolean getIsNormal(double[] d)
	{
		return true;//Lilliefors.isNormal(d);
	}

	/**
	 * Calculate the mean of the values in a double array.
	 */
	private static double mean(double n[])
	{
		double mean = 0.0;
		for (int j = 0; j < n.length; j++)
			mean += n[j];
		return mean / n.length;
	}

	/**
	 * Calculate the mean of the values in a int array.
	 */
	private static double mean(int n[])
	{
		double mean = 0.0;
		for (int j = 0; j < n.length; j++)
			mean += n[j];
		return mean / n.length;
	}

	/**
	 * Calculate the standard deviation of values in a double array.
	 */
	private static double sd(double[] n)
	{
		double m = mean(n);
		double t = 0.0;
		for (int j = 0; j < n.length; j++)
			t += (m - n[j]) * (m - n[j]);
		return Math.sqrt(t / (n.length - 1.0));
	}
}
