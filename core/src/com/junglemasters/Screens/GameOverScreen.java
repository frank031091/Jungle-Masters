package com.junglemasters.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.junglemasters.JungleMasters;
import com.junglemasters.sprites.Franfran;
import com.junglemasters.tools.Box2dworldCreator;



public class GameOverScreen implements Screen {
    private final String GAME_OVER = "GAME OVER";
    private final String PLAY_AGAIN = "Click para intentarlo de nuevo";
    private double minutesAwarded;
    private PlayScreen screen;
    private final String SECONDS_AWARDED = minutesAwarded+" minutos obtenidos";
    private final String POSSIBLE_MINUTES= "De " + Box2dworldCreator.possiblePoints/240+" posibles";
    private Viewport viewport;
    private Stage stage;

    private Game game;

    public GameOverScreen(Game game, Hud hud){
        this.game = game;
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, ((JungleMasters) game).batch);
        minutesAwarded = hud. points/60;
        BitmapFont bitmapFont = new BitmapFont();
        bitmapFont.getData().setScale(4,4);
        this.screen = screen;
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOverLabel =  new Label( GAME_OVER, new Label.LabelStyle(bitmapFont,Color.WHITE));
        Label playAgainLabel = new Label(PLAY_AGAIN, new Label.LabelStyle(bitmapFont,Color.WHITE));
        Label secondsAwardedLabel = new Label(SECONDS_AWARDED, new Label.LabelStyle(bitmapFont,Color.RED));
        Label possibleMinutesLabel = new Label(POSSIBLE_MINUTES, new Label.LabelStyle(bitmapFont, Color.GREEN));


        table.add(gameOverLabel).expandX();
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
