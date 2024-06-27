import com.google.gson.annotations.SerializedName

// Dust 클래스는 API 응답에서 response 필드를 가지고 있습니다.
data class Dust(val response: DustResponse)

// DustResponse 클래스는 API 응답에서 body와 header 필드를 가지고 있습니다.
data class DustResponse(
    @SerializedName("body")
    val dustBody: DustBody, // DustBody 객체를 담고 있습니다.
    @SerializedName("header")
    val dustHeader: DustHeader // DustHeader 객체를 담고 있습니다.
)

// DustBody 클래스는 API 응답의 body 부분을 나타냅니다.
data class DustBody(
    val totalCount: Int, // 총 데이터 수를 나타냅니다.
    @SerializedName("items")
    val dustItem: MutableList<DustItem>?, // DustItem 객체의 리스트를 담고 있습니다.
    val pageNo: Int, // 페이지 번호를 나타냅니다.
    val numOfRows: Int // 한 페이지에 표시되는 데이터 수를 나타냅니다.
)

// DustHeader 클래스는 API 응답의 header 부분을 나타냅니다.
data class DustHeader(
    val resultCode: String, // 결과 코드를 나타냅니다.
    val resultMsg: String // 결과 메시지를 나타냅니다.
)

// DustItem 클래스는 미세먼지 데이터를 나타냅니다.
data class DustItem(
    val so2Grade: String,
    val coFlag: String?,
    val khaiValue: String,
    val so2Value: String,
    val coValue: String,
    val pm25Flag: String?,
    val pm10Flag: String?,
    val o3Grade: String,
    val pm10Value: String,
    val khaiGrade: String,
    val pm25Value: String,
    val sidoName: String,
    val no2Flag: String?,
    val no2Grade: String,
    val o3Flag: String?,
    val pm25Grade: String,
    val so2Flag: String?,
    val dataTime: String,
    val coGrade: String,
    val no2Value: String,
    val stationName: String, // 측정소 이름을 나타냅니다.
    val pm10Grade: String,
    val o3Value: String
)
