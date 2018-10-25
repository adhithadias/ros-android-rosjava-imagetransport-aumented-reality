package org.ros.android.teleop_camera_pubsub;

import org.apache.commons.logging.Log;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import ar_track_alvar_msgs.AlvarMarkers;
import std_msgs.String;

public class DummyAlvarListener extends AbstractNodeMain {
    @Override
    public GraphName getDefaultNodeName() {
            return GraphName.of("adhi_android/alvarlistener");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        final Log log = connectedNode.getLog();
        Subscriber<ar_track_alvar_msgs.AlvarMarkers> subscriber = connectedNode.newSubscriber("ar_pose_marker", AlvarMarkers._TYPE);
        subscriber.addMessageListener(new MessageListener<AlvarMarkers>() {
            @Override
            public void onNewMessage(AlvarMarkers alvarMarkers) {
                log.info("I heard a alvar message: \"" + alvarMarkers.getMarkers().size() + "\"");
            }
        });

//        subscriber.addMessageListener(new MessageListener<String>() {
//            @Override
//            public void onNewMessage(std_msgs.String message) {
//                log.info("I heard a alvar message: \"" + message.getData() + "\"");
//            }
//        });
    }
}
