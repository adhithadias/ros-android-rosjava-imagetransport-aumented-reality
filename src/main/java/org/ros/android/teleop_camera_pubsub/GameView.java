package org.ros.android.teleop_camera_pubsub;

import android.content.Context;
import android.content.SyncStatusObserver;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;

import std_msgs.Int8;
import std_msgs.String;

/**
 * Created by rushd on 7/5/2017.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback, NodeMain {
    private MainThread thread;
    private ImageScaleThread imageScaleThread;
    private CharacterSprite characterSprite;

    /**
     * variables to implement the logic
     */
    private String message;
    private Int8 robot1message;

    static int touchX;
    static int touchY;
    static int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    static int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    static int nearestMarkerId = -1;
    static double distance = 1000;
    static boolean markerTouched = false;
    final int distanceThreshod = 30;

    // robot selection
    static boolean robot1Selected = false;
    static boolean robot2Selected = false;
    static boolean robot1Busy = false;
    static boolean robot2Busy = false;
    static final int robot1MarkerId = 5;
    static final int robot2MarkerId = 20;

    // block placement positions declaration
    static final double pos1X = -0.5;
    static final double pos1Y = -0.2;
    static boolean pos1Occupied = false;

    static final double pos2X = -0.5;
    static final double pos2Y = 0.2;
    static boolean pos2Occupied = false;

    static final int boxHalfWidth = 20;
    static final int pixelsPerMeter = 350; // 350 for 1280x720
    static int pixPos1X = (int) (screenWidth / 4 + pixelsPerMeter * pos1X);
    static int pixPos1Y = (int) (screenHeight / 4 + pixelsPerMeter * pos1Y);

    static int pixPos2X = (int) (screenWidth / 4 + pixelsPerMeter * pos2X);
    static int pixPos2Y = (int) (screenHeight / 4 + pixelsPerMeter * pos2Y);

    public GameView(Context context) {

        super(context);

        getHolder().setFixedSize(1280, 800);
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        //imageScaleThread = new ImageScaleThread();

        setFocusable(true);

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        characterSprite = new CharacterSprite(BitmapFactory.decodeResource(getResources(), R.drawable.avdgreen));

        //imageScaleThread.setRunning(true);
        //imageScaleThread.start();
//        holder.setFixedSize(1280,800);
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                //imageScaleThread.setRunning(false);
                //imageScaleThread.join();
                thread.setRunning(false);
                thread.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void update() {
        characterSprite.update();

    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);
        if (canvas != null) {
            characterSprite.draw(canvas);

        }
    }


    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("adhi_android/command");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {

        final Publisher<Int8> robot1CommandPub = connectedNode.newPublisher("/robo_1_command", Int8._TYPE);
        final Publisher<std_msgs.String> robot1CommandPub2 = connectedNode.newPublisher("/marker_coordinate", std_msgs.String._TYPE);

        OnTouchListener onTouchListener = new OnTouchListener() {
            android.util.Log log;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // get touch coordinates
                // touch corrdinates are not the same as canvas coordinates
                //int x = (int) event.getX();
                //int y = (int) event.getY();

                // transform touch coordinates to canvas coordinates
                touchX = (int) (event.getX() / screenWidth * 1280);
                touchY = (int) (event.getY() / screenHeight * 800);


                //logger.i("TAG", "canvas: " + canvas.getWidth() + " " + canvas.getHeight() + " " + rightMargin + " " + bottomMargin);

//                nearestMarkerId = 0;
//                distance = 1000;
//                markerTouched = false;

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        System.out.println("ACTION_DOWN: " + touchX + "  " + touchY);
                        // Toast.makeText(getContext(), "action down", Toast.LENGTH_SHORT).show();

//                        logger.i("TAG", "touched down: (" + touchX + ", " + touchY + ")");
//                        message = coordinatePublisher.newMessage();
//                        message.setData("touched down: (" + touchX + ", " + touchY + ")");
//                        coordinatePublisher.publish(message);

                        for (int i = 1; i < RosComNode.rectArray.length; i++) {
                            if (RosComNode.rectArray[i] != null) {

//                                logger.i("TAG", "marker: " + i + " is not null " + rectArray[i].centerX() + " " + rectArray[i].centerY());
//                                message = coordinatePublisher.newMessage();
//                                message.setData("marker: " + i + " is not null " + rectArray[i].centerX() + " " + rectArray[i].centerY());
//                                coordinatePublisher.publish(message);

                                double dist = Math.sqrt(Math.pow(RosComNode.rectArray[i].centerX() - touchX, 2) + Math.pow(RosComNode.rectArray[i].centerY() - touchY, 2));

                                if (dist < boxHalfWidth && dist < distance) {
                                    nearestMarkerId = i;
                                    distance = dist;
                                    markerTouched = true;
                                }
                            }
                        }

                        if (markerTouched) {

//                            logger.i("TAG", "You touched marker: " + nearestMarkerId);
//                            message = coordinatePublisher.newMessage();
//                            message.setData("You touched marker: " + nearestMarkerId);
//                            coordinatePublisher.publish(message);

                            if (nearestMarkerId == robot1MarkerId) {
                                robot2Selected = false;
                                if (robot1Selected) {
                                    robot1Selected = false;
                                } else {
                                    robot1Selected = true;

                                }
                            }

                            if (nearestMarkerId == robot2MarkerId) {
                                robot1Selected = false;
                                if (robot2Selected) {
                                    robot2Selected = false;
                                } else {
                                    robot2Selected = true;
                                }
                            }
                        }

                        break;

                    case MotionEvent.ACTION_MOVE:
                        System.out.println("ACTION_MOVE: " + touchX + "  " + touchY);
//                        logger.i("TAG", "moving: (" + touchX + ", " + touchY + ")");
//                        message = coordinatePublisher.newMessage();
//                        message.setData("moving: (" + touchX + ", " + touchY + ")");
//                        coordinatePublisher.publish(message);
                        break;

                    case MotionEvent.ACTION_UP:
//                        System.out.println("ACTION_UP: " + touchX + "  " + touchY);

//                        logger.i("TAG", "touched up: (" + touchX + ", " + touchY + ")");
//                        message = coordinatePublisher.newMessage();
//                        message.setData("touched up: (" + touchX + ", " + touchY + ")");
//                        coordinatePublisher.publish(message);

                        if (markerTouched && nearestMarkerId != robot1MarkerId && nearestMarkerId != robot2MarkerId) {

                            // distances to unloading locations
                            double dist1 = Math.sqrt(Math.pow(pixPos1X - touchX, 2) + Math.pow(pixPos1Y - touchY, 2));
                            double dist2 = Math.sqrt(Math.pow(pixPos2X - touchX, 2) + Math.pow(pixPos2Y - touchY, 2));

                            // implement logic to send message to transfer blocks
                            if (dist1 < 20 && !pos1Occupied) {
                                pos1Occupied = true;
                                System.out.println("Publish message, nearest marker is : " + nearestMarkerId);
                                message = robot1CommandPub2.newMessage();
                                message.setData("" + nearestMarkerId);
                                robot1CommandPub2.publish(message);

                                robot1message = robot1CommandPub.newMessage();
                                robot1message.setData((byte) nearestMarkerId);
                                robot1CommandPub.publish(robot1message);

                            } else if (dist2 < 20 && !pos2Occupied) {
                                pos2Occupied = true;
                            }
                        }

                        nearestMarkerId = -1;
                        markerTouched = false;
                        distance = 1000;

                        break;
                }

                return true;
            }
        };

        this.setOnTouchListener(onTouchListener);

    }

    @Override
    public void onShutdown(Node node) {

    }

    @Override
    public void onShutdownComplete(Node node) {

    }

    @Override
    public void onError(Node node, Throwable throwable) {

    }

    public void onPause() {
        thread.setRunning(false);
//        thread = null;
    }

    public void onResume() {
        thread.setRunning(true);
//            thread.notify();
    }
}







