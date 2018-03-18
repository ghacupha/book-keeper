package io.github.ghacupha.keeper.book;

/**
 * Container for additional details for {@link Entry}
 *
 * @author edwin.njeru
 */
public class EntryDetails {

    private final String narration;

    private String remarks;

    private String invoiceNumber;

    private String supplier;

    public EntryDetails(String narration) {
        this.narration = narration;
        this.remarks="";
        this.invoiceNumber="";
        this.supplier="";
    }

    public EntryDetails(String narration, String remarks) {
        this(narration);
        this.remarks = remarks;
        this.invoiceNumber="";
        this.supplier="";
    }

    public EntryDetails(String narration, String remarks, String invoiceNumber) {
        this(narration,remarks);
        this.invoiceNumber = invoiceNumber;
        this.supplier="";
    }

    public EntryDetails(String narration, String remarks, String invoiceNumber, String supplier) {
        this(narration,remarks,invoiceNumber);
        this.supplier = supplier;
    }

    public String getNarration() {
        return narration;
    }

    public String getRemarks() {
        return remarks;
    }

    public EntryDetails setRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public EntryDetails setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
        return this;
    }

    public String getSupplier() {
        return supplier;
    }

    public EntryDetails setSupplier(String supplier) {
        this.supplier = supplier;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntryDetails that = (EntryDetails) o;

        if (!narration.equals(that.narration)) return false;
        if (!remarks.equals(that.remarks)) return false;
        if (!invoiceNumber.equals(that.invoiceNumber)) return false;
        return supplier.equals(that.supplier);
    }

    @Override
    public int hashCode() {
        int result = narration.hashCode();
        result = 31 * result + remarks.hashCode();
        result = 31 * result + invoiceNumber.hashCode();
        result = 31 * result + supplier.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EntryDetails{");
        sb.append("narration='").append(narration).append('\'');
        sb.append(", remarks='").append(remarks).append('\'');
        sb.append(", invoiceNumber='").append(invoiceNumber).append('\'');
        sb.append(", supplier='").append(supplier).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
