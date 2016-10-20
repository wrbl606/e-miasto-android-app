package pl.marcinwroblewski.e_miasto;

import java.io.File;
import java.util.Date;

/**
 * Created by wrbl on 19.10.16.
 */

public class Complain {

    private long id;
    private String title, content;
    private File photo;
    private boolean accepted;
    private Date dateCreated, dateSolved;


    public Complain(long id, String title, String content, File photo, boolean accepted, Date dateCreated, Date dateSolved) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.photo = photo;
        this.accepted = accepted;
        this.dateCreated = dateCreated;
        this.dateSolved = dateSolved;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public File getPhoto() {
        return photo;
    }

    public void setPhoto(File photo) {
        this.photo = photo;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateSolved() {
        return dateSolved;
    }

    public void setDateSolved(Date dateSolved) {
        this.dateSolved = dateSolved;
    }
}
