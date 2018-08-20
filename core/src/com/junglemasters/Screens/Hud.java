package com.junglemasters.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.junglemasters.JungleMasters;
import com.junglemasters.sprites.Franfran;

public class Hud implements Disposable {

    //Scene2D.ui Stage and its own Viewport for HUD
    public Stage stage;
    private Viewport viewport;

    //Mario points/time Tracking Variables
    private Integer worldTimer;
    private boolean timeUp; // true when the world timer reaches 0
    private float timeCount;
    public int points;
    public double minutesAwarded;
    private int conversionValue = 240;


    //Scene2D widgets
    private Label countdownLabel;
    private Label scoreLabel,minutosConvertidosLabel;
    private Label timeLabel;
    private Label puntosLabel, minutosLabel;

    public Hud(SpriteBatch sb){
        //define our tracking variables
        worldTimer = 600
        ;
        timeCount = 0;
        points = 100;
        minutesAwarded =0.42;
        ;


        //setup the HUD viewport using a new camera seperate from our gamecam
        //define our stage using that viewport and our games spritebatch
        viewport = new FitViewport(JungleMasters.CAMERA_WIDTH, JungleMasters.CAMERA_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        //define a table used to organize our hud's labels
        Table table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        BitmapFont bitmapFont = new BitmapFont();
        bitmapFont.getData().setScale(4,4);

        //define our labels using the String, and a Label style consisting of a font and color
        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(bitmapFont,Color.GREEN));
        scoreLabel =new Label(String.format("%06d", points), new Label.LabelStyle(bitmapFont, Color.GREEN));
        timeLabel = new Label("TIEMPO", new Label.LabelStyle(bitmapFont, Color.RED));
        puntosLabel = new Label("PUNTOS DE VIDA", new Label.LabelStyle(bitmapFont, Color.RED));
        minutosLabel = new Label("MINUTOS", new Label.LabelStyle(bitmapFont, Color.RED));
        minutosConvertidosLabel =new Label(String.format("%.4f", minutesAwarded), new Label.LabelStyle(bitmapFont, Color.GREEN));

        //add our labels to our table, padding the top, and giving them all equal width with expandX
        table.add(puntosLabel).expandX().padTop(10);
        table.add(minutosLabel).expandX();
        table.add(timeLabel).expandX().padTop(10);
        //add a second row to our table
        table.row();
        table.add(scoreLabel).expandX();
        table.add(minutosConvertidosLabel).expandX();
        table.add(countdownLabel).expandX();

        //add our table to the stage
        stage.addActor(table);

    }

    public void update(float dt){
        timeCount += dt;
        if(timeCount >= 1){
            if (worldTimer > 0) {
                worldTimer--;
            } else {
                //Do something if the time is up
            }
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
    }

    public void addScore(int value){
        points += value;
        scoreLabel.setText(String.format("%04d", points));
        setMinutesAwarded(value);
    }

    private void setMinutesAwarded(double value){
        minutesAwarded += value/conversionValue;
        minutosConvertidosLabel.setText(String.format("%.4f", minutesAwarded));
    }



    @Override
    public void dispose() { stage.dispose(); }

    public boolean isTimeUp() {

        return timeUp; }
}


