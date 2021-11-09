import {CompanyDto} from "./company.dto";
import {InvoiceEntryDto} from "./invoice-entry.dto";

export interface InvoiceDto {
invoiceNumber: string,
issueDate: string,
issuer: CompanyDto,
receiver: CompanyDto,
entries: Array<InvoiceEntryDto>
}
