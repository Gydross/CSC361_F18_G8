package com.libgdx.csc361_f18_g8.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.libgdx.csc361_f18_g8.game.Assets;

/**
 * The water overlay object.
 * 
 * @author Aaron Wink
 */
public class WaterOverlay extends AbstractGameObject
{
    private TextureRegion regWaterOverlay; // Declares the texture region for the object.
    private float         length;          // The length, in meters, the water object is
    
    /**
     * WaterOverlay create method
     * 
     * @param length: The length, in meters, the water object must be
     */
    public WaterOverlay(float length)
    {
        this.length = length;
        init();
    }
    
    private void init()
    {
        dimension.set(length * 10, 3);
        
        regWaterOverlay = Assets.instance.levelDecoration.waterOverlay;
        
        origin.x = -dimension.x / 2;
    }
    
    @Override
    public void render(SpriteBatch batch)
    {
        TextureRegion reg = null;
        reg = regWaterOverlay;
        batch.draw(reg.getTexture(), position.x + origin.x, position.y + origin.y, origin.x, origin.y, dimension.x,
                dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
                reg.getRegionHeight(), false, false);
    }
}
