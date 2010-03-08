package org.brainfarm.java.feat.operators;

import java.util.Iterator;

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

	public IEvolutionParameters evolutionParameters;

	public DefaultSpeciationStrategy(IEvolutionParameters evolutionParameters) {
		this.evolutionParameters = evolutionParameters;
	}

	@Override
	public void speciate(IPopulation pop) {
		//clear the population species
		pop.getSpecies().clear();

		// for each organism.....
		for (IOrganism organism : pop.getOrganisms())
			addOrganismToSpecies(pop, organism);
	}

	public void addOrganismToSpecies(IPopulation pop, IOrganism organism) {

		ISpecies newspecies;
		// if list species is empty , create the first species!
		if (pop.getSpecies().isEmpty()) {
			newspecies = FeatFactory.newSpecies(pop); // create a new species
			newspecies.setEvolutionParameters(evolutionParameters);
			pop.getSpecies().add(newspecies); // add this species to list of species
			newspecies.addOrganism(organism);
			organism.setSpecies(newspecies); // Point organism to its species
		} 
		else {
			//find an existing compatible species
			Iterator<ISpecies> itr_specie = pop.getSpecies().iterator();
			boolean done = false;
			while (!done && itr_specie.hasNext()) {
				ISpecies _specie = itr_specie.next();
				// point to first organism of this species (TODO: should be random?)
				IOrganism compare_org = _specie.getOrganisms().get(0);
				// compare organism with the species representative
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
			// if no compatible species found, create one
			if (!done) {
				newspecies = FeatFactory.newSpecies(pop); // create a new species
				newspecies.setEvolutionParameters(evolutionParameters);
				pop.getSpecies().add(newspecies); // add this species to list of species
				newspecies.addOrganism(organism);
				// Add to new species the current organism
				organism.setSpecies(newspecies); // Point organism to its  species
			}
		}
	}
}