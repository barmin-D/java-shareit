package ru.practicum.shareit.requests.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {
    @Query("select ir from ItemRequest ir " +
            "where ir.requestor.id=?1 " +
            "order by ir.created desc")
    Collection<ItemRequest> findAllByRequestor(Integer userId);

    @Query("select ir from ItemRequest ir " +
            "where ir.requestor.id<>?1 " +
            "order by ir.created desc")
    Page<ItemRequest> findAllByRequestorIdDesc(Integer userId, Pageable pageable);
}
