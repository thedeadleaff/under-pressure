import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

// ==============================================================================================
// PanZoomPanel - class to hold a JPanel and implement panning and zooming via mouse and keyboard
// ==============================================================================================

class PanZoomPanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener
{
	private static final long serialVersionUID = 1L;
	final static int BAR_WIDTH = 9;
	final double SCALE_INCREMENT = 0.05;

	int cursorX, cursorY;
	double scaleNew;
	PanZoomIndicatorPanel panZoomIndicatorPanel;
	JPanel panel;
	int panelInitialWidth, panelInitialHeight;
	boolean debug = false;
	PanZoomPanelListener panZoomPanelListener;

	PanZoomPanel(JPanel panelArg)
	{
		/*
		 * NOTE: The PanZoomPanel holds two components only, the TracePanel (which displays the targets and trace data) and the
		 * PanZoomIndicatorPanel (which displays the pan/zoom indicators along the edges of the PanZoomPanel). Furthermore, the
		 * PanZoomPanel's layout manager is set to null. The means the TracePanel's and PanZoomIndicatorPanel's size and position
		 * are *not* determined by a layout manager in the usual way (i.e., via properties such as preferred size, etc.). Their
		 * size and position within the PanZoomPanel are fully determined by explicit calls to setLocation and setSize. See, also,
		 * the following stackoverflow post:
		 * 
		 * https://stackoverflow.com/questions/1783793/java-difference-between-the-setpreferredsize-and-setsize-methods-in-compone
		 */

		// critical step -- see comment above
		this.setLayout(null);

		panel = panelArg;	

		panZoomIndicatorPanel = new PanZoomIndicatorPanel();		
		this.add(panZoomIndicatorPanel);

		setBackground(Color.WHITE);
		setFocusable(true); // enable keyboard input for panning and zooming

		// add listeners required for panning and zooming
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}

	// the interface to be implemented in the host class
	public interface PanZoomPanelListener
	{
		// here, we only define the signature; the method is implemented in the host class
		void onPanZoomPanelAction(PanZoomPanelEvent e);
	}

	// invoked in host class to attach the listener to the PanZoomPanel instance
	public void addPanZoomPanelListener(PanZoomPanelListener panZoomPanelListenerArg)
	{
		panZoomPanelListener = panZoomPanelListenerArg;
	}

	void setInitialSizeAndLocation(int widthArg, int heightArg, int xArg, int yArg)
	{
		/*
		 * Since "scale" is a factor, we need the initial width and height of the panel. All manipulations to change the scale of
		 * the panel are done with respect to these initial values. These values only change if the UI's window is resized.
		 */
		panelInitialWidth = widthArg - 2 * BAR_WIDTH - 3;
		panelInitialHeight = heightArg - 2 * BAR_WIDTH - 3;

		// intial scale factor is 1 (with no horizontal or vertical repositioning)
		setSizeAndOffset(1.0f, 0, 0);

		// set the initial location of the panel w.r.t. the PanZoomPanel's top-left corner
		panel.setLocation(xArg + BAR_WIDTH + 1, yArg + BAR_WIDTH + 1);

		panZoomIndicatorPanel.setLocation(0, 0);
		panZoomIndicatorPanel.setSize(new Dimension(widthArg, heightArg));
	}

	// workhorse method that does panning and zooming
	void setSizeAndOffset(double scale, int xOffset, int yOffset)
	{
		if (scale != scaleNew)
		{
			// update scale factor
			scaleNew = scale;

			// update scale textfield in UI and give scale to panel (since scale is used in drawing)
			panZoomPanelListener.onPanZoomPanelAction(new PanZoomPanelEvent(scaleNew));
		}

		// get current center of trace panel
		double xCenter = panel.getBounds().getCenterX();
		double yCenter = panel.getBounds().getCenterY();

		// set new size of panel based on new value of scale
		panel.setSize(new Dimension((int) Math.round(panelInitialWidth * scaleNew), (int) Math.round(panelInitialHeight * scaleNew)));

		// set new location of panel based on new size and original center point
		int xNew = (int) (xCenter - panel.getBounds().width / 2.0f) + xOffset;
		int yNew = (int) (yCenter - panel.getBounds().height / 2.0f) + yOffset;
		panel.setLocation(xNew, yNew);

		this.repaint();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent mwe)
	{
		int clicks = mwe.getWheelRotation();

		if (mwe.isControlDown()) // zooming
			setSizeAndOffset(Math.max(0.1f, scaleNew - SCALE_INCREMENT * clicks), 0, 0);

		else // panning (y-axis only)
			setSizeAndOffset(scaleNew, 0, clicks * -10);
	}

	@Override
	public void mouseDragged(MouseEvent me)
	{
		int deltaX = me.getX() - cursorX;
		int deltaY = me.getY() - cursorY;
		cursorX = me.getX();
		cursorY = me.getY();
		setSizeAndOffset(scaleNew, deltaX, deltaY);
	}

	@Override
	public void mouseMoved(MouseEvent arg0)
	{
	}

	@Override
	public void mouseClicked(MouseEvent arg0)
	{
	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
	}

	@Override
	public void mousePressed(MouseEvent me)
	{
		// get x-y cursor coordinate at the beginning of a drag operation
		cursorX = me.getX();
		cursorY = me.getY();
	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
	}

	@Override
	public void keyPressed(KeyEvent ke)
	{
		// define panning and zooming key patterns
		final KeyStroke CTRL_PLUS = KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.CTRL_MASK); // zoom in
		final KeyStroke CTRL_RIGHT_BRACKET = KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET, KeyEvent.CTRL_MASK); // zoom in

		final KeyStroke CTRL_MINUS = KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, KeyEvent.CTRL_MASK); // zoom out
		final KeyStroke CTRL_LEFT_BRACKET = KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, KeyEvent.CTRL_MASK); // zoom out

		final KeyStroke CTRL_ZERO = KeyStroke.getKeyStroke(KeyEvent.VK_0, KeyEvent.CTRL_MASK); // reset

		final KeyStroke CTRL_UP_ARROW = KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.CTRL_MASK); // pan up
		final KeyStroke UP_ARROW = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);

		final KeyStroke CTRL_DOWN_ARROW = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.CTRL_MASK); // pan down
		final KeyStroke DOWN_ARROW = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);

		final KeyStroke CTRL_LEFT_ARROW = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.CTRL_MASK); // pan left
		final KeyStroke LEFT_ARROW = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);

		final KeyStroke CTRL_RIGHT_ARROW = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.CTRL_MASK); // pan right
		final KeyStroke RIGHT_ARROW = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);

		// get the key stroke the user typed
		KeyStroke k = KeyStroke.getKeyStroke(ke.getKeyCode(), ke.getModifiers());

		// respond accordingly
		if (k == CTRL_PLUS || k == CTRL_RIGHT_BRACKET)
		{
			setSizeAndOffset(Math.max(0.1f, scaleNew + SCALE_INCREMENT), 0, 0);

		} else if (k == CTRL_MINUS || k == CTRL_LEFT_BRACKET)
		{
			setSizeAndOffset(Math.max(0.1f, scaleNew - SCALE_INCREMENT), 0, 0);

		} else if (k == CTRL_ZERO)
		{
			setSizeAndOffset(1.0f, 0, 0);
			panel.setLocation(1, 1);

		} else if (k == CTRL_UP_ARROW || k == UP_ARROW)
		{
			setSizeAndOffset(scaleNew, 0, -10);

		} else if (k == CTRL_DOWN_ARROW || k == DOWN_ARROW)
		{
			setSizeAndOffset(scaleNew, 0, 10);

		} else if (k == CTRL_LEFT_ARROW || k == LEFT_ARROW)
		{
			setSizeAndOffset(scaleNew, -10, 0);

		} else if (k == CTRL_RIGHT_ARROW || k == RIGHT_ARROW)
		{
			setSizeAndOffset(scaleNew, 10, 0);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{
	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g); // paint background

		Graphics2D g2 = (Graphics2D) g;

		// ===========
		// Debug stuff
		// ===========

		if (debug) // red lines showing panel outline and center
		{
			double xTopLeft = 1;
			double yTopLeft = 1;
			double xBottomRight = this.getWidth() - 3;
			double yBottomRight = this.getHeight() - 3;
			Line2D.Double line = new Line2D.Double(xTopLeft, yTopLeft, xBottomRight, yBottomRight);
			g2.setColor(Color.RED);
			g2.setStroke(new BasicStroke(1));
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

	class PanZoomIndicatorPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;

		PanZoomIndicatorPanel()
		{
			this.setOpaque(false);
		}

		public void paintComponent(Graphics g)
		{
			super.paintComponent(g); // paint background

			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setStroke(new BasicStroke(PanZoomPanel.BAR_WIDTH));

			double xTopLeft = (int) (BAR_WIDTH / 2.0);
			double yTopLeft = (int) (BAR_WIDTH / 2.0);
			double width = this.getWidth() - BAR_WIDTH;
			double height = this.getHeight() - BAR_WIDTH;
			Rectangle2D.Double zoomBarRectangle = new Rectangle2D.Double(xTopLeft, yTopLeft, width, height);
			g2.setColor(Color.WHITE);
			g2.draw(zoomBarRectangle);

			// ==================
			// Vertical Zoom Bars
			// ==================

			g2.setColor(Color.LIGHT_GRAY);
			g2.setStroke(new BasicStroke(PanZoomPanel.BAR_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			height = this.getHeight();
			double yRatio = (double) this.getHeight() / panel.getHeight();
			double viewY = panel.getLocation().y;
			
			double barHeight;
			double barY;
			if (yRatio >= 1) // zoomed out
			{
				barHeight = panel.getHeight();
				barY = panel.getLocation().y;

			} else // zoomed in
			{
				barHeight = Math.min(height, yRatio * height);
				barY = Math.min(viewY + panel.getHeight() - barHeight, Math.max(viewY, Math.abs(barHeight * viewY / height)));
			}

			double x1 = this.getWidth() - BAR_WIDTH / 2.0;
			double y1 = barY + BAR_WIDTH / 2.0;
			double x2 = this.getWidth() - BAR_WIDTH / 2.0;
			double y2 = barY + barHeight - BAR_WIDTH / 2.0;
			Line2D.Double rightViewBar = new Line2D.Double(x1, y1, x2, y2);
			g2.draw(rightViewBar);

			x1 = BAR_WIDTH / 2.0;
			x2 = BAR_WIDTH / 2.0;
			Line2D.Double leftViewBar = new Line2D.Double(x1, y1, x2, y2);
			g2.draw(leftViewBar);

			// ====================
			// Horizontal Zoom Bars
			// ====================

			width = this.getWidth();
			double xRatio = (double) this.getWidth() / panel.getWidth();
			double viewX = panel.getLocation().x;
			
			double barWidth;
			double barX;
			if (xRatio >= 1) // zoomed out
			{
				barWidth = panel.getWidth();
				barX = panel.getLocation().x;

			} else // zoomed in
			{
				barWidth = Math.min(width, xRatio * width);
				barX = Math.min(viewX + panel.getWidth() - barWidth, Math.max(viewX, Math.abs(barWidth * viewX / width)));
			}

			x1 = barX + BAR_WIDTH / 2.0;
			y1 = this.getHeight() - BAR_WIDTH / 2.0;
			x2 = barX + barWidth - BAR_WIDTH / 2.0;
			y2 = this.getHeight() - BAR_WIDTH / 2.0;
			Line2D.Double bottomViewBar = new Line2D.Double(x1, y1, x2, y2);
			g2.draw(bottomViewBar);

			y1 = BAR_WIDTH / 2.0;
			y2 = BAR_WIDTH / 2.0;
			Line2D.Double topViewBar = new Line2D.Double(x1, y1, x2, y2);
			g2.draw(topViewBar);
		}
	}
} // end PanZoomPanel