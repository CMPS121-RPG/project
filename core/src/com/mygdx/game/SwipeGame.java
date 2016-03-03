package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.mesh.SwipeTriStrip;

//implements screen instead of applicationadapter
public class SwipeGame implements Screen {
    OrthographicCamera cam;
    SpriteBatch batch;
    Texture img;
    SwipeHandler swipe;
    Texture tex;
    ShapeRenderer shapes;
    SwipeTriStrip tris;

    //probably wont be neccassary for the actual game
    final MyGdxGame game;
    public SwipeGame(final MyGdxGame game) {
        this.game = game;
    }

    @Override
    public void show () {
        batch = new SpriteBatch();
//        img = new Texture("badlogic.jpg");

        //the triangle strip renderer
        tris = new SwipeTriStrip();

        //a swipe handler with max # of input points to be kept alive
        swipe = new SwipeHandler(10);

        //minimum distance between two points
        swipe.minDistance = 10;

        //minimum distance between first and second point
        swipe.initialDistance = 10;

        //we will use a texture for the smooth edge, and also for stroke effects
        tex = new Texture("gradient.png");
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        shapes = new ShapeRenderer();
        batch = new SpriteBatch();

        cam = new OrthographicCamera();
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //handle swipe input
        Gdx.input.setInputProcessor(swipe);
    }

    @Override
    public void render (float delta) {
//		Gdx.gl.glClearColor(1, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.update();
        batch.setProjectionMatrix(cam.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        tex.bind();

        //the endcap scale
//		tris.endcap = 5f;

        //the thickness of the line
        tris.thickness = 30f;

        //generate the triangle strip from our path
        tris.update(swipe.path());

        //the vertex color for tinting, i.e. for opacity
        tris.color = Color.WHITE;

        //render the triangles to the screen
        tris.draw(cam);

        //uncomment to see debug lines
//		drawDebug();
    }


    //optional debug drawing..
    void drawDebug() {
        Array<Vector2> input = swipe.input();

        //draw the raw input
        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(Color.GRAY);
        for (int i=0; i<input.size-1; i++) {
            Vector2 p = input.get(i);
            Vector2 p2 = input.get(i+1);
            shapes.line(p.x, p.y, p2.x, p2.y);
        }
        shapes.end();

        //draw the smoothed and simplified path
        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(Color.RED);
        Array<Vector2> out = swipe.path();
        for (int i=0; i<out.size-1; i++) {
            Vector2 p = out.get(i);
            Vector2 p2 = out.get(i+1);
            shapes.line(p.x, p.y, p2.x, p2.y);
        }
        shapes.end();


        //render our perpendiculars
        shapes.begin(ShapeRenderer.ShapeType.Line);
        Vector2 perp = new Vector2();

        for (int i=1; i<input.size-1; i++) {
            Vector2 p = input.get(i);
            Vector2 p2 = input.get(i+1);

            shapes.setColor(Color.LIGHT_GRAY);
            perp.set(p).sub(p2).nor();
            perp.set(perp.y, -perp.x);
            perp.scl(10f);
            shapes.line(p.x, p.y, p.x+perp.x, p.y+perp.y);
            perp.scl(-1f);
            shapes.setColor(Color.BLUE);
            shapes.line(p.x, p.y, p.x+perp.x, p.y+perp.y);
        }
        shapes.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        shapes.dispose();
        tex.dispose();
    }

    @Override
    public void resize (int width, int height) {
    }

    @Override
    public void hide() {
    }

}
