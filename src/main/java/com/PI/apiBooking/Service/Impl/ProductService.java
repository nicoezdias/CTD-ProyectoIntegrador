package com.PI.apiBooking.Service.Impl;

import com.PI.apiBooking.Exceptions.ResourceNotFoundException;
import com.PI.apiBooking.Model.DTO.ProductDto;
import com.PI.apiBooking.Model.DTO.Product_CardDto;
import com.PI.apiBooking.Model.DTO.Product_CompleteDto;
import com.PI.apiBooking.Model.Product;
import com.PI.apiBooking.Repository.IProductRepository;
import com.PI.apiBooking.Service.Interfaces.IProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductService implements IProductService {
    protected final static Logger logger = Logger.getLogger(ProductService.class);

    @Autowired
    IProductRepository productRepository;

    @Autowired
    ImageService imageService;

    @Autowired
    Product_FeatureService product_featureService;

    @Autowired
    Product_PolicyService product_policyService;

    @Autowired
    ObjectMapper mapper;

    @Override
    public Set<ProductDto> findAll() {
        Set<ProductDto> productsDto = new HashSet<>();
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            productsDto.add(mapper.convertValue(product, ProductDto.class));
        }
        logger.info("La busqueda fue exitosa: "+ productsDto);
        return productsDto;
    }

    @Override
    public Set<Product_CardDto> findAllCard() {
        Set<Product_CardDto> products_cardDto = new HashSet<>();
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            Product_CardDto product_cardDto = mapper.convertValue(product, Product_CardDto.class);
            product_cardDto.setCategoryName(product.getCategory().getTitle());
            product_cardDto.setAvgRanting(productRepository.averageScoreByProduct(product_cardDto.getId()));
            product_cardDto.setImageProfile(imageService.findProfileImageByProductId(product_cardDto.getId()));
            products_cardDto.add(product_cardDto);
        }
        logger.info("La busqueda fue exitosa: "+ products_cardDto);
        return products_cardDto;
    }

    @Override
    public ProductDto findById(Long id) throws ResourceNotFoundException {
        Product product = checkId(id);
        ProductDto productDto = mapper.convertValue(product, ProductDto.class);
        logger.info("La busqueda fue exitosa: id("+id+")");
        return productDto;
    }

    @Override
    public Product_CompleteDto findByIdComplete(Long id) throws ResourceNotFoundException {
        Product product = checkId(id);
        Product_CompleteDto product_completeDto = mapper.convertValue(product, Product_CompleteDto.class);
        product_completeDto.setCategoryName(product.getCategory().getTitle());
        product_completeDto.setAvgRanting(productRepository.averageScoreByProduct(product_completeDto.getId()));
        product_completeDto.setImagesProduct(imageService.findImagesByProductId(product_completeDto.getId()));
        product_completeDto.setFeaturesProduct(product_featureService.findFeaturesByProductId(product_completeDto.getId()));
        product_completeDto.setPoliciesProduct(product_policyService.findPolicyByProductId(product_completeDto.getId()));
        return product_completeDto;
    }

    @Override
    public Set<ProductDto> findByCategoryId(Long categoryId){
        Set<ProductDto> productsDto = new HashSet<>();
        Set<Product> products = productRepository.findByCategoryId(categoryId);
        for (Product product : products) {
            productsDto.add(mapper.convertValue(product, ProductDto.class));
        }
        logger.info("La busqueda fue exitosa: "+ productsDto);
        return productsDto;
    }

    @Override
    public Set<ProductDto> findByCityId(Long cityId){
        Set<ProductDto> productsDto = new HashSet<>();
        Set<Product> products = productRepository.findByCityId(cityId);
        for (Product product : products) {
            productsDto.add(mapper.convertValue(product, ProductDto.class));
        }
        logger.info("La busqueda fue exitosa: "+ productsDto);
        return productsDto;
    }

    @Override
    public ProductDto save(ProductDto productDto) {
        Product product = mapper.convertValue(productDto, Product.class);
        productRepository.save(product);

        if (productDto.getId() == null){
            productDto.setId(product.getId());
            logger.info("Producto registrado correctamente: "+ productDto);
        }else{
            logger.info("Producto actualizado correctamente: "+ productDto);
        }
        return productDto;
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        checkId(id);
        productRepository.deleteById(id);
        logger.info("Se elimino el producto correctamente: id("+id+")");
    }

    @Override
    public Product checkId(Long id) throws ResourceNotFoundException{
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new ResourceNotFoundException(msjError + id);
        }
        return product.get();
    }
}
