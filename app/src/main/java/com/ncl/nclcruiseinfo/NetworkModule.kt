package com.ncl.nclcruiseinfo

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

private fun provideNCLCruiseInfoAPI(retrofit: Retrofit): NCLCruiseInfoApi =
    retrofit.createRetrofit()

val networkModule = module {

    single {
        provideNCLCruiseInfoAPI(retrofit = get())
    }

    single { provideHTTPLoggingInterceptor() }

    factory {
        GsonBuilder()
            .disableHtmlEscaping()
            .setLenient()
            .setPrettyPrinting()
    }

    single {
        provideGson(gsonBuilder = get())
    }

    single {
        provideDefaultOkHttpClient(
            httpLoggingInterceptor = get()
        )
    }

    single {
        provideRetrofit(
            okHttpClient = get(),
            gSon = get()
        )
    }
}


private fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {

    return HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

private fun provideGson(gsonBuilder: GsonBuilder): Gson {
    return gsonBuilder.create()
}

private fun provideDefaultOkHttpClient(
    httpLoggingInterceptor: HttpLoggingInterceptor
): OkHttpClient {

    val okHttpClientBuilder: OkHttpClient.Builder = when {
        BuildConfig.DEBUG -> provideUnsafeOkHttpBuilder().addInterceptor(httpLoggingInterceptor)
        else -> OkHttpClient.Builder()
    }

    okHttpClientBuilder
        .connectTimeout(Settings.WS_CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .readTimeout(Settings.WS_READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .writeTimeout(Settings.WS_WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .callTimeout(Settings.WS_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .addInterceptor(provideHTTPLoggingInterceptor())

    return okHttpClientBuilder.build()
}

private fun provideUnsafeOkHttpBuilder(): OkHttpClient.Builder {
    try {

        val trustAllManager = object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            @Throws(CertificateException::class)

            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            @Throws(CertificateException::class)
            override fun checkServerTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return emptyArray()
            }
        }

        // Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(trustAllManager)

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("TLSv1.2")
        sslContext.init(null, trustAllCerts, SecureRandom())

        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory

        return OkHttpClient.Builder().apply {
            sslSocketFactory(sslSocketFactory, trustAllManager)
        }
    } catch (e: Exception) {
        throw RuntimeException(e)
    }

}

fun provideRetrofit(okHttpClient: OkHttpClient, gSon: Gson): Retrofit {

    return Retrofit.Builder()
        .baseUrl("https://www.ncl.com/")
        .addConverterFactory(GsonConverterFactory.create(gSon))
        .client(okHttpClient)
        .build()
}

private inline fun <reified T> Retrofit.createRetrofit(): T = this.create(T::class.java)

