package view.tm;

public class NextServiceTM {
    private String cid;
    private String v_number;
    private String c_name;
    private String c_adress;
    private String c_mo_num;
    private String c_email;
    private String ns_date;

    public NextServiceTM() {
    }

    public NextServiceTM(String cid, String v_number, String c_name, String c_adress, String c_mo_num, String c_email, String ns_date) {
        this.cid = cid;
        this.v_number = v_number;
        this.c_name = c_name;
        this.c_adress = c_adress;
        this.c_mo_num = c_mo_num;
        this.c_email = c_email;
        this.ns_date = ns_date;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getV_number() {
        return v_number;
    }

    public void setV_number(String v_number) {
        this.v_number = v_number;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_adress() {
        return c_adress;
    }

    public void setC_adress(String c_adress) {
        this.c_adress = c_adress;
    }

    public String getC_mo_num() {
        return c_mo_num;
    }

    public void setC_mo_num(String c_mo_num) {
        this.c_mo_num = c_mo_num;
    }

    public String getC_email() {
        return c_email;
    }

    public void setC_email(String c_email) {
        this.c_email = c_email;
    }

    public String getNs_date() {
        return ns_date;
    }

    public void setNs_date(String ns_date) {
        this.ns_date = ns_date;
    }

    @Override
    public String toString() {
        return "NextServiceTM{" +
                "cid='" + cid + '\'' +
                ", v_number='" + v_number + '\'' +
                ", c_name='" + c_name + '\'' +
                ", c_adress='" + c_adress + '\'' +
                ", c_mo_num='" + c_mo_num + '\'' +
                ", c_email='" + c_email + '\'' +
                ", ns_date='" + ns_date + '\'' +
                '}';
    }
}
