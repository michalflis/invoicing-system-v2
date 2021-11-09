export interface InvoiceEntryDto {
  entryId: string,
  description: string,
  unit: string,
  price: string,
  quantity: string,
  totalPrice: string,
  vatRate: string,
  vatValue: string,
  carUsedForPersonalReason: boolean,
  carRegistrationNumber: string,
}
