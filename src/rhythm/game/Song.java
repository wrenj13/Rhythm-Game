package rhythm.game;

/**
 * A class that represents a song.
 * 
 * @author REN-JAY_2
 *
 */
public class Song {

	private RhythmEvent[] events;
	private String title;
	private String artist;
	private String songPath;
	private int bpm;
	private int points;
	
	public Song(RhythmEvent[] events, String title, String artist, String songPath, int bpm, int points) {
		this.setEvents(events);
		this.setTitle(title);
		this.setArtist(artist);
		this.setSongPath(songPath);
		this.setBpm(bpm);
		this.setPoints(points);
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getBpm() {
		return bpm;
	}

	public void setBpm(int bpm) {
		this.bpm = bpm;
	}

	public RhythmEvent[] getEvents() {
		return events;
	}

	public void setEvents(RhythmEvent[] events) {
		this.events = events;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getSongPath() {
		return songPath;
	}

	public void setSongPath(String songPath) {
		this.songPath = songPath;
	}
	
}
