package llorx.kspModManager;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.*;

import java.awt.Component;
import java.awt.BorderLayout;

public class CheckTreeCellRenderer extends JPanel implements TreeCellRenderer {
	private CheckTreeSelectionModel selectionModel;
	private TreeCellRenderer delegate;
	private TristateCheckBox checkBox = new TristateCheckBox();
	
	public CheckTreeCellRenderer(TreeCellRenderer delegate, CheckTreeSelectionModel selectionModel) {
		this.delegate = delegate;
		this.selectionModel = selectionModel;
		setLayout(new BorderLayout());
		setOpaque(false);
		checkBox.setOpaque(false);
	}
	
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		Component renderer = delegate.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		
		TreePath path = tree.getPathForRow(row);
		if(path != null) {
			if(selectionModel.isPathSelected(path, true))
				checkBox.setState(TristateCheckBox.SELECTED);
			else
				checkBox.setState(selectionModel.isPartiallySelected(path) ? null : TristateCheckBox.NOT_SELECTED);
		}
		removeAll();
		add(checkBox, BorderLayout.WEST);
		add(renderer, BorderLayout.CENTER);
		return this;
	}
}