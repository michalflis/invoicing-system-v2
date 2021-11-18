package pl.futurecollars.invoicing.controllers.invoice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.fixtures.InvoiceFixture
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WithMockUser
@SpringBootTest
@Stepwise
@AutoConfigureMockMvc
class InvoiceControllerBasicTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService<Invoice> jsonService

    @Autowired
    private JsonService<Invoice[]> jsonListService

    @Shared
    def invoice = InvoiceFixture.invoice(1)

    @Shared
    def updatedInvoice = InvoiceFixture.invoice(1)

    def "should add single invoice"() {
        given:
        def invoiceAsJson = jsonService.convertToJson(invoice)

        when:
        def response = mockMvc.perform(
                post("/invoices").content(invoiceAsJson).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def responseAsInvoice = jsonService.convertToObject(response, Invoice.class)
        invoice.setInvoiceId(responseAsInvoice.getInvoiceId())
        invoice.getIssuer().setCompanyId(responseAsInvoice.getIssuer().getCompanyId())
        invoice.getReceiver().setCompanyId(responseAsInvoice.getReceiver().getCompanyId())

        then:
        invoice == responseAsInvoice

    }

    def "should return list of invoices"() {
        when:
        def response = mockMvc.perform(get("/invoices").with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoices = jsonListService.convertToObject(response, Invoice[].class)

        then:
        invoices.size() > 0
        invoices[0] == invoice
    }

    def "should return short list of invoices"() {
        when:
        def response = mockMvc.perform(get("/invoices/list").with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        response != null
    }

    def "should update invoice"() {
        given:
        updatedInvoice.setInvoiceId(invoice.getInvoiceId())
        updatedInvoice.getIssuer().setCompanyId(invoice.getIssuer().getCompanyId())
        updatedInvoice.getReceiver().setCompanyId(invoice.getReceiver().getCompanyId())

        def updatedInvoiceAsJson = jsonService.convertToJson(updatedInvoice)

        when:
        def response = mockMvc.perform(
                put("/invoices").content(updatedInvoiceAsJson).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        updatedInvoice == jsonService.convertToObject(response, Invoice.class)
    }

    def "should return updatedInvoice by id"() {
        given:
        def id = updatedInvoice.getInvoiceId()

        when:
        def response = mockMvc.perform(get("/invoices/" + id).with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        updatedInvoice == jsonService.convertToObject(response, Invoice.class)
    }

    def "should delete invoice by id"() {
        given:
        def id = updatedInvoice.getInvoiceId()

        when:
        def response = mockMvc.perform(delete("/invoices/" + id).with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        response == "true"
    }

    def "should return empty list of invoices"() {
        when:
        def response = mockMvc.perform(get("/invoices").with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoices = jsonListService.convertToObject(response, Invoice[].class)

        then:
        invoices.size() == 0
        response == "[]"
    }
}


