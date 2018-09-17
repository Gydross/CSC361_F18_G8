package com.libgdx.csc361_f18_g8.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.libgdx.csc361_f18_g8.util.Constants;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * WorldRenderer Renders the game world. Also initializes the camera position
 * and camera viewport.
 * 
 * @author Aaron Wink
 */
public class WorldRenderer implements Disposable
{
	private OrthographicCamera	camera;				// The world's camera
	private SpriteBatch			batch;				// Draws sprites passed into it
	private WorldController		worldController;	// The WorldController

	/**
	 * WorldRenderer - Creation method
	 * 
	 * @param worldController - the WorldController to be stored in the WorldRenderer
	 */
	public WorldRenderer(WorldController worldController)
	{
		this.worldController = worldController;
		init();
	}

	/**
	 * init - Instantiates batch and camera, and initializes/updates camera.
	 */
	private void init()
	{
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
	}

	/**
	 * render - Calls renderTestObjects().
	 */
	public void render()
	{
		renderWorld(batch);
	}

	/**
	 * renderWorld - Draws the world.
	 * @param batch: The batch that will be used to draw sprites based on the level data.
	 */
	private void renderWorld(SpriteBatch batch)
	{
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
	}

	/**
	 * resize - Resizes the viewport and updates the camera.
	 * 
	 * @param width - The desired viewport width
	 * @param height - The desired viewport height
	 */
	public void resize(int width, int height)
	{
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
		camera.update();
	}

	/**
	 * Cleans out batch and frees up memory.
	 */
	@Override
	public void dispose()
	{
		batch.dispose();
	}
}
