package com.libgdx.csc361_f18_g8.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.libgdx.csc361_f18_g8.util.CameraHelper;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.libgdx.csc361_f18_g8.game.objects.Rock;
import com.libgdx.csc361_f18_g8.util.Constants;

/**
 * WorldController
 * Manipulates the game world directly, loads assets,
 *   and establishes player controls.
 * @author Connor Orischak
 */
public class WorldController extends InputAdapter
{
	private static final String TAG = 
			WorldController.class.getName();

	public CameraHelper cameraHelper;


	public WorldController() 
	{
		init();
	}
	private void init() 
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		initLevel();
	}
	public Level level;
	public int lives;
	public int score;
	private void initLevel () 
	{
		score = 0;
		level = new Level(Constants.LEVEL_01);
	}
	/**
	 * Update Chapter 5:
	 * removed the specified methods
	 * instructed by the textbook.
	 * @param width
	 * @param height
	 * @return
	 */
	//	private void initTestObjects()
	//	{
	//		// Create array of 5 test sprites
	//		testSprites = new Sprite[5];
	//		
	//		// Create a list of texture regions
	//		Array<TextureRegion> regions = new Array<TextureRegion>();
	//		regions.add(Assets.instance.bunny.head);
	//		regions.add(Assets.instance.feather.feather);
	//		regions.add(Assets.instance.goldCoin.goldCoin);
	//		
	//		// Create new sprites using a random texture region
	//		for(int i = 0; i < testSprites.length; i++)
	//		{
	//			Sprite spr = new Sprite(texture);
	//			// Define sprite size to be 1x1m in game world
	//			spr.setSize(1, 1);
	//			
	//			//Set origin to sprite's center
	//			spr.setOrigin(spr.getWidth()/ 2.0f, spr.getHeight()/2.0f);
	//			
	//			// Calculate random position for sprite
	//			float randomX = MathUtils.random(-2.0f,2.0f);
	//			float randomY = MathUtils.random(-2.0f,2.0f);
	//			spr.setPosition(randomX, randomY);
	//			
	//			// Insert new sprite into the array
	//			testSprites[i] = spr;
	//		}
	//		selectedSprite = 0;
	//	}

	private Pixmap createProceduralPixmap(int width, int height)
	{
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		pixmap.setColor(1, 0, 0, 0.5f);
		pixmap.fill();
		//Draw yellow colored X shape on square
		pixmap.setColor(1, 1, 0, 1);
		pixmap.drawLine(0, 0, width, height);
		pixmap.drawLine(width, 0, 0, height);
		//Draw cyan colored border around square
		pixmap.setColor(0, 1, 1, 1);
		pixmap.drawRectangle(0, 0, width, height);
		return pixmap;
	}
	/**
	 * removed the call of updateTestOjects
	 * @param deltaTime
	 */
	public void update(float deltaTime) 
	{
		handleDebugInput(deltaTime);
		cameraHelper.update(deltaTime);

	}
	private void handleDebugInput(float deltaTime)
	{
		if (Gdx.app.getType() != ApplicationType.Desktop) return;
		//		//Selected Sprite controls
		//		float sprMoveSpeed = 5 * deltaTime;
		//		if (Gdx.input.isKeyPressed(Keys.A)) moveSelectedSprite(
		//				-sprMoveSpeed,0);
		//		if (Gdx.input.isKeyPressed(Keys.D))
		//				moveSelectedSprite(sprMoveSpeed,0);
		//		if (Gdx.input.isKeyPressed(Keys.W)) moveSelectedSprite(0,
		//				sprMoveSpeed);
		//		if (Gdx.input.isKeyPressed(Keys.S)) moveSelectedSprite(0,
		//				-sprMoveSpeed);

		// Camera controls (move)
		float camMoveSpeed = 5 * deltaTime;
		float camMoveSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camMoveSpeed *=
				camMoveSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed,0);
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed,0);
		if (Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0,camMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0, -camMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) cameraHelper.setPosition(0,0);
		// CameraControlls (zoom)
		float camZoomSpeed = 1*deltaTime;
		float camZoomSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camZoomSpeed *= 
				camZoomSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.COMMA))
			cameraHelper.addZoom(camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.PERIOD))
			cameraHelper.addZoom(-camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.SLASH)) cameraHelper.setZoom(1);
	}

	private void moveCamera (float x, float y)
	{
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x,y);
	}

	//	private void moveSelectedSprite(float x, float y)
	//	{
	//		testSprites[selectedSprite].translate(x, y);
	//	}
	//	private void updateTestObjects(float deltaTime)
	//	{
	//		//Get current rotation from selected sprite
	//		float rotation = testSprites[selectedSprite].getRotation();
	//		//Rotate sprite by 90 degrees per second
	//		rotation += 90 * deltaTime;
	//		//Wrap around at 360 degrees
	//		rotation %= 360;
	//		// set new rotation value to selected sprite
	//		testSprites[selectedSprite].setRotation(rotation);
	//	}

	@Override
	public boolean keyUp(int keycode)
	{
		// Reset game world
		if (keycode == Keys.R)
		{
			init();
			Gdx.app.debug(TAG, "Game world resetted");
		}
		//		else if (keycode == Keys.SPACE)
		//		{
		//			// Select next sprite
		//			selectedSprite = (selectedSprite +1) % testSprites.length;
		//			// Update camera's target to follow the currently
		//			// selected sprite
		//			if (cameraHelper.hasTarget())
		//			{
		//				cameraHelper.setTarget(testSprites[selectedSprite]);
		//			}
		//			Gdx.app.debug(TAG, "Sprite #"+selectedSprite+" selected.");
		//			
		//		}
		//		// Toggle camera follow
		//		else if (keycode == Keys.ENTER)
		//		{
		//			cameraHelper.setTarget(cameraHelper.hasTarget() ? null :
		//				testSprites[selectedSprite]);
		//			Gdx.app.debug(TAG, "Camera follow enabled: " +cameraHelper.hasTarget());
		//		}
		return false;
	}

}




