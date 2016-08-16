package rhythm.component;

import rhythm.graphics.Shader;
import rhythm.graphics.Texture;
import rhythm.graphics.VertexArray;
import rhythm.math.Matrix4f;
import rhythm.math.Vector3f;

/**
 * The abstract class which will be inherited by objects drawn to the display. 
 * 
 * @author Kevin
 *
 */
public abstract class Component {
	/** Default data values */
	public static final int DEFAULT_PRIORITY = 0;
	public static final float[] DEFAULT_VERTICES = new float[]{
			0f, 0f, .2f,
			0f, 1f, .2f,
			1f, 1f, .2f,
			1f, 0f, .2f
	};
	public static final float[] DEFAULT_TEX_COORDS = new float[]{
			0, 1,
			0, 0,
			1, 0,
			1, 1
	};
	public static final byte[] DEFAULT_INDICES = new byte[]{
			0, 1, 2,
			2, 3, 0
	};
	
	/** OpenGL data for use when rendering */
	private VertexArray VAO;
	private Texture[] tex;
	private float[] vertices, texCoords;
	private byte[] indices;
	
	/** Higher priority components will be drawn first */
	private int priority = DEFAULT_PRIORITY;
	
	/** Index of the component in the container. If two components have the same priority, 
	 * the lower index component (inserted first) will be drawn first. */
	private int index;
	
	/** Position of the component */
	public Vector3f position = new Vector3f();
	
	public Component(float[] vertices, byte[] indices, float[] texCoords, String[] texPath){	
		this.vertices = vertices;
		this.indices = indices;
		this.texCoords = texCoords;
		tex = new Texture[texPath.length];
 		for(int i = 0; i < texPath.length; i++)
			tex[i] = new Texture(texPath[i]);
		VAO = new VertexArray(this.vertices, this.indices, this.texCoords);
	}
	
	public Component(float[] vertices, byte[] indices, float[] texCoords, String texPath){
		this(vertices, indices, texCoords, new String[]{ texPath });
	}
	
	public Component(float[] vertices, byte[] indices, float[] texCoords, Texture[] tex){
		this.vertices = vertices;
		this.indices = indices;
		this.texCoords = texCoords;
		this.tex = tex;
		VAO = new VertexArray(this.vertices, this.indices, this.texCoords);
	}
	
	public Component(float[] vertices, byte[] indices, float[] texCoords, Texture tex){
		this(vertices, indices, texCoords, new Texture[]{ tex });
	}
	
	/**
	 * Translates the component by given vector.
	 * @param vector 
	 */
	public void translate(Vector3f vector){
		position.x += vector.x;
		position.y += vector.y;
		position.z += vector.z;
	}
	
	/**
	 * Update the component
	 * @param delta time since last update
	 */
	public abstract void update(double delta);
	/**
	 * Render the component
	 */
	public abstract void render();
	
	/**
	 * Renders the texture at given index with default shader.
	 * @param i index of texture
	 */
	public void render(int i){
		render(i, Shader.shader1);
	}
	
	/**
	 * Renders the texture at given index with given shader.
	 * @param i index of texture
	 * @param shader shader to use when rendering
	 */
	public void render(int i, Shader shader){
		tex[i].bind();
		shader.enable();
		shader.setUniformMat4f("ml_matrix", Matrix4f.translate(position));
		VAO.render();
		shader.disable();
		tex[i].unbind();
	}
	
	
	public Vector3f getPosition(){ return position; }
	public int getPriority(){ return priority; }
	public int getIndex(){ return index; }
	public void setPosition(Vector3f vector){ this.position = vector; }
	public void setPriority(int priority){ this.priority = priority; }
	public void setIndex(int index){ this.index = index; }
}