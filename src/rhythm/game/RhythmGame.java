package rhythm.game;

import rhythm.display.Display;

public class RhythmGame {
	
	public RhythmGame(Song s) {
		Display d = new Display("Rhythm Game", 700, 700, s);
		d.start();
	}
	
	
	public static void main(String[] args) {
		RhythmGame game = new RhythmGame(MusicReader.createYouBelongWithMeTS());
	}
}