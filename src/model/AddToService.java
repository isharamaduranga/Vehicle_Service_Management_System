package model;

public class AddToService {
    private String sid;
    private String vnumber;
    private String model;
    private String modelname;
    private String nic;
    private String oname;
    private String stype;
    private String date;

    public AddToService() {
    }

    public AddToService(String sid, String vnumber, String model, String modelname, String nic, String oname, String stype, String date) {
        this.sid = sid;
        this.vnumber = vnumber;
        this.model = model;
        this.modelname = modelname;
        this.nic = nic;
        this.oname = oname;
        this.stype = stype;
        this.date = date;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getVnumber() {
        return vnumber;
    }

    public void setVnumber(String vnumber) {
        this.vnumber = vnumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModelname() {
        return modelname;
    }

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getOname() {
        return oname;
    }

    public void setOname(String oname) {
        this.oname = oname;
    }

    public String getStype() {
        return stype;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "AddToService{" +
                "sid='" + sid + '\'' +
                ", vnumber='" + vnumber + '\'' +
                ", model='" + model + '\'' +
                ", modelname='" + modelname + '\'' +
                ", nic='" + nic + '\'' +
                ", oname='" + oname + '\'' +
                ", stype='" + stype + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
