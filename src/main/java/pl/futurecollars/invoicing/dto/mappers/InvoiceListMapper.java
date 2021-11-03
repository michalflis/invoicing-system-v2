package pl.futurecollars.invoicing.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.futurecollars.invoicing.dto.InvoiceListDto;
import pl.futurecollars.invoicing.model.Invoice;

@Mapper(componentModel = "spring")
public interface InvoiceListMapper {

    @Mappings(
        {
            @Mapping(source = "issuer.name", target = "issuerName"),
            @Mapping(source = "receiver.name", target = "receiverName")
        }
    )
    InvoiceListDto invoiceListToDto(Invoice invoice);
}
