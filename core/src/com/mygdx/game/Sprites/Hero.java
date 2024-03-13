package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;

public class Hero extends Sprite {
    public enum State { FALLING, JUMPING, STANDING, RUNNING }
    public State currentState;
    public State previousState;

    public Animation heroRun;
    public Animation heroJump;
    public Animation heroStand;

    private float stateTimer;
    private boolean runningRight;

    public World world;
    public Body b2body;
    private TextureRegion heroJustStand;


    public Hero(World world, PlayScreen screen)
    {
        super(screen.getAtlas().findRegion("run"));//
        this.world = world;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<>();
        //RUN
        for(int i = 0; i < 11; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("run"), i * 128, 0, 128, 128));
        heroRun = new Animation(0.1f, frames);
        frames.clear();
        //AFK
        for(int i = 0; i < 11; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("afk"), i * 128, 0, 128, 128 ));
        heroStand = new Animation(0.1f, frames);
        frames.clear();
        //JUMP
        for(int i = 0; i < 16; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("jump2"), i * 128, 0, 128, 128 ));
        //frames.add(new TextureRegion(screen.getAtlas().findRegion("jump"), i * 64, 0, 64, 64 ));
        heroJump = new Animation(0.1f, frames);
        frames.clear();


        heroJustStand = new TextureRegion(getTexture(), 0, 0, 146, 380); // 146x380
        defineHero();
        setBounds(0, 0, 64 / MyGdxGame.PPM, 32 / MyGdxGame.PPM); // натягивания текстуры на модельку
        setRegion(heroJustStand);
    }

    public void update(float dt)
    {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2); // позиция текстурки
        setRegion(getFrame(dt));
    }
    public TextureRegion getFrame(float dt)
    {
        currentState = getState();
        TextureRegion region;

        switch (currentState)
        {
            case JUMPING:
                region = (TextureRegion) heroJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = (TextureRegion) heroRun.getKeyFrame(stateTimer, true);
                break;
            case STANDING:
                region = (TextureRegion) heroStand.getKeyFrame(stateTimer, true);
                break;
            default:
                region = heroJustStand;
                break;
        }
        //Вот это все отвечает за то, в какую сторону бежит бедолага
        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX())
        {
            region.flip(true, false);
            runningRight = false;
        }
        else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX())
        {
            region.flip(true, false);
            runningRight = true;
        }
        //Переключение анимации
        stateTimer = currentState == previousState ? stateTimer +dt : 0;
        previousState = currentState;
        return region;
    }



    public State getState()
    {
        if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }
    public void defineHero()
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(64 / MyGdxGame.PPM, 64 / MyGdxGame.PPM); // позиция героя на сцене
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();

        CircleShape shape = new CircleShape();
        shape.setRadius(15 / MyGdxGame.PPM); //6 or 5.5
        fixtureDef.shape = shape;
        b2body.createFixture(fixtureDef);
    }
}
