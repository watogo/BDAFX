package ch.hslu.bda.watogo.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Job {

    public final StringProperty spider;
    public final StringProperty jobID;
    public final StringProperty startTime;
    public final IntegerProperty numberOfEvents;
    public final StringProperty state;

    public Job(String spider, String jobID, String startTime, int numEvents, String state) {
        this.spider = new SimpleStringProperty(spider);
        this.jobID = new SimpleStringProperty(jobID);
        this.startTime = new SimpleStringProperty(startTime);
        this.numberOfEvents = new SimpleIntegerProperty(numEvents);
        this.state = new SimpleStringProperty(state);
    }
}
