package com.andysierra.aldebaran.modelos;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Property;

import com.andysierra.aldebaran.actividades.Misc;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

public class Medidor extends Observable
{
    public static final int AUTO_ON     = 1;
    public static final int AUTO_OFF    = -1;

    private Handler medidorHandler;
    private Runnable medidorRunnable;
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;
    private ArrayList<Observer> observadores;
    public int modoActual;
    public Properties info;

    public Medidor(final int intervalo) {
        this.medidorHandler = new Handler();
        this.wifiManager = (WifiManager)
                Misc.getInstance().mainActivity.getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
        this.observadores = new ArrayList<Observer>();
        this.modoActual = AUTO_OFF;
        this.medidorRunnable = new Runnable()
        {
            @Override
            public void run() {
                try {
                    wifiInfo = wifiManager.getConnectionInfo();
                    info = new Properties();
                    info.setProperty("rssi",        String.valueOf(wifiInfo.getRssi()));
                    info.setProperty("ssid",        wifiInfo.getSSID());
                    info.setProperty("mac",         wifiInfo.getMacAddress());
                    info.setProperty("frecuencia",  String.valueOf(wifiInfo.getFrequency()));
                    info.setProperty("hssid",       String.valueOf(wifiInfo.getHiddenSSID()));
                    info.setProperty("ip",          String.valueOf(wifiInfo.getIpAddress()));
                    info.setProperty("velocidad",   String.valueOf(wifiInfo.getLinkSpeed()));
                    info.setProperty("wifiState",   String.valueOf(wifiManager.getWifiState()));
                    info.setProperty("nivel",       String.valueOf(wifiManager.calculateSignalLevel(
                            wifiInfo.getRssi(), 5))
                    );
                    notifyObservers(info);
                }
                finally {
                    if(modoActual == AUTO_ON)
                        medidorHandler.postDelayed(medidorRunnable, intervalo);
                    else
                        medidorHandler.removeCallbacks(this);
                }
            }
        };
    }

    @Override
    public synchronized void addObserver(Observer o) {
        this.observadores.add(o);
    }

    @Override
    public void notifyObservers(Object arg) {
        for(Observer o : this.observadores)
            o.update(this,arg);
    }

    public boolean medir(int modo) {
        if(this.wifiInfo.getSupplicantState() != SupplicantState.COMPLETED)
            return false;
        this.modoActual = modo;
        this.medidorRunnable.run();
        return true;
    }

    public void limpiar() {
        this.medidorHandler.removeCallbacks(this.medidorRunnable);
        this.medidorHandler     = null;
        this.medidorRunnable    = null;
        this.observadores       = null;
        this.wifiInfo           = null;
        this.wifiManager        = null;
    }
}
