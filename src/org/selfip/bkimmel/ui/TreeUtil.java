/**
 *
 */
package org.selfip.bkimmel.ui;

import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import org.jdesktop.swingx.JXTreeTable;

/**
 * Provides static utility methods for working with trees.
 * @author brad
 */
public final class TreeUtil {

	/**
	 * Default constructor.  This constructor is private because this is a
	 * utility class.
	 */
	private TreeUtil() {}

	/**
	 * Adds a <code>TreeModelListener</code> that will automatically expand
	 * the tree as new nodes are inserted.
	 * @param tree The <code>JTree</code>.
	 */
	public static void enableAutoExpansion(final JTree tree) {

		tree.getModel().addTreeModelListener(new TreeModelListener() {

			public void treeNodesChanged(TreeModelEvent e) {
				// nothing to do
			}

			public void treeNodesInserted(TreeModelEvent e) {
				tree.expandPath(e.getTreePath());
			}

			public void treeNodesRemoved(TreeModelEvent e) {
				// nothing to do
			}

			public void treeStructureChanged(TreeModelEvent e) {
				// nothing to do
			}

		});

	}

	/**
	 * Adds a <code>TreeModelListener</code> that will automatically expand
	 * the tree as new nodes are inserted.
	 * @param tree The <code>JXTreeTable</code>.
	 */
	public static void enableAutoExpansion(final JXTreeTable treeTable) {

		treeTable.getTreeTableModel().addTreeModelListener(new TreeModelListener() {

			public void treeNodesChanged(TreeModelEvent e) {
				// nothing to do
			}

			public void treeNodesInserted(TreeModelEvent e) {
				treeTable.expandPath(e.getTreePath());
			}

			public void treeNodesRemoved(TreeModelEvent e) {
				// nothing to do
			}

			public void treeStructureChanged(TreeModelEvent e) {
				// nothing to do
			}

		});

	}

}
