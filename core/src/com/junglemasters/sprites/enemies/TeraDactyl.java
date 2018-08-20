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

public class TeraDactyl extends Enemy {

    private Animation walkAnimation, deadAnimation;

    private boolean setToDestroy;
    private State previousState;
    private State currentState;


    public TeraDactyl(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 3; i++)
            frames.add(new TextureRegion(screen.getAtlasForEnemies().findRegion("flying_brown_dino"), i * 128, 0, 128, 99));
        walkAnimation = new Animation(0.25f, frames);
        frames.clear();

        for(int i = 0; i < 1; i++)
            frames.add(new TextureRegion(screen.getAtlasForEnemies().findRegion("dead_brown_dino"), i * 128, 0, 128, 99));
        deadAnimation = new Animation(0.2f, frames);
        frames.clear();


        stateTime = 0;
        setToDestroy = false;
        destroyed = false;
        isRunningRIgth = true;
        isTimeToAttack = false;


        currentState = State.WALKING;
        setBounds(getX(),getY(),128/ JungleMasters.PPM*3,99/JungleMasters.PPM*3);
        velocity = new Vector2(2,1);

    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.friction = 0.4f;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(140/JungleMasters.PPM,50 / JungleMasters.PPM);
        fdef.filter.categoryBits = JungleMasters.ENEMY_BIT;
        fdef.filter.maskBits =JungleMasters.GROUND_BIT |
                JungleMasters.WATER_BIT|
                JungleMasters.FIREBALL_BIT|
                JungleMasters.ENEMY_LIMIT|
                JungleMasters.FRAN_BIT;
        fdef.shape = shape;
        fdef.restitution = 0.5f;
        b2body.createFixture(fdef).setUserData(this);


        //sensor to collide with fran fran in the head, so this enemy can die
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(140/JungleMasters.PPM,55/JungleMasters.PPM),new Vector2(-140/JungleMasters.PPM,55/JungleMasters.PPM));
        fdef.filter.categoryBits = JungleMasters.ENEMY_HEAD_BIT;
        fdef.filter.maskBits = JungleMasters.FRAN_BIT ;
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);


        //sensor to check if fran fran is on the back
        EdgeShape back = new EdgeShape();
        back.set(new Vector2(-300/JungleMasters.PPM,-400/JungleMasters.PPM),new Vector2(-300/JungleMasters.PPM,-600/JungleMasters.PPM));
        fdef.filter.categoryBits = JungleMasters.ENEMY_FRONT_BIT;
        fdef.filter.maskBits = JungleMasters.FRAN_BIT ;
        fdef.shape = back;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);



        //sensor to check if fran fran is on the front
        EdgeShape front = new EdgeShape();
        front.set(new Vector2(300/JungleMasters.PPM,-400/JungleMasters.PPM),new Vector2(300/JungleMasters.PPM,-600/JungleMasters.PPM));
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

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2+30/JungleMasters.PPM);

         getState();
        //If it´s been more than 5 seconds in the attacking state then change the velocity of the sprite back to one
        // and set is time to attack as false so it can change the state to walking
        if (isTimeToAttack && stateTime > 5) {
            velocityOfSprite = 1;
            isTimeToAttack = false;
        }

        switch (currentState){

            case DEAD:
                if (!destroyed){

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

    public void getState(){

        //Check if it is not dead and if it is not time to attack, if it is not then run the walk animation
        if(!destroyed && !setToDestroy && !isTimeToAttack) {
            velocityOfSprite = 1;
            currentState = State.WALKING;
        }
        //If it is not dead and if it is time to attack then run the attacking animation
        else if (isTimeToAttack){
            velocityOfSprite = 2;
            currentState = State.ATTACKING;
        }
        //Check if it is dead
        else if ((setToDestroy && !destroyed)){
            currentState = State.DEAD;
        }
    }

    public TextureRegion getFrame(float dt, State currentState){

        TextureRegion region = null;
        switch(currentState){

            case WALKING:
            case ATTACKING:
                region = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);
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
            b2body.applyLinearImpulse(new Vector2(0.3f,-0.2f),b2body.getWorldCenter(),true);
        else
            b2body.applyLinearImpulse(new Vector2(-0.3f,-0.2f),b2body.getWorldCenter(),true);
    }
}
