package pl.marcinwroblewski.e_miasto.Events;

import java.util.Set;

/**
 * Created by Marcin Wróblewski on 16.10.2016.
 */

public class Event {


    private long id;
    private String name;
    private String desription;
    private String date;
    private String imagePath;
    private Set<String> intrests;


    public Event(String name, long id, String imagePath, Set<String> intrests, String description, String date) {
        this.name = name;
        this.id = id;
        this.imagePath = imagePath;
        this.intrests = intrests;
        this.desription = description;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImage() {
        return imagePath;
    }

    public void setImage(String imagePath) {
        this.imagePath = imagePath;
    }

    public Set<String> getIntrests() {
        return intrests;
    }

    public void setIntrests(Set<String> intrests) {
        this.intrests = intrests;
    }

    public String getDescription() {
        return desription;
    }

    public void setDesription(String desription) {
        this.desription = desription;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
