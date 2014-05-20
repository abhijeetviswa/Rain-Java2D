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
		if (width < Game.width >> 5) width = Game.width >> 5;
		if (height < Game.height >> 5) height = Game.height >> 5;
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
			NodeList nList = doc.getElementsByTagName("map");
			for (int temp = 0; temp < nList.getLength(); temp++)
			{

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE)
				{

					Element eElement = (Element) nNode;

					setWidth(Integer.parseInt(eElement.getAttribute("width")));
					setHeight(Integer.parseInt(eElement.getAttribute("height")));

				}

				// Get Tilesets used in Map
				NodeList tileset = doc.getElementsByTagName("tileset");
				for (temp = 0; temp < tileset.getLength(); temp++)
				{
					nNode = tileset.item(temp);

					if (nNode.getNodeType() == Node.ELEMENT_NODE)
					{
						Element el = (Element) nNode;

						Tileset ts = new Tileset();

						ts.fName = el.getAttribute("name");
						ts.firstGid = Integer.parseInt(el.getAttribute("firstgid"));
						if (temp != tileset.getLength() - 1)
						{
							ts.lastGid = Integer.parseInt(((Element) tileset.item(temp + 1)).getAttribute("firstgid")) - 1;
						} else
						{
							int pix = Game.getGame().textureCacher.getTilesetTexture(ts.fName).getWidth() * Game.getGame().textureCacher.getTilesetTexture(ts.fName).getWidth();
							int tpix = 32 * 32;
							int val = pix / tpix;
							ts.lastGid = val;
						}

						tilesets.add(ts);
					}
				}

				// Get Layer Data
				NodeList layers = doc.getElementsByTagName("layer");
				for (temp = 0; temp <= layers.getLength() - 1; temp++)
				{
					nNode = layers.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE)
					{
						int width, height;
						Element el = (Element) nNode;
						width = Integer.parseInt(el.getAttribute("width"));
						height = Integer.parseInt(el.getAttribute("height"));
						Layer l = new Layer(getWidth(), getHeight());

						NodeList nodeListData = nNode.getChildNodes();
						Node data = null;
						for (int temp2 = 0; temp2 < nodeListData.getLength() - 1; temp2++)
						{
							data = nodeListData.item(temp2);
							if (data.getNodeType() == Node.ELEMENT_NODE) break;
						}
						NodeList nodeListTiles = data.getChildNodes();
						int tCounter = 0;
						int tileArr[] = new int[width * height];
						for (int temp2 = 0; temp2 < nodeListTiles.getLength(); temp2++)
						{
							Node nTile = nodeListTiles.item(temp2);
							if (nTile.getNodeType() == Node.ELEMENT_NODE)
							{
								Element eTile = (Element) nTile;
								if (Integer.parseInt(eTile.getAttribute("gid")) > 0)
								{
									tileArr[tCounter] = Integer.parseInt(eTile.getAttribute("gid"));
								}
								tCounter++;
							}
						}
						parseTilesetData(tileArr, l, tilesets);
						this.layers.add(l);
					}
				}

				// Parse Objects
				NodeList objectgroup = doc.getElementsByTagName("objectgroup");
				for (temp = 0; temp < objectgroup.getLength(); temp++)
				{
					nNode = objectgroup.item(temp);
					String groupName = null;
					if (nNode.getNodeType() == Node.ELEMENT_NODE)
					{
						Element el = (Element) nNode;
						groupName = el.getAttribute("name");
					}
					if (groupName == null) continue;

					// Get the object data
					NodeList nodeListObject = nNode.getChildNodes();
					Node object = null;
					for (int temp2 = 0; temp2 < nodeListObject.getLength(); temp2++)
					{
						object = nodeListObject.item(temp2);
						if (object.getNodeType() == Node.ELEMENT_NODE)
						{
							float x, y;
							int width, height;
							switch (groupName.toLowerCase()) {
							case "collision":
								Element el = (Element) object;
								x = Float.parseFloat(el.getAttribute("x"));
								y = Float.parseFloat(el.getAttribute("y"));
								width = Integer.parseInt(el.getAttribute("width"));
								height = Integer.parseInt(el.getAttribute("height"));
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
		for (int x = 0; x <= (l.width - 1); ++x)
		{
			for (int y = 0; y <= (l.height - 1); ++y)
			{
				for (Tileset t : sets)
				{
					if (tileArr[x + y * l.width] >= t.firstGid && tileArr[x + y * l.width] <= t.lastGid)
					{
						Texture tex = Game.getGame().textureCacher.getTilesetTexture(t.fName);
						for (int sx = 0; sx < (tex.getWidth() >> 5); sx++)
						{
							for (int sy = 0; sy < (tex.getHeight() >> 5); sy++)
							{
								if (t.firstGid != 1)
								{
									if (sx + sy * (tex.getWidth() >> 5) == (tileArr[(x + y * l.width)]) - t.firstGid)
									{
										l.tile[x][y] = new Tile(t.fName, sx, sy);
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
		if (width < Game.width >> 5){
			this.width = Game.width >> 5 + 1;
		}else
		{
			this.width = width;
		}
						
	}

	public void setHeight(int height)
	{
		if (height < Game.height >> 5){
			this.height = Game.height >> 5 + 1;
		}else
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
