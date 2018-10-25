package org.ros.android.teleop_camera_pubsub;

import android.content.Context;
import android.content.res.Resources;
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
import sensor_msgs.CompressedImage;
import std_msgs.Int8;
import std_msgs.String;

public class RosComNode implements NodeMain {

    private String message;
    private Int8 robot1message;

    private MessageCallable<Bitmap, CompressedImage> callable;

    // declaration of paint colours
    private Paint paintBox = new Paint();
    private Paint paintMovingBox = new Paint();
    private Paint paintBlockPlacementPositions = new Paint();
    private Paint paintRobot1Touched = new Paint();
    private Paint paintRobot2Touched = new Paint();
    private Paint paintRobot1Busy = new Paint();
    private Paint paintRobot2Busy = new Paint();

    // Image bitmaps and canvas declaration
    public static Bitmap receivedImageBitmap;
    public static Bitmap drawableBitmap;
    private Canvas canvas;
    private int rightMargin;
    private int bottomMargin;
    private final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;;
    private final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
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

    private int pixPos1X = (int) (screenWidth/2 + pixelsPerMeter * pos1X);
    private int pixPos1Y = (int) (screenHeight/2 + pixelsPerMeter * pos1Y);

    private int pixPos2X = (int) (screenWidth/2 + pixelsPerMeter * pos2X);
    private int pixPos2Y = (int) (screenHeight/2 + pixelsPerMeter * pos2Y);

    private android.util.Log logger;

    List<AlvarMarker> markerList;
    public static Rect[] rectArray = new Rect[40];

    float x = 0;
    float y = 0;
    float z = 0;

    public void setMessageToBitmapCallable(MessageCallable<Bitmap, CompressedImage> callable) {
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
        bottomMargin = 1920;    //this.getBottom();
        rightMargin = 1080;     //this.getRight();

        final Publisher<std_msgs.String> coordinatePublisher = connectedNode.newPublisher("/coordinate", std_msgs.String._TYPE);
        final Publisher<std_msgs.Int8> robot1CommandPublisher = connectedNode.newPublisher("/robo_1_next_block", Int8._TYPE);
        final Publisher<std_msgs.Int8> robot2CommandPublisher = connectedNode.newPublisher("/robot_2_next_block", Int8._TYPE);

        Subscriber<sensor_msgs.CompressedImage> imageSubscriber = connectedNode.newSubscriber("/cv_camera/image_rect_color/compressed", sensor_msgs.CompressedImage._TYPE);
//        Subscriber<sensor_msgs.CompressedImage> imageSubscriber = connectedNode.newSubscriber("/cv_camera/image_resize/compressed", sensor_msgs.CompressedImage._TYPE);

        //Subscriber<T> imageSubscriber = connectedNode.newSubscriber("/cv_camera/image_raw/compressed", sensor_msgs.CompressedImage._TYPE);

        // this does not work
        //Subscriber<T> imageSubscriber = connectedNode.newSubscriber("/cv_camera/image_raw", Image._TYPE);

        imageSubscriber.addMessageListener(new MessageListener<CompressedImage>() {

            @Override
            public void onNewMessage(CompressedImage message) {

                receivedImageBitmap = callable.call(message);
//                drawableBitmap = receivedImageBitmap.copy(Bitmap.Config.ARGB_8888, true);
//                drawableBitmap = Bitmap.createScaledBitmap(receivedImageBitmap, 2560, 1440, true);
//                logger.i("BITMAP SIZE", receivedImageBitmap.getHeight() + "  " + receivedImageBitmap.getWidth());

            }
        });

        Subscriber<ar_track_alvar_msgs.AlvarMarkers> markersSubscriber = connectedNode.newSubscriber("ar_pose_marker", AlvarMarkers._TYPE);
        markersSubscriber.addMessageListener(new MessageListener<AlvarMarkers>() {
            @Override
            public void onNewMessage(AlvarMarkers alvarMarkers) {
//                log.info("I heard a alvar message: \"" + alvarMarkers.getMarkers().size() + "\"");

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

                        int left = (int) (screenWidth/4 + GameView.pixelsPerMeter * x - GameView.boxHalfWidth);
                        int top = (int) (screenHeight/4 + GameView.pixelsPerMeter * y - GameView.boxHalfWidth);
                        int right = (int) (screenWidth/4 + GameView.pixelsPerMeter * x + GameView.boxHalfWidth);
                        int bottom = (int) (screenHeight/4 + GameView.pixelsPerMeter * y + GameView.boxHalfWidth);

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
