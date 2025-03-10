package xyz.oagueda.web.rest.errors;

public class DeleteDeviceWithRepairsException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public DeleteDeviceWithRepairsException() {
        super("Can not delete Device with Repairs", "Device", "deleteDevice");
    }
}
