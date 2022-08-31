package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDbRepository extends JpaRepository<Item, Integer> {
    @Query(value = " select i from Item i " +
            "where i.available=true and upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or i.available=true and upper(i.description) like upper(concat('%', ?1, '%'))" +
            "order by i.id asc")
    List<Item> search(String text);

    @Query(" select i from Item i where i.request.id=?1 order by i.id")
    List<Item> findAllByItemRequest(Integer requestId);
}
