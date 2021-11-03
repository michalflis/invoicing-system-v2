package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invoices")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    @ApiModelProperty(hidden = true)
    @GeneratedValue
    private UUID invoiceId;

    @ApiModelProperty(value = "Invoice number", required = true, example = "2021/09/30/000123")
    private String invoiceNumber;

    @ApiModelProperty(value = "Invoice issue date", required = true, example = "2021-09-30")
    private LocalDate issueDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "issuer_id")
    @ApiModelProperty(value = "Issuer Id", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
    private Company issuer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "receiver_id")
    @ApiModelProperty(value = "Receiver Id", required = true, example = "124e4567-f89b-12d3-a456-426614174000")
    private Company receiver;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "invoice_id")
    private List<InvoiceEntry> entries;
}

