package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


//the class for the Startmenu
public class StartMenuScreen implements Screen {
    Skin skin;
    Stage stage;
    SpriteBatch batch;
    Texture img;
    Sprite sprite;

    final MyGdxGame game;
    public StartMenuScreen(final MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void render (float delta) {
        //this should make the background blueish
        Gdx.gl.glClearColor((float) .1, (float) .1, (float) .66, (float) .8);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //this draws the background image
        batch.begin();
        sprite.draw(batch);
        batch.end();

        //draws the buttons
        stage.act(delta);
        stage.draw();

    }
    @Override
    public void show () {
        batch = new SpriteBatch();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        //Straight up stolen from https://www.pinterest.com/pin/428193877042769820/
        //change this if we ever put this game on the playstore
        img = new Texture("menubackground1.png");
        img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        //change these numbers around so it looks good on the presentation phone
        TextureRegion region = new TextureRegion(img, 0, 0, 600, 400);
        sprite = new Sprite(region);
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sprite.setOrigin(0, 0);

        skin = new Skin();
        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(320, 75, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        //create the custom font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/slkscre.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        BitmapFont OurFont = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        // add ourfont to the skin for our buttons.
        skin.add("default", OurFont);

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
        final TextButton SettingsButton = new TextButton("Mute",textButtonStyle);
        final TextButton QuitButton = new TextButton("Quit",textButtonStyle);
        PlayButton.setPosition(Gdx.graphics.getWidth()/2 - 160, Gdx.graphics.getHeight()/2);
        ContinueButton.setPosition(Gdx.graphics.getWidth()/2 - 160, Gdx.graphics.getHeight()/2 - 85);
        SettingsButton.setPosition(Gdx.graphics.getWidth()/2 - 160, Gdx.graphics.getHeight()/2 - 165);
        QuitButton.setPosition(Gdx.graphics.getWidth()/2 - 160, Gdx.graphics.getHeight()/2 - 245);
        stage.addActor(PlayButton);
        stage.addActor(ContinueButton);
        stage.addActor(SettingsButton);
        stage.addActor(QuitButton);

        PlayButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                PlayButton.setText("Starting new game");

                //TODO make this go to the introscreen or map screen
                //this goes to the swipe thing
                game.setScreen(game.gamescreen);

            }
        });
        ContinueButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                //System.out.println("Clicked! Is checked: " + button.isChecked());
                ContinueButton.setText("Continuing");

                //TODO i dont think we need this button because we dont save anything
                //game.setScreen(game.swipegame);
            }
        });
        SettingsButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                //System.out.println("Clicked! Is checked: " + button.isChecked());
                SettingsButton.setText("Muting");

                //TODO make this mute and unmute the non existant sound
                //mute and unmute;
            }

        });
        QuitButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                QuitButton.setText("Quitting");
                //TODO make this quit the correct way
                System.exit(0);
            }
        });
    }
    @Override
    public void resize (int width, int height) {
    }
    @Override
    public void hide() {
    }
    @Override
    public void pause() {
    }
    @Override
    public void resume() {
    }
    @Override
    public void dispose () {
        stage.dispose();
        skin.dispose();
    }
}
