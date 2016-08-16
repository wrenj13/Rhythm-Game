package display;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

public class Image extends Component{
	Texture texture;
	
	public Image(Texture texture, float x, float y, float width, float height, int priority) {
		super(x, y, width, height, priority);
		this.texture = texture;
	}
	
	public Image(Texture texture, float x, float y, float width, float height) {
		this(texture, x, y, width, height, DEFAULT_PRIORITY);
	}

	@Override
	public void render() {
		texture.bind();
		Color.orange.bind();
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex2f(this.getX(), this.getY());
			glTexCoord2f(((float) texture.getImageWidth()) / texture.getTextureWidth(), 0);
			glVertex2f(this.getX() + this.getWidth(), this.getY());
			glTexCoord2f(((float) texture.getImageWidth()) / texture.getTextureWidth(), ((float) texture.getImageHeight()) / texture.getTextureHeight());
			glVertex2f(this.getX() + this.getWidth(), this.getY() + this.getHeight());
			glTexCoord2f(0, ((float) texture.getImageHeight()) / texture.getTextureHeight());
			glVertex2f(this.getX(), this.getY() + this.getHeight());
		glEnd();
		
	}

	@Override
	public void update(double delta) {
	}

}
