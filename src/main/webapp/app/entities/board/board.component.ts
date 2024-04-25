import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import SharedModule from 'app/shared/shared.module';

@Component({
  standalone: true,
  selector: 'jhi-board',
  templateUrl: './board.component.html',
  styleUrl: './board.component.scss',
  imports: [SharedModule, RouterModule],
})
export default class BoardComponent {}
