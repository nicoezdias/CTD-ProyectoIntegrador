package com.PI.apiBooking.Service.Interfaces;

import com.PI.apiBooking.Model.DTO.ProductDto;
import com.PI.apiBooking.Model.Product;
import com.PI.apiBooking.Service.ICheckId;
import com.PI.apiBooking.Service.IServices;
import java.util.Set;

public interface IProductServices extends IServices<ProductDto>, ICheckId<Product> {

    Long countByCategory(String c);
    Set<ProductDto> findByCategoryId(Long categoryId);
    Set<ProductDto> findByCategoryTitle(String categoryTitle);
    Set<ProductDto> findByCityId(Long cityId);
    Set<ProductDto> findByCityName(String cityTitle);
}
