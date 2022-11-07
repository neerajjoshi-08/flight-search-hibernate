package com.example.utility;

public enum HeaderIndex {
	FLIGHT_NO(0), DEP_LOC(1), ARR_LOC(2), VALID_TILL(3), FLIGHT_TIME(4), FLIGHT_DUR(5), FARE(6),
	SEAT_AVAILABILITY(7), CLASS(8);
	
	final public int index;

	private HeaderIndex(int index) {
		this.index = index;
	}
}