package pl.futurecollars.invoicing.controllers.invoice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.fixtures.InvoiceFixture
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.invoice.InvoiceService
import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
class InvoiceControllerExhaustiveTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private InvoiceService invoiceRepository

    def setup() { invoiceRepository.clear() }

    def cleanup() { invoiceRepository.clear() }

    @Autowired
    private JsonService<Invoice> jsonService

    @Autowired
    private JsonService<Invoice[]> jsonListService

    @Shared
    def invoice = InvoiceFixture.invoice(1)
    def invoice1 = InvoiceFixture.invoice(3)
    def invoice2 = InvoiceFixture.invoice(5)

    def "should add 3 invoices"() {
        given:
        def invoiceAsJson = jsonService.convertToJson(invoice)
        def invoice1AsJson = jsonService.convertToJson(invoice1)
        def invoice2AsJson = jsonService.convertToJson(invoice2)

        expect:
        mockMvc.perform(
                post("/invoices").content(invoiceAsJson).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk())

        mockMvc.perform(
                post("/invoices").content(invoice1AsJson).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk())

        mockMvc.perform(
                post("/invoices").content(invoice2AsJson).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk())
    }

    def "should return list of 2 invoices"() {
        given:
        def invoiceAsJson = jsonService.convertToJson(invoice)
        def invoice1AsJson = jsonService.convertToJson(invoice1)

        when:
        mockMvc.perform(
                post("/invoices").content(invoiceAsJson).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk())

        mockMvc.perform(
                post("/invoices").content(invoice1AsJson).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk())

        def response = mockMvc.perform(get("/invoices").with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoices = jsonListService.convertToObject(response, Invoice[].class)

        then:
        invoices.length == 2
    }

    def "should update not existing invoice"() {
        given:
        def invoiceAsJson = jsonService.convertToJson(invoice)

        when:
        def postResponse = mockMvc.perform(
                post("/invoices").content(invoiceAsJson).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def responseAsInvoice = jsonService.convertToObject(postResponse, Invoice.class)
        def id = responseAsInvoice.getInvoiceId()
        invoice.setInvoiceId(id)
        invoice.getIssuer().setCompanyId(responseAsInvoice.getIssuer().getCompanyId())
        invoice.getReceiver().setCompanyId(responseAsInvoice.getReceiver().getCompanyId())

        UUID updatedId
        for (updatedId = UUID.randomUUID(); updatedId == id;) {
            updatedId = UUID.randomUUID()
        }

        def updatedInvoice = InvoiceFixture.invoice(1)
        updatedInvoice.setInvoiceId(updatedId)
        def updatedInvoiceAsJson = jsonService.convertToJson(updatedInvoice)

        mockMvc.perform(
                put("/invoices").content(updatedInvoiceAsJson).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk())

        def response = mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoices = jsonListService.convertToObject(response, Invoice[].class)

        then:
        invoice != updatedInvoice
        invoices[0] == invoice
    }

    def "should delete not existing invoice"() {
        given:
        def invoiceAsJson = jsonService.convertToJson(invoice)

        when:
        def postResponse = mockMvc.perform(
                post("/invoices").content(invoiceAsJson).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def responseAsInvoice = jsonService.convertToObject(postResponse, Invoice.class)
        def id = responseAsInvoice.getInvoiceId()
        invoice.setInvoiceId(id)
        invoice.getIssuer().setCompanyId(responseAsInvoice.getIssuer().getCompanyId())
        invoice.getReceiver().setCompanyId(responseAsInvoice.getReceiver().getCompanyId())

        UUID updatedId
        for (updatedId = UUID.randomUUID(); updatedId == id;) {
            updatedId = UUID.randomUUID()
        }

        def deleteResponse = mockMvc.perform(
                delete("/invoices/" + updatedId).with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def response = mockMvc.perform(get("/invoices").with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoices = jsonListService.convertToObject(response, Invoice[].class)

        then:
        deleteResponse == "false"
        invoices[0] == invoice
    }

    def "should delete second of 3 invoices"() {
        given:
        def invoiceAsJson = jsonService.convertToJson(invoice)
        def invoice1AsJson = jsonService.convertToJson(invoice1)
        def invoice2AsJson = jsonService.convertToJson(invoice2)

        when:
        def postResponse = mockMvc.perform(
                post("/invoices").content(invoiceAsJson).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def post1Response = mockMvc.perform(
                post("/invoices").content(invoice1AsJson).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def post2Response = mockMvc.perform(
                post("/invoices").content(invoice2AsJson).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def id = jsonService.convertToObject(postResponse, Invoice.class).getInvoiceId()
        invoice.setInvoiceId(id)
        def id1 = jsonService.convertToObject(post1Response, Invoice.class).getInvoiceId()
        invoice1.setInvoiceId(id1)
        def id2 = jsonService.convertToObject(post2Response, Invoice.class).getInvoiceId()
        invoice2.setInvoiceId(id2)

        def deleteResponse = mockMvc.perform(
                delete("/invoices/" + id1).with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def response = mockMvc.perform(get("/invoices").with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoices = jsonListService.convertToObject(response, Invoice[].class)

        then:
        deleteResponse == "true"
        invoices.length == 2
    }
}


