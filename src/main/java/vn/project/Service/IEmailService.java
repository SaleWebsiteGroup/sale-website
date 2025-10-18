package vn.project.Service;

public interface IEmailService {
	void sendOrderConfirmation(String toEmail, String subject, String content);
}
