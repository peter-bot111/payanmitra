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
        // 1. STATES & UTs (28 States + 8 UTs)
        val states = listOf(
            StateEntity("TN", "Tamil Nadu", "தமிழ்நாடு", "तमिलनाडु", "TNSTC / MTC / SETC", "https://tnstc.in", "18002581071"),
            StateEntity("KA", "Karnataka", "கர்நாடகா", "कर्नाटक", "KSRTC / BMTC / NWKRTC", "https://ksrtc.karnataka.gov.in", "08022212121"),
            StateEntity("KL", "Kerala", "கேரளா", "केरल", "KSRTC Kerala", "https://online.keralartc.com", "18001231321"),
            StateEntity("MH", "Maharashtra", "மகாராஷ்டிரா", "महाराष्ट्र", "MSRTC", "https://msrtc.in", "18002330095"),
            StateEntity("UP", "Uttar Pradesh", "உத்தரப் பிரதேசம்", "उत्तर प्रदेश", "UPSRTC", "https://upsrtc.up.gov.in", "18001802877"),
            StateEntity("GJ", "Gujarat", "குஜராத்", "गुजरात", "GSRTC", "https://gsrtc.in", "18002331071"),
            StateEntity("TG", "Telangana", "தெலங்கானா", "तेलंगाना", "TSRTC", "https://tsrtconline.in", "040-69440000"),
            StateEntity("AP", "Andhra Pradesh", "ஆந்திரப் பிரதேசம்", "आंध्र प्रदेश", "APSRTC", "https://apsrtconline.in", "0866-2570005"),
            StateEntity("RJ", "Rajasthan", "ராஜஸ்தான்", "राजस्थान", "RSRTC", "https://rsrtconline.rajasthan.gov.in", "0141-5110011"),
            StateEntity("MP", "Madhya Pradesh", "மத்தியப் பிரதேசம்", "मध्य प्रदेश", "MPSRTC", "https://mpsrtc.in", "0755-2553880"),
            StateEntity("OD", "Odisha", "ஒடிசா", "ओडिशा", "OSRTC", "https://osrtc.in", "0674-2394949"),
            StateEntity("WB", "West Bengal", "மேற்கு வங்காளம்", "पश्चिम बंगाल", "NBSTC / CSTC / WBTC", "https://wbtc.co.in", "033-22483214"),
            StateEntity("AS", "Assam", "அசாம்", "असम", "ASTC", "https://astc.assam.gov.in", "0361-2547226"),
            StateEntity("PB", "Punjab", "பஞ்சாப்", "पंजाब", "PRTC", "https://punjabroadways.gov.in", "0175-2263830"),
            StateEntity("HR", "Haryana", "ஹரியானா", "हरियाणा", "Haryana Roadways", "https://hartrans.gov.in", "01662-274300"),
            StateEntity("BR", "Bihar", "பீகார்", "बिहार", "BSRTC", "https://bsrtc.bihar.gov.in", "0612-2221150"),
            StateEntity("JH", "Jharkhand", "ஜார்ஜண்ட்", "झारखंड", "JSTC", "https://jharkhand.gov.in", "0651-2210230"),
            StateEntity("CG", "Chhattisgarh", "சத்தீஸ்கர்", "छत्तीसगढ़", "CSRTC", "https://cg.gov.in", "0771-2236890"),
            StateEntity("HP", "Himachal Pradesh", "இமாச்சலப் பிரதேசம்", "हिमाचल प्रदेश", "HRTC", "https://hrtchp.com", "0177-2652942"),
            StateEntity("UK", "Uttarakhand", "உத்தரகாண்ட்", "उत्तराखंड", "UTC", "https://utconline.uk.gov.in", "0135-2658753"),
            StateEntity("GA", "Goa", "கோவா", "गोवा", "KTC", "https://ktclgoa.com", "0832-2224188"),
            StateEntity("MN", "Manipur", "மணிப்பூர்", "मणिपुर", "MSRTC Manipur", "https://manipur.gov.in", "0385-2221025"),
            StateEntity("ML", "Meghalaya", "மேகலாயா", "मेघालय", "MST", "https://meghalaya.gov.in", "0364-2221015"),
            StateEntity("SK", "Sikkim", "சிக்கிம்", "सिक्किम", "SNT", "https://sikkim.gov.in", "03592-202072"),
            StateEntity("TR", "Tripura", "திரிபுரா", "त्रिपुरा", "TRTC", "https://tripura.gov.in", "0381-2326780"),
            StateEntity("MZ", "Mizoram", "மிசோரம்", "मिज़ोरम", "MZST", "https://mizoram.gov.in", "0389-2322471"),
            StateEntity("NL", "Nagaland", "நாகாலாந்து", "नागालैंड", "NeST", "https://nagaland.gov.in", "0370-2291434"),
            StateEntity("AR", "Arunachal Pradesh", "அருணாச்சலப் பிரதேசம்", "अरुणाचल प्रदेश", "APST", "https://arunachalpradesh.gov.in", "0360-2212230"),
            StateEntity("DL", "Delhi", "டெல்லி", "दिल्ली", "DTC / DIMTS", "https://dtc.delhi.gov.in", "011-23386084"),
            StateEntity("PY", "Puducherry", "புதுச்சேரி", "पुडुचेरी", "PTC", "https://py.gov.in", "0413-2222055"),
            StateEntity("CH", "Chandigarh", "சண்டிகர்", "चंडीगढ़", "CTU", "https://chdctu.gov.in", "0172-2700022"),
            StateEntity("JK", "J&K", "ஜம்மு காஷ்மீர்", "जम्मू और कश्मीर", "JKRTC", "https://jkrtc.co.in", "0191-2478430")
        )
        db.stateDao().insertStates(states)

        // 2. TAMIL NADU DISTRICTS (38 Districts)
        val tnDistricts = listOf(
            DistrictEntity("TN_CHE", "Chennai", "சென்னை", "चेन्नई", "TN", 13.0827, 80.2707),
            DistrictEntity("TN_CBE", "Coimbatore", "கோயம்புத்தூர்", "कोयंबटूर", "TN", 11.0168, 76.9558),
            DistrictEntity("TN_MDU", "Madurai", "மதுரை", "मदुरै", "TN", 9.9252, 78.1198),
            DistrictEntity("TN_TPJ", "Tiruchirappalli", "திருச்சிராப்பள்ளி", "तिरुचिरापल्ली", "TN", 10.7905, 78.7047),
            DistrictEntity("TN_SAL", "Salem", "சேலம்", "सेलम", "TN", 11.6643, 78.1460),
            DistrictEntity("TN_TNV", "Tirunelveli", "திருநெல்வேலி", "तिरुनेलवेली", "TN", 8.7139, 77.7567),
            DistrictEntity("TN_VEL", "Vellore", "வேலூர்", "वेल्लोर", "TN", 12.9165, 79.1325),
            DistrictEntity("TN_ERD", "Erode", "ஈரோடு", "ईरोड", "TN", 11.3410, 77.7172),
            DistrictEntity("TN_TPR", "Tiruppur", "திருப்பூர்", "तिरुपुर", "TN", 11.1085, 77.3411),
            DistrictEntity("TN_TUT", "Thoothukudi", "தூத்துக்குடி", "थूथुकुडी", "TN", 8.7642, 78.1348),
            DistrictEntity("TN_KAN", "Kancheepuram", "காஞ்சிபுரம்", "कांचीपुरम", "TN", 12.8342, 79.7036),
            DistrictEntity("TN_TNJ", "Thanjavur", "தஞ்சாவூர்", "तंजौर", "TN", 10.7870, 79.1378),
            DistrictEntity("TN_DGL", "Dindigul", "திண்டுக்கல்", "दिंडीगुल", "TN", 10.3673, 77.9803),
            DistrictEntity("TN_CUD", "Cuddalore", "கடலூர்", "कुड्डालोर", "TN", 11.7480, 79.7714),
            DistrictEntity("TN_VLP", "Villupuram", "விழுப்புரம்", "विल्लुपुरम", "TN", 11.9401, 79.4861),
            DistrictEntity("TN_RPT", "Ranipet", "ராணிப்பேட்டை", "रानीपेट", "TN", 12.9224, 79.3328),
            DistrictEntity("TN_KRI", "Krishnagiri", "கிருஷ்ணகிரி", "कृष्णागिरी", "TN", 12.5266, 78.2146),
            DistrictEntity("TN_DHA", "Dharmapuri", "தர்மபுரி", "धर्मपुरी", "TN", 12.1211, 78.1582),
            DistrictEntity("TN_NMK", "Namakkal", "நாமக்கல்", "नामक्कल", "TN", 11.2189, 78.1674),
            DistrictEntity("TN_ARI", "Ariyalur", "அரியலூர்", "अरियालुर", "TN", 11.1401, 79.0782),
            DistrictEntity("TN_PER", "Perambalur", "பெரம்பலூர்", "पेरम्बलूर", "TN", 11.2342, 78.8820),
            DistrictEntity("TN_TVR", "Tiruvarur", "திருவாரூர்", "तिरुवारूर", "TN", 10.7726, 79.6365),
            DistrictEntity("TN_NGP", "Nagapattinam", "நாகப்பட்டினம்", "नागपट्टिनम", "TN", 10.7672, 79.8449),
            DistrictEntity("TN_PDK", "Pudukkottai", "புதுக்கோட்டை", "पुदुक्कोट्टई", "TN", 10.3797, 78.8208),
            DistrictEntity("TN_SVG", "Sivaganga", "சிவகங்கை", "शिवगंगा", "TN", 9.8433, 78.4809),
            DistrictEntity("TN_VNR", "Virudhunagar", "விருதுநகர்", "विरुद्धनगर", "TN", 9.5680, 77.9624),
            DistrictEntity("TN_RMD", "Ramanathapuram", "ராமநாதபுரம்", "रामनाथपुरम", "TN", 9.3639, 78.8395),
            DistrictEntity("TN_TKS", "Tenkasi", "தென்காசி", "तेनकासी", "TN", 8.9593, 77.3149),
            DistrictEntity("TN_KKI", "Kanyakumari", "கன்னியாகுமரி", "कन्याकुमारी", "TN", 8.0883, 77.5385),
            DistrictEntity("TN_NIL", "The Nilgiris", "நீலகிரி", "नीलगिरी", "TN", 11.4916, 76.7337),
            DistrictEntity("TN_TPT", "Tirupattur", "திருப்பத்தூர்", "तिरुमत्तूर", "TN", 12.4926, 78.5678),
            DistrictEntity("TN_CGP", "Chengalpattu", "செங்கல்பட்டு", "चेंगलपट्टू", "TN", 12.6922, 79.9774),
            DistrictEntity("TN_KLK", "Kallakurichi", "கள்ளக்குறிச்சி", "कल्लाकुरिची", "TN", 11.7384, 78.9639),
            DistrictEntity("TN_MYD", "Mayiladuthurai", "மயிலாடுதுறை", "मयीलादुथुरई", "TN", 11.1018, 79.6521),
            DistrictEntity("TN_TVM", "Tiruvannamalai", "திருவண்ணாமலை", "तिरुवन्नमलाई", "TN", 12.2253, 79.0747),
            DistrictEntity("TN_KRR", "Karur", "கரூர்", "करूर", "TN", 10.9601, 78.0766),
            DistrictEntity("TN_TNI", "Theni", "தேனி", "थेनी", "TN", 10.0104, 77.4768)
        )
        db.districtDao().insertDistricts(tnDistricts)

        // 3. DINDIGUL DISTRICT AREAS/TALUKS
        val dindigulAreas = listOf(
            AreaEntity("DGL_01", "Dindigul Central", "திண்டுக்கல் மையம்", "दिंडीगुल सेंट्रल", "TN_DGL", "624001", false, 10.3673, 77.9803),
            AreaEntity("DGL_02", "Palani", "பழனி", "पलानी", "TN_DGL", "624601", false, 10.4500, 77.5200),
            AreaEntity("DGL_03", "Kodaikanal", "கொடைக்கானல்", "कोडाइकनाल", "TN_DGL", "624101", false, 10.2381, 77.4892),
            AreaEntity("DGL_04", "Batlagundu", "வத்தலகுண்டு", "बाटलागुंडू", "TN_DGL", "624202", false, 10.1500, 77.7500),
            AreaEntity("DGL_05", "Natham", "நத்தம்", "नथम", "TN_DGL", "624401", true, 10.2333, 78.2333),
            AreaEntity("DGL_06", "Vedasandur", "வேடசந்தூர்", "वेदासंदूर", "TN_DGL", "624710", true, 10.5333, 77.9500),
            AreaEntity("DGL_07", "Oddanchatram", "ஒட்டன்சத்திரம்", "ओडनचत्रम", "TN_DGL", "624619", false, 10.4833, 77.7500),
            AreaEntity("DGL_08", "Nilakottai", "நிலக்கோட்டை", "निलाकोट्टई", "TN_DGL", "624208", true, 10.1667, 77.8667),
            AreaEntity("DGL_09", "Athoor", "ஆத்தூர்", "आथूर", "TN_DGL", "624701", true, 10.2833, 77.8500),
            AreaEntity("DGL_10", "Shanarpatti", "சாணார்பட்டி", "शन्नारपट्टी", "TN_DGL", "624304", true, 10.3167, 78.1000),
            AreaEntity("DGL_11", "Sirunaickenpalayam", "சிறுநாயக்கன்பாளையம்", "सिरुनायक्कनपालयम", "TN_DGL", "624004", true, 10.3800, 78.0100),
            AreaEntity("DGL_12", "Vadamadurai", "வடமதுரை", "वडमदुरै", "TN_DGL", "624802", true, 10.4667, 78.0833),
            AreaEntity("DGL_13", "Gujiliamparai", "குஜிலியம்பாறை", "गुजिलियमपारई", "TN_DGL", "624703", true, 10.7000, 78.1167),
            AreaEntity("DGL_14", "Reddiyarchatram", "ரெட்டியார்சத்திரம்", "रेड्डियारचत्रम", "TN_DGL", "624003", true, 10.4167, 77.9167),
            AreaEntity("DGL_15", "Ammayanayakanur", "அம்மையநாயக்கனூர்", "अम्मयानायकनूर", "TN_DGL", "624201", true, 10.1333, 77.9000),
            AreaEntity("DGL_16", "Thoppampatti", "தொப்பம்பட்டி", "तोप्पमपट्टी", "TN_DGL", "624617", true, 10.5500, 77.5833),
            AreaEntity("DGL_17", "Pattiveeranpatti", "பட்டிவீரன்பட்டி", "पट्टीवीरनपट्टी", "TN_DGL", "624211", true, 10.2000, 77.7667),
            AreaEntity("DGL_18", "Kannivadi", "கண்ணிவாடி", "कन्निवडी", "TN_DGL", "624705", true, 10.3500, 77.8333),
            AreaEntity("DGL_19", "Annamalai Nagar", "அண்ணாமலை நகர்", "अन्नामलाई नगर", "TN_DGL", "624005", false, 10.3600, 77.9700)
        )
        db.areaDao().insertAreas(dindigulAreas)

        // 4. BUS ROUTES
        val sampleRoutes = listOf(
            BusRouteEntity(
                routeNumber = "182",
                sourceArea = "Dindigul Central",
                destinationArea = "Palani",
                stateCode = "TN",
                corporation = "TNSTC Dindigul",
                totalDistance = 60,
                journeyDuration = 80,
                fareAmount = 45.0,
                busType = "EXPRESS",
                frequency = "Every 15 min",
                operatingDays = "MON-SUN",
                firstBusTime = "05:00 AM",
                lastBusTime = "10:30 PM",
                viaStops = "Reddiyarchatram, Oddanchatram, Chatrapatti"
            ),
            BusRouteEntity(
                routeNumber = "204B",
                sourceArea = "Dindigul Central",
                destinationArea = "Kodaikanal",
                stateCode = "TN",
                corporation = "TNSTC Dindigul",
                totalDistance = 95,
                journeyDuration = 150,
                fareAmount = 85.0,
                busType = "DELUXE",
                frequency = "Every 1 hr",
                operatingDays = "MON-SUN",
                firstBusTime = "06:00 AM",
                lastBusTime = "08:00 PM",
                viaStops = "Batlagundu, Lower Camp, Ghat Road"
            ),
            BusRouteEntity(
                routeNumber = "7A",
                sourceArea = "Dindigul Central",
                destinationArea = "Natham",
                stateCode = "TN",
                corporation = "TNSTC Town Bus",
                totalDistance = 35,
                journeyDuration = 50,
                fareAmount = 22.0,
                busType = "ORDINARY",
                frequency = "Every 20 min",
                operatingDays = "MON-SUN",
                firstBusTime = "05:30 AM",
                lastBusTime = "09:30 PM",
                viaStops = "Shanarpatti, Kosavapatti"
            ),
            BusRouteEntity(
                routeNumber = "L15",
                sourceArea = "Dindigul Central",
                destinationArea = "Batlagundu",
                stateCode = "TN",
                corporation = "TNSTC Ladies Special",
                totalDistance = 32,
                journeyDuration = 45,
                fareAmount = 20.0,
                busType = "ORDINARY",
                frequency = "Every 30 min",
                operatingDays = "MON-SAT",
                firstBusTime = "07:30 AM",
                lastBusTime = "06:30 PM",
                viaStops = "Nilakottai, Ammayanayakanur"
            )
        )
        db.busRouteDao().insertRoutes(sampleRoutes)

        // 5. BUS STOPS
        val sampleStops = listOf(
            BusStopEntity("STOP_01", "Dindigul Bus Stand", "திண்டுக்கல் பேருந்து நிலையம்", 10.3673, 77.9803, "DGL_01", true, true, true),
            BusStopEntity("STOP_02", "Reddiyarchatram Four Road", "ரெட்டியார்சத்திரம் நான்கு ரோடு", 10.4167, 77.9167, "DGL_14", true, false, false),
            BusStopEntity("STOP_03", "Oddanchatram Bye-pass", "ஒட்டன்சத்திரம் பைபாஸ்", 10.4833, 77.7500, "DGL_07", true, true, true),
            BusStopEntity("STOP_04", "Palani Temple Gate Bus Stop", "பழனி கோயில் வாசல் நிறுத்தம்", 10.4500, 77.5200, "DGL_02", true, true, true),
            BusStopEntity("STOP_05", "Batlagundu Main Stand", "வத்தலகுண்டு மெயின் ஸ்டாண்ட்", 10.1500, 77.7500, "DGL_04", true, true, false)
        )
        db.busStopDao().insertStops(sampleStops)

        // 6. LIVE BUSES
        val sampleLiveBuses = listOf(
            LiveBusEntity(
                busNumber = "TN 57 N 2184",
                routeNumber = "182",
                currentLatitude = 10.4000,
                currentLongitude = 77.9300,
                currentSpeed = 48.5,
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
                busNumber = "TN 57 N 1942",
                routeNumber = "204B",
                currentLatitude = 10.2000,
                currentLongitude = 77.6500,
                currentSpeed = 35.0,
                totalSeats = 45,
                occupiedSeats = 38,
                availableSeats = 7,
                driverName = "K. Raman",
                driverPhone = "9443209811",
                busStatus = "DELAYED",
                delayMinutes = 8,
                lastUpdated = System.currentTimeMillis(),
                hasAIS140 = true,
                isPanicActive = false
            )
        )
        db.liveBusDao().insertLiveBuses(sampleLiveBuses)
    }
}
