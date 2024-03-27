package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Sprites.Hero;
import com.mygdx.game.Sprites.WinObject;

//part 9
public class B2WorldCreator {
    public B2WorldCreator(World world, TiledMap map)//, Hero hero
    {
        BodyDef bDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fDef = new FixtureDef();
        Body body;

        //WinObject winObject;
        //Hero hero = hero;

        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set((rect.getX() + rect.getWidth() / 2) / MyGdxGame.PPM, (rect.getY() + rect.getHeight() / 2) / MyGdxGame.PPM);
            body = world.createBody(bDef);
            shape.setAsBox(rect.getWidth() / 2 / MyGdxGame.PPM, rect.getHeight() / 2 / MyGdxGame.PPM);
            fDef.shape = shape;
            body.createFixture(fDef);
        }
        for (MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) // win object
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set((rect.getX() + rect.getWidth() / 2) / MyGdxGame.PPM, (rect.getY() + rect.getHeight() / 2) / MyGdxGame.PPM);
            body = world.createBody(bDef);
            shape.setAsBox(rect.getWidth() / 2 / MyGdxGame.PPM, rect.getHeight() / 2 / MyGdxGame.PPM);
            fDef.shape = shape;
            body.createFixture(fDef);

            new WinObject(world, map, rect);
//            winObject.coorX = (rect.getX() + rect.getWidth() / 2) / MyGdxGame.PPM;
//            winObject.coorY = (rect.getY() + rect.getHeight() / 2) / MyGdxGame.PPM;
        }
    }
}
