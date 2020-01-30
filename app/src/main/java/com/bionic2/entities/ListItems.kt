package com.bionic2.entities

class ListItems {

    var name: String
    var day: String
    var dayNumber: String
    var month: String
    var year: String
    var hour: String? = null
    var minute: String? = null

    constructor(name: String, day: String, dayNumber: String, month: String, year: String, hour: String?, minute: String?) {

        this.name = name
        this.day = day
        this.dayNumber = dayNumber
        this.month = month
        this.hour = hour
        this.minute = minute
        this.year = year
    }


}