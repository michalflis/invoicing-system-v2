package pl.futurecollars.invoicing.service.invoice

import pl.futurecollars.invoicing.fixtures.CompanyFixture
import pl.futurecollars.invoicing.fixtures.InvoiceEntryFixture
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.repository.invoice.InvoiceRepository
import spock.lang.Specification

import java.time.LocalDate

class InvoiceServiceTest extends Specification {

    def issuer = CompanyFixture.company(1)
    def issuer2 = CompanyFixture.company(1)
    def issuerUpdated = CompanyFixture.company(1)
    def receiver = CompanyFixture.company(1)
    def receiver2 = CompanyFixture.company(1)
    def date = new LocalDate(2017, 05, 05)
    def entries = Arrays.asList(InvoiceEntryFixture.product(1), InvoiceEntryFixture.product(2))
    def invoice = new Invoice(UUID.randomUUID(), "XXX", date, issuer, receiver, entries)
    def invoice2 = new Invoice(UUID.randomUUID(), "invoice2", date, issuer2, receiver, entries)
    def invoice3 = new Invoice(UUID.randomUUID(), "invoice13", date, issuer, receiver2, entries)
    def invoiceUpdated = new Invoice(UUID.randomUUID(), "CCC", date, issuerUpdated, receiver, entries)
    InvoiceRepository invoiceRepository = Mock()
    def invoiceService = new InvoiceService(invoiceRepository)

    def "should save invoice in to database"() {
        setup:
        invoiceRepository.save(invoice) >> invoice
        invoiceRepository.findById(invoice.getInvoiceId()) >> Optional.of(invoice)
        when:
        def result = invoiceService.save(invoice)

        then:
        invoiceService.getById(result.getInvoiceId()) != null
        invoiceService.getById(result.getInvoiceId()).getInvoiceNumber() == "XXX"
    }

    def "should get invoice from database by Id"() {
        setup:
        invoiceRepository.findById(invoice.getInvoiceId()) >> Optional.of(invoice)

        when:
        def result = invoiceService.getById(invoice.getInvoiceId())

        then:
        result != null
        result.getInvoiceNumber() == "XXX"
    }

    def "should get list of all invoices from database"() {
        setup:
        invoiceRepository.findAll() >> [invoice, invoice2, invoice3]

        when:
        def result = invoiceService.getAll()

        then:
        result.size() == 3
    }

    def "should update invoice in the database"() {
        setup:
        invoiceRepository.save(invoiceUpdated) >> invoiceUpdated
        invoiceRepository.findById(invoice.getInvoiceId()) >> Optional.of(invoiceUpdated)
        invoiceUpdated.setInvoiceId(invoice.getInvoiceId())

        when:
        def result = invoiceService.update(invoiceUpdated)

        then:
        invoiceService.getById(result.getInvoiceId()) != null
        invoiceService.getById(result.getInvoiceId()).getInvoiceNumber() == "CCC"
    }

    def "should delete invoice from database"() {
        setup:
        invoiceRepository.findById(invoice.getInvoiceId()) >> Optional.of(invoice)
        invoiceRepository.findAll() >> []
        def invoiceService = new InvoiceService(invoiceRepository)

        when:
        def result = invoiceService.delete(invoice.getInvoiceId())

        then:
        result
        invoiceService.getAll().size() == 0
    }
}
