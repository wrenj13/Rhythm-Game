package rhythm.display;

import java.util.ArrayList;

import rhythm.component.Component;
import rhythm.component.Image;
import rhythm.component.Note;
import rhythm.game.RhythmEvent;
import rhythm.math.Vector3f;
import static org.lwjgl.glfw.GLFW.*;

/**
 * Frame for the gameplay. 
 * @author Kevin
 *
 */
public class GameFrame extends Frame {
	
	/** Container that holds the notes */
	public Container notesContainer;
	public Container buttonContainer;
	
	public static final double LEAD_TIME = Note.START_Y - Note.END_Y / Note.SPEED;

	
	public double elapsedTime = 0;
	
	Note[] notes;
	int index;
	int life = 1000;
	
	public GameFrame(){
		this.notesContainer = new Container();
		this.buttonContainer = new Container();
	}
	
	@Override
	public void render() {
		if(life < 0)
			return;
		
		buttonContainer.render();
		notesContainer.render();
	}

	@Override
	public void update(double delta) {
		elapsedTime += delta;
		
		while (index < notes.length && elapsedTime + LEAD_TIME  > notes[index].getNoteTime()) {
				notesContainer.addChild(notes[index++]);
		}
		
		for(Component c : notesContainer.getComponents()){
			Note n = (Note) c;
			if(n.getPosition().y < 0f){
				System.out.println("MISS");
				life -= 10;
				notesContainer.removeChild(n);
				System.out.println("Life:" + life);
			}
		}
		notesContainer.update(delta);
	}
	
	@Override
	public void keyEvent(int key, int action, int mods){
		
		
		if (action == GLFW_PRESS && 
				(key == GLFW_KEY_Q || key == GLFW_KEY_W || key == GLFW_KEY_E || key == GLFW_KEY_R || key == GLFW_KEY_T)){

			int columnIndex = -1;
			if(key == GLFW_KEY_Q)
				columnIndex = 0;
			else if (key == GLFW_KEY_W)
				columnIndex = 1;
			else if (key == GLFW_KEY_E)
				columnIndex = 2;
			else if (key == GLFW_KEY_R)
				columnIndex = 3;
			else if (key == GLFW_KEY_T)
				columnIndex = 4;
			
			Note n = null;
			for(Component c : notesContainer.getComponents()){
				n = (Note) c;
				if(n.getColumnIndex() == columnIndex && Math.abs(elapsedTime - n.getNoteTime() ) < .1f){
					break;
				}
			}
			if (n == null) {
				return;
			}
			if(n.getColumnIndex () == columnIndex && Math.abs(elapsedTime - n.getNoteTime()) < .1f){
				notesContainer.removeChild(n);
				double timeDiff = Math.abs(elapsedTime - n.getNoteTime());
				if(timeDiff < .01f)
					System.out.println("PERFECT!");
				else if (timeDiff < .05f)
					System.out.println("GREAT!");
				else if (timeDiff < .1)
					System.out.println("GOOD");
			} else {
				life -= 10;
				System.out.println("MISS");
			}
			System.out.println("Life:" + life);
		}
	}
	
	@Override
	public void mouseButtonEvent(int button, int action, int mods) {
		
	}

	@Override
	public void mousePosEvent(float xpos, float ypos) {
		
	}

	/**
	 * Loads the rhythm events into the frame.
	 * @param events the rhythm events to load.
	 */
	public void loadSong(RhythmEvent[] events) {
		notesContainer = new Container();
		index = 0;
		notes = new Note[events.length];
		for(int i = 0; i < events.length; i++)
			notes[i] = new Note(events[i].getCircleIndex(), events[i].getStartTime() / 1000.);
		
		
		buttonContainer = new Container();
		for (int i = 0; i < 5; i++)
			buttonContainer.addChild(new Image("res/button.png", .1f, .1f, new Vector3f(.3f + .1f * i, .1f, 0f)));
		
	}


	


	
}