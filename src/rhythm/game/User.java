package rhythm.game;

import java.util.ArrayList;

/**
 * A class that represents a user of the application.
 * 
 * @author REN-JAY_2
 *
 */
public class User {

	private String username;
	private ArrayList<Song> unlockedSongs;
	private int points;
	
	public User(String username) {
		unlockedSongs = new ArrayList<Song>();
		this.setUsername(username);
	}
	
	public void addPoints(int numPoints) {
		points += numPoints;
	}
	
	public int getPoints() {
		return points;
	}
	
	public ArrayList<Song> getSongs() {
		return unlockedSongs;
	}
	
	/**
	 * Allows the user to purchase a song.
	 * If there are not enough points, the method return
	 * 
	 * @param newSong The song to purchase
	 */
	public void purchaseSong(Song newSong) {
		if (points < newSong.getPoints()) {
			System.out.println("Not enough points!");
			return;
		}
		points -= newSong.getPoints();
		unlockedSongs.add(newSong);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
