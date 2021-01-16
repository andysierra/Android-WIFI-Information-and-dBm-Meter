package com.andysierra.aldebaran.vistas;

import android.content.res.TypedArray;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.andysierra.aldebaran.R;
import com.andysierra.aldebaran.actividades.Misc;
import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.andysierra.aldebaran.modelos.Error;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

public class MedidorUI implements Observer
{
    public final static String TAG = "MedidorUI";
    private HalfGauge dbMedidor;
    private int[] cobertura;
    TypedArray colores;
    private Range[] rangos;
    public Button btnPlayPause, btnFormula;

    private TextView infoEstado;
    private TextView infoFrecuencia;
    private TextView infoVelocidad;
    private TextView infoSsid;
    private TextView infoIp;
    private TextView infoNivel;
    private TextView infoMac;

    public MedidorUI(int vista) throws ExceptionInInitializerError{
        this.dbMedidor
                = (HalfGauge) Misc.getInstance().mainActivity.findViewById(vista);
        this.colores
                = Misc.getInstance().mainActivity.getResources().obtainTypedArray(R.array.coloresMedidor);
        this.cobertura
                = Misc.getInstance().mainActivity.getResources().getIntArray(R.array.cobertura);
        this.rangos
                = new Range[cobertura.length/2];
        this.btnPlayPause
                = Misc.getInstance().mainActivity.findViewById(R.id.btnPlayPause);
        this.btnFormula
                = Misc.getInstance().mainActivity.findViewById(R.id.btnFormula);

        infoEstado      = (TextView) Misc.getInstance().mainActivity.findViewById(R.id.infoEstado    );
        infoFrecuencia  = (TextView) Misc.getInstance().mainActivity.findViewById(R.id.infoFrecuencia);
        infoVelocidad   = (TextView) Misc.getInstance().mainActivity.findViewById(R.id.infoVelocidad );
        infoSsid        = (TextView) Misc.getInstance().mainActivity.findViewById(R.id.infoSsid      );
        infoIp          = (TextView) Misc.getInstance().mainActivity.findViewById(R.id.infoIp        );
        infoNivel       = (TextView) Misc.getInstance().mainActivity.findViewById(R.id.infoNivel     );
        infoMac         = (TextView) Misc.getInstance().mainActivity.findViewById(R.id.infoMac       );

        if(cobertura.length%2 != 0 || colores.length() != cobertura.length/2)
            throw Error.OnConstsError(cobertura.length, colores.length());

        this.dbMedidor.setMinValue(cobertura[0]);
        this.dbMedidor.setMaxValue(cobertura[cobertura.length-1]);
        this.dbMedidor.setValue(cobertura[0]); // temp

        for(int i=0; i<cobertura.length/2; i++) {
            rangos[i] = new Range();
            rangos[i].setColor(colores.getColor(i,0));
            rangos[i].setFrom(cobertura[i*2]);
            rangos[i].setTo(cobertura[(i*2)+1]);
            this.dbMedidor.addRange(rangos[i]);
        }
        this.dbMedidor.setVisibility(View.VISIBLE);
    }



    @Override
    public void update(Observable o, Object arg) {
        Properties info = (Properties)arg;
        this.dbMedidor.setValue(Double.parseDouble(info.getProperty("rssi")));
        this.actualizarDatosUi(info);
        Log.println(Log.ASSERT, TAG, "update: "+"INFO QUE ME LLEGA"+info.toString());
    }



    // Método para actualizar la info de la UI.
    // La deprecación es solo para direcciones IPv6
    @SuppressWarnings("deprecation")
    private void actualizarDatosUi(Properties info) {
        this.infoEstado.setText(
                Misc.getInstance().mainActivity
                        .getResources().getStringArray(R.array.estadoWifi)[Integer.parseInt(
                                info.getProperty("wifiState")
                )]
        );
        this.infoFrecuencia.setText(
                String.valueOf(Float.parseFloat(info.getProperty("frecuencia"))/1000)
        );
        this.infoVelocidad.setText(info.getProperty("velocidad")+" Mbps");
        this.infoSsid.setText(info.getProperty("ssid"));
        this.infoIp.setText(Formatter.formatIpAddress(Integer.parseInt(info.getProperty("ip"))));
        this.infoNivel.setText(info.getProperty("nivel"));
        this.infoMac.setText(info.getProperty("mac"));
    }

    public void limpiar() {
        this.dbMedidor = null;
        this.rangos = null;
    }
}