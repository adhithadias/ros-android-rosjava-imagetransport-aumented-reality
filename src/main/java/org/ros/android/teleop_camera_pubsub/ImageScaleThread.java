package org.ros.android.teleop_camera_pubsub;

import android.graphics.Bitmap;

public class ImageScaleThread extends Thread {
    private boolean running;
    public static Bitmap image;
    private int targetFPS = 15;
    private double averageFPS;

    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount =0;
        long targetTime = 1000/targetFPS;

        while(running){
            startTime = System.nanoTime();

            try {
                image = Bitmap.createScaledBitmap(RosComNode.receivedImageBitmap, 2560, 1440, true);
            }catch (Exception e){
                e.printStackTrace();
            }finally{

            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime-timeMillis;

            try{
                this.sleep(waitTime);
            }catch(Exception e){}

            totalTime += System.nanoTime()-startTime;
            frameCount++;
            if(frameCount == targetFPS)
            {
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount =0;
                totalTime = 0;
                System.out.println("scaleImg "+ averageFPS);
            }
        }
    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
    }
}
