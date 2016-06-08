/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.bda.watogo;

import ch.hslu.bda.watogo.model.Job;
import ch.hslu.bda.watogo.view.ContentController;
import java.io.BufferedReader;
import java.io.InputStreamReader;
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
}
