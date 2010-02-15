package org.brainfarm.java.util.writers;

import javax.xml.transform.Result;

import org.brainfarm.java.feat.api.IEvolution;

public interface IResultGenerator {

	Result getResult(IEvolution evolution);
}
