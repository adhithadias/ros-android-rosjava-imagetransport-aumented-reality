package org.ros.android.teleop_camera_pubsub;

public class Tile {
    private BlockType blockType;
    private int coordinateX, coordinateY;

    public BlockType getBlockType() {
        return blockType;
    }

    public void setBlockType(BlockType blockType) {
        this.blockType = blockType;
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(int coordinateX) {
        this.coordinateX = coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(int coordinateY) {
        this.coordinateY = coordinateY;
    }

    Tile(int x, int y, BlockType blockType){
        this.coordinateX = x;
        this.coordinateY = y;
        this.blockType = blockType;
    }
}
