package com.junglemasters.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class MyAssetManager {



    public final AssetManager manager = new AssetManager();
    public final String heroePack 		= "spritesheets/textures.pack";


    public void loadImages(){
        manager.load(heroePack, TextureAtlas.class);
    }


}
