package com.example.galerie_artisanale.entity;

/*import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class SubCategory {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_SubCategory")
    private Long id;
    @Column(columnDefinition = "text")
    private String subCategoryName;

    transient
    private boolean removed ;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subCategory")
    private List<Category> categoryList;

    @Override
    public String toString() {
        return  subCategoryName ;
    }
}*/
