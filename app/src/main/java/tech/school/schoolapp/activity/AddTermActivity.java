package tech.school.schoolapp.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import tech.school.schoolapp.DAO.Repository;
import tech.school.schoolapp.R;
import tech.school.schoolapp.model.Term;

public class AddTermActivity extends AppCompatActivity {
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
   private Calendar calendar = Calendar.getInstance();
   private DatePickerDialog.OnDateSetListener listenStartDate;
    private DatePickerDialog.OnDateSetListener listenEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_term_activity);
        backButtonHandler();
        pickStartDate();
        pickEndDate();
        fillFormValues();
    }

    protected void backButtonHandler(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAlt);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

    }

    public void pickStartDate(){
        Button startDatePicker =findViewById(R.id.termStartDateInput);
        startDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;

                String info = startDatePicker.getText().toString();
                if(info.equals(""))info=startDatePicker.getText().toString();

                try{
                    calendar.setTime(dateFormat.parse(info));
                }catch (ParseException p){
                    p.printStackTrace();
                }

                new DatePickerDialog(AddTermActivity.this, listenStartDate,
                        calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        listenStartDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);


                startDatePicker.setText(dateFormat.format(calendar.getTime()));
            }

        };
    }

    public void pickEndDate(){
        Button endPicker =findViewById(R.id.termEndDateInput);
        endPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;

                String info = endPicker.getText().toString();
                if(info.equals(""))info=endPicker.getText().toString();

                try{
                    calendar.setTime(dateFormat.parse(info));
                }catch (ParseException p){
                    p.printStackTrace();
                }

                new DatePickerDialog(AddTermActivity.this,listenEndDate,
                        calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        listenEndDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);


                endPicker.setText(dateFormat.format(calendar.getTime()));
            }

        };
    }

    public void saveTermClick(View view){
        if(checkNullValues())return;

        Repository repository = new Repository(getApplication());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        EditText termTitleBox = (EditText) findViewById(R.id.termTitleInput);
        Button startDateBox = (Button) findViewById(R.id.termStartDateInput);
        Button endDateBox = (Button) findViewById(R.id.termEndDateInput);

        String titleString = termTitleBox.getText().toString();
        String startString = startDateBox.getText().toString();
        String endString = endDateBox.getText().toString();
        Date startDate = null;
        Date endDate = null;

        Term newTerm = new Term();
        newTerm.setTermTitle(titleString);
        try {
            startDate = dateFormat.parse(startString);
            endDate = dateFormat.parse(endString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        newTerm.setStartDate(startDate);
        newTerm.setEndDate(endDate);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            Term modifyTerm = new Term();
            int termId = bundle.getInt("id");
            String title = bundle.getString("title");
            String startDateString = startDateBox.getText().toString();
            String endDateString = endDateBox.getText().toString();

            modifyTerm.setId(termId);
            modifyTerm.setTermTitle(title);

            try{
                Date startD = dateFormat.parse(startDateString);
                modifyTerm.setStartDate(startD);
                Date endD = dateFormat.parse(endDateString);
                modifyTerm.setEndDate(endD);
            }catch (ParseException p){
                p.printStackTrace();
            }
            repository.mUpdateTerm(modifyTerm);
            Intent i = new Intent(this,TermActivity.class);
            startActivity(i);
            return;
        }

        repository.mInsertTerm(newTerm);

        Intent i = new Intent(this, TermActivity.class);
        startActivity(i);
    }

    private boolean checkNullValues(){
        EditText termTitleBox = (EditText) findViewById(R.id.termTitleInput);
        Button startDateBox = (Button) findViewById(R.id.termStartDateInput);
        Button endDateBox = (Button) findViewById(R.id.termEndDateInput);

        if(termTitleBox == null || startDateBox == null || endDateBox == null){
            Context context = getApplicationContext();
            CharSequence text ="Fill out all the fields";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context,text,duration);
            toast.show();
            return true;
        }
        return false;
    }

    private void fillFormValues(){
        EditText termTitleBox = (EditText) findViewById(R.id.termTitleInput);
        Button startDateBox = (Button) findViewById(R.id.termStartDateInput);
        Button endDateBox = (Button) findViewById(R.id.termEndDateInput);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null){
            int id = bundle.getInt("id");
            String titleText = bundle.getString("title");
            String startDateText = bundle.getString("startDate");
            String endDateText = bundle.getString("endDate");

            termTitleBox.setText(titleText);
            startDateBox.setText(startDateText);
            endDateBox.setText(endDateText);

        }
    }
}
