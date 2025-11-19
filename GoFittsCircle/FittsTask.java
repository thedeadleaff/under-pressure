import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.io.*;

/**
 * <hr>
 * 
 * <table width="100%" summary="FittsTask title">
 * <tr style="background-color:#cccccc">
 * <td align="center">
 * <h1>FittsTask</h1>
 * </table>
 * 
 * <h2>Summary</h2>
 * 
 * <ul>
 * <li>Experiment software to evaluate the performance of computer pointing devices or interaction techniques.
 * <li>Implements the one-dimensional and two-dimensional Fitts' law tasks, as per ISO 9241-9 (2000; updated in 2012 as
 * ISO/TC 9241-411).
 * 
 * <li>Performance data are gathered and saved in output files for follow-up analyses.
 * <p>
 * </ul>
 * 
 * <h2>Related References</h2>
 * 
 * The following publications present research using a version of this software for experimental testing of pointing
 * devices or interaction techniques. NOTE: Earlier versions of this software were called FittsTaskOne or FittsTaskTwo.
 * 
 * <ul>
 * 
 * <li><a href="https://www.tandfonline.com/doi/full/10.1080/10803548.2025.2462437">"The Effect of Knee Extension Exercise on Cognitive and Computer Skill Performance in Office Workers"</a>,
 * by Mohammadian et al. (<i>Int J Occupational Safety and Ergonomics 2025</i>). 
 * 
 * <li><a href="https://doi.org/10.1145/3670653.3670676">"Comparing the Effectiveness and Ergonomics of Smartphone-Based Gamepads"</a>, 
 * by W&uuml;hrl et al. (<i>MuC 2024</i>).
 * 
 * <li><a href="https://doi.org/10.1080/00140139.2024.2411302">"Gesture Centric Interaction: Evaluating Hand and Head Gestures in Touchless Cursor Control"</a>, 
 * by Thushara et al. (<i>Ergonomics 2024</i>).
 * 
 * <li><a href="https://www.yorku.ca/mack/icchp2022.html">"TBS<sup>3</sup>: Two-Bar Single-Switch Scanning for Target Selection" </a>, 
 * by Raynal and MacKenzie (<i>ICCHP 2022</i>).
 *  
 * <li><a href="https://www.yorku.ca/mack/interact2019.html">"FittsFarm: Comparing Children's Drag-and-Drop Performance Using Finger and Stylus Input on Tablets" </a>, 
 * by Cassidy, Read, and MacKenzie (<i>INTERACT 2019</i>).
 * 
 * <li><a href="https://www.yorku.ca/mack/hcii2015b.html">"Camera Mouse + ClickerAID: Dwell vs. Single-muscle Click
 * Actuation in Mouse-replacement Interfaces" </a>, by Magee, Felzer, and MacKenzie (<i>HCII 2015</i>).
 * 
 * <li><a href= "https://ieeexplore.ieee.org/xpl/articleDetails.jsp?tp=&arnumber=6733317">
 * "Human-Computer Interface Controlled by the Lip"</a>, by Jos&eacute; and de Deus Lopes (<i>IEEE Journal of Biomedical
 * and Health Informatics 2015</i>).
 * 
 * <li><a href="https://pro.sagepub.com/content/56/1/521.short">"The Design, Implementation, and Evaluation of a Pointing
 * Device for a Wearable Computer"</a>, by Calvo, Burnett, Finomore, and Perugini (<i>HFES 2012</i>).
 * 
 * <li><a href="https://www.yorku.ca/mack/FuturePlay1.html"> "The Trackball Controller: Improving the Analog Stick"</a>,
 * by Natapov and MacKenzie (<i>FuturePlay 2010</i>).
 * 
 * <li><a href="https://www.yorku.ca/mack/hfes2009.html">"Evaluation of Mouse and Touch Input for a Tabletop Display
 * Using Fitts' Reciprocal Tapping Task"</a>, by Sasangohar, MacKenzie, and Scott (<i>HFES 2009</i>).
 * 
 * <li><a href="https://www.yorku.ca/mack/3dui2009.html">
 * "Effects of Tracking Technology, Latency, and Spatial Jitter on Object Movement" </a> , by Teather, Pavlovych,
 * Stuerzlinger, and MacKenzie (<i>3DUI 2009</i>)
 * 
 * <li><a href="https://www.yorku.ca/mack/eics2009a.html">
 * "An Empirical Comparison of Wiimote Gun Attachments for Pointing Tasks"</a> , by McArthur, Castellucci, and
 * MacKenzie (<i>EICS 2009</i>).
 * 
 * <li><a href="https://www.yorku.ca/mack/hcii2007.html"> "Evaluating Eye Tracking With ISO 9241 - Part 9"</a>, by Zhang
 * and MacKenzie ( <i>HCII 2007</i>).
 * 
 * <li><a href="https://www.yorku.ca/mack/CHI01.htm"> "Accuracy Measures for Evaluating Computer Pointing Devices"</a>,
 * by MacKenzie, Kauppinen, and Silfverberg (<i>CHI 2001</i>).
 * <p>
 * </ul>
 * 
 * The following publications provide background information on Fitts' law and experimental testing using the Fitts'
 * paradigm.
 * 
 * <ul>
 * 
 * <li><a href="https://www.yorku.ca/mack/hhci2018.html">"Fitts' Law"</a>, by MacKenzie (Wiley's <i>Handbook of
 * Human-Computer Interaction</i>, 2018)
 * 
 * <li><a href="https://www.yorku.ca/mack/ijhcs2004.pdf">"Towards a Standard for Pointing Device Evaluation: Perspectives
 * on 27 Years of Fitts' Law Research in HCI"</a>, by Soukoreff and MacKenzie (<i>IJHCS 2004</i>).
 * 
 * <li><a href="https://www.yorku.ca/mack/HCI.html">
 * "Fitts' Law as a Research and Design Tool in Human-Computer Interaction"</a>, by MacKenzie (<i>HCI 1992</i>).
 * <p>
 * </ul>
 * 
 * <h2>Getting Started</h2>
 * 
 * FittsTask is launched from GoFitts by selecting FittsTask and clicking "Go" (below, left). FittsTask begins with a
 * launch panel (below, centre). Clicking "Setup" opens the Setup dialog (below, right). Via the Setup dialog,
 * parameters are set for the current block of testing. (Click images to enlarge.)
 * 
 * <center><p> <a href="FittsTask-0.jpg"><img src="FittsTask-0.jpg" width="150" alt ="FittsTask-0"></a>
 * <a href="FittsTask-1.jpg"><img src="FittsTask-1.jpg" width="350" alt= "FittsTask-1"></a>
 * <a href="FittsTask-2.png"><img src="FittsTask-2.png" width="250" alt= "FittsTask-2"></a> </center>
 * <p> 
 * 
 * The default parameter settings are read from <a href="FittsTask.cfg">FittsTask.cfg</a> but may be changed through the
 * setup dialog for the current invocation. Changes may be saved by clicking the "Save" button (see above).
 * <p>
 * 
 * The setup parameters are as follows:
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="6" summary="setup parameters">
 * <tr style="background-color:#cccccc">
 * <th align="center">Parameter
 * <th align="center">Description
 * 
 * <tr>
 * <td valign="top">Participant code
 * <td>Identifies the current participant. This is used in forming the names for the output data files. Also, the output
 * data files include a column with the participant code. (The same is true for the next four setup parameters.)
 * 
 * <tr>
 * <td valign="top">Condition code
 * <td>An arbitrary code to associate a test condition with a block of trials. This parameter might be useful when
 * testing a condition that is not inherently part of the application. For example, if the software is used to compare a
 * mouse, touchpad, and joystick, the mouse condition could be "C01", the touchpad condition "C02", and the joystick
 * condition "C03".
 * 
 * <tr>
 * <td valign="top">Session code
 * <td>Identifies the session. This code is used if testing proceeds over multiple sessions to gauge the progression of
 * learning.
 * 
 * <tr>
 * <td valign="top">Group code
 * <td>Identifies the group to which the participant was assigned. This code is useful if counterbalancing is used to
 * offset order effects for a within-subjects independent variable. In this case, participants are divided into group
 * and each group is given the conditions in a different order. The group code identifies the group.
 * 
 * <tr>
 * <td valign="top" colspan=2>NOTE: There is no setup parameter for "Block code" . The block code is generated
 * automatically. For any given set of conditions, the first block of testing is "B01", the second is "B02", and so on.
 * Output data files include the block code in the filename and in a column of the data. The first available block code
 * is used in opening data files for output. This prevents overwriting data from an earlier block of testing.
 * 
 * <tr>
 * <td valign="top">Task type
 * <td>Radio buttons to select either the one-dimensional (1D) or two-dimensional (2D) task.
 * 
 * <tr>
 * <td valign="top">Selection method
 * <td>Specifies the method of target selection for each trial. The options are
 * <ul>
 * <li>Mouse button (MB) &ndash; a target selection occurs by pressing and releasing the primary mouse button. This is
 * the default selection method.
 * <li>Dwell time (DT<i>n</i>) &ndash; a target is selected when the mouse cursor enters the target and remains in the
 * target for <i>n</i> milliseconds (see below).
 * <li>Keyboard (KB) &ndash; a target selection occurs by pressing and releasing either the "F" or "J" key on the system
 * keyboard.
 * </ul>
 * <p>
 * 
 * Note: When using dwell time selection, errors are not possible since selection only occurs when the cursor is inside
 * the target.
 * 
 * <tr>
 * <td valign="top">Interaction method
 * <td>Specifies the interaction method to use.  The options are
 * <ul>
 * <li>Point-select &ndash; the standard point-select method for Fitts' law testing
 * <li>Drag-select &ndash; each trial requires an object to be dragged from the source target to the destination target.  
 * Dragging is state-two interaction and typically occurs with the pointing device's primary button depressed (i.e., down).
 * <li>Fitts Farm &ndash; similar to Drag-select, except using using a feed-the-animals motif.  The object to drag is 
 * an apple and a farm animal appears in the destination target.  Research with children is the obvious application. 
 * Fitts Farm was first implemented in an Android application (FittsDragAndDrop) for research presented at <i>INTERACT 2019</i>.  
 * <a href="https://www.yorku.ca/mack/interact2019.html"  style="color:#FF0000;">Click here</a> to view the paper (which includes a link to the Android software).
 * </ul>
 * <p>
 * 
 * NOTE: The Drag-select and Fitts Farm interaction methods were added to GoFitts by Ramon Mas-Sans&oacute; and Maria Francesca Roig-Maim&oacute; (2024-2025).
 * 
 * <tr>
 * <td valign="top">Dwell time
 * <td>This setup parameter only applies when using dwell-time selection (see above). In this case, target selection
 * occurs after the cursor is inside the target for the specified time. The combobox options range from 0 ms to 2000 ms
 * in 100-ms increments.
 * <p>
 * 
 * During the dwell interval, the dwell progression is shown using a rotating arc as visual feedback. The arc appears
 * inside the target and advances around the target, completing a full rotation at the end of the dwell interval. See
 * below.
 * 
 * <center> <a href="FittsTask-8.jpg"><img src="FittsTask-8.jpg" alt= "FittsTask-8" width=400></a> </center>
 * <p>
 * 
 * The colour of the arc is set by the "mouse-over" colour button at the bottom of the Setup dialog.
 * <p>
 * 
 * Note: Setting dwell time to 0 ms is a special case for dwell-time selection. With zero dwell time, selection occurs
 * immediately when the cursor first enters the target. Thus, the dwell timer is not used and there is no visual
 * feedback for the dwell progression.
 * 
 * <tr>
 * <td valign="top">Minimum dwell arc size
 * <td>This setup parameter only applies when using dwell-time selection (see above). The nominal size of the dwell arc
 * is the target width, as seen in the images above. The size of the dwell arc may be increased for small targets using
 * this option. This might be necessary when occlusion is an issue, for example, with finger input.
 * <p>
 * 
 * The first value in the combobox is the default ( "Target width"). Additional values range from 20 pixels to 200
 * pixels, in 20-pixel increments. As an example, if this option is set to 80 pixels and the targets are 40 pixels wide,
 * then the following extended arc appears outside the target:
 * 
 * <center> <a href="FittsTask-9.jpg"><img src="FittsTask-9.jpg" alt= "FittsTask-9" width=400></a> </center>
 * <p>
 * 
 * The extended arc colour is slightly lighter than the inside arc colour.
 * 
 * <tr>
 * <td valign="top">Number of trials (1D) or Targets (2D)
 * <td>An integer value that specifies either the number of trials (1D) or the number of targets (2D).
 * <p>
 * 
 * Only odd numbers are supported. This is primarily a consideration for 2D tasks. Using an odd number ensures that the
 * distance between successive targets is the same for each trial. This distance is a bit less than the specified
 * amplitude, which is the diameter of the layout circle.
 * 
 * <tr>
 * <td valign="top">Target amplitudes
 * <td>Target amplitude (<i>A</i>) is either the horizontal distance between target centres (1D) or the diameter of the
 * layout circle (2D). The units are pixels.
 * <p>
 * 
 * Multiple amplitudes may be specified using a comma delimiter.
 * 
 * <tr>
 * <td valign="top">Target widths
 * <td>Target width (<i>W</i>) is either the width of the target rectangles (1D) or the diameter of the target circles
 * (2D). The units are pixels.
 * <p>
 * 
 * Multiple widths may be specified using a comma delimiter.
 * 
 * <p>
 * Note: A "block" of testing covers all combinations of <i>A</i> and <i>W</i>. A "sequence" refers to a series of
 * trials for one <i>A-W</i> condition. If there are <i>m</i> amplitudes and <i>n</i> widths, the block contains
 * <i>m</i> &times; <i>n</i> sequences.
 * 
 * <tr>
 * <td valign="top">Error threshold
 * <td>Specifies an error threshold (%) above which a sequence of trials is deemed an outlier. In this case, the
 * sequence is repeated. A value of 100 effective disables this feature.
 * <p>
 * 
 * Note: Data are not saved for an outlier sequence. However, the sd2 output file includes a data column for the
 * sequence repeat count ("SRC") for each <i>A-W</i> condition. Usually (hopefully!) SRC is zero.
 * 
 * <tr>
 * <td valign="top">Spatial hysteresis
 * <td>Spatial hysteresis (SH) is a scaling factor to create a larger virtual target &ndash; a hysteresis zone &ndash;
 * to improve target selection. The mouse pointer is deemed to enter the target when it enters the "real target". The
 * pointer is deemed to exit the target when the pointer exits the hysteresis zone.
 * <p>
 * 
 * With SH = 2.0, for example, the hysteresis zone has 2&times; the width of the real target. The default value of 1.0
 * essentially disables this feature.
 * <p>
 * 
 * The idea of spatial hysteresis has not been tested experimentally. It is simply an idea to improve selection for
 * small targets when there is limited space available between the targets. If anyone is interested in testing this idea
 * experimentally, please let me know (Scott MacKenzie, mack "at" cse.yorku.ca).
 * 
 * <tr>
 * <td valign="top">Randomize target conditions
 * <td>A checkbox item. If checked, a random-without-replacement algorithm is used to select the <i>A-W</i> condition
 * for each sequence in a block.
 * 
 * <tr>
 * <td valign="top">Show marker at center of target
 * <td>A checkbox item. If checked, a marker ("+") is shown at the center of the target. Below is an example of a target
 * with a marker (left) and without a marker (right).
 * 
 * <center><a href="FittsTask-10.jpg"><img src="FittsTask-10.jpg" width="200" alt= "FittsTask-10"></a></center>
 * <p>
 * 
 * Including a marker might be useful if targets are selected using gaze selection with an eye tracking apparatus. In
 * this case, participants have a clear fixation point at the center of the target. Even for input using a mouse or
 * other pointing device, it might be useful to include a marker. What is the participant's task? Is the participant
 * required simply to select anywhere within the target, or should the participant strive to select the center of the
 * target? What is effect of these two strategies on throughput?
 * 
 * 
 * <tr>
 * <td valign="top">Use target image
 * <td>A checkbox item. If checked, an image is used for the target. This option only applies to 2D tasks. The default
 * image is a ball in the file <code>targetImage.gif</code>. Change this to an image of your own choosing, if desired.
 * 
 * <tr>
 * <td valign="top">Auditory feedback
 * <td>A checkbox item. If checked, a "tick" auditory sound is emitted upon target selection. A "beep" is emitted if a
 * selection is in error (i.e., outside the target on button-up).
 * 
 * <tr>
 * <td valign="top">Button-down highlight
 * <td>A checkbox item. If checked, uses a different target colour when the mouse button is down (and the cursor is
 * above a target).
 * 
 * <tr>
 * <td valign="top">Mouse-over highlight
 * <td>A checkbox item. If checked, uses a different target colour when the cursor is above a target.
 * 
 * <tr>
 * <td valign="top">Hide cursor
 * <td>A checkbox item.  If checked, hides the cursor.  This option is intended for applications using "direct input" 
 * such as touch on a touch-sensing display or eye input using an eye tracker.  See as well the options for dwell-time selection
 * and mouse-over highlight.
 * 
 * <tr>
 * <td valign="top">Colors
 * <td>Five buttons that launch color choosers to set the colors for the display.
 * <p>
 * </table>
 * </blockquote>
 * 
 * <h2>Operation</h2>
 * 
 * Testing begins when the user clicks "OK" in the setup dialog. The first condition appears. Below are examples for the
 * 1D task (left) and the 2D task (right) using the three interaction methods: point-select (top), drag-select (middle), 
 * and Fitts Farm (bottom).
 * 
 * <center><p> 
 * <a href="FittsTask-3.png"><img src="FittsTask-3.png" height="300" alt="FittsTask-3"></a>
 * <a href="FittsTask-4.png"><img src="FittsTask-4.png" height="300" alt= "FittsTask-4"></a><br>
 * <a href="FittsTask-3DS.png"><img src="FittsTask-3DS.png" height="300" alt="FittsTask-3DS"></a>
 * <a href="FittsTask-4DS.png"><img src="FittsTask-4DS.png" height="300" alt= "FittsTask-4DS"></a><br>
 * <a href="FittsTask-3FF.png"><img src="FittsTask-3FF.png" height="300" alt="FittsTask-3FF"></a>
 * <a href="FittsTask-4FF.png"><img src="FittsTask-4FF.png" height="300" alt= "FittsTask-4FF"></a><br>
 * </center>
 * <p>
 * 
 * 
 * The interaction is depends on the interaction method.  For point-select testing, the user begins a sequence of trials by 
 * clicking on the highlighted target. For the 1D task, the highlight moves back
 * and forth between targets until the specified number of trials are completed. For the 2D task, the highlight moves to
 * a target on the opposite side of the layout circle. Clicking continues in an opposing pattern rotating around the
 * layout circle until all targets are selected. A sequence is finished when the first target is again highlighted and
 * selected.  For drag-select and Fitts-farm testing, target selection requires dragging the object to the highlighted target. 
 * Dragging occurs in state-two (e.g., with the primary device button depressed).
 * <p>
 * 
 * Timing begins on button-up of the first click and continues to button-up on the last click.
 * <p>
 * 
 * Errors are permitted. The only exception is that the initial click to start a sequence of trials must be inside the
 * first highlighted target.
 * <p>
 * 
 * At the end of a sequence of trials, a popup window appears showing results for the sequence ("Sequence Summary"):
 * 
 * <center><p><a href="FittsTask-6.jpg"><img src="FittsTask-6.jpg" width="300" alt= "FittsTask-6"></a></center>
 * <p>
 * 
 * After the last sequence of trials in a block, a popup window appears showing the overall results for the block (
 * "Block Summary"):
 * <center><a href="FittsTask-7.png"><img src="FittsTask-7.png" width="400" alt= "FittsTask-7"></a></center>
 * 
 * <h2>Output Data Files</h2>
 * 
 * There are three output data files: sd1, sd2, and sd3. ("sd" is for "summary data".) The data are comma-delimited for
 * easy importing into a spreadsheet or statistics program.
 * 
 * <h2>sd1 Output File</h2>
 * 
 * The sd1 file contains the summary data on a trial-by-trial basis, one line per trial. The entries are as follows:
 * 
 * <pre>
 * 
 *     "FittsTask" - application identifier
 *     Participant code - from setup dialog
 *     Condition code - from setup dialog
 *     Session code - from setup dialog
 *     Group code - from setup dialog
 *     Task type - 1D or 2D
 *     Selection method - MB, DT<i>n</i>, KB (Note: "n" is the dwell time)
 *     Interaction method - PS, DS, or FF
 *     Block code - generated automatically
*     Trial - trial number
*     A - amplitude (distance to target in pixels)
*     W - width (diameter of target in pixels)
*     Ae - effective amplitude (pixels; see below)
*     dx - delta x (pixels; see below)
*     PT - pointing time (ms)
*     ST - selection time (ms) - the time the button is down
*     MT - movement time (ms) - Note: MT = PT + ST
*     Error - 0 = hit, 1 = miss
*     TRE - target re-entries
*     TAC - task axis crossings
*     MDC - movement direction changes
*     ODC - orthogonal direction changes
*     MV - movement variability
*     ME - movement error
*     MO - movement offset
 * </pre>
 * 
 * The first eight entries are settings from the Setup dialog. The next entry, <code>Block code</code>, is generated
 * automatically as the next available block code in the filenames. The next three entries (<code>Trial</code>,
 * <code>A</code> , <code>W</code>) are the task conditions. The next six ( <code>Ae</code>, <code>dx</code>,
 * <code>PT</code>, <code>ST</code>, <code>MT</code>, <code>Error</code>) are measures of the participant's performance
 * for each trial.
 * <p>
 * 
 * <code>Ae</code> is the effective amplitude &ndash; the actual cursor distance projected on the task axis.
 * <code>Ae</code> &lt; <code>A</code> for undershoots and <code>Ae</code> &gt; <code>A</code> for overshoots.
 * <p>
 * 
 * <code>dx</code> is the delta <i>x</i> of the selection coordinates. It is normalized relative to the center of the
 * target and to the task axis. For example, <code>dx</code> = 1 is the equivalent of a one-pixel overshoot while
 * <code>dx</code> = &minus;1 is the equivalent of a one-pixel undershoot. Note that <code>dx</code> = 0 does not mean
 * selection was precisely at the centre of the target. It means selection was on the line orthogonal to the task axis
 * going through the centre of the target. This is consistent with the inherently one-dimensional nature of Fitts' law.
 * <p>
 * 
 * The last seven measures (<code>TRE</code>, <code>TAC</code>, <code>MDC</code> , <code>ODC</code>, <code>MV</code>,
 * <code>ME</code>, <code>MO</code>) capture accuracy characteristics of the cursor path during a trial. These measures
 * are fully described in <a href="http://www.yorku.ca/mack/CHI01.htm">Accuracy Measures for Evaluating Computer
 * Pointing Devices</a> by MacKenzie, Kauppinen, and Silfverberg (2001). See as well the
 * <a href="AccuracyMeasures.html">API</a> for the <code>AccuracyMeasures</code> class.
 * 
 * <h2>sd2 Output File</h2>
 * 
 * The sd2 file contains summary data on a sequence-by-sequence basis, one line per sequence. The entries are as
 * follows:
 * 
 * <pre>
 * 
 *     "FittsTask" - application identifier
 *     Participant code - from setup dialog
 *     Condition code - from setup dialog
 *     Session code - from setup dialog
 *     Group code - from setup dialog
 *     Task type - 1D or 2D
 *     Selection method - MB, DT<i>n</i>, KB (Note: "n" is the dwell time)
 *     Interaction method - PS, DS, or FF
 *     Block code - from setup dialog
*     SRC - sequence repeat count
*     Trials - number of trials in this sequence
*     A - target amplitude (pixels)
*     W - target width (diameter) (pixels)
*     ID - index of difficulty (bits)
*     Ae - effective target amplitude (pixels)
*     We - effective target width (pixels)
*     IDe - effective index of difficulty (bits)
*     PT - pointing time (ms)
*     ST - selection time (ms)
*     MT - movement time (ms)
*     ER - error rate (%)
*     TP - throughput (bits/s)
*     TRE - target re-entries
*     TAC - task axis crossings
*     MDC - movement direction changes
*     ODC - orthogonal direction changes
*     MV - movement variability
*     ME - movement error
*     MO - movement offset
 * </pre>
 * 
 * The first eight entries are settings from the Setup dialog. The next entry, <code>Block code</code>, is generate
 * automatically as the next available block code in the filenames. The next entry is <code>SRC</code> (sequence repeat
 * count), which is the number of times the sequence was repeated due the error threshold being exceeded. The next four
 * entries ( <code>Trials</code>, <code>A</code>, <code>W</code> , <code>ID</code>) are the task conditions.
 * <p>
 * 
 * The remaining entries are measures of participant behaviour, computed over a sequence of trials for the specified
 * <i>A-W</i> condition. All the values, except <i>TP</i>, are means, computed over the trials in the sequence.
 * <p>
 * 
 * <i>TP</i> is the Fitts' law throughput, in bits/s, computed over the sequence of trials. The calculation of <i>TP</i>
 * uses the <i>dx</i> values in the sd1 file (see above). The standard deviation in the <i>dx</i> values for all trials
 * in a sequence is <i>SD</i> <sub>x</sub> . This is used in the calculation of throughput as follows:
 * 
 * <blockquote> <i>W</i><sub>e</sub> = 4.133 &times; <i>SD</i><sub>x</sub>
 * <p>
 * 
 * <i>ID</i><sub>e</sub> = log<sub>2</sub>(<i>A</i><sub>e</sub> / <i>W</i> <sub>e</sub> + 1)
 * <p>
 * 
 * <i>TP</i> = <i>ID</i><sub>e</sub> / <i>MT</i>
 * <p>
 * </blockquote>
 * 
 * The actual calculation is performed using the <code>Throughput</code> class. Consult the
 * <a href="Throughput.html">API</a> for complete details.
 * 
 * <h2>sd3 Output File</h2>
 * 
 * The sd3 file contains trace data. For each trial, the on-going timestamps, <I>x</I> coordinates, and <I>y</I>
 * coordinates are collected and saved. A separate utility, <a href="FittsTrace.html">FittsTrace</a>, facilitates
 * viewing the trace data. An example trace plot is shown below. Consult <a href="FittsTrace.html">FittsTrace</a> for
 * complete details.
 * 
 * <center><p> <a href="FittsTask-11.jpg"><img src="FittsTask-11.jpg" width=300 alt="FittsTask-11"></a> </center>
 * <p>
 * 
 * The following are examples of "sd" (summary data) files:
 * 
 * <ul>
 * <li><a href="FittsTask-sd1-example.txt">sd1 example</a>
 * <li><a href="FittsTask-sd2-example.txt">sd2 example</a>
 * <li><a href="FittsTask-sd3-example.txt">sd3 example</a>
 * </ul>
 * <p>
 * 
 * Actual output files use "FittsTask" as the base filename. This is followed by the participant code, the condition
 * code, the sequence code, the group code, the task type, the selection method, the interaction method, and the block code, for example,
 * <code>FittsTask-P15-C01-S01-G01-2D-MB-PS-B01.sd1</code>.
 * <p>
 * 
 * In most cases, the sd2 data files are the primary files for analyses in an experimental evaluation. The data in the
 * sd2 files are full-precision, comma-delimited, to facilitate importing into a spreadsheet or statistics application.
 * Below is an example for the sd2 file above, after importing into Microsoft <i>Excel</i>: (click to enlarge)
 * 
 * <center><p> <a href="FittsTask-5.png"><img src="FittsTask-5.png" width=1000 alt= "FittsTask-5"></a> </center>
 * 
 * <h2>Miscellaneous</h2>
 * 
 * When using this program in an experiment, it is a good idea to terminate all other applications and disable the
 * system's network connection. This will maintain the integrity of the data collected and ensure that the program runs
 * without hesitations.
 * <p>
 * 
 * @author Scott MacKenzie, 2008-2025<br>Steven Castellucci, 2014 (<tt>.jar</tt> file generation)<br>Ramon Mas-Sans&oacute; and Maria Francesca Roig-Maim&oacute;, 2024-2025 (Drag-Select and Fitts-Farm interaction modes)
 */

public class FittsTask extends JFrame implements MouseMotionListener, MouseListener, ActionListener, KeyListener
{
	// the following avoids a "warning" with Java 1.5.0 complier (?)
	static final long serialVersionUID = 42L;

	final int ONE_D = Throughput.ONE_DIMENSIONAL;
	final int TWO_D = Throughput.TWO_DIMENSIONAL;

	final String APP_NAME = "FittsTask";
	// final String LAUNCH_MESSAGE = "Setup Required";

	// selection modes
	final int MOUSE_BUTTON = 100;
	final int DWELL = 200;
	final int KEYBOARD = 300;
	
	// interaction modes
	final int POINT_SELECT = 100;
	final int DRAG_SELECT = 200;
	final int FITTS_FARM = 300;

	final int MILLION = 1000000;
	
	
	// setup parameters (NOTE: default values are read from cfg file)
	final int PARAMETERS = 22; // must equal the number of parameters defined below
	final String[] PARTICIPANT_CODES = { "P00", "P01", "P02", "P03", "P04", "P05", "P06", "P07", "P08", "P09", "P10",
			"P11", "P12", "P13", "P14", "P15", "P16", "P17", "P18", "P19", "P20", "P21", "P22", "P23", "P24", "P25",
			"P26", "P27", "P28", "P29", "P30", "P31", "P32", "P33", "P34", "P35", "P36", "P37", "P38", "P39", "P40",
			"P41", "P42", "P43", "P44", "P45", "P46", "P47", "P48", "P49", "P50" };
	final String[] CONDITION_CODES = { "C00", "C01", "C02", "C03", "C04", "C05", "C06", "C07", "C08", "C09", "C10" };
	final String[] SESSION_CODES = { "S00", "S01", "S02", "S03", "S04", "S05", "S06", "S07", "S08", "S09", "S10", "S11",
			"S12", "S13", "S14", "S15", "S16", "S17", "S18", "S19", "S20", "S21", "S22", "S23", "S24", "S25", "S26",
			"S27", "S28", "S29", "S30" };
	final String[] GROUP_CODES = { "G00", "G01", "G02", "G03", "G04", "G05", "G06", "G07", "G08", "G09", "G10" };
	// NOTE: block code generated automatically
	final String[] TASK_TYPE = { "1D", "2D" };
	final String[] SELECTION_METHOD = { "Mouse button", "Dwell time", "Keyboard" }; // RMS - New selection method
	final String[] INTERACTION_METHOD = { "Point-select", "Drag-select", "Fitts farm" };

	final String[] DWELL_TIME = { "0", "100", "200", "300", "400", "500", "600", "700", "800", "900", "1000", "1100",
			"1200", "1300", "1400", "1500", "1600", "1700", "1800", "1900", "2000" };
	final String[] MIN_DWELL_ARC_SIZE = { "Target width", "20 pixels", "40 pixels", "60 pixels", "80 pixels",
			"100 pixels", "120 pixels", "140 pixels", "160 pixels", "180 pixels", "200 pixels" };
	final String[] NUM_TRIALS_TARGETS = { "15", "3", "5", "7", "9", "11", "13", "15", "17", "19", "21", "23", "25",
			"27", "29", "31", "33", "35", "37", "39", "41", "43", "45", "47", "49", "51", "53" };
	final String[] AMPLITUDES = { "100, 200, 400" };
	final String[] WIDTHS = { "20, 40, 80" };
	final String[] ERROR_THRESHOLD = { "50" };
	final String[] SPATIAL_HYSTERESIS = { "1.0" };
	final String[] RANDOMIZE = { "true" };
	final String[] SHOW_MARKER = { "false" };
	final String[] USE_TARGET_IMAGE = { "false" };
	final String[] AUDIO_ON = { "true" };
	final String[] BUTTON_DOWN_HIGHLIGHT = { "false" };
	final String[] MOUSE_OVER_HIGHLIGHT = { "false" };
	final String[] HIDE_CURSOR = { "false" };
	final String[] BUTTON_COLORS = { "0xffffff, 0x666666, 0xccccff, 0x802222, 0x9696fd" };
	// end setup parameters
	
	// 2025-10-30 add cursor "blank" cursor
	BufferedImage cursorImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
            cursorImg, new Point(0, 0), "blank cursor");
	
	// RMS - The images for the food (apple) and the animals to feed
		
	final String[] apples = {"apple.png", "appleandpig.png"};
	final String[] animalNames1 = {"cow.png", "chicken.png", "elephant.png", "giraffe.png", 
			"horse.png", "lion.png", "llama.png", "penguin.png", "pig.png", "rhino.png", 
			"rooster.png", "sheep.png", "tiger.png", "turkey.png", "walrus.png"}; 
	final String[] animalNames2 = {"cow2.png", "chicken2.png", "elephant2.png", "giraffe2.png", 
			"horse2.png", "lion2.png", "llama2.png", "penguin2.png", "pig2.png", "rhino2.png", 
			"rooster2.png", "sheep2.png", "tiger2.png", "turkey2.png", "walrus2.png"};
	BufferedImage[] animals1;
	BufferedImage[] animals2;
	BufferedImage apple;
    BufferedImage graphicTrial, graphicTrial2;
    // END RMS
    
	// other files needed (used when executing from the .jar file for the 1st time)
	//final String[] OTHER_FILES = { "images/targetImage.gif" };
    
    // SM 12/3/2025
	final String[] OTHER_FILES = { // NOTE targetImage.gif at end
			"apple.png", 
			"appleandpig.png", 
			"cow.png", 
			"chicken.png", 
			"elephant.png", 
			"giraffe.png", 
			"horse.png", 
			"lion.png", 
			"llama.png", 
			"penguin.png", 
			"pig.png", 
			"rhino.png", 
			"rooster.png", 
			"sheep.png", 
			"tiger.png", 
			"turkey.png", 
			"walrus.png", 
			"cow2.png", 
			"chicken2.png", 
			"elephant2.png", 
			"giraffe2.png", 
			"horse2.png", 
			"lion2.png", 
			"llama2.png", 
			"penguin2.png", 
			"pig2.png", 
			"rhino2.png", 
			"rooster2.png", 
			"sheep2.png", 
			"tiger2.png", 
			"turkey2.png", 
			"walrus2.png",
			"targetImage.gif"};

	Configuration c;
	SetupItemInfo[] sii;
	JButton setupButton;
	JButton backToGoFitts;
	JButton openAPI;
	Clip tickSound, errorSound;

	private TaskPanel taskPanel;
	private GoFittsLaunchPanel launchPanel;
	private JPanel mainPanel;
	private MessagePanel sequenceResults;
	private MessagePanel blockResults;
	private MessagePanel repeatSequence;

	private BufferedWriter bw1; // writer for sd1 file
	private BufferedWriter bw2; // writer for sd2 file
	private BufferedWriter bw3; // writer for sd3 file

	private int trialCount;
	private long movementTime;
	long pointingTime, selectingTime;
	private long buttonDownTime; // button-down time
	private long buttonUpTime; // button-up time
	private long tOld;

	int selectionMethod;
	int interactionMethod;  // RMS - new interaction method (POINT_SELECT, DRAG_SELECT or FITTS_FARM)
	String selectionMethodCode;
	String interactionMethodCode;  // RMS - new code for interaction method (PS, DS or FF)
	String cursorCode;
	boolean waitStartCircleSelect = true; // RMS - pending to select the initial dragged circle
	
	int cursorX, cursorY;		   // RMS - cursor coordinates when buttonDown
	
    //Setting the radial gradient  
    RadialGradientPaint radialGradient;  // RMS - to draw the heatmap
        
	// arguments read or developed from configuration file or setup dialog
	String participantCode;
	String conditionCode;
	String sessionCode;
	String groupCode;
	String blockCode; // developed in setup dialog from existing filenames
	private String taskType; // 1D, 2D
	private String selectionMethodString;
	private String interactionMethodString;  // RMS - String to describe the interaction method
	private String cursorString;
	private String dwellTimeString;
	private String minimumDwellArcSize;
	private int numTrialsTargets; // number of trials per target condition
	private String amplitudeString; // amplitudes, as space or comma delimited string
	private String widthString; // widths, as space or comma delimited string
	private double errorThreshold; // error rate threshold (%)
	private double hysteresis; // hysteresis factor for out-of-target detection
	private boolean randomize; // randomize target conditions (if 'yes')
	private boolean showMarker; // show "+" at center of target (if 'yes')
	private boolean useTargetImage; // use imageTarget.gif as target
	private boolean audioOn; // beep on error (if 'yes')
	private boolean buttonDownHighlight; // highlight target on button-down
	private boolean mouseOverHighlight; // highlight target on mouse-over
	private boolean hideCursor; // hide cursor (perhaps for eye tracking UI)
	private String buttonColorString; // colors to define the look of the display
	// end arguments

	// other arguments developed from config/setup arguments
	private int taskTypeCode;
	private int[] a; // target amplitude conditions
	private int[] w; // target width conditions
	int xLast, yLast;

	FittsTaskBlock block;
	ArrayList<TraceSample> traceSample;
	StringBuilder traceSamplesSequence;

	boolean buttonDown;
	private boolean inTarget = false;
	private int error;

	Timer dwellSelectionTimer; // times out at end of dwell interval
	boolean dwellOn; // start the timer!
	boolean dwellSelection; // we're using dwell-selection mode
	int dwellTime;
	boolean dwellInProgress;

	BufferedImage targetImage;

	File currentDirectory;
	
	// RMS - new variables for dragging mode
	
	private boolean draggingMode = false;    // RMS - true when dragging mode enabled
	private boolean dragging = false;        // RMS - true if moving with button pressed
	private boolean dragInProgress = false;  // RMS - true if dragging object
	private float defaultDragObjectWidth;	 // RMS - width of the object to drag
    private float touchPointRadius;			 // RMS - radius of the heatmap
    private Random random = new Random();
    
    
	FittsTask(File currentDirectoryArg)
	{
		currentDirectory = currentDirectoryArg;

		// -----------------------
		// create setup parameters
		// -----------------------

		sii = new SetupItemInfo[PARAMETERS];
		sii[0] = new SetupItemInfo(SetupItem.COMBO_BOX, "Participant code ", PARTICIPANT_CODES);
		sii[1] = new SetupItemInfo(SetupItem.COMBO_BOX, "Condition code ", CONDITION_CODES);
		sii[2] = new SetupItemInfo(SetupItem.COMBO_BOX, "Session code ", SESSION_CODES);
		sii[3] = new SetupItemInfo(SetupItem.COMBO_BOX, "Group code ", GROUP_CODES);
		sii[4] = new SetupItemInfo(SetupItem.RADIO_BUTTON, "Task type ", TASK_TYPE);
		sii[5] = new SetupItemInfo(SetupItem.RADIO_BUTTON, "Selection method ", SELECTION_METHOD);
		sii[6] = new SetupItemInfo(SetupItem.RADIO_BUTTON, "Interaction method ", INTERACTION_METHOD);  // RMS New interaction method. All "sii" renumbered
		sii[7] = new SetupItemInfo(SetupItem.COMBO_BOX, "Dwell time", DWELL_TIME);
		sii[8] = new SetupItemInfo(SetupItem.COMBO_BOX, "Minimum dwell arc size", MIN_DWELL_ARC_SIZE);
		sii[9] = new SetupItemInfo(SetupItem.COMBO_BOX, "Number of trials (1D) or Targets (2D)", NUM_TRIALS_TARGETS);
		sii[10] = new SetupItemInfo(SetupItem.TEXT_FIELD, "Amplitudes", AMPLITUDES);
		sii[11] = new SetupItemInfo(SetupItem.TEXT_FIELD, "Widths", WIDTHS);
		sii[12] = new SetupItemInfo(SetupItem.TEXT_FIELD, "Error threshold (%)", ERROR_THRESHOLD);
		sii[13] = new SetupItemInfo(SetupItem.TEXT_FIELD, "Spatial hysteresis", SPATIAL_HYSTERESIS);
		sii[14] = new SetupItemInfo(SetupItem.CHECK_BOX, "Randomize target conditions", RANDOMIZE);
		sii[15] = new SetupItemInfo(SetupItem.CHECK_BOX, "Show marker at centre of target", SHOW_MARKER);
		sii[16] = new SetupItemInfo(SetupItem.CHECK_BOX, "Use target image", USE_TARGET_IMAGE);
		sii[17] = new SetupItemInfo(SetupItem.CHECK_BOX, "Audio feedback", AUDIO_ON);
		sii[18] = new SetupItemInfo(SetupItem.CHECK_BOX, "Button-down highlight ", BUTTON_DOWN_HIGHLIGHT);
		sii[19] = new SetupItemInfo(SetupItem.CHECK_BOX, "Mouse-over highlight", MOUSE_OVER_HIGHLIGHT);
		sii[20] = new SetupItemInfo(SetupItem.CHECK_BOX, "Hide cursor", HIDE_CURSOR);
		sii[21] = new SetupItemInfo(SetupItem.COLOR_BUTTONS, "Background, Foreground, Target, Button-down, Mouse-over",
				BUTTON_COLORS);
		
		openAPI = new JButton("View API in Browser");
		setupButton = new JButton("Start");
		backToGoFitts = new JButton("Back to GoFitts");
		
		// add tooltip text (added July 10, 2023)
		openAPI.setToolTipText(GoFitts.API_LOCATION + "FittsTask.html");
		
		openAPI.setFont(new Font("sanserif", Font.BOLD, 14));
		setupButton.setFont(new Font("sanserif", Font.BOLD, 14));
		backToGoFitts.setFont(new Font("sanserif", Font.BOLD, 14));		

		// create panels for popup windows
		sequenceResults = new MessagePanel("", 24, new Color(255, 255, 215), new Color(0, 0, 153));
		blockResults = new MessagePanel("", 24, new Color(233, 255, 255), new Color(127, 32, 13));
		repeatSequence = new MessagePanel("", 28, new Color(255, 245, 255), new Color(153, 0, 0));
		
		
				
		// changes from Ramon (June 5, 2025) --> white text on black background (revert to original, SM 29/6/2025)
//		sequenceResults = new MessagePanel("", 24, new Color(0, 0, 0), new Color(255, 255, 255)); // from Ramon, June 5, 2025
//		blockResults = new MessagePanel("", 24, new Color(0, 0, 0), new Color(255, 255, 255));
//		repeatSequence = new MessagePanel("", 28, new Color(0, 0, 0), new Color(255, 255, 255));

		launchPanel = new GoFittsLaunchPanel();
		//launchPanel.launchMessage = this.LAUNCH_MESSAGE;
		
		// -------------
		// add listeners
		// -------------

		// give focus to the setupButton when the frame is activated
		this.addWindowFocusListener(new WindowAdapter()
		{
			public void windowGainedFocus(WindowEvent e)
			{
				// don't do this in KEYBOARD mode (to allow the taskPanel to have focus)
				if (!(selectionMethod == KEYBOARD))
					setupButton.requestFocusInWindow();
			}
		});

		// things to do if the window is resized
		this.addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent e)
			{
				if (taskPanel != null)
				{
					int aTmp = block.getSequence(block.getIDX()).getA();
					int wTmp = block.getSequence(block.getIDX()).getW();
					taskPanel.positionTargets(aTmp, wTmp); // reposition targets based on new size of taskPanel
				}
			}
		});

		openAPI.addActionListener(this);
		setupButton.addActionListener(this);
		backToGoFitts.addActionListener(this);

		// ------------------
		// arrange components
		// ------------------

		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(5, 5, 5, 5), new EtchedBorder()));
		mainPanel.add(launchPanel, "Center");

		JPanel buttonPanelCentre = new JPanel();
		buttonPanelCentre.add(setupButton);
		// buttonPanelCentre.add(backToGoFitts);

		JPanel buttonPanelLeft = new JPanel();
		// buttonPanelLeft.add(openAPI);

		JPanel buttonPanelRight = new JPanel();
		JLabel dummy = new JLabel("");
		dummy.setPreferredSize(openAPI.getPreferredSize());
		buttonPanelRight.add(dummy);

		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.add(buttonPanelLeft, BorderLayout.WEST);
		buttonPanel.add(buttonPanelCentre, BorderLayout.CENTER);
		buttonPanel.add(buttonPanelRight, BorderLayout.EAST);

		JPanel p = new JPanel(new BorderLayout());
		p.add(mainPanel, "Center");
		p.add(buttonPanel, "South");

		// fill screen minus task bar, see stackoverflow at...
		// https://stackoverflow.com/questions/10123735/get-effective-screen-size-from-java
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		Insets screenMax = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
		int taskBarSize = screenMax.bottom;
		// p.setLocation(d.width - getWidth(), d.height - getHeight() - taskBarSize);

		this.setContentPane(p);
		
		// RMS - to deal with pixel density. dm is the lenght of an inch in pixels
		float dm = Toolkit.getDefaultToolkit().getScreenResolution();

		defaultDragObjectWidth = dm * 0.5f;  // one half inch
        touchPointRadius = defaultDragObjectWidth*1.5f;
        
        // SM 16/3/2025 - perhaps move to actionPerformed after setup
        //initAnimals(); // RMS - init the animal images
        
        // END RMS
        
	} // end constructor

	
	/**
     * This method is called to set the width of the smallest target in a block.  The drag
     * object (circle or apple) will be 1/4 inch wide, unless the smallest target is smaller than that.  In this
     * case, the drag object will assume the width of the smallest target.
     */
	
    void setDragObjectWidth(int widthArg)
    {
        // smaller of 1/4 inch or the width of the smallest target in a block
        float width = Math.min(widthArg, defaultDragObjectWidth);

        // for the apple (when using Fitts Farm style)
        //dragObjectRectangle = new RectF(0, 0, width, width);

        // for the drag circle
        defaultDragObjectWidth = width;
    }
    
    // RMS Init the arrays with the images of the animals and the apple
    private void initAnimals()
    {
    	//System.out.println("Path: "+System.getProperty("user.dir"));
    	animals1 = new BufferedImage[animalNames1.length];
    	animals2 = new BufferedImage[animalNames2.length];
    	for (int i=0; i<animalNames1.length; i++)
    	{
    		try {
				animals1[i] = ImageIO.read(new File("images" + File.separator + animalNames1[i]));
	    		animals2[i] = ImageIO.read(new File("images" + File.separator + animalNames2[i]));
	        	apple = ImageIO.read(new File("images" + File.separator + apples[0]));
			} catch (IOException e) {
				e.printStackTrace();
			}

    	}
    	nextRandomGraphic();
    }
    
    // RMS get a random animal
    void nextRandomGraphic()
    {
        int i = random.nextInt(animals1.length);
        graphicTrial = animals1[i];    // RMS animal when cursor not inside target
        graphicTrial2 = animals2[i];   // RMS animal when cursor inside target
    }
    
    // END RMS
    
	// called after the user clicks "OK" in the setup dialog
	private void doNewBlock(Configuration c)
	{
		//playSound(null); // attempt to avoid audio delay on first selection (doesn't work!)		
		
		//System.out.println("Got here! -- playSound");
		//GoFitts.showError("Got here!!!");
		
		Color[] buttonColor = null;
		
		// get/convert setup parameters; put in try-loop in case of user input errors in setup dialog
		try
		{
			// initialize setup parameters
			participantCode = c.getConfigurationParameter(0);
			conditionCode = c.getConfigurationParameter(1);
			sessionCode = c.getConfigurationParameter(2);
			groupCode = c.getConfigurationParameter(3);
			// Note: block code generated automatically (see below)
			taskType = c.getConfigurationParameter(4);
			selectionMethodString = c.getConfigurationParameter(5);
			interactionMethodString = c.getConfigurationParameter(6); // RMS - get interactionMethodString from configuration file
																	  // RMS - all indexes have been updated from here on (from 6-19) to (7-20)
			dwellTimeString = c.getConfigurationParameter(7);
			minimumDwellArcSize = c.getConfigurationParameter(8);
			numTrialsTargets = Integer.parseInt(c.getConfigurationParameter(9));
			amplitudeString = c.getConfigurationParameter(10);
			widthString = c.getConfigurationParameter(11);
			errorThreshold = Double.parseDouble(c.getConfigurationParameter(12));
			hysteresis = Double.parseDouble(c.getConfigurationParameter(13));
			randomize = Boolean.parseBoolean(c.getConfigurationParameter(14));
			showMarker = Boolean.parseBoolean(c.getConfigurationParameter(15));
			useTargetImage = Boolean.parseBoolean(c.getConfigurationParameter(16));
			audioOn = Boolean.parseBoolean(c.getConfigurationParameter(17));
			buttonDownHighlight = Boolean.parseBoolean(c.getConfigurationParameter(18));
			mouseOverHighlight = Boolean.parseBoolean(c.getConfigurationParameter(19));
			hideCursor = Boolean.parseBoolean(c.getConfigurationParameter(20));
			buttonColorString = c.getConfigurationParameter(21);

			// some parameters are developed from the above

			String[] s = amplitudeString.split(",");
			a = new int[s.length];
			for (int i = 0; i < a.length; ++i)
				a[i] = Integer.parseInt(s[i].trim());

			s = widthString.split(",");
			w = new int[s.length];
			
			
			int minWidth = MILLION;  // RMS - we need the minWidth to compute the width of the dragging object
			 
			for (int i = 0; i < w.length; ++i)
			{
				w[i] = Integer.parseInt(s[i].trim());
				if (w[i]<minWidth) minWidth = w[i];   // RMS - get the minWidth
			}

			setDragObjectWidth(minWidth); // RMS - set the width of the drag object
			
			s = buttonColorString.split(",");
			buttonColor = new Color[s.length];
			for (int i = 0; i < buttonColor.length; ++i)
				buttonColor[i] = new Color(Integer.decode(s[i].trim()));

			dwellTime = Integer.parseInt(dwellTimeString);

			// commented out -- seems to cause problems (something to do with the resource "path")
			// Oct 20, 2020 -- uncommented out -- trying to get this to work!
			//targetImage = Toolkit.getDefaultToolkit().getImage(FittsTask.class.getResource("targetImage.gif"));
			
			// RMS use bufferedImage instead of Image
    		try {
    			//targetImage = ImageIO.read(new File(OTHER_FILES[0]));
    			int idxEnd = OTHER_FILES.length - 1;
    			targetImage = ImageIO.read(new File("images" + File.separator + OTHER_FILES[idxEnd])); // SM 12/3/2025
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (NumberFormatException e)
		{
			GoFitts.showError("Error parsing setup parameters!  Try again!");
			return;
		}
		
		// 2025-10-30
		//System.out.println("doNewBlock");
		if (hideCursor) {
			//System.out.println("Got here!!!");
			this.setCursor(blankCursor);
		}

		errorSound = initSound("blip1.wav");
		tickSound = initSound("tick.wav");
		//playSound(tickSound);

		switch (selectionMethodString)
		{
			case "Mouse button":
				selectionMethod = MOUSE_BUTTON;
				selectionMethodCode = "MB";
				break;
			case "Dwell time":
				selectionMethod = DWELL;
				selectionMethodCode = "DT" + dwellTime;
				break;
			case "Keyboard":
				selectionMethod = KEYBOARD;
				selectionMethodCode = "KB";
		}
		
		
		 // RMS - Switch added to set interactionMethod and interactionMethodCode from panel

		switch (interactionMethodString)
		{
			case "Point-select":
				interactionMethod = POINT_SELECT;
				interactionMethodCode = "PS";
				break;
			case "Drag-select":
				interactionMethod = DRAG_SELECT;
				interactionMethodCode = "DS";
				break;
			case "Fitts farm":
				interactionMethod = FITTS_FARM;
				interactionMethodCode = "FF";
		}
		// END RMS
		
		if (hideCursor)
			cursorCode = "Hide";
		else
			cursorCode = "Show";
	
		
		taskTypeCode = taskType.equals("1D") ? ONE_D : TWO_D;
		int numberOfSequences = a.length * w.length;

		// create array of cursor trace samples (built-up as interaction progresses)
		traceSample = new ArrayList<TraceSample>();

		// the actual data that will be written to the sd3 file at the end of each sequence
		traceSamplesSequence = new StringBuilder();

		// create a new instance of FittsTaskTwoBlock
		block = new FittsTaskBlock(taskTypeCode, numberOfSequences, numTrialsTargets, a, w, randomize);

		// generate a unique A-W condition (regardless of the sequence index)
		int idxA = block.getNextSequenceIndex() / w.length;
		int idxW = block.getNextSequenceIndex() % w.length;

		// misc initializations
		trialCount = 0;
		buttonUpTime = 0;

		// determine next available block code and baseFileName for output data files
		String baseFileName = GoFitts.getBaseFileName(APP_NAME, participantCode, conditionCode, sessionCode, groupCode,
				taskType, selectionMethodCode, interactionMethodCode, cursorCode);   // RMS - interactionMethodCode added as parameter
		blockCode = baseFileName.split("-")[9]; // NOTE: block code is at idx = 9 in tokenized baseFileName

		// open BufferedWriters for output data files
		bw1 = GoFitts.openBufferedWriter(baseFileName + ".sd1");
		bw2 = GoFitts.openBufferedWriter(baseFileName + ".sd2");
		bw3 = GoFitts.openBufferedWriter(baseFileName + ".sd3");
		
		// 2025-10-30 --> "Cursor" added
		// sd1 header
		String sd1Header = "App,Participant,Condition,Session,Group,TaskType,SelectionMethod,InteractionMethod,Cursor,Block,Trial,"
				+ FittsTaskTrial.getTrialHeader() + System.lineSeparator();  // RMS InteractionMethod added

		// sd2 header
		String sd2Header = "App,Participant,Condition,Session,Group,TaskType,SelectionMethod,InteractionMethod,Cursor,Block,"
				+ FittsTaskBlock.getBlockHeader() + System.lineSeparator(); // RMS InteractionMethod added

		// sd3 header
		String sd3Header = "TRACE DATA" + System.lineSeparator();
		sd3Header += "App,Participant,Condition,Session,Group,TaskType,SelectionMethod,InteractionMethod,Block,Sequence,A,W,Trial,from_x,from_y,to_x,to_y,{t_x_y}"
				+ System.lineSeparator(); // RMS InteractionMethod added

		// write header lines to output data files
		GoFitts.writeData(bw1, sd1Header);
		GoFitts.writeData(bw2, sd2Header);
		GoFitts.writeData(bw3, sd3Header);

		// disable buttons in button panel at bottom of task panel GUI
		openAPI.setEnabled(false);
		setupButton.setEnabled(false);
		backToGoFitts.setEnabled(false);		

		// remove launchPanel and replace with taskPanel
		mainPanel.removeAll();

		// create task panel and add to main panel
		taskPanel = new TaskPanel();
		mainPanel.add(taskPanel, BorderLayout.CENTER);

		// configure size of task panel and provide block-specific parameters
		taskPanel.setSize(new Dimension(mainPanel.getWidth(), mainPanel.getHeight())); // a better way?
		taskPanel.setBlockParameters(taskTypeCode, a[idxA], w[idxW], numberOfSequences, buttonColor);

		// RMS - "if" added to set draggingMode to true or false
		if (interactionMethodString.equals("Point-select"))  
		{
			draggingMode = false;
			switch (selectionMethod)
			{
				case MOUSE_BUTTON:
					taskPanel.addMouseMotionListener(this);
					taskPanel.addMouseListener(this);
					break;
	
				case DWELL:
					if (dwellTime > 0)
					{
						dwellSelectionTimer = new Timer(dwellTime, this);
						dwellSelectionTimer.setRepeats(false); // timer fires once only
					}
	
					taskPanel.initDwellSelection(dwellTime);
					taskPanel.addMouseMotionListener(this);
					// NOTE: no MouseListener in dwell mode
					break;
	
				case KEYBOARD:
					taskPanel.addMouseMotionListener(this);
					// NOTE: no "mouse listener" in keyboard mode
					taskPanel.addKeyListener(this);
					taskPanel.setFocusable(true);
					taskPanel.requestFocusInWindow();
			}
		}
		else
		{   // RMS - if dragging, both mouseMotionListener and mouseListener are needed
			draggingMode = true;
			// To detect both dragging and press/release mouse button
			taskPanel.addMouseMotionListener(this);
			taskPanel.addMouseListener(this);
			taskPanel.sourceTargetIndex = taskPanel.targetOrder[taskPanel.targetOrder.length-1];
		}
		// RMS END
		
		mainPanel.revalidate();

	} // end newBlock

	@Override
	public void keyPressed(KeyEvent ke)
	{
		// long now = ke.getWhen();
		long now = System.nanoTime();
		int key = ke.getKeyCode();

		if (key == KeyEvent.VK_J || key == KeyEvent.VK_F)
		{
			if (trialCount > 0)
			{
				int x = MouseInfo.getPointerInfo().getLocation().x - taskPanel.getLocationOnScreen().x;
				int y = MouseInfo.getPointerInfo().getLocation().y - taskPanel.getLocationOnScreen().y;
				traceSample.add(new TraceSample((now - tOld) / MILLION, x, y)); // last sample
				xLast = x;
				yLast = y;
			}
			buttonDownTime = now;
		}
	}

	@Override
	public void keyReleased(KeyEvent ke)
	{
		// long now = ke.getWhen();
		long now = System.nanoTime();
		int key = ke.getKeyCode();

		if (key == KeyEvent.VK_J || key == KeyEvent.VK_F)
		{
			if (trialCount > 0)
			{
				int x = MouseInfo.getPointerInfo().getLocation().x - taskPanel.getLocationOnScreen().x;
				int y = MouseInfo.getPointerInfo().getLocation().y - taskPanel.getLocationOnScreen().y;
				traceSample.add(new TraceSample((now - tOld) / MILLION, x, y)); // last sample
				xLast = x;
				yLast = y;
			}
			doSelect(now, xLast, yLast);
			taskPanel.repaint();
		}
	}

	@Override
	public void keyTyped(KeyEvent ke)
	{
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		// long now = e.getWhen();
		long now = System.nanoTime();
		if (!draggingMode) // RMS - when in dragging mode
		{
			if (source == dwellSelectionTimer) // we are in dwell mode... timeout! (BTW, dwellTime > 0)
			{
				// FOG experiment output
				//System.out.println("]s");
				//System.out.flush();
				
				dwellInProgress = false;
				buttonDown = false;
				buttonUpTime = now;
	
				if (trialCount == 0)
					tOld = now; // beginning of first trial (we need this here for dwell selection)
	
				// grab one last trace sample for "selection" in dwell mode (must do this here, not in mouse listener)
				if (trialCount > 0)
				{
					int x = MouseInfo.getPointerInfo().getLocation().x - taskPanel.getLocationOnScreen().x;
					int y = MouseInfo.getPointerInfo().getLocation().y - taskPanel.getLocationOnScreen().y;
					traceSample.add(new TraceSample((now - tOld) / MILLION, x, y));
					xLast = x;
					yLast = y;
				}
				doSelect(now, xLast, yLast);
				return;
			}
		}

		if (source == openAPI)
		{
			boolean okReturned = GoFitts.openBrowser("index.html?FittsTask.html");
			if (!okReturned)
			{
				GoFitts.showError("Error launching browser");
				return;
			}
		}

		else if (source == setupButton)
		{			
			// open setup dialog to parameters for this launch of app
			Configuration c = GoFitts.doSetup(this, APP_NAME, sii, OTHER_FILES);		

			if (c == null)
				return; // an error message popped up from within doSetup
			
			// SM 16/3/2025 - move here from constructor (since files aren't available until extracted from JAR file)
			initAnimals(); // RMS - init the animal images

			// do prep things for a new block of trials
			doNewBlock(c);
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
	}

	// -----------------------------------------
	// implement MouseMotionListener methods (2)
	// -----------------------------------------

	public void mouseDragged(MouseEvent me)
	{
		int x = me.getX();
		int y = me.getY();
		long now = System.nanoTime();

		// RMS dragging in on
		//System.out.println("Mouse dragged");
		dragging = true;
		cursorX = x;
		cursorY = y;
		taskPanel.repaint();
		// RMS END
		
		if (trialCount > 0)
			traceSample.add(new TraceSample((now - tOld) / MILLION, x, y));

		xLast = x;
		yLast = y;
	}

	public void mouseMoved(MouseEvent me)
	{
		int x = me.getX();
		int y = me.getY();
		long now = System.nanoTime();
		
		//System.out.println("Mouse moved");
		if (draggingMode) return; // RMS - Do nothing if in dragging mode

		// avoid weird exception if mouse event before panel exists
		if (taskPanel == null)
			return;

		if (trialCount > 0)
			traceSample.add(new TraceSample((now - tOld) / MILLION, x, y));

		// determine if cursor is inside target (NOTE: the logic includes hysteresis)
		if (!inTarget && taskPanel.inTarget(x, y, 1.0))
		{
			inTarget = true; // cursor has entered "actual" target
			if (selectionMethod == MOUSE_BUTTON)
			{
				// FOG experiment output
				//System.out.println("mo");
				//System.out.flush();
			}
		} else if (inTarget && !taskPanel.inTarget(x, y, hysteresis))
		{
			inTarget = false; // cursor has exited target + hysteresis zone, if any)
			if (selectionMethod == MOUSE_BUTTON)
			{
				// FOG experiment output
				//System.out.println("me");
				//System.out.flush();
			}
		}

		if (selectionMethod == DWELL)
		{
			if (dwellTime == 0) // first-entry selection mode (no timer is used)
			{
				if (inTarget)
				{
					// in first-entry select mode, the cursor entering the target is like a "button down" action
					buttonDown = true; // hmmm! Not sure. Really, we've got an instantaneous button down-up
					buttonDownTime = now;
					buttonUpTime = now; // maybe this is correct --> check times! (Looks good!)

					// if dwellTime = 0, the cursor entering the target is ALSO like a "button up" action
					doSelect(now, x, y);

				} else
					buttonDown = false;

			} else // dwellTime > 0
			{
				taskPanel.setInTarget(inTarget);
				if (inTarget)
				{
					if (!dwellInProgress)
					{
						// FOG experiment output
						//System.out.println("[");
						//System.out.flush();

						// start the dwell timer when the cursor enters the target
						dwellSelectionTimer.start();
						dwellInProgress = true;

						// in dwell-select mode, the cursor entering the target is like a "button down" action
						buttonDown = true;
						buttonDownTime = now;
					}

				} else if (dwellSelectionTimer.isRunning())
				{
					dwellSelectionTimer.stop(); // cursor has left the target
					dwellInProgress = false;
					
					// FOG experiment output
					//System.out.println("]c");
					//System.out.flush();
				}
			}
		}
		xLast = x;
		yLast = y;
		taskPanel.repaint();
	}

	// -----------------------------------
	// implement MouseListener methods (5)
	// -----------------------------------

	public void mouseClicked(MouseEvent me)
	{
		//System.out.println("Mouse clicked");
	}

	public void mouseEntered(MouseEvent me)
	{
		//System.out.println("Mouse entered");
	}

	public void mouseExited(MouseEvent me)
	{
		//System.out.println("Mouse exited");
	}

	// a mouse button was pressed
	public void mousePressed(MouseEvent me)
	{
		int x = me.getX();
		int y = me.getY();
		long now = System.nanoTime();
		
		//System.out.println("Mouse pressed");
		
		cursorX = x;
		cursorY = y;
		
		
		
		if (draggingMode)
		{
			if (waitStartCircleSelect)
			{
				if (taskPanel.fromTarget.contains(cursorX, cursorY))
				{
					dragInProgress = true; 
					double width = defaultDragObjectWidth;
					taskPanel.draggedObject = new Rectangle2D.Double(cursorX, cursorY, width, width);
					waitStartCircleSelect = false;
					tOld = now;
				}
			}
			return;
		}
				
		buttonDown = true;
		buttonDownTime = now;

		// avoid weird exception if mouse event before panel exists
		if (taskPanel == null)
			return;

		// determine if cursor is inside target (NOTE: the logic includes hysteresis)
		if (!inTarget && taskPanel.inTarget(x, y, 1.0))
		{
			inTarget = true; // cursor has entered "actual" target
			
			// FOG experiment output
			//System.out.println("mo");
			//System.out.flush();

		} else if (inTarget && !taskPanel.inTarget(x, y, hysteresis))
		{
			inTarget = false; // cursor has exited target + hysteresis zone, if any)
			
			// FOG experiment output
			//System.out.println("me");
			//System.out.flush();
		}

		if (trialCount > 0)
			traceSample.add(new TraceSample((now - tOld) / MILLION, x, y));

		xLast = x;
		yLast = y;
		taskPanel.repaint();
	}

	// ===========================
	// M O U S E _ R E L E A S E D
	// ===========================

	public void mouseReleased(MouseEvent me)
	{
		int x = me.getX();
		int y = me.getY();
		long now = System.nanoTime();

		buttonDown = false;
		buttonUpTime = now;
		
		if (trialCount > 0)
			traceSample.add(new TraceSample((now - tOld) / MILLION, x, y));
		
		// RMS - to control dragging
		//System.out.println("Mouse released");

		if (draggingMode)
		{
			if (dragInProgress)
			{
				if (taskPanel.inTarget(x, y, 1.0))   // inside target
				{
					inTarget = true;	
				}
				else
				{
					inTarget = false;
				}
				
				//*RMS*//
				if (inTarget) {
					buttonDown = true;
					buttonDownTime = now;
					buttonDown = false; // just to have in mind a click-like action is done
					buttonUpTime = now;
				}
				//**//
				doSelect(now, x, y); 

				xLast = x;
				yLast = y;			
			}
			dragging = false;
			dragInProgress = false;
			waitStartCircleSelect = true;
			taskPanel.repaint();
			return;
		}
		// RMS END
		
		// determine if cursor is inside target (NOTE: the logic includes the hysteresis zone, if any)
		if (!inTarget && taskPanel.inTarget(x, y, 1.0))
		{
			inTarget = true;
			
			// FOG experiment output
			// System.out.println("mo");
			// System.out.flush();
			
		} else if (inTarget && !taskPanel.inTarget(x, y, hysteresis))
		{
			inTarget = false;
			
			// FOG experiment output
			//System.out.println("me");
			//System.out.flush();
		}

		// FOG experiment output
		//System.out.println("ms");
		//System.out.flush();
		
		doSelect(now, x, y); 

		xLast = x;
		yLast = y;
		taskPanel.repaint();

	} // end mouseReleased

	private void doSelect(long now, int xArg, int yArg)
	{
		// do not allow a sequence to begin unless the first selection is inside the start target
		if (trialCount == 0 && !inTarget)
			return;

		// we're at the beginning of the 1st trial; set startOfSequenceTime now
		if (trialCount == 0)
			tOld = now;

		// each time we get here, we've got a new buttonUp time
		buttonUpTime = now; // new button up

		// compute MT = PT + ST for this trial
		movementTime = now - tOld;
		pointingTime = movementTime - (buttonUpTime - buttonDownTime);
		selectingTime = movementTime - pointingTime;
		if (!draggingMode) tOld = now;

		movementTime /= MILLION;
		pointingTime /= MILLION;
		selectingTime /= MILLION;

		error = inTarget ? 0 : 1;
		if (audioOn)
			if (error == 1)
				playSound(errorSound);
			else
				playSound(tickSound);

		// we've got what we need from inTarget; set it now to false since we're advancing to the next trial
		inTarget = false;

		if (selectionMethod == MOUSE_BUTTON)
		{			
			// FOG experiment output
			//System.out.println("me");
			//System.out.flush();
		}

		taskPanel.setInTarget(inTarget);

		if (trialCount > 0) // process trial (first click/select doesn't count: that's the beginning of the 1st trial)
			processTrial();

		if (trialCount < numTrialsTargets) // not end of sequence; prepare for next trial
		{
			// 1st sample of next trial begins at same x,y but with t = 0
			
			if (!draggingMode) traceSample.add(new TraceSample(0, xArg, yArg));

			++trialCount;
			taskPanel.advanceActiveTarget(trialCount);

		} else // end of sequence; first see if it needs to be repeated
		{
			FittsTaskSequence sequence = block.getSequence(block.getIDX());
			sequence.computeSequenceSummaryStats();
			double errorRate = sequence.getErrorRate();

			if (errorRate > errorThreshold) // sequence must be repeated
			{
				sequence.incrementSequenceRepeatCount();
				String s = sequence.getRepeatSequenceMessage();
				repeatSequence.setText(s);
				JOptionPane.showMessageDialog(this, repeatSequence, "Repeat Sequence", JOptionPane.INFORMATION_MESSAGE);

				traceSamplesSequence = new StringBuilder();
				trialCount = 0;
				int idxA = block.getNextSequenceIndex() / w.length;
				int idxW = block.getNextSequenceIndex() % w.length;
				taskPanel.setSequenceParameters(a[idxA], w[idxW], block.getIDX(), block.getNumberOfSequences());
				taskPanel.resetActiveTarget();

			} else // sequence ended OK; no need to repeat
			{
				doEndOfSequence(sequence);

				// Now, the question is... Are we at the end of the block?
				if (block.lastSequence()) // Yes! End of last sequence (done!)
				{
					doEndOfBlock();

				} else // No! Prepare for the next sequence
				{
					doPrepareForNextSequence();
				}
			}
		}
	}

	void processTrial()
	{
		FittsTaskTrial trial = block.getSequence(block.getIDX()).getTrial(trialCount - 1);

		// compute indices of "from" and "to" targets, with wrap around to avoid crash
		int idxFrom = (trialCount - 1);
		int idxTo = trialCount;

		// funny tweak needed because of 1D vs. 2D and odd number of targets
		if (trialCount == numTrialsTargets)
			idxTo = taskType.equals("1D") ? 1 : 0;

		trial.setXFrom(taskPanel.centerPoint[taskPanel.targetOrder[idxFrom]].x);
		trial.setYFrom(taskPanel.centerPoint[taskPanel.targetOrder[idxFrom]].y);
		trial.setXTo(taskPanel.centerPoint[taskPanel.targetOrder[idxTo]].x);
		trial.setYTo(taskPanel.centerPoint[taskPanel.targetOrder[idxTo]].y);

		trial.setXSelect(traceSample.get(traceSample.size() - 1).point.x);
		trial.setYSelect(traceSample.get(traceSample.size() - 1).point.y);

		// set the Ae and deltaX for the trial (let the Throughput class do the work)
		trial.setAe(Throughput.getAe(new Point2D.Double(trial.xFrom, trial.yFrom),
				new Point2D.Double(trial.xTo, trial.yTo), new Point2D.Double(trial.xSelect, trial.ySelect)));
		trial.setDx(Throughput.getDeltaX(new Point2D.Double(trial.xFrom, trial.yFrom),
				new Point2D.Double(trial.xTo, trial.yTo), new Point2D.Double(trial.xSelect, trial.ySelect)));

		trial.setPT(pointingTime);
		trial.setST(selectingTime);
		trial.setMT(movementTime);
		trial.setErr(error);

		// give to trial object (so accuracy measures can be computed)
		trial.setTraceSamples(traceSample);

		// ==================================
		// collect trace data at end of trial
		// ==================================

		// lead-in stuff for sd3 file (will change trial to trial)
		StringBuilder leadin = new StringBuilder();
		leadin.append(APP_NAME).append(',');
		leadin.append(participantCode).append(',');
		leadin.append(conditionCode).append(',');
		leadin.append(sessionCode).append(',');
		leadin.append(groupCode).append(',');
		leadin.append(taskType).append(',');
		leadin.append(selectionMethodCode).append(',');
		leadin.append(interactionMethodCode).append(',');  // RMS - store also interactionMethodCode
		leadin.append(blockCode).append(',');
		leadin.append(block.getIDX()).append(',');
		leadin.append(Math.round(trial.amplitude)).append(',');
		leadin.append(Math.round(trial.width)).append(',');
		leadin.append(trialCount - 1).append(','); // "- 1" converts count to index
		
		// Jan. 14, 2023: add Locale.US to prevent comma decimal place in countries like France
//		leadin.append(String.format(Locale.US, "%.1f", trial.xFrom)).append(',');
//		leadin.append(String.format(Locale.US, "%.1f", trial.yFrom)).append(',');
//		leadin.append(String.format(Locale.US, "%.1f", trial.xTo)).append(',');
//		leadin.append(String.format(Locale.US, "%.1f", trial.yTo)).append(',');
		leadin.append(Math.round(trial.xFrom)).append(',');
		leadin.append(Math.round(trial.yFrom)).append(',');
		leadin.append(Math.round(trial.xTo)).append(',');
		leadin.append(Math.round(trial.yTo)).append(',');

		// transfer traceSample ArrayList data to traceSamplesSequence StringBuilder
		traceSamplesSequence.append(leadin);
		traceSamplesSequence.append("t=").append(',');
		for (int i = 0; i < traceSample.size(); ++i)
			traceSamplesSequence.append(traceSample.get(i).t).append(',');
		traceSamplesSequence.append(System.lineSeparator());

		traceSamplesSequence.append(leadin);
		traceSamplesSequence.append("x=").append(',');
		for (int i = 0; i < traceSample.size(); ++i)
			traceSamplesSequence.append((traceSample.get(i).point.x)).append(',');
		traceSamplesSequence.append(System.lineSeparator());

		traceSamplesSequence.append(leadin);
		traceSamplesSequence.append("y=").append(',');
		for (int i = 0; i < traceSample.size(); ++i)
			traceSamplesSequence.append(traceSample.get(i).point.y).append(',');
		traceSamplesSequence.append(System.lineSeparator());

		// prepare trace sample array for next trial
		traceSample = new ArrayList<TraceSample>();
	}

	void doEndOfSequence(FittsTaskSequence sequence)
	{
		// leadin stuff for sd1 file
		StringBuilder leadin = new StringBuilder();
		leadin.append(APP_NAME).append(',');
		leadin.append(participantCode).append(',');
		leadin.append(conditionCode).append(',');
		leadin.append(sessionCode).append(',');
		leadin.append(groupCode).append(',');
		leadin.append(taskType).append(',');
		leadin.append(selectionMethodCode).append(',');
		leadin.append(interactionMethodCode).append(',');  // RMS - InteractionMethodCode information
		leadin.append(cursorCode).append(',');
		leadin.append(blockCode).append(',');

		// write trial data (sd1) and trace data (sd3) at end of sequence

		// first, prepare sd1 trial data (NOTE: sd3 data is ready and waiting in traceSamplesSequence)
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < sequence.getNumberOfTrials(); ++i)
		{
			sb.append(leadin);
			sb.append(sequence.getTrialData(i));
			sb.append(System.lineSeparator());
		}

		// write the sd1 and sd3 data at the end of each sequence
		GoFitts.writeData(bw1, sb.toString());
		GoFitts.writeData(bw3, traceSamplesSequence.toString());

		// present sequence results in popup window
		sequenceResults.setText(sequence.getSequenceSummary());
		JOptionPane.showMessageDialog(this, sequenceResults, "Sequence summary", JOptionPane.INFORMATION_MESSAGE);
	}

	void doPrepareForNextSequence()
	{
		trialCount = 0;
		block.nextSequence();
		traceSamplesSequence = new StringBuilder();

		int idxA = block.getNextSequenceIndex() / w.length;
		int idxW = block.getNextSequenceIndex() % w.length;

		taskPanel.setSequenceParameters(a[idxA], w[idxW], block.getIDX(), block.getNumberOfSequences());
		taskPanel.resetActiveTarget();
		taskPanel.repaint();
	}

	// end of block; write data to sd2 file and return to launch panel
	void doEndOfBlock()
	{
		block.buildArrays();

		// collect together the sd2 data for the entire block
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < block.getNumberOfSequences(); ++i)
		{
			sb.append(APP_NAME).append(',');
			sb.append(participantCode).append(',');
			sb.append(conditionCode).append(',');
			sb.append(sessionCode).append(',');
			sb.append(groupCode).append(',');
			sb.append(taskType).append(',');
			sb.append(selectionMethodCode).append(',');
			sb.append(interactionMethodCode).append(',');
			sb.append(cursorCode).append(',');
			sb.append(blockCode).append(',');
			sb.append(block.getSequenceData(i)); // rest of data, pre-formatted
			sb.append(System.lineSeparator());
		}

		// write the sd2 data at the end of the block
		GoFitts.writeData(bw2, sb.toString());

		// close the data files
		GoFitts.closeBufferedWriter(bw1);
		GoFitts.closeBufferedWriter(bw2);
		GoFitts.closeBufferedWriter(bw3);

		// present block results in popup window
		blockResults.setText(block.getBlockSummary());
		JOptionPane.showMessageDialog(this, blockResults, "Block summary", JOptionPane.INFORMATION_MESSAGE);

		// return control to launch panel (the user can quit or do another block via the setup dialog)
		mainPanel.removeAll();
		mainPanel.add(launchPanel, BorderLayout.CENTER);
		launchPanel.setSize(new Dimension(mainPanel.getWidth(), mainPanel.getHeight())); // a kluge!
		mainPanel.revalidate();
		openAPI.setEnabled(true);
		setupButton.setEnabled(true);
		backToGoFitts.setEnabled(true);
	}

	// --------------------
	// define inner classes
	// --------------------

	class MessagePanel extends JPanel
	{
		// the following avoids a "warning" with Java 1.5.0 complier (?)
		static final long serialVersionUID = 42L;

		private String text;
		private Font f;
		private FontMetrics fm;
		private int width, height;

		MessagePanel(String sArg, int fontSize, Color backArg, Color foreArg)
		{
			f = new Font("Monospaced", Font.BOLD, fontSize);
			this.setFont(f);
			fm = this.getFontMetrics(f);
			this.setText(sArg);
			this.setForeground(foreArg);
			this.setBackground(backArg);
			this.setBorder(BorderFactory.createLineBorder(Color.gray));
			this.setPanelSize();
		}

		private void setPanelSize()
		{
			StringTokenizer st = new StringTokenizer(text, "\n");

			// height is a simple function of the number of lines
			height = fm.getHeight() * st.countTokens();
			height += 20; // extra space above and below

			// for width, find the widest line
			width = 0;
			while (st.hasMoreTokens())
			{
				int tmp = fm.stringWidth(st.nextToken());
				width = width < tmp ? tmp : width;
			}
			width += 30; // extra space, left and right
			this.setPreferredSize(new Dimension(width, height));
		}

		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;

			FontMetrics fm = g2.getFontMetrics();
			int h = fm.getHeight(); // pixel height of characters (for given font)

			StringTokenizer st = new StringTokenizer(text, "\n");
			int count = st.countTokens();

			for (int i = 0; i < count; ++i)
				g2.drawString(st.nextToken(), 10, h + (i * h));
		}

		public void setText(String textArg)
		{
			text = textArg;
			setPanelSize();
		}
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

	// **********************************
	// A class for presenting the targets
	// **********************************

	class TaskPanel extends JPanel implements ActionListener
	{
		// the following avoids a "warning" with Java 1.5.0 complier (?)
		static final long serialVersionUID = 42L;

		final Stroke TARGET_STROKE = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		final int ARC_STROKE_THICKNESS = 10;
		final Stroke ARC_STROKE = new BasicStroke(ARC_STROKE_THICKNESS, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		final Color PROGRESS_MESSAGE_COLOR = new Color(127, 0, 0);
		final Font PROGRESS_MESSAGE_FONT = new Font("SannsSerif", Font.ITALIC, 24);
		final double DWELL_PROGRESS_INTERVAL = 20; // ms
		final double HEIGHT_RATIO = 0.9; // height of rectangle, relative to task panel height

		private String progress1, progress2;
		private Ellipse2D.Double[] target2D;
		private Rectangle2D.Double[] target1D;
		private Rectangle2D.Double fromTarget;    // RMS - target to be dragged
		private Rectangle2D.Double draggedObject; // RMS - object being dragged
		
		int activeTargetIndex = -1;
		int sourceTargetIndex = -1;
		public Point2D.Double[] centerPoint;
		public int[] targetOrder;
		public int mode;

		Color foregroundColor;
		Color backgroundColor;
		Color targetColor;
		Color buttonDownColor;
		Color mouseOverColor;
		Color mouseOverColor2; // for extended arc in dwell-select mode

		boolean dwellSelection, dwellOn;
		Timer dwellProgressTimer;
		double arcAngle;
		double arcAngleIncrement; // the amount the arc increases on each timeout of dwellProgressTimer

		Image targetImageScaled;

		TaskPanel()
		{
			mode = TWO_D; // default (perhaps changed through setup)
		}

		private void initDwellSelection(int dwellTime)
		{
			dwellSelection = true;
			dwellOn = false;

			// we do *not* need a progress timer when using dwell = 0 (i.e., first-entry selection)
			if (dwellTime == 0)
				return;

			dwellProgressTimer = new Timer((int) DWELL_PROGRESS_INTERVAL, this);

			// Found 560 by trial-and-error. I don't get it. Should be 360 for one revolution of the target.
			arcAngleIncrement = -560.0 / (dwellTime / DWELL_PROGRESS_INTERVAL);
			arcAngle = 0;
		}

		// process timer events for dwell-time selection (advance the arc)
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == dwellProgressTimer)
			{
				// calculate new arc angle
				arcAngle += arcAngleIncrement;

				// update the arc
				this.repaint();
			}
		}

		private void setBlockParameters(int modeArg, int aArg, int wArg, int totalSequencesArg, Color[] colorArg)
		{
			mode = modeArg;

			backgroundColor = colorArg[0];
			foregroundColor = colorArg[1];
			targetColor = colorArg[2];
			buttonDownColor = colorArg[3];
			mouseOverColor = colorArg[4];

			// extended arc color (lighten a bit, and partially transparent)
			float red = mouseOverColor.getRed() + (256 - mouseOverColor.getRed()) / 2.0f;
			float green = mouseOverColor.getGreen() + (256 - mouseOverColor.getGreen()) / 2.0f;
			float blue = mouseOverColor.getBlue() + (256 - mouseOverColor.getBlue()) / 2.0f;
			red /= 256;
			green /= 256;
			blue /= 256;
			mouseOverColor2 = new Color(red, green, blue, 0.8f);

			if (mode == TWO_D)
			{
				target2D = new Ellipse2D.Double[numTrialsTargets];
				for (int i = 0; i < target2D.length; ++i)
					target2D[i] = new Ellipse2D.Double(0, 0, 0, 0);
				centerPoint = new Point2D.Double[numTrialsTargets];

			} else // 1D
			{
				target1D = new Rectangle2D.Double[numTrialsTargets];
				for (int i = 0; i < target1D.length; ++i)
					target1D[i] = new Rectangle2D.Double(0, 0, 0, 0);
				centerPoint = new Point2D.Double[numTrialsTargets];
			}

			this.setBackground(backgroundColor);
			this.setBorder(BorderFactory.createLineBorder(Color.gray));

			setSequenceParameters(aArg, wArg, 0, totalSequencesArg);
		}

		private void setSequenceParameters(int aArg, int wArg, int currentSequenceArg, int totalSequencesArg)
		{
			progress1 = String.format("Sequence %d of %d", currentSequenceArg + 1, totalSequencesArg);
			progress2 = String.format("(A = %d, W = %d)", aArg, wArg);

			positionTargets(aArg, wArg);

			// establish order of presenting the target to select (different for 1D vs 2D)

			if (mode == TWO_D)
			{
				targetOrder = new int[numTrialsTargets];
				for (int i = 0; i < targetOrder.length; ++i)
				{
					// the following generates indices to move around the circle in the usual 2D pattern
					int idx = i % 2 == 0 ? (i / 2) : (i + targetOrder.length) / 2;

					// the following provides wrap around (and avoids a crash)
					idx = idx % targetOrder.length;

					targetOrder[i] = idx;
				}

			} else // 1D
			{
				targetOrder = new int[numTrialsTargets + 1]; // NOTE: "+1" for 1D trials
				for (int i = 0; i < targetOrder.length; ++i)
					targetOrder[i] = i;
				targetOrder[targetOrder.length - 1] = 1;
			}
			resetActiveTarget();
		}

		public void positionTargets(int aArg, int wArg)
		{
			double centerX = this.getWidth() / 2.0;
			double centerY = this.getHeight() / 2.0;

			for (int i = 0; i < numTrialsTargets; ++i)
			{
				if (mode == TWO_D)
				{
					double x = centerX + (aArg / 2.0) * Math.cos(2.0 * Math.PI * ((double) i / numTrialsTargets));
					double y = centerY + (aArg / 2.0) * Math.sin(2.0 * Math.PI * ((double) i / numTrialsTargets));
					centerPoint[i] = new Point2D.Double(x, y);
					target2D[i].x = x - wArg / 2.0;
					target2D[i].y = y - wArg / 2.0;
					target2D[i].width = wArg;
					target2D[i].height = wArg;

				} else // 1D
				{
					double leftRightFactor = i % 2 == 0 ? -1.0 : 1.0; // simple way to have the targets alternate sides
					centerPoint[i] = new Point2D.Double();
					centerPoint[i].x = centerX + leftRightFactor * (aArg / 2.0);
					centerPoint[i].y = centerY;
					target1D[i].x = centerX + leftRightFactor * aArg / 2.0 - wArg / 2.0;
					target1D[i].y = (1 - HEIGHT_RATIO) / 2.0 * this.getHeight();
					target1D[i].width = wArg;
					target1D[i].height = HEIGHT_RATIO * this.getHeight();
				}
			}

			// commented out -- causes problem (resource path issue)
			// Oct 20, 2020 -- code uncommented -- trying to get this to work!
			targetImageScaled = targetImage.getScaledInstance(wArg, wArg, Image.SCALE_DEFAULT);
		}

		public void setInTarget(boolean inTarget)
		{
			if (!dwellOn && inTarget)
				dwellProgressTimer.start();

			if (dwellOn && !inTarget)
			{
				dwellProgressTimer.stop();
				arcAngle = 0;
			}

			dwellOn = inTarget;
			this.repaint();
		}

		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			paintTargets(g2);
		}

		private void paintTargets(Graphics2D g2)
		{
			// paint target outlines
			g2.setColor(foregroundColor);
			g2.setStroke(TARGET_STROKE); // set desired stroke

			// stackoverflow:
			// https://stackoverflow.com/questions/11193155/drawing-smooth-curves-in-java-graphics
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			if (mode == TWO_D)
			{
				// do it!
				for (Ellipse2D.Double t : target2D)
					g2.draw(t);

				// paint target to select last, so it appears on top of any overlapping targets
				
				// RMS Modified to add interactionMethod
				if (useTargetImage || interactionMethod == FITTS_FARM)
				{
					if (interactionMethod == FITTS_FARM) // RMS - to draw the animal to fed
					{
						int x = (int) target2D[activeTargetIndex].x;
						int y = (int) target2D[activeTargetIndex].y;
						int w = (int) target2D[activeTargetIndex].width;
						int r = (int) (w/2.0);
						
						if (taskPanel.inTarget(cursorX, cursorY, 1.0) && dragInProgress)
						{
							paintImage(g2, graphicTrial2, x, y, w, w);
							//System.out.println("In target");
						}
						else
						{
							paintImage(g2, graphicTrial, x, y, w, w);
							//System.out.println("Not in target");
						}
					}
					if (useTargetImage && interactionMethod != FITTS_FARM)
						g2.drawImage(targetImageScaled, (int) target2D[activeTargetIndex].x,
								(int) target2D[activeTargetIndex].y, this);

				} else
				{
					// The first task is to determine the target's fill color, which depends on four boolean variables
					// FYI: I used a truth table to figure this out. It was tricky!
					if (mouseOverHighlight && !buttonDownHighlight && inTarget
							|| mouseOverHighlight && buttonDownHighlight && inTarget && !buttonDown)
						g2.setColor(mouseOverColor);
					else if (buttonDownHighlight && inTarget && buttonDown)
						g2.setColor(buttonDownColor);
					else
						g2.setColor(targetColor);

					g2.fill(target2D[activeTargetIndex]);
					g2.setColor(foregroundColor);
					g2.setStroke(TARGET_STROKE); // set desired stroke
					g2.draw(target2D[activeTargetIndex]);
				}

				if (dwellSelection && dwellOn)
				{
					g2.setColor(mouseOverColor);
					g2.setStroke(ARC_STROKE);

					// draw the inside arc
					int arcDiameter = (int) Math.round(target2D[activeTargetIndex].width);
					int x = (int) Math.round(target2D[activeTargetIndex].x + ARC_STROKE_THICKNESS / 2.0);
					int y = (int) Math.round(target2D[activeTargetIndex].y + ARC_STROKE_THICKNESS / 2.0);
					int width = arcDiameter - ARC_STROKE_THICKNESS;
					int height = arcDiameter - ARC_STROKE_THICKNESS;
					g2.drawArc(x, y, width, height, 90, (int) arcAngle);

					// perhaps draw an extended arc (2D only)
					int extendedArcDiameter = getArcDiameter((int) Math.round(target2D[activeTargetIndex].width));
					if (extendedArcDiameter > arcDiameter)
					{
						float extendedArcThickness = (extendedArcDiameter - arcDiameter) / 2.0f;
						double xCenter = target2D[activeTargetIndex].x + arcDiameter / 2.0;
						double yCenter = target2D[activeTargetIndex].y + arcDiameter / 2.0;

						x = (int) Math.round(xCenter - extendedArcDiameter / 2.0 + extendedArcThickness / 2.0);
						y = (int) Math.round(yCenter - extendedArcDiameter / 2.0 + extendedArcThickness / 2.0);
						width = (int) Math.round(extendedArcDiameter - extendedArcThickness);
						height = (int) Math.round(extendedArcDiameter - extendedArcThickness);
						g2.setStroke(
								new BasicStroke(extendedArcThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
						g2.setColor(mouseOverColor2);
						g2.drawArc(x, y, width, height, 90, (int) arcAngle);
					}
				}

				if (showMarker)
				{
					g2.setStroke(TARGET_STROKE); // set desired stroke 
					
					g2.setColor(this.foregroundColor);
					g2.drawLine((int) target2D[activeTargetIndex].getCenterX(),
							(int) target2D[activeTargetIndex].getCenterY() - 3,
							(int) target2D[activeTargetIndex].getCenterX(),
							(int) target2D[activeTargetIndex].getCenterY() + 3); // vertical line (part of"+")
					g2.drawLine((int) target2D[activeTargetIndex].getCenterX() - 3,
							(int) target2D[activeTargetIndex].getCenterY(),
							(int) target2D[activeTargetIndex].getCenterX() + 3,
							(int) target2D[activeTargetIndex].getCenterY()); // horizontal line (part of "+)
				}
				// RMS - if dragging, then paint the drag image
				if (draggingMode && waitStartCircleSelect)
				{					
					g2.setColor(Color.GRAY); // SM 15/3/2025 - gray perhaps
					int x = (int) Math.round(target2D[sourceTargetIndex].x+target2D[sourceTargetIndex].width/2.0);
					int y = (int) Math.round(target2D[sourceTargetIndex].y+target2D[sourceTargetIndex].height/2.0);
					int radius = (int) Math.round(defaultDragObjectWidth/2.0);
					if (interactionMethod == FITTS_FARM)
						paintImage(g2, apple, x-radius, y-radius, radius*2, radius*2);
					else
						g2.fillOval(x-radius, y-radius, radius*2, radius*2);
					//System.out.println("Drawn "+x+" "+y+" "+width+" "+width+" "+sourceTargetIndex);
					radius = (int) Math.round(target2D[sourceTargetIndex].width/2.0);

					fromTarget = new Rectangle2D.Double(x-radius, y-radius, radius*2, radius*2);
				}
				// END RMS

			} else // 1D
			{
				for (int i = 0; i < numTrialsTargets; ++i)
					g2.draw(target1D[i]);

				// paint target to select last, so it appears on top of any overlapping targets

				// The first task is to determine the target's fill color, which depends on four boolean variables
				// FYI: I used a truth table to figure this out. It was tricky!
				if (mouseOverHighlight && !buttonDownHighlight && inTarget
						|| mouseOverHighlight && buttonDownHighlight && inTarget && !buttonDown)
					g2.setColor(mouseOverColor);
				else if (buttonDownHighlight && inTarget && buttonDown)
					g2.setColor(buttonDownColor);
				else
					g2.setColor(targetColor);

				// now paint the target to select
				g2.fill(target1D[activeTargetIndex]);
				g2.setColor(foregroundColor);
				g2.setStroke(TARGET_STROKE); // set desired stroke
				g2.draw(target1D[activeTargetIndex]);
				
				// RMS to add interaction method
				if (interactionMethod == FITTS_FARM)
				{
					// Paint animal
					int x2 = (int) Math.round(target1D[activeTargetIndex].x+target1D[activeTargetIndex].width/2.0);
					int y2 = (int) Math.round(target1D[activeTargetIndex].y+target1D[activeTargetIndex].height/2.0);
					int w2 = (int) Math.round(target1D[activeTargetIndex].width);
					int r2 = (int) Math.round(w2/2.0);

					
					if (taskPanel.inTarget(cursorX, cursorY, 1.0) && dragInProgress)
					{
						paintImage(g2, graphicTrial2, x2-r2, y2-r2, w2, w2);
						//System.out.println("In target");
					}
					else
					{
						paintImage(g2, graphicTrial, x2-r2, y2-r2, w2, w2);
						//System.out.println("Not in target");
					}
				}
				// END RMS

				if (dwellSelection && dwellOn)
				{
					g2.setColor(mouseOverColor);
					g2.setStroke(ARC_STROKE);
					int x = (int) Math.round(target1D[activeTargetIndex].x + ARC_STROKE_THICKNESS / 2.0);
					int y = (int) Math.round(target1D[activeTargetIndex].y + ARC_STROKE_THICKNESS / 2.0);
					int width = (int) Math.round(target1D[activeTargetIndex].width - ARC_STROKE_THICKNESS);
					int height = (int) Math.round(target1D[activeTargetIndex].height - ARC_STROKE_THICKNESS);
					g2.drawArc(x, y, width, height, 90, (int) arcAngle);
				}

				if (showMarker)
				{
					g2.setColor(this.foregroundColor);
					g2.setStroke(TARGET_STROKE); // set desired stroke
					g2.drawLine((int) target1D[activeTargetIndex].getCenterX(),
							(int) target1D[activeTargetIndex].getCenterY() - 3,
							(int) target1D[activeTargetIndex].getCenterX(),
							(int) target1D[activeTargetIndex].getCenterY() + 3); // vertical line (part of "+")
					g2.drawLine((int) target1D[activeTargetIndex].getCenterX() - 3,
							(int) target1D[activeTargetIndex].getCenterY(),
							(int) target1D[activeTargetIndex].getCenterX() + 3,
							(int) target1D[activeTargetIndex].getCenterY()); // horizontal line (part of "+)
				}
				
				// RMS - if dragging, then paint the drag and target images
				if (draggingMode && waitStartCircleSelect)
				{					
					g2.setColor(Color.GRAY); // SM 15/3/2025 - gray perhaps better than black
					int x = (int) Math.round(target1D[sourceTargetIndex].x+target1D[sourceTargetIndex].width/2.0);
					int y = (int) Math.round(target1D[sourceTargetIndex].y+target1D[sourceTargetIndex].height/2.0);
					int radius = (int) Math.round(defaultDragObjectWidth/2.0);
					
					//System.out.println("Drawn "+x+" "+y+" "+width+" "+width+" "+sourceTargetIndex);
					fromTarget = new Rectangle2D.Double(x-radius, y-radius, radius*2, radius*2);
					
					if (interactionMethod == FITTS_FARM)
					{
						// Paint apple
						paintImage(g2, apple, x-radius, y-radius, radius*2, radius*2);
					}
					else
					{
						// Paint circle
						g2.fillOval(x-radius, y-radius, radius*2, radius*2);
					}
				}
			}
			
			if (buttonDown || dragging)
			{
				//System.out.println("Drawing dragging");
				if (dragInProgress)
				{
					paintObject(g2, draggedObject);
				}
				if (interactionMethod != POINT_SELECT) paintCursor(g2);
			}
			// END RMS
			
			// output progress message
			g2.setColor(PROGRESS_MESSAGE_COLOR);
			g2.setFont(PROGRESS_MESSAGE_FONT);
			int height = g2.getFontMetrics().getHeight(); // of characters (for given font)
			g2.drawString(progress1, 10, 10 + height);
			g2.drawString(progress2, 10, 10 + 2 * height);
		}
		
		// RMS - Paints the heatmap
		
		void paintCursor(Graphics2D g2)
		{
			int width = (int) Math.round(touchPointRadius); //(int) Math.round(target1D[sourceTargetIndex].width);
			int x = cursorX-width/2;
			int y = cursorY-width/2;
			
			int radius = (int) Math.round(width/2.0);
			Point center = new Point(x+radius, y+radius);
		    float[] dist = {0f,1f};
		    Color[] colors = {
		            new Color(255, 0, 255, 255),
		            new Color(0, 0, 0, 0)};
		        
		    radialGradient = new RadialGradientPaint(center, radius, dist, colors);
		    
			g2.setPaint(radialGradient);
			g2.fillOval(x, y, width, width);
		}

		void paintImage(Graphics2D g2, BufferedImage i, int x, int y, int w, int h)
		{
			g2.drawImage(i, x, y, w, h, this);
		}
		
		void paintObject(Graphics2D g2, Rectangle2D.Double r)
		{
			int width = (int) Math.round(r.width); 
			int radius = (int) Math.round(r.width/2.0f);
			int x = cursorX-width/2;
			int y = cursorY-width/2; 
			g2.setColor(Color.GRAY); // SM 15/3/2025 - gray might be better than black
			if (interactionMethod == FITTS_FARM)
			{
				paintImage(g2, apple, x, y, width, width);
			}
			else			
				g2.fillOval(x, y, width, width);
		}
		// END RMS
		
		// get minimum arc width (as an integer)
		int getArcDiameter(int targetWidth)
		{
			String[] s = minimumDwellArcSize.split("\\s");
			return minimumDwellArcSize.equals("Target width") ? targetWidth : Integer.parseInt(s[0]);
		}

		public boolean inTarget(double x, double y, double hysteresisFactor)
		{
			if (mode == TWO_D)
			{
				double centerX = target2D[activeTargetIndex].x + target2D[activeTargetIndex].getWidth() / 2.0;
				double centerY = target2D[activeTargetIndex].y + target2D[activeTargetIndex].getHeight() / 2.0;
				double radius = target2D[activeTargetIndex].getWidth() / 2.0 * hysteresisFactor;
				double distanceToCenter = Math.hypot(x - centerX, y - centerY);
				return distanceToCenter <= radius + 1.0; // "+1" seems to be necessary!?

			} else // 1D
			{
				double xTest = target1D[activeTargetIndex].x;
				double yTest = target1D[activeTargetIndex].y;
				double widthTest = target1D[activeTargetIndex].width;
				double heightTest = target1D[activeTargetIndex].height;

				// adjust width and x of test target according to hysteresis factor, if any
				widthTest *= hysteresisFactor;
				xTest = xTest - (widthTest - target1D[activeTargetIndex].width) / 2.0;

				Rectangle2D.Double targetTest = new Rectangle2D.Double(xTest, yTest, widthTest, heightTest);
				return targetTest.contains(x, y);
			}
		}

		public void advanceActiveTarget(int trial)
		{
			if (mode == TWO_D)
			{
				sourceTargetIndex = activeTargetIndex;  // RMS - for dragging source
				activeTargetIndex = targetOrder[trial % targetOrder.length];
			} else // 1D
			{
				// advance the target (if wrap around, set trial to 1 (to avoid repeating the 1st target)
				if (trial == targetOrder.length)
					trial = 1;

				activeTargetIndex = targetOrder[trial];
				
				 // RMS - for dragging source
				sourceTargetIndex = activeTargetIndex+1;
				if (sourceTargetIndex == target1D.length) sourceTargetIndex = 1;
			}
			// System.out.println("ActiveTargetIndex: "+activeTargetIndex+"   sourceTargetIndex: "+sourceTargetIndex);
			nextRandomGraphic();
			this.repaint();
		}

		public void resetActiveTarget()
		{
			activeTargetIndex = targetOrder[0]; // first target is at top (3/4 around circle)
			sourceTargetIndex = targetOrder[targetOrder.length-1];  // RMS init of source target for dragging
		}

	} // end TaskPanel_2D

	// ****************************************************************************************
	// a class to hold the timestamp and coordinate point for a trace sample of cursor movement
	// ****************************************************************************************

	public class TraceSample
	{
		long t; // time in milliseconds since beginning of trial
		Point point; // sample point for current location of cursor

		TraceSample(long tArg, int xArg, int yArg)
		{
			t = tArg;
			point = new Point(xArg, yArg);
		}
	}	

}
