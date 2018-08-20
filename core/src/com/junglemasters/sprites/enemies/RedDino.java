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

public class RedDino extends Enemy {

    private Animation walkAnimation, deadAnimation, attackingAnimation;
    private boolean setToDestroy;

    private State previousState;


    public RedDino(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 11; i++)
            frames.add(new TextureRegion(screen.getAtlasForEnemies().findRegion("walking_red_dino"), i * 200, 0, 200, 150));
        walkAnimation = new Animation(0.125f, frames);
        frames.clear();

        for(int i = 0; i < 7; i++)
            frames.add(new TextureRegion(screen.getAtlasForEnemies().findRegion("attack_red_dino"), i * 200, 0, 200, 150));
        attackingAnimation = new Animation(0.2f, frames);
        frames.clear();

        for(int i = 0; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlasForEnemies().findRegion("dead_red_dino"), i * 200, 0, 200, 150));
        deadAnimation = new Animation(0.2f, frames);
        frames.clear();


        stateTime = 0;
        setToDestroy = false;
        destroyed = false;
        isRunningRIgth = true;
        isTimeToAttack = false;


        setBounds(getX(),getY(),200/ JungleMasters.PPM*2,150/JungleMasters.PPM*2);
        velocity = new Vector2(2,0);



    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(40/JungleMasters.PPM,55 / JungleMasters.PPM);
        fdef.filter.categoryBits = JungleMasters.ENEMY_BIT;
        fdef.filter.maskBits = JungleMasters.GROUND_BIT |
                JungleMasters.BRICK_BIT |
                JungleMasters.WATER_BIT|
                JungleMasters.ENEMY_BIT |
                JungleMasters.FIREBALL_BIT|
                JungleMasters.ENEMY_LIMIT|
                JungleMasters.FRAN_BIT;
        fdef.shape = shape;
        fdef.restitution = 0.5f;
        b2body.createFixture(fdef).setUserData(this);


        //sensor to collide with fran fran
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(40/JungleMasters.PPM,60/JungleMasters.PPM),new Vector2(-40/JungleMasters.PPM,60/JungleMasters.PPM));
        fdef.filter.categoryBits = JungleMasters.ENEMY_HEAD_BIT;
        fdef.filter.maskBits = JungleMasters.FRAN_BIT ;
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

        //sensor to check if fran fran is on the back
        EdgeShape back = new EdgeShape();
        back.set(new Vector2(-300/JungleMasters.PPM,50/JungleMasters.PPM),new Vector2(-40/JungleMasters.PPM,50/JungleMasters.PPM));
        fdef.filter.categoryBits = JungleMasters.ENEMY_FRONT_BIT;
        fdef.filter.maskBits = JungleMasters.FRAN_BIT ;
        fdef.shape = back;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);


        //sensor to check if fran fran is on the front
        EdgeShape front = new EdgeShape();
        front.set(new Vector2(300/JungleMasters.PPM,50/JungleMasters.PPM),new Vector2(40/JungleMasters.PPM,50/JungleMasters.PPM));
        fdef.filter.categoryBits = JungleMasters.ENEMY_FRONT_BIT;
        fdef.filter.maskBits = JungleMasters.FRAN_BIT ;
        fdef.shape = front;
        fdef.isSensor = true;
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

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2+55/JungleMasters.PPM);
        State currentState = getState();

        //If it´s been more than 5 seconds in the attacking state then change the velocity of the sprite back to one
        // and set is time to attack as false so it can change the state to walking
        if (isTimeToAttack && stateTime > 5) {
            velocityOfSprite = 1;
            isTimeToAttack = false;
        }

        switch (currentState){

            case DEAD:
                if (!destroyed){
                    Gdx.app.log("killing", "killing body");
                    world.destroyBody(b2body);
                    screen.hud.addScore(5);
                    destroyed = true;
                    setRegion(getFrame(dt, currentState));
                }
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


    }

    public State getState(){

        //Check if it is not dead and if it is not time to attack, if it is not then run the walk animation
        if(!destroyed && !setToDestroy && !isTimeToAttack && !deadByFruitVar) {
            velocityOfSprite = 1;
            return State.WALKING;
        }
        //If it is not dead and if it is time to attack then run the attacking animation
        else if (isTimeToAttack && !deadByFruitVar){
            velocityOfSprite = 2;
            return State.ATTACKING;
        }
        //Check if it is dead
        else {
            return State.DEAD;
        }
    }


    public TextureRegion getFrame(float dt, State currentState){

        TextureRegion region = null;
        switch(currentState){

            case WALKING:
                region = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);
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
            b2body.applyLinearImpulse(new Vector2(0.6f,0),b2body.getWorldCenter(),true);
        else
            b2body.applyLinearImpulse(new Vector2(-0.6f,0),b2body.getWorldCenter(),true);
    }
}
