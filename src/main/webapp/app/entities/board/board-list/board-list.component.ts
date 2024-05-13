import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';
import SharedModule from 'app/shared/shared.module';
import BoardTaskComponent from '../board-task/board-task.component';
import { IRepair } from 'app/entities/repair/repair.model';

@Component({
  standalone: true,
  selector: 'jhi-board-list',
  templateUrl: './board-list.component.html',
  styleUrl: './board-list.component.scss',
  imports: [SharedModule, RouterModule, BoardTaskComponent],
})
export default class BoardListComponent {
  @Input()
  name!: string;
  @Input()
  repairList!: IRepair[];
}
