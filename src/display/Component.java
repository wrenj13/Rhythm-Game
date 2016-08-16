package display;



public abstract class Component {
	
	public static final int DEFAULT_PRIORITY = 0;
	
	private int priority;
	private int ID; 
	private float x, y;
	private float width, height;
		
	public Component(float x, float y, float width, float height, int priority){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public Component(float x, float y, float width, float height){
		this(x, y, width, height, Component.DEFAULT_PRIORITY);
	}
	
	public abstract void render();
	public abstract void update(double delta);
	
	public void setX(float x){ this.x = x; }
	public void setY(float y){ this.y = y; }
	public void setWidth(float width){ this.width = width; }
	public void setHeight(float height){ this.height = height; }
	public void setPriority(int priority) { this.priority = priority; }
	public void setID(int ID){ this.ID = ID; }
	
	public float getX(){ return x; }
	public float getY(){ return y; }
	public float getWidth(){ return width; }
	public float getHeight(){ return height; }
	public float getPriority(){ return priority; }
	public int getID(){ return ID; }
}
