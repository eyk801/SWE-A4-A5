import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * @author      Ali Eshghi, Ester Zhao, Emily Kim
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
		if (checkExprDate(expirationDate)) {
			Random rand = new Random();
			int i = rand.nextInt(10);
			if (i != 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if the expiration date has passed.
	 * </p>
	 * @param expirationDate
	 * @return false if expiration date has passed
	 */
	public boolean checkExprDate(String expirationDate) {
		// Response bool
		boolean response = false;
		// Parse String expiration date
		String[] arrDate = expirationDate.split("/");
	    String userMonth = arrDate[0];
	    String userYear = arrDate[1];
		
	    // Get current date/time
     	SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yy");
     	String monthYear = dateFormat.format(new Date());
     	String[] currDate = monthYear.split("/");
     	String currMonth = currDate[0];
     	String currYear = currDate[1];
		// Set months and years to integers
     	int userMonthNum = Integer.parseInt(userMonth);
    	int userYearNum = Integer.parseInt(userYear);
     	int currMonthNum = Integer.parseInt(currMonth);
     	int currYearNum = Integer.parseInt(currYear);
     	// Checks if dates have passed
     	if (currYearNum > userYearNum){
     		response = false;
     	}
     	else if (currYearNum < userYearNum){
     		response = true;
     	}
     	else if ( currYearNum == userYearNum ){
     		if (currMonthNum >= userMonthNum){
     			response = false;
     		}
     		else if (currMonthNum < userMonthNum){
     			response = true;
     		}
     	}
     	return response;
	}
}
