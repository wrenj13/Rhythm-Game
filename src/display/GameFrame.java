package display;

public class GameFrame extends Frame {
	public Container notesContainer;
	
	public GameFrame(Container notesContainer){
		this.notesContainer = notesContainer;
	}
	
	@Override
	public void render() {
		
		notesContainer.render();
	}

	@Override
	public void update(double delta) {
		
		notesContainer.update(delta);
	}
}
