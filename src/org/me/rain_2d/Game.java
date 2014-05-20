package org.me.rain_2d;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;

import javax.swing.JFrame;

import org.me.rain_2d.entity.mob.Player;
import org.me.rain_2d.graphics.Screen;
import org.me.rain_2d.graphics.textures.Texture;
import org.me.rain_2d.graphics.textures.TextureCacher;
import org.me.rain_2d.input.Keyboard;
import org.me.rain_2d.input.Mouse;
import org.me.rain_2d.level.Level;

public class Game extends Canvas implements Runnable
{
	private static final long serialVersionUID = 1L;
	public static int width = 800;
	public static int height = 600;
	public static int scale = 1;

	private static Game theGame;

	private Thread game;
	private boolean running = false;

	private Level level;

	private Player player;

	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	// Window
	private JFrame frame;
	private Screen screen;

	// Input
	private Keyboard key;
	private Mouse mouse;

	// Texture Cacher
	public TextureCacher textureCacher;

	private static boolean runningOnJar;

	public Game()
	{
		theGame = this;
		Dimension size = new Dimension(width * scale, height * scale);
		frame = new JFrame();
		screen = new Screen(width, height);
		key = new Keyboard();
		mouse = new Mouse(screen);

		textureCacher = new TextureCacher().cache();

		level = new Level("example");
		// level.addMapNpc(new MapNpc(0,0,
		// textureCacher.getCharacterTexture("player"), level));
		player = new Player(key, mouse, textureCacher.getCharacterTexture("player"), level);

		setPreferredSize(size);
		addKeyListener(key);
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		
		System.out.println("Welcome to Abhi2011's Game");
	}

	public synchronized void start()
	{

		running = true;
		game = new Thread(this, "Rain - Main");

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
		while (running)
		{
			long now = System.nanoTime();
			long nowMillis = System.currentTimeMillis();
			delta += (now - lastTime) / ns;
			lastTime = now;
			// key.left = true;
			while (delta >= 1)
			{
				update(System.currentTimeMillis());
				updates++;
				delta--;
			}
			render();
			if (fpsTimer > nowMillis)
			{
				fps++;
			} else
			{
				fpsTimer = System.currentTimeMillis() + 1000;
				// Make sure we don't show insanely high numbers for the fps
				// when minimzed

				if (frame.getState() != Frame.ICONIFIED)
				{
					frame.setTitle("UPS: " + updates + " FPS: " + fps);
				} else
				{
					frame.setTitle("UPS: " + updates + " FPS: Not Calculating");
				}
				fps = 0;
				updates = 0;
			}
			try
			{
				Thread.sleep(1);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		stop();
	}

	public void update(long tick)
	{
		Texture.unLoadAll(textureCacher);

		key.update();
		level.update(tick);
		mouse.update(player);
		player.update();
	}

	public void render()
	{
		// Only render if were are the active window
		if (frame.getState() == Frame.ICONIFIED) return;

		BufferStrategy bs = getBufferStrategy();
		if (bs == null)
		{
			createBufferStrategy(3);
			return;
		}

		screen.clear();
		clear();
		screen.updateCamera(player.x, player.y, 0, 0, level.getWidth(), level.getHeight());
		level.render(screen, 0, 1);
		player.render(screen);
		level.render(screen, 2, 2);
		System.arraycopy(screen.pixels, 0, pixels, 0, screen.pixels.length);
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		// Clear Screen
		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);
		// End Clear Screen

		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.setColor(Color.yellow);
		// Draw the Cur X, cur Y
		g.drawString("Cur x: " + mouse.getTileX() + ", y: " + mouse.getTileY(), 100, 100);
		g.drawString("Loc x: " + player.x + " y: " + player.x, 100, 120);
		g.dispose();
		bs.show();
	}

	private void clear()
	{
		// Graphics2D g = image.createGraphics();
		// g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR,
		// 0.0f));
		// g.fillRect(0, 0, image.getWidth(), image.getHeight());
		// g.dispose();
	}

	public synchronized void stop()
	{
		try
		{
			game.join();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public static Game getGame()
	{
		return theGame;
	}

	public static boolean runningOnJar()
	{
		return runningOnJar;
	}

	public static void main(String args[])
	{
		// Before anything find out how we are running. (Jar or directly running
		// the .class files)
		final File jarFile = new File(Game.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		if (jarFile.getPath().contains("jar")) runningOnJar = true;

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
