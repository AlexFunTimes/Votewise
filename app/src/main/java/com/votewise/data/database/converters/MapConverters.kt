package com.votewise.data.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MapConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromString(value: String?): Map<String, Double>? {
        if (value == null) return null
        val type = object : TypeToken<Map<String, Double>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromMap(map: Map<String, Double>?): String? {
        return map?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun fromStringToMapString(value: String?): Map<String, String>? {
        if (value == null) return null
        val type = object : TypeToken<Map<String, String>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromMapStringToString(map: Map<String, String>?): String? {
        return map?.let { gson.toJson(it) }
    }
}


