package com.andysierra.aldebaran.vistas;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Handler;
import android.util.Log;

import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

public class Beeper implements Observer
{
    private static final String TAG="Beeper";
    private int intensidadActual, beepIntervalo;
    private double min, max, rango, tick;
    Handler beepHandler;
    Runnable beepRunable;
    ToneGenerator tono;

    public Beeper(int min, int max) {
        this.min = min;
        this.max = max;
        this.rango = max-min;
        this.tick = rango/10;
        this.tono  = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        this.intensidadActual = 0;
        this.beepIntervalo = 0;

        preparar();
    }

    private void preparar() {
        this.beepHandler = new Handler();
        this.beepRunable = new Runnable()
        {
            @Override
            public void run() {
                try {
                    beepIntervalo = 0;
                    boolean medible = false;
                    if(min<max && intensidadActual>=min && intensidadActual<=max) {
                        beepIntervalo = (int) Math.round(((intensidadActual-min)/tick));
                        medible = true;
                    }
                    else if(min>max && intensidadActual<=min && intensidadActual>=max) {
                        beepIntervalo = (int) Math.round(((intensidadActual-max)/tick));
                        medible = true;
                    }
                    Log.println(Log.ASSERT, TAG, "run: BEEPING: datos:"+intensidadActual+" , "+min+" , "+max+" :: "+beepIntervalo);

                    if(medible) tono.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                }
                finally {
                    beepHandler.postDelayed(beepRunable, 1100-beepIntervalo*100);
                }
            }
        };
        this.beepRunable.run();
    }

    @Override
    public void update(Observable o, Object arg) {
        Properties info = (Properties)arg;
        this.intensidadActual = Integer.parseInt(info.getProperty("rssi"));
    }

    public void limpiar() {
        this.beepHandler.removeCallbacks(this.beepRunable);
        this.beepRunable = null;
        this.beepHandler = null;
        this.tono = null;
    }
}
