package com.ta.slk.sistemlayanankegiatan.Model;

public class Comment {
    private String name;
    private String comment;
    private String date;
    private String photo;

    public Comment(String name, String comment, String date, String photo) {
        this.name = name;
        this.comment = comment;
        this.date = date;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }

    public String getPhoto() {
        return photo;
    }
}
