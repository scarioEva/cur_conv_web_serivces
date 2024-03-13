/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package myRS;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author taha
 */
@Path("chgrate")
public class CHGrate {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CHGrate
     */
    public CHGrate() {
    }

    /**
     * updates the exchange rate of the provided currency
     * returns the status of the exchange rate update
     */
    @GET
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public String getJson(@QueryParam("code")String code, @QueryParam("rate")double rate) {
        
        //Boolean value to indicate whether exchange rate was correctly updated
        boolean updated=false;
        double oldRate=0;
        
        //Currency Conversion JSON Array values in a local file
        String CurJsonFile = "/usr/local/rates.json";
        
        //Read the currency rates list from file into a string
        String line = null;
        StringBuilder JSONstrB = new StringBuilder();   
        try {
            BufferedReader freader = new BufferedReader(new FileReader (CurJsonFile));
            while((line = freader.readLine()) != null) {
                JSONstrB.append(line);
            }
        }
        catch(java.io.FileNotFoundException fe) {
            System.err.println(fe);
        }
        catch(java.io.IOException ioe) {
            System.err.println(ioe);
        }
        
        //convert the currency list String into a JSON Array
        JSONObject JSONcurStr = new JSONObject(JSONstrB.toString());
        JSONArray curJSONarray = JSONcurStr.getJSONArray("CurrConv");
                
        //iterate through the JSON Array to find the currency requested for update
        //@QueryParam("code")String code, @QueryParam("rate")double rate
        for (int i=0; i<curJSONarray.length(); i++) {
            JSONObject curObj = curJSONarray.getJSONObject(i);
            if(code.equalsIgnoreCase(curObj.getString("code"))) {
                //get existing rate to display later
                oldRate = curObj.getDouble("rate");
                //update the user currency with the new rate
                curObj.put("rate", rate);
                updated=true;
                break;
            }
        }

        //write the updated array back to the local currency rates dile
        JSONObject rootJobj = new JSONObject();
        rootJobj.put("CurrConv", curJSONarray);
        try {
            FileWriter filew = new FileWriter(CurJsonFile);
            filew.write(rootJobj.toString());
            filew.flush();
            filew.close();
        }
        catch(java.io.FileNotFoundException fe) {
            System.err.println(fe);
        }
        catch(java.io.IOException ioe) {
            System.err.println(ioe);
        }        
        //send update code to requesting client
        if(updated)
            return "the exchange rate for " + code + 
                    " was updated from '" + oldRate + "' to '" + rate + "'";
        else
            return "failed to update the exchange rate for " + code + " !";
    }

    /**
     * PUT method for updating or creating an instance of CHGrate
     * @param content representation for the resource
     */
    @PUT
    @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
