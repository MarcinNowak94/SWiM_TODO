package com.example.swim_todo;

import java.io.File;
import java.io.Serializable;

public class Task implements Serializable {
    private long ID;
    private String name;
    private String tags;
    private String priority;
    private Boolean isDone;
    private long dueDate;
    private File attachment;

    public Task(
            long ID,
            String name,
            String tags,
            String priority,
            long dueDate,
            Boolean isDone,
            File attachment) {
        this.ID = ID;
        this.name = name;
        this.tags = tags;
        this.priority = priority;
        this.isDone = isDone;
        this.dueDate = dueDate;
        this.attachment = attachment;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Boolean isDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public File getAttachment() {
        return attachment;
    }

    public void setAttachment(File attachment) {
        this.attachment = attachment;
    }
}
