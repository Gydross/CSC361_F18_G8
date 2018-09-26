package com.libgdx.csc361_f18_g8.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.libgdx.csc361_f18_g8.game.Assets;

/**
 * Gold Coin class which extends off of
 * AbstractGameObject
 * @author Connor Orischak
 *
 */
public class GoldCoin extends AbstractGameObject
{
	/**
	 * initializes gold coin texture
	 * and creates new boolean value
	 * that knows if the coin has been 
	 * collected
	 */
	private TextureRegion regGoldCoin;
	public boolean collected;

	/**
	 * calls the initialization method 
	 * when a coin needs to be created
	 */
	public GoldCoin () 
	{
		init();
	}	
	/**
	 * sets all values for the coin
	 * where it is, and gives it texture
	 */
	private void init () 
	{
		dimension.set(0.5f, 0.5f);
		regGoldCoin = Assets.instance.goldCoin.goldCoin;
		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		collected = false;
	}
	/**
	 * draws the coin on the screen
	 */
	public void render (SpriteBatch batch)
	{
		if (collected) return;
		TextureRegion reg = null;
		reg = regGoldCoin;
		batch.draw(reg.getTexture(), position.x, position.y,
				origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
				rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}
	/**
	 * adds 100 to the score
	 * @return 100 points
	 */
	public int getScore() 
	{
		return 100;
	}
}
