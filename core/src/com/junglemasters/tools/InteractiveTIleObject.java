package com.junglemasters.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.junglemasters.JungleMasters;
import com.junglemasters.Screens.PlayScreen;
import com.junglemasters.sprites.Franfran;

public abstract class InteractiveTIleObject {
    private World world;
    private TiledMap map;
    private TiledMapTile tile;
    public Body body;
    public PlayScreen screen;
    public Fixture fixture;
    protected MapObject object;
    private Ellipse ellipse;




    public InteractiveTIleObject(PlayScreen screen, MapObject object){

        this.object = object;
        this.screen = screen;
        this.world =screen.getWorld();
        this.map = screen.getMap();

        BodyDef bdef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        CircleShape circleShape = new CircleShape();
        FixtureDef fdef = new FixtureDef();
        if ( object.getClass().getSimpleName().equals("EllipseMapObject")){

            this.ellipse = ((EllipseMapObject)object).getEllipse();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((ellipse.x + ellipse.width / 2) / JungleMasters.PPM, (ellipse.y + ellipse.height / 2) / JungleMasters.PPM);

            body = world.createBody(bdef);
            fdef.friction = 0.4f;
            circleShape.setRadius((ellipse.height / 2) / JungleMasters.PPM);
            fdef.shape = circleShape;
            fixture=  body.createFixture(fdef);


        } else {

            Rectangle rectangle = ((RectangleMapObject)object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rectangle.getX() + rectangle.getWidth()/2)/JungleMasters.PPM, (rectangle.getY()+rectangle.getHeight()/2)/JungleMasters.PPM);

            body = world.createBody(bdef);
            fdef.friction = 0.4f;
            polygonShape.setAsBox(rectangle.getWidth()/2/ JungleMasters.PPM,rectangle.getHeight()/2/ JungleMasters.PPM);
            fdef.shape = polygonShape;
            fixture=   body.createFixture(fdef);
        }

    }


    public abstract void onHeadHit(Franfran franfran);
    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(map.getLayers().getIndex("tiles"));

        int xPosition =(int) (body.getPosition().x * JungleMasters.PPM/128 );
        int yPosition = (int) (body.getPosition().y * JungleMasters.PPM/128 )+1;

        return layer.getCell(xPosition,yPosition);
    }

}
