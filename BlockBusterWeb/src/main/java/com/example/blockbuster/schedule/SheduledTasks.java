package com.example.blockbuster.schedule;

import com.example.blockbuster.BlockBusterApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.boot.devtools.restart.Restarter;

@Component
public class SheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(SheduledTasks.class);

    @Scheduled(cron = "0 0 0 * * *")
    public void endSession() {
        Restarter.getInstance().restart();
    }
}
