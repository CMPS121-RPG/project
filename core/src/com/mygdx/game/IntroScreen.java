package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class IntroScreen implements Screen{

    final MyGdxGame game;
    public IntroScreen(final MyGdxGame game) {
        this.game = game;
    }

    SpriteBatch batch;
    Skin skin;
    Stage stage;
    Sprite sprite;
    Texture img;
    BitmapFont OurFont;
    //CharSequence theintro = "You and your friends are lost in the forest or something deal with ";


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
        OurFont = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        // add ourfont to the skin for our buttons.
        skin.add("default", OurFont);

        //textbuttonstyle wont overwrite the font
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");

        skin.add("default", textButtonStyle);

        // Create a button with the "default" TextButtonStyle.
        final TextButton PlayButton = new TextButton("Continue",textButtonStyle);
        PlayButton.setPosition(Gdx.graphics.getWidth() / 2 - 160, Gdx.graphics.getHeight() / 8);
        stage.addActor(PlayButton);

        PlayButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                PlayButton.setText("Starting new game");

                //TODO make this go to the map screen
                game.setScreen(game.mapscreen);

            }
        });

    }
    @Override
    public void render (float delta) {
        //this should make the background blueish
        Gdx.gl.glClearColor((float) .1, (float) .1, (float) .66, (float) .8);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //this draws the background image
        batch.begin();
        //sprite.draw(batch);
        OurFont.setColor(1, 1, 1, 1);
        //OurFont.draw(batch, theintro, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 50);
        OurFont.draw(batch, "You and your friends are lost in the forest or something.", 20, Gdx.graphics.getHeight() - 10);
        OurFont.draw(batch, "deal with it!", 20, Gdx.graphics.getHeight() - 50);
        OurFont.draw(batch, "Fight some monsters and get to the end.", 20, Gdx.graphics.getHeight() - 90);
        OurFont.draw(batch, "Or just die. Someone else can write the intro.", 20, Gdx.graphics.getHeight() - 130);
        batch.end();

        //draws the buttons
        stage.act(delta);
        stage.draw();
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
    }
}
