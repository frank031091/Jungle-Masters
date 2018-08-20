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

public class Caveman extends Enemy {

    private boolean alreadyRedefinedDeadBody;

    public enum State {WALKING,RUNNING, JUMPING_ATTACKING, JUMPING,IDLE,DEAD,  ATTACKING }
    boolean timeToJump = false;
    private Animation walkAnimation,runAnimation,jumpAttackAnimation,jumpAnimation, idleAnimation,
            deadAnimation, attackingAnimation ;

    private boolean setToDestroy;
    private State currentState;
    private State previousState;
    public boolean timeToWakeUp;

    public Caveman(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        currentState = State.IDLE;



        Array<TextureRegion> frames = new Array<TextureRegion>();


        for(int i = 0; i < 11; i++)
            frames.add(new TextureRegion(screen.getAtlasForEnemies().findRegion("walking_caveman_yellow"), i * 120, 0, 120, 120));
        walkAnimation = new Animation(0.2f, frames);
        frames.clear();

        for(int i = 0; i < 11; i++)
            frames.add(new TextureRegion(screen.getAtlasForEnemies().findRegion("run_caveman_yellow"), i * 120, 0, 120, 120));
        runAnimation = new Animation(0.2f, frames);
        frames.clear();

        for(int i = 0; i < 5; i++)
            frames.add(new TextureRegion(screen.getAtlasForEnemies().findRegion("jump_attack_caveman_yellow"), i * 120, 0, 120, 120));
        jumpAttackAnimation = new Animation(0.2f, frames);
        frames.clear();

        for(int i = 0; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlasForEnemies().findRegion("jump_caveman_yellow"), i * 120, 0, 120, 120));
        jumpAnimation = new Animation(0.2f, frames);
        frames.clear();

        for(int i = 0; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlasForEnemies().findRegion("idle_caveman_yellow"), i * 120, 0, 120, 120));
        idleAnimation = new Animation(0.2f, frames);
        frames.clear();

        for(int i = 0; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlasForEnemies().findRegion("dead_caveman_yellow"), i * 120, 0, 120, 120));
        deadAnimation = new Animation(0.2f, frames);
        frames.clear();

        for(int i = 0; i < 6; i++)
            frames.add(new TextureRegion(screen.getAtlasForEnemies().findRegion("attack_caveman_yellow"), i * 120, 0, 120, 120));
        attackingAnimation = new Animation(0.2f, frames);
        frames.clear();

        stateTime = 0;
        setToDestroy = false;
        destroyed = false;
        isRunningRIgth = true;
        isTimeToAttack = false;


        setBounds(getX(),getY(),120/ JungleMasters.PPM*3,120/JungleMasters.PPM*3);
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
        shape.setAsBox(90/ JungleMasters.PPM,90 / JungleMasters.PPM);
        fdef.filter.categoryBits = JungleMasters.ENEMY_BIT;
        fdef.filter.maskBits = JungleMasters.GROUND_BIT |
                JungleMasters.WATER_BIT|
                JungleMasters.ENEMY_LIMIT|
                JungleMasters.FIREBALL_BIT|
                JungleMasters.FRAN_BIT;
        fdef.shape = shape;
        fdef.restitution = 0.1f;
        b2body.createFixture(fdef).setUserData(this);


        //sensor to collide with fran fran in the head, so this enemy can die
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(90/JungleMasters.PPM,95/JungleMasters.PPM),new Vector2(-90/JungleMasters.PPM,95/JungleMasters.PPM));
        fdef.filter.categoryBits = JungleMasters.ENEMY_HEAD_BIT;
        fdef.filter.maskBits = JungleMasters.FRAN_BIT ;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);

    }


    protected void reDefineEnemy() {

        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);
        BodyDef bdefinition = new BodyDef();
        bdefinition.position.set(position);
        bdefinition.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdefinition);
        b2body.setLinearDamping(400f);
        FixtureDef fdef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(90/ JungleMasters.PPM,90 / JungleMasters.PPM);
        fdef.filter.categoryBits = JungleMasters.ENEMY_BIT;
        fdef.filter.maskBits = JungleMasters.GROUND_BIT |
                JungleMasters.ENEMY_LIMIT|
                JungleMasters.FIREBALL_BIT|
                JungleMasters.WATER_BIT|
                JungleMasters.FRAN_BIT;
        fdef.shape = shape;
        fdef.restitution = 0.1f;
        b2body.createFixture(fdef).setUserData(this);


        //sensor to collide with fran fran in the head, so this enemy can die
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(90/JungleMasters.PPM,95/JungleMasters.PPM),new Vector2(-90/JungleMasters.PPM,95/JungleMasters.PPM));
        fdef.filter.categoryBits = JungleMasters.ENEMY_HEAD_BIT;
        fdef.filter.maskBits = JungleMasters.FRAN_BIT ;
        fdef.shape = head;
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
        b2body.setLinearDamping(400f);
        FixtureDef fdef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(90/ JungleMasters.PPM,20 / JungleMasters.PPM);
        fdef.filter.categoryBits = JungleMasters.DEAD_BIT;
        fdef.filter.maskBits = JungleMasters.GROUND_BIT |
                
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
        if (isTimeToAttack && stateTime > 5) {
            velocityOfSprite = 1;
            isTimeToAttack = false;
        }


        switch (currentState){

            case WALKING:
                b2body.setLinearVelocity(velocity);
                velocityOfSprite = 1;
                setRegion(getFrame(dt, currentState));
                break;

            case RUNNING:
                applyLinearImpulse();
                velocityOfSprite = 2;
                setRegion(getFrame(dt, currentState));
                break;

            case JUMPING_ATTACKING:
                setRegion(getFrame(dt, currentState));
                break;

            case JUMPING:
                setRegion(getFrame(dt, currentState));
                break;

            case IDLE:
                setRegion(getFrame(dt,currentState));
                break;

            case DEAD:
                if (!destroyed){
                    timeToWakeUp = true;
                    setRegion(getFrame(dt, currentState));
                }
                break;

            case ATTACKING:
                setRegion(getFrame(dt, currentState));
                break;



        }

        if (!alreadyRedefinedDeadBody && timeToWakeUp && timesDead <=2 || deadByFruitVar){
            //define its dead body
            defineDeadBody();
            screen.hud.addScore(10);
            currentState = State.DEAD;
            deadByFruitVar = false;
            alreadyRedefinedDeadBody = true;
            //if it´s been dead more than twice and is set to destroy then destroy the body
        }else if (timesDead > 2 && !destroyed){

            world.destroyBody(b2body);
            destroyed = true;
            timeToWakeUp = false;
            currentState = State.DEAD;
            screen.hud.addScore(20);


        }

        //if it´s been dead more than 5 seconds and the body is not destroyed yet and it is time to recreate the plant then bring it
        //back to live
        if (stateTime > 7 && !destroyed && timeToWakeUp){
            currentState = State.WALKING;
            reDefineEnemy();

            alreadyRedefinedDeadBody = false;
            setToDestroy = false;
            timeToWakeUp = false;
            deadByFruitVar = false;
        }




    }

    public void getState(){

        if ((setToDestroy && !destroyed) || deadByFruitVar){
            currentState = State.DEAD;
        } else if (!destroyed && currentState != State.DEAD)
            if (screen.getFranfranPlayer().getStateToCaveman() == State.JUMPING_ATTACKING){
                    if (b2body.getLinearVelocity().y > 0){
                        currentState = State.JUMPING_ATTACKING;
                         }

            } else {
                currentState = screen.getFranfranPlayer().getStateToCaveman();
            }


    }


    public TextureRegion getFrame(float dt, State currentState){

        TextureRegion region = null;
        switch(currentState){

            case WALKING:
                region = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);
                break;
            case RUNNING:
                region = (TextureRegion) runAnimation.getKeyFrame(stateTime, true);
                break;
            case JUMPING_ATTACKING:
                region = (TextureRegion) jumpAttackAnimation.getKeyFrame(stateTime, true);
                break;

            case JUMPING:
                region = (TextureRegion) jumpAnimation.getKeyFrame(stateTime, false);
                break;

            case IDLE:
                region = (TextureRegion) idleAnimation.getKeyFrame(stateTime, true);
                break;

            case DEAD:
                region = (TextureRegion) deadAnimation.getKeyFrame(stateTime, false);
                break;

            case ATTACKING:
                region = (TextureRegion) attackingAnimation.getKeyFrame(stateTime, true);
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

        //  JungleMasters.manager.get("audio/sounds/stomp.wav", Sound.class).play();
    }

    @Override
    public void hitByEnemy(Enemy enemy) {
        //  if(enemy instanceof Turtle && ((Turtle) enemy).currentState == Turtle.State.MOVING_SHELL)
//            setToDestroy = true;
//        else
        reverseVelocity(true, false);
    }


    public void applyLinearImpulse(){
        if (isRunningRIgth )
            b2body.applyLinearImpulse(new Vector2(0.2f,0),b2body.getWorldCenter(),true);
        else
            b2body.applyLinearImpulse(new Vector2(-0.2f,0),b2body.getWorldCenter(),true);
    }

}
