package vn.project.Service.Impl;

public class EmailConfig {
	private static EmailConfig instance;
	private String emailFrom;
	private String emailSubjectPrefix;

	private EmailConfig() {
		this.emailFrom = "phanuan028@gmail.com";
		this.emailSubjectPrefix = "Mã OTP của bạn là: ";
	}

	public static EmailConfig getInstance() {
		if (instance == null) {
			synchronized (EmailConfig.class) {
				if (instance == null) {
					instance = new EmailConfig();
				}
			}
		}
		return instance;
	}

	public String getEmailFrom() {
		return emailFrom;
	}

	public String getEmailSubjectPrefix() {
		return emailSubjectPrefix;
	}
}
