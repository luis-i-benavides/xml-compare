package com.example;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;

public class XmlCompareTests {

	private ClassLoader classLoader = getClass().getClassLoader();

	@Test
	public void given2XMLS_whenNotSimilarIgnoringElementOrderAndWhitespace_thenCorrect() {
		String controlXml = "<struct><int>4</int><boolean>false</boolean></struct>";
		String testXml = "<struct><boolean>false</boolean><int>4</int><string>a string</string></struct>";
		Diff myDiff = compareXML(controlXml, testXml);

		myDiff.getDifferences().iterator().forEachRemaining(System.out::println);
		assertTrue(myDiff.toString(), myDiff.hasDifferences());
	}

	@Test
	public void given2XMLFiles_whenNotSimilarIgnoringElementOrderAndWhitespace_thenCorrect() {
		String controlXml = "controlNotSimilar.xml";
		String testXml = "testNotSimilar.xml";
		Diff myDiff = compareXMLFiles(controlXml, testXml);

		myDiff.getDifferences().iterator().forEachRemaining(System.out::println);
		assertTrue(myDiff.toString(), myDiff.hasDifferences());
	}

	@Test
	public void given2XMLS_whenSimilarIgnoringElementOrderAndWhitespace_thenCorrect() {
		String controlXml = "<a><b>Test Value</b><int>3</int></a>";
		String testXml = "<a><int>3</int>\n <b>\n  Test Value\n </b>\n</a>";
		Diff myDiff = compareXML(controlXml, testXml);
		assertFalse(myDiff.toString(), myDiff.hasDifferences());
	}

	@Test
	public void given2XMLFiles_whenSimilarIgnoringElementOrderAndWhitespace_thenCorrect() {
		String controlXml = "controlSimilar.xml";
		String testXml = "testSimilar.xml";
		Diff myDiff = compareXMLFiles(controlXml, testXml);
		assertFalse(myDiff.toString(), myDiff.hasDifferences());
	}

	private Diff compareXML(String controlXml, String testXml) {
		Diff myDiff = DiffBuilder.compare(Input.fromString(controlXml)).withTest(Input.fromString(testXml))
				.checkForSimilar().withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName)).ignoreWhitespace()
				.build();
		return myDiff;
	}

	private Diff compareXMLFiles(String controlXmlFile, String testXmlFile) {
		Diff myDiff = DiffBuilder.compare(Input.from(new File(classLoader.getResource(controlXmlFile).getFile())))
				.withTest(Input.from(new File(classLoader.getResource(testXmlFile).getFile()))).checkForSimilar()
				.withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName)).ignoreWhitespace().build();
		return myDiff;
	}

}
