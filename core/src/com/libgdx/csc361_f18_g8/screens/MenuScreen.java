package com.libgdx.csc361_f18_g8.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
/**
 * A class for the menu screen that will display options
 * for the player to choose.
 * @author Connor Orischak
 *
 */
public class MenuScreen extends AbstractGameScreen 
{
	private static final String TAG = MenuScreen.class.getName();

	public MenuScreen(Game game)
	{
		super(game);
	}

	@Override
	public void render(float deltaTime)
	{
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (Gdx.input.isTouched())
			game.setScreen(new GameScreen(game));
	}
	/**
	 * 
	 */
	@Override
	public void resize(int width, int height)
	{
	}
	
	/**
	 * shows the menu
	 */
	@Override
	public void show() 
	{
	}
	/**
	 * hides the menu
	 */
	@Override
	public void hide() 
	{
	}

	/**
	 * pauses the current session
	 */
	@Override
	public void pause() 
	{
	}
}
