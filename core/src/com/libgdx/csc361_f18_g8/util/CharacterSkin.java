package com.libgdx.csc361_f18_g8.util;

import com.badlogic.gdx.graphics.Color;

/**
 * Enumerates character skins and their color values.
 * 
 * @author Aaron Wink
 */
public enum CharacterSkin
{
    WHITE("White", 1.0f, 1.0f, 1.0f),
    GRAY("Gray", 0.7f, 0.7f, 0.7f),
    BROWN("Brown", 0.7f, 0.5f, 0.3f);
    
    private String name;
    private Color color = new Color();
    
    /**
     * Defines a character skin.
     * @param name: The name of the skin
     * @param r: Red
     * @param g: Green
     * @param b: Blue
     */
    private CharacterSkin(String name, float r, float g, float b)
    {
        this.name= name;
        color.set(r,g,b, 1.0f);
    }
    
    @Override
    public String toString()
    {
        return name;
    }
    
    public Color getColor()
    {
        return color;
    }
}
