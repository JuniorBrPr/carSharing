package carsharing.models;

public class Car {
    private int id;
    private String name;
    private int companyId;

    public Car() {
    }

    public Car(String name, int companyId) {
        this.name = name;
        this.companyId = companyId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return String.format("%d. %s", id, name);
    }
}