import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {CompanyDto} from "../dto/company.dto";
import {environment} from "../../environments/environment";
import {TaxReportDto} from "../dto/tax-report.dto";

@Injectable({
  providedIn: 'root'
})
export class TaxReportService {

  constructor(private httpClient: HttpClient) { }

  public getTaxReport(data: CompanyDto): Observable<TaxReportDto> {
    return this.httpClient.post<TaxReportDto>(`${environment.apiBasePath}/tax`, data)
  }


}
