package fr.pantheonsorbonne.miage.diploma;

public abstract class DiplomaGeneratorDecorator extends AbstractDiplomaGenerator {

	protected DiplomaGenerator other;

	public DiplomaGeneratorDecorator(DiplomaGenerator other) {
		this.other = other;
	}

}
