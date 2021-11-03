package pl.futurecollars.invoicing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfiguration {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("pl.futurecollars"))
            .paths(PathSelectors.any())
            .build()
            .tags(
                new Tag("invoice-controller", "Controller used to list, add, update and delete invoices"),
                new Tag("company-controller", "Controller used to list, add, update and delete companies"),
                new Tag("tax-calculator-controller", "Controller used to generate tax report")
            )
            .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .description("Application for comprehensive invoice management")
            .title("Invoicing System")
            .contact(
                new Contact(
                    "Michał Flis",
                    "",
                    "michalt.flis@gmail.com")
            )
            .build();
    }
}
