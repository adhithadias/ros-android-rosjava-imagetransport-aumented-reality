package org.ros.android.teleop_camera_pubsub;

import android.content.Context;
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

public class GameView2 extends SurfaceView implements SurfaceHolder.Callback, NodeMain {

    static int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    static int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private SecondaryThread thread;
    private CharacterSprite2 characterSprite;

    static int touchX;
    static int touchY;
    private String message;

    private static final int HEIGHT = 3;
    private static final int WIDTH = 3;
    private static final int LENGTH = 5;
    static final int MAX_BOXES = 15;
    static final int MAX_CYLINDERS = 5;

    public static Tile[][][] space = new Tile[HEIGHT][LENGTH][WIDTH];
    public static Tile[][][] prevSpace = new Tile[HEIGHT][LENGTH][WIDTH];
    public static int numberOfBoxesUsed = 0;
    public static int numberOfCylindersUsed = 0;
    public static LeftSelection leftSelection = LeftSelection.DELETE;
    public static LevelSelection levelSelection = LevelSelection.LEVEL1;
    public static int selectedlevel = 0;

    public GameView2(Context context) {
        super(context);

        getHolder().setFixedSize(1280, 800);
        getHolder().addCallback(this);

        initializeSpace();

        thread = new SecondaryThread(getHolder(), this);
        //imageScaleThread = new ImageScaleThread();

        setFocusable(true);
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("adhi_android/autonomous_command");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {

        final Publisher<Int8> robot1CommandPub = connectedNode.newPublisher("/robo_1_command", Int8._TYPE);
        final Publisher<std_msgs.String> robot1CommandPub2 = connectedNode.newPublisher("/marker_coordinate", std_msgs.String._TYPE);
        final Publisher<std_msgs.String> coordinatePublisher = connectedNode.newPublisher("/marker_coordinate", std_msgs.String._TYPE);


    }

    private void initializeSpace() {
        for (int i=0; i<space.length; i++){
            for (int j=0; j<space[0].length; j++){
                for (int k=0; k<space[0][0].length; k++){

                    space[i][j][k] = new Tile(50 + 50 * j + 5, 58*(2*i+1) + 150*i + 50*k + 5, BlockType.NULL);
//                    prevSpace[i][j][k] = space[i][j][k];
                }
            }
        }

//        prevSpace = space;
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

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inScreenDensity = 10;

        characterSprite = new CharacterSprite2(
                BitmapFactory.decodeResource(getResources(), R.drawable.buildinglevel),
                BitmapFactory.decodeResource(getResources(), R.drawable.recycle_bin),
                BitmapFactory.decodeResource(getResources(), R.drawable.box2),
                BitmapFactory.decodeResource(getResources(), R.drawable.cylinder),
                BitmapFactory.decodeResource(getResources(), R.drawable.undo2),
                BitmapFactory.decodeResource(getResources(), R.drawable.delete_all)
                );

//        BitmapFactory.decodeRe

        //imageScaleThread.setRunning(true);
        //imageScaleThread.start();
//        holder.setFixedSize(1280,800);

        thread.setRunning(true);
        thread.start();


        OnTouchListener onTouchListener = new OnTouchListener() {
            android.util.Log log;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // get touch coordinates
                // touch corrdinates are not the same as canvas coordinates
                //int x = (int) event.getX();
                //int y = (int) event.getY();

                // transform touch coordinates to canvas coordinates
                touchX = (int) (event.getX() / 2);
                touchY = (int) (event.getY() / 2);

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        System.out.println("ACTION_DOWN: " + touchX + "  " + touchY);
                        // Toast.makeText(getContext(), "action down", Toast.LENGTH_SHORT).show();

                        log.i("TAG", "touched down: (" + touchX + ", " + touchY + ")");
//                        message = coordinatePublisher.newMessage();
//                        message.setData("touched down: (" + touchX + ", " + touchY + ")");
//                        coordinatePublisher.publish(message);
                        break;

                    case MotionEvent.ACTION_MOVE:
//                        System.out.println("ACTION_MOVE: " + touchX + "  " + touchY);
//                        log.i("TAG", "moving: (" + touchX + ", " + touchY + ")");
//                        message = coordinatePublisher.newMessage();
//                        message.setData("moving: (" + touchX + ", " + touchY + ")");
//                        coordinatePublisher.publish(message);
                        break;

                    case MotionEvent.ACTION_UP:
                        System.out.println("ACTION_UP: " + touchX + "  " + touchY);

//                        log.i("TAG", "touched up: (" + touchX + ", " + touchY + ")");
//                        message = coordinatePublisher.newMessage();
//                        message.setData("touched up: (" + touchX + ", " + touchY + ")");
//                        coordinatePublisher.publish(message);

                        if(touchX > 0 && touchX<350){
                            if (touchY>58*0+150*0 && touchY<58*2+150*1){
                                levelSelection = LevelSelection.LEVEL1;
                                selectedlevel = 0;
                                System.out.println("Level 1 one selected");
                            }else if (touchY>58*2+150*1 && touchY<58*4+150*2){
                                levelSelection = LevelSelection.LEVEL2;
                                selectedlevel = 1;
                                System.out.println("Level 2 two selected");
                            }else{
                                levelSelection = LevelSelection.LEVEL3;
                                selectedlevel = 2;
                                System.out.println("Level 3 three selected");
                            }
                        }

                        if(touchX>1100 && touchX<1280){
                            if (touchY>160*0 && touchY<160*1){
                                leftSelection = LeftSelection.DELETE;
                            }else if (touchY>160*1 && touchY<160*2){
                                leftSelection = LeftSelection.BOX;
                            }else if (touchY>160*2 && touchY<160*3){
                                leftSelection = LeftSelection.CYLINDER;
                            }else if (touchY>160*3 && touchY<160*4){
                                leftSelection = LeftSelection.UNDO;
                            }else{
                                leftSelection = LeftSelection.DELETE_ALL;
                                clearSpace();
                            }
                        }

                        if (touchX>475 && touchX<475+500 && touchY>375 && touchY<375+300){
                            int interX = (touchX - 475) / 100;
                            int interY = (touchY - 375) / 100;

                            System.out.println("TOUCHED_COORDINATES: " + interX + " | " + interY);

                            if(leftSelection == LeftSelection.DELETE){
                                space[selectedlevel][interX][interY].setBlockType(BlockType.NULL);
                            }else if (leftSelection == LeftSelection.BOX){
                                space[selectedlevel][interX][interY].setBlockType(BlockType.BOX);
                            }else if (leftSelection == LeftSelection.CYLINDER){
                                space[selectedlevel][interX][interY].setBlockType(BlockType.CYLINDER);
                            }else if (leftSelection == LeftSelection.UNDO){

                            }
                        }

                        updateUsage();

                        break;
                }

                return true;
            }
        };

        this.setOnTouchListener(onTouchListener);

    }

    private void updateUsage(){
        int tempNumBoxes = 0;
        int tempNumCylinders = 0;

        for (int i=0; i<space.length; i++){
            for (int j=0; j<space[0].length; j++){
                for (int k=0; k<space[0][0].length; k++){

                    if (space[i][j][k].getBlockType()==BlockType.BOX){
                        tempNumBoxes++;
                    }else if(space[i][j][k].getBlockType()==BlockType.CYLINDER){
                        tempNumCylinders++;
                    }
                }
            }
        }
        numberOfBoxesUsed = tempNumBoxes;
        numberOfCylindersUsed = tempNumCylinders;
    }

    private void clearSpace(){
        for (int i=0; i<space.length; i++){
            for (int j=0; j<space[0].length; j++){
                for (int k=0; k<space[0][0].length; k++){

                    space[i][j][k].setBlockType(BlockType.NULL);
                }
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

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

    public void update(Canvas canvas) {
        characterSprite.update(canvas);

    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);
        if (canvas != null) {
            characterSprite.draw(canvas);

        }
    }
}
