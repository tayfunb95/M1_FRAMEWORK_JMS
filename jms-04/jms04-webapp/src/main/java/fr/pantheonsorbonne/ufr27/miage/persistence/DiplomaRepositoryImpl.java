package fr.pantheonsorbonne.ufr27.miage.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fr.pantheonsorbonne.ufr27.miage.DiplomaInfo;
import fr.pantheonsorbonne.ufr27.miage.Student;
import fr.pantheonsorbonne.ufr27.miage.dto.BinaryDiplomaDTO;
import fr.pantheonsorbonne.ufr27.miage.jms.BinaryDiplomaManager;

@ManagedBean
@ApplicationScoped
public class DiplomaRepositoryImpl implements DiplomaRepository {

	@Inject
	BinaryDiplomaManager handler;

	@PostConstruct
	void init() {
		Thread listener = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						BinaryDiplomaDTO dto = handler.consume();
						DiplomaInfo info = diplomas.keySet().stream().filter(d -> d.getId() == dto.getId()).findAny()
								.orElseThrow(() -> new NoSuchElementException());
						diplomas.put(info, dto.getData());
					} catch (NoSuchElementException e) {
						System.err.println("received diploma for a request that isn't ours");
					}
				}

			}
		});

		listener.start();
	}

	Map<DiplomaInfo, byte[]> diplomas = new HashMap<DiplomaInfo, byte[]>();

	public DiplomaRepositoryImpl() {
		DiplomaInfo dummy = new DiplomaInfo();
		dummy.setGrade(10);
		dummy.setId(1);
		dummy.setMajor("CS");
		Student student = new Student();
		student.setId(2606);
		student.setFirstName("Nicolas");
		student.setLastName("Herbaut");
		dummy.setStudent(student);
		diplomas.put(dummy, "coucou".getBytes());

	}

	@Override
	public byte[] getDiploma(DiplomaInfo info) {
		if (!diplomas.containsKey(info)) {
			diplomas.put(info, new byte[0]);
			handler.requestBinDiploma(info);
			return null;
		} else {
			byte[] data = diplomas.get(info);
			if (data.length == 0) {
				return null;
			} else {
				return data;
			}
		}

	}

	@Override
	public byte[] getDiploma(Integer id) {
		Optional<DiplomaInfo> diplomaSpec = diplomas.keySet().stream().filter(k -> k.getId() == id).findFirst();
		if (!diplomaSpec.isPresent())
			return null;
		else {

			return diplomas.get(diplomaSpec.get());
		}

	}

}
