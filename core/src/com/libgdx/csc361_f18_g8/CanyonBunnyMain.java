package com.libgdx.csc361_f18_g8;

import com.badlogic.gdx.ApplicationListener;
import com.libgdx.csc361_f18_g8.game.WorldController;
import com.libgdx.csc361_f18_g8.game.WorldRenderer;

public class CanyonBunnyMain implements ApplicationListener
{
    private static final String TAG = CanyonBunnyMain.class.getName();
    
    private WorldController worldController;
    private WorldRenderer worldRenderer;
    
    @Override public void create () { }
    @Override public void render () { }
    @Override public void resize (int width, int height) { }
    @Override public void pause () { }
    @Override public void resume () { }
    @Override public void dispose () { }
}
