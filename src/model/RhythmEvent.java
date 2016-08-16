package model;

import javax.sound.midi.MidiMessage;

/**
 * A class to hold information for a Rhythm game event.
 * 
 * @author REN-JAY_2
 *
 */
public class RhythmEvent {
	private int velocity;
	private int startTime; // in ms
	private long numTicks; 
	private MidiMessage msg; // data from the MidiEvent
	private int circleIndex; // from 0 to 4
	
	/**
	 * Constructs a RhythmEvent object based on a velocity (sound intensity) and a time.
	 * 
	 * @param newVelocity The volume of the note
	 * @param newStartTime The starting time in ms of the note
	 * @param newMsg The MIDI message that came with the MidiEvent
	 * @param newTicks The number of ticks since the beginning of the song of the note
	 */
	public RhythmEvent(int newVelocity, int newStartTime, MidiMessage newMsg, long newTicks) {
		velocity = newVelocity;
		startTime = newStartTime;
		msg = newMsg;
		circleIndex = (int) (Math.random() * 5);
		numTicks = newTicks;
	}
	
	/**
	 * Returns the start time of the event
	 * 
	 * @return The start time of the event
	 */
	public int getStartTime() {
		return startTime;
	}
	
	/**
	 * Returns the sound intensity of the event.
	 * 
	 * @return The sound intensity of the event.
	 */
	public int getVelocity() {
		return velocity;
	}
	
	/**
	 * Returns the circle index
	 * 
	 * @return The circle index
	 */
	public int getCircleIndex() {
		return circleIndex;
	}
	
	/**
	 * Returns the number of ticks since the beginning of the song.
	 * 
	 * @return The number of ticks since the beginning of the song.
	 */
	public long getTicks() {
		return numTicks;
	}
	
	/**
	 * Sets the index of the circle
	 * 
	 * @param newIndex The new index of the circle
	 */
	public void setCircleIndex(int newIndex) {
		circleIndex = newIndex;
	}
	
	/**
	 * Returns the MidiEvent information for the event
	 * 
	 * @return The MidiEvent information for the event
	 */
	public MidiMessage getMessage() {
		return msg;
	}
	
	/**
	 * Returns a deep copy of the RhythmEvent array, with the exception of the byte array.
	 * 
	 * @return A deep copy of the RhythmEvent array, with the exception of the byte array.
	 */
	public RhythmEvent copy() {
		RhythmEvent copy = new RhythmEvent(velocity, startTime, msg, numTicks);
		copy.setCircleIndex(getCircleIndex());
		return copy;
	}
}
