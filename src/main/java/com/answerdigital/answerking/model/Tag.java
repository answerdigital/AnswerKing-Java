package com.answerdigital.answerking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "tag_product",
            joinColumns = { @JoinColumn(referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(referencedColumnName = "id") }
    )
    private List<Product> products;

    public Tag(final String name, final String description, final List<Product> products) {
        this.name = name;
        this.description = description;
        this.products = products;
    }
}
