package jpa;

public class CommonData {

	private static final String PERSISTENCE_UNIT_NAME = "VONATDB";
	private static final String DBASE_DIRECTORY = "C:\\Users\\Tomi\\Documents\\MEGA\\Szakirány labor 1\\IIT1\\laborfeladat_2\\db";
	
	public static String getDir() {
		return DBASE_DIRECTORY;
	}
	public static String getUnit() {
		return PERSISTENCE_UNIT_NAME;
	}

}
