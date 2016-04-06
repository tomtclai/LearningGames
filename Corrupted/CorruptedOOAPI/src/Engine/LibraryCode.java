package Engine;

public abstract class LibraryCode extends BaseCode {
	private Rectangle background = null;

	private static final int INIT_WIDTH = 800;
	private static final int INIT_HEIGHT = 480;

	public void initConfig(GameWindow theWindow) {
		super.initConfig(theWindow);

		// window.setSize(800, 600);
		// window.setSize(1200, 675);
		window.setSize(getInitWidth(), getInitHeight());

		// world.SetWorldCoordinate(134.0f);
		world.SetWorldCoordinate(100.0f);
		world.setPosition(0.0f, 0.0f);
	}

	public int getInitWidth() {
		return INIT_WIDTH;
	}

	public int getInitHeight() {
		return INIT_HEIGHT;
	}

	protected Rectangle getBackgroundImage() {
		return background;
	}

	protected void setBackgroundImage(String image) {
		if (background == null) {
			background = new Rectangle();
			background.center.set(world.getWidth() * 0.5f,
					world.getHeight() * 0.5f);
			background.size.set(world.getWidth(), world.getHeight());
			resources.moveToBackOfDrawSet(background);
		}

		background.setImage(image);

		if (background.texture == null) {
			background.visible = false;
		} else {
			background.visible = true;
		}
	}
	
	protected void clear() {}
}
