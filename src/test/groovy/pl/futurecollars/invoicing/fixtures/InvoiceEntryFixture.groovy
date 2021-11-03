package pl.futurecollars.invoicing.fixtures

import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat

import java.math.RoundingMode

class InvoiceEntryFixture {

    static product(int id) {
        InvoiceEntry.builder()
                .entryId(0)
                .description("Product $id")
                .unit("pcs")
                .price(BigDecimal.valueOf(100 * id).setScale(2, RoundingMode.HALF_UP))
                .quantity(BigDecimal.valueOf(2).setScale(2, RoundingMode.HALF_UP))
                .totalPrice((BigDecimal.valueOf(100 * id) * BigDecimal.valueOf(2))
                        .setScale(2, RoundingMode.HALF_UP))
                .vatRate(Vat.VAT_8)
                .vatValue((BigDecimal.valueOf(100* id) * BigDecimal.valueOf(2) * BigDecimal.valueOf(0.08))
                        .setScale(2, RoundingMode.HALF_UP))
                .carRegistrationNumber(null)
                .carUsedForPersonalReason(false)
                .build()
    }

    static gasolineForTheCarUsedForPersonalReason() {
        InvoiceEntry.builder()
                .entryId(250)
                .description("Gasoline 95")
                .unit("liters")
                .price(BigDecimal.valueOf(6.19).setScale(2, RoundingMode.HALF_UP))
                .quantity(BigDecimal.valueOf(46).setScale(2, RoundingMode.HALF_UP))
                .totalPrice((BigDecimal.valueOf(6.19) * BigDecimal.valueOf(46))
                        .setScale(2, RoundingMode.HALF_UP))
                .vatRate(Vat.VAT_8)
                .vatValue((BigDecimal.valueOf(6.19) * BigDecimal.valueOf(46) * BigDecimal.valueOf(0.23))
                        .setScale(2, RoundingMode.HALF_UP))
                .carRegistrationNumber("H1 00001")
                .carUsedForPersonalReason(true)
                .build()
    }
}
