package pl.futurecollars.invoicing.service.company

import pl.futurecollars.invoicing.fixtures.CompanyFixture
import pl.futurecollars.invoicing.repository.company.CompanyRepository
import spock.lang.Specification

class CompanyServiceTest extends Specification {

    CompanyRepository companyRepository = Mock()

    def companyService = new CompanyService(companyRepository)
    def company = CompanyFixture.company(1)
    def company2 = CompanyFixture.company(2)
    def company3 = CompanyFixture.company(3)
    def updatedCompany = CompanyFixture.company(4)

    def "should save company in to database"() {
        setup:
        companyRepository.save(company) >> company
        companyRepository.findById(company.getCompanyId()) >> Optional.of(company)

        when:
        def result = companyService.save(company)

        then:
        companyService.getById(result.getCompanyId()) != null
        companyService.getById(result.getCompanyId()).getName() == "Company 1"
    }

    def "should get company from database by Id"() {
        setup:
        companyRepository.findById(company.getCompanyId()) >> Optional.of(company)

        when:
        def result = companyService.getById(company.getCompanyId())

        then:
        result != null
        result.getName() == "Company 1"
    }

    def "should get list of all companies from database"() {
        setup:
        companyRepository.findAll() >> [company, company2, company3]

        when:
        def result = companyService.getAll()

        then:
        result.size() == 3
    }

    def "should update company in the database"() {
        setup:
        updatedCompany.setCompanyId(company.getCompanyId())
        companyRepository.save(updatedCompany) >> updatedCompany
        companyRepository.findById(company.getCompanyId()) >> Optional.of(updatedCompany)

        when:
        def result = companyService.update(updatedCompany)

        then:
        companyService.getById(result.getCompanyId()) != null
        companyService.getById(result.getCompanyId()).getName() == "Company 4"
    }

    def "should delete company from database"() {
        setup:
        companyRepository.findById(company.getCompanyId()) >> Optional.of(company)
        companyRepository.findAll() >> []

        when:
        def result = companyService.delete(company.getCompanyId())

        then:
        result
        companyService.getAll().size() == 0
    }
}



