package xyz.oagueda.web.rest.errors;

public class DeleteCustomerWithDevicesException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public DeleteCustomerWithDevicesException() {
        super("Can not delete Customer with Devices", "Customer", "deleteCustomer");
    }
}
