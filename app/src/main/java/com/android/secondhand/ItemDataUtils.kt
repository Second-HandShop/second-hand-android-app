package com.android.secondhand

import com.google.gson.Gson

class ItemDataUtils {

    companion object{

        fun getPosts(): List<Item>{
            val posts = """
        [
    {
    "name": "2009 Toyota Corolla",
    "price": 7800,
    "category": "Cars",
    "image_paths": [
    "/v1637982129/SecondHandShop/2009%20Toyota%20Corolla_1.jpg",
    "/v1637982124/SecondHandShop/2009%20Toyota%20Corolla_2.jpg",
    "/v1637982117/SecondHandShop/2009%20Toyota%20Corolla_3.jpg"
    ],
    "description": "Mileage: 124400. Newly replaced tires, window wipe, 2 brake pads, resurace front rotors, clean rear brake, motor oil, oil filter, oil, etc. Clean title."
    },
    {
    "name": "Desk Lamp",
    "price": 10,
    "category": "Household",
    "image_paths": [
    "v1637982241/SecondHandShop/Desk%20Lamp.jpg"
    ],
    "description": "In good condition."
    },
    {
    "name": "Organizers",
    "price": 10,
    "category": "Household",
    "image_paths": [
    "/v1637982270/SecondHandShop/Organizer.jpg"
    ],
    "description": "${'$'}10/each"
    },
    {
    "name": "Coffee Maker",
    "price": 10,
    "category": "Household",
    "image_paths": [
    "/v1637982311/SecondHandShop/Coffee%20Maker.jpg"
    ],
    "description": "Almost new."
    },
    {
    "name": "Makeup Organizer",
    "price": 15,
    "category": "Household",
    "image_paths": [
    "/v1637982364/SecondHandShop/Makeup%20Organizer.jpg"
    ],
    "description": "DreamGenius Makeup Organizer 3 Pieces Acrylic Cosmetic Storage Drawers and Jewelry Display Box Transparent-A"
    },
    {
    "name": "Rolling Clothing Rack",
    "price": 5,
    "category": "Household",
    "image_paths": [
    "/v1637982391/SecondHandShop/Rolling%20Clothing%20Rack_1.jpg",
    "/v1637982414/SecondHandShop/Rolling%20Clothing%20Rack_2.jpg"
    ],
    "description": "3 staged rolling rack. Up to 53 inch high, 33 inch wide"
    },
    {
    "name": "Utility Cart",
    "price": 10,
    "category": "Household",
    "image_paths": [
    "/v1637982460/SecondHandShop/Utility%20Cart.jpg"
    ],
    "description": "Bought from IKEA"
    },
    {
    "name": "Trash Bins",
    "price": 1,
    "category": "Household",
    "image_paths": [
    "/v1637982479/SecondHandShop/Trash%20Bins.jpg"
    ],
    "description": "${'$'}1 for small one and ${'$'}3 for big one."
    },
    {
    "name": "Cooking pot",
    "price": 10,
    "category": "Household",
    "image_paths": [
    "/v1637982505/SecondHandShop/Cooking%20Pot.jpg"
    ],
    "description": "8.5 inch diameter, 5 inch depth."
    },
    {
    "name": "Heater",
    "price": 50,
    "category": "Household",
    "image_paths": [
    "/v1637982531/SecondHandShop/Heater.jpg"
    ],
    "description": "Like new."
    },
    {
    "name": "Seville Classic Fan",
    "price": 25,
    "category": "Household",
    "image_paths": [
    "/v1637982578/SecondHandShop/Seville%20Classic%20Fan_1.jpg",
    "/v1637982604/SecondHandShop/Seville%20Classic%20Fan_2.jpg",
    "/v1637982628/SecondHandShop/Seville%20Classic%20Fan_3.jpg"
    ],
    "description": "40-inch height. Remote included."
    },
    {
    "name": "65-inch LG TV & stand",
    "price": 800,
    "category": "Furniture",
    "image_paths": [
    "/v1637982748/SecondHandShop/65%20LG%20TV%20and%20stand.jpg"
    ],
    "description": "LG TV - ${'$'}800. Original price is ${'$'}1700. TV stand - ${'$'}70."
    },
    {
    "name": "Loveseat and coffee table",
    "price": 80,
    "category": "Furniture",
    "image_paths": [
    "/v1637982795/SecondHandShop/Loveseat%20and%20coffee%20table.jpg"
    ],
    "description": "Loveseat - ${'$'}80. Coffee table - ${'$'}20"
    },
    {
    "name": "Three drawer dresser × 2",
    "price": 40,
    "category": "Furniture",
    "image_paths": [
    "/v1637982839/SecondHandShop/Three%20drawer%20dresser%20%C3%97%202.jpg"
    ],
    "description": "${'$'}40 for two."
    },
    {
    "name": "55-inch Sharp TV & stand",
    "price": 150,
    "category": "Furniture",
    "image_paths": [
    "/v1637982862/SecondHandShop/55%20Sharp%20TV%20and%20stand.jpg"
    ],
    "description": "TV - ${'$'}150. Stand - ${'$'}30."
    },
    {
    "name": "Nightstand × 2",
    "price": 60,
    "category": "Furniture",
    "image_paths": [
    "/v1637982883/SecondHandShop/Nightstand%20%C3%97%202.jpg"
    ],
    "description": "${'$'}60 for two"
    },
    {
    "name": "Solid wood queen size bed",
    "price": 300,
    "category": "Furniture",
    "image_paths": [
    "/v1637982902/SecondHandShop/Solid%20wood%20queen%20size%20bed_1.jpg",
    "/v1637982916/SecondHandShop/Solid%20wood%20queen%20size%20bed_2.jpg"
    ],
    "description": "Original price is ${'$'}700."
    },
    {
    "name": "File organizer",
    "price": 10,
    "category": "Books & Supplies",
    "image_paths": [
    "/v1637982977/SecondHandShop/File%20organizer.jpg"
    ],
    "description": "IKEA item catalog:21919"
    },
    {
    "name": "IdeaPad P400 touchscreen laptop",
    "price": 120,
    "category": "Electronics",
    "image_paths": [
    "/v1637983047/SecondHandShop/IdeaPad%20P400%20touchscreen%20laptop_1.jpg",
    "/v1637983064/SecondHandShop/IdeaPad%20P400%20touchscreen%20laptop_2.jpg",
    "/v1637983088/SecondHandShop/IdeaPad%20P400%20touchscreen%20laptop_3.jpg"
    ],
    "description": "In good condition."
    },
    {
    "name": "Nintendo Switch Lite",
    "price": 80,
    "category": "Electronics",
    "image_paths": [
    "/v1637983113/SecondHandShop/Nintendo%20Switch%20Lite.jpg"
    ],
    "description": "Purchased less than one year ago. Cover included."
    },
    {
    "name": "JBL Horizon Bluetooth Clock",
    "price": 35,
    "category": "Electronics",
    "image_paths": [
    "/v1637983133/SecondHandShop/JBL%20Horizon%20Bluetooth%20Clock_1.jpg",
    "/v1637983159/SecondHandShop/JBL%20Horizon%20Bluetooth%20Clock_2.jpg",
    "/v1637983173/SecondHandShop/JBL%20Horizon%20Bluetooth%20Clock_3.jpg"
    ],
    "description": "With USB Charging and Ambient Light, Black"
    },
    {
    "name": "Razer BlackWidow Gaming Keyboard",
    "price": 46,
    "category": "Electronics",
    "image_paths": [
    "/v1637983194/SecondHandShop/Razer%20BlackWidow%20Gaming%20Keyboard_1.jpg",
    "/v1637983214/SecondHandShop/Razer%20BlackWidow%20Gaming%20Keyboard_2.jpg",
    "/v1637983229/SecondHandShop/Razer%20BlackWidow%20Gaming%20Keyboard_3.jpg",
    "/v1637983246/SecondHandShop/Razer%20BlackWidow%20Gaming%20Keyboard_4.jpg"
    ],
    "description": "Razer BlackWidow Essential Mechanical Gaming Keyboard: Green Mechanical Switches - Tactile & Clicky - Individual Key Green LED backlighting - 10 Key Anti-Ghosting - Programmable Macro Functionality. In good condition."
    },
    {
    "name": "Samsung UR55 28in 4K UHD monitor",
    "price": 250,
    "category": "Electronics",
    "image_paths": [
    "/v1637983279/SecondHandShop/Samsung%20UR55%2028%22%204K%20UHD%20monitor.jpg"
    ],
    "description": "Used for less than 1 year. Excellent condition. No scratch, no dead pixel. Model: LU28R55OUQNXZA. Currently it is attached to a desk mount, but can be put back to its original fixed stand. Mounting arm is for free. Comes with original packaging and cables"
    },
    {
    "name": "Acer 28-inch 60hz IPS 4K monitor",
    "price": 200,
    "category": "Electronics",
    "image_paths": [
    "/v1637983303/SecondHandShop/Acer%2028%22%2060hz%20IPS%204K%20monitor_1.jpg",
    "/v1637983323/SecondHandShop/Acer%2028%22%2060hz%20IPS%204K%20monitor_2.jpg"
    ],
    "description": "Model: CB28K smiiprx. Feature: with speaker and adjustable stand. List price: ${'$'}320 before tax. Used for a year. Excellent condition. No dead pixel, no scratch. Packed in original box without missing parts. "
    },
    {
    "name": "Huawei P30 256G",
    "price": 300,
    "category": "Electronics",
    "image_paths": [
    "/v1637983345/SecondHandShop/Huawei%20P30%20256G_1.jpg",
    "/v1637983366/SecondHandShop/Huawei%20P30%20256G_2.jpg",
    "/v1637983410/SecondHandShop/Huawei%20P30%20256G_3.jpg",
    "/v1637983387/SecondHandShop/Huawei%20P30%20256G_4.jpg",
    "/v1637983425/SecondHandShop/Huawei%20P30%20256G_5.jpg"
    ],
    "description": "There are some small cracks on the screen, but it doesn't affect use at all."
    },
    {
    "name": "Nintendo Switch",
    "price": 250,
    "category": "Electronics",
    "image_paths": [
    "/v1637983495/SecondHandShop/Nintendo%20Switch.jpg"
    ],
    "description": "In great condition. Includes The Legend of Zelda."
    },
    {
    "name": "2018 Toyota Camry L",
    "price": 22000,
    "category": "Cars",
    "image_paths": [
    "/v1637983573/SecondHandShop/2018%20Toyota%20Camry%20L.jpg"
    ],
    "description": "Silver Toyota Camry L, 25600 miles. I am the first owner. It is well maintained. No accidents. Clean title."
    },
    {
    "name": "2010 Honda Accord LX-P",
    "price": 6700,
    "category": "Cars",
    "image_paths": [
    "/v1637983611/SecondHandShop/2010%20Honda%20Accord%20LX-P_1.jpg",
    "/v1637983626/SecondHandShop/2010%20Honda%20Accord%20LX-P_2.jpg",
    "/v1637983640/SecondHandShop/2010%20Honda%20Accord%20LX-P_3.jpg",
    "/v1637983653/SecondHandShop/2010%20Honda%20Accord%20LX-P_4.jpg"
    ],
    "description": "Mileage - 127K miles. Clean titie. No accidents."
    },
    {
    "name": "2014 Ford Focus",
    "price": 10000,
    "category": "Cars",
    "image_paths": [
    "/v1637983708/SecondHandShop/2014%20Ford%20Focus_1.jpg",
    "/v1637983722/SecondHandShop/2014%20Ford%20Focus_2.jpg",
    "/v1637983738/SecondHandShop/2014%20Ford%20Focus_3.jpg",
    "/v1637983752/SecondHandShop/2014%20Ford%20Focus_4.jpg",
    "/v1637983766/SecondHandShop/2014%20Ford%20Focus_5.jpg",
    "/v1637983791/SecondHandShop/2014%20Ford%20Focus_6.jpg"
    ],
    "description": "Mileage: 52K"
    }
    ]
        """.trimIndent()

            // create Movie List from JSON String by using Gson!!
            return Gson().fromJson(posts, Array<Item>::class.java).asList()
        }
    }
}