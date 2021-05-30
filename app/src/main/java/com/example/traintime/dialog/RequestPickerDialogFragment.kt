package com.example.traintime.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.traintime.database.TimetableRequest
import com.example.traintime.databinding.FragmentRequestPickerDialogBinding

private val COUNTRIES_CITIES = listOf(
    "基隆市", "新北市", "臺北市", "桃園市",
    "新竹縣", "新竹市", "苗栗縣", "臺中市",
    "彰化縣", "南投縣", "雲林縣", "嘉義縣",
    "嘉義市", "臺南市", "高雄市", "屏東縣",
    "臺東縣", "花蓮縣", "宜蘭縣",
)

private val STATIONS = listOf(
    listOf("基隆", "三坑", "八堵", "七堵", "百福", "海科館", "暖暖"),
    listOf(
        "五堵", "汐止", "汐科", "板橋", "浮洲", "樹林", "南樹林", "山佳",
        "鶯歌", "福隆", "貢寮", "雙溪", "牡丹", "三貂嶺", "大華", "十分",
        "望古", "嶺腳", "平溪", "菁桐", "猴硐", "瑞芳", "八斗子", "四腳亭"
    ),
    listOf("南港", "松山", "臺北", "臺北-環島", "萬華"),
    listOf("桃園", "內壢", "中壢", "埔心", "楊梅", "富岡", "新富"),
    listOf(
        "北湖", "湖口", "新豐", "竹北", "竹中", "六家", "上員", "榮華",
        "竹東", "橫山", "九讚頭", "合興", "富貴", "內灣"
    ),
    listOf("北新竹", "千甲", "新莊", "新竹", "三姓橋", "香山"),
    listOf(
        "崎頂", "竹南", "談文", "大山", "後龍", "龍港", "白沙屯", "新埔",
        "通霄", "苑裡", "造橋", "豐富", "苗栗", "南勢", "銅鑼", "三義"
    ),
    listOf(
        "日南", "大甲", "臺中港", "清水", "沙鹿", "龍井", "大肚", "追分",
        "泰安", "后里", "豐原", "栗林", "潭子", "頭家厝", "松竹", "太原",
        "精武", "臺中", "五權", "大慶", "烏日", "新烏日", "成功"
    ),
    listOf(
        "彰化", "花壇", "大村", "員林", "永靖", "社頭", "田中", "二水",
        "源泉"
    ),
    listOf("濁水", "龍泉", "集集", "水里", "車埕"),
    listOf("林內", "石榴", "斗六", "斗南", "石龜"),
    listOf("大林", "民雄", "水上", "南靖"),
    listOf("嘉北", "嘉義"),
    listOf(
        "後壁", "新營", "柳營", "林鳳營", "隆田", "拔林", "善化", "南科",
        "新市", "永康", "大橋", "臺南", "保安", "仁德", "中洲", "長榮大學",
        "沙崙"
    ),
    listOf(
        "大湖", "路竹", "岡山", "橋頭", "楠梓", "新左營", "左營", "內惟",
        "美術館", "鼓山", "三塊厝", "高雄", "民族", "科工館", "正義", "鳳山",
        "後庄", "九曲堂"
    ),
    listOf(
        "六塊厝", "屏東", "歸來", "麟洛", "西勢", "竹田", "潮州", "崁頂",
        "南州", "鎮安", "林邊", "佳冬", "東海", "枋寮", "加祿", "內獅",
        "枋山"
    ),
    listOf(
        "大武", "瀧溪", "金崙", "太麻里", "知本", "康樂", "臺東", "山里",
        "鹿野", "瑞源", "瑞和", "關山", "海端", "池上"
    ),
    listOf(
        "富里", "東竹", "東里", "玉里", "三民", "瑞穗", "富源", "大富",
        "光復", "萬榮", "鳳林", "南平", "林榮新光", "豐田", "壽豐", "平和",
        "志學", "吉安", "花蓮", "北埔", "景美", "新城", "崇德", "和仁",
        "和平"
    ),
    listOf(
        "漢本", "武塔", "南澳", "東澳", "永樂", "蘇澳", "蘇澳新", "新馬",
        "冬山", "羅東", "中里", "二結", "宜蘭", "四城", "礁溪", "頂埔",
        "頭城", "外澳", "龜山", "大溪", "大里", "石城"
    )
)

private val STATION_IDS = listOf(
    listOf("0900", "0910", "0920", "0930", "0940", "7361", "7390"),
    listOf(
        "0950", "0960", "0970", "1020", "1030", "1040", "1050", "1060",
        "1070", "7290", "7300", "7310", "7320", "7330", "7331", "7332",
        "7333", "7334", "7335", "7336", "7350", "7360", "7362", "7380"
    ),
    listOf("0980", "0990", "1000", "1001", "1010"),
    listOf("1080", "1090", "1100", "1110", "1120", "1130", "1140"),
    listOf(
        "1150", "1160", "1170", "1180", "1193", "1194", "1201", "1202",
        "1203", "1204", "1205", "1206", "1207", "1208"
    ),
    listOf("1190", "1191", "1192", "1210", "1220", "1230"),
    listOf(
        "1240", "1250", "2110", "2120", "2130", "2140", "2150", "2160",
        "2170", "2180", "3140", "3150", "3160", "3170", "3180", "3190"
    ),
    listOf(
        "2190", "2200", "2210", "2220", "2230", "2240", "2250", "2260",
        "3210", "3220", "3230", "3240", "3250", "3260", "3270", "3280",
        "3290", "3300", "3310", "3320", "3330", "3340", "3350"
    ),
    listOf(
        "3360", "3370", "3380", "3390", "3400", "3410", "3420", "3430",
        "3431"
    ),
    listOf("3432", "3433", "3434", "3435", "3436"),
    listOf("3450", "3460", "3470", "3480", "3490"),
    listOf("4050", "4060", "4090", "4100"),
    listOf("4070", "4080"),
    listOf(
        "4110", "4120", "4130", "4140", "4150", "4160", "4170", "4180",
        "4190", "4200", "4210", "4220", "4250", "4260", "4270", "4271",
        "4272"
    ),
    listOf(
        "4290", "4300", "4310", "4320", "4330", "4340", "4350", "4360",
        "4370", "4380", "4390", "4400", "4410", "4420", "4430", "4440",
        "4450", "4460"
    ),
    listOf(
        "4470", "5000", "5010", "5020", "5030", "5040", "5050", "5060",
        "5070", "5080", "5090", "5100", "5110", "5120", "5130", "5140",
        "5160"
    ),
    listOf(
        "5190", "5200", "5210", "5220", "5230", "5240", "6000", "6010",
        "6020", "6030", "6040", "6050", "6060", "6070"
    ),
    listOf(
        "6080", "6090", "6100", "6110", "6120", "6130", "6140", "6150",
        "6160", "6170", "6180", "6190", "6200", "6210", "6220", "6230",
        "6240", "6250", "7000", "7010", "7020", "7030", "7040", "7050",
        "7060"
    ),
    listOf(
        "7070", "7080", "7090", "7100", "7110", "7120", "7130", "7140",
        "7150", "7160", "7170", "7180", "7190", "7200", "7210", "7220",
        "7230", "7240", "7250", "7260", "7270", "7280"
    )
)

class RequestPickerDialogFragment : DialogFragment() {
    private var mListener: ((TimetableRequest) -> Unit)? = null

    fun setListener(listener: (TimetableRequest) -> Unit) {
        mListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val binding = FragmentRequestPickerDialogBinding.inflate(it.layoutInflater).apply {
                fromCountryOrCity.data = COUNTRIES_CITIES
                fromStation.data = STATIONS[0]
                toCountryOrCity.data = COUNTRIES_CITIES
                toStation.data = STATIONS[0]

                fromCountryOrCity.setOnItemSelectedListener { _, _, position ->
                    fromStation.data = STATIONS[position]
                    fromStation.selectedItemPosition = 0
                }
                toCountryOrCity.setOnItemSelectedListener { _, _, position ->
                    toStation.data = STATIONS[position]
                    toStation.selectedItemPosition = 0
                }

                confirmButton.setOnClickListener {
                    mListener?.invoke(
                        TimetableRequest(
                            fromCountryOrCity.selectedItemData as String,
                            toCountryOrCity.selectedItemData as String,
                            fromStation.selectedItemData as String,
                            toStation.selectedItemData as String,
                            STATION_IDS[fromCountryOrCity.selectedItemPosition][fromStation.selectedItemPosition],
                            STATION_IDS[toCountryOrCity.selectedItemPosition][toStation.selectedItemPosition],
                        )
                    )
                }
            }

            builder.setView(binding.root)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // set background transparent
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}