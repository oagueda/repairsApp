//Material
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { NgModule } from '@angular/core';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { MatStepperModule } from '@angular/material/stepper';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';

const components = [MatToolbarModule, MatIconModule, DragDropModule, MatStepperModule, MatFormFieldModule, MatInputModule];

@NgModule({
  declarations: [],
  imports: [components],
  exports: components,
})
export class MaterialLibModule {}
