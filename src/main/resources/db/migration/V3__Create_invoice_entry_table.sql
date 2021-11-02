CREATE TABLE public.invoice_entries
(
entry_id serial NOT NULL PRIMARY KEY,
description varchar(200) NOT NULL,
price numeric(10,2) NOT NULL,
quantity numeric(10,2) NOT NULL,
unit varchar(20),
total_price numeric(10,2) NOT NULL,
vat_rate varchar(10) NOT NULL,
vat_value numeric(10,2) NOT NULL,
car_used_for_personal_reason boolean NOT NULL DEFAULT FALSE,
car_registration_number varchar(20),
invoice_id uuid,
CONSTRAINT invoice_fk FOREIGN KEY(invoice_id) REFERENCES public.invoices(invoice_id)
);

