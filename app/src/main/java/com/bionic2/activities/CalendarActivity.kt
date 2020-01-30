package com.bionic2.activities

//import com.bionic2.Services.ConnexionService

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bionic2.entities.DateTime
import com.bionic2.entities.ListItems
import com.bionic2.NextDates
import com.bionic2.R
import com.bionic2.View.RobotoCalendarView
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class CalendarActivity : AppCompatActivity(), RobotoCalendarView.RobotoCalendarListener ,  View.OnClickListener{


    companion object{

        lateinit var robotoCalendarView: RobotoCalendarView
        lateinit var txtDisplayWorkout: TextView
        val current_day = SimpleDateFormat("EEEE dd MM yyyy")
        val dayTime = SimpleDateFormat("dd MMMM yyyy")
        val shortDateNumber = SimpleDateFormat("yyyyMMdd")
        val dateNumber = SimpleDateFormat("yyyyMMddHHmm")
        val monthString = SimpleDateFormat("MMMM")
        val monthNumber = SimpleDateFormat("MM")
        var todayNumber = 0L
        var listWorkouts = ArrayList<ListItems>()
    }

    var dateChoose = ""
    var today =  listOf<String>()
    var todayDateTime = DateTime()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar)

        //Date du jour

        val calendar = Calendar.getInstance()
        val date = calendar.time

        //Récupération des éléments de la vue calandar.xml

        robotoCalendarView = findViewById(R.id.robotoCalendarPicker)
        txtDisplayWorkout = findViewById(R.id.txt_display_workout) as TextView
        val txtDisplayNextWorkout = findViewById(R.id.txt_display_next_workout) as TextView
        val butShowNextWorkout = findViewById<Button>(R.id.but_show_next_workouts)

        //listener sur les bouttons du layout

        butShowNextWorkout.setOnClickListener(this) // Affiche les prochains évenements dans une PopUp

        // ( Pour l'exemple je rentre les données en dur. l'appli d'origine utilse Firebase )

        //anciens évenements
        listWorkouts.add(ListItems("bras", "lundi", "20", "janvier", "2020", "9", "30"))
        listWorkouts.add(ListItems("jambes", "jeudi", "23", "janvier", "2020", "15", "10"))
        listWorkouts.add(ListItems("dos", "mercredi", "29", "janvier", "2020", "17", "15"))
        //Prochains évenement
        listWorkouts.add(ListItems("bras", "jeudi", "13", "février", "2020", "10", "20"))
        listWorkouts.add(ListItems("jambes", "vendredi", "14", "février", "2020", "14", "30"))
        listWorkouts.add(ListItems("dos", "samedi", "15", "février", "2020", "17", "15"))


        todayNumber = shortDateNumber.format(date).toLong()
        today = current_day.format(date).split(" ")
        todayDateTime = DateTime(today[0], today[1], today[2], today[3])

        txtDisplayNextWorkout.text = displayNextWorkout(todayNumber)  // affiche la date pour la prochaine séance

        //def des paramètres de robotoClendarView

        robotoCalendarView.setRobotoCalendarListener(this)

        robotoCalendarView.setShortWeekDays(true)

        robotoCalendarView.showDateTitle(true)

        getWorkoutsPlans(robotoCalendarView, todayNumber)  //Affichage des markers : old events (points gris) next events (points rouge)

    }

    override
    fun onClick(v: View)
    {
        when(v.id){

            R.id.but_show_next_workouts ->{

                val dialogFragment = NextDates(listWorkouts)
                val bundle = Bundle()
                bundle.putBoolean("notAlertDialog", true)
                dialogFragment.arguments = bundle
                val ft = supportFragmentManager.beginTransaction()
                val prev = supportFragmentManager.findFragmentByTag("dialog")
                if (prev != null)
                {
                    ft.remove(prev)
                }
                ft.addToBackStack(null)
                dialogFragment.show(ft, "dialog")
            }


        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed();
        return true;
    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onDayClick(date: Date) {

        dateChoose = current_day.format(date)
        val dateChooseNumber = shortDateNumber.format(date).toLong()

        displayWorkoutFromDayCick(txtDisplayWorkout,dateChooseNumber, this)

    }


    fun getWorkoutsPlans(calendarView: RobotoCalendarView, today: Long){

        val thisMonth = monthString.format(calendarView.date)
        var wDateString: String
        var workoutDate: Date
        var workoutDateNumbers: Long

        for(workout in listWorkouts){

            if(thisMonth == workout.month) {

                wDateString = workout.dayNumber+" "+workout.month+" "+workout.year
                workoutDate = parseStringToDate(wDateString)
                workoutDateNumbers = parseDateToLong(wDateString)

                if(workoutDateNumbers >= today)
                calendarView.markCircleImage1(workoutDate)
                else
                calendarView.markCircleImage2(workoutDate)

            }
        }
    }

    fun displayWorkoutFromDayCick(textView: TextView,dateClicked: Long, context: Context){

        var workoutDate: String
        var workoutDateNumber: Long

            for (plan in listWorkouts) {

            workoutDate = plan.dayNumber + " " + plan.month + " " + plan.year
            workoutDateNumber = parseDateToLong(workoutDate)

            if(workoutDateNumber == dateClicked){

                textView.text = "Séance programmée : "+ plan.name+" à "+plan.hour+":"+plan.minute
                break
            }
            else
                textView.text= getString(R.string.no_wkt)

        }

    }

    fun displayNextWorkout (todayDate: Long): String{

        var prevDate = 0L
        var textToDisplay = ""
        var workoutDate: String
        var workoutDateNumber: Long

        for (plan in listWorkouts!!) {

            workoutDate = plan.dayNumber + " " + plan.month + " " + plan.year

            // On récupère les dates en format "chiffres" pour simplifier notre algo !

           workoutDateNumber = parseDateToLong(workoutDate)


            if (todayDate == workoutDateNumber) {

                textToDisplay = "Aujourd'hui : "+ plan.name +" à "+ plan.hour+":"+plan.minute


            } else if (workoutDateNumber > todayDate) {

                if (prevDate == 0L) {

                    prevDate = workoutDateNumber
                    textToDisplay = "Prochaine séance : "+ plan.name +"\n"+ workoutDate

                }
                if (workoutDateNumber != prevDate) {

                    if (workoutDateNumber < prevDate) {

                        textToDisplay = "Prochaine séance : "+ plan.name +"\n"+ workoutDate
                    }

                    prevDate = workoutDateNumber
                }

            }
            else if (prevDate == 0L){

                textToDisplay = getString(R.string.no_wkt_plan)

            }


        }

        return textToDisplay
    }

    fun parseDateToLong(date: String): Long{

        var DateParse: Date = dayTime.parse(date)
        var DateNumber = shortDateNumber.format(DateParse).toLong()

        return DateNumber
    }

    fun parseStringToDate(date: String): Date{

        var dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRANCE)
        var localDate = LocalDate.parse(date, dateFormatter)
        var mdate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())

        return mdate
    }

    override fun onDayLongClick(date: Date) {

    }

    override fun onRightButtonClick() {

        getWorkoutsPlans(robotoCalendarView, todayNumber)

    }

    override fun onLeftButtonClick() {

        getWorkoutsPlans(robotoCalendarView, todayNumber)
    }

}