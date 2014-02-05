package org.me.rain_2d.entity.mob;

import org.me.rain_2d.graphics.textures.Screen;
import org.me.rain_2d.graphics.textures.Texture;

public class NPC extends Mob
{
	
	public NPC(Texture sheet){
		this.sheet = sheet;
	}
	
	@Override
	public void render(Screen screen){
		screen.renderMob(x, y, xOffset, yOffset, getSprite());
	}
}
