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
			return new Sprite(32, texX, texY, Game.getGame().textureCacher.getTilesetTexture(tex));
		}
		return null;
	}

	public void render(int x, int y, Screen screen)
	{
		Sprite sprite = getSprite();
		if (sprite != null)
		{
			screen.renderTile(screen.convertMapX(x) + screen.getXOffset(), screen.convertMapY(y) + screen.getYOffset(), sprite);
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
