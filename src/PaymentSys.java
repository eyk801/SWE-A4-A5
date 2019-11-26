import java.util.Random;

/**
 * @author      Ali Eshghi, Ester Zhao
 * @version     1.0
 */
public class PaymentSys {
	
	/**
	 * Payment System constructor.
	 */
	public PaymentSys() {
		
	}
	
	/**
	 * Validate input credit card information. Validates 90% of requests.
	 * </p>
	 * @param creditNum			credit card number
	 * @param CVV				credit card CVV
	 * @param expirationDate	credit card expiration date
	 * @return
	 */
	public boolean validate(long creditNum, int CVV, String expirationDate) {
		Random rand = new Random();
		int i = rand.nextInt(10);
		if (i != 0) {
			return true;
		} else {
			return false;
		}
	}
}
