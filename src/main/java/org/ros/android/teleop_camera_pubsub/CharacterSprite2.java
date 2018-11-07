package org.ros.android.teleop_camera_pubsub;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CharacterSprite2 {
    private Bitmap level;
    private Bitmap constructionLevel;
    private Bitmap dustbin;
    private Bitmap box;
    private Bitmap smallBox;
    private Bitmap cylinder;
    private Bitmap smallCylinder;
    private Bitmap undo;
    private Bitmap deleteAll;
    private int x, y;

    static int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    static int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    // declaration of paint colours
    private Paint paintBox = new Paint();
    private Paint paintWhite = new Paint();
    private Paint paintBlue = new Paint();
    private Paint paintLargeLetters = new Paint();
    private Paint paintExtraLargeLetters = new Paint();

    public CharacterSprite2(Bitmap floor, Bitmap dustbin, Bitmap box, Bitmap cylinder, Bitmap undo, Bitmap deleteAll) {

        this.level = Bitmap.createScaledBitmap(floor,250,150,true);
        this.dustbin = Bitmap.createScaledBitmap(dustbin,80,80,true);
        this.box = Bitmap.createScaledBitmap(box,80,80,true);
        this.cylinder = Bitmap.createScaledBitmap(cylinder,80,80,true);
        this.constructionLevel = Bitmap.createScaledBitmap(floor,500,300,true);
        this.smallBox = Bitmap.createScaledBitmap(box,30,30,true);
        this.smallCylinder = Bitmap.createScaledBitmap(cylinder,30,30,true);
        this.undo = Bitmap.createScaledBitmap(undo,80,80,true);
        this.deleteAll = Bitmap.createScaledBitmap(deleteAll,80,80,true);

//        x = screenWidth/2;
//        y = screenHeight/2;
        x = (screenWidth/2 - 2560/2) / 2;
        y = (screenHeight/2 - 1440/2) / 2;

        // set paint colour
        this.paintBox.setColor(Color.parseColor("#CD5C5C"));

        this.paintWhite.setColor(Color.WHITE);
        this.paintWhite.setStrokeWidth(2);
        this.paintWhite.setTextSize(15);

        this.paintBlue.setColor(Color.GRAY);

        this.paintLargeLetters.setColor(Color.WHITE);
        this.paintLargeLetters.setTextSize(25);

        this.paintExtraLargeLetters.setColor(Color.WHITE);
        this.paintExtraLargeLetters.setTextSize(70);

    }

    public void draw(Canvas canvas) {
//        image = Bitmap.createScaledBitmap(RosComNode.drawableBitmap, 2560, 1440, true);
//        canvas.drawBitmap(ImageScaleThread.image, x, y, null);
//        System.out.println(screenWidth + "  " + screenHeight);
//        System.out.println(x + "  " + y);

//        canvas.drawRect(130,200,160,240, paintBox);

        drawStaticMap(canvas);

    }

    private void drawStaticMap(Canvas canvas){

        if(GameView2.levelSelection==LevelSelection.LEVEL1){
            canvas.drawRect(0,0,350, 58*2+150*1, paintBlue);
        }else if(GameView2.levelSelection==LevelSelection.LEVEL2){
            canvas.drawRect(0,58*2+150*1,350, 58*4+150*2, paintBlue);
        }else{
            canvas.drawRect(0,58*4+150*2,350, 800, paintBlue);
        }

        if(GameView2.leftSelection==LeftSelection.DELETE){
            canvas.drawRect(1100,0,1280, 160*1, paintBlue);
        }else if(GameView2.leftSelection==LeftSelection.BOX){
            canvas.drawRect(1100,160*1,1280, 160*2, paintBlue);
        }else if(GameView2.leftSelection==LeftSelection.CYLINDER){
            canvas.drawRect(1100,160*2,1280, 160*3, paintBlue);
        }else if(GameView2.leftSelection==LeftSelection.UNDO){
            canvas.drawRect(1100,160*3,1280, 160*4, paintBlue);
        }else{
            canvas.drawRect(1100,160*4,1280, 800, paintBlue);
        }

        canvas.drawText("Level 1", 20,30, paintWhite);
        canvas.drawBitmap(level,50,58*1+150*0,null);
        canvas.drawLine(0,58*2+150*1, 350, 58*2+150*1, paintWhite);

        canvas.drawText("Level 2", 20,58*2+150*1+30, paintWhite);
        canvas.drawBitmap(level,50,58*3+150*1,null);
        canvas.drawLine(0,58*4+150*2, 350, 58*4+150*2, paintWhite);

        canvas.drawText("Level 3", 20,58*4+150*2+30, paintWhite);
        canvas.drawBitmap(level,50,58*5+150*2,null);

        // Divide the level region
        canvas.drawLine(350, 0, 350, 800, paintWhite);

        // construction level
        canvas.drawBitmap(constructionLevel, 475,375,null);


        // items in the construction tab pane
        canvas.drawBitmap(smallBox, 475,125,null);
        canvas.drawBitmap(smallCylinder, 475,200,null);

        // Divide the item region
        canvas.drawLine(1100, 0, 1100, 800, paintWhite);

        canvas.drawBitmap(dustbin, 1150, 40, null);
        canvas.drawLine(1100, 160, 1280, 160, paintWhite);
        canvas.drawBitmap(box, 1150, 160 + 40, null);
        canvas.drawLine(1100, 160*2, 1280, 160*2, paintWhite);
        canvas.drawBitmap(cylinder,1150, 160*2 + 40, null);
        canvas.drawLine(1100, 160*3, 1280, 160*3, paintWhite);
        canvas.drawBitmap(undo,1150, 160*3 + 40, null);
        canvas.drawLine(1100, 160*4, 1280, 160*4, paintWhite);
        canvas.drawBitmap(deleteAll,1150, 160*4 + 40, null);

    }

    public void update(Canvas canvas) {

//        System.out.println("Adhitha");

        for (int i=0; i<GameView2.space.length; i++){
            for (int j=0; j<GameView2.space[0].length; j++){
                for (int k=0; k<GameView2.space[0][0].length; k++){
                    if (GameView2.space[i][j][k].getBlockType() == BlockType.BOX){
                        canvas.drawBitmap(smallBox, GameView2.space[i][j][k].getCoordinateX(), GameView2.space[i][j][k].getCoordinateY(), null);
                    }else if(GameView2.space[i][j][k].getBlockType() == BlockType.CYLINDER){
                        canvas.drawBitmap(smallCylinder, GameView2.space[i][j][k].getCoordinateX(), GameView2.space[i][j][k].getCoordinateY(), null);
                    }
                }
            }
        }

        for (int j=0; j<GameView2.space[GameView2.selectedlevel].length; j++){
            for (int k=0; k<GameView2.space[GameView2.selectedlevel][0].length; k++){
                if (GameView2.space[GameView2.selectedlevel][j][k].getBlockType() == BlockType.BOX){
                    canvas.drawBitmap(box, 475 + j*100 + 10, 375 + k*100 +10, null);
                }else if (GameView2.space[GameView2.selectedlevel][j][k].getBlockType() == BlockType.CYLINDER){
                    canvas.drawBitmap(cylinder, 475 + j*100 + 10, 375 + k*100 + 10, null);
                }
            }
        }


        canvas.drawText("" + (GameView2.MAX_BOXES-GameView2.numberOfBoxesUsed), 525,125+20, paintLargeLetters);
        canvas.drawText("" + (GameView2.MAX_CYLINDERS-GameView2.numberOfCylindersUsed), 525,200+20, paintLargeLetters);

        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        String strDate = dateFormat.format(currentTime);

        DateFormat dateFormat2 = new SimpleDateFormat("E, MMM dd");
        String strDate2 = dateFormat2.format(currentTime);


        canvas.drawText(strDate, 700,150, paintExtraLargeLetters);
        canvas.drawText(strDate2, 800,200, paintWhite);

    }
}
