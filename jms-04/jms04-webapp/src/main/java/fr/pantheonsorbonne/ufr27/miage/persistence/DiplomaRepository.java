package fr.pantheonsorbonne.ufr27.miage.persistence;

import java.io.ByteArrayInputStream;
import java.util.Optional;

import fr.pantheonsorbonne.ufr27.miage.DiplomaInfo;

public interface DiplomaRepository {

	public byte[] getDiploma(DiplomaInfo info);

	public byte[] getDiploma(Integer id);

}
