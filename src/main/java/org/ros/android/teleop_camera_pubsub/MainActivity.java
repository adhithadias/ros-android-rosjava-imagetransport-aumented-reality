package org.ros.android.teleop_camera_pubsub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import org.ros.android.MessageCallable;
import org.ros.address.InetAddressFactory;
import org.ros.android.BitmapFromCompressedImage;
import org.ros.android.RosActivity;
//import org.ros.android.view.RosImageView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.util.Random;

import std_msgs.String;

import static android.content.ContentValues.TAG;

/**
 * @author adhithadias27@gmail.com (Adhitha Dias)
 */


//public class MainActivity extends RosActivity {
//
//    private CameraImageView cameraImageView;
//    private RosComNode rosComNode;
//    private GameView gameView;
//    private android.util.Log logger;
//    private boolean method = true;
//
//    public MainActivity() {
//        super("ImageTutorial", "ImageTutorial");
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.d(TAG, "onCreate: Testing 1 ...");
//
//        // remove title
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        if(method) {
//            gameView = new GameView(this);
//            setContentView(gameView);
//
//            rosComNode = new RosComNode();
//            rosComNode.setMessageToBitmapCallable(new BitmapFromCompressedImage());
//        }else{
//            setContentView(R.layout.activity_main);
//            cameraImageView = (CameraImageView) findViewById(R.id.image);
//            cameraImageView.setMessageToBitmapCallable(new BitmapFromCompressedImage());
//        }
//
//    }
//
//    @Override
//    protected void init(NodeMainExecutor nodeMainExecutor) {
//        Log.d(TAG, "init: Creating talkers and listeners...");
//
//        Log.d(TAG, "init: Executing all ros nodes");
//        NodeConfiguration nodeConfiguration =
//                NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress(),
//                        getMasterUri());
//
//        final int random = new Random().nextInt(61) + 20;
//
//        if (method) {
//            nodeMainExecutor.execute(rosComNode, nodeConfiguration.setNodeName("android/video_view" + random));
//            nodeMainExecutor.execute(gameView, nodeConfiguration.setNodeName("android/commands" + random));
//        }else{
//            nodeMainExecutor.execute(cameraImageView, nodeConfiguration.setNodeName("android/video_view" + random));
//        }
//        NodeConfiguration extraNodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
//        extraNodeConfiguration.setMasterUri(getMasterUri());
//        Log.d(TAG, "init: all nodes are executed!!!");
//    }
//}

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureButton();
    }

    void configureButton(){
        Button manualActivityButton = (Button) findViewById(R.id.manualActivityButton);

        manualActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ManualActivity.class));
            }
        });

    }
}
