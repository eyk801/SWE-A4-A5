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
		List<Integer> bikeIds1 = new ArrayList<>();
		Station station1 = new Station(1, 10, 20, true,
	            "testAddress1", "testAddress2", bikeIds1);
		Map<Integer, Station> stations = new HashMap<>();
		stations.put(1, station1);
		
		User user1 = new User("testUser1", "testPassword1", 0, 
				1234567890, 122, "12/25");
		Map<String, User> users = new HashMap<>();
		users.put("testUser1", user1);
		
		vBSTester.setStations(stations);
		vBSTester.setUsers(users);
		vBSTester.setBikes(new HashMap<>());
		vBSTester.setCurrRides(new ArrayList<>());
		vBSTester.setMainReqs(new HashMap<>());
		vBSTester.setLastBikeId(0);
		vBSTester.setLastMainReqId(0);
		vBSTester.setLastRideId(0);
		vBSTester.setLastStationId(1);
		vBSTester.setRides(null);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testCreateStation() {
		assertFalse(vBSTester.stationExists(2));
		vBSTester.addStation(10, false, "testAddress2", "testStation2");
		assertTrue(vBSTester.stationExists(2));
		assertEquals(vBSTester.getStations().get(2).getName(), "testStation2");
	}
	
	@Test
	void testAddBikes() {
		assertEquals(vBSTester.getBikes().size(), 0);
		assertEquals(vBSTester.getStations().get(1).getNumBikes(), 0);
		vBSTester.addBikes(1, 10);
		assertEquals(vBSTester.getBikes().size(), 10);
		assertEquals(vBSTester.getStations().get(1).getNumBikes(), 10);
	}

}
