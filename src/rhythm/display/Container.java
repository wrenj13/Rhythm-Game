package rhythm.display;

import java.util.Comparator;
import java.util.TreeSet;

import rhythm.component.Component;

/**
 * Class that holds components to be drawn. 
 * @author Kevin
 *
 */
public class Container{

	/** All components held by the container */
	private TreeSet<Component> components;
	private int componentIDCounter = 0;
	
	public Container() {
		this.components = new TreeSet<Component>(new Comparator<Component>() {
			@Override
			/** Components drawn by highest priority and, in the case of ties, by index. */
			public int compare(Component o1, Component o2){
				if(o1.getPriority() == o2.getPriority()){
					if(o1.getIndex() == o2.getIndex())
						return 0;
					else if (o1.getIndex() < o2.getIndex())
						return 1;
					else if (o2.getIndex() < o1.getIndex())
						return -1;
				} 
				
				return (o1.getPriority() > o2.getPriority()) ? 1 : -1;
			}
		});
	}
	
	public synchronized Component[] getComponents(){
		Component[] componentsArr = components.toArray(new Component[components.size()]);
		return componentsArr;
	}
	
	/**
	 * Adds a component to the container.
	 * @param child component to add
	 */
	public synchronized void addChild(Component child){
		child.setIndex(componentIDCounter++);
		components.add(child);
	}
	
	/**
	 * Removes a component from the container.
	 * @param child component to remove.
	 */
	public synchronized void removeChild(Component child){
		components.remove(child);
		child.setIndex(-1);
	}
	
	/**
	 * Renders all the components in the container.
	 */
	public synchronized void render(){		
		for(Component child: components){
			child.render();
		}
	}
	
	/**
	 * Updates all the components in the container.
	 * @param delta time in seconds since last update.
	 */
	public synchronized void update(double delta) {
		for(Component child: components){
			child.update(delta);
		}
	}

}