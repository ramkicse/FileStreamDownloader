/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramki.rest;

import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author ramki
 */
@ApplicationScoped

public class Data {
    private long totalCompleted;
    private long size;
    private String name;

    public Data() {
        totalCompleted=0;
        size=0;
    }
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    

    public long getTotalCompleted() {
        return totalCompleted;
    }

    public void setTotalCompleted(long totalCompleted) {
        this.totalCompleted = totalCompleted;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Data{" + "totalCompleted=" + (totalCompleted/(1000*1000))+" MB ("+totalCompleted+ " bytes), size=" + + (size/(1000*1000))+" MB ("+size+ " bytes), name=" + name + '}';
    }

   
    
}
