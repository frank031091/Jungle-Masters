package com.junglemasters.objects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.junglemasters.JungleMasters;
import com.junglemasters.Screens.Hud;
import com.junglemasters.Screens.PlayScreen;
import com.junglemasters.sprites.Franfran;
import com.junglemasters.tools.InteractiveTIleObject;

public class Bricks extends InteractiveTIleObject {


    public Bricks(PlayScreen screen, MapObject object) {
        super(screen, object);

        fixture.setUserData(this);
        setCategoryFilter(JungleMasters.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Franfran franfran) {

        franfran.screen.hud.addScore(1);
        getCell().setTile(null);
        setCategoryFilter(JungleMasters.DESTROYED_BIT);

    }


}
