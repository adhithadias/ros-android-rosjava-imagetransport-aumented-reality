package org.ros.android.teleop_camera_pubsub;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import static org.jboss.netty.buffer.ChannelBuffers.*;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;

import std_msgs.Int8;
import std_msgs.String;
import std_msgs.ByteMultiArray;

import static org.jboss.netty.buffer.ChannelBuffers.wrappedBuffer;

public class GameView2 extends SurfaceView implements SurfaceHolder.Callback, NodeMain {

    public static ChannelBuffer buffer;
    public static ChannelBuffer channelBuffer;

    static int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    static int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private SecondaryThread thread;
    private CharacterSprite2 characterSprite;

    static int touchX;
    static int touchY;

    private static final int HEIGHT = 3;
    private static final int WIDTH = 3;
    private static final int LENGTH = 5;
    static final int MAX_BOXES = 15;
    static final int MAX_CYLINDERS = 5;

    static final int CONSTRUCTION_SITE_TOP = 300;
    static final int CONSTRUCTION_SITE_LEFT = 475;

    public static Tile[][][] space = new Tile[HEIGHT][LENGTH][WIDTH];
    public static Tile[][][] prevSpace = new Tile[HEIGHT][LENGTH][WIDTH];
    public static int numberOfBoxesUsed = 0;
    public static int numberOfCylindersUsed = 0;
    public static LeftSelection leftSelection = LeftSelection.DELETE;
    public static LevelSelection levelSelection = LevelSelection.LEVEL1;
    public static int selectedlevel = 0;

    Publisher<std_msgs.String> coordinatePublisher;
    private String message;
    Publisher<std_msgs.ByteMultiArray> structurePublisher;
    private ByteMultiArray structureByteMultiArray;
    Publisher<std_msgs.String> structureStringPublisher;
    private String structureString;

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
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                System.out.println("ACTIONUP");
                break;

        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        System.out.println("CLICK PERFORMED");
        return super.performClick();
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {

        final Publisher<Int8> robot1CommandPub = connectedNode.newPublisher("/robo_1_command", Int8._TYPE);
        final Publisher<std_msgs.String> robot1CommandPub2 = connectedNode.newPublisher("/marker_coordinate", std_msgs.String._TYPE);
//        final Publisher<std_msgs.String> coordinatePublisher = connectedNode.newPublisher("/marker_coordinate", std_msgs.String._TYPE);
        coordinatePublisher = connectedNode.newPublisher("/marker_coordinate", std_msgs.String._TYPE);
        structurePublisher = connectedNode.newPublisher("/building/structure", ByteMultiArray._TYPE);
        structureStringPublisher = connectedNode.newPublisher("/buildingStructure", String._TYPE);

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
                getContext(),
                BitmapFactory.decodeResource(getResources(), R.drawable.buildinglevel),
                BitmapFactory.decodeResource(getResources(), R.drawable.recycle_bin),
                BitmapFactory.decodeResource(getResources(), R.drawable.box2),
                BitmapFactory.decodeResource(getResources(), R.drawable.cylinder),
                BitmapFactory.decodeResource(getResources(), R.drawable.undo2),
                BitmapFactory.decodeResource(getResources(), R.drawable.cross),
                BitmapFactory.decodeResource(getResources(), R.drawable.send),
                BitmapFactory.decodeResource(getResources(), R.drawable.background2),
                BitmapFactory.decodeResource(getResources(), R.drawable.help)
                );

//        characterSprite = new CharacterSprite2(getContext());

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
                touchX = (int) (event.getX() / screenWidth * 1280);
                touchY = (int) (event.getY() / screenHeight * 800);

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        break;

                    case MotionEvent.ACTION_MOVE:
                        break;

                    case MotionEvent.ACTION_UP:
//                        System.out.println("ACTION_UP: " + touchX + "  " + touchY);

//                        log.i("TAG", "touched up: (" + touchX + ", " + touchY + ")");
//                        message = coordinatePublisher.newMessage();
//                        message.setData("touched up: (" + touchX + ", " + touchY + ")");
//                        coordinatePublisher.publish(message);

                        // Select the level to place the boxes
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

                        /**
                         * selection from the left pane
                         * delete
                         * box
                         * cylinder
                         * undo or delete all
                         */
                        if(touchX>1100 && touchX<1280){
                            if (touchY>160*0 && touchY<160*1){
                                leftSelection = LeftSelection.DELETE;
//                                Toast.makeText(getContext(), "Delete selected", Toast.LENGTH_SHORT).show();
                            }else if (touchY>160*1 && touchY<160*2){
                                leftSelection = LeftSelection.BOX;
//                                Toast.makeText(getContext(), "Box selected", Toast.LENGTH_SHORT).show();
                            }else if (touchY>160*2 && touchY<160*3){
                                leftSelection = LeftSelection.CYLINDER;
//                                Toast.makeText(getContext(), "Cylinder selected", Toast.LENGTH_SHORT).show();
                                System.out.println("Cylinder pressed!");
                            }else if (touchY>160*3 && touchY<160*4){
                                leftSelection = LeftSelection.UNDO;
//                                Toast.makeText(getContext(), "Undo selected", Toast.LENGTH_SHORT).show();
                            }else{
                                leftSelection = LeftSelection.DELETE;
//                                Toast.makeText(getContext(), "Delete all selected", Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Confirm Deletion");
                                builder.setMessage("You are about to clear all the levels. Do you want to proceed ?");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getContext(), "Data cleared", Toast.LENGTH_SHORT).show();
                                        clearSpace();
                                    }
                                });

                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getContext(), "Operation cancelled", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                builder.show();
//                                clearSpace();
                            }
                        }

                        /**
                         * Place boxes in the construction area
                         * chosen level is shown
                         */
                        if (touchX>CONSTRUCTION_SITE_LEFT && touchX<CONSTRUCTION_SITE_LEFT+500 && touchY>CONSTRUCTION_SITE_TOP && touchY<CONSTRUCTION_SITE_TOP+300){
                            int interX = (touchX - CONSTRUCTION_SITE_LEFT) / 100;
                            int interY = (touchY - CONSTRUCTION_SITE_TOP) / 100;

//                            System.out.println("TOUCHED_COORDINATES: " + interX + " | " + interY);

                            if(leftSelection == LeftSelection.DELETE){
                                space[selectedlevel][interX][interY].setBlockType(BlockType.NULL);

                            }else if (leftSelection == LeftSelection.BOX){
                                if(numberOfBoxesUsed<MAX_BOXES){
                                    space[selectedlevel][interX][interY].setBlockType(BlockType.BOX);
                                }else{
                                    Toast.makeText(getContext(), "No more boxes left!", Toast.LENGTH_SHORT).show();
                                }

                            }else if (leftSelection == LeftSelection.CYLINDER){
                                if (numberOfCylindersUsed<MAX_CYLINDERS){
                                    space[selectedlevel][interX][interY].setBlockType(BlockType.CYLINDER);
                                }else{
                                    Toast.makeText(getContext(), "No more cylinders left!", Toast.LENGTH_SHORT).show();
                                }

                            }else if (leftSelection == LeftSelection.UNDO){

                            }
                        }

                        // Send button
                        if (touchX>CharacterSprite2.SEND_BUTTON_LEFT && touchX<CharacterSprite2.SEND_BUTTON_LEFT+CharacterSprite2.SEND_BUTTON_SIZE &&
                                touchY>CharacterSprite2.SEND_BUTTON_TOP && touchY<CharacterSprite2.SEND_BUTTON_TOP+CharacterSprite2.SEND_BUTTON_SIZE ){

//                            System.out.println("Send button pressed!");
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Confirm Data Sending");
                            builder.setMessage("You are about to send the structure to the server. Do you want to proceed ?");
                            builder.setCancelable(true);
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (coordinatePublisher!=null && coordinatePublisher.hasSubscribers()){
                                        System.out.println("I have " + coordinatePublisher.getNumberOfSubscribers() + " subscriber(s)");
                                        message = coordinatePublisher.newMessage();
                                        message.setData("You touched up: (" + touchX + ", " + touchY + ")");
                                        coordinatePublisher.publish(message);

                                        publishStructure();

                                        Toast.makeText(getContext(), "Structure sent to the server computer.", Toast.LENGTH_SHORT).show();
                                    }else{
                                        if (coordinatePublisher==null){
                                            Toast.makeText(getContext(), "Device not connected to ROSCORE. Please reconnect.", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(getContext(), "Server computer not connected (no subscribers). Data sending failed.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }
                            });

                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getContext(), "Data sending cancelled", Toast.LENGTH_SHORT).show();
                                }
                            });

                            builder.show();
                        }

                        // help button
                        if (touchX>CharacterSprite2.HELP_BUTTON_LEFT && touchX<CharacterSprite2.HELP_BUTTON_LEFT+CharacterSprite2.HELP_BUTTON_SIZE &&
                                touchY>CharacterSprite2.HELP_BUTTON_TOP && touchY<CharacterSprite2.HELP_BUTTON_TOP+CharacterSprite2.HELP_BUTTON_SIZE ){
//                            getContext().startActivity(new Intent(getContext(), HelpActivity.class));

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Help Dialog");
                            builder.setMessage("\u25CF Select the level from the right side of the window. \n" +
                                    "\u25CF Select the item you need to place from the left side. \n" +
                                    "\u25CF Touch the tiles you need to place the boxes or cylinders to place them on the construction area. \n" +
                                    "\u25CF Delete and undo buttons can be used accordingly. \n" +
                                    "\u25CF After you have finished placing the blocks click send button. \n" +
                                    "\u25CF Maximum number of blocks that can be used for the construction are shown above the placement area." +
                                    "\n\nContact adhithadias27@gmail.com for more details.");
                            builder.setCancelable(true);
                            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    Toast.makeText(getContext(), "Neutral button clicked", Toast.LENGTH_SHORT).show();
                                }
                            });
                            builder.show();
                        }

                        updateUsage();

                        break;
                }

                return true;
            }
        };

        this.setOnTouchListener(onTouchListener);
    }

    private void publishStructure() {
        structureString = structureStringPublisher.newMessage();

        java.lang.String str = "";

        for (int i=0; i<space.length; i++){
            for (int j=0; j<space[0].length; j++){
                for (int k=0; k<space[0][0].length; k++){

                    if (space[i][j][k].getBlockType()==BlockType.BOX){
                        str = str + "1 0 ";
                    }else if(space[i][j][k].getBlockType()==BlockType.CYLINDER){
                        str = str + "0 1 ";
                    }else{
                        str = str + "0 0 ";
                    }
                }
            }
        }

        structureString.setData(str);
        structureStringPublisher.publish(structureString);


//        structureByteMultiArray = structurePublisher.newMessage();
//        ChannelBuffer wrappedBuffer = wrappedBuffer(new byte[128], new byte[256]);
//        structureByteMultiArray.setData(wrappedBuffer);
//
//        short[] arr = new short[30];
//        for (short i=0; i<30; i++){
//            arr[i] = i;
//        }
//
//        channelBuffer = wrappedBuffer(new byte[] { 1, 2, 3, 4, 5 });
//
//        buffer = dynamicBuffer();
//        buffer.writeBytes(new byte[] { 1, 2, 3, 4, 5 });
//
//        ChannelBuffer buffers = ChannelBuffers.copiedBuffer();
//
//        structureByteMultiArray.setData(buffers);
//
//        structurePublisher.publish(structureByteMultiArray);

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
