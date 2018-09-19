package com.libgdx.csc361_f18_g8.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.libgdx.csc361_f18_g8.game.objects.AbstractGameObject;

/**
 * CameraHelper Updates the position of the camera based on a focal point;
 * additionally, handles the zoom of the camera.
 * 
 * @author Connor Orischak
 */
public class CameraHelper
{
    private static final String TAG = CameraHelper.class.getName();
    
    private final float MAX_ZOOM_IN  = 0.25f; // The maximum zoom.
    private final float MAX_ZOOM_OUT = 10.0f; // The minimum zoom.
    
    private Vector2 position;
    private float   zoom;
    
    private AbstractGameObject target;
    
    public CameraHelper()
    {
        position = new Vector2();
        zoom = 1.0f;
    }
    
    /**
     * Updates the position.x and position.y, which will be reflected in applyTo
     * below.
     * 
     * @param deltaTime
     */
    public void update(float deltaTime)
    {
        if (!hasTarget())
            return;
        position.x = target.position.x + target.origin.x;
        position.y = target.position.y + target.origin.y;
    }
    
    public AbstractGameObject getTarget()
    {
        return target;
    }
    
    public void setPosition(float x, float y)
    {
        this.position.set(x, y);
    }
    
    public Vector2 getPosition()
    {
        return position;
    }
    
    public void addZoom(float amount)
    {
        setZoom(zoom + amount);
    }
    
    public void setZoom(float zoom)
    {
        this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
    }
    
    public float getZoom()
    {
        return zoom;
    }
    
    public void setTarget(AbstractGameObject target)
    {
        this.target = target;
    }
    
    public boolean hasTarget()
    {
        return target != null;
    }
    
    /**
     * Determines whether or not the currently stored target is the desired one.
     * 
     * @param target: The target to be compared against.
     * @return true/false depending on whether or not the local target matches the
     *         specified one.
     */
    public boolean hasTarget(AbstractGameObject target)
    {
        return hasTarget() && this.target.equals(target);
    }
    
    /**
     * Changes the zoom and position of a camera, then updates it.
     * 
     * @param camera: The camera to be altered
     */
    public void applyTo(OrthographicCamera camera)
    {
        camera.position.x = position.x;
        camera.position.y = position.y;
        camera.zoom = zoom;
        camera.update();
    }
    
}
