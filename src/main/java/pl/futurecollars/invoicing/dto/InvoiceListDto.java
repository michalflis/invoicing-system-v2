package pl.futurecollars.invoicing.dto;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvoiceListDto {

    @ApiModelProperty(value = "Invoice number", required = true, example = "2021/09/30/000123")
    private String invoiceNumber;

    @ApiModelProperty(value = "Invoice issue date", required = true, example = "2021-09-30")
    private LocalDate issueDate;

    @ApiModelProperty(value = "Issuer name", required = true)
    private String issuerName;

    @ApiModelProperty(value = "Receiver name", required = true)
    private String receiverName;

}
