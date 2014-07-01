package llorx.kspModManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.io.File;
import java.util.Collections;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Display a file system in a JTree view
 * 
 * @version $Id: FileTree.java,v 1.9 2004/02/23 03:39:22 ian Exp $
 * @author Ian Darwin
 * @moded by Llorx to add CheckBoxes and fit KSP Mod Manager
 */
public class FileTreeModel extends JPanel {
  /** Construct a FileTree */
  
  public CheckTreeManager checkTreeManager;
  private String[] excludeList;
  
  public FileTreeModel(File dir, String[] excludeList) {
    this.excludeList = excludeList;
    
    setLayout(new BorderLayout());

    // Make a tree list with all the nodes, and make it a JTree
    JTree tree = new JTree(addNodes(null, dir));
	
    // Add a listener
    tree.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
            .getPath().getLastPathComponent();
      }
    });

	checkTreeManager = new CheckTreeManager(tree); 
	
    // Lastly, put the JTree into a JScrollPane.
    JScrollPane scrollpane = new JScrollPane();
    scrollpane.getViewport().add(tree);
    add(BorderLayout.CENTER, scrollpane);
  }

  /** Add nodes from under "dir" into curTop. Highly recursive. */
  DefaultMutableTreeNode addNodes(DefaultMutableTreeNode curTop, File dir) {
    String curPath = dir.getPath();
    String curName = dir.getName();
    DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(curName);
    if (curTop != null) { // should only be null at root
      curTop.add(curDir);
    }
    Vector ol = new Vector();
    String[] tmp = dir.list();
    for (int i = 0; i < tmp.length; i++)
      ol.addElement(tmp[i]);
    Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
    File f;
    Vector files = new Vector();
    // Make two passes, one for Dirs and one for Files. This is #1.
    for (int i = 0; i < ol.size(); i++) {
      String thisObject = (String) ol.elementAt(i);
      String thisObjectLower = thisObject.toLowerCase();
      boolean exclude = false;
      for (String exc: this.excludeList) {
        if (thisObjectLower.matches(exc)) {
          exclude = true;
          break;
        }
      }
      if (exclude == false) {
        String newPath;
        if (curPath.equals("."))
          newPath = thisObject;
        else
          newPath = curPath + File.separator + thisObject;
        if ((f = new File(newPath)).isDirectory())
          addNodes(curDir, f);
        else
          files.addElement(thisObject);
      }
    }
    // Pass two: for files.
    for (int fnum = 0; fnum < files.size(); fnum++)
      curDir.add(new DefaultMutableTreeNode(files.elementAt(fnum)));
    return curDir;
  }

  public Dimension getMinimumSize() {
    return new Dimension(200, 400);
  }

  public Dimension getPreferredSize() {
    return new Dimension(200, 400);
  }
}