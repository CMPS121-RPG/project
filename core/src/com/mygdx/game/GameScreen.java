package com.mygdx.game;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.audio.Music;

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

    Music battletheme = Gdx.audio.newMusic(Gdx.files.internal("FFVbattle.mp3"));
    Sound attacksound = Gdx.audio.newSound(Gdx.files.internal("attack.wav"));

    Skin skin;
    Stage stage;
    SpriteBatch batch;
    Sprite sprite;
    BitmapFont OurFont;
    //textures used
    Texture backgroundimg, partymember1img, greenslime, health1texture, health2texture, warriorsprite, archersprite, magesprite;
    //only needed for a spritesheet
    private TextureRegion[]     partymembersregion = new TextureRegion[4];
    TextButton attack1button, attack2button, attack3button, pausebutton;
    int partymemberturn;    //0: warrior 1:archer 2:mage
    int x1, x2, x3;         //positions for the party members sprites

    //get the number of enemies, and the type of enemies
    //for now theres 3
    int numberofenemies = 3;

    final MyGdxGame game;
    public GameScreen(final MyGdxGame game) {
        this.game = game;
    }

    //create the party members
    final WarriorClass SwordBro = new WarriorClass();
    float warriorhealth = SwordBro.basehealth;
    final ArcherClass Hippie = new ArcherClass();
    float archerhealth = Hippie.basehealth;
    final MageClass Avatar = new MageClass();
    float magehealth = Avatar.basehealth;

    //create the monsters... for now 3 slimes
    final SmallMonster slime1 = new SmallMonster();
    float slime1health = slime1.basehealth;
    final SmallMonster slime2 = new SmallMonster();
    float slime2health = slime2.basehealth;
    final SmallMonster slime3 = new SmallMonster();
    float slime3health = slime3.basehealth;

    //TODO make it so that the enemies on screen are the ones from the map?
    /* mapenemy1 is the value from the object that holds the enemy data in the map
        do this 2 more times for enemies 2 and 3
    if(getmapenemy1 == 1){
        final SmallMonster enemy1 = new SmallMonster();
        float enemy1health = enemy1.basehealth;
    }
    if(getmapenemy2 == 2){
        final MediumMonster enemy1 = new MediumMonster();
        float enemy1health = enemy1.basehealth;
    }
    if(getmapenemy3 == 3){
        final MediumMonster enemy1 = new MediumMonster();
        float enemy1health = enemy1.basehealth;
    }*/


    @Override
    public void show () {
        battletheme.play();
        battletheme.setVolume(game.state.volume);
        battletheme.setLooping(true);
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        //set the background image
        backgroundimg = new Texture("menubackground1.png");
        backgroundimg.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion region = new TextureRegion(backgroundimg, 0, 0, 600, 400);
        sprite = new Sprite(region);
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sprite.setOrigin(0, 0);

        //partymember1img = new Texture("tempsprite.png");
        //TextureRegion partymember1texture = new TextureRegion(partymember1img);
        //seperate the sprites from the sprite sheat
        //partymembersregion[0] = new TextureRegion(partymember1texture, 0, 0, 56, 56);
        //partymembersregion[1] = new TextureRegion(partymember1texture, 56, 0, 56, 56);
        //partymembersregion[2] = new TextureRegion(partymember1texture, 112, 0, 56, 56);

        greenslime = new Texture("GreenSlime.png");
        warriorsprite = new Texture("SlashHeores Warrior.png");
        archersprite = new Texture("SlashHeroes Archer.png");
        magesprite = new Texture("SlashHeroesMage.png");



        Pixmap healthbarpixmap = new Pixmap(100, 25, Pixmap.Format.RGBA8888);
        healthbarpixmap.setColor(Color.BLACK);
        healthbarpixmap.fill();
        Pixmap healthbarpixmap2 = new Pixmap(100, 25, Pixmap.Format.RGBA8888);
        healthbarpixmap2.setColor(Color.RED);
        healthbarpixmap2.fill();
        health1texture = new Texture(healthbarpixmap);
        health2texture = new Texture(healthbarpixmap2);



        skin = new Skin();
        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth()/4, 100, Pixmap.Format.RGBA8888);
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
        //textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        //textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        //textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        // Create a button with the "default" TextButtonStyle.
        attack1button = new TextButton("attack1",textButtonStyle);
        attack2button = new TextButton("attack2",textButtonStyle);
        attack3button = new TextButton("attack3",textButtonStyle);
        pausebutton = new TextButton("pause",textButtonStyle);
        attack1button.setPosition(0, 0);
        attack2button.setPosition(Gdx.graphics.getWidth()/4, 0);
        attack3button.setPosition(Gdx.graphics.getWidth()/2, 0);
        pausebutton.setPosition(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 4, 0);
        stage.addActor(attack1button);
        stage.addActor(attack2button);
        stage.addActor(attack3button);
        stage.addActor(pausebutton);

        //set the buttons texts to be different according to the different classes turns
        attack1button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                attacksound.play(.5f);
                warriorhealth = warriorhealth - 10;
                if(partymemberturn == 0){
                    game.swipegame.setScene(SwipeGame.SCENETYPE.WARRIOR1);
                    game.setScreen(game.swipegame);
                }
                if(partymemberturn == 1){
                    game.swipegame.setScene(SwipeGame.SCENETYPE.ARCHER1);
                    game.setScreen(game.swipegame);
                }
                if(partymemberturn == 2){
                    game.swipegame.setScene(SwipeGame.SCENETYPE.MAGE1);
                    game.setScreen(game.swipegame);
                }
                if(checkifwin() == true){
                    //TODO go back to the map
                }

                //TODO make this go to defend scene?

                if(checkiflose() == true){
                    //TODO go to lose screen
                    game.setScreen(game.startmenuscreen);
                }
                switchpartymember();
            }
        });
        attack2button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                attacksound.play();
                archerhealth = archerhealth - 10;

                if(partymemberturn == 0){
                    game.swipegame.setScene(SwipeGame.SCENETYPE.WARRIOR2);
                    game.setScreen(game.swipegame);
                }
                if(partymemberturn == 1){
                    game.swipegame.setScene(SwipeGame.SCENETYPE.ARCHER2);
                    game.setScreen(game.swipegame);
                }
                if(partymemberturn == 2){
                    game.swipegame.setScene(SwipeGame.SCENETYPE.MAGE2);
                    game.setScreen(game.swipegame);
                }
                if(checkifwin() == true){
                    //TODO go back to the map
                }

                //TODO make this go to defend scene?

                if(checkiflose() == true){
                    //TODO go to lose screen
                    game.setScreen(game.startmenuscreen);
                }
                switchpartymember();
            }
        });
        attack3button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                attacksound.play();
                magehealth = magehealth - 10;
                if(partymemberturn == 0){
                    game.swipegame.setScene(SwipeGame.SCENETYPE.WARRIOR3);
                    game.setScreen(game.swipegame);
                }
                if(partymemberturn == 1){
                    game.swipegame.setScene(SwipeGame.SCENETYPE.ARCHER3);
                    game.setScreen(game.swipegame);
                }
                if(partymemberturn == 2){
                    game.swipegame.setScene(SwipeGame.SCENETYPE.MAGE3);
                    game.setScreen(game.swipegame);
                }
                if(checkifwin() == true){
                    //TODO go back to the map
                }

                //TODO make this go to defend scene?

                if(checkiflose() == true){
                    //TODO go to lose screen
                    game.setScreen(game.startmenuscreen);
                }
                switchpartymember();
            }

        });
        pausebutton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                //TODO make this pause the game
                attacksound.play();
                pausebutton.setText("pausing");
                //game.swipegame.setScene(SwipeGame.SCENETYPE.WARRIOR1);
                //game.setScreen(game.pausescreen);
                game.setScreen(game.mapscreen);
                battletheme.stop();//use this whenever you change back to the map screen!
                //game.setScreen(game.pausescreen);
            }
        });

//        Music mp3Music = Gdx.audio.newMusic(Gdx.files.internal("data/RideOfTheValkyries.mp3"));
        Music mp3Music = Gdx.audio.newMusic(Gdx.files.internal("FFVbattle.mp3"));
        mp3Music.play();
    }
    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor((float) .1, (float) .1, (float) .66, (float) .8);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        batch.begin();
        //draws the background
        sprite.draw(batch);
        //draws the temp sprites        //x,y , zoomed x,y
        String warriorhealthstring = "Slashy " + (int)warriorhealth + "/100";
        String magehealthstring = "Avatar " + (int)magehealth + "/50";
        String archerhealthstring = "Woosh " + (int)archerhealth + "/75";

        placepartymembers();
        //draws the temp sprites        //x,y , zoomed x,y
        OurFont.setColor(1, 1, 1, 1);


        if(warriorhealth > 0) {
            float warriorhealthbar = (float)(220.0 * (warriorhealth / SwordBro.basehealth));
            batch.draw(warriorsprite, x1, 100, 128, 128);
            batch.draw(health1texture, 20, Gdx.graphics.getHeight() - 315, 220, 20);
            batch.draw(health2texture, 20, Gdx.graphics.getHeight() - 315, warriorhealthbar, 20);
            OurFont.draw(batch, warriorhealthstring, 20, Gdx.graphics.getHeight() - 300);   //-15 of whatever the healthbars y is
        }
        if(archerhealth > 0) {
            float archerhealthbar = (float)(220.0 * (archerhealth / 75.0));
            batch.draw(archersprite, x2, 240, 128, 128);
            batch.draw(health1texture, 20, Gdx.graphics.getHeight() - 190, 220, 20);
            batch.draw(health2texture, 20, Gdx.graphics.getHeight() - 190, archerhealthbar, 20);
            OurFont.draw(batch, archerhealthstring , 20, Gdx.graphics.getHeight() - 175);
        }
        if(magehealth > 0) {
            float magehealthbar = (float)(220.0 * (magehealth / 50.0));
            batch.draw(magesprite, x3, 370, 128, 128);
            batch.draw(health1texture, 20, Gdx.graphics.getHeight() - 45, 220, 20);
            batch.draw(health2texture, 20, Gdx.graphics.getHeight() - 45, magehealthbar, 20);
            OurFont.draw(batch, magehealthstring , 20, Gdx.graphics.getHeight() - 30);
        }

        //draws the enemies for now, probably change this to a system like the one for party members
        /*for(int i = 0; i < numberofenemies; i++) {
            batch.draw(greenslime, Gdx.graphics.getWidth() - 100, (100*i) + 150, 64, 64);
        }*/

        //TODO Set up this logic for the enemy types
        /*
        String enemy1healthstring = enemy1.name + " " + (int)enemy1health + "/" + enemy1.basehealth;
        String enemy2healthstring = enemy2.name + " " + (int)enemy2health + "/" + enemy2.basehealth;
        String enemy3healthstring = enemy3.name + " " + (int)enemy3health + "/" + enemy3.basehealth;

        if(enemy1health > 0){

            float enemy1healthbar = (float)(220.0 * (enemy1health / slime1.basehealth));
            batch.draw(greenslime, Gdx.graphics.getWidth() - 100, 100 + 150, 64, 64);
            batch.draw(health1texture, 600, Gdx.graphics.getHeight() - 45, 220, 20);
            batch.draw(health2texture, 600, Gdx.graphics.getHeight() - 45, 220, 20);
            OurFont.draw(batch, enemy1healthstring , 20, Gdx.graphics.getHeight() - 235);

        }
        if(enemy2health > 0){

            float enemy2healthbar = (float)(220.0 * (enemy2health / slime2.basehealth));
            batch.draw(greenslime, Gdx.graphics.getWidth() - 100, 200 + 150, 64, 64);
            batch.draw(health1texture, 600, Gdx.graphics.getHeight() - 250, 220, 20);
            batch.draw(health2texture, 600, Gdx.graphics.getHeight() - 250, 220, 20);
            OurFont.draw(batch, enemy2healthstring , 20, Gdx.graphics.getHeight() - 235);
        }
        if(enemy3health > 0){

            float enemy3healthbar = (float)(220.0 * (enemy3health / slime3.basehealth));
            batch.draw(greenslime, Gdx.graphics.getWidth() - 100, 300 + 150, 64, 64);
            batch.draw(health1texture, 600, Gdx.graphics.getHeight() - 350, 220, 20);
            batch.draw(health2texture, 600, Gdx.graphics.getHeight() - 350, 220, 20);
            OurFont.draw(batch, enemy3healthstring , 20, Gdx.graphics.getHeight() - 235);
        }*/


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
        stage.dispose();
        skin.dispose();
        attacksound.dispose();
    }
    public void switchpartymember(){
        if(partymemberturn == 0){
            if(archerhealth > 0) {
                partymemberturn = 1;
                return;
            }
            if(magehealth > 0) {
                partymemberturn = 2;
                return;
            }
        }
        if(partymemberturn == 1){
            if(magehealth > 0) {
                partymemberturn = 2;
                return;
            }
            if(warriorhealth > 0){
                partymemberturn = 0;
                return;
            }
        }
        if(partymemberturn == 2){
            if(warriorhealth > 0) {
                partymemberturn = 0;
                return;
            }
            if(archerhealth > 0){
                partymemberturn = 1;
                return;
            }
        }
    }

    public boolean checkiflose(){
        Boolean lose;
        if(warriorhealth <= 0 && archerhealth <= 0 && magehealth <= 0){
            lose = true;
            return lose;
        }else{
            lose = false;
            return lose;
        }
    }

    public boolean checkifwin(){
        Boolean win;
        if(slime1health <= 0 && slime2health <= 0 && slime3health <= 0){
            win = true;
            return win;
        }else{
            win = false;
            return win;
        }
    }
    public void placepartymembers(){
        x1 = 50;
        x2 = 50;
        x3 = 50;
        if(partymemberturn == 0) {
            x1 = (Gdx.graphics.getWidth()/2 - 280);
            attack1button.setText("Heroic Strike");
            attack2button.setText("Onslaught");
            attack3button.setText("Armor Breaker");
        }
        if(partymemberturn == 1){
            x2 = (Gdx.graphics.getWidth()/2 - 280);
            attack1button.setText("Steady Shot");
            attack2button.setText("Hail of Arrows");
            attack3button.setText("Aimed Shot");
        }
        if(partymemberturn == 2) {
            x3 = (Gdx.graphics.getWidth()/2 - 280);
            attack1button.setText("Fire");
            attack2button.setText("Earth");
            attack3button.setText("Water");
        }

    }

    public void dealDamage () {
        int amt = 20;
        if (slime1health > 0) {
            slime1health -= amt;
        } else if (slime2health > 0) {
            slime2health -= amt;
        } else {
            slime3health -= amt;
        }
    }
}
