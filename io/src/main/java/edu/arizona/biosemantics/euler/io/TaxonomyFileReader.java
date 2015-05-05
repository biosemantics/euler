package edu.arizona.biosemantics.euler.io;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;

import edu.arizona.biosemantics.euler.alignment.shared.model.Taxonomy;

public class TaxonomyFileReader extends TaxonomyReader {

	private String file;

	public TaxonomyFileReader(String file) {
		this.file = file;
	}
	
	public Taxonomy read() throws Exception {
		try(InputStream inputStream = new FileInputStream(file)) {
			try(BOMInputStream bOMInputStream = new BOMInputStream(inputStream)) {
				ByteOrderMark bom = bOMInputStream.getBOM();
			    String charsetName = bom == null ? StandardCharsets.UTF_8.name() : bom.getCharsetName();
				String content = IOUtils.toString(bOMInputStream, charsetName);
				return super.read(content);
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		TaxonomyFileReader fileTaxonomyReader = new TaxonomyFileReader("C:/Users/rodenhausen/etcsite/users/1068/euler/1.txt");
		Taxonomy t = fileTaxonomyReader.read();
	}
	
}
