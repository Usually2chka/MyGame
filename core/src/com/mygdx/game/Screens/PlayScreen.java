package com.mygdx.game.Screens;

import static com.mygdx.game.MyGdxGame.PPM;
import static com.mygdx.game.MyGdxGame.V_HEIGHT;
import static com.mygdx.game.MyGdxGame.V_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Sprites.Hero;
import com.mygdx.game.Tools.B2WorldCreator;

import box2dLight.PointLight;
import box2dLight.RayHandler;


public class PlayScreen implements Screen, InputProcessor {

    private MyGdxGame game;
    private TextureAtlas atlas;
    private Hud hud;
    private OrthographicCamera gameCam;
    private Viewport gamePort;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;

    private Hero player;
    private RayHandler rayHandler;
    private PointLight myLight;
    private static int jump = 0;



    public PlayScreen(MyGdxGame game)
    {
        atlas = new TextureAtlas("animation.atlas"); //justHero.atlas

        this.game = game;
        gameCam = new OrthographicCamera();

        gamePort = new FillViewport(MyGdxGame.V_WIDTH / PPM,MyGdxGame.V_HEIGHT / PPM, gameCam);// поле видимости нашего персонажа

        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("TmxPr2.tmx");

        renderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);
        gameCam.position.set(gamePort.getScreenWidth() / 2 , gamePort.getScreenHeight() / 2 , 0);

        world = new World(new Vector2(0, -10), true);

        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(world, map);

        player = new Hero(world, this);

        Gdx.input.setInputProcessor(this);

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.5f);

        myLight = new PointLight(rayHandler, 1000, Color.WHITE, 1.5f, 0, 0);
        myLight.setSoft(true);
        myLight.setSoftnessLength(0f);
        myLight.setStaticLight(false);


        myLight.attachToBody(player.b2body, 0, 0);
    }

    public TextureAtlas getAtlas()
    {
        return atlas;
    }
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

        rayHandler.update();

        world.step(1/60f, 6, 2);

        player.update(dt);

        gameCam.position.y = player.b2body.getPosition().y + (75 / PPM);
        gameCam.position.x = player.b2body.getPosition().x + (100 / PPM);

        gameCam.update();

        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        update(delta);
        //Gdx.gl.glClearColor(0,0,0, 0);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        rayHandler.updateAndRender();
        b2dr.render(world, gameCam.combined);

        game.batch.begin(); //начало------------------------------------------

        game.batch.setProjectionMatrix(gameCam.combined);
        player.draw(game.batch);
        rayHandler.setCombinedMatrix(gameCam.combined, 0, 0, V_WIDTH, V_HEIGHT);

        game.batch.end(); //конец-------------------------------------------
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
        rayHandler.dispose();
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
