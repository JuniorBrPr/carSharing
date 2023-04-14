package carsharing.models;

public class Company {
    private int id;
    private String name;

    public Company() {
    }

    public Company(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%d. %s", id, name);
    }
}
