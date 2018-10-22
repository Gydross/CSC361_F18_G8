package com.libgdx.csc361_f18_g8.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.libgdx.csc361_f18_g8.game.Assets;
import com.libgdx.csc361_f18_g8.util.Constants;
import com.libgdx.csc361_f18_g8.util.CharacterSkin;
import com.libgdx.csc361_f18_g8.util.GamePreferences;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
//Ch10
import com.badlogic.gdx.math.MathUtils;
import com.libgdx.csc361_f18_g8.util.AudioManager;
//ch12
import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Bunny head object which is the main
 * character of the game and the object the
 * player controls.
 * @author Connor
 *
 */
public class BunnyHead extends AbstractGameObject
{
	public static final String TAG = BunnyHead.class.getName();

	private final float JUMP_TIME_MAX = 0.3f;
	private final float JUMP_TIME_MIN = 0.1f;
	private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f;

	public enum VIEW_DIRECTION { LEFT, RIGHT }
	public enum JUMP_STATE { GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING }

	private TextureRegion regHead;

	public VIEW_DIRECTION viewDirection;
	public float timeJumping;
	public JUMP_STATE jumpState;
	public boolean hasFeatherPowerup;
	public float timeLeftFeatherPowerup;

	public ParticleEffect dustParticles = new ParticleEffect();

	//ch12 animations
	private Animation animNormal;
	private Animation animCopterTransform;
	private Animation animCopterTransformBack;
	private Animation animCopterRotate;

	/**
	 * BunnyHead creation method
	 */
	public BunnyHead()
	{
		init();
	}

	/**
	 * Initializes Bunny's bounding box, physics settings, image, states;
	 *   focuses the camera on Bunny as well.
	 */
	public void init()
	{
		dimension.set(1,1);
		regHead = Assets.instance.bunny.head;
		animNormal = Assets.instance.bunny.animNormal;
		animCopterTransform = Assets.instance.bunny.animCopterTransform;
		animCopterTransformBack =
				Assets.instance.bunny.animCopterTransformBack;
		animCopterRotate = Assets.instance.bunny.animCopterRotate;
		setAnimation(animNormal);

		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);

		// Bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);

		// Set physics values
		terminalVelocity.set(3.0f, 4.0f);
		friction.set(12.0f, 0.0f);
		acceleration.set(0.0f, -25.0f);

		// View direction
		viewDirection = VIEW_DIRECTION.RIGHT;

		// Jump state
		jumpState = JUMP_STATE.FALLING;
		timeJumping = 0;

		// Powerups
		hasFeatherPowerup = false;
		timeLeftFeatherPowerup = 0;

		// Particles
		dustParticles.load(Gdx.files.internal("particles/dust.pfx"), Gdx.files.internal("particles"));
	}

	/**
	 * Changes the current jumping state.
	 * @param jumpKeyPressed: Has the player pressed the jump key or not?
	 */
	public void setJumping(boolean jumpKeyPressed)
	{
		// God, I love switch statements. So clean.
		switch (jumpState)
		{
		case GROUNDED:  // Character is standing on a platform
			if (jumpKeyPressed)
			{
				AudioManager.instance.play(Assets.instance.sounds.jump);

				// Start counting jump time from the beginning
				timeJumping = 0;
				jumpState = JUMP_STATE.JUMP_RISING;
			}
			break;

		case JUMP_RISING:  // Rising in the air
			if (!jumpKeyPressed)
				jumpState = JUMP_STATE.JUMP_FALLING;
			break;

		case FALLING:       // Falling down
		case JUMP_FALLING:  // Falling down after jump
			if (jumpKeyPressed && hasFeatherPowerup)
			{
				AudioManager.instance.play(
						Assets.instance.sounds.jumpWithFeather, 1,
						MathUtils.random(1.0f, 1.1f));
				timeJumping = JUMP_TIME_OFFSET_FLYING;
				jumpState = JUMP_STATE.JUMP_RISING;
			}
			break;
		}
	}

	/**
	 * If Bunny has picked up a feather powerup, sets hasFeatherPowerup to true
	 *   and grants the powerup a time limit.
	 * @param pickedUp: Was the powerup picked up or not?
	 */
	public void setFeatherPowerup(boolean pickedUp)
	{
		hasFeatherPowerup = pickedUp;
		if (pickedUp)
		{
			timeLeftFeatherPowerup = Constants.ITEM_FEATHER_POWERUP_DURATION;
		}
	}

	/**
	 * Determines whether or not Bunny has an active feather powerup.
	 * @return true iff Bunny has a feather powerup AND the feather powerup
	 *   isn't expired.
	 */
	public boolean hasFeatherPowerup()
	{
		return hasFeatherPowerup && timeLeftFeatherPowerup > 0;
	}

	/**
	 * Updates Bunny's position, orientation, and powerup state
	 */
	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);

		// If Bunny's velocity is less than 0, make them face left; otherwise, they face right
		if (velocity.x != 0)
			viewDirection = velocity.x < 0 ? VIEW_DIRECTION.LEFT : VIEW_DIRECTION.RIGHT;

		if (timeLeftFeatherPowerup > 0)
		{
			if (animation == animCopterTransformBack) 
			{
				// Restart "Transform" animation if another feather power-up
				// was picked up during "TransformBack" animation. Otherwise,
				// the "TransformBack" animation would be stuck while the
				// power-up is still active.
				setAnimation(animCopterTransform);
			}
			timeLeftFeatherPowerup -= deltaTime;
			if (timeLeftFeatherPowerup < 0)
			{
				// Disable the powerup
				timeLeftFeatherPowerup = 0;
				setFeatherPowerup(false);
				setAnimation(animCopterTransformBack);

			}
		}
		dustParticles.update(deltaTime);
		// Change animation state according to feather power-up
		if (hasFeatherPowerup) 
		{
			if (animation == animNormal) 
			{
				setAnimation(animCopterTransform);
			}
			else if (animation == animCopterTransform) 
			{
				if (animation.isAnimationFinished(stateTime))
					setAnimation(animCopterRotate);
			}
		} 
		else
		{
			if (animation == animCopterRotate)
			{
				if (animation.isAnimationFinished(stateTime))
					setAnimation(animCopterTransformBack);
			} 
			else if (animation == animCopterTransformBack) 
			{
				if (animation.isAnimationFinished(stateTime))
					setAnimation(animNormal);
			}
		}
	}

	/**
	 * Tracks time spent in mid-air and changes jumping states accordingly
	 */
	@Override
	protected void updateMotionY (float deltaTime)
	{
		switch (jumpState)
		{
		case GROUNDED:
			jumpState = JUMP_STATE.FALLING;
			break;

		case JUMP_RISING:
			// Keep track of jump time
			timeJumping += deltaTime;

			// Jump time left?
			if (timeJumping <= JUMP_TIME_MAX)
			{
				// Still jumping
				velocity.y = terminalVelocity.y;
			}
			break;

		case FALLING:
			break;

		case JUMP_FALLING:
			// Add delta times to track jump time
			timeJumping += deltaTime;

			// Jump to minimal height if jump key was pressed too short
			if (timeJumping > 0 && timeJumping <= JUMP_TIME_MIN)
			{
				// Still jumping
				velocity.y = terminalVelocity.y;
			}
			break;
		}
		if (jumpState != JUMP_STATE.GROUNDED)
		{
			dustParticles.allowCompletion();
			super.updateMotionY(deltaTime);
		}
	}

	@Override
	public void render (SpriteBatch batch)
	{
		TextureRegion reg = null;

		// Draw Particles
		dustParticles.draw(batch);

		// Apply skin color
		batch.setColor(CharacterSkin.values()[GamePreferences.instance.charSkin].getColor());

		float dimCorrectionX = 0;
		float dimCorrectionY = 0;
		if (animation != animNormal)
		{
			dimCorrectionX = 0.05f;
			dimCorrectionY = 0.2f;
		}
		// Set special color when game object has a feather powerup
		if (hasFeatherPowerup)
			batch.setColor(1.0f, 0.8f, 0.0f, 1.0f);

		// Draw image
		reg = (TextureRegion) animation.getKeyFrame(stateTime, true);
		batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x + dimCorrectionX, dimension.y + dimCorrectionY, scale.x, scale.y, 
				rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), viewDirection == VIEW_DIRECTION.LEFT, false);

		// Reset color to white
		batch.setColor(1,1,1,1);
	}
}
