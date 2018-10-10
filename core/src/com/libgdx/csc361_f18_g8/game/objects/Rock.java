package com.libgdx.csc361_f18_g8.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.libgdx.csc361_f18_g8.game.Assets;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

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
	// chapter 8 instance variables for floating rocks
	private final float FLOAT_CYCLE_TIME = 2.0f;
	private final float FLOAT_AMPLITUDE = 0.25f;
	private float floatCycleTimeLeft;
	private boolean floatingDownwards;
	private Vector2 floatTargetPosition;

	public Rock()
	{
		init();
	}

	private void init()
	{
		dimension.set(1,1.5f);
		regEdge = Assets.instance.rock.edge;
		regMiddle = Assets.instance.rock.middle;
		// start length of this rock
		setLength(1);
		// gives floating details to rocks (ch8)
		floatingDownwards = false;
		floatCycleTimeLeft = MathUtils.random(0,
				FLOAT_CYCLE_TIME / 2);
		floatTargetPosition = null;
	}

	public void setLength(int length)
	{
		this.length = length;
		// Update bounding box for collision detection
		bounds.set(0,0,dimension.x*length,dimension.y);
	}

	public void increaseLength(int amount)
	{
		setLength(length + amount);
	}

	@Override
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = null;
		float relX = 0;
		float relY = 0;

		//Draw left edge
		reg = regEdge;
		relX -= dimension.x / 4;
		batch.draw(reg.getTexture(), position.x + relX, position.y + relY,
				origin.x, origin.y, dimension.x/4, dimension.y, scale.x, scale.y, rotation,
				reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		//draw middle
		relX = 0;
		reg = regMiddle;
		for (int i=0; i<length;i++)
		{
			batch.draw(reg.getTexture(), position.x + relX, position.y + relY,
					origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
					reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			relX += dimension.x;
		}
		//draw right edge
		reg = regEdge;
		batch.draw(reg.getTexture(), position.x + relX, position.y + relY,
				origin.x + dimension.x/8, origin.y, dimension.x/4,
				dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), true, false);
	}

	/**
	 * updates the position of the rock
	 * and makes the rock look like it is moving on its own
	 */
	@Override
	public void update (float deltaTime)
	{
		super.update(deltaTime);
		floatCycleTimeLeft -= deltaTime;
		
		if (floatCycleTimeLeft <= 0) 
		{
			floatCycleTimeLeft = FLOAT_CYCLE_TIME;
			floatingDownwards = !floatingDownwards;
			floatTargetPosition.y += FLOAT_AMPLITUDE
					* (floatingDownwards ? -1 : 1);
		} else {
		    body.setLinearVelocity(body.getLinearVelocity().scl(0.98f));
		}
	}
}











