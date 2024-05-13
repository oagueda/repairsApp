import { Component, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import SharedModule from 'app/shared/shared.module';
import BoardListComponent from './board-list/board-list.component';
import { RepairService } from '../repair/service/repair.service';
import { IRepair } from '../repair/repair.model';
import { Status } from '../enumerations/status.model';
@Component({
  standalone: true,
  selector: 'jhi-board',
  templateUrl: './board.component.html',
  styleUrl: './board.component.scss',
  imports: [SharedModule, RouterModule, BoardListComponent],
})
export default class BoardComponent implements OnInit {
  todoRepairs: IRepair[] = [];
  reviewRepairs: IRepair[] = [];
  wipRepairs: IRepair[] = [];
  doneRepairs: IRepair[] = [];
  status = Status;

  protected repairService = inject(RepairService);

  ngOnInit(): void {
    this.repairService.query({ size: 999 }).subscribe({
      next: response => {
        this.separateRepairsByStatus(response.body ?? []);
      },
    });
  }

  protected separateRepairsByStatus(allRepairs: IRepair[]): void {
    this.todoRepairs = allRepairs.filter(repair => {
      return repair.status === Status.TODO;
    });
    this.reviewRepairs = allRepairs.filter(repair => {
      return repair.status === Status.REVIEW;
    });
    this.wipRepairs = allRepairs.filter(repair => {
      return repair.status === Status.WIP;
    });
    this.doneRepairs = allRepairs.filter(repair => {
      return repair.status === Status.DONE;
    });
  }
}
