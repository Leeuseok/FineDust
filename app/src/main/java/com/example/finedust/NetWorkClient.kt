import com.example.finedust.BuildConfig
import com.example.finedust.NetWorkInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetWorkClient {
    // 데이터를 가져올 base URL
    private const val DUST_BASE_URL = "https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/"

    // OkHttpClient 생성
    private fun createOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()

        // 디버그 모드일 때만 로깅을 활성화
        if (BuildConfig.DEBUG)
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        else
            interceptor.level = HttpLoggingInterceptor.Level.NONE

        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS) // 연결 타임아웃 설정
            .readTimeout(20, TimeUnit.SECONDS) // 읽기 타임아웃 설정
            .writeTimeout(20, TimeUnit.SECONDS) // 쓰기 타임아웃 설정
            .addNetworkInterceptor(interceptor) // 네트워크 인터셉터 추가
            .build()
    }

    // Retrofit 객체 생성
    private val dustRetrofit = Retrofit.Builder()
        .baseUrl(DUST_BASE_URL) // 베이스 URL 설정
        .addConverterFactory(GsonConverterFactory.create()) // Gson 컨버터 팩토리 추가
        .client(createOkHttpClient()) // OkHttpClient 설정
        .build()

    // Retrofit 인터페이스 구현체 생성
    val dustNetWork: NetWorkInterface = dustRetrofit.create(NetWorkInterface::class.java)
}
