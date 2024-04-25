package xyz.oagueda.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.oagueda.domain.Customer;
import xyz.oagueda.domain.Device;
import xyz.oagueda.repository.CustomerRepository;
import xyz.oagueda.service.dto.CustomerDTO;
import xyz.oagueda.service.mapper.CustomerMapper;
import xyz.oagueda.web.rest.errors.DeleteCustomerWithDevicesException;

/**
 * Service Implementation for managing {@link xyz.oagueda.domain.Customer}.
 */
@Service
@Transactional
public class CustomerService {

    private final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    private final DeviceService deviceService;

    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, DeviceService deviceService) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.deviceService = deviceService;
    }

    /**
     * Save a customer.
     *
     * @param customerDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerDTO save(CustomerDTO customerDTO) {
        log.debug("Request to save Customer : {}", customerDTO);
        Customer customer = customerMapper.toEntity(customerDTO);
        customer = customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }

    /**
     * Update a customer.
     *
     * @param customerDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerDTO update(CustomerDTO customerDTO) {
        log.debug("Request to update Customer : {}", customerDTO);
        Customer customer = customerMapper.toEntity(customerDTO);
        customer = customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }

    /**
     * Partially update a customer.
     *
     * @param customerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerDTO> partialUpdate(CustomerDTO customerDTO) {
        log.debug("Request to partially update Customer : {}", customerDTO);

        return customerRepository
            .findById(customerDTO.getId())
            .map(existingCustomer -> {
                customerMapper.partialUpdate(existingCustomer, customerDTO);

                return existingCustomer;
            })
            .map(customerRepository::save)
            .map(customerMapper::toDto);
    }

    /**
     * Get one customer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerDTO> findOne(Long id) {
        log.debug("Request to get Customer : {}", id);
        return customerRepository.findById(id).map(customerMapper::toDto);
    }

    /**
     * Delete the customer by id.
     *
     * @param id the id of the entity.
     * @param force if the deletion should be forced.
     */
    public void delete(Long id, boolean force) {
        log.debug("Request to delete Customer : {}, force: {}", id, force);
        Customer customer = customerRepository.findById(id).orElseThrow();
        if (force) {
            for (Device device : customer.getDevices()) {
                deviceService.delete(device.getId(), true);
            }
        }
        for (Device device : customer.getDevices()) {
            if (Boolean.FALSE.equals(device.getDeleted())) {
                throw new DeleteCustomerWithDevicesException();
            }
        }
        customer.setDeleted(true);
    }
}
