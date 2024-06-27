package com.example.finedust

import DustItem // DustItem 클래스를 가져옵니다.
import NetWorkClient // NetWorkClient를 가져옵니다.
import android.graphics.Color // Color 클래스를 가져옵니다.
import android.os.Bundle // Bundle 클래스를 가져옵니다.
import androidx.appcompat.app.AppCompatActivity // AppCompatActivity를 가져옵니다.
import androidx.lifecycle.lifecycleScope // lifecycleScope를 가져옵니다.
import com.example.finedust.databinding.ActivityMainBinding // ActivityMainBinding을 가져옵니다.
import com.skydoves.powerspinner.IconSpinnerAdapter // IconSpinnerAdapter를 가져옵니다.
import kotlinx.coroutines.launch // launch를 가져옵니다.

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) } // ActivityMainBinding을 초기화합니다.
    var items = mutableListOf<DustItem>() // DustItem을 저장할 리스트를 초기화합니다.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 시/도 스피너 아이템 선택 리스너를 설정합니다.
        binding.spinnerViewSido.setOnSpinnerItemSelectedListener<String> { _, _, _, text ->
            communicateNetWork(setUpDustParameter(text))
        }

        // 구/군 스피너 아이템 선택 리스너를 설정합니다.
        binding.spinnerViewGoo.setOnSpinnerItemSelectedListener<String> { _, _, _, text ->

            // 선택된 항목을 필터링하여 가져옵니다.
            var selectedItem = items.filter { f -> f.stationName == text }

            // 선택된 항목의 정보를 UI에 표시합니다.
            binding.tvCityname.text = selectedItem[0].sidoName + "  " + selectedItem[0].stationName
            binding.tvDate.text = selectedItem[0].dataTime
            binding.tvP10value.text = selectedItem[0].pm10Value + " ㎍/㎥"

            // 미세먼지 등급에 따라 UI를 업데이트합니다.
            when (getGrade(selectedItem[0].pm10Value)) {
                1 -> {
                    binding.mainBg.setBackgroundColor(Color.parseColor("green"))
                    binding.ivFace.setImageResource(R.drawable.mise1)
                    binding.tvP10grade.text = "좋음"
                    binding.tvP10grade.setTextColor(Color.BLUE)
                }

                2 -> {
                    binding.mainBg.setBackgroundColor(Color.parseColor("#D6A478"))
                    binding.ivFace.setImageResource(R.drawable.mise2)
                    binding.tvP10grade.text = "보통"
                    binding.tvP10grade.setTextColor(Color.GREEN)
                }

                3 -> {
                    binding.mainBg.setBackgroundColor(Color.parseColor("#DF7766"))
                    binding.ivFace.setImageResource(R.drawable.mise3)
                    binding.tvP10grade.text = "나쁨"
                    binding.tvP10grade.setTextColor(Color.RED)
                }

                4 -> {
                    binding.mainBg.setBackgroundColor(Color.parseColor("#BB3320"))
                    binding.ivFace.setImageResource(R.drawable.mise4)
                    binding.tvP10grade.text = "매우나쁨"
                    binding.tvP10grade.setTextColor(Color.MAGENTA)
                }
            }
        }
    }

    // 네트워크 통신을 수행합니다.
    private fun communicateNetWork(param: HashMap<String, String>) = lifecycleScope.launch() {
        val responseData = NetWorkClient.dustNetWork.getDust(param) // 네트워크로부터 데이터를 가져옵니다.

        val adapter = IconSpinnerAdapter(binding.spinnerViewGoo) // 스피너 아이템을 설정합니다.
        items = responseData.response.dustBody.dustItem!! // 가져온 데이터를 리스트에 저장합니다.

        val goo = ArrayList<String>() // 스피너 아이템 리스트를 초기화합니다.
        items.forEach {
            goo.add(it.stationName) // 스피너 아이템을 추가합니다.
        }

        runOnUiThread {
            binding.spinnerViewGoo.setItems(goo) // 스피너 아이템을 설정합니다.
        }

    }

    // 미세먼지 등급을 반환합니다.
    private fun setUpDustParameter(sido: String): HashMap<String, String> {
        val authKey = // 인증 키를 설정합니다.
            "YmWn6FdXPsSKW2aoiMfRHcpeD46e2tlQ/dR0wjyfYbcWJZuyGfslAgWC3dvqQHXdNkr2sqouUzYjqQfxUTPEQg=="
        return hashMapOf( // 파라미터를 설정하여 반환합니다.
            "serviceKey" to authKey,
            "returnType" to "json",
            "numOfRows" to "100",
            "pageNo" to "1",
            "sidoName" to sido,
            "ver" to "1.0"
        )
    }

    // 미세먼지 등급을 반환합니다.
    fun getGrade(value: String): Int {
        val mValue = value.toInt() // 입력값을 정수로 변환합니다.
        var grade = 1 // 기본 등급을 설정합니다.
        grade = if (mValue >= 0 && mValue <= 30) { // 범위에 따라 등급을 설정합니다.
            1
        } else if (mValue >= 31 && mValue <= 80) {
            2
        } else if (mValue >= 81 && mValue <= 100) {
            3
        } else 4
        return grade // 등급을 반환합니다.
    }
}
