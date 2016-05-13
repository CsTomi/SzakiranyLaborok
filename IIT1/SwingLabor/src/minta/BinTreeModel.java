package minta;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

class BinTreeModel implements TreeModel {
    ArrayList<TreeModelListener> listeners;
    BinTree root;
    public BinTreeModel() {
	listeners = new ArrayList<TreeModelListener>();
	root = new BinTree();
    }
    public void addTreeModelListener(TreeModelListener l){
	listeners.add(l);
    }
    public void removeTreeModelListener(TreeModelListener l){
	listeners.remove(l);
    }
    public Object getChild(Object parent, int index){
	BinTree bt = (BinTree)parent;
	if (bt == null) return null;
	switch (index) {
	    case 0: return bt.left;
	    case 1: return bt.right;
	}
	return null;
    }
    public int getChildCount(Object parent){
	BinTree bt = (BinTree)parent;
	if (bt == null) return 0;

	return (bt.isNull()) ? 0 : 2;
    }
    public int getIndexOfChild(Object parent, Object child){
	BinTree bt = (BinTree)parent;
	int n = -1;
	if (bt == null) return n;
	if (bt.left == child) n = 0;
	if (bt.right == child) n = 1;
	return n;
    }
    public Object getRoot(){
	return root;
    }
    public boolean isLeaf(Object node){
	BinTree bt = (BinTree)node;
	return (bt!=null && bt.isNull());
    }
    public void valueForPathChanged(TreePath path, Object newValue){
    }
    public void insert(double d) {
	ArrayList<BinTree> path = root.insert(d);
	Object[] opath = path.toArray();
	TreeModelEvent e = new TreeModelEvent(this, opath);
	for (TreeModelListener tml : listeners) {
	    tml.treeNodesInserted(e);
	}
    }
}

