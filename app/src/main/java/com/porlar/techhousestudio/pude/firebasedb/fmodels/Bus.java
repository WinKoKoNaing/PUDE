package com.porlar.techhousestudio.pude.firebasedb.fmodels;

import java.util.List;

public class Bus {

    public String busName;
    public String busId;
    public List<BusPhoneNo> phoneNos;

    public Bus() {
    }

    public Bus(String busId, String busName, List<BusPhoneNo> phoneNos) {
        this.phoneNos = phoneNos;
        this.busName = busName;
        this.busId = busId;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public List<BusPhoneNo> getPhoneNos() {
        return phoneNos;
    }

    public void setPhoneNos(List<BusPhoneNo> phoneNos) {
        this.phoneNos = phoneNos;
    }
}
