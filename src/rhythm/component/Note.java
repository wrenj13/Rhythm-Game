package rhythm.component;

import rhythm.graphics.Shader;
import rhythm.graphics.Texture;
import rhythm.math.Vector3f;

public class Note extends Component{
	
	private static final Texture TEXTURE = new Texture("res/circle.png");
	public static final float START_Y = .9f;
	public static final float END_Y = .1f;
	public static final float START_X = .3f;
	public static final float X_INTERVAL = .1f;
	public static final float SPEED = 1f;
	public static final float WIDTH = .1f;
	public static final float HEIGHT = .1f;
	
	/** Index of the column of the note. */
	private int columnIndex;
	/** Time in seconds in which the note should play. */
	private double noteTime;


	
	public Note(int circleIndex, double noteTime) {
		super(new float[] {
				0f, 0f, 0f,
				0f, HEIGHT, 0f,
				WIDTH, HEIGHT, 0f,
				WIDTH, 0f, 0f
		}, Component.DEFAULT_INDICES, Component.DEFAULT_TEX_COORDS, TEXTURE);
		
		this.columnIndex = circleIndex;
		this.noteTime = noteTime;
		this.translate(new Vector3f(.3f + .1f * this.columnIndex, Note.START_Y, 0f));
	}

	@Override
	/**
	 * Renders the note at the appropriate time. 
	 */
	public void render() {
		
		switch (columnIndex) {
			case 0:	
				/** Red */
				Shader.shader2.setUniform3f("color_filter", new Vector3f(1f, 0f, 0f));
				break;
			case 1:
				/** Yellow */
				Shader.shader2.setUniform3f("color_filter", new Vector3f(1f, 1f, 0f));
				break;
			case 2:
				/** Green */
				Shader.shader2.setUniform3f("color_filter", new Vector3f(0f, 1f, 0f));
				break;
			case 3:
				/** Blue */
				Shader.shader2.setUniform3f("color_filter", new Vector3f(0f, 0f, 1f));
				break;
			case 4:
				/** Purple */
				Shader.shader2.setUniform3f("color_filter", new Vector3f(1f, 0f, 1f));
				break;
		}
		render(0, Shader.shader2);
	}

	@Override
	/**
	 * Updates the note by recording time elapsed and moving the note if appropriate. 
	 */
	public void update(double delta) {
		this.translate(new Vector3f(0, (float) -(delta / (START_Y - END_Y) / SPEED * (START_Y - END_Y)), 0));
	}
	
	public int getColumnIndex(){
		return columnIndex;
	}

	public double getNoteTime(){
		return noteTime;
	}

}