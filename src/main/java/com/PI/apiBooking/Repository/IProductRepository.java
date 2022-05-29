package com.PI.apiBooking.Repository;

import com.PI.apiBooking.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.title = ?1")
    Long countByCategory(String category);

    @Query("SELECT p FROM Product p WHERE p.category.id = ?1")
    Set<Product> findByCategoryId(Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.category.title = ?1")
    Set<Product> findByCategoryTitle(String categoryTitle);

    @Query("SELECT p FROM Product p WHERE p.city.id = ?1")
    Set<Product> findByCityId(Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.city.name = ?1")
    Set<Product> findByCityName(String categoryTitle);

}
