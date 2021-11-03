package pl.futurecollars.invoicing.service.tax

import pl.futurecollars.invoicing.fixtures.CompanyFixture
import pl.futurecollars.invoicing.fixtures.InvoiceFixture
import pl.futurecollars.invoicing.service.invoice.InvoiceService
import spock.lang.Shared
import spock.lang.Specification


class TaxCalculatorServiceTest extends Specification {

    private InvoiceService invoiceService = Mock()
     private TaxCalculatorService taxCalculatorService = new TaxCalculatorService(invoiceService)

    @Shared
    def invoice = InvoiceFixture.invoice(5)
    def invoice1 = InvoiceFixture.invoice(1)
    def invoiceGasoline = InvoiceFixture.invoiceWithGasoline(1)
    def company2 = CompanyFixture.company(2)


    def setup() {
        invoice.getIssuer().setTaxIdentificationNumber(company2.getTaxIdentificationNumber())
        invoice1.getReceiver().setTaxIdentificationNumber(company2.getTaxIdentificationNumber())
        invoiceGasoline.getReceiver().setTaxIdentificationNumber(company2.getTaxIdentificationNumber())
        invoiceService.getAll() >> [invoice, invoice1, invoiceGasoline]
    }

    def "should calculate income for company(2)"() {
        when:
        def result = taxCalculatorService.income(company2.getTaxIdentificationNumber())

        then:
        result == 3600
    }

    def "should calculate cost for company(2)"() {
        when:
        def result = taxCalculatorService.costs(company2.getTaxIdentificationNumber())

        then:
        result == 2484.74
    }

    def "should calculate incoming VAT for company(2)"() {
        when:
        def result = taxCalculatorService.incomingVat(company2.getTaxIdentificationNumber())

        then:
        result == 288.00
    }

    def "should calculate outgoing VAT for company(2)"() {
        when:
        def result = taxCalculatorService.outgoingVat(company2.getTaxIdentificationNumber())

        then:
        result == 241.49
    }

    def "should calculate earnings for company(2)"() {
        when:
        def result = taxCalculatorService.incomeMinusCosts(company2.getTaxIdentificationNumber())

        then:
        result == 1115.26
    }

    def "should calculate VAT to pay for company(2)"() {
        when:
        def result = taxCalculatorService.vatToPay(company2.getTaxIdentificationNumber())

        then:
        result == 46.51
    }

    def "should generate tax Report for company(2)"() {
        when:
        def result = taxCalculatorService.getTaxReport(company2)

        then:
        result.getIncome() == 3600.00
        result.getCosts() == 2484.74
        result.getIncomingVat() == 288.00
        result.getOutgoingVat() == 241.49
        result.getIncomeMinusCosts() == 1115.26
        result.getVatToPay() == 46.51
        result.getPensionInsurance() == 500.97
        result.getIncomeMinusCostsMinusPensionInsurance() == 614.29
        result.getTaxCalculationBase() == 614.00
        result.getIncomeTax() == 116.66
        result.getHealthInsurance9() == 90.00
        result.getHealthInsurance775() == 77.50
        result.getIncomeTaxMinusHealthInsurance() == 39.16
        result.getFinalIncomeTaxValue() == 39.00
    }
}
