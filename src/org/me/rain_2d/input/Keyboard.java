package org.me.rain_2d.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener
{

	private boolean[] keys = new boolean[120];
	public boolean up, down, left, right;
	public boolean ctrl, shift;

	public void update()
	{
		up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
		down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
		shift = keys[KeyEvent.VK_SHIFT];
	}

	@Override
	public void keyTyped(KeyEvent e)
	{

	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() > keys.length) return;
		keys[e.getKeyCode()] = true;
		ctrl = e.isControlDown();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() > keys.length) return;
		keys[e.getKeyCode()] = false;
		ctrl = e.isControlDown();
	}

}
