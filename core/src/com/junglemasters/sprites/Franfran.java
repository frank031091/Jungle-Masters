package com.junglemasters.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.junglemasters.JungleMasters;
import com.junglemasters.Screens.Hud;
import com.junglemasters.Screens.PlayScreen;
import com.junglemasters.objects.FireBall;
import com.junglemasters.sprites.enemies.Caveman;


import java.util.HashMap;
import java.util.Map;



public class Franfran extends Sprite {

    private Array<FireBall> fireballs;
    World world;
    public Body b2body;
    public PlayScreen screen;
    public int speedOfAnimation;
    public boolean win, letsCall;
    public boolean isDyingVar;
    public boolean franFranIsDead;
    public boolean isDestroyed;

    public float getStateTimer() {
        return stateTimer;
    }

    public enum States {IDLE,ATTACKING,ATTACKING_IN_THE_AIR,DEAD,HURT,JUMP_LOOP,JUMP_START,RUN_ATTACKING,RUN_THORWING,
    RUNNING,SLIDING,THROWING,THROWING_IN_THE_AIR, CELEBRATING};

    public States previousState;
    public States currentState;
    public float stateTimer;
    public boolean isRunningRIgth ;

    Array<TextureRegion> frames = new Array<TextureRegion>();
    public Map animationsAndStrings = new HashMap<String, Animation>();
    public Map regionsAndwidth = new HashMap<String, Float>();
    public Map regionsAndHeight = new HashMap<String, Float>();

    //Variables used to get the center of our sprite
    public float franWidth;
    public float franHeight;

    //Variables used to get the center of the b2dBody
    public float bodyX;
    public float bodyY;

    public boolean onTopOfEnemy;


    public Franfran(PlayScreen playscreen){

        this.screen = playscreen;
        isDyingVar = false;
        franFranIsDead = false;
        isDestroyed = false;
        previousState = currentState = States.IDLE;
        isRunningRIgth = true;
        stateTimer = 0;
        //Initialize all animations of our main character
       initializeAnimations();
        this.world = playscreen.getWorld();
        defineFranfran();
        fireballs = new Array<FireBall>();


    }

    private void initializeAnimations() {

        //Cycles through all the regions in our texture atlas
        for (TextureAtlas.AtlasRegion region: screen.getAtlasForHeroe().getRegions()){
            //adds the single images to our frames array by cycling through all the region using this region width and height
            for (int i = 0; i <= 7; i++)
                frames.add(new TextureRegion(region,i*region.getRegionWidth()/8,0,region.getRegionWidth()/8,region.getRegionHeight()));

            //Add the name of the region and its corresponding animation
            animationsAndStrings.put(region.name,new Animation(0.1f,frames));
            regionsAndwidth.put(region.name,region.getRegionWidth()/8);
            regionsAndHeight.put(region.name,region.getRegionHeight());
            //Clear the frames variable so we can go with the next region
            frames.clear();
        }



    }




    public void update(float dt){

        isDying();
        hasfranFranWon();
        setRegion(getFrame(dt));

        //We use setBounds to set the position and size of our sprite
        setBounds(bodyX,bodyY,
                franWidth *2,
                franHeight *2);

        for (FireBall ball : fireballs) {
            ball.update(dt);
            if (!ball.isDestroyed())
            if (ball.isDestroyed())
                fireballs.removeValue(ball, true);
        }

        if (franFranIsDead){
            //To prevent bugs avoid destroying the main character body
           // world.destroyBody(b2body);
        }




    }

    public TextureRegion getFrame(float dt){

         getState();
        TextureRegion region = null;

        switch (currentState){
            case IDLE:
                region = (TextureRegion) ((Animation)animationsAndStrings.get("idle")).getKeyFrame(stateTimer,true);
                Gdx.app.log("animation","setting idle body");
               setBoundsValues("idle");
                break;
            case ATTACKING:
                region = (TextureRegion) ((Animation)animationsAndStrings.get("attacking")).getKeyFrame(stateTimer,true);
                setBoundsValues("attacking");
                break;
            case ATTACKING_IN_THE_AIR:
                region = (TextureRegion) ((Animation)animationsAndStrings.get("attacking_in_the_air")).getKeyFrame(stateTimer,true);
                setBoundsValues("attacking_in_the_air");
                break;
            case DEAD:
                region = (TextureRegion) ((Animation)animationsAndStrings.get("dying")).getKeyFrame(stateTimer,false);
                Gdx.app.log("animation","setting dead body");
                setBoundsValues("dying");
                break;
            case HURT:
                region = (TextureRegion) ((Animation)animationsAndStrings.get("hurt")).getKeyFrame(stateTimer,false);
                setBoundsValues("hurt");
                break;
            case JUMP_LOOP:
                region = (TextureRegion) ((Animation)animationsAndStrings.get("jump_loop")).getKeyFrame(stateTimer,true);
                setBoundsValues("jump_loop");
                break;
            case JUMP_START:
                region = (TextureRegion) ((Animation)animationsAndStrings.get("jump_start")).getKeyFrame(stateTimer,false);
                setBoundsValues("jump_start");

                break;
            case RUN_ATTACKING:
                region = (TextureRegion) ((Animation)animationsAndStrings.get("run_attacking")).getKeyFrame(stateTimer,true);
                setBoundsValues("run_attacking");

                break;
            case RUN_THORWING:
                region = (TextureRegion) ((Animation)animationsAndStrings.get("run_throwing")).getKeyFrame(stateTimer,true);
                setBoundsValues("run_throwing");

                break;
            case RUNNING:
                region = (TextureRegion) ((Animation)animationsAndStrings.get("running")).getKeyFrame(stateTimer,true);
                setBoundsValues("running");

                break;
            case SLIDING:
                region = (TextureRegion) ((Animation)animationsAndStrings.get("sliding")).getKeyFrame(stateTimer,true);
                setBoundsValues("sliding");

                break;
            case THROWING:
                region = (TextureRegion) ((Animation)animationsAndStrings.get("throwing")).getKeyFrame(stateTimer,true);
                setBoundsValues("throwing");

                break;
            case THROWING_IN_THE_AIR:
                region = (TextureRegion) ((Animation)animationsAndStrings.get("throwing_in_the_air")).getKeyFrame(stateTimer,true);
                setBoundsValues("throwing_in_the_air");
                break;
            case CELEBRATING:
                region = (TextureRegion) ((Animation)animationsAndStrings.get("celebrating")).getKeyFrame(stateTimer,true);
                setBoundsValues("celebrating");

                b2body.setLinearVelocity( new  Vector2((float) 0 ,b2body.getLinearVelocity().y));

                applyImpulse();

                if (stateTimer >8){
                    letsCall = true;
                }

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
        stateTimer = currentState == previousState ? stateTimer + dt*speedOfAnimation : 0;
        //update previous state
        previousState = currentState;
        //return our final adjusted frame
        return region;

    }

    int timeCelebrating =1;

    public void applyImpulse(){
        if (timeCelebrating >0 && b2body.getLinearVelocity().y ==0){
            b2body.applyLinearImpulse(new Vector2(0.4f, 0),
                    b2body.getWorldCenter(), true);
            timeCelebrating ++;
            if (timeCelebrating>30) {
                timeCelebrating=-1;
            }
        }

        else if (timeCelebrating<0 && b2body.getLinearVelocity().y ==0){
            b2body.applyLinearImpulse(new Vector2(-0.4f, 0),
                    b2body.getWorldCenter(), true);
            timeCelebrating--;
            if (timeCelebrating<-30) {
                timeCelebrating=1;
            }
        }


    }

    public void setBoundsValues(String regionName){

            franWidth = (float) (((Integer)regionsAndwidth.get(regionName))/JungleMasters.PPM/1.5);
            franHeight = (float) (((Integer)regionsAndHeight.get(regionName))/JungleMasters.PPM/1.5);
            bodyX =  (b2body.getPosition().x - franWidth );
            bodyY =  (b2body.getPosition().y - franHeight +50    /JungleMasters.PPM);


    }

    public void getState(){

            boolean isJumping = b2body.getLinearVelocity().y != 0;
            boolean isRunning =b2body.getLinearVelocity().x != 0;

            if (!isJumping &&!isRunning && screen.throwing &&!isDyingVar &&!franFranIsDead && !win) currentState = States.THROWING;
            else if (!isJumping && isRunning && screen.throwing &&!isDyingVar &&!franFranIsDead && !win) currentState = States.RUN_THORWING;
            else if (!isJumping && isRunning && screen.attacking &&!isDyingVar&&!franFranIsDead && !win) currentState = States.RUN_ATTACKING;
            else if (isJumping && screen.throwing &&!isDyingVar&&!franFranIsDead && !win) currentState = States.THROWING_IN_THE_AIR;
            else if (isJumping && screen.attacking &&!isDyingVar&&!franFranIsDead && !win) currentState = States.ATTACKING_IN_THE_AIR;

            else if (isJumping && !onTopOfEnemy &&!isDyingVar&&!franFranIsDead && !win) {
//                Gdx.app.log("jum","here "+b2body.getPosition().y+ " "+enemyX);
                currentState = States.JUMP_LOOP;
            }
                // else if (b2body.getLinearVelocity().y<0 && previousState != States.JUMP_LOOP) return States.JUMP_START;
            else if (isRunning && screen.sliding &&!isDyingVar&&!franFranIsDead && !win) currentState = States.SLIDING;
            else if (isRunning  && !screen.sliding && !onTopOfEnemy &&!isDyingVar&&!franFranIsDead && !win) currentState = States.RUNNING;
            else if (!isRunning && screen.attacking &&!isDyingVar&&!franFranIsDead && !win) currentState = States.ATTACKING;
            else if (onTopOfEnemy&&!franFranIsDead && !win){
                currentState = States.IDLE;
            } else if (isDyingVar && !franFranIsDead && !win){
                Gdx.app.log("state", currentState +" " + franFranIsDead);
                currentState = States.HURT;
            } else if (franFranIsDead && !win){

                currentState = States.DEAD;

            } else    if (win) {
                currentState = States.CELEBRATING;
            }
            else currentState = States.IDLE;

    }

    public Caveman.State getStateToCaveman(){

        switch (currentState){
            case JUMP_LOOP:
            case THROWING_IN_THE_AIR:
            case ATTACKING_IN_THE_AIR:
            case THROWING:
                return Caveman.State.JUMPING_ATTACKING;
            case RUNNING:
            case ATTACKING:
                return Caveman.State.RUNNING;

            case RUN_ATTACKING:
            case RUN_THORWING:
                return Caveman.State.ATTACKING;
            default:
                if ((previousState == States.JUMP_LOOP ||
                        previousState == States.ATTACKING_IN_THE_AIR
                                || previousState == States.THROWING_IN_THE_AIR )
                        && (
                        currentState == States.IDLE ||
                        currentState == States.RUNNING ||
                        currentState == States.THROWING ||
                        currentState == States.ATTACKING )){
                    return Caveman.State.JUMPING_ATTACKING;
                }else
                    return Caveman.State.WALKING;

        }

    }

    public void hasfranFranWon(){

        win = b2body.getPosition().x > 71739 / JungleMasters.PPM &&
                b2body.getPosition().x < 72111 / JungleMasters.PPM &&
                b2body.getPosition().x > 1920 / JungleMasters.PPM &&
                b2body.getPosition().y > 3034 / JungleMasters.PPM
                && b2body.getPosition().y > 2434 / JungleMasters.PPM;
    }


    public void defineFranfran(){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(300
                /JungleMasters.PPM,2000/JungleMasters.PPM);

        b2body = world.createBody(bodyDef);


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 0.8f;

        CircleShape shape = new CircleShape();
        shape.setRadius (45/JungleMasters.PPM);
        fixtureDef.filter.categoryBits = JungleMasters.FRAN_BIT;


        fixtureDef.filter.maskBits = JungleMasters.GROUND_BIT |
                JungleMasters.BRICK_BIT|
                JungleMasters.ENEMY_BIT|
                JungleMasters.ENEMY_FRONT_BIT |
                JungleMasters.WATER_BIT|
                JungleMasters.GROUND_ESCAPE|
                JungleMasters.ENEMY_HEAD_BIT;



        fixtureDef.shape = shape;
        b2body.createFixture(fixtureDef).setUserData(this);
        shape.setPosition(new Vector2(0,90/JungleMasters.PPM));

        b2body.createFixture(fixtureDef).setUserData(this);

        //sensor to collide with bricks, with restitution
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-40/JungleMasters.PPM,160/JungleMasters.PPM),new Vector2(40/JungleMasters.PPM,160/JungleMasters.PPM));
        fixtureDef.filter.categoryBits = JungleMasters.FRAN_HEAD_BIT;
        fixtureDef.filter.maskBits = JungleMasters.BRICK_BIT|  JungleMasters.HIDDEN_BIT ;
        fixtureDef.shape = head;
        fixtureDef.isSensor = true;
        b2body.createFixture(fixtureDef).setUserData(this);

        //Sensor to collide with seconds without restitution
        CircleShape bodySensor = new CircleShape();
        bodySensor.setRadius( 70/JungleMasters.PPM);
        fixtureDef.filter.categoryBits = JungleMasters.FRAN_BODY_SENSOR_BIT;
        fixtureDef.filter.maskBits = JungleMasters.SECONDS_BIT;
        fixtureDef.shape = bodySensor;
        fixtureDef.isSensor = true;
        b2body.createFixture(fixtureDef).setUserData(this);
        bodySensor.setPosition(new Vector2(0,80/JungleMasters.PPM));
        b2body.createFixture(fixtureDef).setUserData(this);

    }

    public boolean isFalling(){
        return  b2body.getLinearVelocity().y > 0;
    }

    int ballsCount = 0;
    public void fire() {

        if (((ballsCount%14) ==0)&& screen.hud.points >0){
            fireballs.add(new FireBall(screen, b2body.getPosition().x, b2body.getPosition().y, isRunningRIgth));

         screen.hud.addScore(-1);


        }

        ballsCount = ballsCount +1;


    }

    public void draw(Batch batch) {
        super.draw(batch);

            for (FireBall ball : fireballs)
            ball.draw(batch);
    }

    public void isDying (){
        if (isDyingVar && !onTopOfEnemy){
            if (screen.hud.points >0){
                screen.hud.addScore(-1);

            }
            else{
                franFranIsDead = true;
                Gdx.app.log("dead","dead "+franFranIsDead);

            }

        }
    }


    public void interpolateCurrentPosition(float alpha) {


        float position_previousX = b2body.getPosition().x;
        float position_previousY = b2body.getPosition().y;

                    //---- interpolate: currentState*alpha + previousState * ( 1.0 - alpha ); ------------------
        b2body.getPosition().x = b2body.getPosition().x * alpha + position_previousX * (1.0f - alpha);
        b2body.getPosition().y = b2body.getPosition().y * alpha + position_previousY * (1.0f - alpha);

                }

}
