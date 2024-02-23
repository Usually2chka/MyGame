package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Sprites.Hero;
import com.mygdx.game.Tools.B2WorldCreator;


public class PlayScreen implements Screen, InputProcessor {

    private MyGdxGame game;
    private TextureAtlas atlas;


    private Hud hud;
    private OrthographicCamera gameCam;
    private Viewport gamePort;//, gamePort2;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;

    private Hero player;

    private static int jump = 0;



    public PlayScreen(MyGdxGame game)
    {
        atlas = new TextureAtlas("proba2.atlas"); //justHero.atlas

        this.game = game;
        gameCam = new OrthographicCamera();

        gamePort = new FillViewport(MyGdxGame.V_WIDTH / MyGdxGame.PPM,MyGdxGame.V_HEIGHT / MyGdxGame.PPM, gameCam);// поле видимости нашего персонажа

        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("TmxPr2.tmx");

        renderer = new OrthogonalTiledMapRenderer(map, 1 / MyGdxGame.PPM);
        gameCam.position.set(gamePort.getScreenWidth() / 2 , gamePort.getScreenHeight() / 2 , 0);

        world = new World(new Vector2(0, -10), true);

        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(world, map);

        player = new Hero(world, this);

        Gdx.input.setInputProcessor(this);
    }

    public TextureAtlas getAtlas()
    {
        return atlas;
    }
//comment

    @Override
    public void show() {

    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(((screenX < gamePort.getScreenWidth() / 2) && player.b2body.getLinearVelocity().x >= -1.5) && Gdx.input.isTouched())
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        if(((screenX > gamePort.getScreenWidth() / 2) && player.b2body.getLinearVelocity().x <= 1.5) && Gdx.input.isTouched())
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        if(((screenY > gamePort.getScreenHeight() / 2.3) && player.b2body.getLinearVelocity().y <= 3) && Gdx.input.isTouched() && jump < 2)
        {
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
            jump++;
        }


        return false;
    }
    public void handleInput(float dt) //for pc
    {
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && jump < 2)
        {
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
            jump++;
        }
        if((Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2))
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        if((Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2))
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
    }

    public void update(float dt)
    {
        if(player.b2body.getLinearVelocity().y == 0)
            jump = 0;
        handleInput(dt);
        touchDown(Gdx.input.getX(), Gdx.input.getY(), 0, 0);

        world.step(1/60f, 6, 2);

        player.update(dt);

        gameCam.position.y = player.b2body.getPosition().y + (75 / MyGdxGame.PPM);
        gameCam.position.x = player.b2body.getPosition().x + (100 / MyGdxGame.PPM);

        gameCam.update();

        renderer.setView(gameCam);

    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();

        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);

        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
    @Override
    public boolean keyTyped(char character) {
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        return false;
    }
}
