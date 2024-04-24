import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';
import { RepairService } from '../service/repair.service';
import { IRepair } from '../repair.model';
import { RepairFormService } from './repair-form.service';

import { RepairUpdateComponent } from './repair-update.component';

describe('Repair Management Update Component', () => {
  let comp: RepairUpdateComponent;
  let fixture: ComponentFixture<RepairUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let repairFormService: RepairFormService;
  let repairService: RepairService;
  let deviceService: DeviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RepairUpdateComponent],
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
      .overrideTemplate(RepairUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RepairUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    repairFormService = TestBed.inject(RepairFormService);
    repairService = TestBed.inject(RepairService);
    deviceService = TestBed.inject(DeviceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Device query and add missing value', () => {
      const repair: IRepair = { id: 456 };
      const device: IDevice = { id: 32287 };
      repair.device = device;

      const deviceCollection: IDevice[] = [{ id: 10370 }];
      jest.spyOn(deviceService, 'query').mockReturnValue(of(new HttpResponse({ body: deviceCollection })));
      const additionalDevices = [device];
      const expectedCollection: IDevice[] = [...additionalDevices, ...deviceCollection];
      jest.spyOn(deviceService, 'addDeviceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ repair });
      comp.ngOnInit();

      expect(deviceService.query).toHaveBeenCalled();
      expect(deviceService.addDeviceToCollectionIfMissing).toHaveBeenCalledWith(
        deviceCollection,
        ...additionalDevices.map(expect.objectContaining),
      );
      expect(comp.devicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const repair: IRepair = { id: 456 };
      const device: IDevice = { id: 18324 };
      repair.device = device;

      activatedRoute.data = of({ repair });
      comp.ngOnInit();

      expect(comp.devicesSharedCollection).toContain(device);
      expect(comp.repair).toEqual(repair);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRepair>>();
      const repair = { id: 123 };
      jest.spyOn(repairFormService, 'getRepair').mockReturnValue(repair);
      jest.spyOn(repairService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ repair });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: repair }));
      saveSubject.complete();

      // THEN
      expect(repairFormService.getRepair).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(repairService.update).toHaveBeenCalledWith(expect.objectContaining(repair));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRepair>>();
      const repair = { id: 123 };
      jest.spyOn(repairFormService, 'getRepair').mockReturnValue({ id: null });
      jest.spyOn(repairService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ repair: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: repair }));
      saveSubject.complete();

      // THEN
      expect(repairFormService.getRepair).toHaveBeenCalled();
      expect(repairService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRepair>>();
      const repair = { id: 123 };
      jest.spyOn(repairService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ repair });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(repairService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDevice', () => {
      it('Should forward to deviceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(deviceService, 'compareDevice');
        comp.compareDevice(entity, entity2);
        expect(deviceService.compareDevice).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
