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
    Sprite sprite;
    BitmapFont OurFont;
    //textures used
    Texture backgroundimg, partymember1img, greenslime;
    CharSequence currentpartymember = "warrior";
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
    int warriorhealth = SwordBro.basehealth;
    final ArcherClass Hippie = new ArcherClass();
    int archerhealth = Hippie.basehealth;
    final MageClass Avatar = new MageClass();
    int magehealth = Avatar.basehealth;

    //create the monsters... for now 3 slimes
    final SmallMonster slime1 = new SmallMonster();
    int slime1health = slime1.basehealth;
    final SmallMonster slime2 = new SmallMonster();
    int slime2health = slime2.basehealth;
    final SmallMonster slime3 = new SmallMonster();
    int slime3health = slime3.basehealth;


    @Override
    public void show () {

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

        partymember1img = new Texture("tempsprite.png");
        TextureRegion partymember1texture = new TextureRegion(partymember1img);
        //seperate the sprites from the sprite sheat
        partymembersregion[0] = new TextureRegion(partymember1texture, 0, 0, 56, 56);
        partymembersregion[1] = new TextureRegion(partymember1texture, 56, 0, 56, 56);
        partymembersregion[2] = new TextureRegion(partymember1texture, 112, 0, 56, 56);

        greenslime = new Texture("GreenSlime.png");

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
        pausebutton.setPosition(Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/4, 0);
        stage.addActor(attack1button);
        stage.addActor(attack2button);
        stage.addActor(attack3button);
        stage.addActor(pausebutton);

        //set the buttons texts to be different according to the different classes turns
        attack1button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                warriorhealth = warriorhealth - 10;
                if(partymemberturn == 0){
                    //TODO go to warrior 1 attack
                    game.swipegame.setScene(SwipeGame.SCENETYPE.WARRIOR1);
                    game.setScreen(game.swipegame);
                }
                if(partymemberturn == 1){
                    //TODO go to archer 1 attack
                    game.swipegame.setScene(SwipeGame.SCENETYPE.ARCHER1);
                    game.setScreen(game.swipegame);
                }
                if(partymemberturn == 2){
                    //TODO go to mage 1 attack
                    game.swipegame.setScene(SwipeGame.SCENETYPE.MAGE1);
                    game.setScreen(game.swipegame);
                }
                if(checkifwin() == true){
                    //TODO go back to the map
                }
                //defend thing
                if(checkiflose() == true){
                    //TODO go to lose screen
                    game.setScreen(game.startmenuscreen);
                }
                switchpartymember();
            }
        });
        attack2button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                archerhealth = archerhealth - 10;
                if(partymemberturn == 0){
                    //TODO go to warrior 2 attack
                    game.swipegame.setScene(SwipeGame.SCENETYPE.WARRIOR2);
                    game.setScreen(game.swipegame);
                }
                if(partymemberturn == 1){
                    //TODO go to archer 2 attack
                    game.swipegame.setScene(SwipeGame.SCENETYPE.ARCHER2);
                    game.setScreen(game.swipegame);
                }
                if(partymemberturn == 2){
                    //TODO go to mage 2 attack
                    game.swipegame.setScene(SwipeGame.SCENETYPE.MAGE2);
                    game.setScreen(game.swipegame);
                }
                if(checkifwin() == true){
                    //TODO go back to the map
                }
                //defend thing
                if(checkiflose() == true){
                    //TODO go to lose screen
                    game.setScreen(game.startmenuscreen);
                }
                switchpartymember();
            }
        });
        attack3button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                magehealth = magehealth - 10;
                if(partymemberturn == 0){
                    //TODO go to warrior 3 attack
                    game.swipegame.setScene(SwipeGame.SCENETYPE.WARRIOR3);
                    game.setScreen(game.swipegame);
                }
                if(partymemberturn == 1){
                    //TODO go to archer 3 attack
                    game.swipegame.setScene(SwipeGame.SCENETYPE.ARCHER3);
                    game.setScreen(game.swipegame);
                }
                if(partymemberturn == 2){
                    //TODO go to mage 3 attack
                    game.swipegame.setScene(SwipeGame.SCENETYPE.MAGE3);
                    game.setScreen(game.swipegame);
                }
                if(checkifwin() == true){
                    //TODO go back to the map
                }
                //defend thing
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
                pausebutton.setText("pausing");
                game.swipegame.setScene(SwipeGame.SCENETYPE.WARRIOR1);
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
        int x1, x2, x3;
        x1 = 50;
        x2 = 50;
        x3 = 50;
        if(partymemberturn == 0) {
            x1 = (Gdx.graphics.getWidth()/2 - 280);
            currentpartymember = "Warrior " + warriorhealth;
            attack1button.setText("Heroic Strike");
            attack2button.setText("Onslaught");
            attack3button.setText("Armor Breaker");
        }
        if(partymemberturn == 1){
            x2 = (Gdx.graphics.getWidth()/2 - 280);
            currentpartymember = "Archer " + archerhealth;
            attack1button.setText("Steady Shot");
            attack2button.setText("Hail of Arrows");
            attack3button.setText("Aimed Shot");
        }
        if(partymemberturn == 2) {
            x3 = (Gdx.graphics.getWidth()/2 - 280);
            currentpartymember = "Avatar " + magehealth;
            attack1button.setText("Fire");
            attack2button.setText("Earth");
            attack3button.setText("Water");
        }

        placepartymembers();
        //draws the temp sprites        //x,y , zoomed x,y
        if(warriorhealth > 0) {
            batch.draw(partymembersregion[0], x1, 100, 112, 112);
        }
        if(archerhealth > 0) {
            batch.draw(partymembersregion[1], x2, 200, 112, 112);
        }
        if(magehealth > 0) {
            batch.draw(partymembersregion[2], x3, 300, 112, 112);
        }

        //draws the enemies for now, probably change this to a system like the one for party members
        for(int i = 0; i < numberofenemies; i++) {
            batch.draw(greenslime, Gdx.graphics.getWidth() - 100, (100*i) + 150, 64, 64);
        }

        //display who's turn it is
        OurFont.setColor(1, 1, 1, 1);
        OurFont.draw(batch, currentpartymember , 20, Gdx.graphics.getHeight() - 30);

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
            currentpartymember = "Warrior " + warriorhealth + "/100";
            attack1button.setText("Heroic Strike");
            attack2button.setText("Onslut");
            attack3button.setText("Armor Breaker");
        }
        if(partymemberturn == 1){
            x2 = (Gdx.graphics.getWidth()/2 - 280);
            currentpartymember = "Archer " + archerhealth + "/75";
            attack1button.setText("Steady Shot");
            attack2button.setText("Hail of Arrows");
            attack3button.setText("Aimed Shot");
        }
        if(partymemberturn == 2) {
            x3 = (Gdx.graphics.getWidth()/2 - 280);
            currentpartymember = "Avatar " + magehealth + "/50";
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
