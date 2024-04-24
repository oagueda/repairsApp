import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IPattern } from 'app/entities/pattern/pattern.model';
import { PatternService } from 'app/entities/pattern/service/pattern.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { IDevice } from '../device.model';
import { DeviceService } from '../service/device.service';
import { DeviceFormService } from './device-form.service';

import { DeviceUpdateComponent } from './device-update.component';

describe('Device Management Update Component', () => {
  let comp: DeviceUpdateComponent;
  let fixture: ComponentFixture<DeviceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deviceFormService: DeviceFormService;
  let deviceService: DeviceService;
  let patternService: PatternService;
  let customerService: CustomerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, DeviceUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DeviceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeviceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deviceFormService = TestBed.inject(DeviceFormService);
    deviceService = TestBed.inject(DeviceService);
    patternService = TestBed.inject(PatternService);
    customerService = TestBed.inject(CustomerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call pattern query and add missing value', () => {
      const device: IDevice = { id: 456 };
      const pattern: IPattern = { id: 17881 };
      device.pattern = pattern;

      const patternCollection: IPattern[] = [{ id: 29052 }];
      jest.spyOn(patternService, 'query').mockReturnValue(of(new HttpResponse({ body: patternCollection })));
      const expectedCollection: IPattern[] = [pattern, ...patternCollection];
      jest.spyOn(patternService, 'addPatternToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ device });
      comp.ngOnInit();

      expect(patternService.query).toHaveBeenCalled();
      expect(patternService.addPatternToCollectionIfMissing).toHaveBeenCalledWith(patternCollection, pattern);
      expect(comp.patternsCollection).toEqual(expectedCollection);
    });

    it('Should call Customer query and add missing value', () => {
      const device: IDevice = { id: 456 };
      const customer: ICustomer = { id: 31780 };
      device.customer = customer;

      const customerCollection: ICustomer[] = [{ id: 24665 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [customer];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ device });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining),
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const device: IDevice = { id: 456 };
      const pattern: IPattern = { id: 3170 };
      device.pattern = pattern;
      const customer: ICustomer = { id: 11023 };
      device.customer = customer;

      activatedRoute.data = of({ device });
      comp.ngOnInit();

      expect(comp.patternsCollection).toContain(pattern);
      expect(comp.customersSharedCollection).toContain(customer);
      expect(comp.device).toEqual(device);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDevice>>();
      const device = { id: 123 };
      jest.spyOn(deviceFormService, 'getDevice').mockReturnValue(device);
      jest.spyOn(deviceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ device });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: device }));
      saveSubject.complete();

      // THEN
      expect(deviceFormService.getDevice).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(deviceService.update).toHaveBeenCalledWith(expect.objectContaining(device));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDevice>>();
      const device = { id: 123 };
      jest.spyOn(deviceFormService, 'getDevice').mockReturnValue({ id: null });
      jest.spyOn(deviceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ device: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: device }));
      saveSubject.complete();

      // THEN
      expect(deviceFormService.getDevice).toHaveBeenCalled();
      expect(deviceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDevice>>();
      const device = { id: 123 };
      jest.spyOn(deviceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ device });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deviceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePattern', () => {
      it('Should forward to patternService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(patternService, 'comparePattern');
        comp.comparePattern(entity, entity2);
        expect(patternService.comparePattern).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCustomer', () => {
      it('Should forward to customerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(customerService, 'compareCustomer');
        comp.compareCustomer(entity, entity2);
        expect(customerService.compareCustomer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
