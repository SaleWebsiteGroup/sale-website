package vn.project.Service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.project.Entity.Invoices;
import vn.project.Repository.IInvoicesRepository;
import vn.project.Service.IInvoicesService;

@Service
public class InvoicesService implements IInvoicesService{

	@Autowired
	IInvoicesRepository invoiceRepository;

	@Override
	public <S extends Invoices> S save(S entity) {
		return invoiceRepository.save(entity);
	}

	@Override
	public List<Invoices> findAll() {
		return invoiceRepository.findAll();
	}

	@Override
	public Optional<Invoices> findById(Integer id) {
		return invoiceRepository.findById(id);
	}

	@Override
	public long count() {
		return invoiceRepository.count();
	}

	@Override
	public void deleteById(Integer id) {
		Optional<Invoices> invoice = invoiceRepository.findById(id);
		if(invoice.isPresent()) {
			invoiceRepository.deleteById(id);
		}
	}

	@Override
	public void delete(Invoices entity) {
		Optional<Invoices> invoice = invoiceRepository.findById(entity.getInvoiceid());
		if(invoice.isPresent()) {
			Invoices invoicepresent = invoice.get();
			invoiceRepository.delete(invoicepresent);
		}
	}

	@Override
	public void deleteAll() {
		invoiceRepository.deleteAll();
	}


}
