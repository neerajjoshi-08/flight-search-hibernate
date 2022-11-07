package com.example.utility;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.hibernate.Session;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

public class FileResources extends Thread {
	private static Session session = HibernateUtility.buildSessionFactory().openSession();
	private File file;

	public FileResources() {
		this.file = null;
	}

	public FileResources(File file) {
		this.file = file;
	}

	/* Loading and parsing the CSV file */
	synchronized private void loadFile(File file) throws IOException, CsvException {
		String filePath = file.getPath();
		FileReader fileReader = new FileReader(filePath);

		Character separator = '|'; // Separator used in file.
		CSVParser parser = new CSVParserBuilder().withSeparator(separator).build();
		CSVReader csvReader = new CSVReaderBuilder(fileReader).withCSVParser(parser).build();

		List<String[]> fileDataTemp = csvReader.readAll();
		try {
			saveDataToTable(fileDataTemp);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void saveDataToTable(List<String[]> fileDataTemp) throws ParseException {
		for (int i = 1; i < fileDataTemp.size(); ++i) {

			String[] data = fileDataTemp.get(i);
			String flightNo = data[HeaderIndex.FLIGHT_NO.index];
			String departureLocation = data[HeaderIndex.DEP_LOC.index];
			String arrivalLocation = data[HeaderIndex.ARR_LOC.index];
			Date validTill = new Date(
					new SimpleDateFormat("dd-MM-yyyy").parse(data[HeaderIndex.VALID_TILL.index]).getTime());
			Time flightTime = Time.valueOf(data[HeaderIndex.FLIGHT_TIME.index].substring(0, 2) + ":"
					+ data[HeaderIndex.FLIGHT_TIME.index].substring(2) + ":" + 0);
			Double flightDur = Double.parseDouble(data[HeaderIndex.FLIGHT_DUR.index]);
			Double fare = Double.parseDouble(data[HeaderIndex.FARE.index]);
			String seatAvailability = data[HeaderIndex.SEAT_AVAILABILITY.index];
			String flightClass = data[HeaderIndex.CLASS.index];

			/* Creating a Flight object */
			Flight flight = new Flight(flightNo, departureLocation, arrivalLocation, validTill, flightTime,
					flightDur, fare, seatAvailability, flightClass);

			synchronized (this) {
				/* Beginning the transaction */
				session.beginTransaction();

				/* Saving the flight data in table */
				session.save(flight);

				/* Committing the changes done in table */
				session.getTransaction().commit();
			}
		}
	}

	@Override
	public void run() {
		try {
			loadFile(file);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (CsvException e) {
			System.out.println(e.getMessage());
		}
	}
}