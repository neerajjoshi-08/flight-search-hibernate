package com.example.utility;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Flight_Details")
public class Flight implements Iterable<String> {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	private String flightNo;

	private String departureLocation;

	private String arrivalLocation;

	private Date validTill;

	private Time flightTime;

	private Double flightDur;

	private Double fare;

	private String seatAvailability;

	private String flightClass;

	public Flight() {
	}
	
	public Flight(String departureLocation, String arrivalLocation, Date validTill, String flightClass) {
		this.departureLocation = departureLocation;
		this.arrivalLocation = arrivalLocation;
		this.validTill = validTill;
		this.flightClass = flightClass;
	}


	Flight(String flightNo, String departureLocation, String arrivalLocation, Date validTill, Time flightTime,
			Double flightDur, Double fare, String seatAvailability, String flightClass) {
		this.flightNo = flightNo;
		this.departureLocation = departureLocation;
		this.arrivalLocation = arrivalLocation;
		this.validTill = validTill;
		this.flightTime = flightTime;
		this.flightDur = flightDur;
		this.fare = fare;
		this.seatAvailability = seatAvailability;
		this.flightClass = flightClass;
	}

	public String getFlightNo() {
		return flightNo;
	}

	public String getDepartureLocation() {
		return departureLocation;
	}

	public String getArrivalLocation() {
		return arrivalLocation;
	}

	public Date getValidTill() {
		return validTill;
	}

	public Time getFlightTime() {
		return flightTime;
	}

	public Double getFlightDur() {
		return flightDur;
	}

	public Double getFare() {
		return fare;
	}

	public String getSeatAvailability() {
		return seatAvailability;
	}

	public String getFlightClass() {
		return flightClass;
	}

	/* Increasing the fare for specific conditions */
	public void setFare(Double rate) {
		double inc = 1 + rate / 100;
		this.fare = this.fare * inc;
	}

	boolean equals(final Flight flight) throws ParseException {
		boolean isEqualDepartureLocation = departureLocation.equalsIgnoreCase(flight.getDepartureLocation());
		boolean isEqualArrivaLocation = arrivalLocation.equalsIgnoreCase(flight.getArrivalLocation());
		boolean isEqualFlightDate = validTill.compareTo(flight.getValidTill()) >= 0;
		boolean isEqualFlightClass = flightClass.contains(flight.getFlightClass());

		return isEqualDepartureLocation && isEqualArrivaLocation && isEqualFlightDate && isEqualFlightClass;
	}

	@Override
	public Iterator<String> iterator() {
		Iterator<String> it = null;
		try {
			it = new FlightDataIterator();
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		return it;
	}

	/* Nested class for creating user defined iterator */
	private class FlightDataIterator implements Iterator<String> {
		private final String[] allFields;
		private int index;

		public FlightDataIterator() throws ParseException {
			this.allFields = new String[] { flightNo, departureLocation, arrivalLocation,
					new SimpleDateFormat("dd-MM-yyyy")
							.format(new SimpleDateFormat("yyyy-MM-dd").parse(validTill.toString())).toString(),
					flightTime.toString().substring(0, 2) + flightTime.toString().substring(3, 5),
					String.format("%.2f", flightDur), String.format("%.2f", fare), seatAvailability, flightClass };
			this.index = 0;
		}

		@Override
		public boolean hasNext() {
			return index != allFields.length;
		}

		@Override
		public String next() {
			return allFields[index++];
		}
	}
}