package rhythm.display;

/**
 * Abstract class for holding, rendering, and updating containers. 
 * Used to decide the size and layout of a set of containers. 
 * @author Kevin
 *
 */
public abstract class Frame {
	public abstract void render();
	public abstract void update(double delta);
	public abstract void keyEvent(int key, int action, int mods);
	public abstract void mouseButtonEvent(int button, int action, int mods);
	public abstract void mousePosEvent(float xpos, float ypos);
}