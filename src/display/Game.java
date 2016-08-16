package display;

import java.io.File;

import model.MusicReader;
import model.RhythmEvent;

import org.lwjgl.glfw.GLFW;

public class Game {
	Display d;
	
	public Game(){
		d = new Display(new GameFrame(new Container()), 1000, 1000, "Game");
	}
	
	
	public void playSong(RhythmEvent[] events){
		GameFrame g = new GameFrame(new Container());
		Thread t = (new Thread(d));
		t.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		for (RhythmEvent e: events){
			g.notesContainer.addChild(new Note(d.getTexture("test"), .1f *e.getCircleIndex(), 0f, .1f, .1f, e.getStartTime() / 1000f));
		}
		d.setFrame(g);

		
	}
	
	public static void main(String[] args) {
		Game g = new Game();
		
		MusicReader mr = new MusicReader();
		RhythmEvent[] melodyEvents = MusicReader.createYouBelongWithMeTS();
		g.playSong(melodyEvents);
		/* Uncomment to play the music
		 try {
			mr.playMusic(melodyEvents, (float) 0.0, 60);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		 */
		// Do display stuff here
	}
}
