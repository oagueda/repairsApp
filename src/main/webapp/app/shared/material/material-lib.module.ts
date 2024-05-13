//Material
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { NgModule } from '@angular/core';
import { DragDropModule } from '@angular/cdk/drag-drop';

const components = [MatToolbarModule, MatIconModule, DragDropModule];

@NgModule({
  declarations: [],
  imports: [components],
  exports: components,
})
export class MaterialLibModule {}
