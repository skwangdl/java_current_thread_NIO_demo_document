package com.pandora.api.impl;

import com.pandora.api.Axe;
import com.pandora.api.Person;

public class Chinese implements Person {
    private Axe axe;

    public void setAxe(Axe axe){
        this.axe = axe;
    }

    public void useAxe() {
        System.out.println("i want to fire");
        System.out.println(axe.chop());
    }
}
