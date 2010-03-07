package org.brainfarm.java.feat.operators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.brainfarm.java.feat.api.IOrganism;
import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.ISpecies;
import org.brainfarm.java.feat.api.operators.ISpeciationStrategy;
import org.brainfarm.java.feat.api.params.IEvolutionParameters;

/**
 * This is the speciation strategy employed by the original
 * JNeat by Ugo Vierucci.
 * 
 * @author dtuohy, orig. Ugo Vierucci
 *
 */
public class DefaultSpeciationStrategy implements ISpeciationStrategy {

	protected IEvolutionParameters evolutionParameters;
	
	public DefaultSpeciationStrategy(IEvolutionParameters evolutionParameters) {
		this.evolutionParameters = evolutionParameters;
	}
	
	@Override
	public void speciate(IPopulation pop) {
		
		//clear the population species
		pop.getSpecies().clear();
		
		IOrganism compare_org = null; // Organism for comparison
		ISpecies newspecies = null;

		int counter = 0; // Species counter

		// for each organism.....
		for (IOrganism organism : pop.getOrganisms()) {

			// if list species is empty , create the first species!
			if (pop.getSpecies().isEmpty()) {
				newspecies = FeatFactory.newSpecies(++counter); // create a new specie
				newspecies.setEvolutionParameters(evolutionParameters);
				
				pop.getSpecies().add(newspecies); // add this species to list of species
				newspecies.addOrganism(organism);
				// Add to new spoecies the current organism
				organism.setSpecies(newspecies); // Point organism to its species
			} else {
				// looop in all species.... (each species is a Vector of
				// organism...)
				Iterator<ISpecies> itr_specie = pop.getSpecies().iterator();
				boolean done = false;

				while (!done && itr_specie.hasNext()) {

					// point _species-esima
					ISpecies _specie = itr_specie.next();
					// point to first organism of this _specie-esima
					compare_org = _specie.getOrganisms().get(0);
					// compare _organism-esimo('_organism') with first organism
					// in current specie('compare_org')
					double curr_compat = organism.getGenome().compatibility(compare_org.getGenome());

					if (curr_compat < evolutionParameters.getDoubleParameter(COMPAT_THRESHOLD)) {
						// Found compatible species, so add this organism to it
						_specie.addOrganism(organism);
						// update in organism pointer to its species
						organism.setSpecies(_specie);
						// force exit from this block ...
						done = true;
					}
				}

				// if no found species compatible , create specie
				if (!done) {
					newspecies = FeatFactory.newSpecies(++counter); // create a new specie
					newspecies.setEvolutionParameters(evolutionParameters);
					
					pop.getSpecies().add(newspecies); // add this species to list of
												// species
					newspecies.addOrganism(organism);
					// Add to new species the current organism
					organism.setSpecies(newspecies); // Point organism to its
														// species

				}

			}

		}

		pop.setLastSpecies(counter); // Keep track of highest species
	}

}
