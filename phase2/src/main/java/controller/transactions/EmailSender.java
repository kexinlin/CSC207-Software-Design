//package controller.transactions;
//import model.persons.*;
//import model.transactors.Account;
//import model.transactors.CreditCardAccount;
//
//import java.util.*;
//import javax.mail.*;
//import javax.mail.internet.*;
//import javax.activation.*;
//
//public class EmailSender {
//	private String from = "csc207atm@gmail.com";
//	private String fromPwd = "123QWE123QWE";
//	private String fromHost = "smtp.gmail.com";
//
//	public EmailSender() {
//	}
//
//
//	/**
//	 * Sent user its creditcard bill
//	 * @param acc a creditcard account which its bill date reached
//	 */
//	public void sendCreditCardBill(CreditCardAccount acc) {
//		AccountOwner user = acc.getOwner();
//		if (!user.getEmail().equals(null)) {
//			Properties props = System.getProperties();
//			props.setProperty("smtp.gmail.com", "localHost");
//			Session session = Session.getDefaultInstance(props);
//			try {
//				MimeMessage message = new MimeMessage(session);
//				message.setFrom(new InternetAddress(from));
//				message.addRecipient(Message.RecipientType.TO,
//					new InternetAddress(user.getEmail()));
//				String mail = String.format("Hi , \n Your creditcard has a new balance of %d! \n Please pay" +
//					"your balance as soon as possible !",acc.getBalance() );
//				message.setSubject("Your CreditCard Bill is ready");
//				message.setText(mail);
//				Transport.send(message);
//				System.out.println("Sent message successfully....");
//			} catch (MessagingException mex) {
//				mex.printStackTrace();
//			}
//		}
//	}
//}
//
