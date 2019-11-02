package fr.pantheonsorbonne.ufr27.miage.dto;

public class BinaryDiplomaDTO {
	byte data[];
	int id;

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
