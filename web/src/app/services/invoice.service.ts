import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {InvoiceListDto} from "../dto/invoice-list.dto";
import {InvoiceDto} from "../dto/invoice.dto";

@Injectable({
  providedIn: 'root'
})
export class InvoiceService {

  private options = {
    headers: new HttpHeaders({'content-type':'application/json'}),
    withCredentials: true
  }

  constructor(private httpClient: HttpClient) {
  }

  public getInvoiceList(): Observable<Array<InvoiceListDto>> {
    return this.httpClient.get<Array<InvoiceListDto>>(`${environment.apiBasePath}/invoices/list`)
  }

  public getInvoice(id: string): Observable<InvoiceDto> {
    return this.httpClient.get<InvoiceDto>(`${environment.apiBasePath}/invoices/${id}`)
  }

  public deleteInvoice(id: string): Observable<any> {
    return this.httpClient.delete(`${environment.apiBasePath}/invoices/${id}`, this.options)
  }

  public updateInvoice(data: InvoiceDto): Observable<InvoiceDto> {
    return this.httpClient.put<InvoiceDto>(`${environment.apiBasePath}/invoices`, data, this.options)
  }

  public saveInvoice(data: InvoiceDto): Observable<InvoiceDto> {
    return this.httpClient.post<InvoiceDto>(`${environment.apiBasePath}/invoices`, data, this.options)
  }

}
