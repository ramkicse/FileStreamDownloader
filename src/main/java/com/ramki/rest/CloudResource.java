/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramki.rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;



import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 * REST Web Service
 *
 * @author ramki
 */
@Path("cloud")
@RequestScoped
public class CloudResource {

    @Context
    private UriInfo context;
    @Inject
    Data data;

    /**
     * Creates a new instance of CloudResource
     */
    public CloudResource() {
    }

    /**
     * Retrieves representation of an instance of com.ramki.CloudResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("{size}")
    @Produces("text/html")
    public String getHtml(@PathParam("size") int size) {
        data.setSize(size*1000*1000);
        data.setName("data.zip");

        return "good : "+data;
    }


    /**
     * PUT method for updating or creating an instance of CloudResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
     @GET
    @Path("reset")
    @Produces("text/html")
    public String putHtml() {
        
             data.setTotalCompleted(0);
             data.setSize(0);
             data.setName(null);
        
             
         
         return "good : "+data;
    }

     
     @GET
    @Path("seek/{size}")
    @Produces("text/html")
    public String changeTotalComplte(@PathParam("size")long size) {
        
             data.setTotalCompleted(size);
           
             
         
         return "good : "+data;
    }
    
    @GET
    @Path("now")
    @Produces("text/html")
    public String getCurrentPointer() {
        
            
           
             
         
         return "Current Pointer : "+(data.getTotalCompleted()/(1000*1000))+" MB ("+data.getTotalCompleted()+" bytes)";
    }

}
