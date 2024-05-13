//Material
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { NgModule } from '@angular/core';

const components = [MatToolbarModule, MatIconModule];

@NgModule({
  declarations: [],
  imports: [components],
  exports: components,
})
export class MaterialLibModule {}
