package pl.futurecollars.invoicing.service.invoice;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.repository.generic.GenericRepository;
import pl.futurecollars.invoicing.repository.invoice.InvoiceRepository;

@AllArgsConstructor
@Service
public class InvoiceService implements GenericRepository<Invoice> {

    private final InvoiceRepository invoiceRepository;

    @Override
    public Invoice save(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice getById(UUID id) {
        return invoiceRepository.findById(id).orElseThrow(() -> new RuntimeException("Invoice Id: " + id + " does not exist"));
    }

    @Override
    public List<Invoice> getAll() {
        List<Invoice> invoiceList = new ArrayList<>();
        invoiceRepository.findAll().forEach(invoiceList::add);
        return invoiceList;
    }

    @Override
    public Invoice update(Invoice updatedInvoice) {
        if (invoiceRepository.findById(updatedInvoice.getInvoiceId()).isPresent()) {
            return invoiceRepository.save(updatedInvoice);
        }
        return null;
    }

    @Override
    public boolean delete(UUID id) {
        if (invoiceRepository.findById(id).isPresent()) {
            invoiceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        invoiceRepository.deleteAll();
    }
}
