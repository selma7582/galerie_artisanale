package com.example.galerie_artisanale.repository;

import com.example.galerie_artisanale.entity.Dimension;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DimensionRepository extends JpaRepository<Dimension,Long> {


    Dimension findByDescription(String description);

    //findByShapeShapeName(String shape);
}
