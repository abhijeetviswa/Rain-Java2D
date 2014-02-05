package org.me.rain_2d.level;

import org.me.rain_2d.Game;
import org.me.rain_2d.graphics.textures.Screen;
import org.me.rain_2d.graphics.textures.Sprite;
import org.me.rain_2d.graphics.textures.Texture;

public class Tile
{
	String tex;
	int texX, texY;
	boolean emptyTile;

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
		if (!emptyTile) {
			return new Sprite(32, texX - 1, texY, Game.getGame().textureCacher.getTilesetTexture(tex));
		}
		return null;
	}

	public void render(int x, int y, Screen screen)
	{
		Sprite sprite = getSprite();
		if (sprite != null) {
			screen.renderTile(x, y, sprite);
		}
	}

}
