package com.example.application;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.example.utility.LoadResources;
import com.example.utility.Flight;
import com.example.utility.FlightSearch;
import com.example.utility.HibernateUtility;

public class FlightApp {
	public static void main(String[] args) {
		/* For hiding the log messages from console. */
//		@SuppressWarnings("unused")
//		org.jboss.logging.Logger logger = org.jboss.logging.Logger.getLogger("org.hibernate");
//		java.util.logging.Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.OFF);
		
		try (Scanner sc = new Scanner(System.in)) {
			char repeat = 'n';
			boolean isException = true;

			/* Thread handling */
			Thread t = new LoadResources();
			t.start();
			t.join();

			do {
				try {

					/* Flight Departure Location */
					System.out.print("Enter Departure Location: ");
					String departureLocation = sc.nextLine();

					/* Flight Arrival Location */
					System.out.print("Enter Arrival Location: ");
					String arrivalLocation = sc.nextLine();

					/* Flight Date */
					Date flightDate = null;
					isException = true;
					do {
						try {
							System.out.print("Enter Flight Date: ");
							flightDate = Date.valueOf(new SimpleDateFormat("yyyy-MM-dd")
									.format(new SimpleDateFormat("dd-MM-yyyy").parse(sc.nextLine())));
							isException = false;

						} catch (Exception e) {
							System.out.println("Invalid date entered!");
						}
					} while (isException);

					/* Flight Class */
					String flightClass = null;
					isException = true;
					do {
						try {
							System.out.print("Enter Flight Class (Type 'E' (=Economic) or 'B' (=Business)): ");
							flightClass = sc.nextLine();

							if (!("E".equalsIgnoreCase(flightClass)) && !("B".equalsIgnoreCase(flightClass))) {
								throw new InputMismatchException();
							}
							isException = false;

						} catch (InputMismatchException ime) {
							System.out.println("Invalid input entered!");
						}
					} while (isException);

					/* Output Preference */
					String outputPreference = null;
					isException = true;
					do {
						try {
							System.out.print(
									"Enter Output Preference (Type 'F' (=Sort by Fare) or 'B' (=Sort by both fare and duration): ");
							outputPreference = sc.nextLine();

							if (!("F".equalsIgnoreCase(outputPreference))
									&& !("B".equalsIgnoreCase(outputPreference))) {
								throw new InputMismatchException();
							}
							isException = false;

						} catch (InputMismatchException ime) {
							System.out.println("Invalid input entered!");
						}
					} while (isException);

					Flight flight = new Flight(departureLocation, arrivalLocation, flightDate, flightClass);

					FlightSearch fs = new FlightSearch();
					fs.searchFlight(flight, outputPreference);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}

				isException = true;
				do {
					try {
						System.out.print("Do you want to continue (y/n): ");
						repeat = sc.nextLine().charAt(0);

						if ((repeat != 'y') && (repeat != 'n')) {
							throw new InputMismatchException();
						}
						System.out.println();
						isException = false;

					} catch (InputMismatchException ime) {
						System.out.println("Invalid input entered!");
					}
				} while (isException);

			} while (repeat == 'y');

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		HibernateUtility.shutdown();
	}
}