package org.ros.android.teleop_camera_pubsub;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import org.ros.android.MessageCallable;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;

public class DummyListenerTextView<T> extends android.support.v7.widget.AppCompatTextView implements NodeMain {

    private String topicName;
    private String messageType;
    private MessageCallable<String, T> callable;

    public DummyListenerTextView(Context context) {
        super(context);
    }

    public DummyListenerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DummyListenerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setMessageToStringCallable(MessageCallable<String, T> callable) {
        this.callable = callable;
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("adhi_android/ros_text_view");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        Subscriber<T> subscriber = connectedNode.newSubscriber(topicName, messageType);
        subscriber.addMessageListener(new MessageListener<T>() {

            @Override
            public void onNewMessage(final T message) {
                if (callable != null) {
                    post(new Runnable() {

                        @Override
                        public void run() {
                            setText(callable.call(message));
                        }
                    });
                } else {
                    post(new Runnable() {

                        @Override
                        public void run() {
                            setText(message.toString());
                        }
                    });
                }
                postInvalidate();
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
