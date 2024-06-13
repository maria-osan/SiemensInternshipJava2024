import 'package:flutter/material.dart';
import 'hotel_details.dart';

class HotelList extends StatelessWidget {
  final List<dynamic> hotels;

  const HotelList({super.key, required this.hotels});

  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: ListView.builder(
        itemCount: hotels.length,
        itemBuilder: (context, index) {
          return GestureDetector(
            onTap: () {
              // Navigate to HotelDetailsPage when a hotel is tapped
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => HotelDetailsPage(hotelId: hotels[index]['id']),
                ),
              );
            },
            child: ListTile(
              title: Text(hotels[index]['name']),
              subtitle: Text('Location: ${hotels[index]['latitude']} N, ${hotels[index]['longitude']} E'),
            ),
          );
        },
      ),
    );
  }
}