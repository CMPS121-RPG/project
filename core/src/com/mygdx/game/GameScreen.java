package com.mygdx.game;

import com.badlogic.gdx.Game;
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

import java.awt.Label;
import java.awt.TextField;

/*
The main gameScreen
displays health, party member sprites, enemy sprites, and 4 buttons:
    attack1, attack2, attack3, pause
    if attack button pressed goto attackscreen (the swiping thing) returns success or fail or %
        this determines how much health the enemy loses
    if pause pressed goto the pause screen
    if monsters health == 0 it dies, if no more monsters you win
        if win return to map screen
    after your attack the enemy attacks
        goto defense screen (the swiping thing) returns success or fail or %
    if partymembers health == 0 they die, if no members you lose
        if lose lose screen? quit game? or goto main menu?
    if partymembers health > 0  they can attack
 */
public class GameScreen implements Screen{

    Skin skin;
    Stage stage;
    SpriteBatch batch;
    Texture backgroundimg, partymember1img;
    Sprite sprite;
    BitmapFont OurFont;
    CharSequence currentpartymember = "warrior";
    private TextureRegion[]     partymembersregion = new TextureRegion[4];
    int partymemberturn;

    final MyGdxGame game;
    public GameScreen(final MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show () {
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        //set the background image
        backgroundimg = new Texture("menubackground1.png");
        backgroundimg.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        //change these numbers around so it looks good on the presentation phone
        TextureRegion region = new TextureRegion(backgroundimg, 0, 0, 600, 400);
        sprite = new Sprite(region);
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sprite.setOrigin(0, 0);

        partymember1img = new Texture("tempsprite.png");
        TextureRegion partymember1texture = new TextureRegion(partymember1img);
        //seperate the sprites from the sprite sheat
        partymembersregion[0] = new TextureRegion(partymember1texture, 0, 0, 56, 56);
        partymembersregion[1] = new TextureRegion(partymember1texture, 56, 0, 56, 56);
        partymembersregion[2] = new TextureRegion(partymember1texture, 112, 0, 56, 56);
        partymembersregion[3] = new TextureRegion(partymember1texture, 168, 0, 56, 56);

        skin = new Skin();
        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(200, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        //create the custom font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/slkscre.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 18;
        OurFont = generator.generateFont(parameter);
        generator.dispose();

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
        final TextButton attack1button = new TextButton("attack1",textButtonStyle);
        final TextButton attack2button = new TextButton("attack2",textButtonStyle);
        final TextButton attack3button = new TextButton("attack3",textButtonStyle);
        final TextButton pausebutton = new TextButton("pause",textButtonStyle);
        attack1button.setPosition(0, 0);
        attack2button.setPosition(200, 0);
        attack3button.setPosition(400, 0);
        pausebutton.setPosition(Gdx.graphics.getWidth() - 200, 0);
        stage.addActor(attack1button);
        stage.addActor(attack2button);
        stage.addActor(attack3button);
        stage.addActor(pausebutton);

        //set the buttons texts to be different according to the different classes turns
        attack1button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                attack1button.setText("Attacking");
                //TODO
                //game.setScreen(game.swipegame);
                switchpartymember();

            }
        });
        attack2button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                //System.out.println("Clicked! Is checked: " + button.isChecked());
                attack2button.setText("Attacking");

                //TODO
                //game.setScreen(game.swipegame);
                switchpartymember();
            }
        });
        attack3button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                //System.out.println("Clicked! Is checked: " + button.isChecked());
                attack3button.setText("Attacking");

                //TODO
                //game.setScreen(game.swipegame);
                switchpartymember();
            }

        });
        pausebutton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                //TODO make this pause the game
                pausebutton.setText("pausing");
                game.setScreen(game.swipegame);
            }
        });

    }
    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor((float) .1, (float) .1, (float) .66, (float) .8);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        batch.begin();
        //draws the background
        sprite.draw(batch);
        //draws the temp sprites        //x,y , zoomed x,y
        int x1, x2, x3, x4;
        x1 = 50;
        x2 = 50;
        x3 = 50;
        x4 = 50;
        if(partymemberturn == 0) {
            x1 = (Gdx.graphics.getWidth()/2 - 56) ;
        }
        if(partymemberturn == 1){
            x2 = (Gdx.graphics.getWidth()/2 - 56) ;
        }
        if(partymemberturn == 2) {
            x3 = (Gdx.graphics.getWidth()/2 - 56) ;
        }
        if(partymemberturn == 3){
            x4 = (Gdx.graphics.getWidth()/2 - 56) ;
        }
        batch.draw(partymembersregion[0], x1, 100, 112, 112);
        batch.draw(partymembersregion[1], x2, 200, 112, 112);
        batch.draw(partymembersregion[2], x3, 300, 112, 112);
        batch.draw(partymembersregion[3], x4, 400, 112, 112);

        //display who's turn it is
        OurFont.setColor(1, 1, 1, 1);
        OurFont.draw(batch, currentpartymember , Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight() - 50);

        batch.end();

        //draws the buttons
        stage.act(delta);
        stage.draw();
    }

    public void switchpartymember(){
        if(partymemberturn == 0){
            partymemberturn = 1;
            currentpartymember = "warrior";
            return;
        }
        if(partymemberturn == 1){
            partymemberturn = 2;
            currentpartymember = "mage";
            return;
        }
        if(partymemberturn == 2){
            partymemberturn = 3;
            currentpartymember = "archer";
            return;
        }
        if(partymemberturn == 3){
            partymemberturn = 0;
            currentpartymember = "bardmonk";
            return;
        }
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
