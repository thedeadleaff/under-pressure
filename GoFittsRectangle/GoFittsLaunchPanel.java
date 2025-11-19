import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

class GoFittsLaunchPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	final Color MESSAGE_COLOR = new Color(155, 155, 155);
	final Color BACKGROUND_COLOR = new Color(200, 200, 200);
	final Font MESSAGE_FONT = new Font("SannsSerif", Font.ITALIC, 96);

	String launchMessage, launchSubMessage;
	Color messageColor;
	Font messageFont;
	FontMetrics fm;

	GoFittsLaunchPanel()
	{
		messageFont = this.MESSAGE_FONT;
		messageColor = this.MESSAGE_COLOR;
		this.setBackground(this.BACKGROUND_COLOR);
		this.setPreferredSize(new Dimension(800, 400));
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		if (launchMessage == null || launchMessage.length() == 0)
			return;

		// paint main message (in middle of panel)
		messageFont = this.MESSAGE_FONT;
		g2.setFont(messageFont);
		g2.setColor(messageColor);
		fm = g2.getFontMetrics();
		int width = this.getWidth();
		int height = this.getHeight();		
		
		// if necessary, reduce size of font so message fits comfortably in available space
		while (fm.stringWidth(launchMessage) > 0.8 * width)
		{
			int newFontSize = (int)(messageFont.getSize() * 0.9);		
			messageFont = messageFont.deriveFont((float)newFontSize);
			g2.setFont(messageFont);
			fm = g2.getFontMetrics();
		}

		height = fm.getHeight(); // of characters (for given font)
		width = fm.stringWidth(launchMessage);
		int ascent = fm.getAscent();
		int x = this.getWidth() / 2 - width / 2;
		int y = this.getHeight() / 2 + height / 2 - ascent;
		g2.drawString(launchMessage, x, y);
		
		if (launchSubMessage == null || launchSubMessage.length() == 0)
			return;

		// paint sub message (below main message)
		messageFont = messageFont.deriveFont((float)(messageFont.getSize() / 2));
		g2.setFont(messageFont);		
		fm = g2.getFontMetrics();
		height = fm.getHeight(); // of characters (for given font)
		width = fm.stringWidth(launchSubMessage);
		x = this.getWidth() / 2 - width / 2;
		y = this.getHeight() / 2 + height / 2;
		g2.drawString(launchSubMessage, x, y);
	}
}