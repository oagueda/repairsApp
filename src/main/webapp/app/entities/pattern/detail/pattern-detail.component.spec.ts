import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PatternDetailComponent } from './pattern-detail.component';

describe('Pattern Management Detail Component', () => {
  let comp: PatternDetailComponent;
  let fixture: ComponentFixture<PatternDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PatternDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PatternDetailComponent,
              resolve: { pattern: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PatternDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PatternDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pattern on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PatternDetailComponent);

      // THEN
      expect(instance.pattern()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
