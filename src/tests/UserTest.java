package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import rhythm.game.MusicReader;
import rhythm.game.Song;
import rhythm.game.User;

public class UserTest {

	User user;
	Song tsOrig;
	Song easyTS;
	Song doubleTS;
	
	@Before
	public void instantiateSongs() {
		user = new User("Bob");
		user.addPoints(100);
		tsOrig = MusicReader.createYouBelongWithMeTS();
		easyTS = new Song(MusicReader.createEasyVersion(tsOrig.getEvents()), tsOrig.getTitle() + ": Easy", tsOrig.getArtist(), tsOrig.getSongPath(), tsOrig.getBpm(), tsOrig.getPoints() / 2);
		doubleTS = new Song(MusicReader.populateDouble(tsOrig.getEvents()), tsOrig.getTitle() + ": Expert", tsOrig.getArtist(), tsOrig.getSongPath(), tsOrig.getBpm(), tsOrig.getPoints() * 2);
	}
	
	@Test
	public void testSongAddition() {
		user.purchaseSong(tsOrig);
		user.purchaseSong(easyTS);
		user.purchaseSong(doubleTS);
		assertEquals(user.getSongs().size(), 3);
		assertSame(user.getSongs().get(1), easyTS);
		assertEquals(user.getPoints(), 100-easyTS.getPoints()-tsOrig.getPoints()-doubleTS.getPoints());
	}
	
	@Test
	public void testNotEnoughPoints() {
		user.purchaseSong(doubleTS);
		user.purchaseSong(doubleTS);
		user.purchaseSong(doubleTS);
		user.purchaseSong(doubleTS);
		user.purchaseSong(doubleTS);
		// 0 points
		assertEquals(user.getSongs().size(), 5);
		user.purchaseSong(easyTS);
		assertEquals(user.getSongs().size(), 5);	
	}

}
