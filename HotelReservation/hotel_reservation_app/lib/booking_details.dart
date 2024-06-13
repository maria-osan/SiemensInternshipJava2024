import 'package:flutter/material.dart';

import 'hotel_details.dart';

class BookingDetailsPage extends StatelessWidget {
  final List<Room> bookedRooms;
  final DateTime startDate;
  final DateTime endDate;

  const BookingDetailsPage({
    super.key,
    required this.bookedRooms,
    required this.startDate,
    required this.endDate,
  });

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Booking Details'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Text('Booking successful!'),
            Text('Start Date: ${startDate.toString()}'),
            Text('End Date: ${endDate.toString()}'),
            Column(
              children: bookedRooms.map((room) {
                return Text('Booked Room: ${room.roomNumber.toString()}');
              }).toList(),
            ),
          ],
        ),
      ),
    );
  }
}