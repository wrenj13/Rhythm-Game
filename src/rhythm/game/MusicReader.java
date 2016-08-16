package rhythm.game;

import java.io.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A class to analyze music file and extract relevant information from it, including beat.
 * 
 * @author REN-JAY_2
 *
 */
public class MusicReader {

	public static final int NOTE_ON = 0x90;
	public static final int NOTE_OFF = 0x80;
	public static final int DEFAULT_BPM = 120;
	   
	// Inspired by http://stackoverflow.com/questions/3850688/reading-midi-files-in-java
	/**
	 * This method iterates through a MIDI file and extracts information on all the events in the given track with a velocity > 0.
	 * It stores the information in a RhythmEvent array.
	 * 
	 * @param file The MIDI file to examine
	 * @param trackNumber The number of the track to process
	 * @param bpm The beats per minute of the song. 0 => default BPM, which is 120
	 * @return A RhythmEvent array of information
	 */
	public RhythmEvent[] extractMidiInformation(File file, int trackNumber, int bpm) {
		if (bpm == 0) {
			bpm = DEFAULT_BPM;
		}
		ArrayList<RhythmEvent> eventList = new ArrayList<RhythmEvent>();
		int deltaTime = 20; // minimum time between events, in ms
		try {
			Sequence sequence = MidiSystem.getSequence(file);
			double ticksPerBeat = sequence.getResolution();
			Track[] tracks = sequence.getTracks();
			int lastTime = -20;
			Track t = tracks[trackNumber];
			// Get the main melody and percussion
			for (int index = 0; index < t.size(); index++) {
				MidiEvent me = t.get(index);
				MidiMessage msg = me.getMessage();
				if (msg instanceof ShortMessage) {
					ShortMessage shortMsg = (ShortMessage) msg;
					if (shortMsg.getCommand() == NOTE_ON) {
						int velocity = shortMsg.getData2();
						int time = (int) (me.getTick() * 60000 / (ticksPerBeat * bpm));
						// we only want to count the event if it is a certain amount of time after the last event
						if ((time - lastTime) > deltaTime && velocity > 0) {
							eventList.add(new RhythmEvent(velocity, time, shortMsg, me.getTick()));
							lastTime = time;
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		RhythmEvent[] retArray = new RhythmEvent[eventList.size()];
		return eventList.toArray(retArray);
	}
	
	/**
	 * A test method to output information from a RhythmEvent array based on its timing.
	 * 
	 * @param events The events to output information
	 */
	public void playFile(RhythmEvent[] events) {
		class RhythmTask extends TimerTask
		{
			Timer t;
			double currTime = 0.0;
			long period; // in ms
			RhythmEvent[] events;
			int currentEventIndex;
			double delta;
			
			/**
			 * Creates a new Rhythm
			 * 
			 * @param newPeriod the time between each run, in ms
			 * @param newEvents
			 */
			public RhythmTask(long newPeriod, RhythmEvent[] newEvents, Timer newTimer) {
				period = newPeriod;
				events = newEvents;
				currentEventIndex = 0;
				period = newPeriod;
				delta = period / 10000.;
				t = newTimer;
			}
			
			/**
			 * Outputs event information if the event time matches the current time.
			 */
			public void run() {
				RhythmEvent currentEvent = events[currentEventIndex];
				double currEventTime = (currentEvent.getStartTime() / period) * (period / 1000.0);
				System.out.println(currTime + " compared to " + currEventTime);
				
				if (Math.abs(currTime - currEventTime) < delta) {
					System.out.println("Note with velocity " + currentEvent.getVelocity() + " at time " + currTime + " s");
					currentEventIndex++;
				}
				if (currentEventIndex >= events.length) {
					System.out.println("Song over!");
					cancel();
					t.cancel();
				}
				currTime += period / 1000.;
			}
		}
		Timer t = new Timer();
		long delay = 0;
		long period = 100; //ms
		t.schedule(new RhythmTask(period, events, t), delay, period); // Every 100ms starting from t=0ms
	}
	
	public void outputInformation(RhythmEvent[] events) {
		for (RhythmEvent e : events) {
			System.out.println("Start time: " + e.getStartTime() + " ms, with velocity " + e.getVelocity());
		}
	}

	/**
	 * Plays the music from a given set of RhythmEvents, given the division type and the resolution (ticks per beat) of the sequence.
	 * The method is suitable for a one-track song.
	 * 
	 * @param events The array of RhythmEvent objects
	 * @param divType PPQ or one of the SMPTE types
	 * @param resolution Ticks per beat
	 * @throws InvalidMidiDataException 
	 * @throws MidiUnavailableException 
	 */
	public void playMusic(RhythmEvent[] events, float divType, int resolution, int BPM) throws InvalidMidiDataException, MidiUnavailableException {
		Sequencer sequencer = null;
		// Get default sequencer.
		sequencer = MidiSystem.getSequencer();
		if (sequencer == null) {
		    System.out.println("Error -- sequencer device is not supported.");
		    return;
		} else {
			sequencer.open();
		}
		Sequence sequence = new Sequence(divType, resolution, 1);
		Track track = sequence.createTrack();
		for (int i = 0; i < events.length; i++) {
			RhythmEvent e = events[i];
			track.add(new MidiEvent(e.getMessage(), e.getTicks()));
		}
		sequencer.setSequence(sequence);
		sequencer.setTempoInBPM(BPM);
		sequencer.start();
	//	sequencer.close();
	}
	
	public void playFile(File file) throws MidiUnavailableException, InvalidMidiDataException, IOException {
		Sequencer sequencer = MidiSystem.getSequencer();
		sequencer.open();
		Sequence sequence = MidiSystem.getSequence(file);
		sequencer.setSequence(sequence);
		sequencer.start();	
	}
	
	/**
	 * Adds all the events in sourceEvents to resultEvents between the start and end times (in ms)
	 * 
	 * @param resultEvents The array in which events are being stored
	 * @param sourceEvents The array from which events are being copied over
	 * @param timeStart The start time
	 * @param timeEnd The end time
	 */
	public void addEvents(ArrayList<RhythmEvent> resultEvents, RhythmEvent[] sourceEvents, int timeStart, int timeEnd) {
		int index = BSfindIndex(sourceEvents, timeStart);
		if (index > sourceEvents.length) {
			return;
		}
		boolean process = true;
		while (process) {
			if (index < sourceEvents.length && sourceEvents[index].getStartTime() <= timeEnd) {
				resultEvents.add(sourceEvents[index]);
				index++;
			} else {
				process = false;
			}
		}
	}
	
	/**
	 * A binary search method to find the index of the event with the given start time
	 * 
	 * @param events The array to search through
	 * @param time The time to search for
	 * @return The index of the time, or the closest index greater than the given time if the time could not be found
	 */
	public static int BSfindIndex(RhythmEvent[] events, int time) {
		int start = 0;
		int end = events.length - 1;
		while (start <= end) {
			int mid = (start + end) / 2;
			if (events[mid].getStartTime() == time) {
				return mid;
			}
			if (events[mid].getStartTime() > time) {
				end = mid - 1;
			}
			else {
				start = mid + 1;
			}
		}
		// couldn't find it so return the closest one after it
		return start;
	}
	
	/**
	 * Creates an easier version of songs by removing half of the RhythmEvents.
	 * 
	 * @param events The events to prune
	 * @return The new array of RhythmEvents
	 */
	public static RhythmEvent[] createEasyVersion(RhythmEvent[] events) {
		RhythmEvent[] ret = new RhythmEvent[events.length / 2];
		for (int i = 1; i < events.length; i+=2) {
			ret[i / 2] = events[i];
		}
		return ret;
	}

	/**
	 * Pseudo-randomly chooses notes in the song to duplicate. The player will press two keys at once in the game.
	 * 
	 * @param events The events to edit
	 * @return The new array of RhythmEvents
	 */
	public static RhythmEvent[] populateDouble(RhythmEvent[] events) {
		ArrayList<RhythmEvent> newEvents = new ArrayList<RhythmEvent>();
		for (int i = 0; i < events.length; i++) {
			newEvents.add(events[i]);
			 // 1 in 5 chance
			if (events[i].getStartTime() % 5 == 0 && events[i].getCircleIndex() != 2) {
				RhythmEvent copy = events[i].copy();
				copy.setCircleIndex(4 - events[i].getCircleIndex());
				newEvents.add(copy);
			}
		}
		RhythmEvent[] retArray = new RhythmEvent[newEvents.size()];
		newEvents.toArray(retArray);
		return retArray;
	}
	
	/**
	 * Creates the array of RhythmEvents for "You belong with me" by Taylor Swift.
	 * This array has been customized for melody and percussion.
	 * 
	 * @return The array of RhythmEvents for "You belong with me" by Taylor Swift
	 */
	public static Song createYouBelongWithMeTS() {
		MusicReader mr = new MusicReader();
		RhythmEvent[] melodyEvents = mr.extractMidiInformation(new File("res/tswift_belong.mid"), 1, 130);
		RhythmEvent[] drumEvents = mr.extractMidiInformation(new File("res/tswift_belong.mid"), 14, 130);
		ArrayList<RhythmEvent> events = new ArrayList<RhythmEvent>();
		mr.addEvents(events, drumEvents, 0, 11000);
		mr.addEvents(events, melodyEvents, 11000, 55000);
		mr.addEvents(events, drumEvents, 55000, 59500);
		mr.addEvents(events, melodyEvents, 59500, 70000);
		mr.addEvents(events, drumEvents, 70000, 73800);
		mr.addEvents(events, melodyEvents, 73800, 195000);
		mr.addEvents(events, drumEvents, 195000, 201000);
		mr.addEvents(events, melodyEvents, 201000, 224000);
		RhythmEvent[] songEvents = new RhythmEvent[events.size()];
		events.toArray(songEvents);
		Song ret = new Song(songEvents, "You Belong With Me", "Taylor Swift", "res/tswift_belong.wav", 130, 10);
		return ret;
	}
	
	public static Song createGee() {
		MusicReader mr = new MusicReader();
		RhythmEvent[] melodyEvents = mr.extractMidiInformation(new File("res/gee.mid"), 1, 100);
		ArrayList<RhythmEvent> events = new ArrayList<RhythmEvent>();
		mr.addEvents(events, melodyEvents, 0, 197000);
		RhythmEvent[] songEvents = new RhythmEvent[events.size()];
		events.toArray(songEvents);
		Song ret = new Song(songEvents, "Gee", "Girl's Generation", "res/gee.mid", 100, 10);
		return ret;
	}
	
	public static void main(String[] args) {
		MusicReader mr = new MusicReader();
		File song = new File("res/gee.mid");
		RhythmEvent[] melodyEvents = mr.extractMidiInformation(song, 1, 100);
	//	RhythmEvent[] rhythmEvents = mr.extractMidiInformation(song, 6, 100);
		ArrayList<RhythmEvent> events = new ArrayList<RhythmEvent>();
		mr.addEvents(events, melodyEvents, 0, 197000);
//		mr.addEvents(events, rhythmEvents, 0, 142000);
		RhythmEvent[] songEvents = new RhythmEvent[events.size()];
		events.toArray(songEvents);
		mr.outputInformation(songEvents);
		try {
			mr.playMusic(songEvents, (float) 0.0, MidiSystem.getSequence(song).getResolution(), 100);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
