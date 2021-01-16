package com.andysierra.aldebaran.modelos;

import com.andysierra.aldebaran.R;
import com.andysierra.aldebaran.actividades.Misc;

public class Error
{
    // ERROR CUANDO EL ARCHIVO CONSTS.xml NO TIENE LOS ARRAYS INDICADOS
    public static ExceptionInInitializerError OnConstsError(int coberturaLength,
                                                            int coloresMedidorLenght) {
        String mensaje =Misc.getInstance().mainActivity.getResources().getString(R.string.consts_error);
        mensaje += "[longitud de array cobertura: "+coberturaLength+
                ", longitud de array coloresMedidor"+coloresMedidorLenght+"]\n";
        ExceptionInInitializerError error = new ExceptionInInitializerError(mensaje);

        return error;
    }
}
