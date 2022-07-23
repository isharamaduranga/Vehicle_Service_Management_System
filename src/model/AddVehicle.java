package model;

public class AddVehicle {
    private String v_num;
    private String nic;
    private String name;
    private String model;
    private String model_num;
    private String fuelType;
    private String date;

    public AddVehicle() {
    }

    public AddVehicle(String v_num, String nic, String name, String model, String model_num, String fuelType, String date) {
        this.v_num = v_num;
        this.nic = nic;
        this.name = name;
        this.model = model;
        this.model_num = model_num;
        this.fuelType = fuelType;
        this.date = date;
    }

    public String getV_num() {
        return v_num;
    }

    public void setV_num(String v_num) {
        this.v_num = v_num;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModel_num() {
        return model_num;
    }

    public void setModel_num(String model_num) {
        this.model_num = model_num;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "AddVehicle{" +
                "v_num='" + v_num + '\'' +
                ", nic='" + nic + '\'' +
                ", name='" + name + '\'' +
                ", model='" + model + '\'' +
                ", model_num='" + model_num + '\'' +
                ", fuelType='" + fuelType + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
