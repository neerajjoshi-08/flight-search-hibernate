package com.example.utility;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

public class FlightSearch {
	private final static int CNT_HEADER = 9;
	private int[] maxLength = new int[CNT_HEADER];

	/* Searching flight and getting the data from table */
	public void searchFlight(final Flight flight, final String outputPreference)
			throws RuntimeException, ParseException {

		Session session = HibernateUtility.getSessionFactory().openSession();

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Flight> cq = cb.createQuery(Flight.class);
		Root<Flight> rootEntry = cq.from(Flight.class);
		CriteriaQuery<Flight> allData = cq.select(rootEntry);
		TypedQuery<Flight> allQuery = session.createQuery(allData);
		List<Flight> dataFromTable = allQuery.getResultList();

		List<Flight> result = new ArrayList<>();

		for (Flight Flight : dataFromTable) {
			if (Flight.equals(flight)) {
				if (Flight.getFlightClass().contains("B")) {
					Flight.setFare(40.0); // Increasing the fare.
				}

				result.add(Flight);
			}
		}

		display(result, outputPreference);
	}

	/* Calculating max length of a cell */
	private void calculatingCell(final List<Flight> result) {
		int idx = 0;
		for (HeaderIndex hi : HeaderIndex.values()) {
			maxLength[idx] = Math.max(maxLength[idx], hi.toString().length());
			idx++;
		}

		for (Flight fd : result) {
			idx = 0;
			for (String s : fd) {
				maxLength[idx] = Math.max(maxLength[idx], s.length());
				idx++;
			}
		}
	}

	/* Displaying the output */
	private void display(final List<Flight> result, final String outputPreference) {
		calculatingCell(result);

		if ("F".equals(outputPreference)) {
			Collections.sort((List<Flight>) result, new SortByFare());
		} else if ("B".equals(outputPreference)) {
			Collections.sort((List<Flight>) result, new SortByBoth());
		}

		if (result.size() > 0) {
			System.out.println("\nOutput:");
			displayHeader();
			for (Flight fd : result) {
				displayData(fd);
				System.out.println();
			}
			decorative("-", 93);
			System.out.println();
		} else {
			throw new RuntimeException("Provided data for the flight not found!");
		}
	}

	/* Displaying the header in a pretty manner */
	private void displayHeader() {
		decorative("-", 93);
		System.out.println();
		System.out.print('|');

		int idx = 0;
		for (HeaderIndex hi : HeaderIndex.values()) {
			int len = hi.toString().length();
			decorative(" ", (Math.floor(maxLength[idx] - len) / 2.0));
			System.out.print(hi);
			decorative(" ", (Math.ceil(maxLength[idx] - len + 1) / 2.0));
			System.out.print('|');
			idx++;
		}
		System.out.println();
		decorative("-", 93);
		System.out.println();
	}

	/* Displaying the data */
	private void displayData(final Flight fd) {
		System.out.print('|');
		int idx = 0;
		for (String s : fd) {
			int len = s.length();
			decorative(" ", (Math.floor(maxLength[idx] - len) / 2.0));
			System.out.print(s);
			decorative(" ", (Math.ceil(maxLength[idx] - len + 1) / 2.0));
			System.out.print('|');
			idx++;
		}
	}

	/* For displaying a passed string up to passed integer parameter */
	private void decorative(final String s, final double times) {
		int intTimes = (int) times;
		for (int i = 0; i < intTimes; ++i) {
			System.out.print(s);
		}
	}

	/* Sorting based on Flight fare */
	private class SortByFare implements Comparator<Flight> {
		public int compare(Flight f1, Flight f2) {
			double f1Fare = f1.getFare();
			double f2Fare = f2.getFare();

			if (f1Fare > f2Fare) {
				return 1;
			} else if (f1Fare < f2Fare) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	/* Sorting based on Flight fare and duration */
	private class SortByBoth implements Comparator<Flight> {
		public int compare(Flight f1, Flight f2) {
			double f1Fare = f1.getFare();
			double f2Fare = f2.getFare();

			if (f1Fare > f2Fare) {
				return 1;
			} else if (f1Fare < f2Fare) {
				return -1;
			} else {
				double f1FlightDur = f1.getFlightDur();
				double f2FlightDur = f2.getFlightDur();

				if (f1FlightDur > f2FlightDur) {
					return 1;
				} else if (f1FlightDur < f2FlightDur) {
					return -1;
				} else {
					return 0;
				}
			}
		}
	}
}