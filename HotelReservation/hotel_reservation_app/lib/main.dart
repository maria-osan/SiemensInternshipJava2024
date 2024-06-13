import 'package:flutter/material.dart';
import 'hotel_list.dart';
import 'package:geolocator/geolocator.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Hotel Reservation',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key});

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  late double latitude, longitude, radius;
  List<dynamic> hotels = [];

  String location = '';

  void handleSearch() async {
    // Get the user's current location
    Position position = await Geolocator.getCurrentPosition(
      desiredAccuracy: LocationAccuracy.high,
    );

    // Update 'location' with the user's latitude and longitude
    setState(() {
      latitude = position.latitude;
      longitude = position.longitude;

      location = 'Latitude: ${position.latitude}, Longitude: ${position.longitude}';
    });

    // Make sure location and radius are not empty
    if (latitude != null && longitude != null && radius != null) {
      // Make HTTP GET request to your backend API
      final url = 'http://localhost:8080/hotels?latitude=$latitude&longitude=$longitude&radius=$radius';
      final response = await http.get(Uri.parse(url));

      // Check if the request was successful
      if (response.statusCode == 200) {
        print(response.body);

        // Decode the JSON response
        final List<dynamic> jsonData = jsonDecode(response.body);

        // Update the 'hotels' state with the response
        setState(() {
          hotels = jsonData;
        });
      } else {
        // Handle error
        print('Error: ${response.statusCode}');
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: const Text(
            'Hotel Reservation Management',
          textAlign: TextAlign.center,
        ),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(location), // Display the current location
            TextField(
              onChanged: (value) {
                setState(() {
                  radius = double.parse(value);
                });
              },
              decoration: const InputDecoration(
                hintText: 'Enter radius in kilometers',
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: ElevatedButton(
                onPressed: handleSearch,
                child: const Text('Search'),
              ),
            ),
            HotelList(hotels: hotels),
          ],
        ),
      ),
    );
  }
}
