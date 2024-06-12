import { Component, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import SharedModule from 'app/shared/shared.module';
import BoardListComponent from './board-list/board-list.component';
import { RepairService } from '../repair/service/repair.service';
import { IRepair } from '../repair/repair.model';
import { Status } from '../enumerations/status.model';
import { CdkDragDrop, transferArrayItem } from '@angular/cdk/drag-drop';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalComponent } from '../modal/modal.component';
import { Options } from '../enumerations/options.model';
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

  doneRepairsTotal = 0;

  protected repairService = inject(RepairService);
  private modalService = inject(NgbModal);

  ngOnInit(): void {
    this.load();
  }

  drop(event: CdkDragDrop<IRepair[]>) {
    if (event.previousContainer !== event.container) {
      transferArrayItem(event.previousContainer.data, event.container.data, event.previousIndex, 0);
      switch (event.container.id) {
        case Status.TODO:
          event.container.data[0].status = Status.TODO;
          this.repairService.update(event.container.data[0]).subscribe(() => this.load());
          break;
        case Status.REVIEW:
          event.container.data[0].status = Status.REVIEW;
          this.repairService.update(event.container.data[0]).subscribe(() => this.load());
          break;
        case Status.WIP:
          event.container.data[0].status = Status.WIP;
          this.repairService.update(event.container.data[0]).subscribe(() => this.load());
          break;
        case Status.DONE:
          event.container.data[0].status = Status.DONE;
          this.repairService.update(event.container.data[0]).subscribe(() => this.load());
          break;
        default:
          break;
      }
    }
  }

  protected load(): void {
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
    this.todoRepairs.sort((a, b) => {
      return new Date(b.lastModifiedDate ?? 0).getTime() - new Date(a.lastModifiedDate ?? 0).getTime();
    });
    this.reviewRepairs = allRepairs.filter(repair => {
      return repair.status === Status.REVIEW;
    });
    this.reviewRepairs.sort((a, b) => {
      return new Date(b.lastModifiedDate ?? 0).getTime() - new Date(a.lastModifiedDate ?? 0).getTime();
    });
    this.wipRepairs = allRepairs.filter(repair => {
      return repair.status === Status.WIP;
    });
    this.wipRepairs.sort((a, b) => {
      return new Date(b.lastModifiedDate ?? 0).getTime() - new Date(a.lastModifiedDate ?? 0).getTime();
    });
    this.doneRepairs = allRepairs.filter(repair => {
      return repair.status === Status.DONE;
    });
    this.doneRepairs.sort((a, b) => {
      return new Date(b.lastModifiedDate ?? 0).getTime() - new Date(a.lastModifiedDate ?? 0).getTime();
    });
    this.doneRepairsTotal = this.doneRepairs.length;
    this.doneRepairs = this.doneRepairs.slice(0, 5);
  }

  openModal(option: number): void {
    switch (option) {
      case 0:
        this.openRepair();
        break;
      case 1:
        this.openDevice();
        break;
      case 2:
        this.openCustomer();
        break;
      default:
        break;
    }
  }

  openRepair(): void {
    const modalRef = this.modalService.open(ModalComponent, { fullscreen: true });
    modalRef.componentInstance.options = [Options.REPAIR];
    modalRef.closed.subscribe(() => {
      this.ngOnInit();
    });
  }

  openDevice(): void {
    const modalRef = this.modalService.open(ModalComponent, { fullscreen: true });
    modalRef.componentInstance.options = [Options.DEVICE, Options.REPAIR];
    modalRef.closed.subscribe(() => {
      this.ngOnInit();
    });
  }
  openCustomer(): void {
    const modalRef = this.modalService.open(ModalComponent, { fullscreen: true });
    modalRef.componentInstance.options = [Options.CUSTOMER, Options.DEVICE, Options.REPAIR];
    modalRef.closed.subscribe(() => {
      this.ngOnInit();
    });
  }
}
