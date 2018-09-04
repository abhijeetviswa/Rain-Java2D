package org.me.rain_2d.level;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.me.rain_2d.Game;
import org.me.rain_2d.graphics.Screen;
import org.me.rain_2d.graphics.textures.Texture;
import static java.lang.Math.toIntExact;

public class Level
{

	private int width, height;
	protected ArrayList<Layer> layers = new ArrayList<Layer>();
	protected ArrayList<MapNpc> npcs = new ArrayList<MapNpc>();

	public Level(int width, int height)
	{
		this.width = width;
		this.height = height;
		if (width < Game.winWidth >> 5) width = Game.winWidth >> 5;
		if (height < Game.winHeight >> 5) height = Game.winHeight >> 5;
		generateLevel();
	}

	public Level(String path)
	{
		loadLevel(path);
	}

	protected void generateLevel()
	{

	}

	@SuppressWarnings("unchecked")
	private void loadLevel(String name)
	{
		ArrayList<Tileset> tilesetList = new ArrayList<Tileset>(0);
		JSONParser parser = new JSONParser();
		try
		{
			JSONObject obj = (JSONObject) parser.parse(new InputStreamReader(getClass().getResourceAsStream("/levels/" + name + ".json")));

			this.setWidth(toIntExact((long) obj.get("width")));
			this.setHeight(toIntExact((long) obj.get("height")));

			// Loading the tilesets
			JSONArray tilesets = (JSONArray) obj.get("tilesets");
			Iterator<JSONObject> tileSetIterator = tilesets.iterator();
			while (tileSetIterator.hasNext())
			{
				JSONObject tileset = tileSetIterator.next();
				Tileset ts = new Tileset();
				ts.fName = (String) tileset.get("name");
				Texture tex =  Game.getGame().textureCacher.getTilesetTexture(ts.fName);
				ts.firstGid = toIntExact((long) tileset.get("firstgid"));
				ts.lastGid = ts.firstGid + (tex.getHeight() * tex.getWidth() / (32 * 32));				
				tilesetList.add(ts);
			}

			// Loading the layers and objects
			JSONArray layers = (JSONArray) obj.get("layers");
			ListIterator<JSONObject> listIterator = layers.listIterator();
			while (listIterator.hasNext())
			{
				JSONObject layer = (JSONObject) listIterator.next();
				String type = ((String) layer.get("type")).trim().toLowerCase();
				int width, height;
				if (type.compareTo("tilelayer") == 0)
				{
					width = toIntExact((long) layer.get("width"));
					height = toIntExact((long) layer.get("height"));
					Layer l = new Layer(width, height);

					int tCounter = 0;
					int tileArr[] = new int[width * height];
					JSONArray data = (JSONArray) layer.get("data");
					Iterator<Long> dataIterator = data.iterator();
					while (dataIterator.hasNext())
					{
						tileArr[tCounter] = toIntExact(dataIterator.next());
						tCounter++;
					}

					parseTilesetData(tileArr, l, tilesetList);
					this.layers.add(l);
					listIterator.remove(); // So that the object group iterator doesn't have to go through tilelayers
				}
			}

			// Get the object groups (stuff like collisions)
			listIterator = layers.listIterator();
			while (listIterator.hasNext())
			{
				JSONObject layer = (JSONObject) listIterator.next();
				String type = ((String) layer.get("type")).trim().toLowerCase();
				if (type.compareTo("objectgroup") == 0)
				{
					String objGroupName = ((String) layer.get("name")).trim().toLowerCase();
					if (objGroupName == null) continue;

					JSONArray objects = (JSONArray) layer.get("objects");
					Iterator<JSONObject> objectIterator = objects.iterator();
					while (objectIterator.hasNext())
					{
						int width, height;
						int x1, y1;
						JSONObject object = (JSONObject) objectIterator.next();
						switch (objGroupName)
						{
						case "collision":
							x1 = toIntExact((long) object.get("x")) / 32;;
							y1 = toIntExact((long) object.get("y")) / 32;;
							width = toIntExact((long) object.get("width")) / 32;
							height = toIntExact((long) object.get("height")) / 32;;
							int x2 = x1 + width;
							int y2 = y1 + height;
							for (int x = x1; x <= x2; x++)
							{
								for (int y = y1; y <= y2; y++)
								{
									Layer l = this.layers.get(0);
									l.getTile(x, y).setCollidable(true);
								}
							}
							break;
						}

					}
				}
			}
		} catch (

		Exception e)
		{
			System.err.println("Error while loading level: " + name);
			e.printStackTrace();
		}
	}

	public void addMapNpc(MapNpc npc)
	{
		if (npc == null) throw new IllegalArgumentException("npc = null");
		npcs.add(npc);
	}

	public void parseTilesetData(int[] tileArr, Layer l, ArrayList<Tileset> sets)
	{
		boolean shouldBreak = false;

		for (int y = 0; y < (l.height); y++)
		{
			for (int x = 0; x < (l.width); x++)
			{
				shouldBreak = false;
				for (Tileset t : sets)
				{
					if (shouldBreak) break;
					if (tileArr[x + y * l.width] >= t.firstGid && tileArr[x + y * l.width] <= t.lastGid)
					{
						Texture tex = Game.getGame().textureCacher.getTilesetTexture(t.fName);
						for (int sx = 0; sx < (tex.getWidth() >> 5); sx++)
						{
							if (shouldBreak) break;
							for (int sy = 0; sy < (tex.getHeight() >> 5); sy++)
							{
								if (shouldBreak) break;
								if (t.firstGid != 1)
								{
									if (sx + sy * (tex.getWidth() >> 5) == (tileArr[(x + y * l.width)]) - t.firstGid)
									{
										l.tile[x][y] = new Tile(t.fName, sx, sy);
										shouldBreak = true;
										break;
									}
								} else
								{
									if (sx + sy * (tex.getWidth() >> 5) == tileArr[(x + y * l.width)])
									{
										l.tile[x][y] = new Tile(t.fName, sx - 1, sy);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public int calculateHeuristic(int x0, int y0, int x1, int y1)
	{
		int distance = Math.abs(x1 - x0) + Math.abs(y1 - y0);
		return distance;
	}

	public void update(long tick)
	{
		for (MapNpc mn : npcs)
		{
			mn.update(tick);
		}
	}

	@SuppressWarnings("unused")
	private void time()
	{

	}

	public void render(Screen screen)
	{		
		// Render the ground later and the mask layer
		for (int i = 0; i < layers.size(); i++)
		{
			this.layers.get(i).render(screen);
		}

		for (MapNpc mn : npcs)
		{
			mn.render(screen);
		}
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setWidth(int width)
	{
		if (width < Game.winWidth >> 5)
		{
			this.width = Game.winWidth >> 5 + 1;
		} else
		{
			this.width = width;
		}

	}

	public void setHeight(int height)
	{
		if (height < Game.winHeight >> 5)
		{
			this.height = Game.winHeight >> 5 + 1;
		} else
		{
			this.height = height;
		}
	}

	public Layer getLayer(int index)
	{
		return layers.get(index);
	}

	class Tileset
	{
		String fName;
		int firstGid;
		int lastGid;
	}
}
