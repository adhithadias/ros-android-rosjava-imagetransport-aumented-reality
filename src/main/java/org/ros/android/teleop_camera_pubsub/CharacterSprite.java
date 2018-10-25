package org.ros.android.teleop_camera_pubsub;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by rushd on 7/5/2017.
 */

public class CharacterSprite {


    private Bitmap image;
    private int x, y;
    private int xVelocity = 10;
    private int yVelocity = 5;
    static int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    static int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;


    // declaration of paint colours
    private Paint paintBox = new Paint();
    private Paint paintMovingBox = new Paint();
    private Paint paintBlockPlacementPositions = new Paint();
    private Paint paintRobot1Touched = new Paint();
    private Paint paintRobot2Touched = new Paint();
    private Paint paintRobot1Busy = new Paint();
    private Paint paintRobot2Busy = new Paint();
    private Paint textPaint = new Paint();
//    private int width =

    public CharacterSprite(Bitmap bmp) {
        image = bmp;
//        x = screenWidth/2;
//        y = screenHeight/2;
        x = (screenWidth/2 - 2560/2) / 2;
        y = (screenHeight/2 - 1440/2) / 2;
        // set paint colour
        this.paintBox.setColor(Color.parseColor("#CD5C5C"));
        this.paintMovingBox.setColor(Color.RED);
        this.paintBlockPlacementPositions.setColor(Color.CYAN);
        this.paintRobot1Touched.setColor(Color.YELLOW);
        this.paintRobot2Touched.setColor(Color.YELLOW);
        this.paintRobot1Busy.setColor(Color.DKGRAY);
        this.paintRobot2Busy.setColor(Color.DKGRAY);
        this.textPaint.setColor(Color.BLACK);
        this.textPaint.setTextSize(20);

    }


    public void draw(Canvas canvas) {
//        image = Bitmap.createScaledBitmap(RosComNode.drawableBitmap, 2560, 1440, true);
//        canvas.drawBitmap(ImageScaleThread.image, x, y, null);
//        System.out.println(screenWidth + "  " + screenHeight);
//        System.out.println(x + "  " + y);
        canvas.drawBitmap(RosComNode.receivedImageBitmap, x, y, null);

//        canvas.drawRect(130,200,160,240, paintBox);


        for (int i = 0; i < RosComNode.rectArray.length; i++) {

            //AlvarMarker marker = markerList.get(i);
            //int markerId = marker.getId();

            if (RosComNode.rectArray[i] != null) {

                if (i == GameView.robot1MarkerId) {

                    if (GameView.robot1Selected) {
                        canvas.drawRect(RosComNode.rectArray[i], paintRobot1Touched);
                    } else if (GameView.robot1Busy) {
                        canvas.drawRect(RosComNode.rectArray[i], paintRobot1Busy);
                    } else {
                        canvas.drawRect(RosComNode.rectArray[i], paintBox);
                    }

                } else if (i == GameView.robot2MarkerId) {

                    if (GameView.robot2Selected) {
                        canvas.drawRect(RosComNode.rectArray[i], paintRobot1Touched);
                    } else if (GameView.robot2Busy) {
                        canvas.drawRect(RosComNode.rectArray[i], paintRobot1Busy);
                    } else {
                        canvas.drawRect(RosComNode.rectArray[i], paintBox);
                    }

                } else {
                    //logger.i("Array", "rect number: " + i + " " + rectArray[i].left + " " + rectArray[i].top + " " + rectArray[i].right + " " + rectArray[i].bottom);
                    canvas.drawRect(RosComNode.rectArray[i], paintBox);
                    canvas.drawText(Integer.toString(i), RosComNode.rectArray[i].centerX() - 5, RosComNode.rectArray[i].centerY() + 3, textPaint);
                    //canvas.drawRect(50,50,60,70, paint);
                    //canvas.drawRect(70,50,80,70, paint);
                }

                //canvas.drawRect(rectArray[i].left, rectArray[i].top, rectArray[i].right, rectArray[i].bottom, paintBox);


            }
        }
        if (GameView.markerTouched && GameView.nearestMarkerId != GameView.robot1MarkerId && GameView.nearestMarkerId != GameView.robot2MarkerId) {
            //if (markerTouched && nearestMarkerId!=robot1MarkerId && nearestMarkerId!=robot2MarkerId) {


            canvas.drawRect(GameView.touchX - GameView.boxHalfWidth, GameView.touchY - GameView.boxHalfWidth, GameView.touchX + GameView.boxHalfWidth, GameView.touchY + GameView.boxHalfWidth, paintMovingBox);
            //canvas.drawPoint(pixPos1X, pixPos1Y, paint);
            //canvas.drawPoint(pixPos2X, pixPos2Y, paint);
            if (!GameView.pos1Occupied) {
                canvas.drawCircle(GameView.pixPos1X, GameView.pixPos1Y, GameView.boxHalfWidth / 2, paintBlockPlacementPositions);
            }

            if (!GameView.pos2Occupied) {
                canvas.drawCircle(GameView.pixPos2X, GameView.pixPos2Y, GameView.boxHalfWidth / 2, paintBlockPlacementPositions);
            }
        }

        if (GameView.pos1Occupied) {
            canvas.drawRect(GameView.pixPos1X - GameView.boxHalfWidth / 2, GameView.pixPos1Y - GameView.boxHalfWidth / 2, GameView.pixPos1X + GameView.boxHalfWidth / 2, GameView.pixPos1Y + GameView.boxHalfWidth / 2, paintMovingBox);
        }

        if (GameView.pos2Occupied) {
            canvas.drawRect(GameView.pixPos2X - GameView.boxHalfWidth / 2, GameView.pixPos2Y - GameView.boxHalfWidth / 2, GameView.pixPos2X + GameView.boxHalfWidth / 2, GameView.pixPos2Y + GameView.boxHalfWidth / 2, paintMovingBox);
        }


    }


    public void update() {

//        System.out.println(screenWidth + "  " + screenHeight);
//            x += xVelocity;
//            y += yVelocity;
//            if ((x > screenWidth - image.getWidth()) || (x < 0)) {
//                xVelocity = xVelocity*-1;
//            }
//            if ((y > screenHeight - image.getHeight()) || (y < 0)) {
//                yVelocity = yVelocity*-1;
//            }

    }


}


