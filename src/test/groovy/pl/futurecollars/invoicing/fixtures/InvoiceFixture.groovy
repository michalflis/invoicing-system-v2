package pl.futurecollars.invoicing.fixtures

import pl.futurecollars.invoicing.model.Invoice
import java.time.LocalDate

class InvoiceFixture {

    static invoice(int id) {
        Invoice.builder()
                .invoiceId(UUID.randomUUID())
                .invoiceNumber(LocalDate.now().toString() + "000$id")
                .issueDate(LocalDate.now())
                .issuer(CompanyFixture.company(id))
                .receiver(CompanyFixture.company(id + 4))
                .entries(List.of(InvoiceEntryFixture.product(id),
                        InvoiceEntryFixture.product(id + 1),
                        InvoiceEntryFixture.product(id + 2)))
                .build()
    }

    static invoiceWithGasoline(int id) {
        Invoice.builder()
                .invoiceId(UUID.randomUUID())
                .invoiceNumber(LocalDate.now().toString() + "020$id")
                .issueDate(LocalDate.now())
                .issuer(CompanyFixture.company(id))
                .receiver(CompanyFixture.company(id + 4))
                .entries(List.of(InvoiceEntryFixture.gasolineForTheCarUsedForPersonalReason(),
                        InvoiceEntryFixture.product(id + 1),
                        InvoiceEntryFixture.product(id + 2)))
                .build()
    }
}
