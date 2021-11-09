import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {CompaniesComponent} from './components/companies/companies.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {HttpClientModule} from "@angular/common/http";
import {CompanyComponent} from './components/company/company.component';
import {TaxReportComponent} from './components/tax-report/tax-report.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { InvoicesComponent } from './components/invoices/invoices.component';
import { InvoiceComponent } from './components/invoice/invoice.component';


@NgModule({
  declarations: [
    AppComponent,
    CompaniesComponent,
    CompanyComponent,
    TaxReportComponent,
    InvoicesComponent,
    InvoiceComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
