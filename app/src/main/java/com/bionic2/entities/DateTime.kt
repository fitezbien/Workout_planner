package com.bionic2.entities


class DateTime {


    var day: String? = null
    var dayNumber: String? = null
    var month: String? = null
    var year: String? = null

    constructor(){}

    constructor(day: String, dayNumber: String, month: String, year: String){

        this.day = day
        this.month = month
        this.dayNumber = dayNumber
        this.year = year
    }

}