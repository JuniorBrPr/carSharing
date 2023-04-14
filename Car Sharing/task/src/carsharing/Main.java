package carsharing;

public class Main {

    public static void main(String[] args) {
        CarSharing carSharing = new CarSharing(args.length > 0 ? args[1] : "carSharing");
        carSharing.start();
    }
}
