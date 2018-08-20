package com.junglemasters.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.junglemasters.JungleMasters;
import com.junglemasters.sprites.Franfran;
import com.junglemasters.sprites.enemies.Caveman;
import com.junglemasters.tools.Box2dworldCreator;
import com.junglemasters.tools.Controller;
import com.junglemasters.tools.Enemy;
import com.junglemasters.tools.MyAssetManager;
import com.junglemasters.tools.WorldContactListener;

import static com.junglemasters.JungleMasters.PPM;

public class PlayScreen implements Screen {



    Controller controller;
    //Game passed onto this screen
    JungleMasters game;
    //camera to show the action
    OrthographicCamera camera;
    //sets the way the images will be rendered on different types of screens
    Viewport viewport;

    //variables used to render the map
    TmxMapLoader mapLoader;
    TiledMap map;
    OrthogonalTiledMapRenderer mapRenderer;

    //b2dworld variables
    World world;
    Box2DDebugRenderer box2DDebugRenderer;
    Box2dworldCreator creator;
    public Franfran franfranPlayer;

    public TextureAtlas atlasForEnemies,atlasForHeroe;
    public Hud hud;

    public boolean jump = false;
    public boolean running_rigth= false;
    public boolean running_left = false;
    public boolean sliding = false;
    public boolean attacking = false;
    public boolean throwing = false;



    public MyAssetManager assMan = new MyAssetManager();


    public PlayScreen(JungleMasters jungleMasters){


        assMan.loadImages();
        assMan.manager.finishLoading();
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("levels/level1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map,1/ PPM);
        game = jungleMasters;

        //Collection of images for our heroe
        atlasForHeroe = assMan.manager.get("spritesheets/textures.pack");
        atlasForEnemies =new TextureAtlas(Gdx.files.internal("spritesheets/textures_enemies.pack"));

        //initialize b2d variables
        world = new World(new Vector2(0,-12),true);
       //box2DDebugRenderer = new Box2DDebugRenderer(false,false,false,false,false,false);
       box2DDebugRenderer = new Box2DDebugRenderer();



        //main character
        franfranPlayer = new Franfran(this);
        //Variable used to create all bodies of our world
        creator = new Box2dworldCreator(this);

        world.setContactListener(new WorldContactListener());


        //Hud used in the game to show statistics
        hud = new Hud(game.batch);
        //982.0 600.0 tablet
        //1794.0 1080.0

        float w = (float) Gdx.graphics.getWidth();
        float h = (float) Gdx.graphics.getHeight();

        //Initialize variables
       camera = new OrthographicCamera();
        viewport = new  FitViewport(JungleMasters.CAMERA_WIDTH/PPM,JungleMasters.CAMERA_HEIGHT/PPM,camera);

        Gdx.app.log("with and height",w+" "+h+" "+Gdx.graphics.getDensity());

       //set the position of the camera to the center of the world
        camera.position.set(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2,0);


        controller = new Controller(game);

    }



    @Override
    public void show() {

    }

    public Franfran getFranfranPlayer() {
        return franfranPlayer;
    }

    public TextureAtlas getAtlasForHeroe() {
        return atlasForHeroe;
    }
    public TextureAtlas getAtlasForEnemies() {
        return atlasForEnemies;
    }


    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    //Handles every input of the user
    public void handleInput(float dt){
//        if (Gdx.input.isTouched()) camera.position.x += 50 *2* dt;

        if (!franfranPlayer.win){

            boolean maxLinearVelocityRight = franfranPlayer.b2body.getLinearVelocity().x <= 4f;
            boolean maxLinearVelocityLeft = franfranPlayer.b2body.getLinearVelocity().x >= -4f;
            boolean maxLinearVelocitySpeedUpRight = franfranPlayer.b2body.getLinearVelocity().x <= 0.12f;
            boolean maxLinearVelocitySpeedUpLeft = franfranPlayer.b2body.getLinearVelocity().x >= -0.12f;
            boolean maxImpulseInY = franfranPlayer.b2body.getLinearVelocity().y == 0;

//            jump = Gdx.input.isKeyJustPressed(Input.Keys.UP);
//            running_rigth= Gdx.input.isKeyPressed(Input.Keys.RIGHT);
//            running_left = Gdx.input.isKeyPressed(Input.Keys.LEFT);
//            sliding = Gdx.input.isKeyPressed(Input.Keys.DOWN);
//            boolean speedUp = Gdx.input.isKeyPressed(Input.Keys.Z);
//            attacking = Gdx.input.isKeyPressed(Input.Keys.A);
//            throwing = Gdx.input.isKeyPressed(Input.Keys.S);


            jump = controller.isUpPressed();
            running_rigth= controller.isRightPressed();
            running_left = controller.isLeftPressed();
            sliding = controller.isDownPressed();
            boolean speedUp = Gdx.input.isKeyPressed(Input.Keys.Z);
            attacking = Gdx.input.isKeyPressed(Input.Keys.A);
            throwing = Gdx.input.isKeyPressed(Input.Keys.S);


            if (throwing) franfranPlayer.fire();
            if (sliding && franfranPlayer.b2body.getLinearVelocity().x!=0)  {
                if (franfranPlayer.b2body.getLinearVelocity().x <0)
                    franfranPlayer.b2body.applyForce(new Vector2(8f,0), franfranPlayer.b2body.getWorldCenter(),false);
                else if (franfranPlayer.b2body.getLinearVelocity().x >0)
                    franfranPlayer.b2body.applyForce(new Vector2(-8f,0), franfranPlayer.b2body.getWorldCenter(),false);
            }

            // Check if the up key is pressed and if the velocity in the y axis is 0 or is the jump key is pressed and fran fran is on top
            //of an enemy, if any of the cases apply, then apply the linear impuls in the y direction
            if (jump && maxImpulseInY || (jump &&franfranPlayer.onTopOfEnemy)){
                franfranPlayer.b2body.applyLinearImpulse(new Vector2(0, 12f),
                        franfranPlayer.b2body.getWorldCenter(), true);
            }

            if (running_rigth && maxLinearVelocityRight ){
                franfranPlayer.b2body.applyLinearImpulse(new Vector2(1f, 0),
                        franfranPlayer.b2body.getWorldCenter(), true);
                franfranPlayer.speedOfAnimation = 2;
                Gdx.app.log("running","right");
            }

            if (running_left && maxLinearVelocityLeft){
                franfranPlayer.b2body.applyLinearImpulse(new Vector2(-1f, 0),
                        franfranPlayer.b2body.getWorldCenter(), true);
                franfranPlayer.speedOfAnimation = 2;
                Gdx.app.log("running","left");
            }

            // Check if is speeding up

            if (speedUp&& franfranPlayer.b2body.getLinearVelocity().y == 0){
                if (maxLinearVelocitySpeedUpLeft && running_left ){
                    franfranPlayer.b2body.applyLinearImpulse(new Vector2(-0.4f, 0),
                            franfranPlayer.b2body.getWorldCenter(), true);
                    franfranPlayer.speedOfAnimation = 4;
                }

                else  if (maxLinearVelocitySpeedUpRight && running_rigth){
                    franfranPlayer.b2body.applyLinearImpulse(new Vector2(0.4f, 0),
                            franfranPlayer.b2body.getWorldCenter(), true);
                    franfranPlayer.speedOfAnimation = 4;
                }

            }

            //Make our caveman jump
            if (jump){
                //This impulse our caveman is it is within reach
                for (Caveman cavemen: creator.getCavemen()){

                    if (cavemen.b2body.isActive()) {

                        boolean maxImpulseForCavemanInY = cavemen.b2body.getLinearVelocity().y == 0;

                        if (cavemen.isRunningRIgth &&
                                cavemen.b2body.getPosition().x - franfranPlayer.b2body.getPosition().x  < 600/ PPM
                                && maxImpulseForCavemanInY){
                            cavemen.b2body.applyLinearImpulse(new Vector2(0.2f, 10.25f), cavemen.b2body.getWorldCenter(), true);
                        }
                        else{
                            if ( !cavemen.isRunningRIgth&&
                                    cavemen.b2body.getPosition().x - franfranPlayer.b2body.getPosition().x  < 600/ PPM && maxImpulseForCavemanInY)
                                cavemen.b2body.applyLinearImpulse(new Vector2(-0.2f, 10.25f), cavemen.b2body.getWorldCenter(), true);
                        }
                    }

                }
            }

        }



    }


    private static final float STEP_TIME = 1/60f;
    private float accumulator = 0;

    private void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();

        accumulator += Math.min(delta, 0.25f);

        while (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;

            world.step(STEP_TIME, 10, 8);
        }
    }


    //mehtod called from the rendered method
    public void update(float dt) {
        handleInput(dt);



        stepWorld();
       // world.step(1 / 60f, 6, 2);
        if (!franfranPlayer.franFranIsDead || franfranPlayer.stateTimer<5){
            franfranPlayer.update(dt);
            franfranPlayer.interpolateCurrentPosition(accumulator/STEP_TIME);
        }


        for (Enemy enemy : creator.getEnemies()) {
            enemy.update(dt);
                if (!enemy.destroyed && !enemy.b2body.isActive() && enemy.getX() < franfranPlayer.getX() + 1500 / PPM) {
                    enemy.b2body.setActive(true);
            }

        }

            hud.update(dt);

            //Set the camera to follow our main character
            //very important to update the camera every render cycle
            camera.update();
            //set the view of the renderer to what the camera can see

            if (franfranPlayer.b2body.getPosition().x < 900/ PPM ){
                camera.position.x = (900/ PPM);
            } else {
                camera.position.x = franfranPlayer.b2body.getPosition().x;
            }

            if (franfranPlayer.b2body.getPosition().y < 3800/ PPM &&
                    franfranPlayer.b2body.getPosition().y > 550/ PPM
                    ){
                camera.position.y =franfranPlayer.b2body.getPosition().y;
            }


            mapRenderer.setView(camera);

        }


    @Override
    public void render(float delta) {


        update(delta);
        Gdx.gl.glClearColor(221, 248, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.render();
        box2DDebugRenderer.render(world,camera.combined);
        //we only render what the camera can see
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        franfranPlayer.draw(game.batch);

        for (Enemy enemy : creator.getEnemies()){
               enemy.draw(game.batch);
        }

        game.batch.end();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();


        if (gameOver()){
            Gdx.app.log("GameOver", "now calling");
            game.setScreen(new GameOverScreen(game, hud));
            dispose();
        }

        if (franfranPlayer.letsCall){
            game.setScreen(new YouWinScreen(game,hud));
            dispose();
        }

        if(Gdx.app.getType() == Application.ApplicationType.Android)
            controller.draw();
    }

    @Override
    public void resize(int width, int height) {
        //update the screen when resized
        viewport.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
        map.dispose();
        hud.dispose();
        box2DDebugRenderer.dispose();
        assMan.manager.dispose();
        world.dispose();
        atlasForEnemies.dispose();
        atlasForHeroe.dispose();



    }

    public boolean gameOver (){
        if ((franfranPlayer.currentState == Franfran.States.DEAD) && franfranPlayer.getStateTimer() > 3){
            return  true;
        } else {
            return false;
        }
    }


}
