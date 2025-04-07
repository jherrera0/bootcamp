package reactive_backend.bootcamp.domain.model;

import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PageCustomTest {

    @Test
    void pageCustomConstructorSetsAllFields() {
        List<String> items = List.of("item1", "item2");
        PageCustom<String> pageCustom = new PageCustom<>(1, 10, 5, items);

        assertEquals(1, pageCustom.getCurrentPage());
        assertEquals(10, pageCustom.getPageSize());
        assertEquals(5, pageCustom.getTotalPages());
        assertEquals(items, pageCustom.getItems());
    }

    @Test
    void pageCustomDefaultConstructorSetsFieldsToNull() {
        PageCustom<String> pageCustom = new PageCustom<>();

        assertNull(pageCustom.getCurrentPage());
        assertNull(pageCustom.getPageSize());
        assertNull(pageCustom.getTotalPages());
        assertNull(pageCustom.getItems());
    }

    @Test
    void setCurrentPageUpdatesCurrentPage() {
        PageCustom<String> pageCustom = new PageCustom<>();
        pageCustom.setCurrentPage(2);

        assertEquals(2, pageCustom.getCurrentPage());
    }

    @Test
    void setPageSizeUpdatesPageSize() {
        PageCustom<String> pageCustom = new PageCustom<>();
        pageCustom.setPageSize(20);

        assertEquals(20, pageCustom.getPageSize());
    }

    @Test
    void setTotalPagesUpdatesTotalPages() {
        PageCustom<String> pageCustom = new PageCustom<>();
        pageCustom.setTotalPages(10);

        assertEquals(10, pageCustom.getTotalPages());
    }

    @Test
    void setItemsUpdatesItems() {
        List<String> items = List.of("item1", "item2");
        PageCustom<String> pageCustom = new PageCustom<>();
        pageCustom.setItems(items);

        assertEquals(items, pageCustom.getItems());
    }

    @Test
    void setItemsToEmptyList() {
        PageCustom<String> pageCustom = new PageCustom<>();
        pageCustom.setItems(Collections.emptyList());

        assertTrue(pageCustom.getItems().isEmpty());
    }
}