package com.libgdx.csc361_f18_g8.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.libgdx.csc361_f18_g8.game.WorldController;
import com.libgdx.csc361_f18_g8.game.WorldRenderer;

/**
 * Contains the majority of CanyonBunnyMain's old functionality;
 *   compatible with changing screens
 * @author Aaron Wink
 *
 */
public class GameScreen extends AbstractGameScreen
{
    private static final String TAG = GameScreen.class.getName();
    
    private WorldController worldController;
    private WorldRenderer worldRenderer;
    
    private boolean paused;
    
    /**
     * GameScreen creation method
     * @param game: The game the screen is tracking
     */
    public GameScreen(Game game)
    {
        super(game);
    }
    
    @Override
    public void render(float deltaTime)
    {
        // Do not update the game world when paused.
        if (!paused)
        {
            // Update the game world by the time that has passed since the last frame.
            worldController.update(deltaTime);
        }
        
        // Sets the clear screen color to: Cornflower Blue
        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
        
        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Render the game world to the screen
        worldRenderer.render();
    }
    
    @Override
    public void resize(int width, int height)
    {
        worldRenderer.resize(width, height);
    }
    
    /**
     * Essentially the old create() method from CanyonBunnyMain (see deprecated)
     */
    @Override
    public void show()
    {
        worldController = new WorldController(game);
        worldRenderer = new WorldRenderer(worldController);
        Gdx.input.setCatchBackKey(true);
    }
    
    /**
     * Essentially the old dispose() method from CanyonBunnyMain (see deprecated)
     */
    @Override
    public void hide()
    {
        worldRenderer.dispose();
        Gdx.input.setCatchBackKey(false);
    }
    
    @Override
    public void pause()
    {
        paused = true;
    }
    
    @Override
    public void resume()
    {
        super.resume();
        paused = false;
    }
}
