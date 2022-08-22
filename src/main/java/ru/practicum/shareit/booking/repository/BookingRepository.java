package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Collection<Booking> findAllByBookerOrderByStartDesc(User user);

    @Query("select b from Booking b" +
            " where b.booker=?1 and b.start<=?2 and b.end>=?3" +
            " order by b.id asc ")
    Collection<Booking> findAllByBookerCurrent(User user, LocalDateTime now, LocalDateTime localDateTime);

    Collection<Booking> findAllByBookerAndStatusOrderByStartAsc(User user, Status status);

    Collection<Booking> findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(User user, LocalDateTime now,
                                                                                    LocalDateTime localDateTime);

    Collection<Booking> findAllByItemOwnerAndEndBeforeOrderByStartDesc(User user, LocalDateTime now);

    @Query("select b from Booking b" +
            " where  b.start>CURRENT_DATE " +
            " order by b.id desc ")
    Collection<Booking> findAllByBookerInFuture(User user);

    @Query("select b from Booking b " +
            "where b.booker=?1 and b.end<=?2" +
            " order by b.id desc ")
    Collection<Booking> findAllByBookerInPast(User user, LocalDateTime now);

    Collection<Booking> findAllByItemOwnerOrderByIdDesc(User user);

    Collection<Booking> findAllByItemOwnerAndStatus(User user, Status status);

    Booking findFirstByItemOwnerAndStartAfterAndStatusOrderByStartAsc(User user, LocalDateTime now, Status approved);

    Booking findFirstByItemOwnerAndStartBeforeAndStatusOrderByStartDesc(User user, LocalDateTime now, Status approved);

    Booking findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(Item item, LocalDateTime now, Status approved);

    Booking findFirstByItemAndStartAfterAndStatusOrderByStartAsc(Item item, LocalDateTime now, Status approved);

    Booking findFirstByItemAndBookerAndEndIsBeforeAndStatusOrderByStartDesc(Item item, User user,
                                                                            LocalDateTime now, Status approved);
}
