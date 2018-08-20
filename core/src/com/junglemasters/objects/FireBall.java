package com.junglemasters.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.junglemasters.JungleMasters;
import com.junglemasters.Screens.PlayScreen;

import java.util.Random;

public class FireBall extends Sprite {

    PlayScreen screen;
    World world;
    float stateTime;
    boolean destroyed;
    boolean setToDestroy;
    boolean fireRight;
    TextureRegion rock, apple, banana, cherry;
    Body b2body;

    private Random number = new Random();
    private int textureInt = number.nextInt(5);
    public FireBall(PlayScreen screen, float x, float y, boolean fireRight){
        this.fireRight = fireRight;
        this.screen = screen;
        this.world = screen.getWorld();

        banana =  new TextureRegion(screen.getAtlasForHeroe().findRegion("objects"), 0, 0, 32, 32);
        cherry =  new TextureRegion(screen.getAtlasForHeroe().findRegion("objects"), 32, 0, 32, 32);
        rock =  new TextureRegion(screen.getAtlasForHeroe().findRegion("objects"), 64, 0, 32, 32);
        apple =  new TextureRegion(screen.getAtlasForHeroe().findRegion("objects"), 96, 0, 32, 32);


        setRegion(getRandomTextture());
        setBounds(x, y, 64 / JungleMasters.PPM, 64 / JungleMasters.PPM);
        defineFireBall();
    }


    public TextureRegion getRandomTextture(){

        switch (textureInt){
            case 0:
                return banana;
            case 1:
                return cherry;
            case 2:
                return rock;
            default:
                return apple;
        }

    }

    public void defineFireBall(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(fireRight ? getX() + 50 /JungleMasters.PPM : getX() - 50 /JungleMasters.PPM, getY() + 24/JungleMasters.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        if(!world.isLocked())
            b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(16 / JungleMasters.PPM);
        fdef.filter.categoryBits = JungleMasters.FIREBALL_BIT;
        fdef.filter.maskBits = JungleMasters.GROUND_BIT |
                JungleMasters.FIREBALL_BIT|
                JungleMasters.BRICK_BIT |
                JungleMasters.ENEMY_FRONT_BIT|
                JungleMasters.ENEMY_BIT;

        fdef.shape = shape;
        fdef.restitution = 0;
        fdef.friction = 0;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setLinearVelocity(new Vector2(fireRight ? 20 : -20, 2.5f));
    }

    public void update(float dt){
        stateTime += dt;
        setRegion(getRandomTextture());
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if((stateTime > 3 || setToDestroy) && !destroyed) {
                world.destroyBody(b2body);

            destroyed = true;
        }
        if(b2body.getLinearVelocity().y > 2f)
            b2body.setLinearVelocity(b2body.getLinearVelocity().x, 2f);
        if((fireRight && b2body.getLinearVelocity().x < 0) || (!fireRight && b2body.getLinearVelocity().x > 0))
            setToDestroy();
    }

    public void setToDestroy(){
        setToDestroy = true;
    }

    public boolean isDestroyed(){
        return destroyed;
    }


}
