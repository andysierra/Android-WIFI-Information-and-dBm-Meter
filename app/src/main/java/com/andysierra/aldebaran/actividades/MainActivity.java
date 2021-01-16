package com.andysierra.aldebaran.actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.andysierra.aldebaran.R;
import com.andysierra.aldebaran.controladores.Control;
import java.lang.ref.WeakReference;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG="MainActivity";
    private Control app;
    private String permisoUbicacion;
    private int usuarioPermite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Misc.getInstance(new WeakReference<MainActivity>(this)); // Singleton
        this.pedirPermiso(); // Pedir permiso al usuario para obtener ubicación
    }


    // Pedir permiso para obtener la información del WIFI (Ver los permisos en el manifiesto)
    private void pedirPermiso() {

         this.permisoUbicacion= Manifest.permission.ACCESS_FINE_LOCATION;
         this.usuarioPermite = PackageManager.PERMISSION_GRANTED;

        if(ActivityCompat.checkSelfPermission(this, permisoUbicacion) != usuarioPermite)
            ActivityCompat.requestPermissions(this, new String[]{permisoUbicacion}, 0);
        else onRequestPermissionsResult(0, new String[]{permisoUbicacion}, new int[]{usuarioPermite});
    }


    // Escucha cuando le doy permisos a la app
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        Log.println(Log.ASSERT, TAG, "onRequestPermissionsResult: ["+
                Arrays.toString(permissions)+"] ["+
                Arrays.toString(grantResults)+"]\n");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(permissions[0] != this.permisoUbicacion && grantResults[0] != this.usuarioPermite) {

            Toast.makeText(this,
                    this.getResources().getString(R.string.permiso_wifi_error),
                    Toast.LENGTH_LONG).show();
        }
        this.app = new Control();
        this.app.iniciar();
    }


    // Limpiar toda la app y salir
    public void limpiar() {

        if(this.app != null) this.app.limpiar();
        Misc.getInstance().limpiar();
        this.finish();
        System.exit(0);
    }
}