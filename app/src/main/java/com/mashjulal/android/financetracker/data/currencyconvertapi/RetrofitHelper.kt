package com.mashjulal.android.financetracker.data.currencyconvertapi

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

private const val BASE_URL = "https://free.currencyconverterapi.com/"

/**
 * Helper class for building retrofit client for currencyconverterapi.com.
 */
class RetrofitHelper {

    val service: CurrencyService by lazy {
        val gson = GsonBuilder()
                .registerTypeAdapter(CurrencyRateModel::class.java, CurrencyRateJsonDeserializer())
                .setLenient()
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        retrofit.create(CurrencyService::class.java)
    }
}

class CurrencyRateJsonDeserializer : JsonDeserializer<CurrencyRateModel> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): CurrencyRateModel {
        val jsonObject = json.asJsonObject
        val keys = jsonObject.keySet().toList()
        val value = jsonObject.get(keys[0])
        return CurrencyRateModel(value.asJsonObject.get("val").asBigDecimal)
    }

}