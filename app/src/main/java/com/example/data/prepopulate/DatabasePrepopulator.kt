package com.example.data.prepopulate

import com.example.data.local.PayanMitraDatabase
import com.example.data.local.entities.AreaEntity
import com.example.data.local.entities.BusRouteEntity
import com.example.data.local.entities.BusStopEntity
import com.example.data.local.entities.DistrictEntity
import com.example.data.local.entities.LiveBusEntity
import com.example.data.local.entities.StateEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DatabasePrepopulator {

    suspend fun prepopulate(db: PayanMitraDatabase) = withContext(Dispatchers.IO) {
        // 1. ALL 36 STATES & UNION TERRITORIES OF INDIA
        val states = listOf(
            StateEntity("TN", "Tamil Nadu", "தமிழ்நாடு", "तमिलनाडु", "TNSTC / MTC / SETC", "https://tnstc.in", "18002581071"),
            StateEntity("KA", "Karnataka", "கர்நாடகா", "कर्नाटक", "KSRTC / BMTC / NWKRTC", "https://ksrtc.karnataka.gov.in", "08022212121"),
            StateEntity("KL", "Kerala", "கேரளா", "केरल", "KSRTC Kerala (Swift)", "https://online.keralartc.com", "18001231321"),
            StateEntity("MH", "Maharashtra", "மகாராஷ்டிரா", "महाराष्ट्र", "MSRTC (Shivneri)", "https://msrtc.in", "18002330095"),
            StateEntity("UP", "Uttar Pradesh", "உத்தரப் பிரதேசம்", "उत्तर प्रदेश", "UPSRTC (Janrath)", "https://upsrtc.up.gov.in", "18001802877"),
            StateEntity("DL", "Delhi", "டெல்லி", "दिल्ली", "DTC / DIMTS", "https://dtc.delhi.gov.in", "011-23386084"),
            StateEntity("GJ", "Gujarat", "குஜராத்", "गुजरात", "GSRTC", "https://gsrtc.in", "18002331071"),
            StateEntity("RJ", "Rajasthan", "ராஜஸ்தான்", "राजस्थान", "RSRTC", "https://rsrtconline.rajasthan.gov.in", "0141-5110011"),
            StateEntity("TG", "Telangana", "தெலங்கானா", "तेलंगाना", "TSRTC", "https://tsrtconline.in", "040-69440000"),
            StateEntity("AP", "Andhra Pradesh", "ஆந்திரப் பிரதேசம்", "आंध्र प्रदेश", "APSRTC (Garuda)", "https://apsrtconline.in", "0866-2570005"),
            StateEntity("GA", "Goa", "கோவா", "गोवा", "KTC Kadamba", "https://ktclgoa.com", "0832-2224188"),
            StateEntity("WB", "West Bengal", "மேற்கு வங்கம்", "पश्चिम बंगाल", "WBTC / SBSTC", "https://wbtc.co.in", "033-22361924"),
            StateEntity("PB", "Punjab", "பஞ்சாப்", "पंजाब", "PRTC / Punbus", "https://punbus.punjab.gov.in", "0172-2704023"),
            StateEntity("HR", "Haryana", "ஹரியானா", "हरियाणा", "Haryana Roadways", "https://hartrans.gov.in", "0172-2704014"),
            StateEntity("MP", "Madhya Pradesh", "மத்தியப் பிரதேசம்", "मध्य प्रदेश", "MPRTC / Amrit Bus", "https://transport.mp.gov.in", "0755-2555555"),
            StateEntity("BR", "Bihar", "பீகார்", "बिहार", "BSRTC", "https://bsrtc.bihar.gov.in", "0612-2222222"),
            StateEntity("OR", "Odisha", "ஒடிசா", "ओडिशा", "OSRTC", "https://osrtc.in", "18003451122"),
            StateEntity("AS", "Assam", "அசாம்", "असम", "ASTC", "https://astc.assam.gov.in", "0361-2730033"),
            StateEntity("UT", "Uttarakhand", "உத்தரகாண்ட்", "उत्तराखंड", "UTC Uttarakhand", "https://utconline.uk.gov.in", "18001804145"),
            StateEntity("HP", "Himachal Pradesh", "இமாச்சலப் பிரதேசம்", "हिमाचल प्रदेश", "HRTC Himachal", "https://hrtchp.com", "0177-2656326"),
            StateEntity("JH", "Jharkhand", "ஜார்ஃகண்ட்", "झारखंड", "JSRTC", "https://jsrtc.jharkhand.gov.in", "0651-2400000"),
            StateEntity("CT", "Chhattisgarh", "சத்தீஸ்கர்", "छत्तीसगढ़", "CGSRTC", "https://cgtransport.gov.in", "0771-2400000"),
            StateEntity("JK", "Jammu and Kashmir", "ஜம்மு காஷ்மீர்", "जम्मू और कश्मीर", "JKSRTC", "https://jksrtc.jk.gov.in", "0191-2476000"),
            StateEntity("TR", "Tripura", "திரிபுரா", "त्रिपुरा", "TRTC Tripura", "https://trtc.tripura.gov.in", "0381-2323000"),
            StateEntity("ML", "Meghalaya", "மேகலாயா", "मेघालय", "MTC Meghalaya", "https://megtransport.gov.in", "0364-2222000"),
            StateEntity("MN", "Manipur", "மணிப்பூர்", "मणिपुर", "MSRTC Manipur", "https://manipur.gov.in", "0385-2444000"),
            StateEntity("NL", "Nagaland", "நாகாலாந்து", "नागालैंड", "NST Nagaland", "https://nst.nagaland.gov.in", "03862-222000"),
            StateEntity("MZ", "Mizoram", "மிசோரம்", "मिजोरम", "MZRTC Mizoram", "https://transport.mizoram.gov.in", "0389-2322000"),
            StateEntity("SK", "Sikkim", "சிக்கிம்", "सिक्किम", "SNT Sikkim", "https://sikkim.gov.in", "03592-202000"),
            StateEntity("AR", "Arunachal Pradesh", "அருணாச்சலப் பிரதேசம்", "अरुणाचल प्रदेश", "APSTS Arunachal", "https://apsts.arunachal.gov.in", "0360-2212000"),
            StateEntity("CH", "Chandigarh", "சண்டிகர்", "चंडीगढ़", "CTU Chandigarh", "https://chdctu.gov.in", "0172-2704000"),
            StateEntity("PY", "Puducherry", "புதுச்சேரி", "पुडुचेरी", "PRTC Puducherry", "https://py.gov.in", "0413-2280000"),
            StateEntity("AN", "Andaman and Nicobar", "அந்தமான் நிக்கோபார்", "अंडमान और निकोबार", "STS Andaman", "https://andaman.gov.in", "03192-232000"),
            StateEntity("LA", "Ladakh", "லடாக்", "लद्दाख", "Ladakh Transport Service", "https://ladakh.gov.in", "01982-252000"),
            StateEntity("DN", "Dadra and Nagar Haveli", "தாத்ரா நாகர் ஹவேலி", "दादरा और नगर हवेली", "DNHSRTC", "https://dnh.gov.in", "0260-2642000"),
            StateEntity("LD", "Lakshadweep", "லட்சத்தீவு", "लक्षद्वीप", "Lakshadweep Transport", "https://lakshadweep.gov.in", "04896-262000")
        )
        db.stateDao().insertStates(states)

        // 2. MAJOR DISTRICTS PER STATE/UT
        val districts = listOf(
            // TAMIL NADU
            DistrictEntity("TN_CHE", "Chennai", "சென்னை", "चेन्नई", "TN", 13.0827, 80.2707),
            DistrictEntity("TN_CBE", "Coimbatore", "கோயம்புத்தூர்", "कोयंबटूर", "TN", 11.0168, 76.9558),
            DistrictEntity("TN_MDU", "Madurai", "மதுரை", "मदुरै", "TN", 9.9252, 78.1198),
            DistrictEntity("TN_DGL", "Dindigul", "திண்டுக்கல்", "दिंडीगुल", "TN", 10.3673, 77.9803),
            DistrictEntity("TN_TPJ", "Tiruchirappalli", "திருச்சிராப்பள்ளி", "तिरुचिरापल्ली", "TN", 10.7905, 78.7047),

            // KARNATAKA
            DistrictEntity("KA_BLR", "Bengaluru Urban", "பெங்களூரு", "बेंगलुरु", "KA", 12.9716, 77.5946),
            DistrictEntity("KA_MYS", "Mysuru", "மைசூரு", "मैसूर", "KA", 12.2958, 76.6394),
            DistrictEntity("KA_MNG", "Mangaluru", "மங்களூரு", "मंगलुरु", "KA", 12.9141, 74.8560),

            // KERALA
            DistrictEntity("KL_TVM", "Thiruvananthapuram", "திருவனந்தபுரம்", "तिरुवनंतपुरम", "KL", 8.5241, 76.9366),
            DistrictEntity("KL_EKM", "Ernakulam / Kochi", "கொச்சி", "एर्णाकुलम", "KL", 9.9312, 76.2673),

            // MAHARASHTRA
            DistrictEntity("MH_MUM", "Mumbai", "மும்பை", "मुंबई", "MH", 18.9388, 72.8355),
            DistrictEntity("MH_PUN", "Pune", "புனே", "पुणे", "MH", 18.5204, 73.8567),

            // UTTAR PRADESH
            DistrictEntity("UP_LKO", "Lucknow", "லக்னோ", "लखनऊ", "UP", 26.8467, 80.9462),
            DistrictEntity("UP_VNS", "Varanasi", "வாரணாசி", "वाराणसी", "UP", 25.3176, 82.9739),

            // DELHI
            DistrictEntity("DL_DEL", "Central Delhi", "டெல்லி", "दिल्ली", "DL", 28.6139, 77.2090),

            // GUJARAT
            DistrictEntity("GJ_AMD", "Ahmedabad", "அகமதாபாத்", "अहमदाबाद", "GJ", 23.0225, 72.5714),
            DistrictEntity("GJ_SUR", "Surat", "சூரத்", "सूरत", "GJ", 21.1702, 72.8311),

            // RAJASTHAN
            DistrictEntity("RJ_JAI", "Jaipur", "ஜெய்ப்பூர்", "जयपुर", "RJ", 26.9124, 75.7873),
            DistrictEntity("RJ_UDI", "Udaipur", "உதய்பூர்", "उदयपुर", "RJ", 24.5854, 73.7125),

            // TELANGANA & ANDHRA PRADESH
            DistrictEntity("TG_HYD", "Hyderabad", "ஹைதராபாத்", "हैदराबाद", "TG", 17.3850, 78.4867),
            DistrictEntity("AP_VGA", "Vijayawada", "விஜயவாடா", "विजयवाड़ा", "AP", 16.5062, 80.6480),

            // GOA
            DistrictEntity("GA_PAN", "Panaji Goa", "பனாஜி", "पणजी", "GA", 15.4909, 73.8278),

            // WEST BENGAL, PUNJAB, HARYANA, MP, BIHAR
            DistrictEntity("WB_KOL", "Kolkata", "கொல்கத்தா", "कोलकाता", "WB", 22.5726, 88.3639),
            DistrictEntity("PB_ASR", "Amritsar", "அமிர்தசரஸ்", "अमृतसर", "PB", 31.6340, 74.8723),
            DistrictEntity("HR_GUG", "Gurugram", "குருகிராம்", "गुरुग्राम", "HR", 28.4595, 77.0266),
            DistrictEntity("MP_BHO", "Bhopal", "போபால்", "भोपाल", "MP", 23.2599, 77.4126),
            DistrictEntity("BR_PAT", "Patna", "பாட்னா", "पटना", "BR", 25.5941, 85.1376),
            DistrictEntity("UT_DDN", "Dehradun", "டெஹ்ராடூன்", "देहरादून", "UT", 30.3165, 78.0322),
            DistrictEntity("HP_SHI", "Shimla", "சிம்லா", "शिमला", "HP", 31.1048, 77.1734),
            DistrictEntity("JK_SGR", "Srinagar", "ஸ்ரீநகர்", "श्रीनगर", "JK", 34.0837, 74.7973),
            DistrictEntity("CH_CHD", "Chandigarh City", "சண்டிகர்", "चंडीगढ़", "CH", 30.7333, 76.7794),
            DistrictEntity("PY_PDY", "Puducherry Central", "புதுச்சேரி", "पुडुचेरी", "PY", 11.9416, 79.8083)
        )
        db.districtDao().insertDistricts(districts)

        // 3. AREAS FOR EACH DISTRICT
        val areas = listOf(
            AreaEntity("DGL_01", "Dindigul Central", "திண்டுக்கல் மையம்", "दिंडीगुल सेंट्रल", "TN_DGL", "624001", false, 10.3673, 77.9803),
            AreaEntity("DGL_02", "Palani", "பழனி", "पलानी", "TN_DGL", "624601", false, 10.4500, 77.5200),
            AreaEntity("CHE_01", "Chennai Koyambedu", "சென்னை கோயம்பேடு", "चेन्नई कोयम्बेडु", "TN_CHE", "600107", false, 13.0694, 80.1948),
            AreaEntity("CBE_01", "Coimbatore Gandhipuram", "கோயம்புத்தூர் காந்திபுரம்", "कोयंबटूर गांधीपुरम", "TN_CBE", "641012", false, 11.0183, 76.9644),
            AreaEntity("MDU_01", "Madurai Mattuthavani", "மதுரை மாட்டுத்தாவணி", "मदुरै माटुथवानी", "TN_MDU", "625007", false, 9.9485, 78.1560),
            AreaEntity("BLR_01", "Bengaluru Majestic", "பெங்களூரு மெஜஸ்டிக்", "बेंगलुरु मैजेस्टिक", "KA_BLR", "560009", false, 12.9767, 77.5713),
            AreaEntity("MYS_01", "Mysuru Suburb Stand", "மைசூரு", "मैसूर", "KA_MYS", "570001", false, 12.3050, 76.6550),
            AreaEntity("TVM_01", "Thiruvananthapuram Central", "திருவனந்தபுரம்", "तिरुवनंतपुरम", "KL_TVM", "695001", false, 8.4882, 76.9520),
            AreaEntity("EKM_01", "Kochi Vytilla Mobility Hub", "கொச்சி", "कोच्चि", "KL_EKM", "682019", false, 9.9658, 76.3202),
            AreaEntity("MUM_01", "Mumbai Dadar TT", "மும்பை தாதர்", "मुंबई दादर", "MH_MUM", "400014", false, 19.0178, 72.8478),
            AreaEntity("PUN_01", "Pune Swargate", "புனே ஸ்வார்கேட்", "पुणे स्वारगेट", "MH_PUN", "411042", false, 18.5018, 73.8586),
            AreaEntity("LKO_01", "Lucknow Alambagh", "லக்னோ ஆலம்பாக்", "लखनऊ आलमबाग", "UP_LKO", "226005", false, 26.8150, 80.9000),
            AreaEntity("DEL_01", "Delhi ISBT Kashmere Gate", "டெல்லி காஷ்மீர் கேட்", "दिल्ली कश्मीरी गेट", "DL_DEL", "110006", false, 28.6667, 77.2300),
            AreaEntity("AMD_01", "Ahmedabad Geeta Mandir", "அகமதாபாத் கீதா மந்திர்", "अहमदाबाद गीता मंदिर", "GJ_AMD", "380022", false, 23.0125, 72.5850),
            AreaEntity("JAI_01", "Jaipur Sindhi Camp", "ஜெய்ப்பூர் சிந்தி கேம்ப்", "जयपुर सिंधी कैंप", "RJ_JAI", "302001", false, 26.9250, 75.7980),
            AreaEntity("HYD_01", "Hyderabad MGBS", "ஹைதராபாத் MGBS", "हैदराबाद एमजीबीएस", "TG_HYD", "500001", false, 17.3783, 78.4811),
            AreaEntity("VGA_01", "Vijayawada PNBS", "விஜயவாடா PNBS", "विजयवाड़ा पीएनबीएस", "AP_VGA", "520001", false, 16.5083, 80.6200),
            AreaEntity("PAN_01", "Panaji Kadamba Bus Stand", "பனாஜி கடம்பா", "पणजी कदंब", "GA_PAN", "403001", false, 15.4950, 73.8350),
            AreaEntity("KOL_01", "Kolkata Esplanade", "கொல்கத்தா எஸ்பிளனேடு", "कोलकाता एस्प्लेनेड", "WB_KOL", "700069", false, 22.5645, 88.3512),
            AreaEntity("ASR_01", "Amritsar Bus Stand", "அமிர்தசரஸ்", "अमृतसर", "PB_ASR", "143001", false, 31.6250, 74.8800),
            AreaEntity("SHI_01", "Shimla ISBT Tutikandi", "சிம்லா ISBT", "शिमला आईएसबीटी", "HP_SHI", "171004", false, 31.0900, 77.1550)
        )
        db.areaDao().insertAreas(areas)

        // 4. REALISTIC BUS ROUTES FOR STATES ACROSS INDIA
        val routes = listOf(
            // TAMIL NADU (TNSTC / SETC)
            BusRouteEntity("182", "Dindigul Central", "Palani", "TN", "TNSTC Dindigul", 60, 80, 45.0, "EXPRESS", "Every 15 min", "MON-SUN", "05:00 AM", "10:30 PM", "Reddiyarchatram, Oddanchatram", true, false, false, 52),
            BusRouteEntity("SETC-101", "Chennai Koyambedu", "Madurai Mattuthavani", "TN", "SETC Tamil Nadu", 460, 480, 450.0, "ULTRA DELUXE AC", "Every 30 min", "MON-SUN", "06:00 AM", "11:30 PM", "Villupuram, Trichy", true, true, false, 36),
            BusRouteEntity("SETC-108", "Chennai Koyambedu", "Coimbatore Gandhipuram", "TN", "SETC Tamil Nadu", 510, 540, 520.0, "AC SLEEPER", "Every 1 hr", "MON-SUN", "07:00 AM", "11:00 PM", "Vellore, Salem, Erode", true, true, true, 30),

            // KARNATAKA (KSRTC Airavat / Rajahamsa)
            BusRouteEntity("KA-FLY-01", "Bengaluru Majestic", "Mysuru Suburb Stand", "KA", "KSRTC Rajahamsa", 145, 180, 220.0, "VOLVO EXECUTIVE AC", "Every 20 min", "MON-SUN", "05:00 AM", "11:45 PM", "Maddur, Mandya", true, true, false, 45),
            BusRouteEntity("KA-AIR-99", "Bengaluru Majestic", "Chennai Koyambedu", "KA", "KSRTC Airavat Club Class", 350, 360, 680.0, "MULTI-AXLE VOLVO AC", "Every 45 min", "MON-SUN", "06:00 AM", "11:30 PM", "Hosur, Krishnagiri, Vellore", true, true, true, 40),

            // MAHARASHTRA (MSRTC Shivneri)
            BusRouteEntity("MH-SHIV-01", "Mumbai Dadar TT", "Pune Swargate", "MH", "MSRTC Shivneri AC", 150, 210, 520.0, "VOLVO AC EXPRESS", "Every 15 min", "MON-SUN", "05:00 AM", "11:59 PM", "Navi Mumbai, Lonavala", true, true, false, 42),

            // UTTAR PRADESH (UPSRTC Janrath)
            BusRouteEntity("UP-JAN-12", "Lucknow Alambagh", "Varanasi Central", "UP", "UPSRTC Janrath AC", 310, 360, 420.0, "JANRATH AC", "Every 30 min", "MON-SUN", "05:30 AM", "10:30 PM", "Rae Bareli, Prayagraj", true, true, false, 48),

            // DELHI (DTC Express)
            BusRouteEntity("DL-DTC-534", "Delhi ISBT Kashmere Gate", "Gurugram IFFCO Chowk", "DL", "DTC Electric Express", 35, 60, 30.0, "LOW FLOOR AC", "Every 10 min", "MON-SUN", "05:00 AM", "11:00 PM", "Connaught Place, Dhaula Kuan", false, true, false, 40),

            // KERALA (KSRTC Swift)
            BusRouteEntity("KL-SWIFT-05", "Thiruvananthapuram Central", "Kochi Vytilla Mobility Hub", "KL", "KSRTC Swift G-Gaja", 205, 270, 310.0, "SEATER AIR SUSPENSION", "Every 30 min", "MON-SUN", "04:30 AM", "11:00 PM", "Kollam, Alappuzha", true, true, false, 44),

            // GUJARAT (GSRTC Gurjarnagri)
            BusRouteEntity("GJ-GSRTC-88", "Ahmedabad Geeta Mandir", "Surat Central", "GJ", "GSRTC Gurjarnagri", 265, 290, 280.0, "EXPRESS AIR SUSPENSION", "Every 20 min", "MON-SUN", "05:00 AM", "11:15 PM", "Nadiad, Vadodara", true, false, false, 50),

            // RAJASTHAN (RSRTC Goldline)
            BusRouteEntity("RJ-RSRTC-101", "Jaipur Sindhi Camp", "Udaipur City Stand", "RJ", "RSRTC Express", 390, 420, 410.0, "SUPER EXPRESS", "Every 1 hr", "MON-SUN", "06:00 AM", "10:00 PM", "Ajmer, Bhilwara", true, false, false, 48),

            // TELANGANA & ANDHRA PRADESH (TSRTC / APSRTC Garuda)
            BusRouteEntity("TG-TSRTC-200", "Hyderabad MGBS", "Vijayawada PNBS", "TG", "TSRTC Garuda Plus AC", 275, 300, 390.0, "VOLVO MULTI-AXLE", "Every 20 min", "MON-SUN", "05:00 AM", "11:30 PM", "Suryapet, Nandigama", true, true, false, 40),

            // GOA (KTC Kadamba)
            BusRouteEntity("GA-KTC-01", "Panaji Kadamba Bus Stand", "Margao KTC Stand", "GA", "KTC Kadamba Express", 33, 50, 40.0, "SHUTTLE AC", "Every 15 min", "MON-SUN", "06:00 AM", "09:30 PM", "Bambolim, Agassaim", false, true, false, 40),

            // WEST BENGAL & HIMACHAL
            BusRouteEntity("WB-WBTC-12", "Kolkata Esplanade", "Howrah Station", "WB", "WBTC AC Express", 12, 30, 20.0, "ELECTRIC AC", "Every 10 min", "MON-SUN", "06:00 AM", "10:30 PM", "Park Street, Rabindra Sadan", false, true, false, 35),
            BusRouteEntity("HP-HRTC-77", "Shimla ISBT Tutikandi", "Chandigarh ISBT Sector 43", "HP", "HRTC Himsuta AC", 115, 210, 350.0, "VOLVO AC HILL EXPRESS", "Every 45 min", "MON-SUN", "05:30 AM", "09:00 PM", "Solan, Kalka", true, true, false, 38)
        )
        db.busRouteDao().insertRoutes(routes)

        // 5. SAMPLE BUS STOPS
        val stops = listOf(
            BusStopEntity("STOP_01", "Dindigul Central Bus Stand", "திண்டுக்கல் பேருந்து நிலையம்", 10.3673, 77.9803, "DGL_01", true, true, true),
            BusStopEntity("STOP_02", "Palani Temple Bus Terminal", "பழனி கோயில் வாசல் நிறுத்தம்", 10.4500, 77.5200, "DGL_02", true, true, true),
            BusStopEntity("STOP_03", "Chennai Koyambedu CMBT", "சென்னை கோயம்பேடு", 13.0694, 80.1948, "CHE_01", true, true, true),
            BusStopEntity("STOP_04", "Bengaluru Majestic KSRTC Stand", "பெங்களூரு மெஜஸ்டிக்", 12.9767, 77.5713, "BLR_01", true, true, true),
            BusStopEntity("STOP_05", "Mumbai Dadar Asiad Bus Stand", "மும்பை தாதர்", 19.0178, 72.8478, "MUM_01", true, true, true),
            BusStopEntity("STOP_06", "Delhi ISBT Kashmere Gate Platform 4", "டெல்லி காஷ்மீர் கேட்", 28.6667, 77.2300, "DEL_01", true, true, true)
        )
        db.busStopDao().insertStops(stops)

        // 6. LIVE BUSES WITH GPS LAT/LON IN THEIR RESPECTIVE STATES
        val liveBuses = listOf(
            LiveBusEntity(
                busNumber = "TN 57 N 2184",
                routeNumber = "182",
                currentLatitude = 10.4000,
                currentLongitude = 77.9300,
                currentSpeed = 52.0,
                totalSeats = 52,
                occupiedSeats = 28,
                availableSeats = 24,
                driverName = "M. Selvam",
                driverPhone = "9842100112",
                busStatus = "ON_TIME",
                delayMinutes = 0,
                lastUpdated = System.currentTimeMillis(),
                hasAIS140 = true,
                isPanicActive = false
            ),
            LiveBusEntity(
                busNumber = "TN 01 AN 8892",
                routeNumber = "SETC-101",
                currentLatitude = 11.9400,
                currentLongitude = 79.4800,
                currentSpeed = 64.0,
                totalSeats = 36,
                occupiedSeats = 20,
                availableSeats = 16,
                driverName = "R. Murugan",
                driverPhone = "9443311224",
                busStatus = "ON_TIME",
                delayMinutes = 0,
                lastUpdated = System.currentTimeMillis(),
                hasAIS140 = true,
                isPanicActive = false
            ),
            LiveBusEntity(
                busNumber = "KA 01 F 9012",
                routeNumber = "KA-FLY-01",
                currentLatitude = 12.6500,
                currentLongitude = 77.1000,
                currentSpeed = 70.0,
                totalSeats = 45,
                occupiedSeats = 25,
                availableSeats = 20,
                driverName = "S. Suresh Gowda",
                driverPhone = "9880112233",
                busStatus = "ON_TIME",
                delayMinutes = 0,
                lastUpdated = System.currentTimeMillis(),
                hasAIS140 = true,
                isPanicActive = false
            ),
            LiveBusEntity(
                busNumber = "MH 12 RN 4501",
                routeNumber = "MH-SHIV-01",
                currentLatitude = 18.7500,
                currentLongitude = 73.4000,
                currentSpeed = 68.5,
                totalSeats = 42,
                occupiedSeats = 30,
                availableSeats = 12,
                driverName = "Ganesh Patil",
                driverPhone = "9822004411",
                busStatus = "ON_TIME",
                delayMinutes = 0,
                lastUpdated = System.currentTimeMillis(),
                hasAIS140 = true,
                isPanicActive = false
            ),
            LiveBusEntity(
                busNumber = "UP 32 CZ 1102",
                routeNumber = "UP-JAN-12",
                currentLatitude = 25.8000,
                currentLongitude = 81.8000,
                currentSpeed = 58.0,
                totalSeats = 48,
                occupiedSeats = 32,
                availableSeats = 16,
                driverName = "Ramakant Yadav",
                driverPhone = "9415008822",
                busStatus = "ON_TIME",
                delayMinutes = 0,
                lastUpdated = System.currentTimeMillis(),
                hasAIS140 = true,
                isPanicActive = false
            ),
            LiveBusEntity(
                busNumber = "DL 1P C 8812",
                routeNumber = "DL-DTC-534",
                currentLatitude = 28.5500,
                currentLongitude = 77.1500,
                currentSpeed = 32.0,
                totalSeats = 40,
                occupiedSeats = 22,
                availableSeats = 18,
                driverName = "Rajesh Sharma",
                driverPhone = "9811003344",
                busStatus = "ON_TIME",
                delayMinutes = 0,
                lastUpdated = System.currentTimeMillis(),
                hasAIS140 = true,
                isPanicActive = false
            )
        )
        db.liveBusDao().insertLiveBuses(liveBuses)
    }
}
