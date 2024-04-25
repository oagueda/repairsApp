import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICustomer } from '../customer.model';
import { CustomerService } from '../service/customer.service';

@Component({
  standalone: true,
  templateUrl: './customer-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CustomerDeleteDialogComponent {
  customer?: ICustomer;
  force: boolean = false;

  protected customerService = inject(CustomerService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.customerService.delete(id, this.force).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
