package pl.marcinwroblewski.e_miasto.Events;

import java.io.File;
import java.util.Set;

/**
 * Created by Admin on 16.10.2016.
 */

public class Event {

    private String name;
    private long id;
    private File image;
    private Set<String> intrests;
    private String desription;

    public Event(String name, long id, File image, Set<String> intrests, String description) {
        this.name = name;
        this.id = id;
        this.image = image;
        this.intrests = intrests;
        this.desription = description;
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

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
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


}
