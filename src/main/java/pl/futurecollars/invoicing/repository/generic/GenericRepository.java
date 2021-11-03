package pl.futurecollars.invoicing.repository.generic;

import java.util.List;
import java.util.UUID;

public interface GenericRepository<T> {

    T save(T object);

    T getById(UUID id);

    List<T> getAll();

    T update(T updatedInvoice);

    boolean delete(UUID id);

    void clear();
}
