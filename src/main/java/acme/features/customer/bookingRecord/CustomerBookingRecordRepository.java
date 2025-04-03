
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.booking.Passenger;

@Repository
public interface CustomerBookingRecordRepository extends AbstractRepository {

	@Query("SELECT b FROM Booking b WHERE b.id=:bookingId")
	Booking getBookingById(int bookingId);

	@Query("SELECT p FROM Passenger p WHERE p.customer.id=:customerId and p.isPublished=true")
	Collection<Passenger> getAllPassengersByCustomer(int customerId);

	@Query("SELECT br.passenger FROM BookingRecord br WHERE br.booking.id=:bookingId")
	Collection<Passenger> getPassengersInBooking(int bookingId);

	@Query("SELECT b FROM Booking b WHERE b.customer.id=:customerId and b.isPublished=false")
	Collection<Booking> getBookingsByCustomerId(int customerId);

	@Query("SELECT br FROM BookingRecord br WHERE br.booking.customer.id=:customerId")
	Collection<BookingRecord> getBookingRecordsByCustomerId(int customerId);

}
