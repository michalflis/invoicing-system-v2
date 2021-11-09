import {Component, OnInit} from '@angular/core';
import {CompanyDto} from "../../dto/company.dto";
import {TaxReportDto} from "../../dto/tax-report.dto";
import {ActivatedRoute, Router} from "@angular/router";
import {TaxReportService} from "../../services/tax-report.service";
import {CompanyService} from "../../services/company.service";
import {Observable} from "rxjs";

@Component({
  selector: 'app-tax-report',
  templateUrl: './tax-report.component.html',
  styleUrls: ['./tax-report.component.scss']
})
export class TaxReportComponent implements OnInit {

  company: CompanyDto = {
    "companyId": '',
    "taxIdentificationNumber": '',
    "address": '',
    "name": '',
    "healthyInsurance": '',
    "pensionInsurance": '',
  }

  taxReport: TaxReportDto = {
    "costs": '',
    "finalIncomeTaxValue": '',
    "healthInsurance775": '',
    "healthInsurance9": '',
    "income": '',
    "incomeMinusCosts": '',
    "incomeMinusCostsMinusPensionInsurance": '',
    "incomeTax": '',
    "incomeTaxMinusHealthInsurance": '',
    "incomingVat": '',
    "outgoingVat": '',
    "pensionInsurance": '',
    "taxCalculationBase": '',
    "vatToPay": ''
  }

  companyId: string | null

  constructor(private activatedRoot: ActivatedRoute, private taxReportService: TaxReportService, private companyService: CompanyService, private router: Router) {
    this.companyId = this.activatedRoot.snapshot.paramMap.get('id')
  }

  ngOnInit(): void {
    if (!!this.companyId) {
      this.getTaxReport(this.companyId)
    }
 }

  getTaxReport(id: string): void {
    if (!!this.companyId) {
      this.companyService.getCompany(this.companyId).subscribe(data => {
          this.company = data
          this.taxReportService.getTaxReport(this.company).subscribe(data => {
              this.taxReport = data
              console.log(this.taxReport)
            },
            error => {
              console.log(error)
            }
          )
        },
        error => {
          console.log(error)
        }
      )
    }
  }

  goToCompany(id: string): void {
    this.router.navigate(['companies', id])
  }

}
