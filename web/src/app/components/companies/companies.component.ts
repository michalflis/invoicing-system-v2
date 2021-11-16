import {Component, OnInit} from '@angular/core';
import {CompanyListDto} from "../../dto/company-list.dto";
import {CompanyService} from "../../services/company.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-companies',
  templateUrl: './companies.component.html',
  styleUrls: ['./companies.component.scss']
})
export class CompaniesComponent implements OnInit {

  list: Array<CompanyListDto> = []

  constructor(private companyService: CompanyService, private router: Router) {
  }

  ngOnInit(): void {
    this.companyService.getCompanyList().subscribe(data => {
        this.list = data
      },
      error => {
        console.log(error)
      }
    )
  }

  goToDetails(id: string): void {
    this.router.navigate(['companies', id])
  }

  goToTaxReport(id: string): void {
    this.router.navigate(['tax', id])
  }

  delete(id: string): void {
    this.companyService.deleteCompany(id).subscribe(data => {
        console.log(data)
        this.ngOnInit()
      },
      error => {
        console.log(error)
      }
    )
  }
}
