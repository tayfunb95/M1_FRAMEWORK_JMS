package fr.pantheonsorbonne.miage.persistence;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import fr.pantheonsorbonne.ufr27.miage.Student;

public class StudentRepository implements Iterable<Student> {

	private String db;
	private java.util.Iterator<Student> currentIterator = null;

	private StudentRepository(String db) {
		this.db = db;
	};

	public static StudentRepository withDB(String db) {
		return new StudentRepository(db);
	}

	public StudentRepository add(Student s) {
		try (FileWriter writer = new FileWriter(this.db)) {
			CSVPrinter csvFilePrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

			List<?> lst = StreamSupport.stream(this.spliterator(), false)
					.map((student -> Arrays.asList(student.getId(), student.getFirstName(), student.getLastName())))
					.collect(Collectors.toList());
			for (Object o : lst) {
				csvFilePrinter.printRecord(o);
			}
			csvFilePrinter.close();

		} catch (IOException e) {
			throw new RuntimeException("failed to update db file");
		}
		return this;

	}

	@Override
	public java.util.Iterator<Student> iterator() {
		try (FileReader reader = new FileReader(this.db)) {

			CSVParser parser = CSVParser.parse(reader, CSVFormat.DEFAULT);
			List<CSVRecord> csvRecords = parser.getRecords();
			List<Student> students = new ArrayList<>();
			for (CSVRecord rec : csvRecords) {
				Student student = new Student();
				student.setFirstName(rec.get(0));
				student.setLastName(rec.get(1));
				student.setId(Integer.parseInt(rec.get(2)));
				students.add(student);
			}

			this.currentIterator = students.iterator();
			return this.currentIterator;

		} catch (IOException e) {
			Logger.getGlobal().info("IO PB" + e.getMessage());
			return Collections.EMPTY_SET.iterator();
		}
	}

}
