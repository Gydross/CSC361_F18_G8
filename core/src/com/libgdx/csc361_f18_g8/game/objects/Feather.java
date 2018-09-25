package com.libgdx.csc361_f18_g8.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.libgdx.csc361_f18_g8.game.Assets;
/**
 * Feather class that extends off of
 * AbstractGameObject
 * @author Connor Orischak
 *
 */
public class Feather extends AbstractGameObject
{
	/**
	 * instance variables exclusive to the feather
	 */
	private TextureRegion regFeather;
	public boolean collected;

	public Feather ()
	{
		init();
	}
	/**
	 * created boundaries
	 * and confirmed that it
	 * has not been collected
	 */
	private void init ()
	{
		dimension.set(0.5f, 0.5f);
		regFeather = Assets.instance.feather.feather;
		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		collected = false;
	}
	/**
	 * draws the feather on the screen
	 */
	public void render (SpriteBatch batch) 
	{
		if (collected) return;
		TextureRegion reg = null;
		reg = regFeather;
		batch.draw(reg.getTexture(), position.x, position.y,
				origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
				rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}
	/**
	 * adds a score of 250 
	 * @return 250
	 */
	public int getScore() 
	{
		return 250;
	}
}
