package com.shafiya.fileparser.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(IpoOrder.TABLE_NAME)
public class IpoOrder implements Serializable {
    public static final String TABLE_NAME = "ipo_order";
    private static final String ID = "id";
    private static final String INVESTOR_NAME = "investor_name";
    private static final String SID = "sid";
    private static final String BOOK_PRICE = "book_price";
    private static final String BOOK_QTY = "book_qty";
    private static final String BOOK_TOTAL = "book_total";
    private static final String OFFERING_PRICE = "offering_price";
    private static final String OFFERING_QTY = "offering_qty";
    private static final String OFFERING_TOTAL = "offering_total";
    private static final String ALLOTMENT_QTY = "allotment_qty";
    private static final String ALLOTMENT_TOTAL = "allotment_total";

    @Id
    private Long id;

    @Column(INVESTOR_NAME)
    private String investorName;

    @Column(SID)
    private String sid;

    @Column(BOOK_PRICE)
    private BigDecimal bookPrice;

    @Column(BOOK_QTY)
    private BigDecimal bookQty;

    @Column(BOOK_TOTAL)
    private BigDecimal bookTotal;

    @Column(OFFERING_PRICE)
    private BigDecimal offeringPrice;

    @Column(OFFERING_QTY)
    private BigDecimal offeringQty;

    @Column(OFFERING_TOTAL)
    private BigDecimal offeringTotal;

    @Column(ALLOTMENT_QTY)
    private BigDecimal allocatedQty;

    @Column(ALLOTMENT_TOTAL)
    private BigDecimal allocatedTotal;

    public Long getId() {
        return id;
    }

    public String getInvestorName() {
        return investorName;
    }

    public String getSid() {
        return sid;
    }

    public BigDecimal getBookPrice() {
        return bookPrice;
    }

    public BigDecimal getBookQty() {
        return bookQty;
    }

    public BigDecimal getBookTotal() {
        return bookTotal;
    }

    public BigDecimal getOfferingPrice() {
        return offeringPrice;
    }

    public BigDecimal getOfferingQty() {
        return offeringQty;
    }

    public BigDecimal getOfferingTotal() {
        return offeringTotal;
    }

    public BigDecimal getAllocatedQty() {
        return allocatedQty;
    }

    public BigDecimal getAllocatedTotal() {
        return allocatedTotal;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setInvestorName(String investorName) {
        this.investorName = investorName;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setBookPrice(BigDecimal bookPrice) {
        this.bookPrice = bookPrice;
    }

    public void setBookQty(BigDecimal bookQty) {
        this.bookQty = bookQty;
    }

    public void setBookTotal(BigDecimal bookTotal) {
        this.bookTotal = bookTotal;
    }

    public void setOfferingPrice(BigDecimal offeringPrice) {
        this.offeringPrice = offeringPrice;
    }

    public void setOfferingQty(BigDecimal offeringQty) {
        this.offeringQty = offeringQty;
    }

    public void setOfferingTotal(BigDecimal offeringTotal) {
        this.offeringTotal = offeringTotal;
    }

    public void setAllocatedQty(BigDecimal allocatedQty) {
        this.allocatedQty = allocatedQty;
    }

    public void setAllocatedTotal(BigDecimal allocatedTotal) {
        this.allocatedTotal = allocatedTotal;
    }
}
