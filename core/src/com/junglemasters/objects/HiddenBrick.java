package com.junglemasters.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.junglemasters.JungleMasters;
import com.junglemasters.Screens.Hud;
import com.junglemasters.Screens.PlayScreen;
import com.junglemasters.sprites.Franfran;
import com.junglemasters.tools.InteractiveTIleObject;

public class HiddenBrick extends InteractiveTIleObject {

    private static TiledMapTileSet tileSet;
    private final int GROUND_TILE = 22;

    public HiddenBrick(PlayScreen screen, MapObject object) {
        super(screen, object);

        tileSet = screen.getMap().getTileSets().getTileSet(0);
        fixture.setUserData(this);
        setCategoryFilter(JungleMasters.HIDDEN_BIT);
    }

    @Override
    public void onHeadHit(Franfran franfran) {



        if (franfran.isFalling()){
            screen.hud.addScore(1);
            getCell().setTile(tileSet.getTile(22));
            setCategoryFilter(JungleMasters.GROUND_ESCAPE);
        }


    }
}
