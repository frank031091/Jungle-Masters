package com.junglemasters.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.junglemasters.JungleMasters;
import com.junglemasters.sprites.Franfran;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            case JungleMasters.FRAN_HEAD_BIT | JungleMasters.BRICK_BIT:
                if (fixA.getFilterData().categoryBits == JungleMasters.FRAN_HEAD_BIT){
                    ((InteractiveTIleObject)fixB.getUserData()).onHeadHit((Franfran) fixA.getUserData());
                } else {
                    ((InteractiveTIleObject) fixA.getUserData()).onHeadHit((Franfran) fixB.getUserData());
                }
                break;

            case JungleMasters.FRAN_HEAD_BIT | JungleMasters.HIDDEN_BIT:
                if (fixA.getFilterData().categoryBits == JungleMasters.FRAN_HEAD_BIT){
                    ((InteractiveTIleObject)fixB.getUserData()).onHeadHit((Franfran) fixA.getUserData());
                } else {
                    ((InteractiveTIleObject) fixA.getUserData()).onHeadHit((Franfran) fixB.getUserData());
                }
                break;

            case JungleMasters.FIREBALL_BIT | JungleMasters.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == JungleMasters.ENEMY_BIT){
                    ((Enemy)fixA.getUserData()).hitByFruit();
                } else {
                    ((Enemy)fixB.getUserData()).hitByFruit();
                }
            break;
            case JungleMasters.FRAN_BODY_SENSOR_BIT | JungleMasters.SECONDS_BIT:
                if (fixA.getFilterData().categoryBits == JungleMasters.FRAN_BODY_SENSOR_BIT){
                    ((InteractiveTIleObject)fixB.getUserData()).onHeadHit((Franfran) fixA.getUserData());
                } else {
                    ((InteractiveTIleObject) fixA.getUserData()).onHeadHit((Franfran) fixB.getUserData());
                }
                break;
            case JungleMasters.ENEMY_HEAD_BIT | JungleMasters.FRAN_BIT:

                if(fixA.getFilterData().categoryBits == JungleMasters.ENEMY_HEAD_BIT){

                    ((Franfran) fixB.getUserData()).onTopOfEnemy = true;

                    if (((Franfran) fixB.getUserData()).currentState == Franfran.States.ATTACKING_IN_THE_AIR ||
                            ((Franfran) fixB.getUserData()).currentState == Franfran.States.ATTACKING){

                        ((Enemy)fixA.getUserData()).hitOnHead((Franfran) fixB.getUserData());

                    }

                } else{

                    ((Franfran) fixA.getUserData()).onTopOfEnemy= true;

                    if (((Franfran) fixA.getUserData()).currentState == Franfran.States.ATTACKING_IN_THE_AIR ||
                                ((Franfran) fixA.getUserData()).currentState == Franfran.States.ATTACKING){

                            ((Enemy)fixB.getUserData()).hitOnHead((Franfran) fixA.getUserData());

                        }
                    }

                break;

            case JungleMasters.WATER_BIT | JungleMasters.FRAN_BIT:
            case JungleMasters.ENEMY_BIT | JungleMasters.FRAN_BIT:
                if(fixA.getFilterData().categoryBits == JungleMasters.FRAN_BIT){

                    ((Franfran)fixA.getUserData()).isDyingVar = true;
                 //   Gdx.app.log("g","fran fran is dead");
                }

                else{
                    ((Franfran)fixB.getUserData()).isDyingVar = true;
                  //  Gdx.app.log("g","fran fran is dead");

                }
                break;
            case JungleMasters.ENEMY_BIT | JungleMasters.ENEMY_LIMIT:
                case JungleMasters.ENEMY_BIT | JungleMasters.GROUND_BIT:
                    if(fixA.getFilterData().categoryBits == JungleMasters.ENEMY_BIT)
                        ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                    else
                        ((Enemy)fixB.getUserData()).reverseVelocity(true,false);
                    break;

            case JungleMasters.ENEMY_BIT | JungleMasters.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).hitByEnemy((Enemy)fixB.getUserData());
                ((Enemy)fixB.getUserData()).hitByEnemy((Enemy)fixA.getUserData());
                break;

            case JungleMasters.FRAN_BIT | JungleMasters.ENEMY_FRONT_BIT:
            case JungleMasters.FIREBALL_BIT | JungleMasters.ENEMY_FRONT_BIT:
                if(fixA.getFilterData().categoryBits == JungleMasters.ENEMY_FRONT_BIT){

                        if ( !((Enemy) fixA.getUserData()).isTimeToAttack){
                            ((Enemy) fixA.getUserData()).isTimeToAttack = true;
                            ((Enemy) fixA.getUserData()).stateTime = 0;
                        }


                }

                else {

                        if ( !((Enemy) fixB.getUserData()).isTimeToAttack) {

                            ((Enemy) fixB.getUserData()).isTimeToAttack = true;
                            ((Enemy) fixB.getUserData()).stateTime = 0;

                        }

                }

                break;


        }
    }

    @Override
    public void endContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            case JungleMasters.ENEMY_HEAD_BIT | JungleMasters.FRAN_BIT:

                if(fixA.getFilterData().categoryBits == JungleMasters.ENEMY_HEAD_BIT){

                    ((Franfran) fixB.getUserData()).onTopOfEnemy = false;

                } else{

                    ((Franfran) fixA.getUserData()).onTopOfEnemy= false;
                }
                break;

            case JungleMasters.WATER_BIT | JungleMasters.FRAN_BIT:
            case JungleMasters.ENEMY_BIT | JungleMasters.FRAN_BIT:
                if(fixA.getFilterData().categoryBits == JungleMasters.FRAN_BIT){

                    ((Franfran)fixA.getUserData()).isDyingVar = false;
                    //   Gdx.app.log("g","fran fran is dead");
                }

                else{
                    ((Franfran)fixB.getUserData()).isDyingVar = false;
                    //  Gdx.app.log("g","fran fran is dead");

                }
                break;
        }




    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
