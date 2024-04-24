import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../pattern.test-samples';

import { PatternFormService } from './pattern-form.service';

describe('Pattern Form Service', () => {
  let service: PatternFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PatternFormService);
  });

  describe('Service methods', () => {
    describe('createPatternFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPatternFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
          }),
        );
      });

      it('passing IPattern should create a new form with FormGroup', () => {
        const formGroup = service.createPatternFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
          }),
        );
      });
    });

    describe('getPattern', () => {
      it('should return NewPattern for default Pattern initial value', () => {
        const formGroup = service.createPatternFormGroup(sampleWithNewData);

        const pattern = service.getPattern(formGroup) as any;

        expect(pattern).toMatchObject(sampleWithNewData);
      });

      it('should return NewPattern for empty Pattern initial value', () => {
        const formGroup = service.createPatternFormGroup();

        const pattern = service.getPattern(formGroup) as any;

        expect(pattern).toMatchObject({});
      });

      it('should return IPattern', () => {
        const formGroup = service.createPatternFormGroup(sampleWithRequiredData);

        const pattern = service.getPattern(formGroup) as any;

        expect(pattern).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPattern should not enable id FormControl', () => {
        const formGroup = service.createPatternFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPattern should disable id FormControl', () => {
        const formGroup = service.createPatternFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
