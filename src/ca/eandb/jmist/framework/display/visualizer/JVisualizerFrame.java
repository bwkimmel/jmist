/**
 * 
 */
package ca.eandb.jmist.framework.display.visualizer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import ca.eandb.jmist.framework.Display;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.util.io.FileUtil;

/**
 * @author brad
 *
 */
public final class JVisualizerFrame extends JFrame implements Display {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 6053456293463524773L;
	
	private static final String DEFAULT_TITLE = "jMIST Visualizer";
	
	private static final Dimension MINIMUM_SIZE = new Dimension(640, 480);
	
	private final JVisualizerDisplay display;
	
	public JVisualizerFrame(JColorVisualizerPanel visualizerPanel) {		
		display = new JVisualizerDisplay(visualizerPanel);
		
		JScrollPane displayScrollPane = new JScrollPane(display);
		
		setLayout(new BorderLayout());
		add(displayScrollPane, BorderLayout.CENTER);
		add(visualizerPanel, BorderLayout.SOUTH);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu;
		JMenuItem menuItem;
		
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		
		menuItem = new JMenuItem("Save", KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Save the image to a file.");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save_OnActionPerformed(e);
			}
		});
		menu.add(menuItem);
		
		menuBar.add(menu);
		setJMenuBar(menuBar);
		setTitle(DEFAULT_TITLE);
		setMinimumSize(MINIMUM_SIZE);

		pack();
	}

	private void save_OnActionPerformed(ActionEvent event) {
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "Image Files", ImageIO.getWriterFileSuffixes());
	    chooser.setFileFilter(filter);
		chooser.setAcceptAllFileFilterUsed(false);
	    int returnVal = chooser.showSaveDialog(this);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	    	File file = chooser.getSelectedFile();
	    	String suffix = FileUtil.getExtension(file.getName());
	    	try {
				boolean foundWriter = ImageIO.write(display.getRenderedImage(), suffix, file);
				if (!foundWriter) {
					String message = String.format("Invalid image file format (%s)", suffix);
					JOptionPane.showMessageDialog(this, message, "Error saving image", JOptionPane.WARNING_MESSAGE);
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(),
						"Error saving image", JOptionPane.WARNING_MESSAGE);
			}
	    }
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#initialize(int, int, ca.eandb.jmist.framework.color.ColorModel)
	 */
	@Override
	public void initialize(int w, int h, ColorModel colorModel) {
		display.initialize(w, h, colorModel);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#fill(int, int, int, int, ca.eandb.jmist.framework.color.Color)
	 */
	@Override
	public void fill(int x, int y, int w, int h, Color color) {
		display.fill(x, y, w, h, color);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#setPixel(int, int, ca.eandb.jmist.framework.color.Color)
	 */
	@Override
	public void setPixel(int x, int y, Color pixel) {
		display.setPixel(x, y, pixel);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#setPixels(int, int, ca.eandb.jmist.framework.Raster)
	 */
	@Override
	public void setPixels(int x, int y, Raster pixels) {
		display.setPixels(x, y, pixels);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Display#finish()
	 */
	@Override
	public void finish() {
		display.finish();
	}

}
