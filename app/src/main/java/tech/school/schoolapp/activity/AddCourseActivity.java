package tech.school.schoolapp.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import tech.school.schoolapp.DAO.Repository;
import tech.school.schoolapp.R;
import tech.school.schoolapp.model.Course;
import tech.school.schoolapp.model.CourseStatus;
import tech.school.schoolapp.model.Instructor;
import tech.school.schoolapp.model.Term;

public class AddCourseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Repository repository = new Repository(getApplication());
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener listenStartDate;
    private DatePickerDialog.OnDateSetListener listenEndDate;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_course_activity);
        backButtonHandler();
        pickStartDate();
        pickEndDate();
        statusSpinner();
        instructorSpinner();
        termSpinner();
        fillFormValues();
    }
    protected void onResume() {
        super.onResume();

    }


    protected void backButtonHandler(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

    }

    public void pickStartDate(){
        Button startPicker =findViewById(R.id.startDateButton);
        startPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;

                String info = startPicker.getText().toString();
                if(info.equals(""))info=startPicker.getText().toString();

                try{
                    calendar.setTime(dateFormat.parse(info));
                }catch (ParseException p){
                    p.printStackTrace();
                }

                new DatePickerDialog(AddCourseActivity.this, listenStartDate,
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


                startPicker.setText(dateFormat.format(calendar.getTime()));
            }

        };
    }

    public void pickEndDate(){
        Button endPicker =findViewById(R.id.endDateButton);
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

                new DatePickerDialog(AddCourseActivity.this, listenEndDate,
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

    public void statusSpinner(){
        Spinner statusSpinner = (Spinner) findViewById(R.id.statusSpinner);
        ArrayList<CourseStatus> statusList = new ArrayList<>(Arrays.asList(CourseStatus.values()));
        ArrayAdapter<CourseStatus> adapter = new ArrayAdapter<>( this,android.R.layout.simple_spinner_item,statusList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

    }
    public void instructorSpinner(){
        Spinner instructorSpinner = (Spinner) findViewById(R.id.instructorSpinner);
        ArrayList<Instructor> instructorList = new ArrayList<>(repository.mGetAllInstructors());
        ArrayAdapter<Instructor> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,instructorList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        instructorSpinner.setAdapter(adapter);
    }

    public void termSpinner(){
        Spinner termSpinner = (Spinner) findViewById(R.id.termSpinner);
        ArrayList<Term> termList = new ArrayList<>(repository.mGetAllTerms());
        ArrayAdapter<Term> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,termList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        termSpinner.setAdapter(adapter);
    }

    private boolean checkNullValues(){
        EditText courseTitle = (EditText) findViewById(R.id.courseTitleEdit);
        Button startPicker =findViewById(R.id.startDateButton);
        Button endPicker = findViewById(R.id.endDateButton);
        Spinner courseStatus = (Spinner) findViewById(R.id.statusSpinner);
        Spinner termSpinner = (Spinner) findViewById(R.id.termSpinner);
        Spinner instructor = (Spinner) findViewById(R.id.instructorSpinner);

        String noSelection = "Date Picker";

        if(courseTitle == null || startPicker.getText().toString().equals(noSelection) || endPicker.getText().toString().equals(noSelection)  || courseStatus == null || termSpinner.getSelectedItem() == null || instructor.getSelectedItem() == null){
            Context context = getApplicationContext();
            CharSequence text ="Fill out all the fields";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context,text,duration);
            toast.show();
            return true;
        }
        return false;
    }


    public void saveButtonHandler(View view) {
        if(checkNullValues())return;

        Course course = new Course();
        EditText courseTitle = (EditText) findViewById(R.id.courseTitleEdit);
        Button startPicker =findViewById(R.id.startDateButton);
        Button endPicker = findViewById(R.id.endDateButton);
        Spinner courseStatus = (Spinner) findViewById(R.id.statusSpinner);
        Spinner termSpinner = (Spinner) findViewById(R.id.termSpinner);
        Spinner instructor = (Spinner) findViewById(R.id.instructorSpinner);

        String startDateString = startPicker.getText().toString();
        String endDateString = endPicker.getText().toString();
        Date endDate = null;
        Date startDate = null;
        try {
            startDate = dateFormat.parse(startDateString);
            endDate = dateFormat.parse(endDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        course.setStartDate(startDate);
        course.setEndDate(endDate);
        course.setTitle(String.valueOf(courseTitle.getText()));
        course.setStatus(courseStatus.getSelectedItem().toString());

        Instructor courseInstructor = (Instructor) instructor.getSelectedItem();
        course.setInstructorId(courseInstructor.getId());

        Term courseTerm = (Term) termSpinner.getSelectedItem();
        course.setTermId(courseTerm.getId());



        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            Course modifyCourse = new Course();
            int courseId = bundle.getInt("id");
            String title = bundle.getString("title");
            String startDString = startPicker.getText().toString();
            String endDString = endPicker.getText().toString();

            try{
                Date cStart = dateFormat.parse(startDString);
                modifyCourse.setStartDate(cStart);
                Date cEnd = dateFormat.parse(endDString);
                modifyCourse.setEndDate(cEnd);
            }catch (ParseException p){
                p.printStackTrace();
            }

            String modCourseStatus = courseStatus.getSelectedItem().toString();
            int termId = ((Term) termSpinner.getSelectedItem()).getId();
            int instructorId = ((Instructor) instructor.getSelectedItem()).getId();
            modifyCourse.setId(courseId);
            modifyCourse.setTitle(title);
            modifyCourse.setStatus(modCourseStatus);
            modifyCourse.setTermId(termId);
            modifyCourse.setInstructorId(instructorId);

            repository.mUpdateCourse(modifyCourse);
            Intent i = new Intent(this,CourseActivity.class);
            startActivity(i);
            return;
        }

        repository.insertCourse(course);

        Intent i = new Intent(this,CourseActivity.class);
        startActivity(i);
    }

    private void fillFormValues(){
        EditText courseTitle = (EditText) findViewById(R.id.courseTitleEdit);
        Button startPicker =findViewById(R.id.startDateButton);
        Button endPicker = findViewById(R.id.endDateButton);
        Spinner courseStatus = (Spinner) findViewById(R.id.statusSpinner);
        Spinner termSpinner = (Spinner) findViewById(R.id.termSpinner);
        Spinner instructorSpinner = (Spinner) findViewById(R.id.instructorSpinner);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null){
            int id = bundle.getInt("id");
            String titleText = bundle.getString("title");
            String startDateText = bundle.getString("startDate");
            String endDateText = bundle.getString("endDate");
            String courseStatusText = bundle.getString("status");
            int termId = bundle.getInt("termId");
            int instructorId = bundle.getInt("instructorId");


            ArrayList<CourseStatus> statusList = new ArrayList<>(Arrays.asList(CourseStatus.values()));
            ArrayAdapter<CourseStatus> courseAdapter = new ArrayAdapter<>( this,android.R.layout.simple_spinner_item,statusList);
            courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            courseStatus.setAdapter(courseAdapter);
            courseStatus.setSelection(courseAdapter.getPosition(CourseStatus.valueOf(courseStatusText)));

            ArrayList<Term> termList = new ArrayList<>(repository.mGetAllTerms());

            ArrayAdapter<Term> termAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,termList);
            termAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            termSpinner.setAdapter(termAdapter);
            for(int i=0;i<termList.size();i++){
                Term t = termList.get(i);
                if(t.getId() == termId) termSpinner.setSelection(i);
            }

            ArrayList<Instructor> instructorList = new ArrayList<>(repository.mGetAllInstructors());
            ArrayAdapter<Instructor> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,instructorList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            instructorSpinner.setAdapter(adapter);


            for (int i = 0; i < instructorList.size();i++){
                Instructor iN = instructorList.get(i);

                if(iN.getId() == instructorId)instructorSpinner.setSelection(i);

            }




            courseTitle.setText(titleText);
            startPicker.setText(startDateText);
            endPicker.setText(endDateText);

        }

    }

    public void cancelButton(View view){
        Intent intent = new Intent(this,CourseActivity.class);
        startActivity(intent);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
