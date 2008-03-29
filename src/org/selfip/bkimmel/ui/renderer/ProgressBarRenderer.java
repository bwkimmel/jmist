/**
 *
 */
package org.selfip.bkimmel.ui.renderer;

import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * A <code>TableCellRenderer</code> that renders a progress bar within the
 * table cell.  The value of the cell must be the <code>JProgressBar</code> to
 * be rendered.
 *
 * This class is a singleton.
 *
 * @author brad
 */
public final class ProgressBarRenderer implements TableCellRenderer {

	/** The single <code>ProgressBarRenderer</code> instance. */
	private static ProgressBarRenderer instance = null;

	/**
	 * Default constructor.  This constructor is private because this class is
	 * a singleton.
	 */
	private ProgressBarRenderer() {
		// nothing to do.
	}

	/**
	 * Gets the single <code>ProgressBarRenderer</code> instance.
	 * @return The single <code>ProgressBarRenderer</code> instance.
	 */
	public static ProgressBarRenderer getInstance() {
		if (instance == null) {
			instance = new ProgressBarRenderer();
		}
		return instance;
	}

	/**
	 * Sets <code>ProgressBarRenderer</code> as the default table cell renderer
	 * for <code>JProgressBar</code> columns.
	 * @param table The <code>JTable</code> in which to use this renderer.
	 */
	public static void applyTo(JTable table) {
		table.setDefaultRenderer(JProgressBar.class, getInstance());
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		return (JProgressBar) value;

	}

}
