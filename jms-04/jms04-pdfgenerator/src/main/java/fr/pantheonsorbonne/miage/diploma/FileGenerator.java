package fr.pantheonsorbonne.miage.diploma;

/**
 * an object able to be serialized as a File
 * 
 * @author nherbaut
 *
 */
public abstract class FileGenerator<GENERATOR extends DiplomaGenerator> {

	protected final GENERATOR generator;

	public FileGenerator(GENERATOR t) {
		this.generator = t;
	}

	public abstract void generateFile(String outputFile);
}
