package org.me.rain_2d.level;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.me.rain_2d.Game;
import org.me.rain_2d.graphics.textures.Screen;
import org.me.rain_2d.graphics.textures.Sprite;
import org.me.rain_2d.graphics.textures.Texture;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Level
{

	protected int width, height;
	protected ArrayList<Layer> layers = new ArrayList<Layer>();
	protected ArrayList<MapNpc> npcs = new ArrayList<MapNpc>();

	public Level(int width, int height)
	{
		this.width = width;
		this.height = height;
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
		try {
			File level = new File(Level.class.getResource("/levels/" + name + ".tmx").toURI());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(level);

			doc.getDocumentElement().normalize();

			// Get Map Main Data
			NodeList nList = doc.getElementsByTagName("map");
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					width = Integer.parseInt(eElement.getAttribute("width"));
					height = Integer.parseInt(eElement.getAttribute("height"));

				}

				// Get Tilesets used in Map
				NodeList tileset = doc.getElementsByTagName("tileset");
				for (temp = 0; temp < tileset.getLength(); temp++) {
					nNode = tileset.item(temp);

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element el = (Element) nNode;

						Tileset ts = new Tileset();

						ts.fName = el.getAttribute("name");
						ts.firstGid = Integer.parseInt(el.getAttribute("firstgid"));
						if (temp != tileset.getLength() - 1) {
							ts.lastGid = Integer.parseInt(((Element) tileset.item(temp + 1)).getAttribute("firstgid")) - 1;
						} else {
							int pix = Game.getGame().textureCacher.getTilesetTexture(ts.fName).getWidth()
									* Game.getGame().textureCacher.getTilesetTexture(ts.fName).getWidth();
							int tpix = 32 * 32;
							int val = pix / tpix;
							ts.lastGid = val;
						}

						tilesets.add(ts);
					}
				}

				// Get Layer Data
				NodeList layer = doc.getElementsByTagName("layer");
				for (temp = 0; temp < layer.getLength() - 1; temp++) {
					nNode = layer.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element el = (Element) nNode;
						Layer l = new Layer(Integer.parseInt(el.getAttribute("width")), Integer.parseInt(el.getAttribute("height")));

						NodeList nodeListData = nNode.getChildNodes();
						Node data = null;
						for (int temp2 = 0; temp2 < nodeListData.getLength() - 1; temp2++) {
							data = nodeListData.item(temp2);
							if (data.getNodeType() == Node.ELEMENT_NODE) break;
						}
						NodeList nodeListTiles = data.getChildNodes();
						int tCounter = 0;
						int tileArr[] = new int[l.width * l.height];
						for (int temp2 = 0; temp2 < nodeListTiles.getLength(); temp2++) {
							Node nTile = nodeListTiles.item(temp2);
							if (nTile.getNodeType() == Node.ELEMENT_NODE) {
								Element eTile = (Element) nTile;
								if (Integer.parseInt(eTile.getAttribute("gid")) > 0) {
									tileArr[tCounter] = Integer.parseInt(eTile.getAttribute("gid"));
								}
								tCounter++;
							}
						}
						parseTilesetData(tileArr, l, tilesets);
						layers.add(l);
					}
				}
			}
		} catch (Exception e) {
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
		for (int x = 0; x <= (l.width - 1); ++x) {
			for (int y = 0; y <= (l.height - 1); ++y) {
				for (Tileset t : sets) {
					if (tileArr[x + y * l.width] >= t.firstGid && tileArr[x + y * l.width] <= t.lastGid) {
						Texture tex = Game.getGame().textureCacher.getTilesetTexture(t.fName);
						for (int sx = 1; sx < (tex.getWidth() >> 5); sx++) {
							for (int sy = 0; sy < (tex.getHeight() >> 5); sy++) {
								if (sx + sy * (tex.getWidth() >> 5) == tileArr[x + y * l.width]) {
									l.tile[x][y] = new Tile(t.fName, sx, sy);
								}
							}
						}
					}
				}
			}
		}
	}

	public void update()
	{
		for (MapNpc mn : npcs) {
			mn.update();
		}
	}

	@SuppressWarnings("unused")
	private void time()
	{

	}

	public void render(int xScroll, int yScroll, Screen screen)
	{
		// All coordinates are using tile precision.
		screen.setOffset(xScroll, yScroll);
		int l = 0;
		for (Layer layer : layers) {
			layer.render(screen,l);
			l++;
		}

		for (MapNpc mn : npcs) {
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

	class Tileset
	{
		String fName;
		int firstGid;
		int lastGid;
	}
}
