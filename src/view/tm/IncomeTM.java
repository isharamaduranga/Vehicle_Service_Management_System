package view.tm;

public class IncomeTM {
    private String serviceId;
    private String sDate;
    private double amount;

    public IncomeTM() {
    }

    public IncomeTM(String serviceId, String sDate, double amount) {
        this.serviceId = serviceId;
        this.sDate = sDate;
        this.amount = amount;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "IncomeTM{" +
                "serviceId='" + serviceId + '\'' +
                ", sDate='" + sDate + '\'' +
                ", amount=" + amount +
                '}';
    }
}
