package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

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
    final MyGdxGame game;
    public GameScreen(final MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show () {
    }
    @Override
    public void render (float delta) {
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
