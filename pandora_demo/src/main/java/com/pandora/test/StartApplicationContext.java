package com.pandora.test;

import com.pandora.api.Person;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartApplicationContext {

    @Test
    public void testStartApplicationContext(){
        String springResourcePath = new StartApplicationContext().getPathStringByFileName("spring-config.xml");
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(springResourcePath);
        Person person = (Person) ctx.getBean("chinese");
        person.useAxe();
    }

    private String getPathStringByFileName(String filename){
        String fileName = this.getClass().getClassLoader().getResource(filename).getPath();
        return fileName;
    }
}
