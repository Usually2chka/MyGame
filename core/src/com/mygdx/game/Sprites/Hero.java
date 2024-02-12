package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;

public class Hero extends Sprite {
    public World world;
    public Body b2body;

    public Hero(World world)
    {
        this.world = world;
        defineHero();
    }
    public void defineHero()
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(64 / MyGdxGame.PPM, 64 / MyGdxGame.PPM); // позиция героя на сцене
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / MyGdxGame.PPM);

        fixtureDef.shape = shape;
        b2body.createFixture(fixtureDef);
    }
}
