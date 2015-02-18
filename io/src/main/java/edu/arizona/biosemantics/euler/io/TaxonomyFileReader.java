package edu.arizona.biosemantics.euler.io;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import edu.arizona.biosemantics.euler.alignment.shared.model.Taxonomy;

public class TaxonomyFileReader extends TaxonomyReader {

	private String file;

	public TaxonomyFileReader(String file) {
		this.file = file;
	}
	
	public Taxonomy read() throws Exception {
		byte[] encoded = Files.readAllBytes(Paths.get(file));
		String content = new String(encoded, StandardCharsets.UTF_8);
		return super.read(content);
	}
	
	public static void main(String[] args) throws Exception {
		TaxonomyFileReader fileTaxonomyReader = new TaxonomyFileReader("C:/Users/rodenhausen/etcsite/users/1068/euler/1.txt");
		Taxonomy t = fileTaxonomyReader.read();
	}
	
}
