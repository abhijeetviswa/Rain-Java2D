package org.me.rain_2d;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import org.me.rain_2d.entity.mob.Player;
import org.me.rain_2d.graphics.textures.Screen;
import org.me.rain_2d.graphics.textures.Texture;
import org.me.rain_2d.graphics.textures.TextureCacher;
import org.me.rain_2d.input.Keyboard;
import org.me.rain_2d.input.Mouse;
import org.me.rain_2d.level.Level;
import org.me.rain_2d.level.MapNpc;
import org.me.rain_2d.level.RandomLevel;

public class Game extends Canvas implements Runnable
{
	private static final long serialVersionUID = 1L;
	public static int width = 800;
	public static int height = width / 4 * 3;
	public static int scale = 1;

	private static Game theGame;

	private Thread game;
	private boolean running = false;

	private Level level;

	private Player player;

	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	// Window
	private JFrame frame;
	private Screen screen;

	// Input
	private Keyboard key;
	private Mouse mouse;

	// Texture Cacher
	public TextureCacher textureCacher;

	public Game()
	{
		theGame = this;
		Dimension size = new Dimension(width * scale, height * scale);
		frame = new JFrame();
		screen = new Screen(width, height);
		key = new Keyboard();
		mouse = new Mouse();

		textureCacher = new TextureCacher().cache();

		level = new Level("example");
		player = new Player(key, textureCacher.getCharacterTexture("player"), level);

		setPreferredSize(size);
		addKeyListener(key);
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
	}

	public synchronized void start()
	{

		running = true;
		Thread.currentThread().setName("Rain - Main");
		game = new Thread(this, "Rain - Display");

		game.start();
	}

	@Override
	public void run()
	{
		long lastTime = System.nanoTime();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;

		int fps = 0;
		int updates = 0;
		long fpsTimer = 0;
		while (running) {
			long now = System.nanoTime();
			long nowMillis = System.currentTimeMillis();
			delta += (now - lastTime) / ns;
			lastTime = now;
			// key.left = true;
			while (delta >= 1) {
				update();
				updates++;
				delta--;
			}
			render();
			if (fpsTimer > nowMillis) {
				fps++;
			} else {
				fpsTimer = System.currentTimeMillis() + 1000;
				// Make sure we don't show insanely high numbers for the fps
				// when minimzed

				if (frame.getState() != Frame.ICONIFIED) {
					frame.setTitle("UPS: " + updates + " FPS: " + fps);
				} else {
					frame.setTitle("UPS: " + updates + " FPS: Not Calculating");
				}
				fps = 0;
				updates = 0;
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		stop();
	}

	public void update()
	{
		Texture.unLoadAll(textureCacher);

		key.update();
		level.update();
		player.update();
	}

	public void render()
	{
		// Only render if were are the active window
		if (frame.getState() == Frame.ICONIFIED) return;

		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		screen.clear();

		int xScroll = (((player.x << 5) + player.xOffset) - (screen.getWidth() / 2));
		int yScroll = (((player.y << 5) + player.yOffset) - (screen.getHeight() / 2));
		level.render(xScroll, yScroll, screen);
		player.render(screen);

		System.arraycopy(screen.pixels, 0, pixels, 0, screen.pixels.length);
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

		g.setColor(Color.white);
		g.drawString(String.valueOf(player.x) + ", " + String.valueOf(player.y), 100, 100);
		g.drawString(String.valueOf(mouse.getTileX()) + ", " + String.valueOf(mouse.getTileY()), 100, 115);
		g.dispose();
		bs.show();
	}

	public synchronized void stop()
	{
		try {
			game.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static Game getGame()
	{
		return theGame;
	}

	public static void main(String args[])
	{
		Game game = new Game();
		game.frame.setResizable(false);
		game.setName("Rain - 2D");
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);

		game.start();
	}

}
