package com.libgdx.csc361_f18_g8.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.libgdx.csc361_f18_g8.screens.MenuScreen;
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
import com.badlogic.gdx.math.Rectangle;
import com.libgdx.csc361_f18_g8.game.objects.Rock;
import com.libgdx.csc361_f18_g8.util.Constants;
import com.libgdx.csc361_f18_g8.game.objects.BunnyHead;
import com.libgdx.csc361_f18_g8.game.objects.BunnyHead.JUMP_STATE;
import com.libgdx.csc361_f18_g8.game.objects.Feather;
import com.libgdx.csc361_f18_g8.game.objects.GoldCoin;
import com.libgdx.csc361_f18_g8.util.AudioManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.libgdx.csc361_f18_g8.game.objects.Carrot;


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
	private float timeLeftGameOverDelay;

	private Game game;

	public float livesVisual;
	public float scoreVisual;
	private boolean goalReached;
	public World b2world;


	/**
	 * Chapter 11 code
	 * initPhysics creates a world with gravity
	 * pulling things down at 9.81 meters per second
	 */
	private void initPhysics () {
		if (b2world != null) b2world.dispose();
		b2world = new World(new Vector2(0, -9.81f), true);
		// Rocks
		Vector2 origin = new Vector2();
		for (Rock rock : level.rocks) {
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.KinematicBody;
			bodyDef.position.set(rock.position);
			Body body = b2world.createBody(bodyDef);
			rock.body = body;
			PolygonShape polygonShape = new PolygonShape();
			origin.x = rock.bounds.width / 2.0f;
			origin.y = rock.bounds.height / 2.0f;
			polygonShape.setAsBox(rock.bounds.width / 2.0f,
					rock.bounds.height / 2.0f, origin, 0);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
		}
	}

	/**
	 * Spawns carrots in the world at a specific
	 * location
	 * @param pos
	 * @param numCarrots
	 * @param radius
	 */
	private void spawnCarrots (Vector2 pos, int numCarrots,	float radius) 
	{
		float carrotShapeScale = 0.5f;
		// create carrots with box2d body and fixture
		for (int i = 0; i<numCarrots; i++)
		{
			Carrot carrot = new Carrot();
			// calculate random spawn position, rotation, and scale
			float x = MathUtils.random(-radius, radius);
			float y = MathUtils.random(5.0f, 15.0f);
			float rotation = MathUtils.random(0.0f, 360.0f)
					* MathUtils.degreesToRadians;
			float carrotScale = MathUtils.random(0.5f, 1.5f);
			carrot.scale.set(carrotScale, carrotScale);
			// create box2d body for carrot with start position
			// and angle of rotation
			BodyDef bodyDef = new BodyDef();
			bodyDef.position.set(pos);
			bodyDef.position.add(x, y);
			bodyDef.angle = rotation;
			Body body = b2world.createBody(bodyDef);
			body.setType(BodyType.DynamicBody);
			carrot.body = body;
			// create rectangular shape for carrot to allow
			// interactions (collisions) with other objects
			PolygonShape polygonShape = new PolygonShape();
			float halfWidth = carrot.bounds.width / 2.0f * carrotScale;
			float halfHeight = carrot.bounds.height /2.0f * carrotScale;
			polygonShape.setAsBox(halfWidth * carrotShapeScale,
					halfHeight * carrotShapeScale);
			// set physics attributes
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			fixtureDef.density = 50;
			fixtureDef.restitution = 0.5f;
			fixtureDef.friction = 0.5f;
			body.createFixture(fixtureDef);
			polygonShape.dispose();
			// finally, add new carrot to list for updating/rendering
			level.carrots.add(carrot);
		}
	}

	/**
	 * checks bunny's collision with the goal
	 */
	private void onCollisionBunnyWithGoal () {
		goalReached = true;
		timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_FINISHED;
		Vector2 centerPosBunnyHead =
				new Vector2(level.bunnyHead.position);
		centerPosBunnyHead.x += level.bunnyHead.bounds.width;
		spawnCarrots(centerPosBunnyHead, Constants.CARROTS_SPAWN_MAX,
				Constants.CARROTS_SPAWN_RADIUS);
	}
	private void backToMenu()
	{
		// Switch to the menu screen
		game.setScreen(new MenuScreen(game));
	}

	/**
	 * Chapter 6 updates
	 */
	// Rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	/**
	 * tests the collision with the Rock
	 * @param rock
	 */
	private void onCollisionBunnyHeadWithRock(Rock rock)
	{
		BunnyHead bunnyHead = level.bunnyHead;
		float heightDifference = Math.abs(bunnyHead.position.y
				- ( rock.position.y + rock.bounds.height));
		if (heightDifference > 0.25f)
		{
			boolean hitRightEdge = bunnyHead.position.x > (
					rock.position.x + rock.bounds.width / 2.0f);
			if (hitRightEdge) 
			{
				bunnyHead.position.x = rock.position.x + rock.bounds.width;
			} else 
			{
				bunnyHead.position.x = rock.position.x -
						bunnyHead.bounds.width;
			}
			return;
		}
		switch (bunnyHead.jumpState)
		{
		case GROUNDED:
			break;
		case FALLING:
		case JUMP_FALLING:
			bunnyHead.position.y = rock.position.y +
			bunnyHead.bounds.height + bunnyHead.origin.y;
			bunnyHead.jumpState = JUMP_STATE.GROUNDED;
			break;
		case JUMP_RISING:
			bunnyHead.position.y = rock.position.y +
			bunnyHead.bounds.height + bunnyHead.origin.y;
			break;
		}
	}
	/**
	 * creates logic for collisions with gold coins
	 * @param goldcoin
	 */
	private void onCollisionBunnyWithGoldCoin(GoldCoin goldcoin) 
	{
		goldcoin.collected = true;
		AudioManager.instance.play(Assets.instance.sounds.pickupCoin);
		score += goldcoin.getScore();
		Gdx.app.log(TAG, "Gold coin collected");
	}
	/**
	 * creates logic for collision with a feather
	 * @param feather
	 */
	private void onCollisionBunnyWithFeather(Feather feather) 
	{
		feather.collected = true;
		AudioManager.instance.play(Assets.instance.sounds.pickupFeather);
		score += feather.getScore();
		level.bunnyHead.setFeatherPowerup(true);
		Gdx.app.log(TAG, "Feather collected");
	}
	/**
	 * tests collisions
	 */
	private void testCollisions () 
	{
		r1.set(level.bunnyHead.position.x, level.bunnyHead.position.y,
				level.bunnyHead.bounds.width, level.bunnyHead.bounds.height);
		// Test collision: Bunny Head <-> Rocks
		for (Rock rock : level.rocks) 
		{
			r2.set(rock.position.x, rock.position.y, rock.bounds.width,
					rock.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionBunnyHeadWithRock(rock);
			// IMPORTANT: must do all collisions for valid
			// edge testing on rocks.
		}
		// Test collision: Bunny Head <-> Gold Coins
		for (GoldCoin goldcoin : level.goldcoins) 
		{
			if (goldcoin.collected) continue;
			r2.set(goldcoin.position.x, goldcoin.position.y,
					goldcoin.bounds.width, goldcoin.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionBunnyWithGoldCoin(goldcoin);
			break;
		}
		// Test collision: Bunny Head <-> Feathers
		for (Feather feather : level.feathers) 
		{
			if (feather.collected) continue;
			r2.set(feather.position.x, feather.position.y,
					feather.bounds.width, feather.bounds.height);
			if (!r1.overlaps(r2)) continue;
			onCollisionBunnyWithFeather(feather);
			break;
		}
	}

	public WorldController(Game game) 
	{
		this.game = game;
		init();
	}

	private void init() 
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		livesVisual = lives;
		timeLeftGameOverDelay = 0;
		initLevel();
	}

	public Level level;
	public int lives;
	public int score;

	private void initLevel () 
	{
		score = 0;
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.bunnyHead);
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
	 * removed the call of updateTestOjects (CH4-5)
	 * Ch6 added isGameOver, and isPlayerInWater
	 * @param deltaTime
	 */
	public void update(float deltaTime) 
	{
		handleDebugInput(deltaTime);
		if (isGameOver()) 
		{
			timeLeftGameOverDelay -= deltaTime;
			if (timeLeftGameOverDelay < 0) backToMenu();
		} 
		else 
		{
			handleInputGame(deltaTime);
		}
		level.update(deltaTime);
		testCollisions();
		cameraHelper.update(deltaTime);
		if (!isGameOver() && isPlayerInWater()) 
		{
			AudioManager.instance.play(Assets.instance.sounds.liveLost);
			lives--;
			if (isGameOver())
				timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
			else
				initLevel();
		}
		level.mountains.updateScrollPosition(cameraHelper.getPosition());
		if (livesVisual > lives)
			livesVisual = Math.max(lives, livesVisual - 1 * deltaTime);
		scoreVisual = Math.min(score, scoreVisual + 250 * deltaTime);
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
		if (!cameraHelper.hasTarget(level.bunnyHead))
		{
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
		//Toggle camera follow
		else if (keycode == Keys.ENTER)
		{
			cameraHelper.setTarget(cameraHelper.hasTarget()
					? null: level.bunnyHead);
			Gdx.app.debug(TAG, "Camera follow enabled: "
					+ cameraHelper.hasTarget());
		} else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
			// Back to the menu
			backToMenu();
		}

		return false;
	}

	/**
	 * allows the bunny to move
	 * @param deltaTime
	 */
	private void handleInputGame (float deltaTime) 
	{
		if (cameraHelper.hasTarget(level.bunnyHead)) 
		{
			// Player Movement
			if (Gdx.input.isKeyPressed(Keys.LEFT))
			{
				level.bunnyHead.velocity.x =
						-level.bunnyHead.terminalVelocity.x;
			} 
			else if (Gdx.input.isKeyPressed(Keys.RIGHT))
			{
				level.bunnyHead.velocity.x =
						level.bunnyHead.terminalVelocity.x;
			} 
			else
			{
				// Execute auto-forward movement on non-desktop platform
				if (Gdx.app.getType() != ApplicationType.Desktop)
				{
					level.bunnyHead.velocity.x =
							level.bunnyHead.terminalVelocity.x;
				}
			}
			// Bunny Jump
			if (Gdx.input.isTouched() ||
					Gdx.input.isKeyPressed(Keys.SPACE))
			{
				level.bunnyHead.setJumping(true);
			} 
			else 
			{
				level.bunnyHead.setJumping(false);
			}
		}
	}

	/**
	 * checks if bunny has any lives left
	 */
	public boolean isGameOver () 
	{
		return lives < 0;
	}
	/**
	 * checks if bunny is in water.
	 * @return sets bunny at a new position
	 */
	public boolean isPlayerInWater () 
	{
		return level.bunnyHead.position.y < -5;
	}

}




