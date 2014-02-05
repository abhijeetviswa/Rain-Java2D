package org.me.rain_2d.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.event.MouseInputListener;

import org.me.rain_2d.entity.mob.Player;

public class Mouse implements MouseMotionListener, MouseInputListener
{
	int x = 0, y = 0;
	int tileX = 0, tileY = 0;

	public int getMouseX()
	{
		return x;
	}

	public int getMouseY()
	{
		return y;
	}

	public int getTileX()
	{
		return (x >> 5);
	}

	public int getTileY()
	{
		return (y >> 5);
	}

	public void mouseMoved(MouseEvent e)
	{
		x = e.getX();
		y = e.getY();
	}

	public void mouseClicked(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{

	}

	public void mouseReleased(MouseEvent e)
	{

	}

	public void mouseEntered(MouseEvent e)
	{

	}

	public void mouseExited(MouseEvent e)
	{

	}

	public void mouseDragged(MouseEvent e)
	{
	}

}
