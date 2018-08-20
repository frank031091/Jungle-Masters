package com.junglemasters.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.junglemasters.Screens.PlayScreen;
import com.junglemasters.sprites.Franfran;

public abstract class Enemy extends Sprite {

    public enum State {WALKING, ATTACKING, DEAD}
    public int timesDead;
    protected World world;
    public float stateTime;
    protected PlayScreen screen;
    public Body b2body;
    public boolean setToDestroy;
    public boolean destroyed;
    protected Vector2 velocity;
    protected float velocityOfSprite = 1;
    public boolean isTimeToAttack;
    public boolean isRunningRIgth;
    public int hitsByFruit;
    public boolean deadByFruitVar;

    public Enemy(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        deadByFruitVar = false;
        hitsByFruit = 0;

        b2body.setActive(false);
    }

    protected abstract void defineEnemy();
    public abstract void update(float dt);
    public abstract void hitOnHead(Franfran franfran);
    public void hitByFruit(){
        if (hitsByFruit >= 3){
            deadByFruitVar = true;
            timesDead = timesDead+1;
            setToDestroy = true;
            hitsByFruit =0;
        }else {
            hitsByFruit = hitsByFruit +1;
            deadByFruitVar = false;
        }
    };
    public abstract void hitByEnemy(Enemy enemy);

    public void reverseVelocity(boolean x, boolean y){
        if(x)
            velocity.x = -velocity.x;

        if(y)
            velocity.y = -velocity.y;
    }

}
