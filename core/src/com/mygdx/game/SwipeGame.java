package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.mesh.SwipeTriStrip;
import com.sun.org.apache.xerces.internal.util.ShadowedSymbolTable;

//implements screen instead of applicationadapter
public class SwipeGame implements Screen {
    OrthographicCamera cam;
    SpriteBatch batch;
    Texture img;
    SwipeHandler swipe;
    Texture tex;
    ShapeRenderer shapes;
    SwipeTriStrip tris;
    boolean isDown = false;
    boolean goodDraw = false;
    Array<Vector2> guide = new Array<Vector2>();
    double maxDist = 200;
    private BitmapFont font;
    String displayText = "";

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
        swipe = new SwipeHandler(10, this);

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

        for (int i = 0; i < 10; i++) {
            guide.add(new Vector2(400 + i * 100, 100 + i * 100));
        }
        font = new BitmapFont();
        font.setColor(Color.RED);
    }

    @Override
    public void render (float delta) {
//		Gdx.gl.glClearColor(1, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();

//        System.out.println(swipe.input());

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
        tris.draw(cam, isDown);

        //uncomment to see debug lines
//		drawDebug();
        drawGoal();

        batch.begin();
        font.draw(batch, displayText, 200, 1000);
        font.getData().setScale(4);
        batch.end();
    }

    public void touch(boolean _isDown) {
//        System.out.println("touch " + _isDown);
        this.isDown = _isDown;
        tris.resetAlpha();
        if (!_isDown) {
            CheckSwipePackage result = checkSwipe(swipe.input());
//            System.out.println(result);
            displayText = result.toString();
            if (result.completion > 0.5 && result.accuracy > 0.5) {
                goodDraw = true;
            } else {
                goodDraw = false;
            }
        }
    }

    class CheckSwipePackage {
        double completion = 0;
        double accuracy = 0;

        public CheckSwipePackage(double _completion, double _accuracy) {
            completion = _completion;
            accuracy = _accuracy;
        }

        public String toString() {
            return "completion: " + completion + ", accuracy: " + accuracy;
        }
    }

    public CheckSwipePackage checkSwipe(Array<Vector2> path) {
        boolean[] swiped = new boolean[guide.size];
        double[] distances = new double[guide.size];
        for (int i = 0; i < distances.length; i++) {
            distances[i] = -1;
        }

        for (int i = 0; i < path.size; i++) {
            int closestID = -1;
            double minDist = 0;
            for (int j = 0; j < guide.size; j++) {
                double dist = Math.sqrt(Math.pow(path.get(i).x - guide.get(j).x, 2) + Math.pow(path.get(i).y - guide.get(j).y, 2));
//                System.out.println(i + " : " + j + " : " + dist);
                if (dist < maxDist && (closestID == -1 || minDist > dist)) {
                    closestID = j;
                    minDist = dist;
//                    System.out.println(j + " : " + minDist);
                }
            }
            if (closestID != -1) {
                if (!swiped[closestID]) {
                    swiped[closestID] = true;
                    if (distances[closestID] == -1 || distances[closestID] > minDist) {
                        distances[closestID] = minDist;
                    }
                }
            }
        }

        int numSwiped = 0;
        double totalDist = 0;
        for (int i = 0; i < guide.size; i++) {
            if (swiped[i]) {
                numSwiped++;
            }
            totalDist += distances[i];
        }

        double completion = numSwiped / (double)guide.size;
        double accuracy = (maxDist - (totalDist / guide.size)) / maxDist;

        CheckSwipePackage output = new CheckSwipePackage(completion, accuracy);
        return output;
    }

    void drawGoal() {
//        Array<Vector2> input = new Array<Vector2>();
//        for (int i = 0; i < 10; i++) {
//            input.add(new Vector2(i * 100, i * 100));
//        }
//        input = swipe.input();

//        Array<Vector2> input = swipe.input();

        //draw the raw input
        shapes.begin(ShapeRenderer.ShapeType.Filled);
//        shapes.setColor(Color.GRAY);
        shapes.setColor(Color.RED);
        if (goodDraw) {
            shapes.setColor(Color.GREEN);
        }
        for (int i=0; i<guide.size-1; i++) {
            Vector2 p = guide.get(i);
            Vector2 p2 = guide.get(i+1);
//            shapes.line(p.x, p.y, p2.x, p2.y);
            shapes.rectLine(p, p2, 5);
//            shapes.
        }
        shapes.end();
    }

    //optional debug drawing..
    void drawDebug() {
        Array<Vector2> input = swipe.input();

        //draw the raw input
        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(Color.GRAY);
        for (int i = 0; i < input.size - 1; i++) {
            Vector2 p = input.get(i);
            Vector2 p2 = input.get(i + 1);
            shapes.line(p.x, p.y, p2.x, p2.y);
        }
        shapes.end();

        //draw the smoothed and simplified path
        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(Color.RED);
        Array<Vector2> out = swipe.path();
        for (int i = 0; i < out.size - 1; i++) {
            Vector2 p = out.get(i);
            Vector2 p2 = out.get(i + 1);
            shapes.line(p.x, p.y, p2.x, p2.y);
        }
        shapes.end();


        //render our perpendiculars
        shapes.begin(ShapeRenderer.ShapeType.Line);
        Vector2 perp = new Vector2();

        for (int i = 1; i < input.size - 1; i++) {
            Vector2 p = input.get(i);
            Vector2 p2 = input.get(i + 1);

            shapes.setColor(Color.LIGHT_GRAY);
            perp.set(p).sub(p2).nor();
            perp.set(perp.y, -perp.x);
            perp.scl(10f);
            shapes.line(p.x, p.y, p.x + perp.x, p.y + perp.y);
            perp.scl(-1f);
            shapes.setColor(Color.BLUE);
            shapes.line(p.x, p.y, p.x + perp.x, p.y + perp.y);
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
