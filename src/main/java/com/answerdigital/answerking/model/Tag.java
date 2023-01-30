package com.answerdigital.answerking.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
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

    @ManyToMany(cascade = CascadeType.ALL)
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
