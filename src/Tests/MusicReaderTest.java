package tests;

import static org.junit.Assert.*;
import rhythm.game.MusicReader;
import rhythm.game.RhythmEvent;

import org.junit.Test;

public class MusicReaderTest {

	@Test
	public void testEasyVersion() {
		RhythmEvent[] events = MusicReader.createYouBelongWithMeTS().getEvents();
		RhythmEvent[] easyEvents = MusicReader.createEasyVersion(events);
		assertTrue((events.length / 2 - easyEvents.length) < 2);
	}
	
	@Test
	public void testPopulateDouble() {
		RhythmEvent[] events = MusicReader.createYouBelongWithMeTS().getEvents();
		RhythmEvent[] doubleEvents = MusicReader.populateDouble(events);
		assertTrue(events.length < doubleEvents.length);
		// find first one that doesnt match
		for (int i = 0; i < events.length; i++) {
			if (events[i].getStartTime() != doubleEvents[i].getStartTime()) {
				assertEquals(events[i-1].getCircleIndex(), 4 - doubleEvents[i].getCircleIndex());
				assertEquals(events[i-1].getStartTime(), doubleEvents[i].getStartTime());
				assertNotEquals(events[i-1], doubleEvents[i]);
				break;
			}
		}
	}

	@Test
	public void testBS() {
		RhythmEvent[] events = new RhythmEvent[10];
		for (int i = 0; i < 10; i++) {
			events[i] = new RhythmEvent(10, i * 100, null, 0);
		}
		int index = MusicReader.BSfindIndex(events, 500);
		assertEquals(index, 5);
		index = MusicReader.BSfindIndex(events, 750);
		assertEquals(index, 8);
	}
}
