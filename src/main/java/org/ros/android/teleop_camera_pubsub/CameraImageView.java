package org.ros.android.teleop_camera_pubsub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.commons.logging.Log;
import org.ros.android.MessageCallable;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;

import java.util.List;

import ar_track_alvar_msgs.AlvarMarker;
import ar_track_alvar_msgs.AlvarMarkers;
import sensor_msgs.Image;
import std_msgs.Int8;
import std_msgs.String;

public class CameraImageView  extends android.support.v7.widget.AppCompatImageView implements NodeMain {

    private String message;
    private Int8 robot1message;

    private MessageCallable<Bitmap, sensor_msgs.CompressedImage> callable;

    // declaration of paint colours
    private Paint paintBox = new Paint();
    private Paint paintMovingBox = new Paint();
    private Paint paintBlockPlacementPositions = new Paint();
    private Paint paintRobot1Touched = new Paint();
    private Paint paintRobot2Touched = new Paint();
    private Paint paintRobot1Busy = new Paint();
    private Paint paintRobot2Busy = new Paint();

    // Image bitmaps and canvas declaration
    private Bitmap receivedImageBitmap;
    private Bitmap drawableBitmap;
    private Canvas canvas;
    private int rightMargin;
    private int bottomMargin;
    private final int canvasX = 320;
    private final int canvasY = 180;
    private final int scaleFactor = 4;
    private final int pixelsPerMeter = 82; // 350 for 1280x720
    private final int boxHalfWidth = 5;

    private int touchX;
    private int touchY;

    private int nearestMarkerId = -1;
    private double distance = 1000;
    private boolean markerTouched = false;

    // robot selection
    private boolean robot1Selected = false;
    private boolean robot2Selected = false;
    private boolean robot1Busy = false;
    private boolean robot2Busy = false;
    private final int robot1MarkerId = 5;
    private final int robot2MarkerId = 20;

    // block placement positions declaration
    private final double pos1X = -0.5;
    private final double pos1Y = -0.2;
    private boolean pos1Occupied = false;

    private final double pos2X = -0.5;
    private final double pos2Y = 0.2;
    private boolean pos2Occupied = false;

    private int pixPos1X = (int) (canvasX/2 + pixelsPerMeter * pos1X);
    private int pixPos1Y = (int) (canvasY/2 + pixelsPerMeter * pos1Y);

    private int pixPos2X = (int) (canvasX/2 + pixelsPerMeter * pos2X);
    private int pixPos2Y = (int) (canvasY/2 + pixelsPerMeter * pos2Y);

    private android.util.Log logger;

    List<AlvarMarker> markerList;
    Rect[] rectArray = new Rect[40];

    float x = 0;
    float y = 0;
    float z = 0;

    public CameraImageView(Context context) {
        super(context);
    }

    public CameraImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setMessageToBitmapCallable(MessageCallable<Bitmap, sensor_msgs.CompressedImage> callable) {
        this.callable = callable;
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("adhi_android/ros_image_view");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {

        final Log log = connectedNode.getLog();

        // set paint colour
        this.paintBox.setColor(Color.parseColor("#CD5C5C"));
        this.paintMovingBox.setColor(Color.RED);
        this.paintBlockPlacementPositions.setColor(Color.CYAN);
        this.paintRobot1Touched.setColor(Color.YELLOW);
        this.paintRobot2Touched.setColor(Color.YELLOW);
        this.paintRobot1Busy.setColor(Color.DKGRAY);
        this.paintRobot2Busy.setColor(Color.DKGRAY);

        // get image height and width in pixels 1920 and 1080 respectively
        bottomMargin = this.getBottom();
        rightMargin = this.getRight();

        final Publisher<std_msgs.String> coordinatePublisher = connectedNode.newPublisher("/coordinate", std_msgs.String._TYPE);
        final Publisher<std_msgs.Int8> robot1CommandPublisher = connectedNode.newPublisher("/robo_1_next_block", Int8._TYPE);
        final Publisher<std_msgs.Int8> robot2CommandPublisher = connectedNode.newPublisher("/robot_2_next_block", Int8._TYPE);

        //Subscriber<T> imageSubscriber = connectedNode.newSubscriber("/cv_camera/image_rect_color/compressed", sensor_msgs.CompressedImage._TYPE);
        Subscriber<sensor_msgs.CompressedImage> imageSubscriber = connectedNode.newSubscriber("/cv_camera/image_resize/compressed", sensor_msgs.CompressedImage._TYPE);

        //Subscriber<T> imageSubscriber = connectedNode.newSubscriber("/cv_camera/image_raw/compressed", sensor_msgs.CompressedImage._TYPE);

        // this does not work
        //Subscriber<T> imageSubscriber = connectedNode.newSubscriber("/cv_camera/image_raw", Image._TYPE);

        imageSubscriber.addMessageListener(new MessageListener<sensor_msgs.CompressedImage>() {

            @Override
            public void onNewMessage(final sensor_msgs.CompressedImage message) {

                post(new Runnable() {

                    @Override
                    public void run() {
                        receivedImageBitmap = callable.call(message);
                        //Bitmap resizedBitmap = Bitmap.createBitmap(receivedImageBitmap,0,0,1280,720);
                        //Bitmap.createBitmap(receivedImageBitmap, 0, 0, width, height, matrix, false);
                        //Bitmap resizedBitmap = Bitmap.createScaledBitmap(receivedImageBitmap, canvasX, canvasY, false);
                        drawableBitmap = receivedImageBitmap.copy(Bitmap.Config.ARGB_8888, true);
                        //drawableBitmap = Bitmap.createScaledBitmap(callable.call(message), canvasX, canvasY, false).copy(Bitmap.Config.ARGB_8888, true);
                        canvas = new Canvas(drawableBitmap);

                        //logger.i("BITMAP SIZE", receivedImageBitmap.getHeight() + "  " + receivedImageBitmap.getWidth());

//                        canvas.drawRect(50,50,60,70, paint);
                        //canvas.drawRect(180,80,220,120, paintRobot1Busy);
                        //canvas.drawText("Adhitha", 200, 100, paintBox); // paint defines the text color, stroke width, size

                        for (int i = 0; i < rectArray.length; i++) {

                            //AlvarMarker marker = markerList.get(i);
                            //int markerId = marker.getId();

                            if (rectArray[i] != null) {

                                if (i == robot1MarkerId) {

                                    if (robot1Selected) {
                                        canvas.drawRect(rectArray[i], paintRobot1Touched);
                                    } else if (robot1Busy) {
                                        canvas.drawRect(rectArray[i], paintRobot1Busy);
                                    }else{
                                        canvas.drawRect(rectArray[i], paintBox);
                                    }

                                } else if (i == robot2MarkerId) {

                                    if (robot2Selected) {
                                        canvas.drawRect(rectArray[i], paintRobot1Touched);
                                    } else if (robot2Busy) {
                                        canvas.drawRect(rectArray[i], paintRobot1Busy);
                                    }else{
                                        canvas.drawRect(rectArray[i], paintBox);
                                    }

                                } else{
                                    //logger.i("Array", "rect number: " + i + " " + rectArray[i].left + " " + rectArray[i].top + " " + rectArray[i].right + " " + rectArray[i].bottom);
                                    canvas.drawRect(rectArray[i], paintBox);
                                    canvas.drawText(Integer.toString(i), rectArray[i].centerX()-5, rectArray[i].centerY()+3, paintRobot1Busy);
                                    //canvas.drawRect(50,50,60,70, paint);
                                    //canvas.drawRect(70,50,80,70, paint);
                                }

                                //canvas.drawRect(rectArray[i].left, rectArray[i].top, rectArray[i].right, rectArray[i].bottom, paintBox);


                            }
                        }
                        if (markerTouched && nearestMarkerId!=robot1MarkerId && nearestMarkerId!=robot2MarkerId) {
                            //if (markerTouched && nearestMarkerId!=robot1MarkerId && nearestMarkerId!=robot2MarkerId) {


                            canvas.drawRect(touchX - boxHalfWidth, touchY - boxHalfWidth, touchX + boxHalfWidth, touchY + boxHalfWidth, paintMovingBox);
                            //canvas.drawPoint(pixPos1X, pixPos1Y, paint);
                            //canvas.drawPoint(pixPos2X, pixPos2Y, paint);
                            if (!pos1Occupied) {
                                canvas.drawCircle(pixPos1X, pixPos1Y, 3, paintBlockPlacementPositions);
                            }

                            if (!pos2Occupied) {
                                canvas.drawCircle(pixPos2X, pixPos2Y, 3, paintBlockPlacementPositions);
                            }
                        }

                        if (pos1Occupied) {
                            canvas.drawRect(pixPos1X - 3, pixPos1Y - 3, pixPos1X + 3, pixPos1Y + 3, paintMovingBox);
                        }

                        if (pos2Occupied) {
                            canvas.drawRect(pixPos2X - 3, pixPos2Y - 3, pixPos2X + 3, pixPos2Y + 3, paintMovingBox);
                        }

                        setImageBitmap(drawableBitmap);
                    }
                });
                postInvalidate();
            }
        });

        Subscriber<ar_track_alvar_msgs.AlvarMarkers> markersSubscriber = connectedNode.newSubscriber("ar_pose_marker", AlvarMarkers._TYPE);
        markersSubscriber.addMessageListener(new MessageListener<AlvarMarkers>() {
            @Override
            public void onNewMessage(AlvarMarkers alvarMarkers) {
                //log.info("I heard a alvar message: \"" + alvarMarkers.getMarkers().size() + "\"");

                markerList = alvarMarkers.getMarkers();

                //Rect rect = new Rect();
                //rect.set(430, 50, 470, 70);
                //rectArray[1] = rect;

                for (int i = 0; i < markerList.size(); i++) {

                    AlvarMarker marker = markerList.get(i);
                    int markerId = marker.getId();

                    if (markerId > 0 && markerId <= 40) {
                        x = (float) marker.getPose().getPose().getPosition().getX();
                        y = (float) marker.getPose().getPose().getPosition().getY();
                        z = (float) marker.getPose().getPose().getPosition().getZ();

                        x = x / z * 3;
                        y = y / z * 3;

                        //log.info("Marker: \"" + markerId + "  " + x + " " + y + "\"");

                        int left = (int) (canvasX/2 + pixelsPerMeter * x - boxHalfWidth);
                        int top = (int) (canvasY/2 + pixelsPerMeter * y - boxHalfWidth);
                        int right = (int) (canvasX/2 + pixelsPerMeter * x + boxHalfWidth);
                        int bottom = (int) (canvasY/2 + pixelsPerMeter * y + boxHalfWidth);

                        if (rectArray[markerId] != null) {
                            rectArray[markerId].set(left, top, right, bottom);
                        } else {
                            rectArray[markerId] = new Rect(left, top, right, bottom);
                        }

                        //rectArray[markerId].set(left, top, right, bottom);
                    }
                }
            }

        });

        OnTouchListener onTouchListener = new OnTouchListener() {
            android.util.Log log;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // get touch coordinates
                // touch corrdinates are not the same as canvas coordinates
                //int x = (int) event.getX();
                //int y = (int) event.getY();

                // transform touch coordinates to canvas coordinates
                touchX = (int) (event.getX() * ((float) canvasX / rightMargin));
                touchY = (int) (event.getY() * ((float) canvasY / bottomMargin));

//                logger.i("TAG", "canvas: " + canvas.getWidth() + " " + canvas.getHeight() + " " + rightMargin + " " + bottomMargin);

//                nearestMarkerId = 0;
//                distance = 1000;
//                markerTouched = false;

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        // Toast.makeText(getContext(), "action down", Toast.LENGTH_SHORT).show();

//                        logger.i("TAG", "touched down: (" + touchX + ", " + touchY + ")");
//                        message = coordinatePublisher.newMessage();
//                        message.setData("touched down: (" + touchX + ", " + touchY + ")");
//                        coordinatePublisher.publish(message);

                        for (int i = 1; i < rectArray.length; i++) {
                            if (rectArray[i] != null) {

//                                logger.i("TAG", "marker: " + i + " is not null " + rectArray[i].centerX() + " " + rectArray[i].centerY());
//                                message = coordinatePublisher.newMessage();
//                                message.setData("marker: " + i + " is not null " + rectArray[i].centerX() + " " + rectArray[i].centerY());
//                                coordinatePublisher.publish(message);

                                double dist = Math.sqrt(Math.pow(rectArray[i].centerX() - touchX, 2) + Math.pow(rectArray[i].centerY() - touchY, 2));

                                if (dist < 7 && dist < distance) {
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
//                        logger.i("TAG", "moving: (" + touchX + ", " + touchY + ")");
//                        message = coordinatePublisher.newMessage();
//                        message.setData("moving: (" + touchX + ", " + touchY + ")");
//                        coordinatePublisher.publish(message);
                        break;

                    case MotionEvent.ACTION_UP:

//                        logger.i("TAG", "touched up: (" + touchX + ", " + touchY + ")");
//                        message = coordinatePublisher.newMessage();
//                        message.setData("touched up: (" + touchX + ", " + touchY + ")");
//                        coordinatePublisher.publish(message);

                        if (markerTouched && nearestMarkerId != robot1MarkerId && nearestMarkerId != robot2MarkerId) {
                            double dist1 = Math.sqrt(Math.pow(pixPos1X - touchX, 2) + Math.pow(pixPos1Y - touchY, 2));
                            double dist2 = Math.sqrt(Math.pow(pixPos2X - touchX, 2) + Math.pow(pixPos2Y - touchY, 2));

                            // implement logic to send message to transfer blocks
                            if (dist1 < 20 && !pos1Occupied) {
                                pos1Occupied = true;
//                                message = coordinatePublisher.newMessage();
//                                message.setData("touched up: (" + touchX + ", " + touchY + ")");
//                                coordinatePublisher.publish(message);
                                robot1message.setData((byte) nearestMarkerId);
                                robot1CommandPublisher.publish(robot1message);
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

}