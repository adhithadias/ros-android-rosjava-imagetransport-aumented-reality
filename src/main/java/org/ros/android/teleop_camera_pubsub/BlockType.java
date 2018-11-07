package org.ros.android.teleop_camera_pubsub;

public enum BlockType {
    NULL(0), BOX(1), CYLINDER(2), BRIDGE_VERTICAL(3), BRIDGE_HORIZONTAL(4);

    final int type;

    BlockType(int type){
        this.type = type;
    }
}
