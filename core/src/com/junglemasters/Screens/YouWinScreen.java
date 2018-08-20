package com.junglemasters.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.junglemasters.JungleMasters;
import com.junglemasters.tools.Box2dworldCreator;

import java.text.DecimalFormat;



public class YouWinScreen implements Screen {
    private final String YOU_WIN = "MISION CUMPLIDA";
    private final String PLAY_AGAIN = "Click para guardar tus minutos y salir";
    private final String POSSIBLE_MINUTES= "De " + Box2dworldCreator.possiblePoints/240+" posibles";
    private Viewport viewport;
    private Stage stage;

    private Game game;

    public YouWinScreen (Game game, Hud hud){
        this.game = game;
        viewport = new FitViewport(JungleMasters.CAMERA_WIDTH, JungleMasters.CAMERA_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((JungleMasters) game).batch);

        BitmapFont bitmapFont = new BitmapFont();
        bitmapFont.getData().setScale(4,4);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label youWinLabel =  new Label(YOU_WIN, new Label.LabelStyle(bitmapFont, Color.WHITE));
        Label playAgainLabel = new Label(PLAY_AGAIN, new Label.LabelStyle(bitmapFont,Color.WHITE));
        Label secondsAwardedLabel = new Label( String.format("%.2f", hud.minutesAwarded)+ " minutos obtenidos",
                new Label.LabelStyle(bitmapFont,Color.RED));
        Label possibleMinutesLabel = new Label(POSSIBLE_MINUTES, new Label.LabelStyle(bitmapFont, Color.GREEN));


        table.add(youWinLabel).expandX();
        table.row();
        table.add(playAgainLabel).expandX().padTop(10f);
        table.row();
        table.add(secondsAwardedLabel).expandX().padTop(10f);
        table.row();
        table.row();
        table.add(possibleMinutesLabel).expandX().padTop(10f);


        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()) {
            game.setScreen(new PlayScreen((JungleMasters) game));
            dispose();
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {


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
        stage.dispose();
    }

}
