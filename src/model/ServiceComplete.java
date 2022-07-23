package model;

import javafx.scene.control.SingleSelectionModel;
import view.tm.IncomeTM;

public class ServiceComplete {
    private String sid;
    private String v_number;
    private String model;
    private String cid;
    private String cname;
    private String service;
    private String item_replace;
    private String result;
    private String other;
    private String date;
    private String charge;

    public ServiceComplete(String sid, String date, double charge) {

        this.sid=sid;
        this.date=date;
        this.charge= String.valueOf(charge);

    }

    public ServiceComplete(String sid, String v_number, String model, String cid, String cname, String service, String item_replace, String result, String other, String date, String charge) {
        this.sid = sid;
        this.v_number = v_number;
        this.model = model;
        this.cid = cid;
        this.cname = cname;
        this.service = service;
        this.item_replace = item_replace;
        this.result = result;
        this.other = other;
        this.date = date;
        this.charge = charge;
    }


    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getV_number() {
        return v_number;
    }

    public void setV_number(String v_number) {
        this.v_number = v_number;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getItem_replace() {
        return item_replace;
    }

    public void setItem_replace(String item_replace) {
        this.item_replace = item_replace;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    @Override
    public String toString() {
        return "ServiceComplete{" +
                "sid='" + sid + '\'' +
                ", v_number='" + v_number + '\'' +
                ", model='" + model + '\'' +
                ", cid='" + cid + '\'' +
                ", cname='" + cname + '\'' +
                ", service='" + service + '\'' +
                ", item_replace='" + item_replace + '\'' +
                ", result='" + result + '\'' +
                ", other='" + other + '\'' +
                ", date='" + date + '\'' +
                ", charge='" + charge + '\'' +
                '}';
    }
}
