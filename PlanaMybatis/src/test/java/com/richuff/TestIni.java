package com.richuff;

import com.richuff.task.SchedulingTask;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.richuff.task.SchedulingTask.cron;

public class TestIni {
    @Autowired
    private SchedulingTask schedulingTask;

    @Test
    public void IniTest(){
        System.out.println("cron = " + cron);
    }
}
