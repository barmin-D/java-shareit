package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByBookerOrderByStartDesc(User user, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.booker=?1 and b.start<=?2 and b.end>=?3 " +
            "order by b.id asc")
    List<Booking> findAllByBookerCurrent(User user, LocalDateTime now, LocalDateTime localDateTime, Pageable pageable);

    List<Booking> findAllByBookerAndStatusOrderByStartAsc(User user, Status status, Pageable pageable);

    @Query("select b from Booking b join fetch Item i on b.item.id=i.id " +
            "where i.owner=?1 and b.start<?2 and b.end>?3 order by b.start desc")
    List<Booking> findAllByOwnerCurrent(User user, LocalDateTime now, LocalDateTime localDateTime, Pageable pageable);

    List<Booking> findAllByItemOwnerAndEndBeforeOrderByStartDesc(User user, LocalDateTime now, Pageable pageable);

    @Query("select b from Booking b " +
            "where  b.start>CURRENT_DATE " +
            "order by b.id desc")
    List<Booking> findAllByBookerInFuture(User user, Pageable pageable);

    @Query("select b from Booking b " +
            "where b.booker=?1 and b.end<=?2 " +
            "order by b.id desc")
    List<Booking> findAllByBookerInPast(User user, LocalDateTime now, Pageable pageable);

    List<Booking> findAllByItemOwnerOrderByIdDesc(User user, Pageable pageable);

    List<Booking> findAllByItemOwnerAndStatus(User user, Status status, Pageable pageable);

    @Query("select b from Booking b join fetch Item i on b.item.id=i.id " +
            "where i.id=?1 and i.owner=?2 and b.start>?3  order by b.start asc")
    Booking findFirstByItemOwnerAsc(Integer itemId, User user, LocalDateTime now);

    @Query("select b from Booking b join fetch Item i on b.item.id=i.id " +
            "where i.id=?1 and i.owner=?2 and b.start<?3  order by b.start desc")
    Booking findFirstByItemOwnerDesc(Integer itemId, User user, LocalDateTime now);

    @Query("select b from Booking b join fetch Item i on b.item.id=i.id " +
            "where i=?1 and b.start<?2 and b.status=?3 order by b.start desc")
    Booking findFirstByItemDesc(Item item, LocalDateTime now, Status approved);

    @Query("select b from Booking b join fetch Item i on b.item.id=i.id " +
            "where i=?1 and b.start>?2 and b.status=?3 order by b.start asc")
    Booking findFirstByItemAsc(Item item, LocalDateTime now, Status approved);

    @Query("select b from Booking b join fetch Item i on b.item.id=i.id " +
            "where i=?1 and b.booker=?2 and b.end<?3 and b.status=?4 order by b.start desc")
    Booking findFirstByItemAndBookerDesc(Item item, User user, LocalDateTime now, Status approved);
}
