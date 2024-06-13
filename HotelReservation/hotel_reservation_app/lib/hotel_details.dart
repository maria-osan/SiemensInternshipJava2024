import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:intl/intl.dart';

import 'booking_details.dart';

class HotelDetailsPage extends StatefulWidget {
  final int hotelId;

  const HotelDetailsPage({super.key, required this.hotelId});

  @override
  _HotelDetailsPageState createState() => _HotelDetailsPageState();
}

class _HotelDetailsPageState extends State<HotelDetailsPage> {
  List<Room> rooms = [];
  bool isLoading = true;
  late DateTime startDate;
  late DateTime endDate;

  TextEditingController feedbackController = TextEditingController();

  @override
  void initState() {
    super.initState();
    fetchRooms();

    // Set initial start and end dates to today and 7 days from today respectively
    startDate = DateTime.now();
    endDate = startDate.add(const Duration(days: 7));
  }

  Future<void> fetchRooms() async {
    final response = await http.get(Uri.parse('http://localhost:8080/hotels/${widget.hotelId}/rooms'));

    if (response.statusCode == 200) {
      setState(() {
        rooms = (jsonDecode(response.body) as List).map((roomJson) => Room.fromJson(roomJson)).toList();
        isLoading = false;
      });
    } else {
      print('Failed to load rooms: ${response.statusCode}');
    }
  }

  void bookSelectedRooms() async {
    // Prepare list of selected rooms
    List<Room> selectedRooms = rooms.where((room) => room.isSelected).toList();
    if (selectedRooms.isEmpty) {
      // No rooms selected, show error message or prompt user to select rooms
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
        content: Text('No rooms selected. Please select rooms to book.'),
      ));
      return;
    }

    // Calculate start and end dates (example: 7 days from today)
    //DateTime startDate = DateTime.now();
    //DateTime endDate = startDate.add(const Duration(days: 7));

    bool bookedSuccessfuly = false;

    for (Room room in selectedRooms) {
      // Send booking request to backend
      final response = await http.post(
        Uri.parse('http://localhost:8080/bookings'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({
          'roomNumber': room.roomNumber,
          'startDate': startDate.toIso8601String(),
          'endDate': endDate.toIso8601String(),
        }),
      );

      if (response.statusCode == 200) {
        bookedSuccessfuly = true;
        // Show confirmation message
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
        content: Text('Room(s) booked successfully!'),
        ));
      } else if(response.statusCode == 409) {
        // Handle conflict status
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(
          content: Text('Failed to book room ${room.roomNumber}: Booking dates overlap with an existing booking.'),
        ));
      } else if (response.statusCode == 400) {
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(
          content: Text('Failed to book room ${room.roomNumber}: The room is not available.'),
        ));
      } else {
        // Booking failed, show error message
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(
          content: Text('Failed to book room ${room.roomNumber}: ${response.body}'),
        ));
      }
    }

    if(bookedSuccessfuly) {
      // Booking successful, navigate to booking details page
      Navigator.push(
        context,
        MaterialPageRoute(
          builder: (context) => BookingDetailsPage(
            bookedRooms: selectedRooms,
            startDate: startDate,
            endDate: endDate,
          ),
        ),
      );
    }

    // Reset selected rooms list
    setState(() {
      for (Room room in selectedRooms) {
        room.isSelected = false;
      }
    });

    // Reload the list of rooms
    await fetchRooms();
  }

  void submitFeedback() async {
    String feedback = feedbackController.text;
    print('Feedback submitted: $feedback');

    // Define the URL of your backend API endpoint
    String apiUrl = 'http://localhost:8080/feedbacks';

    // Create a map for the request body
    Map<String, dynamic> requestBody = {
      'feedback': feedback,
      'hotelId': widget.hotelId.toString()
    };

    // Send the feedback to the backend API
    try {
      var response = await http.post(
        Uri.parse(apiUrl),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode(requestBody)
      );

      if (response.statusCode == 200) {
        // Clear the feedback text field after successful submission
        feedbackController.clear();

        // Show a confirmation message to the user
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text('Feedback submitted successfully!'),
          ),
        );
      } else {
        // Show an error message if the request was not successful
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text('Failed to submit feedback. Please try again later.'),
          ),
        );
      }
    } catch (e) {
      print('Error submitting feedback: $e');
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('An error occurred while submitting feedback.'),
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Available Rooms'),
      ),
      body: isLoading ? const Center(child: CircularProgressIndicator()) : Column(
        children: [
          Expanded(
            child: ListView.builder(
              itemCount: rooms.length,
              itemBuilder: (context, index) {
                final room = rooms[index];
                return ListTile(
                  title: Text('Room ${room.roomNumber}'),
                  subtitle: Text('${room.type} - \$${room.price}'),
                  trailing: room.available ? const Icon(Icons.check_circle) : const Icon(Icons.cancel),
                  onTap: () {
                    setState(() {
                      room.isSelected = !room.isSelected;
                    });
                  },
                );
              },
            ),
          ),
          Column(
            children: [
              const Text(
                'Book room(s):',
                style: TextStyle(
                  fontSize: 20,
                ),
                textAlign: TextAlign.left,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  // Start Date Picker
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: ElevatedButton(
                      onPressed: () => _selectDate(context, true),
                      child: Text('Select Start Date: ${DateFormat('yyyy-MM-dd').format(startDate)}'),
                    ),
                  ),
                  // End Date Picker
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: ElevatedButton(
                      onPressed: () => _selectDate(context, false),
                      child: Text('Select End Date: ${DateFormat('yyyy-MM-dd').format(endDate)}'),
                    ),
                  ),
                ],
              ),
              // Booking button
              const SizedBox(height: 10),
              Padding(
                padding: const EdgeInsets.all(8.0),
                child: ElevatedButton(
                  onPressed: bookSelectedRooms,
                  child: const Text('Book Selected Rooms'),
                ),
              ),
              const SizedBox(height: 40),
              const Text(
                'Leave a feedback',
                style: TextStyle(
                  fontSize: 20,
                ),
                textAlign: TextAlign.left,
              ),
              Padding(
                padding: const EdgeInsets.all(8.0),
                child: TextField(
                  controller: feedbackController,
                  decoration: const InputDecoration(
                    hintText: 'Enter your feedback',
                  ),
                ),
              ),
              Padding(
                padding: const EdgeInsets.all(8.0),
                child: ElevatedButton(
                  onPressed: submitFeedback,
                  child: const Text('Submit Feedback'),
                ),
              ),
          ],
          ),
        ],
      ),
    );
  }

  Future<void> _selectDate(BuildContext context, bool isStartDate) async {
    final DateTime? pickedDate = await showDatePicker(
      context: context,
      initialDate: isStartDate ? startDate : endDate,
      firstDate: DateTime.now(),
      lastDate: DateTime(2100),
    );
    if (pickedDate != null) {
      setState(() {
        if (isStartDate) {
          startDate = pickedDate;
        } else {
          endDate = pickedDate;
        }
      });
    }
  }
}

class Room {
  final int roomNumber;
  final String type;
  final double price;
  final bool available;
  bool isSelected;

  Room({required this.roomNumber, required this.type, required this.price, required this.available, this.isSelected = false});

  factory Room.fromJson(Map<String, dynamic> json) {
    return Room(
      roomNumber: json['roomNumber'],
      type: json['type'],
      price: json['price'].toDouble(),
      available: json['available'],
    );
  }
}