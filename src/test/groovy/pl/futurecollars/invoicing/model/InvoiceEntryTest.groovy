package pl.futurecollars.invoicing.model

import pl.futurecollars.invoicing.fixtures.InvoiceEntryFixture
import spock.lang.Specification

class InvoiceEntryTest extends Specification {

    def "should build InvoiceEntry object"() {
        setup:
        def invoiceEntry = InvoiceEntryFixture.product(1)

        when:
        def result = invoiceEntry

        then:
        result != null
    }

    def "should calculate VAT value"() {
        setup:
        def invoiceEntry = InvoiceEntryFixture.product(1)

        when:
        def result = invoiceEntry.vatValue

        then:
        result == 16
    }
}
