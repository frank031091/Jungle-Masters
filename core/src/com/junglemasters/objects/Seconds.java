package com.junglemasters.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.junglemasters.JungleMasters;
import com.junglemasters.Screens.Hud;
import com.junglemasters.Screens.PlayScreen;
import com.junglemasters.sprites.Franfran;
import com.junglemasters.tools.InteractiveTIleObject;

public class Seconds extends InteractiveTIleObject {
    private TextureRegion fruits;
    public Seconds(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(JungleMasters.SECONDS_BIT);

    }

    @Override
    public void onHeadHit(Franfran franfran) {
        getCell().setTile(null);
        screen.hud.addScore(1);
        setCategoryFilter(JungleMasters.DESTROYED_BIT);
    }

}
