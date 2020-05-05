package co.com.k4soft.parqueaderouco.view.reportemovimiento;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.com.k4soft.parqueaderouco.R;
import co.com.k4soft.parqueaderouco.adapter.ReporteMovimientoAdapter;
import co.com.k4soft.parqueaderouco.entidades.Movimiento;
import co.com.k4soft.parqueaderouco.persistencia.room.DataBaseHelper;
import co.com.k4soft.parqueaderouco.utilities.DateUtil;

public class ReporteMovimientoActivity extends AppCompatActivity {

    @BindView(R.id.btnFechaInicio)
    public Button btnFechaInicio;
    @BindView(R.id.btnFechaFinal)
    public Button btnFechaFinal;
    @BindView(R.id.btnGenerarReporte)
    public Button btnGenerarReporte;
    @BindView(R.id.txtFechaInicio)
    public TextView txtFechaInicio;
    @BindView(R.id.txtFechaFinal)
    public TextView txtFechaFinal;
    @BindView(R.id.listaViewMovimiento)
    public ListView listaViewMovimiento;

    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private DataBaseHelper db;
    private ReporteMovimientoAdapter reporteMovimientoAdapter;
    private List<Movimiento> listaReporteMovimientos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_movimiento_activity);
        ButterKnife.bind(this);
        db = DataBaseHelper.getDBMainThread(this);
    }

    public void elegirFechaInicio(View view) {
        calendar = Calendar.getInstance();
        int diaInicio = calendar.get(Calendar.DAY_OF_MONTH);
        int mesInicio = calendar.get(Calendar.MONTH);
        int anioInicio= calendar.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String fecha = year + "-" + (month + 1) + "-" + dayOfMonth;
                Date fechaMod = DateUtil.convertStringToDateNotHour(fecha);
                txtFechaInicio.setText(DateUtil.convertDateToString(fechaMod));
            }
        }, anioInicio, mesInicio, diaInicio);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    public void elegirFechaFinal(View view) {
        calendar = Calendar.getInstance();
        int diaFinal = calendar.get(Calendar.DAY_OF_MONTH);
        int mesFinal = calendar.get(Calendar.MONTH);
        int anioFinal = calendar.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String fecha = year + "-" + (month + 1) + "-" + dayOfMonth;
                Date fechaMod = DateUtil.convertStringToDateNotHour(fecha);
                txtFechaFinal.setText(DateUtil.convertDateToString(fechaMod));
            }
        }, anioFinal , mesFinal, diaFinal);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }


    public void generarReporte(View view) {
        listaReporteMovimientos = null;
        listaReporteMovimientos = db.getMovimientoDAO().findByFecha(txtFechaInicio.getText().toString(), txtFechaFinal.getText().toString());
        if (listaReporteMovimientos.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.no_hay_registros, Toast.LENGTH_LONG).show();
        } else {
            reporteMovimientoAdapter = new ReporteMovimientoAdapter(this,listaReporteMovimientos);
            listaViewMovimiento.setAdapter(reporteMovimientoAdapter);
        }
    }
}
