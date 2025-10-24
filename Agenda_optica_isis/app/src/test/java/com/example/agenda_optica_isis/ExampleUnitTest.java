package com.example.agenda_optica_isis;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.agenda_optica_isis.model.Paciente;
import com.example.agenda_optica_isis.model.SistemaReservas;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void testActualizar(){
        SistemaReservas sis = new SistemaReservas();
        sis.crearPaciente("ana.martinez@email.com", "Ana Isabel Martínez", "456789123", "3203456789", 1978, 11, 5, "C.C.", "femenino");
        boolean seactualizo = sis.actualizarPaciente("ana.martinez@email.com", "Ana Isabelartínez", "456789123", "3203456789", 1978, 11, 5, "C.C.", "femenino");
        assertEquals(seactualizo, true);
    }

}