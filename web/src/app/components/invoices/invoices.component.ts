import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {InvoiceService} from "../../services/invoice.service";
import {InvoiceListDto} from "../../dto/invoice-list.dto";

@Component({
  selector: 'app-invoices',
  templateUrl: './invoices.component.html',
  styleUrls: ['./invoices.component.scss']
})
export class InvoicesComponent implements OnInit {

  list: Array<InvoiceListDto> = []

  constructor(private invoiceService: InvoiceService, private router: Router) {
  }

  ngOnInit(): void {
    this.invoiceService.getInvoiceList().subscribe(data => {
        this.list = data
      },
      error => {
        console.log(error)
      }
    )
  }

  goToDetails(id: string): void {
    this.router.navigate(['invoices', id])
  }

  delete(id: string): void {
    this.invoiceService.deleteInvoice(id).subscribe(data => {
        console.log(data)
        this.ngOnInit()
      },
      error => {
        console.log(error)
      }
    )
  }

}
