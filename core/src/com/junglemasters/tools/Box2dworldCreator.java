package com.junglemasters.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import com.junglemasters.JungleMasters;
import com.junglemasters.Screens.PlayScreen;
import com.junglemasters.objects.Bricks;
import com.junglemasters.objects.HiddenBrick;
import com.junglemasters.objects.Seconds;
import com.junglemasters.sprites.Franfran;
import com.junglemasters.sprites.enemies.Caveman;
import com.junglemasters.sprites.enemies.PurplePlant;
import com.junglemasters.sprites.enemies.RedDino;
import com.junglemasters.sprites.enemies.TeraDactyl;
import com.junglemasters.sprites.enemies.WhiteDino;

import static com.junglemasters.JungleMasters.PPM;

public class Box2dworldCreator {


    private final Array<Bricks> bricks;
    private final Array<HiddenBrick> hiddenBricks;
    public static int possiblePoints = 0;
    private Array<RedDino> redDinos;
    private Array<WhiteDino> whiteRhinos;
    private Array<TeraDactyl> teraDactyles;
    private Array<PurplePlant> purplePlants;
    private Array<Caveman> cavemen;
    World world;
    TiledMap map;
    PlayScreen screen;
    Array<Seconds> seconds;
    public Box2dworldCreator(PlayScreen playscreen) {


        world = playscreen.getWorld();
        map = playscreen.getMap();
        screen = playscreen;

        BodyDef bodyDef = new BodyDef();
        //shape used to define the ground and the walls
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;


        int groundIndex = map.getLayers().getIndex("ground");
        int limitIndex = map.getLayers().getIndex("limits");
        int bricksIndex = map.getLayers().getIndex("bricks");
        int islandsIndex = map.getLayers().getIndex("islands");
        int waterIndex = map.getLayers().getIndex("water");
        int secondsIndex = map.getLayers().getIndex("seconds");
        int redDinoIndex = map.getLayers().getIndex("redDinos");
        int whiteRhinosIndex = map.getLayers().getIndex("whiteRhinos");
        int teraDactylIndex = map.getLayers().getIndex("teraDactyles");
        int purplePlantIndex = map.getLayers().getIndex("purplePlant");
        int cavemenIndex = map.getLayers().getIndex("cavemen");
        int hiddenGroundIndex = map.getLayers().getIndex("hiddenTile");
        int carretaIndex = map.getLayers().getIndex("carreta");


        //Create the ground
        for (MapObject object: map.getLayers().get(groundIndex).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject)object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth()/2)/JungleMasters.PPM, (rectangle.getY()+rectangle.getHeight()/2)/JungleMasters.PPM);

            body = world.createBody(bodyDef);
            shape.setAsBox(rectangle.getWidth()/2/ JungleMasters.PPM,rectangle.getHeight()/2/ JungleMasters.PPM);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);

        }

        //Create the limits
        for (MapObject object: map.getLayers().get(limitIndex).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject)object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth()/2)/JungleMasters.PPM, (rectangle.getY()+rectangle.getHeight()/2)/JungleMasters.PPM);

            body = world.createBody(bodyDef);
            shape.setAsBox(rectangle.getWidth()/2/ JungleMasters.PPM,rectangle.getHeight()/2/ JungleMasters.PPM);
            fixtureDef.filter.categoryBits = JungleMasters.ENEMY_LIMIT;
            fixtureDef.filter.maskBits = JungleMasters.ENEMY_BIT;
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);

        }



        //Create the islands
        for (MapObject object: map.getLayers().get(islandsIndex).getObjects().getByType(PolylineMapObject.class)){
            Polyline polyline = ((PolylineMapObject) object).getPolyline();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(polyline.getX()/JungleMasters.PPM,polyline.getY()/JungleMasters.PPM);
            fixtureDef.filter.categoryBits = JungleMasters.ISLAND_BIT;
            fixtureDef.filter.maskBits = JungleMasters.FRAN_BIT;
            body =  world.createBody(bodyDef);
            float[] vertices = polyline.getVertices();
            for (int i = 0; i< vertices.length;i++){
                vertices[i] =  vertices[i] / JungleMasters.PPM;
            }
            shape.set(vertices);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);

        }

        //Create the water
        for (MapObject object: map.getLayers().get(waterIndex).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject)object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth()/2)/JungleMasters.PPM,
                    (rectangle.getY()+rectangle.getHeight()/2)/JungleMasters.PPM);

            fixtureDef.filter.categoryBits = JungleMasters.WATER_BIT;
            fixtureDef.filter.maskBits = JungleMasters.FRAN_BIT |
                                        JungleMasters.ENEMY_BIT;

            body = world.createBody(bodyDef);
            fixtureDef.friction = 0;
            shape.setAsBox(rectangle.getWidth()/2/ JungleMasters.PPM,rectangle.getHeight()/2/ JungleMasters.PPM);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);

        }

        //Create the bricks
        bricks = new Array<Bricks>();
        for (MapObject object: map.getLayers().get(bricksIndex).getObjects().getByType(RectangleMapObject.class)){
           bricks.add(new Bricks(screen,object));
        }


        //Create the hidden bricks
        hiddenBricks = new Array<HiddenBrick>();
        for (MapObject object: map.getLayers().get(hiddenGroundIndex).getObjects().getByType(RectangleMapObject.class)){
         hiddenBricks.add(new HiddenBrick(screen,object));
        }


        //Create the seconds
        seconds = new Array<Seconds>();
        for(MapObject object : map.getLayers().get(secondsIndex).getObjects().getByType(EllipseMapObject.class)){
           seconds.add(new Seconds(screen, object));
        }

        //Create red dinos enemies
        redDinos = new Array<RedDino>();
        for(MapObject object : map.getLayers().get(redDinoIndex).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            redDinos.add(new RedDino(screen, rect.getX() / JungleMasters.PPM, rect.getY() / JungleMasters.PPM));
        }

        //Create white rhinos enemies
        whiteRhinos = new Array<WhiteDino>();
        for(MapObject object : map.getLayers().get(whiteRhinosIndex).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            whiteRhinos.add(new WhiteDino(screen, rect.getX() / JungleMasters.PPM, rect.getY() / JungleMasters.PPM));
        }

        //Create teraDactyles enemies
        teraDactyles = new Array<TeraDactyl>();
        for(MapObject object : map.getLayers().get(teraDactylIndex).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            teraDactyles.add(new TeraDactyl(screen, rect.getX() / JungleMasters.PPM, rect.getY() / JungleMasters.PPM));
        }
        //Create purple plant enemies
        purplePlants = new Array<PurplePlant>();
        for(MapObject object : map.getLayers().get(purplePlantIndex).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            purplePlants.add(new PurplePlant(screen, rect.getX() / JungleMasters.PPM, rect.getY() / JungleMasters.PPM));
        }

        //Create purple plant enemies
        cavemen = new Array<Caveman>();
        for(MapObject object : map.getLayers().get(cavemenIndex).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            cavemen.add(new Caveman(screen, rect.getX() / JungleMasters.PPM, rect.getY() / JungleMasters.PPM));
        }









//
//
//            rDef.collideConnected = false;
//            rDef.localAnchorA.set(-200/JungleMasters.PPM, -200/JungleMasters.PPM);
//            rDef.enableMotor = true;
//            rDef.motorSpeed = 0.5f;
//            rDef.maxMotorTorque = 0.9f;



           // Gdx.app.log("get ","here "+world.createJoint(rDef).toString());


//        JointBuilder.joinWithRevoluteMotor(world,)


        possiblePoints = hiddenBricks.size +bricks.size+seconds.size+redDinos.size * 5+whiteRhinos.size *15
                +teraDactyles.size*10+((purplePlants.size+cavemen.size)*30);
    }

    public Array<Caveman> getCavemen() {
        return cavemen;
    }
    public Array<PurplePlant> getPurplePlants() {
        return purplePlants;
    }

    public Array<Enemy> getEnemies(){
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(teraDactyles);
        enemies.addAll(redDinos);
        enemies.addAll(whiteRhinos);
        enemies.addAll(purplePlants);
         enemies.addAll(cavemen);
        return enemies;
    }



}
