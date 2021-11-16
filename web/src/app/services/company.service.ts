import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {CompanyListDto} from "../dto/company-list.dto";
import {Observable} from "rxjs";
import {CompanyDto} from "../dto/company.dto";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class CompanyService {

  constructor(private httpClient: HttpClient) {
  }

  public getCompanyList(): Observable<Array<CompanyListDto>> {
    return this.httpClient.get<Array<CompanyListDto>>(`${environment.apiBasePath}/companies/list`)
  }

  public getCompany(id: string): Observable<CompanyDto> {
    return this.httpClient.get<CompanyDto>(`${environment.apiBasePath}/companies/${id}`)
  }

  public deleteCompany(id: string): Observable<any> {
    return this.httpClient.delete(`${environment.apiBasePath}/companies/${id}`)
  }

  public updateCompany(data: CompanyDto): Observable<CompanyDto> {
    return this.httpClient.put<CompanyDto>(`${environment.apiBasePath}/companies`, data)
  }

  public saveCompany(data: CompanyDto): Observable<CompanyDto> {
    return this.httpClient.post<CompanyDto>(`${environment.apiBasePath}/companies`, data)
  }

}
