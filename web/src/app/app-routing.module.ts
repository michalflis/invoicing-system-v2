import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CompaniesComponent} from "./components/companies/companies.component";
import {CompanyComponent} from "./components/company/company.component";
import {TaxReportComponent} from "./components/tax-report/tax-report.component";
import {InvoicesComponent} from "./components/invoices/invoices.component";
import {InvoiceComponent} from "./components/invoice/invoice.component";

const routes: Routes = [
  {
    path: 'companies/list',
    component: CompaniesComponent
  },
  {
    path: 'companies/create',
    component: CompanyComponent
  },
  {
    path: 'companies/:id',
    component: CompanyComponent
  },
  {
    path: 'tax',
    component: TaxReportComponent
  },
  {
    path: 'tax/:id',
    component: TaxReportComponent
  },
  {
    path: 'invoices/list',
    component: InvoicesComponent
  },
  {
    path: 'invoices/:id',
    component: InvoiceComponent
  },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
