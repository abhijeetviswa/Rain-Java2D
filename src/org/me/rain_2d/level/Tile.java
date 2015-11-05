package org.me.rain_2d.level;

import org.me.rain_2d.Game;
import org.me.rain_2d.graphics.Screen;
import org.me.rain_2d.graphics.textures.Sprite;

public class Tile
{

    String tex;
    int texX, texY;
    boolean emptyTile;
    boolean collision;
    Sprite sprite;

    public Tile(String tex, int texX, int texY)
    {
        this.tex = tex;
        this.texX = texX;
        this.texY = texY;
        this.emptyTile = false;
    }

    public Tile(boolean emptyTile)
    {
        this.emptyTile = true;
    }

    public Sprite getSprite()
    {
        if (!emptyTile)
        {
            if (sprite == null)
            {
                sprite = new Sprite(32, texX, texY, Game.getGame().textureCacher.getTilesetTexture(tex));
                return sprite;
            } else
            {
                return sprite;
            }
        }
        return null;
    }

    public void render(int x, int y, Screen screen)
    {
        Sprite tempSprite = getSprite();
        if (tempSprite != null)
        {
            screen.renderTile(screen.convertMapX(x), screen.convertMapY(y), screen.getXOffset(), screen.getYOffset(), tempSprite);
        }
    }

    public boolean isCollidable()
    {
        return collision;
    }

    public void setCollidable(boolean val)
    {
        collision = val;
    }

}
