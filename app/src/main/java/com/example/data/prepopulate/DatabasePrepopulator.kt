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
            StateEntity("DL", "Delhi", "டெல்லி", "दिल्ली", "DTC / DIMTS", "https://dtc.delhi.gov.in", "011-23386084"),
            StateEntity("GA", "Goa", "கோவா", "गोवा", "KTC", "https://ktclgoa.com", "0832-2224188")
        )
        db.stateDao().insertStates(states)

        // 2. TAMIL NADU DISTRICTS & MAJOR CITIES
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
            DistrictEntity("TN_KKI", "Kanyakumari", "கன்னியாகுமரி", "कन्याकुमारी", "TN", 8.0883, 77.5385),
            DistrictEntity("TN_NIL", "The Nilgiris", "நீலகிரி", "नीलगिरी", "TN", 11.4916, 76.7337)
        )
        db.districtDao().insertDistricts(tnDistricts)

        // 3. AREAS / CITIES
        val allAreas = listOf(
            AreaEntity("DGL_01", "Dindigul Central", "திண்டுக்கல் மையம்", "दिंडीगुल सेंट्रल", "TN_DGL", "624001", false, 10.3673, 77.9803),
            AreaEntity("DGL_02", "Palani", "பழனி", "पलानी", "TN_DGL", "624601", false, 10.4500, 77.5200),
            AreaEntity("DGL_03", "Kodaikanal", "கொடைக்கானல்", "कोडाइकनाल", "TN_DGL", "624101", false, 10.2381, 77.4892),
            AreaEntity("DGL_04", "Batlagundu", "வத்தலகுண்டு", "बाटलागुंडू", "TN_DGL", "624202", false, 10.1500, 77.7500),
            AreaEntity("DGL_05", "Natham", "நத்தம்", "நத்தம்", "TN_DGL", "624401", true, 10.2333, 78.2333),
            AreaEntity("CHE_01", "Chennai Koyambedu", "சென்னை கோயம்பேடு", "चेन्नई कोयम्बेडु", "TN_CHE", "600107", false, 13.0694, 80.1948),
            AreaEntity("CHE_02", "Chennai Tambaram", "சென்னை தாம்பரம்", "चेन्नई ताम्बरम", "TN_CHE", "600045", false, 12.9249, 80.1000),
            AreaEntity("CBE_01", "Coimbatore Gandhipuram", "கோயம்புத்தூர் காந்திபுரம்", "कोयंबटूर गांधीपुरम", "TN_CBE", "641012", false, 11.0183, 76.9644),
            AreaEntity("MDU_01", "Madurai Mattuthavani", "மதுரை மாட்டுத்தாவணி", "मदुरै माटुथवानी", "TN_MDU", "625007", false, 9.9485, 78.1560),
            AreaEntity("TPJ_01", "Trichy Central Stand", "திருச்சி மத்திய பேருந்து நிலையம்", "तिरुचि सेंट्रल", "TN_TPJ", "620001", false, 10.7981, 78.6872),
            AreaEntity("SAL_01", "Salem New Bus Stand", "சேலம் புதிய பேருந்து நிலையம்", "सेलम नया बस स्टैंड", "TN_SAL", "636004", false, 11.6683, 78.1400),
            AreaEntity("BLR_01", "Bengaluru Majestic", "பெங்களூரு மெஜஸ்டிக்", "बेंगलुरु मैजेस्टिक", "KA", "560009", false, 12.9767, 77.5713),
            AreaEntity("HYD_01", "Hyderabad MGBS", "ஹைதராபாத் MGBS", "हैदराबाद एमजीबीएस", "TG", "500001", false, 17.3783, 78.4811),
            AreaEntity("TVM_01", "Thiruvananthapuram Central", "திருவனந்தபுரம்", "तिरुवनंतपुरम", "KL", "695001", false, 8.4882, 76.9520),
            AreaEntity("MUM_01", "Mumbai Dadar", "மும்பை தாதர்", "मुंबई दादर", "MH", "400014", false, 19.0178, 72.8478)
        )
        db.areaDao().insertAreas(allAreas)

        // 4. BUS ROUTES (Inter-city + Local)
        val sampleRoutes = listOf(
            // Inter-City Bookable Routes
            BusRouteEntity(
                routeNumber = "SETC-101",
                sourceArea = "Chennai Koyambedu",
                destinationArea = "Madurai Mattuthavani",
                stateCode = "TN",
                corporation = "SETC Tamil Nadu",
                totalDistance = 460,
                journeyDuration = 480,
                fareAmount = 450.0,
                busType = "ULTRA DELUXE AC",
                frequency = "Every 30 min",
                operatingDays = "MON-SUN",
                firstBusTime = "06:00 AM",
                lastBusTime = "11:30 PM",
                viaStops = "Villupuram, Trichy",
                isBookable = true,
                isACBus = true,
                isSleeper = false
            ),
            BusRouteEntity(
                routeNumber = "SETC-108",
                sourceArea = "Chennai Koyambedu",
                destinationArea = "Coimbatore Gandhipuram",
                stateCode = "TN",
                corporation = "SETC Tamil Nadu",
                totalDistance = 510,
                journeyDuration = 540,
                fareAmount = 520.0,
                busType = "AC SLEEPER",
                frequency = "Every 1 hr",
                operatingDays = "MON-SUN",
                firstBusTime = "07:00 AM",
                lastBusTime = "11:00 PM",
                viaStops = "Vellore, Salem, Erode",
                isBookable = true,
                isACBus = true,
                isSleeper = true
            ),
            BusRouteEntity(
                routeNumber = "TNSTC-302",
                sourceArea = "Coimbatore Gandhipuram",
                destinationArea = "Madurai Mattuthavani",
                stateCode = "TN",
                corporation = "TNSTC Coimbatore",
                totalDistance = 215,
                journeyDuration = 240,
                fareAmount = 180.0,
                busType = "EXPRESS",
                frequency = "Every 20 min",
                operatingDays = "MON-SUN",
                firstBusTime = "05:00 AM",
                lastBusTime = "10:00 PM",
                viaStops = "Tiruppur, Dindigul",
                isBookable = true,
                isACBus = false,
                isSleeper = false
            ),
            BusRouteEntity(
                routeNumber = "KSRTC-501",
                sourceArea = "Bengaluru Majestic",
                destinationArea = "Chennai Koyambedu",
                stateCode = "KA",
                corporation = "KSRTC FlyBus / Airavat",
                totalDistance = 350,
                journeyDuration = 360,
                fareAmount = 650.0,
                busType = "VOLVO MULTI-AXLE AC",
                frequency = "Every 45 min",
                operatingDays = "MON-SUN",
                firstBusTime = "05:30 AM",
                lastBusTime = "11:45 PM",
                viaStops = "Hosur, Krishnagiri, Vellore",
                isBookable = true,
                isACBus = true,
                isSleeper = false
            ),
            BusRouteEntity(
                routeNumber = "SETC-220",
                sourceArea = "Madurai Mattuthavani",
                destinationArea = "Bengaluru Majestic",
                stateCode = "TN",
                corporation = "SETC Express",
                totalDistance = 430,
                journeyDuration = 450,
                fareAmount = 580.0,
                busType = "NON-AC SLEEPER",
                frequency = "Every 2 hrs",
                operatingDays = "MON-SUN",
                firstBusTime = "08:00 AM",
                lastBusTime = "10:30 PM",
                viaStops = "Dindigul, Salem, Hosur",
                isBookable = true,
                isACBus = false,
                isSleeper = true
            ),
            // Local & Regional Routes
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
                viaStops = "Reddiyarchatram, Oddanchatram, Chatrapatti",
                isBookable = true,
                isACBus = false,
                isSleeper = false
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
                viaStops = "Batlagundu, Lower Camp, Ghat Road",
                isBookable = true,
                isACBus = false,
                isSleeper = false
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
                viaStops = "Shanarpatti, Kosavapatti",
                isBookable = false,
                isACBus = false,
                isSleeper = false
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
                busNumber = "TN 01 AN 8892",
                routeNumber = "SETC-101",
                currentLatitude = 11.9400,
                currentLongitude = 79.4800,
                currentSpeed = 62.0,
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
            )
        )
        db.liveBusDao().insertLiveBuses(sampleLiveBuses)
    }
}
