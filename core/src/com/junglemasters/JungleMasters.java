package com.junglemasters;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.junglemasters.Screens.Hud;
import com.junglemasters.Screens.PlayScreen;


public class JungleMasters extends Game {

    public SpriteBatch batch;
	public static final int CAMERA_WIDTH = 1794;
	public static final int CAMERA_HEIGHT = 1080;
	public static final float PPM = 100;

	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short FRAN_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short SECONDS_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short HIDDEN_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_HEAD_BIT = 128;
	public static final short ENEMY_FRONT_BIT = 256;
	public static final short FRAN_HEAD_BIT = 512;
	public static final short FRAN_BODY_SENSOR_BIT = 1024;
    public static final short ENEMY_LIMIT = 2048;
	public static final short WATER_BIT = 4096;
	public static final short ISLAND_BIT = 8192;
	public static final short GROUND_ESCAPE = 16384;
    public static final short DEAD_BIT = (short) 32768;
	public static final short FIREBALL_BIT = (short) 32768;
	public boolean HUD;

	@Override
	public void create () {
		batch = new SpriteBatch();

		setScreen(new PlayScreen(this));

	}

	@Override
	public void render () {
		super.render();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		super.dispose();
	}
}
