package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;

//ApplicationAdapter
//This class gets called on launch
//it loads all the things and then sets the screen to the startmenu
public class MyGdxGame extends Game {

	public StartMenuScreen startmenuscreen;
	public SwipeGame swipegame;

	@Override
	public void create () {

		swipegame = new SwipeGame(this);
		startmenuscreen = new StartMenuScreen(this);
		setScreen(startmenuscreen);

	}
	@Override
	public void resize (int width, int height) {
		super.resize(width, height);
	}
	@Override
	public void render () {
		super.render();
	}
	@Override
	public void pause() {
		super.pause();
	}
	@Override
	public void resume() {
		super.resume();
	}
	@Override
	public void dispose () {

	}

}
