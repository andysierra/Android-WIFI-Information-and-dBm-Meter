package com.andysierra.aldebaran.vistas;

import android.app.Dialog;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.andysierra.aldebaran.R;
import com.andysierra.aldebaran.actividades.Misc;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

public class FormulaUI implements Observer
{
    public Dialog dialogo;
    private TextView txfFormula;
    public Button btnCerrarFormulaUI;

    public FormulaUI() {
        this.dialogo = new Dialog(Misc.getInstance().mainActivity);
        this.dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.dialogo.setContentView(R.layout.dialog_formula_ui);
        this.dialogo.setTitle("Formula");

        this.txfFormula = (TextView) this.dialogo.findViewById(R.id.txfFormula);
        this.btnCerrarFormulaUI = (Button) this.dialogo.findViewById(R.id.btnCerrarFormulaUI);
    }

    @Override
    public void update(Observable o, Object arg) {
        Properties info = (Properties)arg;
        double potenciaMiliWatts =Math.pow(10,(Double.parseDouble(info.getProperty("rssi"))*(-1)/10));
        this.txfFormula.setText("P(mW) = 1mW * 10 ^("+Double.parseDouble(info.getProperty("rssi"))*(-1)+" dBm / 10)\nP(mW) = "+
                String.format("%.4f",potenciaMiliWatts)+" mW");
    }
}
