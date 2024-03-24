package com.mygdx.game.Screens;

import static com.mygdx.game.MyGdxGame.PPM;
import static com.mygdx.game.MyGdxGame.V_HEIGHT;
import static com.mygdx.game.MyGdxGame.V_WIDTH;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;

public class ChooseSkinScreen implements Screen, InputProcessor {
    //recycling view
    private Viewport viewport;
    private Stage stage;
    public Game game;
    private OrthographicCamera gameCam;
    private SpriteBatch batch;

    private Sprite sprite;
    private Sprite buttonUp;
    private Sprite buttonDown;


    private Viewport gamePort;
    private static int amount;

    private static final int SIZE_BUTTONS = 200;

    private Texture upButton;
    private Texture downButton;
    private Texture img;
    public ChooseSkinScreen(Game game)
    {

        this.game = game;
        batch = new SpriteBatch();

        img = new Texture("backWhite.png");
        upButton = new Texture("up.png");
        downButton = new Texture("down.png");


        sprite = new Sprite(img);

        buttonUp = new Sprite(upButton);
        buttonDown = new Sprite(downButton);

        sprite.setPosition(Gdx.graphics.getWidth()/2 - sprite.getWidth() / 2, Gdx.graphics.getHeight() / 2 - sprite.getHeight() / 2);
        buttonUp.setPosition(10,10);
        buttonDown.setPosition(1, 1);

        viewport = new FitViewport(V_WIDTH, V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((MyGdxGame) game).batch);

        Gdx.input.setInputProcessor(this);
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        if (((screenY < viewport.getScreenHeight() / 2 ) && (amount > -40) && Gdx.input.isTouched())) {
            sprite.translate(0, -5f);
            amount--;
        }
        //Спуск вниз
        if  ((screenY > viewport.getScreenHeight() / 2) && (amount < 40) && Gdx.input.isTouched()) {
            sprite.translate(0, 5f);
            amount++;
        }



        return false;
    }
    @Override
    public boolean scrolled(float amountX, float amountY) {
        if (amountY < 0 && amount > -40) {
            sprite.translate(0, -15f);
            //gameCam.position.y = gameCam.position.y + 100;
            //gameCam.position.set(0, 500, 0);
            //gameCam.update();
            amount--;
        }
        if  (amountY > 0 && amount < 40) {
            sprite.translate(0, 15f);
            //gameCam.position.y = gameCam.position.y - 100;
            //gameCam.position.set(0, -500, 0);
            //gameCam.update();
            amount++;
        }
        return false;

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(sprite, sprite.getX(), sprite.getY());
        batch.draw(buttonDown, buttonDown.getX(), buttonDown.getY());
        batch.draw(buttonUp, buttonUp.getX(), buttonUp.getY());
        batch.end();

        //touchDragged(Gdx.input.getX(), Gdx.input.getY(), 0);


    }

    @Override
    public void dispose() {
        img.dispose();
        stage.dispose();
    }
    //useless
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
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
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

}
