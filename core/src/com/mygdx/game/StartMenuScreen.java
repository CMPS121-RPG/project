package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
    Texture img, titleimg;
    Sprite sprite;
    Music openingtheme = Gdx.audio.newMusic(Gdx.files.internal("FFVIIprelude.mp3"));

    Sound select = Gdx.audio.newSound(Gdx.files.internal("select.wav"));

    final MyGdxGame game;
    public StartMenuScreen(final MyGdxGame game) {
        this.game = game;
    }


    @Override
    public void render (float delta) {
        //this should make the background blueish
        Gdx.gl.glClearColor((float) .1, (float) .1, (float) .66, (float) .8);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //this draws the background image and the title
        batch.begin();
        sprite.draw(batch);
        //texture, starting x, starting y, width, height
        //the title img is originally 128 by 32
//        batch.draw(titleimg, -50, Gdx.graphics.getHeight()/2, 1028, 256);
        float titleWidth = 2056;
        float titleHeight = 512;
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        batch.draw(titleimg, width / 2 - titleWidth / 2, (float)(height * 0.7) - titleHeight / 2, titleWidth, titleHeight);
        batch.end();

        //draws the buttons
        stage.act(delta);
        stage.draw();

    }
    @Override
    public void show () {
        openingtheme.play();
        openingtheme.setLooping(true);
        //openingtheme.setVolume(game.state.volume);
        batch = new SpriteBatch();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        //Straight up stolen from https://www.pinterest.comore
//        img = new Texture("menubackground1.png");
        img = new Texture("menu_background_fix.png");
        titleimg = new Texture("SlashHeroesTitle.png");
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
        parameter.size = 80;
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
        //final TextButton ContinueButton = new TextButton("Continue",textButtonStyle);
        final TextButton SettingsButton = new TextButton("Mute",textButtonStyle);
        final TextButton QuitButton = new TextButton("Quit",textButtonStyle);
        PlayButton.setPosition(Gdx.graphics.getWidth()/2 - 160, Gdx.graphics.getHeight()/2 - 150);
        //ContinueButton.setPosition(Gdx.graphics.getWidth()/2 - 160, Gdx.graphics.getHeight()/2 - 85);
//        SettingsButton.setPosition(Gdx.graphics.getWidth()/2 - 160, Gdx.graphics.getHeight()/2 - 165);
        SettingsButton.setPosition(Gdx.graphics.getWidth()/2 - 160, Gdx.graphics.getHeight()/2 - 300);
//        QuitButton.setPosition(Gdx.graphics.getWidth()/2 - 160, Gdx.graphics.getHeight()/2 - 245);
        QuitButton.setPosition(Gdx.graphics.getWidth()/2 - 160, Gdx.graphics.getHeight()/2 - 450);
        stage.addActor(PlayButton);
        //stage.addActor(ContinueButton);
        stage.addActor(SettingsButton);
        stage.addActor(QuitButton);

        PlayButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                select.play();
                PlayButton.setText("Starting new game");
                openingtheme.stop();
                game.setScreen(game.introscreen);

            }
        });
        SettingsButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                //System.out.println("Clicked! Is checked: " + button.isChecked());
                select.play();
                if (game.state.volume == 0f){
                    game.state.volume = .7f;
                    openingtheme.play();
                    openingtheme.setVolume(game.state.volume);
                    SettingsButton.setText("Mute");
                }else{
                    game.state.volume = 0f;
                    openingtheme.stop();
                    SettingsButton.setText("Unmute");
                }

                //TODO make this mute and unmute the non existant sound
                //mute and unmute;
            }

        });
        QuitButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                select.play();
                openingtheme.stop();
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
        select.dispose();
    }
}
