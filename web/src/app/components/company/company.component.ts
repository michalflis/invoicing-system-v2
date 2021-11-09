import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CompanyService} from "../../services/company.service";
import {CompanyDto} from "../../dto/company.dto";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-company',
  templateUrl: './company.component.html',
  styleUrls: ['./company.component.scss']
})
export class CompanyComponent implements OnInit {

  companyId: string | null
  public item: CompanyDto = {
    "companyId": '',
    "taxIdentificationNumber": '',
    "address": '',
    "name": '',
    "healthyInsurance": '',
    "pensionInsurance": '',
  }

  formGroup: FormGroup;
  create: boolean = false;

  constructor(private activatedRoot: ActivatedRoute, private companyService: CompanyService, private router: Router) {
    this.companyId = this.activatedRoot.snapshot.paramMap.get('id')

    this.create = !this.companyId

    this.formGroup = new FormGroup(
      {
        taxIdentificationNumber: new FormControl({
          disabled: !this.create,
          value: ''
        }, [Validators.required, Validators.pattern("^\d{10}$")]),
        address: new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(255)]),
        name: new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(255)]),
        healthyInsurance: new FormControl('', [Validators.required, Validators.pattern("^\d*[.]?\d{0,2}$")]),
        pensionInsurance: new FormControl('', [Validators.required, Validators.pattern("^\d*[.]?\d{0,2}$")]),
      }
    )
  }

  ngOnInit(): void {
    if (!!this.companyId)
      this.companyService.getCompany(this.companyId).subscribe(data => {
          this.item = data

          this.formGroup.patchValue({
              ...data
            }
          )
        },
        error => {
          console.log(error)
        }
      )
  }

  update(): void {
    this.companyService.updateCompany({
        ...this.formGroup.value,
        companyId: this.item.companyId,
        taxIdentificationNumber: this.item.taxIdentificationNumber
      }
    ).subscribe(data => {
        this.item = data
        this.ngOnInit()
      },
      error => {
        console.log(error)
      }
    )
  }

  save(): void {
    this.companyService.saveCompany({
        ...this.formGroup.value,
      }
    ).subscribe(() => {
        this.router.navigate(['companies', 'list'])
      },
      error => {
        console.log(error)
      }
    )
  }

  delete(id: string): void {
    this.companyService.deleteCompany(id).subscribe(() => {
        this.router.navigate(['companies', 'list'])
      },
      error => {
        console.log(error)
      }
    )
  }

  goToTaxReport(id: string): void {
    this.router.navigate(['tax', id])
  }
}
