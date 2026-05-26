package com._hills.backend.dto;

import com._hills.backend.entity.CustomerOrder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CustomerOrderResponse {

    private String id;
    private String customerName;
    private String customerEmail;
    private BigDecimal total;
    private String status;
    private String vendorName;
    private LocalDate deliveryDate;
    private LocalDateTime assignedAt;
    private LocalDateTime placedAt;
    private List<ItemDto> items;

    @Data
    public static class ItemDto {
        private String name;
        private String brand;
        private Integer qty;
        private BigDecimal price;
    }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.charAt(0) + s.substring(1).toLowerCase().replace('_', ' ');
    }

    public static CustomerOrderResponse from(CustomerOrder o) {
        CustomerOrderResponse r = new CustomerOrderResponse();
        r.setId(o.getId());
        r.setCustomerName(o.getCustomer().getName());
        r.setCustomerEmail(o.getCustomer().getEmail());
        r.setTotal(o.getTotal());
        r.setStatus(capitalize(o.getStatus().name()));
        r.setVendorName(o.getVendorName());
        r.setDeliveryDate(o.getDeliveryDate());
        r.setAssignedAt(o.getAssignedAt());
        r.setPlacedAt(o.getPlacedAt());
        r.setItems(o.getItems().stream().map(i -> {
            ItemDto item = new ItemDto();
            item.setName(i.getProductName());
            item.setBrand(i.getBrand());
            item.setQty(i.getQty());
            item.setPrice(i.getUnitPrice());
            return item;
        }).toList());
        return r;
    }
}
