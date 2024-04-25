package com.bond.fold.flutter_l10n_fold

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.internal.Streams
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class MapTypeAdapter : TypeAdapter<Map<String, String>>() {
    override fun write(out: JsonWriter?, value: Map<String, String>?) {
        Gson().toJson(value, object : TypeToken<Map<String, String>>() {}.type, out)
    }

    override fun read(input: JsonReader?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        val jsonElement = Streams.parse(input)
        for ((key, value) in jsonElement.asJsonObject.entrySet()) {
            map[key] = value.asString
        }
        return map
    }
}