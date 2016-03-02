package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MyGdxGame extends ApplicationAdapter {
	Skin skin;
	Stage stage;
	SpriteBatch batch;
	//Texture img;

	/* may or may not be necessary later
	TODO make the menu better
	Game g;
	public StartMenu(Game g){
		create();
		this.g=g;
	}
	public StartMenu(){
		create();
	}
	*/
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		//img = new Texture("badlogic.jpg");

		skin = new Skin();
		// Generate a 1x1 white texture and store it in the skin named "white".
		Pixmap pixmap = new Pixmap(400, 100, Format.RGBA8888);
		pixmap.setColor(Color.GREEN);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));

		// Store the default libgdx font under the name "default".
		BitmapFont bfont = new BitmapFont();
		//bfont.scale(1);
		skin.add("default", bfont);

		//textbuttonstyle wont overwrite the font
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);

		textButtonStyle.font = skin.getFont("default");

		skin.add("default", textButtonStyle);

		// Create a button with the "default" TextButtonStyle.
		final TextButton PlayButton = new TextButton("PLAY",textButtonStyle);
		final TextButton ContinueButton = new TextButton("Continue",textButtonStyle);
		final TextButton SettingsButton = new TextButton("Settings",textButtonStyle);
		final TextButton QuitButton = new TextButton("Quit",textButtonStyle);
		PlayButton.setPosition(Gdx.graphics.getWidth()/2 - 200, Gdx.graphics.getHeight()/2 + 100);
		ContinueButton.setPosition(Gdx.graphics.getWidth()/2 - 200, Gdx.graphics.getHeight()/2);
		SettingsButton.setPosition(Gdx.graphics.getWidth()/2 - 200, Gdx.graphics.getHeight()/2 - 100);
		QuitButton.setPosition(Gdx.graphics.getWidth()/2 - 200, Gdx.graphics.getHeight()/2 - 200);
		stage.addActor(PlayButton);
		stage.addActor(ContinueButton);
		stage.addActor(SettingsButton);
		stage.addActor(QuitButton);

		PlayButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				PlayButton.setText("Starting new game");

				//TODO change the buttons to go to the correct places
				//g.setScreen( new GameScreen());
			}
		});
		ContinueButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				//System.out.println("Clicked! Is checked: " + button.isChecked());
				ContinueButton.setText("Continuing");

				//TODO change the buttons to go to the correct places
				//g.setScreen( new GameScreen());
			}
		});
		SettingsButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				//System.out.println("Clicked! Is checked: " + button.isChecked());
				SettingsButton.setText("Going to Settings");

				//TODO change the buttons to go to the correct places
				//g.setScreen( new GameScreen());
			}
		});
		QuitButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				//System.out.println("Clicked! Is checked: " + button.isChecked());
				QuitButton.setText("Quitting");

				//TODO this is the the wrong way to exit but whatever
				//Activity.finish();
				System.exit(0);
			}
		});

	}
	@Override
	public void render () {
		//this should make the background blueish
		Gdx.gl.glClearColor((float) .1, (float) .1, (float) .66, (float) .8);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();

		batch.begin();
		//batch.draw(img, 0, 0);
		batch.end();
	}
	@Override
	public void resize (int width, int height) {
	}
	@Override
	public void dispose () {
		stage.dispose();
		skin.dispose();
	}

}
