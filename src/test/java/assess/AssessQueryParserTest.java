package assess;

import assess.syntax.AssessQueryLexer;
import assess.syntax.AssessQueryParser;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AssessQueryParserTest {
	private AssessQueryParser createParser (String query) throws IOException {
		InputStream stream = new ByteArrayInputStream(query.getBytes(StandardCharsets.UTF_8));
		ANTLRInputStream input = new ANTLRInputStream(stream);
		AssessQueryLexer lexer = new AssessQueryLexer (input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		return new AssessQueryParser(tokens);
	}

	// This should be throwing a mismatch warning (TODO: Create custom exception?)
	@Test
	public void givenInvalidQuery_whenParsingQuery_thenThrowException() throws IOException, RecognitionException {
		String query = "with SALES by month labels quartiles";
		createParser(query).parse();
		assertTrue(true); //TODO: Catch mismatch message
	}

	// Unit Tests

	@Test
	public void givenSelectionFiltersInQuery_whenParsing_thenCollectPredicates()
			throws IOException, RecognitionException {
		String predicates = "date = '20/5/2019', type = 'Fresh Fruit', country = 'Italy'";
		AssessQueryParser parser = createParser(predicates);

		HashMap<String, String> expected = new HashMap<>();
		expected.put("date", "20/5/2019");
		expected.put("type", "Fresh Fruit");
		expected.put("country", "Italy");

		HashMap<String, String> actual = parser.selection_predicates();

		assertEquals(expected, actual);
	}

	@Test
	public void givenGroupByPredicatesInQuery_whenParsing_theCollectGroupByPredicates()
			throws IOException, RecognitionException {
		String predicates = "country, product";
		AssessQueryParser parser = createParser(predicates);

		HashSet<String> expected = new HashSet<>();
		expected.add("product");
		expected.add("country");

		HashSet<String> actual = parser.group_by_set();

		assertEquals(expected, actual);
	}

	// Integration (AssessParser and AssessBuilder) Tests

	/**
	 * In this test we use the simplest example of a valid query.
	 * The system retrieve the storeSales measurements and then labels
	 * them based on quartiles.
	 */
	@Test
	public void parseSimpleQuery() throws IOException, RecognitionException {
		String query = "with SALES by month assess storeSales labels quartiles";
		createParser(query).parse();
		assertTrue(true);
	}

	/**
	 * Using a constant benchmark, the user assess the cells of the target cube
	 * against some fixed value.
	 */
	@Test
	public void parseQueryWithConstantBenchmark() throws IOException, RecognitionException {
		String query = "with SALES by month\n" +
				"assess storeSales against 1000\n" +
				"using minMaxNorm(difference(storeSales, 1000))\n" +
				"labels stars";
		createParser(query).parse();
		assertTrue(true);
	}

	@Test
	public void parseQueryWithSiblingBenchmark() throws IOException, RecognitionException {
		String query = "with SALES for type = 'Fresh Fruit', country = 'Italy'\n" +
				"by product, country assess quantity\n" +
				"against country = 'France'\n" +
				"using percOfTotal(difference(quantity, benchmark.quantity))\n"+
				"labels {[-inf, -0.2): bad, [-0.2,0.2]: ok, (0.2, inf]: good}";
		createParser(query).parse();
		assertTrue(true);
	}

	@Test
	public void parseQueryWithPastBenchmark() throws IOException, RecognitionException {
		String query = "with SALES " +
				"for month = '1997/07', store = 'SmartMart'\n" +
				"by month, store, date\n" +
				"assess storeSales against past 4 \n" +
				"using ratio(storeSales, benchmark.storeSales)\n"+
				"labels {[0, 0.9): worse, [0.9, 1.1]: fine, (1.1,inf): better}";
		createParser(query).parse();
		assertTrue(true);
	}
}