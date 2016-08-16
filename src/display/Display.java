package display;

import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;


public class Display implements Runnable {

	private long window;
	private int windowWidth, windowHeight;
	private String windowTitle;
	
	private double lastTime;
	
	private Frame frame;
	private float mouseX, mouseY;
	private boolean exit = false;
	
	private GLFWMouseButtonCallback mouseButtonCallback;
	private GLFWCursorPosCallback mousePosCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWFramebufferSizeCallback framebufferSizeCallback;
	private GLFWErrorCallback errorCallback;

	private HashMap<String, Texture> textures;
	
	public Display(Frame frame, int width, int height, String title){
		this.frame = frame;
		mouseX = 0f;
		mouseY = 0f;
		windowWidth = width;
		windowHeight = height;
		windowTitle = title;
	
		textures = new HashMap<String, Texture>();
		if (glfwInit() != GL_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");
	}

	@Override
	public void run() {
		init();
		loadTextures();
		lastTime = GLFW.glfwGetTime();
		while (!exit) {
			
			glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			draw();
			update();
			glfwSwapBuffers(window);
			glfwPollEvents();
			
		}

		glfwTerminate();
		mouseButtonCallback.release();
		mousePosCallback.release();
		keyCallback.release();
		framebufferSizeCallback.release();
		errorCallback.release();
	}

	private void init(){
		
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

		window = glfwCreateWindow(windowWidth, windowHeight, windowTitle, NULL, NULL);


		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window!");

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);

		GLContext.createFromCurrent();
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 1, 1, 0, 1, -1);

		glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

		
		glfwSetFramebufferSizeCallback(window, framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
			@Override
			public void invoke(long window, int width, int height) {
				windowWidth = width;
				windowHeight = height;
				glViewport(0, 0, width, height);
			}
	
		});

		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
			}
		});

		glfwSetCursorPosCallback(window, mousePosCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				mouseX = (float) xpos / windowWidth;
				mouseY = (float) ypos / windowHeight;
			}
		});

		glfwSetMouseButtonCallback(window, mouseButtonCallback = new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long window, int button, int action, int mods) {
			}
		});

		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - windowWidth) / 2,
				(GLFWvidmode.height(vidmode) - windowHeight) / 2);
		glfwShowWindow(window);
	}
	
	private void loadTextures(){
		try {
			List<String> textureNames = Files.readAllLines(Paths.get("res/res.properties"));
			for (String s : textureNames){
				System.out.println(s);
				textures.put(s.split("=")[0],
						TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/" + s.split("=")[1]), GL_LINEAR));
				Texture texture = textures.get(s.split("=")[0]);
				texture.bind();
				GL30.glGenerateMipmap(GL_TEXTURE_2D); 
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			}
		} catch (IOException e) {
			System.err.println("Error: Could not load textures!");
			e.printStackTrace();
		}
	}

	public void setFrame(Frame frame){
		this.frame = frame;
	}

	private void draw(){
		frame.render();
	}
	
	private void update(){
		double currTime = GLFW.glfwGetTime();
		frame.update(currTime - lastTime);
		lastTime = currTime;
	}
	
	public Texture getTexture(String name){
		return textures.get(name);
	}
}
