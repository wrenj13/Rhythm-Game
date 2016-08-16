package rhythm.input;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

public class Input extends GLFWKeyCallback{

	public static boolean[] keys = new boolean[65535];
	public static boolean[] pKeys = new boolean[65535];
	
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		keys[key] = action != GLFW_RELEASE;
		if(action == GLFW_RELEASE){
			System.out.println("Key Released");
			pKeys[key] = true;
		}
	}

	public static boolean isKeyDown(int keycode) {
		return keys[keycode];
	}
	
	public static boolean isKeyUp(int keycode){
		return pKeys[keycode];
	}
	
	
}