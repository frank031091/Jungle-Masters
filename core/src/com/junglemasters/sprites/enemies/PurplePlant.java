package com.junglemasters.sprites.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.junglemasters.JungleMasters;
import com.junglemasters.Screens.Hud;
import com.junglemasters.Screens.PlayScreen;
import com.junglemasters.sprites.Franfran;
import com.junglemasters.tools.Enemy;


public class PurplePlant extends Enemy {

    private Animation idleAnimation, deadAnimation, attackingAnimation;

    private State currentState;
    private State previousState;
    public boolean shouldWeBringItBack;
    private boolean alreadyRedefinedDeadBody;


    public PurplePlant(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        currentState = State.WALKING;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 3; i++)
            frames.add(new TextureRegion(screen.getAtlasForEnemies().findRegion("idle_purple_plant"), i * 180, 0, 180, 99));
        idleAnimation = new Animation(0.1f, frames);
        frames.clear();

        for(int i = 0; i < 7; i++)
            frames.add(new TextureRegion(screen.getAtlasForEnemies().findRegion("attack_purple_plant"), i * 180, 0, 180, 99));
        attackingAnimation = new Animation(0.1f, frames);
        frames.clear();

        for(int i = 0; i < 3; i++)
            frames.add(new TextureRegion(screen.getAtlasForEnemies().findRegion("dead_purple_plant"), i * 180, 0, 180, 99));
        deadAnimation = new Animation(0.2f, frames);
        frames.clear();


        stateTime = 0;
        setToDestroy = false;
        destroyed = false;
        isRunningRIgth = true;
        isTimeToAttack = false;

        timesDead =0;

        setBounds(getX(),getY(),180/ JungleMasters.PPM*3,99/JungleMasters.PPM*3);
        velocity = new Vector2(0,-4f);

    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
    timesDead = 0;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(90/ JungleMasters.PPM,90 / JungleMasters.PPM);
        fdef.filter.categoryBits = JungleMasters.ENEMY_BIT;
        fdef.filter.maskBits = JungleMasters.GROUND_BIT |
                JungleMasters.BRICK_BIT |
                JungleMasters.WATER_BIT|
                JungleMasters.FIREBALL_BIT|
                JungleMasters.ENEMY_LIMIT|
                JungleMasters.FRAN_BIT;
        fdef.shape = shape;
        fdef.restitution = 0.5f;
        b2body.createFixture(fdef).setUserData(this);


        //sensor to collide with fran fran in the head, so this enemy can die
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(90/JungleMasters.PPM,95/JungleMasters.PPM),new Vector2(-90/JungleMasters.PPM,95/JungleMasters.PPM));
        fdef.filter.categoryBits = JungleMasters.ENEMY_HEAD_BIT;
        fdef.filter.maskBits = JungleMasters.FRAN_BIT ;
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);


        //sensor to check if fran fran is on the back
        EdgeShape back = new EdgeShape();
        back.set(new Vector2(-400/JungleMasters.PPM,50/JungleMasters.PPM),new Vector2(-90/JungleMasters.PPM,50/JungleMasters.PPM));
        fdef.filter.categoryBits = JungleMasters.ENEMY_FRONT_BIT;
        fdef.filter.maskBits = JungleMasters.FRAN_BIT |
                JungleMasters.FIREBALL_BIT;
        fdef.shape = back;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);



        //sensor to check if fran fran is on the front
        EdgeShape front = new EdgeShape();
        front.set(new Vector2(400/JungleMasters.PPM,50/JungleMasters.PPM),new Vector2(90/JungleMasters.PPM,50/JungleMasters.PPM));
        fdef.filter.categoryBits = JungleMasters.ENEMY_FRONT_BIT;
        fdef.filter.maskBits = JungleMasters.FRAN_BIT |
                JungleMasters.FIREBALL_BIT;
        fdef.shape = front;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);


    }

    protected void reDefinePlant() {

        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);
        BodyDef bdefinition = new BodyDef();
        bdefinition.position.set(position);
        bdefinition.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdefinition);

        FixtureDef fdef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(90/ JungleMasters.PPM,90 / JungleMasters.PPM);
        fdef.filter.categoryBits = JungleMasters.ENEMY_BIT;
        fdef.filter.maskBits = JungleMasters.GROUND_BIT |
                JungleMasters.WATER_BIT|
                JungleMasters.FIREBALL_BIT|
                JungleMasters.BRICK_BIT |
                JungleMasters.ENEMY_LIMIT|
                JungleMasters.FRAN_BIT;
        fdef.shape = shape;
        fdef.restitution = 0.5f;
        b2body.createFixture(fdef).setUserData(this);


        //sensor to collide with fran fran in the head, so this enemy can die
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(90/JungleMasters.PPM,95/JungleMasters.PPM),new Vector2(-90/JungleMasters.PPM,95/JungleMasters.PPM));
        fdef.filter.categoryBits = JungleMasters.ENEMY_HEAD_BIT;
        fdef.filter.maskBits = JungleMasters.FRAN_BIT ;
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);


        //sensor to check if fran fran is on the back
        EdgeShape back = new EdgeShape();
        back.set(new Vector2(-400/JungleMasters.PPM,0/JungleMasters.PPM),new Vector2(0/JungleMasters.PPM,0/JungleMasters.PPM));
        fdef.filter.categoryBits = JungleMasters.ENEMY_FRONT_BIT;
        fdef.filter.maskBits = JungleMasters.FRAN_BIT |
                JungleMasters.FIREBALL_BIT;
        fdef.shape = back;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);



        //sensor to check if fran fran is on the front
        EdgeShape front = new EdgeShape();
        front.set(new Vector2(400/JungleMasters.PPM,0/JungleMasters.PPM),new Vector2(0/JungleMasters.PPM,0/JungleMasters.PPM));
        fdef.filter.categoryBits = JungleMasters.ENEMY_FRONT_BIT;
        fdef.filter.maskBits = JungleMasters.FRAN_BIT |
                JungleMasters.FIREBALL_BIT;
        fdef.shape = front;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);


    }

    public void defineDeadBody(){

        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);
        BodyDef bdefinition = new BodyDef();
        bdefinition.position.set(position);
        bdefinition.type = BodyDef.BodyType.StaticBody;

        b2body = world.createBody(bdefinition);

        FixtureDef fdef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(90/ JungleMasters.PPM,20 / JungleMasters.PPM);
        fdef.filter.categoryBits = JungleMasters.DEAD_BIT;
        fdef.filter.maskBits = JungleMasters.GROUND_BIT |
                JungleMasters.ENEMY_BIT |
                JungleMasters.FRAN_BIT;
        fdef.shape = shape;
        fdef.restitution = 0.1f;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch){
        //it will draw the sprite only if it is not destroyed and if it has been dead for at least 3 seconds
         if(!destroyed || stateTime < 3)
            super.draw(batch);
    }

    @Override
    public void update(float dt) {
        stateTime += dt;

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2+30/JungleMasters.PPM);

        getState();

        //If it´s been more than 5 seconds in the attacking state then change the velocity of the sprite back to one
        // and set is time to attack as false so it can change the state to walking
        if (isTimeToAttack && stateTime > 2) {
            velocityOfSprite = 1;
            isTimeToAttack = false;
        }

        switch (currentState){

            case DEAD:
                    shouldWeBringItBack = true;
                    setRegion(getFrame(dt, currentState));
                break;
            case ATTACKING:
                applyImpulse();
                setRegion(getFrame(dt, currentState));
                break;
            case WALKING:

                b2body.setLinearVelocity(velocity);
                setRegion(getFrame(dt, currentState));
                break;

        }

        //If it is the moment to recreate the plant and it hass been dead less than twice then


        if (!alreadyRedefinedDeadBody && shouldWeBringItBack && timesDead <=2 || deadByFruitVar){
            //define its dead body
            defineDeadBody();
            screen.hud.addScore(10);
            deadByFruitVar = false;
            alreadyRedefinedDeadBody = true;

            //if it´s been dead more than twice and is set to destroy then destroy the body
        }else if (timesDead > 2 && !destroyed){


            world.destroyBody(b2body);
            screen.hud.addScore(20);
            destroyed = true;
            shouldWeBringItBack = false;


        }

        //if it´s been dead more than 5 seconds and the body is not destroyed yet and it is time to recreate the plant then bring it
        //back to live
        if (stateTime > 7 && !destroyed && shouldWeBringItBack){
            currentState = State.WALKING;
            reDefinePlant();

            alreadyRedefinedDeadBody = false;
            setToDestroy = false;
            shouldWeBringItBack = false;
            deadByFruitVar = false;
        }





    }

    public void getState(){

        //Check if it is not dead and if it is not time to attack, if it is not then run the walk animation
        if(!destroyed && !setToDestroy && !isTimeToAttack) {
            velocityOfSprite = 1;
            currentState = State.WALKING;

        }
        //If it is not dead and if it is time to attack then run the attacking animation
        else if (isTimeToAttack && currentState != State.DEAD){
            velocityOfSprite = 2;
            currentState = State.ATTACKING;
        }
        //Check if it is dead
        else {
            currentState = State.DEAD;
        }

    }




    public TextureRegion getFrame(float dt, State currentState){

        TextureRegion region = null;
        switch(currentState){

            case WALKING:
                region = (TextureRegion) idleAnimation.getKeyFrame(stateTime, true);
                break;
            case ATTACKING:
                region = (TextureRegion) attackingAnimation.getKeyFrame(stateTime, true);
                break;
            case DEAD:
                region = (TextureRegion) deadAnimation.getKeyFrame(stateTime, false);
                break;
        }

        //if FranFran is running left and the texture isnt facing left... flip it.
        if ((b2body.getLinearVelocity().x < 0 || !isRunningRIgth) && !region.isFlipX()) {
            region.flip(true, false);
            isRunningRIgth = false;
        }

        //if FranFran is running right and the texture isnt facing right... flip it.
        else if ((b2body.getLinearVelocity().x > 0 || isRunningRIgth) && region.isFlipX()) {
            region.flip(true, false);
            isRunningRIgth = true;
        }

        //if the current state is the same as the previous state increase the state timer.
        //otherwise the state has changed and we need to reset timer.
        stateTime = currentState == previousState ? stateTime + dt*velocityOfSprite : 0;
        //update previous state
        previousState = currentState;
        //return our final adjusted frame
        return region;

    }

    @Override
    public void hitOnHead(Franfran franfran) {
        setToDestroy = true;
        timesDead = timesDead+1;
        Gdx.app.log("here", timesDead + " ");
        //  JungleMasters.manager.get("audio/sounds/stomp.wav", Sound.class).play();
    }

    @Override
    public void hitByEnemy(Enemy enemy) {
        //  if(enemy instanceof Turtle && ((Turtle) enemy).currentState == Turtle.State.MOVING_SHELL)
//            setToDestroy = true;
//        else
        reverseVelocity(true, false);
    }


    public void applyImpulse(){
        if (isRunningRIgth)
            b2body.applyLinearImpulse(new Vector2(0.6f,0.4f),b2body.getWorldCenter(),true);
        else
            b2body.applyLinearImpulse(new Vector2(-0.6f,0.4f),b2body.getWorldCenter(),true);
    }

}
