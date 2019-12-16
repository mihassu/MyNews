package ru.mihassu.mynews.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import ru.mihassu.mynews.data.network.RegnumApi;

@Module
public class AppComponentModule {

    private  final String BASE_URL = "https://regnum.ru/";

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
    return new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(SimpleXmlConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
    }

    @Provides
    @Singleton
    RegnumApi provideRegnumApi(Retrofit retrofit) {
        return retrofit.create(RegnumApi.class);
    }
}
