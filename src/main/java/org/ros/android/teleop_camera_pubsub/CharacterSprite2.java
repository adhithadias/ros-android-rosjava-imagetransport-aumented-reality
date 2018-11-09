package org.ros.android.teleop_camera_pubsub;

import android.content.Context;
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
    Context context;

    private Bitmap level;
    private Bitmap constructionLevel;
    private Bitmap dustbin;
    private Bitmap box;
    private Bitmap smallBox;
    private Bitmap cylinder;
    private Bitmap smallCylinder;
    private Bitmap undo;
    private Bitmap deleteAll;
    private Bitmap send;
    private Bitmap background;
    private Bitmap help;
    private int x, y;

    static int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    static int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    // declaration of paint colours
    private Paint paintBox = new Paint();
    private Paint paintWhite = new Paint();
    private Paint paintBlue = new Paint();
    private Paint paintLargeLetters = new Paint();
    private Paint paintExtraLargeLetters = new Paint();


    // button placement
    static final int SEND_BUTTON_TOP = 160*4 + 20;
    static final int SEND_BUTTON_LEFT = 950;
    static final int SEND_BUTTON_SIZE = 100;

    static final int HELP_BUTTON_LEFT = 1035;
    static final int HELP_BUTTON_TOP = 25;
    static final int HELP_BUTTON_SIZE = 40;

    static final int CONSTRUCTION_SITE_LEFT = 475;
    static final int CONSTRUCTION_SITE_TOP = 300;
    static final int CONSTRUCTION_SITE_WIDTH = 500;
    static final int CONSTRUCTION_SITE_HEIGHT = 300;

    static final int ICON_SIZE = 80;
    static final int SMALL_ICON_SIZE = 30;


    public CharacterSprite2(Context context) {

        this.context = context;
        this.help = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.help), HELP_BUTTON_SIZE, HELP_BUTTON_SIZE,true);

        this.level = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.buildinglevel),250,150,true);
        this.constructionLevel = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.buildinglevel), CONSTRUCTION_SITE_WIDTH, CONSTRUCTION_SITE_HEIGHT,true);

        this.dustbin = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.delete2), ICON_SIZE, ICON_SIZE,true);

        this.box = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.box2), ICON_SIZE, ICON_SIZE,true);
        this.smallBox = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.box2), SMALL_ICON_SIZE, SMALL_ICON_SIZE,true);

        this.cylinder = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.cylinder), ICON_SIZE, ICON_SIZE,true);
        this.smallCylinder = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.cylinder),SMALL_ICON_SIZE,SMALL_ICON_SIZE,true);

        this.undo = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.undo2), ICON_SIZE, ICON_SIZE,true);
        this.deleteAll = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.cross), ICON_SIZE, ICON_SIZE,true);
        this.send = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.send), SEND_BUTTON_SIZE, SEND_BUTTON_SIZE,true);
        this.background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.background2), 1280, 800,true);

//        x = screenWidth/2;
//        y = screenHeight/2;
        x = (screenWidth/2 - 2560/2) / 2;
        y = (screenHeight/2 - 1440/2) / 2;

        // set paint colour
        this.paintBox.setColor(Color.parseColor("#CD5C5C"));

        this.paintWhite.setColor(Color.WHITE);
        this.paintWhite.setStrokeWidth(2);
        this.paintWhite.setTextSize(15);

//        this.paintBlue.setColor(Color.GRAY);
        this.paintBlue.setColor(context.getResources().getColor(R.color.darkBlue));

        this.paintLargeLetters.setColor(Color.WHITE);
        this.paintLargeLetters.setTextSize(25);

        this.paintExtraLargeLetters.setColor(Color.WHITE);
        this.paintExtraLargeLetters.setTextSize(70);

    }

    public CharacterSprite2(Context context, Bitmap floor, Bitmap dustbin, Bitmap box, Bitmap cylinder, Bitmap undo, Bitmap deleteAll, Bitmap send, Bitmap background, Bitmap help) {

        this.context = context;

        this.level = Bitmap.createScaledBitmap(floor,250,150,true);
        this.dustbin = Bitmap.createScaledBitmap(dustbin, ICON_SIZE, ICON_SIZE,true);
        this.box = Bitmap.createScaledBitmap(box, ICON_SIZE, ICON_SIZE,true);
        this.cylinder = Bitmap.createScaledBitmap(cylinder, ICON_SIZE, ICON_SIZE,true);
        this.constructionLevel = Bitmap.createScaledBitmap(floor,500,300,true);
        this.smallBox = Bitmap.createScaledBitmap(box,SMALL_ICON_SIZE, SMALL_ICON_SIZE,true);
        this.smallCylinder = Bitmap.createScaledBitmap(cylinder, SMALL_ICON_SIZE, SMALL_ICON_SIZE,true);
        this.undo = Bitmap.createScaledBitmap(undo, ICON_SIZE, ICON_SIZE,true);
        this.deleteAll = Bitmap.createScaledBitmap(deleteAll, ICON_SIZE, ICON_SIZE,true);
        this.send = Bitmap.createScaledBitmap(send, SEND_BUTTON_SIZE, SEND_BUTTON_SIZE,true);
        this.background = Bitmap.createScaledBitmap(background, 1280, 800,true);
        this.help = Bitmap.createScaledBitmap(help, HELP_BUTTON_SIZE, HELP_BUTTON_SIZE,true);

//        x = screenWidth/2;
//        y = screenHeight/2;
        x = (screenWidth/2 - 2560/2) / 2;
        y = (screenHeight/2 - 1440/2) / 2;

        // set paint colour
        this.paintBox.setColor(Color.parseColor("#CD5C5C"));

        this.paintWhite.setColor(Color.WHITE);
        this.paintWhite.setStrokeWidth(2);
        this.paintWhite.setTextSize(15);

//        this.paintBlue.setColor(Color.GRAY);
        this.paintBlue.setColor(context.getResources().getColor(R.color.darkBlue));

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
        canvas.drawBitmap(background,0,0,null);

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

        /**
         * Construction level
         */
        // items in the construction tab pane
        canvas.drawBitmap(smallBox, 475,125,null);
//        canvas.drawText("" + (GameView2.MAX_BOXES-GameView2.numberOfBoxesUsed), 525,125+20, paintLargeLetters);
        canvas.drawText("" + GameView2.numberOfBoxesUsed + " / " + GameView2.MAX_BOXES, 525,125+22, paintLargeLetters);
        canvas.drawBitmap(smallCylinder, 475,175,null);
//        canvas.drawText("" + (GameView2.MAX_CYLINDERS-GameView2.numberOfCylindersUsed), 525,200+20, paintLargeLetters);
        canvas.drawText("" + GameView2.numberOfCylindersUsed + " / " + GameView2.MAX_CYLINDERS, 525,175+22, paintLargeLetters);

        // Show date and time on canvas
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        String strDate = dateFormat.format(currentTime);

        DateFormat dateFormat2 = new SimpleDateFormat("E, MMM dd");
        String strDate2 = dateFormat2.format(currentTime);

        canvas.drawText(strDate, 700,150, paintExtraLargeLetters);
        canvas.drawText(strDate2, 800,200, paintWhite);

        // draw area
        canvas.drawBitmap(constructionLevel, CONSTRUCTION_SITE_LEFT, CONSTRUCTION_SITE_TOP,null);

        // send button placement
        canvas.drawBitmap(send, SEND_BUTTON_LEFT, SEND_BUTTON_TOP, null);

        // help icon
        canvas.drawBitmap(help, HELP_BUTTON_LEFT, HELP_BUTTON_TOP, null);

        /**
         * ITEM REGION | LEFT PANE
         */
        // Divide the item region
        canvas.drawLine(1100, 0, 1100, 800, paintWhite);

        canvas.drawText("Delete Item", 1125,40-20, paintWhite);
        canvas.drawBitmap(dustbin, 1150, 40, null);                             // draw delete

        canvas.drawLine(1100, 160, 1280, 160, paintWhite);              // hor line
        canvas.drawText("Box", 1125,160+25, paintWhite);
        canvas.drawBitmap(box, 1150, 160 + 40, null);                           // box

        canvas.drawLine(1100, 160*2, 1280, 160*2, paintWhite);          // hor line
        canvas.drawText("Cylinder", 1125,160*2+25, paintWhite);
        canvas.drawBitmap(cylinder,1150, 160*2 + 40, null);                     // cylinder

        canvas.drawLine(1100, 160*3, 1280, 160*3, paintWhite);          // hor line
        canvas.drawText("Undo", 1125,160*3+25, paintWhite);
        canvas.drawBitmap(undo,1150, 160*3 + 40, null);                         // undo

        canvas.drawLine(1100, 160*4, 1280, 160*4, paintWhite);          // hor line
        canvas.drawText("Delete All", 1125,160*4+25, paintWhite);
        canvas.drawBitmap(deleteAll,1150, 160*4 + 40, null);                    // delete all

    }

    public void update(Canvas canvas) {

//        System.out.println("Adhitha");

        // draw blocks in the view area
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

        // draw blocks in the construction area
        for (int j=0; j<GameView2.space[GameView2.selectedlevel].length; j++){
            for (int k=0; k<GameView2.space[GameView2.selectedlevel][0].length; k++){
                if (GameView2.space[GameView2.selectedlevel][j][k].getBlockType() == BlockType.BOX){
                    canvas.drawBitmap(box, CONSTRUCTION_SITE_LEFT + j*100 + 10, CONSTRUCTION_SITE_TOP + k*100 +10, null);
                }else if (GameView2.space[GameView2.selectedlevel][j][k].getBlockType() == BlockType.CYLINDER){
                    canvas.drawBitmap(cylinder, CONSTRUCTION_SITE_LEFT + j*100 + 10, CONSTRUCTION_SITE_TOP + k*100 + 10, null);
                }
            }
        }

    }
}
