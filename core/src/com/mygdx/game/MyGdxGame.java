package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.audio.Music;

//ApplicationAdapter
//This class gets called on launch
//it loads all the things and then sets the screen to the startmenu
public class MyGdxGame extends Game {

	public StartMenuScreen startmenuscreen;
	public IntroScreen introscreen;
	public MapScreen mapscreen;
	public SwipeGame swipegame;
	public GameScreen gamescreen;
	public PauseScreen pausescreen;
	public MapState state;
	//public Music battlesong = newMusic("FFVbattle");


	@Override
	public void create () {

		introscreen = new IntroScreen(this);
		mapscreen = new MapScreen(this);
		pausescreen = new PauseScreen(this);
		swipegame = new SwipeGame(this);
		gamescreen = new GameScreen(this);
		startmenuscreen = new StartMenuScreen(this);
		setScreen(startmenuscreen);

		state = new MapState();

		//Music battletheme = newMusic("FFVbattle.mp3");
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
