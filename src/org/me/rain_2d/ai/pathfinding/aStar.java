package org.me.rain_2d.ai.pathfinding;

import java.awt.List;
import java.util.ArrayList;

public class aStar
{
	public final int MOVE_COST = 5;

	// private ArrayList<ArrayList<Integer>> open = new
	// ArrayList<ArrayList<Integer>>()
	// private ArrayList<ArrayList<Integer>> closed = new
	// ArrayList<ArrayList<Integer>>();

	private boolean open[];
	private boolean closed[];

	public int calculateHeuristic(int x0, int y0, int x1, int y1)
	{
		int distance = Math.abs(x1 - x0) + Math.abs(y1 - y0);
		return distance;
	}

	public void calculatePath(int x0, int y0, int x1, int y1, int mapMaxX, int mapMaxY)
	{
		open = new boolean[mapMaxX * mapMaxY];
		closed = new boolean[mapMaxX * mapMaxY];
		int x = x0, y = y0;
		int tempX, tempY;
		while (x != x1 && y != y1)
		{
			// Get all the tiles around the current tile
			closed[x * + y * mapMaxX] = true;
			
		}
	}

	public void Convert2DTo1D(int x, int y)
	{

	}
}
