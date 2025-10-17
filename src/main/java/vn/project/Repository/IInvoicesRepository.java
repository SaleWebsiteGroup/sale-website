package vn.project.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.project.Entity.Invoices;


@Repository
public interface IInvoicesRepository extends JpaRepository<Invoices, Integer>{

	List<Invoices> findByInvoiceid(int invoiceid);

	List<Invoices> findByTotalamount(long totalamount);
}
