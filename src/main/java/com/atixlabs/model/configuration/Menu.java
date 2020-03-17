package com.atixlabs.model.configuration;

import com.google.common.collect.Sets;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@Builder
@Entity
@Table(name = "menus")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_PARENT")
    private Menu parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Menu> items = Sets.newHashSet();

    private String name;
    private String code;
    private String icon;
    private String uri;
    private String type;

    @Column(name = "item_order")
    private Integer order;

    public boolean isMainMenu() {
        return parent == null;
    }

    public Menu() {

    }

    public Menu(Integer id, Menu parent, Collection<Menu> items, String name, String code, String icon, String uri, String type, Integer order) {
        this.id = id;
        this.parent = parent;
        this.items = items;
        this.name = name;
        this.code = code;
        this.icon = icon;
        this.uri = uri;
        this.type = type;
        this.order = order;
    }
}
