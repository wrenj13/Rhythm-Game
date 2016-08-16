package display;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

public class Note extends Image{
	public static final float DEFAULT_SPEED_PER_SECOND = 1f;
	
	double startTime;
	float speed;
	
	public Note(Texture texture, float x, float y, float width, float height, double startTime) {
		super(texture, x, y, width, height);
		this.startTime = startTime; //- 1f / DEFAULT_SPEED_PER_SECOND;
		this.speed = DEFAULT_SPEED_PER_SECOND;
	}
	
	@Override
	public void render() {
		if(startTime < 0){
			super.render();
		}
	}

	@Override
	public void update(double delta){
		if(startTime < 0){
			this.setY(this.getY() + .003f);
		} else {
			startTime -= delta;
		}
	}
	
}
