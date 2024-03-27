package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;

public class WinObject extends InteractiveTileObject{
    private short ONTOUCHED;



    private PlayScreen screen;


    public WinObject(World world, TiledMap map, Rectangle bounds)
    {
        super(world, map, bounds);
        fixture.setUserData(this);
        setCategoryFilter(MyGdxGame.WINOBJECT_BIT);


    }
    @Override
    public void onHit() {
        Gdx.app.log("", "Collision");
        setCategoryFilter(MyGdxGame.ONTOUCHED_BIT);
        ONTOUCHED = MyGdxGame.ONTOUCHED_BIT;
        if (ONTOUCHED == MyGdxGame.ONTOUCHED_BIT)
            screen.nextLvl();

    }
}
