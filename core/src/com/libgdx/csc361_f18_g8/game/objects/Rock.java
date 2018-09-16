package com.libgdx.csc361_f18_g8.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.libgdx.csc361_f18_g8.game.Assets;

/**
 * Rock class object
 * @author Connor Orischak
 *
 */
public class Rock extends AbstractGameObject
{
	private TextureRegion regEdge;
	private TextureRegion regMiddle;
	private int length;
	
	public Rock()
	{
		init();
	}
	
	private void init()
	{
		dimension.set(1,1.5f);
		regEdge = Assets.instance.rock.edge;
		regMiddle = Assets.instance.rock.edge;
		// start length of this rock
		setLength(1);
	}
	
	public void setLength(int length)
	{
		this.length = length;
	}
	
	public void increaseLength(int amount)
	{
		setLength(length + amount);
	}
	
	
}











