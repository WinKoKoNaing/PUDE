package com.porlar.techhousestudio.pude.firebasedb.fmodels;

import java.util.List;



public class Post {
    public String majorId;
    public String majorCode;
    public String postDate;
    public String content, majorName;
    public List<String> imageUrs;

    public Post() {
    }

    public Post(String majorId,String majorCode, String postDate, String content, String majorName, List<String> imageUrs) {
        this.majorId = majorId;
        this.postDate = postDate;
        this.content = content;
        this.majorName = majorName;
        this.imageUrs = imageUrs;
        this.majorCode = majorCode;
    }
}
