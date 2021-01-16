package com.andysierra.aldebaran.actividades;

import java.lang.ref.WeakReference;

public class Misc
{
    private static final String TAG="Misc";
    private static Misc instancia = null;
    public MainActivity mainActivity;

    private Misc(WeakReference<MainActivity> mainActivityWeakReference) {
        this.mainActivity = mainActivityWeakReference.get();
    }

    private Misc() {}

    public static Misc getInstance(WeakReference<MainActivity> mainActivityWeakReference) {
        if(instancia==null)
            instancia = new Misc(mainActivityWeakReference);
        return instancia;
    }

    public static Misc getInstance() {
        if(instancia==null)
            instancia = new Misc();
        return instancia;
    }

    public void limpiar() {
        this.mainActivity = null;
        Misc.instancia = null;
    }
}
