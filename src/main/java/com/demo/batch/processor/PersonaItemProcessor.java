package com.demo.batch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.demo.batch.models.Persona;

public class PersonaItemProcessor implements ItemProcessor<Persona, Persona> {
	private static final Logger LOG = LoggerFactory.getLogger(PersonaItemProcessor.class);

	@Override
	public Persona process(Persona item) throws Exception {
		String primerNombre = item.getPrimerNombre().toUpperCase();
		String segundoNombre = item.getSegundoNombre().toUpperCase();
		String telefono = item.getTelefono().toUpperCase();

		Persona p = new Persona(primerNombre, segundoNombre, telefono);

		LOG.info("Procesando persona: " + p);

		return p;
	}

}
