package ru.mirea;

import org.springframework.boot.SpringApplication;

public class StartSpring extends Thread {

    private final SetCtx comp;

    public StartSpring(SetCtx comp) {
        this.comp = comp;
    }

    @Override
    public void run() {
        synchronized (comp) {
            comp.setCt(SpringApplication.run(MireaApplication.class));
        }
    }
}
