package rhythm.display;

import java.io.File;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.JFrame;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GLContext;

import rhythm.game.MusicReader;
import rhythm.game.RhythmEvent;
import rhythm.game.Song;
import rhythm.graphics.Shader;
import rhythm.math.Matrix4f;


public class Display implements Runnable {

	/** Window variables. */
	private long window;
	private int windowWidth, windowHeight;
	private String windowTitle;
	private Song song;
	
	/** Stores the previous time to calculate the difference when updating */
	private double lastTime;
	
	/** The Frame with all the components to render */
	private Frame frame;
	
	/** The location of the mouse */
	private float mouseX, mouseY;
	
	/** Exit flag */
	private boolean exit = false;
	
	/** Callback functions for the associated events */
	private GLFWMouseButtonCallback mouseButtonCallback;
	private GLFWCursorPosCallback mousePosCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWFramebufferSizeCallback framebufferSizeCallback;
	private GLFWErrorCallback errorCallback;
	
	public Display(String title, int width, int height, Song newSong){
		mouseX = 0f;
		mouseY = 0f;
		windowWidth = width;
		windowHeight = height;
		windowTitle = title;
		frame = new EmptyFrame();
		song = newSong;
	}
	
	public void start(){
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		init();
		
		playSong(song);
		
		while (!exit) {
			update();
			render();
		}

		glfwTerminate();
		mouseButtonCallback.release();
		mousePosCallback.release();
		keyCallback.release();
		framebufferSizeCallback.release();
		errorCallback.release();
	}

	/**
	 * Initializes the display and OpenGL features.
	 */
	public void init(){
		if (glfwInit() != GL_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");

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
		glActiveTexture(GL_TEXTURE1);

		glClearColor(1f, 1f, 1f, 0f);


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
				frame.keyEvent(key, action, mods);
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
		
		Shader.loadAll();
				
		
		Matrix4f pr_matrix = Matrix4f.orthographic(0f, 1f, 0f, 1f, -1f, 1f);
		
		Shader.shader1.enable();
		Shader.shader1.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.shader1.setUniform1i("tex", 1);
		Shader.shader1.setUniformMat4f("vw_matrix", Matrix4f.identity());
		Shader.shader1.disable();
		
		Shader.shader2.enable();
		Shader.shader2.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.shader2.setUniform1i("tex", 1);
		Shader.shader2.setUniformMat4f("vw_matrix", Matrix4f.identity());
		Shader.shader2.disable();
		
		glfwShowWindow(window);
	}
	
	/**
	 * Updates all components. 
	 */
	public void update(){
		glfwPollEvents();
		double currTime = GLFW.glfwGetTime();
		frame.update(currTime - lastTime);
		lastTime = currTime;
	}
	
	/**
	 * Renders objects onto the display.
	 */
	public void render(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		frame.render();
		glfwSwapBuffers(window);
	}
	
	/**
	 * Swaps the frame to a new frame. 
	 * @param newFrame the frame that will now be rendered
	 */
	public void changeFrame(Frame newFrame){
		this.frame = newFrame;
	}
	
	/**
	 * Plays You Belong With Me.
	 */
	public void playSong(Song song){
		GameFrame g = new GameFrame();
		g.loadSong(song.getEvents());
		
		try {
			File f = new File(song.getSongPath());
			AudioInputStream stream = AudioSystem.getAudioInputStream(f);
			AudioFormat format = stream.getFormat();
		    DataLine.Info info = new DataLine.Info(Clip.class, format);
		    Clip clip = (Clip) AudioSystem.getLine(info);
		    clip.open(stream);
		    clip.start();
		} catch (Exception e) {
			System.err.println("Error: Could not load music!");
		}
		changeFrame(g);

		lastTime = GLFW.glfwGetTime();
	}
	
}