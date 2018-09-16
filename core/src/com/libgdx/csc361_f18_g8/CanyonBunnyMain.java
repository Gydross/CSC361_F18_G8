package com.libgdx.csc361_f18_g8;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.libgdx.csc361_f18_g8.game.WorldController;
import com.libgdx.csc361_f18_g8.game.WorldRenderer;
import com.badlogic.gdx.assets.AssetManager;
import com.libgdx.csc361_f18_g8.game.Assets;

/**
 * CanyonBunnyMain The core of the game; handles renderer updates and automates
 * asset disposal.
 * 
 * @author Aaron Wink
 */
public class CanyonBunnyMain implements ApplicationListener
{
	private static final String TAG = CanyonBunnyMain.class.getName();

	private WorldController	worldController;	// The game's dedicated WorldController
	private WorldRenderer	worldRenderer;		// The game's dedicated WorldRenderer

	private boolean paused; // Is the game paused?

	/**
	 * create - Called at game start; loads assets, instantiates worldController and
	 * worldRenderer, and sets the game to unpaused.
	 */
	@Override
	public void create()
	{
		// Set LibGDX log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		// Load assets
		Assets.instance.init(new AssetManager());

		// Initialize controller and renderer
		worldController = new WorldController();
		worldRenderer = new WorldRenderer(worldController);

		// Game world is active at start
		paused = false;
	}

	/**
	 * Calls on worldController to update the game and worldRenderer to update the
	 * display.
	 */
	@Override
	public void render()
	{
		// Do not update the game world when paused.
		if (!paused)
		{
			// Update game world by the time that has passed
			// since last rendered frame.
			worldController.update(Gdx.graphics.getDeltaTime());
		}

		// Sets the clear screen color to: Cornflower Blue
		// Opted not to use hexadecimal for the sake of easy reading.
		Gdx.gl.glClearColor(100 / 255.0f, 149 / 255.0f, 237 / 255.0f, 255 / 255.0f);

		// Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render game world to screen
		worldRenderer.render();
	}

	/**
	 * resize - Resizes the viewport.
	 * 
	 * @param width - Desired viewport width
	 * @param height - Desired viewport height
	 */
	@Override
	public void resize(int width, int height)
	{
		worldRenderer.resize(width, height);
	}

	/**
	 * pause - Pauses the game.
	 */
	@Override
	public void pause()
	{
		paused = true;
	}

	/**
	 * resume - Unpauses the game.
	 */
	@Override
	public void resume()
	{
		paused = false;
	}

	/**
	 * dispose - Clears out unused resources and frees memory.
	 */
	@Override
	public void dispose()
	{
		worldRenderer.dispose();
		Assets.instance.dispose();
	}
}
