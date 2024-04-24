import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { PatternService } from '../service/pattern.service';
import { IPattern } from '../pattern.model';
import { PatternFormService } from './pattern-form.service';

import { PatternUpdateComponent } from './pattern-update.component';

describe('Pattern Management Update Component', () => {
  let comp: PatternUpdateComponent;
  let fixture: ComponentFixture<PatternUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let patternFormService: PatternFormService;
  let patternService: PatternService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, PatternUpdateComponent],
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
      .overrideTemplate(PatternUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PatternUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    patternFormService = TestBed.inject(PatternFormService);
    patternService = TestBed.inject(PatternService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const pattern: IPattern = { id: 456 };

      activatedRoute.data = of({ pattern });
      comp.ngOnInit();

      expect(comp.pattern).toEqual(pattern);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPattern>>();
      const pattern = { id: 123 };
      jest.spyOn(patternFormService, 'getPattern').mockReturnValue(pattern);
      jest.spyOn(patternService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pattern });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pattern }));
      saveSubject.complete();

      // THEN
      expect(patternFormService.getPattern).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(patternService.update).toHaveBeenCalledWith(expect.objectContaining(pattern));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPattern>>();
      const pattern = { id: 123 };
      jest.spyOn(patternFormService, 'getPattern').mockReturnValue({ id: null });
      jest.spyOn(patternService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pattern: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pattern }));
      saveSubject.complete();

      // THEN
      expect(patternFormService.getPattern).toHaveBeenCalled();
      expect(patternService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPattern>>();
      const pattern = { id: 123 };
      jest.spyOn(patternService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pattern });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(patternService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
