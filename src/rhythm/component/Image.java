package rhythm.component;

import rhythm.math.Vector3f;

/**
 * A component that displays an image. 
 * 
 * @author Kevin
 *
 */
public class Image extends Component{
	
	public Image(String texturePath, float width, float height, Vector3f position){
		super(new float[] {
				0f, 0f, 0f,
				0f, height, 0f,
				width, height, 0f,
				width, 0f, 0f
		}, DEFAULT_INDICES, DEFAULT_TEX_COORDS, texturePath);
		this.translate(position);
	}
	
	public Image(String texturePath, float width, float height){
		this(texturePath, width, height, new Vector3f());
	}
	
	public Image(String texturePath){
		super(DEFAULT_VERTICES, DEFAULT_INDICES, DEFAULT_TEX_COORDS, texturePath);
	}
	
	@Override
	public void render() {
		this.render(0);
	}
	
	@Override
	public void update(double delta){
		
	}

}