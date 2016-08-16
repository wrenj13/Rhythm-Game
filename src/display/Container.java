package display;

import java.util.Comparator;
import java.util.TreeSet;


public class Container{
	
	private TreeSet<Component> children;
	private int childrenIDCounter = 0;
	
	public Container() {
		this.children = new TreeSet<Component>(new Comparator<Component>() {
			@Override
			public int compare(Component o1, Component o2){
				return (o1.getPriority() == o2.getPriority()) ? (o1.getID() > o2.getID() ? -1 : 1) : (o1.getPriority() > o2.getPriority()) ? 1 : -1;
			}
		});
	}
	
	public synchronized void addChild(Component child){
		child.setID(childrenIDCounter++);
		children.add(child);
	}
	
	public synchronized void render(){		
		for(Component child: children){
			child.render();
		}
	}
	
	public void update(double delta) {
		for(Component child: children){
			child.update(delta);
		}
	}

}
