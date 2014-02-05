package org.me.rain_2d.level;

import java.util.Random;

import org.me.rain_2d.entity.mob.NPC;
import org.me.rain_2d.graphics.textures.Texture;

public class MapNpc extends NPC
{
	int x, y;
	int moveTimer;

	public MapNpc(int x, int y, Texture sheet, Level level)
	{
		super(sheet);
		this.x = x;
		this.y = y;
		this.level = level;
	}

	public void update()
	{
		moveTimer++;
		if (moveTimer % 13 == 0) {
			int i;
			Random random = new Random();
			i = random.nextInt(3);
			if (i == 0) {
				move(-1, 0);
			} else if (i == 1) {
				move(0, -1);
			} else if (i == 2) {
				move(1, 0);
			} else if (i == 3) {
				move(1, 1);
			}
		}else{
			move(0,0);
		}
	}

}
