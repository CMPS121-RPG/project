package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.mesh.SwipeTriStrip;

import java.awt.SystemTray;

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
    Array<Array<Vector2>> guides = new Array<Array<Vector2>>();
    double maxDist = 200;
    private BitmapFont font;
    String displayText = "";
    boolean goodSwipes[] = new boolean[0];
    double lineWidth = 5;
    int minWidth = 15;
    int maxWidth = 30;
    double curTime = 0;
    double pulseRate = 0.03;
    boolean widthIncreasing = true;
//    Texture backgroundimg;
//    Sprite sprite;


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
        swipe = new SwipeHandler(100, this);

        //minimum distance between two points
        swipe.minDistance = 10;

        //minimum distance between first and second point
        swipe.initialDistance = 10;

        //we will use a texture for the smooth edge, and also for stroke effects
        tex = new Texture("gradient.png");
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

//        //set the background image
//        backgroundimg = new Texture("menubackground1.png");
//        backgroundimg.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//        TextureRegion region = new TextureRegion(backgroundimg, 0, 0, 600, 400);
//        sprite = new Sprite(region);
//        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        sprite.setOrigin(0, 0);

        shapes = new ShapeRenderer();
        batch = new SpriteBatch();

        cam = new OrthographicCamera();
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //handle swipe input
        Gdx.input.setInputProcessor(swipe);

//        for (int i = 0; i < 10; i++) {
//            guides.add(new Vector2(400 + i * 100, 100 + i * 100));
//        }
        font = new BitmapFont();
        font.setColor(Color.RED);

        curTime = System.currentTimeMillis();
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
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);

        cam.update();
        batch.setProjectionMatrix(cam.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        tex.bind();
        //draws the background
//        batch.begin();
//        sprite.draw(batch);
//        batch.end();

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
        double newTime = System.currentTimeMillis();
        double timeDif = newTime - curTime;
        if (widthIncreasing) {
            lineWidth += timeDif * pulseRate;
            if (lineWidth >= maxWidth) {
                lineWidth = maxWidth;
                widthIncreasing = false;
            }
        } else {
            lineWidth -= timeDif * pulseRate;
            if (lineWidth <= minWidth) {
                lineWidth = minWidth;
                widthIncreasing = true;
            }
        }
//        log("" + timeDif, "" + timeDif * pulseRate, "" + lineWidth, "" + widthIncreasing);
        curTime = newTime;

        drawGoal();

        batch.begin();
        font.draw(batch, displayText, 200, 1000);
        font.getData().setScale(4);
        batch.end();
    }

    class Point {
        double x;
        double y;
        public Point(double _x, double _y) {
            x = _x;
            y = _y;
        }
    }

    Array<Array<Vector2>> makeGuides(Point[]... pointArrays) {
        Array<Array<Vector2>> output = new Array<Array<Vector2>>();
        for(Point[] points : pointArrays) {
            Array<Vector2> row = new Array<Vector2>();
            for (Point point : points) {
                row.add(loc(point));
            }
            output.add(row);
        }
        return output;
    }

    Vector2 loc(double _widthPercent, double _heightPercent) {
        float widthPercent = (float) _widthPercent;
        float heightPercent = (float) _heightPercent;
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        return new Vector2(width * widthPercent, height * heightPercent);
    }

    Vector2 loc(Point point) {
        float widthPercent = (float) point.x;
        float heightPercent = (float) point.y;
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        return new Vector2(width * widthPercent, height * heightPercent);
    }

    Point point(double x, double y) {
        return new Point(x, y);
    }

    public void setScene(SCENETYPE sceneType) {
//        System.out.println(guides);
//        guides = new Array<Array>();
//        Array<Vector2[]> newGuides = new Array<Vector2[]>();
        switch (sceneType) {
            case WARRIOR1:
//                newGuide = new Vector2[]{
//                        loc(0.2, 0.2),
//                        loc(0.4, 0.4),
//                        loc(0.6, 0.6),
//                        loc(0.8, 0.8)
//                };
                guides = makeGuides(
                        new Point[]{
                                point(0.2, 0.2),
                                point(0.4, 0.4),
                                point(0.6, 0.6),
                                point(0.8, 0.8)}
                );
//                System.out.println(guides);
                goodSwipes = new boolean[1];
                break;
            case WARRIOR2:
//                newGuide = new Vector2[]{
//                        loc()
//                };
                guides = makeGuides(
                        new Point[]{
                                point(0.2, 0.2),
                                point(0.4, 0.4),
                                point(0.6, 0.6),
                                point(0.8, 0.8)},
                        new Point[]{
                                point(0.2, 0.8),
                                point(0.4, 0.6),
                                point(0.6, 0.4),
                                point(0.8, 0.2)
                        }
                );
                goodSwipes = new boolean[2];
                break;
            case WARRIOR3:
                guides = makeGuides(
                        new Point[]{
                                point(0.2, 0.2),
                                point(0.4, 0.2),
                                point(0.6, 0.2),
                                point(0.8, 0.2)},
                        new Point[]{
                                point(0.2, 0.5),
                                point(0.4, 0.5),
                                point(0.6, 0.5),
                                point(0.8, 0.5)},
                        new Point[]{
                                point(0.2, 0.8),
                                point(0.4, 0.8),
                                point(0.6, 0.8),
                                point(0.8, 0.8)}
                );
                goodSwipes = new boolean[3];
                break;
            case ARCHER1:
                guides = makeGuides(
                        new Point[]{
                                point(0.5, 0.2),
                                point(0.25, 0.5),
                                point(0.5, 0.8),
                                point(0.75, 0.5),
                                point(0.5, 0.2)}
                );
                goodSwipes = new boolean[1];
                break;
            case ARCHER2:
                guides = makeGuides(
                        new Point[]{
                                point(0.2, 0.2),
                                point(0.2, 0.4),
                                point(0.2, 0.6),
                                point(0.2, 0.8)},
                        new Point[]{
                                point(0.5, 0.2),
                                point(0.5, 0.4),
                                point(0.5, 0.6),
                                point(0.5, 0.8)},
                        new Point[]{
                                point(0.8, 0.2),
                                point(0.8, 0.4),
                                point(0.8, 0.6),
                                point(0.8, 0.8)}
                );
                goodSwipes = new boolean[3];
                break;
            case ARCHER3:
                guides = makeGuides(
                        new Point[]{
                                point(0.2, 0.2),
                                point(0.8, 0.5),
                                point(0.2, 0.8)}
                );
                goodSwipes = new boolean[1];
                break;
            case MAGE1:
                guides = makeGuides(
                        new Point[]{
                                point(0.4, 0.6),
                                point(0.2, 0.5),
                                point(0.2, 0.3),
                                point(0.4, 0.2)},
                        new Point[]{
                                point(0.6, 0.8),
                                point(0.5, 0.6),
                                point(0.5, 0.4),
                                point(0.5, 0.2)},
                        new Point[]{
                                point(0.6, 0.6),
                                point(0.8, 0.5),
                                point(0.8, 0.3),
                                point(0.6, 0.2)}
                );
                goodSwipes = new boolean[3];
                break;
            case MAGE2:
                guides = makeGuides(
                        new Point[]{
                                point(0.4, 0.6),
                                point(0.2, 0.5),
                                point(0.4, 0.3),
                                point(0.2, 0.2)},
                        new Point[]{
                                point(0.6, 0.8),
                                point(0.4, 0.6),
                                point(0.6, 0.4),
                                point(0.4, 0.2)},
                        new Point[]{
                                point(0.6, 0.6),
                                point(0.8, 0.5),
                                point(0.6, 0.3),
                                point(0.8, 0.2)}
                );
                goodSwipes = new boolean[3];
                break;
            case MAGE3:
                guides = makeGuides(
                        new Point[]{
                                point(0.2, 0.1),
                                point(0.3, 0.3),
                                point(0.4, 0.5),
                                point(0.5, 0.7),
                                point(0.6, 0.9),
                                point(0.7, 0.7),
                                point(0.8, 0.5),
                                point(0.7, 0.3),
                                point(0.6, 0.1),
                                point(0.5, 0.3)}
                );
                goodSwipes = new boolean[1];
                break;
        }
//        guides.addAll(newGuide);
//        System.out.println(guides);
//        guides = newGuides;
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
//                goodDraw = true;
                goodSwipes[result.index] = true;
            }
            System.out.println("=====");
            for (boolean goodSwipe:
                    goodSwipes) {
                log("" + goodSwipe);
            }
            boolean flag = true;
            for (boolean goodSwipe:
                 goodSwipes) {
                if (!goodSwipe) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                game.gamescreen.dealDamage();
                game.setScreen(game.gamescreen);
            }
        }
    }

    class CheckSwipePackage {
        double completion = 0;
        double accuracy = 0;
        int index = 0;

        public CheckSwipePackage(double _completion, double _accuracy, int _index) {
            completion = _completion;
            accuracy = _accuracy;
            index = _index;
        }

        public String toString() {
            return "completion: " + completion + ", accuracy: " + accuracy + ", index: " + index;
        }
    }

    public CheckSwipePackage checkSwipe(Array<Vector2> path) {
        CheckSwipePackage output = new CheckSwipePackage(0, 0, -1);

        for (int x = 0; x < guides.size; x++) {
            Array<Vector2> guide = guides.get(x);
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
                    }
                    if (distances[closestID] == -1 || distances[closestID] > minDist) {
                        distances[closestID] = minDist;
                    }
                }
//            if (closestID != -1) {
//                log("" + path.get(i), "" + guides.get(closestID), "" + closestID, "" + minDist);
//            }
            }

            int numSwiped = 0;
            double totalDist = 0;
            for (int i = 0; i < guide.size; i++) {
                if (swiped[i]) {
                    numSwiped++;
                    totalDist += distances[i]; // TODO Fix accuracy on less than 100% swipe completion
                }
            }

            double completion = numSwiped / (double) guide.size;
            double accuracy = (maxDist - (totalDist / guide.size)) / maxDist;

//        for (double d : distances) {
//            log("" + d);
//        }

//        CheckSwipePackage output = new CheckSwipePackage(completion, accuracy);
            if (completion > output.completion) {
                output = new CheckSwipePackage(completion, accuracy, x);
            }
        }

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
//        if (goodDraw) {
//            shapes.setColor(Color.GREEN);
//        }
//        System.out.println(guides.toArray().length);
//        Vector2[][] foo = guides.toArray();
//        System.out.println(foo[0][0]);
//        log(guides.toString());
        for (int j = 0; j < guides.size; j++) {
            if (goodSwipes[j]) {
                continue;
            }
            Array<Vector2> guide = guides.get(j);
//            log(guide.toString());
            for (int i = 0; i < guide.size - 1; i++) {
                Vector2 p = guide.get(i);
                Vector2 p2 = guide.get(i + 1);
//            shapes.line(p.x, p.y, p2.x, p2.y);
                shapes.rectLine(p, p2, (float)lineWidth);
//                log("" + p, "" + p2);
//            shapes.
            }
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

    public enum SCENETYPE {
        WARRIOR1,
        WARRIOR2,
        WARRIOR3,
        ARCHER1,
        ARCHER2,
        ARCHER3,
        MAGE1,
        MAGE2,
        MAGE3
    }

    void log (String... args) {
        String output = "";
        for (String s : args) {
            output += s + " : ";
        }
        System.out.println(output);
    }
}
