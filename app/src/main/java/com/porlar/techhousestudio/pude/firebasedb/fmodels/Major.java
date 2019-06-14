package com.porlar.techhousestudio.pude.firebasedb.fmodels;

/**
 * Created by USER on 2/2/2019.
 */

public class Major {
    public String majorName,startName;
    public boolean available;

    public Major(String majorName, String startName,boolean available) {
        this.majorName = majorName;
        this.startName = startName;
        this.available = available;
    }
}
