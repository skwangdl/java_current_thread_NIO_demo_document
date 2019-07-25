package com.pandora.test;

import com.taobao.hsf.standalone.HSFEasyStarter;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class StartPandora {

    @Test
    public void testStartPandora() throws IOException {
        File directory = new File("");
        String courseFile = directory.getCanonicalPath() ;
        System.out.println(courseFile);
        HSFEasyStarter.start(courseFile + "pandora_demo", "2019-05-stable");
    }
}
