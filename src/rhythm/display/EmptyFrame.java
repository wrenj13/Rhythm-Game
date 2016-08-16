package rhythm.display;

/**
 * Frame that contains, draws, and renders nothing. 
 * @author Kevin
 *
 */
public class EmptyFrame extends Frame{
	public void render(){}
	public void update(double delta){}
	public void keyEvent(int key, int action, int mods){}
	public void mouseButtonEvent(int button, int action, int mods){}
	public void mousePosEvent(float xpos, float ypos){}
}