package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
import com.badlogic.gdx.audio.Sound;
import java.util.Random;

//displays the map
//Level select
//Series of connected nodes (static graphic)
//pressing on locations provides some kind of feedback and goes to certain fight scene
public class MapScreen implements Screen{
    Skin skin;
    Stage stage;
    SpriteBatch batch;
    Texture img;
    Sprite sprite;

    Random random = new Random();

    Music maptheme = Gdx.audio.newMusic(Gdx.files.internal("FFoverworld.mp3"));

    final MyGdxGame game;
    public MapScreen(final MyGdxGame game) {
        this.game = game;
    }

    //String PlayerPos = "";

    float z1xpos = Gdx.graphics.getWidth()/2 + 100;  //+260 orginal value
    float z1ypos = Gdx.graphics.getHeight()/2 - 100;     //-95 original value

    float z2axpos = Gdx.graphics.getWidth() / 2 - 40;
    float z2aypos =  Gdx.graphics.getHeight() / 2 - 140;

    float z2bxpos = Gdx.graphics.getWidth()/2 + 300;
    float z2bypos = Gdx.graphics.getHeight()/2 - 300;

    float z3xpos = Gdx.graphics.getWidth()/2 + 50;
    float z3ypos = Gdx.graphics.getHeight()/2 - 400;

    float z4axpos = Gdx.graphics.getWidth()/2 - 200;
    float z4aypos = Gdx.graphics.getHeight()/2 - 650;

    float z4bxpos = Gdx.graphics.getWidth()/2 - 250;
    float z4bypos = Gdx.graphics.getHeight()/2 - 450;

    float z5xpos = Gdx.graphics.getWidth()/2 - 500;
    float z5ypos = Gdx.graphics.getHeight()/2 - 500;

    TextButton Zone1;
    TextButton Zone2a;
    TextButton Zone2b;
    TextButton Zone3;
    TextButton Zone4a;
    TextButton Zone4b;
    TextButton Zone5;

    Sound select = Gdx.audio.newSound(Gdx.files.internal("select.wav"));

    @Override
    public void show () {
        maptheme.play();
        maptheme.setVolume(game.state.volume);
        maptheme.setLooping(true);
        batch = new SpriteBatch();


        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        //Straight up stolen from https://www.pinterest.com/pin/428193877042769820/
        //change this if we ever put this game on the playstore
        img = new Texture("menu_background.png");
        img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        //change these numbers around so it looks good on the presentation phone
        TextureRegion region = new TextureRegion(img, 0, 0, 600, 400);
        sprite = new Sprite(region);
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sprite.setOrigin(0, 0);

        skin = new Skin();
        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(320, 75, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fillCircle(pixmap.getWidth() / 2, pixmap.getHeight() / 2, pixmap.getHeight() / 2 - 1);

        skin.add("white", new Texture(pixmap));
        pixmap.dispose();
        //create the custom font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/slkscre.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        BitmapFont OurFont = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        // add ourfont to the skin for our buttons.
        skin.add("default", OurFont);

        //textbuttonstyle wont overwrite the font
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.WHITE);
        textButtonStyle.disabled = skin.newDrawable("white", Color.RED);
        textButtonStyle.font = skin.getFont("default");

        skin.add("default", textButtonStyle);

        // Create a button with the "default" TextButtonStyle.
        Zone1 = new TextButton(game.state.Zone1Str,textButtonStyle);
        Zone1.setDisabled(game.state.Zone1_disabled);

        Zone2a = new TextButton(game.state.Zone2aStr, textButtonStyle);
        Zone2a.setDisabled(game.state.Zone2a_disabled);

        Zone2b = new TextButton(game.state.Zone2bStr,textButtonStyle);
        Zone2b.setDisabled(game.state.Zone2b_disabled);

        Zone3 = new TextButton(game.state.Zone3Str, textButtonStyle);
        Zone3.setDisabled(game.state.Zone3_disabled);

        Zone4a = new TextButton(game.state.Zone4aStr, textButtonStyle);
        Zone4a.setDisabled(game.state.Zone4a_disabled);

        Zone4b = new TextButton(game.state.Zone4bStr, textButtonStyle);
        Zone4b.setDisabled(game.state.Zone4b_disabled);

        Zone5 = new TextButton(game.state.Zone5Str, textButtonStyle);
        Zone5.setDisabled(game.state.Zone5_disabled);

        //Zone3b = new TextButton()

    //    final TextButton Returnmain = new TextButton("RETURN TO MAIN", textButtonStyle);

        Zone1.setPosition(z1xpos,z1ypos);
        Zone2a.setPosition(z2axpos,z2aypos);
        Zone2b.setPosition(z2bxpos,z2bypos);
        Zone3.setPosition (z3xpos, z3ypos);
        Zone4a.setPosition(z4axpos, z4aypos);
        Zone4b.setPosition(z4bxpos, z4bypos);
        Zone5.setPosition(z5xpos, z5ypos);

    //    Returnmain.setPosition(100, 100);

    //    stage.addActor(Returnmain);

        stage.addActor(Zone1);
        stage.addActor(Zone2a);
        stage.addActor(Zone2b);
        stage.addActor(Zone3);
        stage.addActor(Zone4a);
        stage.addActor(Zone4b);
        stage.addActor(Zone5);

        Zone1.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                //Zone1.setText("Here");
                //game.state.Zone1Str = "Here";
                select.play();
                Zone1.setDisabled(true);
                game.state.Zone1_disabled = true;

                Zone2a.setDisabled(false);
                game.state.Zone2a_disabled = false;
                Zone2b.setDisabled(false);
                game.state.Zone2b_disabled = false;

                game.state.difficulty = 1;

                game.setScreen(game.gamescreen);
                maptheme.stop();

            }
        });
        Zone2a.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {

                //get rid of old screen
                game.gamescreen.dispose();

                select.play();
                Zone2a.setDisabled(true);
                Zone2b.setDisabled(true);
                game.state.Zone2a_disabled = true;
                game.state.Zone2b_disabled = true;

                Zone3.setDisabled(false);
                game.state.Zone3_disabled = false;

                //game.state.difficulty = random.nextInt(2) + 1;//returns an int from 1 to 2
                game.state.difficulty = 2;
                //make new screen
                game.gamescreen.show();
                game.setScreen(game.gamescreen);
                maptheme.stop();
            }

        });
        Zone2b.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                select.play();
                Zone2b.setDisabled(true);
                Zone2a.setDisabled(true);

                game.state.Zone2b_disabled = true;
                game.state.Zone2a_disabled = true;

                Zone3.setDisabled(false);
                game.state.Zone3_disabled = false;

                game.state.difficulty = random.nextInt(2) + 1;//returns int from 1 to 2
                game.gamescreen.dispose();
                game.gamescreen.show();
                game.setScreen(game.gamescreen);
                maptheme.stop();
            }
        });

        Zone3.addListener (new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                select.play();
                Zone3.setDisabled(true);
                game.state.Zone3_disabled = true;

                Zone4a.setDisabled(false);
                Zone4b.setDisabled(false);

                game.state.Zone4a_disabled = false;
                game.state.Zone4b_disabled = false;

                game.state.difficulty = 2;
                game.gamescreen.dispose();
                game.gamescreen.show();
                game.setScreen(game.gamescreen);
                maptheme.stop();
            }
        });

        Zone4a.addListener (new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                select.play();

                Zone4a.setDisabled(true);
                game.state.Zone4a_disabled = true;
                Zone4b.setDisabled(true);
                game.state.Zone4b_disabled = true;

                Zone5.setDisabled(false);
                game.state.Zone5_disabled = false;

                game.state.difficulty = random.nextInt(2) +2; //returns an int from 2-3
                game.gamescreen.dispose();
                game.gamescreen.show();
                game.setScreen(game.gamescreen);
                maptheme.stop();
            }
        });

        Zone4b.addListener (new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                select.play();

                Zone4a.setDisabled(true);
                game.state.Zone4a_disabled = true;
                Zone4b.setDisabled(true);
                game.state.Zone4b_disabled = true;

                Zone5.setDisabled(false);
                game.state.Zone5_disabled = false;

                game.state.difficulty = random.nextInt(2) + 2; //returns an int from 2-3
                game.gamescreen.dispose();
                game.gamescreen.show();
                game.setScreen(game.gamescreen);
                maptheme.stop();
            }
        });

        Zone5.addListener (new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                select.play();

                Zone5.setDisabled(true);
                game.state.Zone5_disabled = true;

                Zone1.setDisabled(false);
                game.state.Zone1_disabled = false;

                game.state.difficulty = 3;
                game.gamescreen.dispose();
                game.gamescreen.show();
                game.setScreen(game.gamescreen);
                maptheme.stop();
            }
        });


        /*Returnmain.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor){
                game.setScreen(game.startmenuscreen);
            }
        });*/
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
        select.dispose();
    }

}
