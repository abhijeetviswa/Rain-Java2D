package org.me.rain_2d.level;

import org.me.rain_2d.Game;

public class RandomLevel extends Level
{

	//private static final Random random = new Random();

	public RandomLevel(int width, int height)
	{
		super(width, height);
		
	}

	@Override
	protected void generateLevel()
	{
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				//tiles[x + y * width] = 0;
			}
		}
		
		npcs.add(new MapNpc(2, 2, Game.getGame().textureCacher.getCharacterTexture("player"), this));

	}
}