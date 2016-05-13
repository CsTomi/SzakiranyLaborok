package labor;

import java.util.*;

import javax.swing.event.*;
import javax.swing.tree.*;

class FileTreeModel implements TreeModel {
    private ArrayList<TreeModelListener> listeners;
    private FileWrap root;
    
    public FileTreeModel() {
		this.listeners = new ArrayList<TreeModelListener>();
		this.root = new FileWrap();
    }
    
    public void addTreeModelListener(TreeModelListener l){
    	listeners.add(l);
    }
    
    public void removeTreeModelListener(TreeModelListener l){
    	listeners.remove(l);
    }
    
    public Object getChild(Object parent, int index){
		FileWrap bt = (FileWrap)parent;
		
		if (bt == null) return null;
		
		String[] children = bt.list();
		return new FileWrap(bt.getPath(), children[index]);
	}
    
    public int getChildCount(Object parent){
    	FileWrap bt = (FileWrap)parent;
		if (bt == null) return 0;
		if (bt.isDirectory()) {
			String[] fileList = bt.list(); // Itt semmi értelme a rendezésnek
			if (fileList != null)
				return fileList.length;
		}
		return 0;
    }
    
    public int getIndexOfChild(Object parent, Object child){
		FileWrap par = (FileWrap)parent;
		FileWrap chi = (FileWrap)child;
		
		if (par == null) return -1;
		if (chi == null) return -1;
		
		String[] list = par.list();
		Arrays.sort(list);
		for (int i = 0; i < list.length; i++) {
			if (chi.getPath().equals(list[i]))
				return i;
		}
		return -1;
    }
    
    public Object getRoot(){
    	return this.root;
    }
    
    /**
     * Beállítja az új gyökeret és szól a listenereknek.
     * Ha be tudta állítani a gyökeret, akkor igazzal tér vissza.
     * @param newRoot Az új gyökere a fának
     * @return Igaz, ha a gyökér új. Hamis, ha nem tudta beállítani
     */
    public boolean setRoot(Object newRoot) {
    	// nem létezõ, vagy null file esetén marad a régi root
    	if (newRoot != null) {
    		FileWrap temp = (FileWrap)newRoot;
    		if (temp.exists()) {
    			this.root = temp;
	    		//View értesítés
	        	for (TreeModelListener listener : listeners) {
	    			listener.treeStructureChanged(new TreeModelEvent(this, new TreePath(this.root)));
	    		}
	        	return true;
    		}
        }
    	
    	return false;
    }
    
    public boolean isLeaf(Object node){ //aki nem directory
    	FileWrap bt = (FileWrap)node;
    	return bt.isFile();
    }
        
    public void valueForPathChanged(TreePath path, Object newValue){
    	
    }
}