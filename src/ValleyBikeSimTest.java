import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
/**
 * ValleyBikeSim Testing Class
 * Be sure to change the path variable
 * in ValleyBikeSim before running!
 */
class ValleyBikeSimTest {
	
	static ValleyBikeSim vBSTester = ValleyBikeSim.getInstance();

	@Test
	void testReadData_validData() {
		assertNotNull(vBSTester.getBikes());
		assertNotNull(vBSTester.getStations());
		assertNotNull(vBSTester.getRides());
		assertNotNull(vBSTester.getCurrRides());
		assertNotNull(vBSTester.getMainReqs());
		assertNotNull(vBSTester.getUsers());
	}
	
	@Test
	void testCheckOutBike_rideCreated_dataUpdated() {
		String user1 = "emilykim";
		int ridesSize = vBSTester.getRides().size();
		int stationBikeSize = vBSTester.getStations().get(1).getNumBikes();
		int currentRidesSize = vBSTester.getCurrRides().size();
		
		vBSTester.checkOutBike(user1, 1);
		assertEquals(vBSTester.getStations().get(1).getNumBikes(), stationBikeSize-1);
		assertEquals(vBSTester.getCurrRides().size(), currentRidesSize+1);
		assertEquals(vBSTester.getRides().size(), ridesSize+1);
		vBSTester.checkInBike(user1, 1);
	}
	
	@Test
	void testCheckInBike_rideCreated_dataUpdated() {
		String user2 = "esterzhao";
		vBSTester.checkOutBike(user2, 1);
		
		int stationBikeSize = vBSTester.getStations().get(1).getNumBikes();
		int currentRidesSize = vBSTester.getCurrRides().size();
		
		vBSTester.checkInBike(user2, 1);
		assertEquals(vBSTester.getStations().get(1).getNumBikes(), stationBikeSize+1);
		assertEquals(vBSTester.getCurrRides().size(), currentRidesSize-1);
		
	}
	
	@Test
	void testAddStation_validStation_stationCreated() {
		int stationSize = vBSTester.getStations().size();
		int lastId = vBSTester.getLastStationId();
		vBSTester.addStation(10, true, "testStationAddress", "testStationName");
		assertEquals(vBSTester.getStations().size(), stationSize +1);
		assertTrue(vBSTester.getStations().containsKey(lastId+1));
	}
	
	@Test
	void testCreateUser_validUser_userCreated() {
		String username = "alieshghi";
		String password = "password3";
		int membership = 0;
		long cardNum = 1234567891;
		int cVV = 123;
		String expDate = "11/25";
		int usersSize = vBSTester.getUsers().size();
		vBSTester.createUser(username, password, membership, cardNum, cVV, expDate);
		assertEquals(vBSTester.getUsers().size(), usersSize+1);
		assertTrue(vBSTester.getUsers().containsKey(username));
	}
	
	@Test
	void testResolveIssues_validAndInvalidIssue_validIssueResolved() {
		int issueSize = vBSTester.getMainReqs().size();
		int issue1 = 1;
		int invalidIssue = 10;
		ArrayList<Integer> issues = new ArrayList<>();
		issues.add(issue1);
		issues.add(invalidIssue);
		
		vBSTester.resolveIssues(issues);
		assertEquals(vBSTester.getMainReqs().size(), issueSize-1);
		assertNull(vBSTester.getMainReqs().get(1));
	}
	
	@Test
	void testReportIssue_validIssue_issueAdded() {
		int issueSize = vBSTester.getMainReqs().size();
		int lastIssueID = vBSTester.getLastMainReqId();
		
		vBSTester.reportIssue("emilykim", 1, "Test issue");
		assertEquals(vBSTester.getMainReqs().size(), issueSize+1);
		assertNotNull(vBSTester.getMainReqs().get(lastIssueID+1));
	}
	
	@Test
	void testAddBikes_bikesAdded() {
		int numStationBikes = vBSTester.getStations().get(1).getNumBikes();
		int numTotalBikes = vBSTester.getBikes().size();
		int lastBikeId = vBSTester.getLastBikeId();
		vBSTester.addBikes(1, 5);
		
		assertEquals(vBSTester.getStations().get(1).getNumBikes(), numStationBikes+5);
		assertEquals(vBSTester.getBikes().size(), numTotalBikes+5);
		assertEquals(vBSTester.getLastBikeId(), lastBikeId+5);
	}
	
	@Test
	void testAddBikes_tooManyBikes_bikesNotAdded() {
		int numStation1Bikes = vBSTester.getStations().get(1).getNumBikes();
		int numTotalBikes = vBSTester.getBikes().size();
		int station1AvDocks = vBSTester.getStations().get(1).getAvDocks();
		int lastBikeId = vBSTester.getLastBikeId();
		
		vBSTester.addBikes(1, station1AvDocks+1);
		
		assertEquals(vBSTester.getStations().get(1).getNumBikes(), numStation1Bikes);
		assertEquals(vBSTester.getBikes().size(), numTotalBikes);
		assertEquals(vBSTester.getLastBikeId(), lastBikeId);
		assertEquals(vBSTester.getStations().get(1).getAvDocks(), station1AvDocks);
	}
	
	@Test
	/**
	 * It is difficult to test the equalize method 
	 * so we chose to start by setting two stations
	 * Stations 1 and 3 to 100% capacity
	 * and stations 2 and 4 to 0 based on stations data file
	 * Then, we checked that after running equalize stations
	 * stations 1 and 3 had decreased in bike to capacity ratio
	 * and stations 2 and 4 had increased in bike to capacity ratio
	 */
	void testEqualizeStations_stationsEqualized() {
		int numStations = vBSTester.getStations().size();
		vBSTester.readStationData();
		
		for (int i=0; i <numStations; i=i+2) {
			int AvDocks = vBSTester.getStations().get(i+1).getAvDocks();
			vBSTester.addBikes(i+1, AvDocks);
		}
		
		assertEquals(vBSTester.getStations().size(), 4);
		
		int numStation1Bikes = vBSTester.getStations().get(1).getNumBikes();
		int numStation2Bikes = vBSTester.getStations().get(2).getNumBikes();
		int numStation3Bikes = vBSTester.getStations().get(3).getNumBikes();
		int numStation4Bikes = vBSTester.getStations().get(4).getNumBikes();

		int numStation1Capacity = vBSTester.getStations().get(1).getCapacity();
		int numStation2Capacity = vBSTester.getStations().get(2).getCapacity();
		int numStation3Capacity = vBSTester.getStations().get(3).getCapacity();
		int numStation4Capacity = vBSTester.getStations().get(4).getCapacity();
		
		int station1Percentage = (int)(((float) numStation1Bikes / numStation1Capacity * 100));
		int station2Percentage = (int)(((float) numStation2Bikes / numStation2Capacity * 100));
		int station3Percentage = (int)(((float) numStation3Bikes / numStation3Capacity * 100));
		int station4Percentage = (int)(((float) numStation4Bikes / numStation4Capacity * 100));

		vBSTester.equalizeStations();
		
		int finalNumStation1Bikes = vBSTester.getStations().get(1).getNumBikes();
		int finalNumStation2Bikes = vBSTester.getStations().get(2).getNumBikes();
		int finalNumStation3Bikes = vBSTester.getStations().get(3).getNumBikes();
		int finalNumStation4Bikes = vBSTester.getStations().get(4).getNumBikes();

		int finalNumStation1Capacity = vBSTester.getStations().get(1).getCapacity();
		int finalNumStation2Capacity = vBSTester.getStations().get(2).getCapacity();
		int finalNumStation3Capacity = vBSTester.getStations().get(3).getCapacity();
		int finalNumStation4Capacity = vBSTester.getStations().get(4).getCapacity();
		
		int finalStation1Percentage = (int)(((float) finalNumStation1Bikes / finalNumStation1Capacity * 100));
		int finalStation2Percentage = (int)(((float) finalNumStation2Bikes / finalNumStation2Capacity * 100));
		int finalStation3Percentage = (int)(((float) finalNumStation3Bikes / finalNumStation3Capacity * 100));
		int finalStation4Percentage = (int)(((float) finalNumStation4Bikes / finalNumStation4Capacity * 100));
		
		assertTrue(station1Percentage > finalStation1Percentage);
		assertTrue(station2Percentage < finalStation2Percentage);
		assertTrue(station3Percentage > finalStation3Percentage);
		assertTrue(station4Percentage < finalStation4Percentage);
		

	}
}
