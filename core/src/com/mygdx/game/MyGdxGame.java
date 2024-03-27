package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.ChooseLvl;
import com.mygdx.game.Screens.ChooseSkinScreen;
import com.mygdx.game.Screens.DiedScreen;
import com.mygdx.game.Screens.PlayScreen;

public class MyGdxGame extends Game {
	public static final int V_WIDTH = 800; //600
	public static final int V_HEIGHT = 400; //1280х960 //500
	public static final float PPM = 100;

	public static final short DEFAULT_BIT = 1;
	public static final short HERO_BIT = 2;
	public static final short WINOBJECT_BIT = 4;
	public static final short ONTOUCHED_BIT = 8;


	public SpriteBatch batch;

	public boolean heroIsDead;

	public boolean isDead()
	{
		return heroIsDead;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));

		//setScreen(new ChooseSkinScreen(this));
		//setScreen(new ChooseLvl(this));
		//setScreen(new DiedScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
