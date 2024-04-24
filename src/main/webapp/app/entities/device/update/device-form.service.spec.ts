import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../device.test-samples';

import { DeviceFormService } from './device-form.service';

describe('Device Form Service', () => {
  let service: DeviceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeviceFormService);
  });

  describe('Service methods', () => {
    describe('createDeviceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDeviceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            brand: expect.any(Object),
            model: expect.any(Object),
            serialNumber: expect.any(Object),
            warranty: expect.any(Object),
            password: expect.any(Object),
            hasPattern: expect.any(Object),
            simPinCode: expect.any(Object),
            notes: expect.any(Object),
            deleted: expect.any(Object),
            pattern: expect.any(Object),
            customer: expect.any(Object),
          }),
        );
      });

      it('passing IDevice should create a new form with FormGroup', () => {
        const formGroup = service.createDeviceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            brand: expect.any(Object),
            model: expect.any(Object),
            serialNumber: expect.any(Object),
            warranty: expect.any(Object),
            password: expect.any(Object),
            hasPattern: expect.any(Object),
            simPinCode: expect.any(Object),
            notes: expect.any(Object),
            deleted: expect.any(Object),
            pattern: expect.any(Object),
            customer: expect.any(Object),
          }),
        );
      });
    });

    describe('getDevice', () => {
      it('should return NewDevice for default Device initial value', () => {
        const formGroup = service.createDeviceFormGroup(sampleWithNewData);

        const device = service.getDevice(formGroup) as any;

        expect(device).toMatchObject(sampleWithNewData);
      });

      it('should return NewDevice for empty Device initial value', () => {
        const formGroup = service.createDeviceFormGroup();

        const device = service.getDevice(formGroup) as any;

        expect(device).toMatchObject({});
      });

      it('should return IDevice', () => {
        const formGroup = service.createDeviceFormGroup(sampleWithRequiredData);

        const device = service.getDevice(formGroup) as any;

        expect(device).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDevice should not enable id FormControl', () => {
        const formGroup = service.createDeviceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDevice should disable id FormControl', () => {
        const formGroup = service.createDeviceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
