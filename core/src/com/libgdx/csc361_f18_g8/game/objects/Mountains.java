package com.libgdx.csc361_f18_g8.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.libgdx.csc361_f18_g8.game.Assets;
import com.badlogic.gdx.math.MathUtils;
/**
 * Mountain object class
 * @author Connor Orischak
 *
 */
public class Mountains extends AbstractGameObject
{
	private TextureRegion regMountainLeft;
	private TextureRegion regMountainRight;
	
	private int length;
	
	public Mountains(int length)
	{
		this.length = length;
		init();
	}
	/**
	 * initialization
	 */
	public void init()
	{
		dimension.set(10,2);
		regMountainLeft = Assets.instance.levelDecoration.mountainLeft;
		regMountainRight = Assets.instance.levelDecoration.mountainRight;
		
		//shift mountain and extend length
		
		origin.x = -dimension.x * 2;
		length += dimension.x * 2;
	}
	/**
	 * draws a mountain visible in gameplay
	 * @param batch
	 * @param offsetX
	 * @param offsetY
	 * @param tintColor
	 */
	private void drawMountain (SpriteBatch batch, float offsetX,
		float offsetY, float tintColor)
	{
		TextureRegion reg = null;
		batch.setColor(tintColor, tintColor, tintColor, 1);
		float xRel = dimension.x * offsetX;
		float yRel = dimension.y * offsetY;
		// mountains span the whole level
		int mountainLength = 0;
		mountainLength += MathUtils.ceil(length / (2 * dimension.x));
		mountainLength += MathUtils.ceil(0.5f + offsetX);
		for (int i = 0; i < mountainLength; i++) {
			// mountain left
			reg = regMountainLeft;
			batch.draw(reg.getTexture(), origin.x + xRel, position.y +
					origin.y + yRel, origin.x, origin.y, dimension.x, dimension.y,
					scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
					reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			xRel += dimension.x;
			// mountain right
			reg = regMountainRight;
			batch.draw(reg.getTexture(),origin.x + xRel, position.y +
					origin.y + yRel, origin.x, origin.y, dimension.x, dimension.y,
					scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
			reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			xRel += dimension.x;
		}
		// reset color to white
		batch.setColor(1, 1, 1, 1);
	}
	/**
	 * renders the mountain scenes
	 * @param batch
	 */
	@Override
	public void render (SpriteBatch batch) 
	{
		// distant mountains (dark gray)
		drawMountain(batch, 0.5f, 0.5f, 0.5f);
		// distant mountains (gray)
		drawMountain(batch, 0.25f, 0.25f, 0.7f);
		// distant mountains (light gray)
		drawMountain(batch, 0.0f, 0.0f, 0.9f);
	}
		
	
}
