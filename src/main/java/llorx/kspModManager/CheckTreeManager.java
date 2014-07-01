package llorx.kspModManager;

import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import javax.swing.JTree;
import javax.swing.JCheckBox;

public class CheckTreeManager extends MouseAdapter implements TreeSelectionListener {
    private CheckTreeSelectionModel selectionModel;
    private JTree tree = new JTree();
    int hotspot = new JCheckBox().getPreferredSize().width;
	
    public CheckTreeManager(JTree tree) {
        this.tree = tree;
        selectionModel = new CheckTreeSelectionModel(tree.getModel());
		selectionModel.addSelectionPath(new TreePath(((DefaultMutableTreeNode)tree.getModel().getRoot()).getPath()));
        tree.setCellRenderer(new CheckTreeCellRenderer(tree.getCellRenderer(), selectionModel));
        tree.addMouseListener(this);
        selectionModel.addTreeSelectionListener(this);
    }
	
    public void mouseClicked(MouseEvent me) {
        TreePath path = tree.getPathForLocation(me.getX(), me.getY());
        if(path == null)
            return;
        if(me.getX()>tree.getPathBounds(path).x+hotspot)
            return;
 
        boolean selected = selectionModel.isPathSelected(path, true);
        selectionModel.removeTreeSelectionListener(this);
		
        try {
            if(selected)
                selectionModel.removeSelectionPath(path);
            else
                selectionModel.addSelectionPath(path);
        } finally {
            selectionModel.addTreeSelectionListener(this);
            tree.treeDidChange();
        }
    }
	
    public CheckTreeSelectionModel getSelectionModel() {
        return selectionModel;
    }
	
    public void valueChanged(TreeSelectionEvent e) {
        tree.treeDidChange();
    }
}