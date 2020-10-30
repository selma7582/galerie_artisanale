package com.example.galerie_artisanale.repository;

import com.example.galerie_artisanale.entity.Category;
import com.example.galerie_artisanale.entity.Ordered;
import com.example.galerie_artisanale.entity.OrderedStatus;
import com.example.galerie_artisanale.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Ordered,Long> {
    List<Ordered> findAll();
    List<Ordered> findByUser(User user);

    /**
     * Get the unique invalid Ordered which is the current Shopping cart that is waiting for the user validation
     * @param user
     * @return
     */

    List<Ordered> findByUserAndStatus(User user, OrderedStatus status);
    //List<Ordered> findByUserIdAndStatus(Long id , OrderedStatus status);

}
