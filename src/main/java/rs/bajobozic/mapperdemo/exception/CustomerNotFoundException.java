package rs.bajobozic.mapperdemo.exception;

public class CustomerNotFoundException extends RuntimeException {
    private String customerId;

    public CustomerNotFoundException(String message, Throwable e) {
        super(message, e);
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }

    public CustomerNotFoundException(String message, String customerId) {
        super(message);
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

}
