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
       String []op = sis.obtenerNombresOptometras();
       assertEquals("Diana yineth González Martínez", op[0]);
       assertEquals("Sara alejandra mongui gonzalez", op[1]);
    }

}