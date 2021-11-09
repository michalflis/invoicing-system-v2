import {Component, OnInit} from '@angular/core';
import {CompanyDto} from "../../dto/company.dto";
import {InvoiceDto} from "../../dto/invoice.dto";
import {InvoiceEntryDto} from "../../dto/invoice-entry.dto";
import {ActivatedRoute, Router} from "@angular/router";
import {InvoiceService} from "../../services/invoice.service";
import {CompanyListDto} from "../../dto/company-list.dto";
import {CompanyService} from "../../services/company.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-invoice',
  templateUrl: './invoice.component.html',
  styleUrls: ['./invoice.component.scss']
})
export class InvoiceComponent implements OnInit {

  invoiceId: string | null

  public entry: InvoiceEntryDto = {
    "entryId": '',
    "description": '',
    "unit": '',
    "price": '',
    "quantity": '',
    "totalPrice": '',
    "vatRate": '',
    "vatValue": '',
    "carUsedForPersonalReason": false,
    "carRegistrationNumber": '',
  }

  public issuer: CompanyDto = {
    "companyId": '',
    "taxIdentificationNumber": '',
    "address": '',
    "name": '',
    "healthyInsurance": '',
    "pensionInsurance": '',
  }

  public receiver: CompanyDto = {
    "companyId": '',
    "taxIdentificationNumber": '',
    "address": '',
    "name": '',
    "healthyInsurance": '',
    "pensionInsurance": '',
  }

  public invoice: InvoiceDto = {
    "invoiceNumber": '',
    "issueDate": '',
    "issuer": this.issuer,
    "receiver": this.receiver,
    "entries": []
  }

  edit: boolean = false
  create: boolean = false
  selectedIssuer: string = ''
  selectedReceiver: string = ''
  selectedVatRate: string = ''
  companyList: Array<CompanyListDto> = []
  entryFormGroup: FormGroup;
  topFormGroup: FormGroup;

  constructor(private activatedRoot: ActivatedRoute, private invoiceService: InvoiceService, private companyService: CompanyService, private router: Router) {
    this.invoiceId = this.activatedRoot.snapshot.paramMap.get('id')

    if (this.invoiceId == "create") {
      this.edit = true;
      this.create = true;
    }

    this.entryFormGroup = new FormGroup(
      {
        description: new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(255)]),
        unit: new FormControl('', [Validators.required, Validators.minLength(1), Validators.maxLength(20)]),
        price: new FormControl('', [Validators.required, Validators.pattern("^\d*[.]?\d{0,2}$")]),
        quantity: new FormControl('', [Validators.required, Validators.pattern("^\d*[.]?\d{0,2}$")]),
        vatRate: new FormControl(this.selectedVatRate, [Validators.required]),
      }
    )

    this.topFormGroup = new FormGroup(
      {
        invoiceNumber: new FormControl(this.invoice.invoiceNumber, [Validators.required, Validators.minLength(3), Validators.maxLength(50)]),
        issueDate: new FormControl(this.invoice.issueDate, [Validators.required, Validators.minLength(1), Validators.maxLength(20)]),
        issuerId: new FormControl(this.selectedIssuer, [Validators.required, Validators.minLength(36), Validators.maxLength(36)]),
        receiverId: new FormControl(this.selectedReceiver, [Validators.required, Validators.minLength(36), Validators.maxLength(36)]),
      }
    )
  }

  ngOnInit(): void {
    if (!!this.invoiceId && this.invoiceId != "create")
      this.invoiceService.getInvoice(this.invoiceId).subscribe(data => {
          this.invoice = data
          console.log(this.invoice)

          this.topFormGroup.patchValue({
              ...data
            }
          )
          this.entryFormGroup.patchValue({
              ...data
            }
          )
        },
        error => {
          console.log(error)
        }
      )
    console.log(this.create, this.edit)
    this.companyService.getCompanyList().subscribe(data => {
        this.companyList = data
      },
      error => {
        console.log(error)
      }
    )
  }

  updateInvoice(): void {
    this.invoice.invoiceNumber = this.topFormGroup.controls['invoiceNumber'].value
    this.invoice.issueDate = this.topFormGroup.controls['issueDate'].value
    this.invoiceService.updateInvoice(this.invoice).subscribe(data => {
        this.invoice = data
        this.cancel()
      },
      error => {
        console.log(error)
      }
    )
  }

  saveInvoice(): void {
    this.invoice.invoiceNumber = this.topFormGroup.controls['invoiceNumber'].value
    this.invoice.issueDate = this.topFormGroup.controls['issueDate'].value
    this.invoiceService.saveInvoice(this.invoice).subscribe(() => {
        this.router.navigate(['invoices', 'list'])
      },
      error => {
        console.log(error)
      }
    )
  }

  cancel(): void {
    this.edit = false
    this.ngOnInit()
  }

  addEntry(): void {
    this.invoice.entries.push(
      {
        ...this.entryFormGroup.value,
        carUsedForPersonalReason: false
      }
    )
  }

  deleteEntry(i: number): void {
    this.invoice.entries.splice(i, 1)
  }

  updateIssuer(id: string): void {
    if (!!id)
      this.companyService.getCompany(id).subscribe(data => {
          this.invoice.issuer = data
        },
        error => {
          console.log(error)
        }
      )
  }

  updateReceiver(id: string): void {
    if (!!id)
      this.companyService.getCompany(id).subscribe(data => {
          this.invoice.receiver = data
        },
        error => {
          console.log(error)
        }
      )
  }
}
