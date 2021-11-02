package pl.futurecollars.invoicing.controllers.tax

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.fixtures.CompanyFixture
import pl.futurecollars.invoicing.fixtures.InvoiceFixture
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.TaxReport
import pl.futurecollars.invoicing.service.company.CompanyService
import pl.futurecollars.invoicing.service.invoice.InvoiceService
import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Shared
import spock.lang.Specification

import java.math.RoundingMode

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class TaxCalculatorControllerTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService<Invoice> jsonServiceInvoice

    @Autowired
    private JsonService<Company> jsonServiceCompany

    @Autowired
    private JsonService<TaxReport> jsonServiceTaxReport

    @Autowired
    private InvoiceService invoiceService

    @Shared
    def invoice = InvoiceFixture.invoice(4)
    def invoice1 = InvoiceFixture.invoice(1)
    def company = CompanyFixture.company(4)

    def setup() {
        invoiceService.clear()
    }

    def cleanup() {
        invoiceService.clear()
    }

    def "Should get tax report for company(4)"() {

        given:
        invoice.getIssuer().setCompanyId(company.getCompanyId())
        invoice.getIssuer().setTaxIdentificationNumber(company.getTaxIdentificationNumber())
        invoice1.getReceiver().setCompanyId(company.getCompanyId())
        invoice1.getReceiver().setTaxIdentificationNumber(company.getTaxIdentificationNumber())

        def invoiceAsJson = jsonServiceInvoice.convertToJson(invoice)
        def invoice1AsJson = jsonServiceInvoice.convertToJson(invoice1)

        mockMvc.perform(
                post("/invoices").content(invoiceAsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

        mockMvc.perform(
                post("/invoices").content(invoice1AsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

        def updatedCompanyAsJson = jsonServiceCompany.convertToJson(company)

        def taxReport = TaxReport.builder()
                .incomingVat(BigDecimal.valueOf(240.00).setScale(2, RoundingMode.HALF_UP))
                .outgoingVat(BigDecimal.valueOf(96.00).setScale(2, RoundingMode.HALF_UP))
                .income(BigDecimal.valueOf(3000.00).setScale(2, RoundingMode.HALF_UP))
                .costs(BigDecimal.valueOf(1200.00).setScale(2, RoundingMode.HALF_UP))
                .incomeMinusCosts(BigDecimal.valueOf(1800.00).setScale(2, RoundingMode.HALF_UP))
                .vatToPay(BigDecimal.valueOf(144.00).setScale(2, RoundingMode.HALF_UP))
                .pensionInsurance(BigDecimal.valueOf(500.97).setScale(2, RoundingMode.HALF_UP))
                .incomeMinusCostsMinusPensionInsurance(BigDecimal.valueOf(1299.03).setScale(2, RoundingMode.HALF_UP))
                .taxCalculationBase(BigDecimal.valueOf(1299).setScale(2, RoundingMode.HALF_UP))
                .incomeTax(BigDecimal.valueOf(246.81).setScale(2, RoundingMode.HALF_UP))
                .healthInsurance9(BigDecimal.valueOf(90.00).setScale(2, RoundingMode.HALF_UP))
                .healthInsurance775(BigDecimal.valueOf(77.50).setScale(2, RoundingMode.HALF_UP))
                .incomeTaxMinusHealthInsurance(BigDecimal.valueOf(169.31).setScale(2, RoundingMode.HALF_UP))
                .finalIncomeTaxValue(BigDecimal.valueOf(169.00).setScale(2, RoundingMode.HALF_UP))
                .build()

        when:
        def response = mockMvc.perform(
                post("/tax").content(updatedCompanyAsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        jsonServiceTaxReport.convertToObject(response, TaxReport.class) == taxReport
    }
}
