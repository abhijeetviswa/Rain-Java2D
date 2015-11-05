package org.me.rain_2d.level;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.me.rain_2d.Game;
import org.me.rain_2d.graphics.Screen;
import org.me.rain_2d.graphics.textures.Texture;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

	private void loadLevel(String name)
	{
		ArrayList<Tileset> tilesets = new ArrayList<Tileset>();
		try
		{

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(getClass().getResourceAsStream("/levels/" + name + ".tmx"));

			doc.getDocumentElement().normalize();

			// Get Map Main Data
			Element mapEl = (Element) doc.getElementsByTagName("map").item(0);

			setWidth(Integer.parseInt(mapEl.getAttribute("width")));
			setHeight(Integer.parseInt(mapEl.getAttribute("height")));

			// Get Tilesets used in Map
			NodeList tilesetListNode = doc.getElementsByTagName("tileset");
			for (int temp = 0; temp < tilesetListNode.getLength(); temp++)
			{
				Element tilesetEl = (Element) tilesetListNode.item(temp);
				Tileset ts = new Tileset();

				ts.fName = tilesetEl.getAttribute("name");
				ts.firstGid = Integer.parseInt(tilesetEl.getAttribute("firstgid"));
				if (temp != tilesetListNode.getLength() - 1)
				{
					ts.lastGid = Integer.parseInt(((Element) tilesetListNode.item(temp + 1)).getAttribute("firstgid"))
							- 1;
				} else
				{
					int pix = Game.getGame().textureCacher.getTilesetTexture(ts.fName).getWidth()
							* Game.getGame().textureCacher.getTilesetTexture(ts.fName).getWidth();
					int tpix = 32 * 32;
					int val = pix / tpix;
					ts.lastGid = val;
				}

				tilesets.add(ts);
			}

			// Get Layer Data
			NodeList layers = doc.getElementsByTagName("layer");
			for (int temp = 0; temp < layers.getLength(); temp++)
			{
				Element layerEl = (Element) layers.item(temp);
				int width = Integer.parseInt(layerEl.getAttribute("width"));
				int height = Integer.parseInt(layerEl.getAttribute("height"));
				Layer l = new Layer(this.width, this.height);
				Node dataNode = layerEl.getElementsByTagName("data").item(0);
				NodeList tiles = ((Element) dataNode).getElementsByTagName("tile");

				int tCounter = 0;
				int tileArr[] = new int[width * height];
				for (int temp2 = 0; temp2 < tiles.getLength(); temp2++)
				{
					Element tileEl = (Element) tiles.item(temp2);
					tileArr[tCounter] = Integer.parseInt(tileEl.getAttribute("gid"));
					tCounter++;
				}
				parseTilesetData(tileArr, l, tilesets);
				this.layers.add(l);
			}

			// Parse Objects
			NodeList objectGroups = doc.getElementsByTagName("objectgroup");
			for (int temp = 0; temp < objectGroups.getLength(); temp++)
			{
				Element objectGroupEl = (Element) objectGroups.item(temp);
				String groupName = null;
				groupName = objectGroupEl.getAttribute("name");
				if (groupName == null) continue;

				// Get the object data
				NodeList objects = objectGroupEl.getElementsByTagName("object");
				for (int temp2 = 0; temp2 < objects.getLength(); temp2++)
				{
					Element objectEl = (Element) objects.item(temp2);

					float x, y;
					int width, height;
					switch (groupName.toLowerCase())
					{
					case "collision":
						x = Float.parseFloat(objectEl.getAttribute("x"));
						y = Float.parseFloat(objectEl.getAttribute("y"));
						width = Integer.parseInt(objectEl.getAttribute("width"));
						height = Integer.parseInt(objectEl.getAttribute("height"));
						// Align everything properly and send the data
						// x1 = (int) (Math.ceil())) - 1);
						float xF = x + width;
						float yF = y + height;
						int x1 = (int) Math.rint(x / 32.0);
						int y1 = (int) Math.rint(y / 32.0);
						int x2 = (int) (Math.rint(xF / 32.0)) - 1;
						int y2 = (int) (Math.rint(yF / 32.0)) - 1;
						for (int ycounter = y1; ycounter <= y2; ycounter++)
						{
							for (int xcounter = x1; xcounter <= x2; xcounter++)
							{
								Layer l = this.layers.get(0);
								l.getTile(xcounter, ycounter).setCollidable(true);
							}
						}
					}
				}
			}

		} catch (Exception e)
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

	public void render(Screen screen, int renderLayerBegin, int renderLayerEnd)
	{
		// All coordinates are using tile precision.
		// screen.setOffset(xScroll, yScroll);

		// Render the ground later and the mask layer
		for (int i = renderLayerBegin; i <= renderLayerEnd; i++)
		{
			if (i == 2)
			{
				// System.out.println("test");
			}
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
