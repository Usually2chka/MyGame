package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.InteractiveTileObject;
import com.mygdx.game.Sprites.WinObject;

public class WorldContactListener implements ContactListener {
    private PlayScreen screen;
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA.getUserData() == "hero" || fixB.getUserData() == "hero") {
            Fixture head = fixA.getUserData() == "hero" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass()))
            {
                ((InteractiveTileObject) object.getUserData()).onHit();
                //((WinObject) object.getUserData()).onHit();
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        //screen.nextLvl();
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
