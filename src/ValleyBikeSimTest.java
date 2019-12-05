import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/**
 * ValleyBikeSim Testing Class
 * Be sure to change the path variable
 * in ValleyBikeSim before running!
 */
class ValleyBikeSimTest {
	
	static ValleyBikeSim vBSTester = ValleyBikeSim.getInstance();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testReadData_validData() {
		assertEquals(vBSTester.getBikes().size(), 3);
		assertEquals(vBSTester.getStations().size(), 2);
		assertEquals(vBSTester.getRides().size(), 3);
		assertEquals(vBSTester.getCurrRides().size(), 0);
		assertEquals(vBSTester.getMainReqs().size(), 2 );
		assertEquals(vBSTester.getUsers().size(), 2);
		
	}
	
	@Test
	void testCheckOutBike_rideCreated_dataUpdated() {
		String user1 = "emilykim";
		assertEquals(vBSTester.getStations().get(1).getNumBikes(), 3);
		vBSTester.checkOutBike(user1, 1);
		assertEquals(vBSTester.getStations().get(1).getNumBikes(), 2);
		assertEquals(vBSTester.getCurrRides().size(), 1);
		assertEquals(vBSTester.getRides().size(), 4);
		vBSTester.checkInBike(user1, 1);
	}
	
	@Test
	void testCheckInBike_rideCreated_dataUpdated() {
		int ridesSize = vBSTester.getRides().size();
		String user2 = "esterzhao";
		vBSTester.checkOutBike(user2, 1);
		assertEquals(vBSTester.getStations().get(1).getNumBikes(), 2);
		vBSTester.checkInBike(user2, 1);
		assertEquals(vBSTester.getStations().get(1).getNumBikes(), 3);
		assertEquals(vBSTester.getCurrRides().size(), 0);
		assertEquals(vBSTester.getRides().size(), ridesSize+1);
	}
}
