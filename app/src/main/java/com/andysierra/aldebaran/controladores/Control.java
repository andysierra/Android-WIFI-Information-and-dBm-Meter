package com.andysierra.aldebaran.controladores;

import android.view.View;

import com.andysierra.aldebaran.modelos.Medidor;
import com.andysierra.aldebaran.vistas.FormulaUI;
import com.andysierra.aldebaran.vistas.MedidorUI;
import com.andysierra.aldebaran.R;

public class Control
{
    private static final String TAG="Control";
    private MedidorUI medidorUI;
    private FormulaUI dialogoUI;
    private Medidor medidor;

    public Control() {
        this.medidorUI= new MedidorUI(R.id.dbMedidor);
        this.dialogoUI= new FormulaUI();
        this.medidor = new Medidor(500);
        this.medidor.addObserver(this.medidorUI);
        this.medidor.addObserver(this.dialogoUI);

        prepararEventos();
    }

    private void prepararEventos() {

        // Botón play/pause
        this.medidorUI.btnPlayPause.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                medidor.medir(medidor.modoActual*(-1));
                medidorUI.btnPlayPause.setText(
                        medidorUI.btnPlayPause.getText() == "PLAY" ?
                                "PAUSE" : "PLAY"
                );
            }
        });

        // Botón para mostrar la formula
        this.medidorUI.btnFormula.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                dialogoUI.dialogo.show();
            }
        });

        // Botón para cerrar el dialogo
        this.dialogoUI.btnCerrarFormulaUI.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                dialogoUI.dialogo.hide();
            }
        });
    }

    public void iniciar() {
        this.medidor.medir(Medidor.AUTO_ON);
    }

    public void limpiar() {
        this.medidor.medir(Medidor.AUTO_OFF);
        this.medidor.limpiar();
        this.medidor = null;
        this.medidorUI.limpiar();
        this.medidorUI = null;
    }
}
