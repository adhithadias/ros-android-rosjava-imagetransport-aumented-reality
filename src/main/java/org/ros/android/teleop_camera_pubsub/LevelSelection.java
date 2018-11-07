package org.ros.android.teleop_camera_pubsub;

public enum LevelSelection {
    LEVEL1(0), LEVEL2(1), LEVEL3(2);

    final int level;

    LevelSelection(int level){
        this.level = level;
    }
}
