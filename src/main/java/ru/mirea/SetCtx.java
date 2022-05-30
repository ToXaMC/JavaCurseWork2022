package ru.mirea;

import org.springframework.context.ConfigurableApplicationContext;

public class SetCtx {

    private ConfigurableApplicationContext ct;

    private Start st;

    public SetCtx() {
    }

    public void setCt(ConfigurableApplicationContext ct1) {
        ct = ct1;
        MireaApplication.ctx = ct;
        Start.starts();
    }
}
