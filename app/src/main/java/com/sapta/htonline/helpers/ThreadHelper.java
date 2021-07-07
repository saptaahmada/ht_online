package com.sapta.htonline.helpers;

public class ThreadHelper {

    public void setTimeOut(Runnable runnable, int millis) {
        new Thread(() -> {
            try {
                Thread.sleep(millis);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }
}
