package id.codemockup.ramu.core.network

import id.codemockup.ramu.core.data.remote.response.Response
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/** Adapts native Hermes payloads to the application envelope without changing their wire shape. */
internal class HermesResponseConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit,
    ): Converter<ResponseBody, *>? {
        if (getRawType(type) != Response::class.java) return null
        require(type is ParameterizedType) { "Hermes Response must declare a payload type." }
        val payloadType = getParameterUpperBound(0, type)
        val delegate = retrofit.nextResponseBodyConverter<Any>(this, payloadType, annotations)
        return Converter<ResponseBody, Response<Any>> { body ->
            Response(requireNotNull(delegate.convert(body)) { "Hermes returned an empty response body." })
        }
    }
}
