package org.brainfarm.java.feat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.brainfarm.java.feat.api.IEvolutionContext;
import org.brainfarm.java.feat.api.IEvolutionController;
import org.brainfarm.java.feat.api.params.IEvolutionParameters;
import org.junit.Test;

public class EvolutionControllerTest {

	@Test
	public void testLoadDefaultEvolutionParameters() {
		
		IEvolutionContext context = new EvolutionContext();
		
		IEvolutionController controller = new EvolutionController(context);
		
		controller.loadEvolutionParameters(null);
		
		IEvolutionParameters parameters = context.getEvolutionParameters();
		
		// Check the default parameters are loaded into the context.
		assertNotNull(parameters);
		
		// At the moment i'm only testing a single parameter's default value.
		assertNotNull(parameters.getParameter("weight_mut_power"));
		assertEquals("weight_mut_power should be 2.5 by default", new Double(2.5), parameters.getParameter("weight_mut_power").getDoubleValue());
	}
	
	@Test
	public void testLoadCustomParametersProperties() {
		
		IEvolutionContext context = new EvolutionContext();
		
		IEvolutionController controller = new EvolutionController(context);
		
		String location = new File("").getAbsolutePath() + "/src/test/resources/custom-parameters.properties";
		
		controller.loadEvolutionParameters(location);
		
		IEvolutionParameters parameters = context.getEvolutionParameters();
		
		// Check the default parameters are loaded into the context.
		assertNotNull(parameters);
		
		// Make sure the default value is correct.
		assertNotNull(parameters.getParameter("weight_mut_power"));
		assertEquals("weight_mut_power should be 2.5 by default", new Double(2.5), parameters.getParameter("weight_mut_power").getDoubleValue());

		// Test the custom 'Double' parameter has overwritten the default.
		assertNotNull(parameters.getParameter("recur_prob"));
		assertEquals("recur_prob should be 0.35 after being overwritten", new Double(0.35), parameters.getParameter("recur_prob").getDoubleValue());

		// Test the custom 'Integer' parameter has overwritten the default.
		assertNotNull(parameters.getParameter("dropoff_age"));
		assertEquals("dropoff_age should be 110 after being overwritten", new Integer(110), parameters.getParameter("dropoff_age").getIntValue());

	}
}
