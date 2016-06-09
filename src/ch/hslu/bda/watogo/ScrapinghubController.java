/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.bda.watogo;

import ch.hslu.bda.watogo.model.Job;
import ch.hslu.bda.watogo.model.Settings;
import ch.hslu.bda.watogo.view.ContentController;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Niklaus
 */
public class ScrapinghubController {

    private final String key;
    private final String projectID;
    private ObservableList<Job> jobData = FXCollections.observableArrayList();
    private ObservableList<String> spiderList = FXCollections.observableArrayList();

    public ScrapinghubController(String key, String projectID) {
        this.key = key;
        this.projectID = projectID;
    }

    protected ObservableList<Job> retrieveJobs() {
        JSONObject response;
        try {
            Process p;
            String cmd = "curl -u " + this.key + ": \"https://app.scrapinghub.com/api/jobs/list.json?project="
                    + this.projectID + "\"";
            System.out.println(cmd);
            p = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            while (true) {
                String s = br.readLine();
                if (s == null) {
                    break;
                }
                response = new JSONObject(s);
                JSONArray jobsArray = response.getJSONArray("jobs");
                JSONObject jobJSON;
                String spiderName, startedTime, state, jobID;
                int itemsScraped;
                for (int j = 0; j < jobsArray.length(); j++) {
                    jobJSON = jobsArray.getJSONObject(j);
                    spiderName = jobJSON.getString("spider");
                    jobID = jobJSON.getString("id");

                    if (jobJSON.has("started_time")) {
                        startedTime = jobJSON.getString("started_time");
                    } else {
                        startedTime = "-";
                    }
                    itemsScraped = jobJSON.getInt("items_scraped");

                    if (jobJSON.has("close_reason")) {
                        state = jobJSON.getString("close_reason");
                    } else {
                        state = jobJSON.getString("state");
                    }

                    jobData.add(new Job(spiderName, jobID, startedTime, itemsScraped, state));
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Please fill in the correct Settings and restart the application",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return jobData;
    }

    protected ObservableList<String> getSpiderNames() {
        if (!jobData.isEmpty()) {
            for (Job element : jobData) {
                if (!spiderList.contains(element.spider.getValue())) {
                    spiderList.add(element.spider.getValue());
                }
            }
        }

        return spiderList;
    }

    protected void showLog(ContentController contentControl, Job job) {
        try {
            Process p;
            String cmd = "curl -u " + this.key + ": https://storage.scrapinghub.com/logs/" + job.jobID.getValue();
            System.out.println(cmd);
            p = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String totalS = "";
            while (true) {
                String s = br.readLine();
                if (s == null) {
                    break;
                }
                totalS += s + "\r\n";
            }
            contentControl.addText(totalS); //Add total String to logArea

            //spiderMessageLabel.setText(s);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Please fill in the correct Settings and restart the application",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public JSONArray getJsonArray(ContentController contentControl, Job job) {
        String totalS = "";
        JSONArray myJSONArray = null;
        try {
            Process p;
            String cmd = "curl -u " + key + ": https://storage.scrapinghub.com/items/" + job.jobID.getValue() + "?format=json";
            System.out.println(cmd);
            p = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            while (true) {
                String s = br.readLine();
                if (s == null) {
                    break;
                }
                totalS += s + "\r\n";
            }

            myJSONArray = new JSONArray(totalS);
            
            String myDateVonString = "";
            String myDateBisString = "";
            
            for (int i = 0; i < myJSONArray.length(); i++) {
                myDateVonString += myJSONArray.getJSONObject(i).get("Datum_von").toString()+"~";
                myDateBisString += myJSONArray.getJSONObject(i).get("Datum_bis").toString()+"~";
            }
            
            /*
            System.out.println("vor Parsen:");
            for(int i = 0; i < myJSONArray.length(); i++){
                
                System.out.println(myJSONArray.getJSONObject(i).toString());
            }
            */
            
            ParseDateToCommonFormat parser = new ParseDateToCommonFormat();
                
            try {
                Process pro;
                Process pro2;
                //String date = myJSONArray.getJSONObject(i).get("Datum_von").toString();
                //date = date.replace(' ', ';');
                
                myDateVonString = myDateVonString.replace(' ', ';');
                myDateVonString = myDateVonString.substring(0, myDateVonString.length()-1); //letztes zeichen abschneiden
                myDateBisString = myDateBisString.replace(' ', ';');
                myDateBisString = myDateBisString.substring(0, myDateBisString.length()-1); //letztes zeichen abschneiden
                        
                //String command[] = {"python", "scripts/parseDate.py", "--date=" + date};
                String command[] = {"python", "scripts/parseDate.py", "--date=" + myDateVonString};
                String command2[] = {"python", "scripts/parseDate.py", "--date=" + myDateBisString};
                pro = Runtime.getRuntime().exec(command);
                pro2 = Runtime.getRuntime().exec(command2);
                
                BufferedReader buffRead = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                BufferedReader buffRead2 = new BufferedReader(new InputStreamReader(pro2.getInputStream()));
                
                String s = "";
                String s2 = "";
                String datumVom = "";
                String datumBis = "";
                int count = 0;
                
                while (true) {
                    s = buffRead.readLine();
                    s2 = buffRead2.readLine();
                    
                    if (s == null || s2 == null) {
                        break;
                    }
                    datumVom = parser.parseDate(s);
                    datumBis = parser.parseDate(s2);
                    
                    if(!datumVom.equals("")){
                        myJSONArray.getJSONObject(count).put("Datum_von", datumVom);
                    }
                    if(!datumBis.equals("")){
                        myJSONArray.getJSONObject(count).put("Datum_bis", datumBis);
                    }
                    count++;
                    //totalS += result + "\r\n";
                }
                
                
                
                /*
                System.out.println("nach Parsen:");
                for(int i = 0; i < myJSONArray.length(); i++){
                    System.out.println(myJSONArray.getJSONObject(i).toString());
                }
                */
                
                //System.out.println("Normales Datum: " + myJSONArray.getJSONObject(i).get("Datum_von"));
                //System.out.println(result);
                //this.jLabel1.setText(this.jLabel1.getText() + " " + result);
            } catch (Exception ex) {
                System.out.println("Parse Error");
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    ex.toString(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        
        //System.out.println(myJSONArray.toString().substring(1, myJSONArray.toString().length()-1));
        
        return myJSONArray;
    }
}
