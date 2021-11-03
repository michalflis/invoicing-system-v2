package pl.futurecollars.invoicing.model

import pl.futurecollars.invoicing.fixtures.CompanyFixture
import spock.lang.Specification

class CompanyTest extends Specification {

    def "should build Company object"() {
        setup:
        def company = CompanyFixture.company(1)

        when:
        def result = company

        then:
        result != null
    }
}


