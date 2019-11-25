import java.util.Random;

public class PaymentSys {
	
	/**
	 * Payment System class constructor
	 * 
	 * TODO: More specific methods? Decide how to use this
	 */
	public PaymentSys() {
		
	}
	
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
