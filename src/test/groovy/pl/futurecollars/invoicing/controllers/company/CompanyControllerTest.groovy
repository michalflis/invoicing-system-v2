package pl.futurecollars.invoicing.controllers.company;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.dto.CompanyDto
import pl.futurecollars.invoicing.dto.mappers.CompanyMapper
import pl.futurecollars.invoicing.fixtures.CompanyFixture
import pl.futurecollars.invoicing.service.company.CompanyService
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
class CompanyControllerTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService<CompanyDto> jsonService

    @Autowired
    private JsonService<CompanyDto[]> jsonListService

    @Autowired
    private CompanyMapper companyMapper

    @Autowired
    private CompanyService companyService

    @Shared
    def company = CompanyFixture.company(1)
    def updatedCompany = CompanyFixture.company(1)
    def companyDto = new CompanyDto(company.getCompanyId(), company.getTaxIdentificationNumber(), company.getAddress(),
            company.getName(), company.getHealthyInsurance(), company.getPensionInsurance())
    def updatedCompanyDto = new CompanyDto(updatedCompany.getCompanyId(), updatedCompany.getTaxIdentificationNumber(), updatedCompany.getAddress(),
            updatedCompany.getName(), updatedCompany.getHealthyInsurance(), updatedCompany.getPensionInsurance())

    @Shared
    UUID id

    def "should add single company"() {
        given:
        companyService.clear()
        def companyAsJson = jsonService.convertToJson(companyDto)

        when:
        def response = mockMvc.perform(
                post("/companies").content(companyAsJson).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        id = jsonService.convertToObject(response, CompanyDto.class).getCompanyId()
        companyDto.setCompanyId(id)

        then:
        companyDto == jsonService.convertToObject(response, CompanyDto.class)
    }

    def "should return list of companies"() {
        when:
        def response = mockMvc.perform(get("/companies").with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def companies = jsonListService.convertToObject(response, CompanyDto[].class)
        companyDto.setCompanyId(companies[0].getCompanyId());
        then:
        companies.size() > 0
        companies.contains(companyDto)
    }

    def "should return short list of companies"() {
        when:
        def response = mockMvc.perform(get("/companies/list"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        response != null
    }

    def "should update company"() {
        given:
        updatedCompanyDto.setCompanyId(id)
        def updatedInvoiceAsJson = jsonService.convertToJson(updatedCompanyDto)

        when:
        def response = mockMvc.perform(
                put("/companies").content(updatedInvoiceAsJson).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        updatedCompanyDto == jsonService.convertToObject(response, CompanyDto.class)
    }

    def "should return updatedCompany by id"() {
        when:
        def response = mockMvc.perform(get("/companies/" + id).with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        updatedCompanyDto.getTaxIdentificationNumber() == jsonService.convertToObject(response, CompanyDto.class).getTaxIdentificationNumber()
    }

    def "should delete company by id"() {
        when:
        def response = mockMvc.perform(delete("/companies/" + id).with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        response == "true"
    }

    def "should return empty list of companies"() {
        when:
        def response = mockMvc.perform(get("/companies").with(csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def companies = jsonListService.convertToObject(response, CompanyDto[].class)

        then:
        companies.size() == 0
        response == "[]"
    }
}
