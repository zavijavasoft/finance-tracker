package com.mashjulal.android.financetracker.data.currencyconvertapi

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class CurrencyRateJsonDeserializer : JsonDeserializer<CurrencyRateModel> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): CurrencyRateModel {
        val jsonObject = json.asJsonObject
        val keys = jsonObject.keySet().toList()
        val value = jsonObject.get(keys[0])
        return CurrencyRateModel(value.asJsonObject.get("val").asBigDecimal)
    }
}